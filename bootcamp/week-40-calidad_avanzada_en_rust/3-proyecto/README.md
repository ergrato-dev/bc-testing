# Proyecto Semanal - Calidad avanzada en la suite Rust del dominio

> **🎯 ÚNICO ENTREGABLE**: este proyecto es el único entregable obligatorio de la semana.

## Objetivo

Pasar las herramientas de la semana por la suite Rust de tu dominio, la de la [semana 39](../../week-39-async_apis_y_bd_en_rust/3-proyecto/README.md), y documentar lo que encuentran en `calidad.md`, como hiciste en Go en la [semana 35](../../week-35-concurrencia_y_calidad_en_go/3-proyecto/README.md):

- propiedades con proptest, una de ellas contra PostgreSQL, hasta encontrar un fallo;
- un benchmark con criterion, con una línea base y una comparación;
- la cobertura con cargo-llvm-cov;
- los mutantes que sobreviven con cargo-mutants, analizados uno a uno;
- clippy con `-D warnings` y la suite con nextest.

## Dónde va

En la carpeta `rust/` de tu repositorio `qa-<nombre>-<apellido>` ([proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)), con el `compose.yaml` de la raíz.

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

## Paso a paso

### Paso 1: la rama y el starter

1. Crea la rama de la semana:

   ```bash
   cd qa-<nombre>-<apellido>
   git switch main && git pull
   git switch -c week-40
   ```

2. Copia el starter:

   ```bash
   cp -rn <ruta-al-bootcamp>/bootcamp/week-40-calidad_avanzada_en_rust/3-proyecto/starter/. .
   ```

   | Archivo | Qué es |
   |---|---|
   | `rust/tests/properties.rs` | Un test con `#[ignore = "TODO: paso 3"]` |
   | `rust/tests/pg_properties.rs` | Un test con `#[ignore = "TODO: paso 4"]` |
   | `rust/benches/hot_path.rs` | Solo un comentario con el TODO del paso 5 |
   | `rust/calidad.md` | El informe, con una sección por herramienta y un TODO en cada una |

3. Arranca PostgreSQL y ejecuta la suite:

   ```bash
   docker compose up -d --wait
   cd rust
   cargo test
   # ...
   #      Running tests/pg_properties.rs (...)
   # test valid_entities_round_trip_through_postgres ... ignored, TODO: paso 4
   # ...
   #      Running tests/properties.rs (...)
   # test domain_properties ... ignored, TODO: paso 3
   # ...
   ```

### Paso 2: las herramientas

1. Añade las dependencias de los tests ([teoría 01](../1-teoria/01-propiedades-con-proptest.md#instalar-proptest) y [teoría 03](../1-teoria/03-benchmarks-con-criterion.md#instalar-criterion)):

   ```bash
   cargo add --dev proptest@=1.12.0
   cargo add --dev criterion@=0.8.2 --features html_reports
   ```

2. Añade `llvm-tools-preview` a `rust-toolchain.toml`, para que cargo-llvm-cov tenga sus herramientas con la versión de Rust del proyecto:

   ```toml
   [toolchain]
   channel = "1.100.0"
   components = ["rustfmt", "clippy", "llvm-tools-preview"]
   ```

   La siguiente orden de cargo en la carpeta instala el componente.

3. Instala las herramientas de cargo, si no las tienes del [lab 04](../2-practicas/lab-04-cobertura-mutantes-y-clippy/README.md#preparación):

   ```bash
   cargo install --locked cargo-llvm-cov@0.9.1 cargo-nextest@0.9.152 cargo-mutants@27.1.0
   ```

4. Añade `mutants.out*` al `.gitignore` de la raíz del repositorio. cargo-mutants deja ahí sus resultados, y no son código.

El proyecto no usa cargo-fuzz: necesita nightly y tarda minutos en encontrar algo. Las propiedades de proptest hacen un papel parecido dentro de `cargo test`.

### Paso 3: las propiedades del dominio

Sustituye el contenido de `tests/properties.rs` por un bloque `proptest!` con al menos cinco propiedades. Busca:

| Tipo de propiedad | Ejemplo del museo |
|---|---|
| Todo lo bien formado se acepta | `accepts_every_well_formed_code(code in "[A-Z]{3}-[0-9]{3}")` |
| Todo lo que pasa de un límite se rechaza | `rejects_names_over_the_limit(extra in 1usize..200)`, con nombres de `MAX_NAME_LENGTH + extra` caracteres |
| Un cálculo respeta su tope o su mínimo | `late_fee(days) <= MAX_LATE_FEE` con cualquier `u32` |
| Un cálculo crece con su entrada | Más días de retraso nunca cuestan menos |
| Ida y vuelta | `Status::parse(s.as_str()) == Some(s)`, con `s in prop_oneof![Just(...), ...]` |

Usa `any::<u32>()` (o el tipo de tus números) en los cálculos: los extremos, como `u32::MAX`, son los que los tests de ejemplo no prueban. Ejecuta:

```bash
cargo test --test properties
# ...
# test result: ok. 7 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.02s
```

Si una propiedad falla, decide si el error está en el código o en la propiedad, corrígelo y anótalo en `calidad.md`.

### Paso 4: una propiedad contra PostgreSQL

Los tests de la semana 39 guardan en PostgreSQL entidades escritas a mano. Esta propiedad guarda las que genera proptest: **todo lo que tu validación acepta, la base de datos lo guarda y lo devuelve igual**.

proptest llama a la propiedad de forma síncrona, y el almacén es async. El test crea un runtime de tokio y entra en él con `block_on` en cada caso. El runtime, el pool y las migraciones se preparan una sola vez, con `LazyLock` de la biblioteca estándar. El ejemplo del museo:

```rust
use museum::{PgStore, Piece, PieceStore, Status, validate_piece};
use proptest::prelude::*;
use sqlx::PgPool;
use std::sync::LazyLock;
use tokio::runtime::Runtime;

static RUNTIME: LazyLock<Runtime> = LazyLock::new(|| Runtime::new().unwrap());

static STORE: LazyLock<PgStore> = LazyLock::new(|| {
    RUNTIME.block_on(async {
        let pool = PgPool::connect(env!("DATABASE_URL")).await.unwrap();
        sqlx::migrate!().run(&pool).await.unwrap();
        PgStore { pool }
    })
});

async fn round_trip(store: &PgStore, piece: &Piece) -> Result<Option<Piece>, String> {
    store.save(piece).await?;
    let got = store.find_by_code(&piece.code).await;
    sqlx::query("DELETE FROM pieces WHERE code = $1")
        .bind(&piece.code)
        .execute(&store.pool)
        .await
        .map_err(|e| e.to_string())?;
    got
}

proptest! {
    #![proptest_config(ProptestConfig::with_cases(64))]

    #[test]
    fn postgres_stores_every_valid_piece(
        code in "[A-Z]{3}-[0-9]{3}",
        name in any::<String>(),
        declared_value in any::<u32>(),
    ) {
        prop_assume!(validate_piece(&code, &name, declared_value).is_ok());
        let piece = Piece { code, name, declared_value, status: Status::Available };

        let store = &*STORE; // fuera de block_on: STORE usa el runtime al crearse
        let got = RUNTIME.block_on(round_trip(store, &piece));

        prop_assert_eq!(got, Ok(Some(piece)));
    }
}
```

| Pieza | Qué / Para qué / Impacto |
|---|---|
| `LazyLock` | Crea el valor la primera vez que se usa y lo comparte después. Sin él, cada caso abriría un pool nuevo |
| `sqlx::migrate!().run(&pool)` | Aplica `migrations/` a la base `museum` de `compose.yaml`. `#[sqlx::test]` lo hacía por ti en una base nueva; aquí la base es la de siempre, y `migrate!` no repite las migraciones ya aplicadas |
| `DELETE` al final de cada caso | La base es compartida: cada caso deja la tabla como la encontró |
| `let store = &*STORE;` antes de `block_on` | Crear `STORE` llama a `block_on`. Si la primera vez fuera dentro de otro `block_on`, tokio entraría en pánico con `Cannot start a runtime from within a runtime` |
| `with_cases(64)` | Cada caso habla con PostgreSQL: 64 casos tardan unas décimas de segundo |
| `prop_assume!(validate_piece(...).is_ok())` | Genera piezas de cualquier forma y descarta las que la validación rechaza. Si la estrategia generara solo piezas que ya sabes válidas, no encontraría nada |

Adapta los nombres a tu dominio y ejecuta:

```bash
cargo test --test pg_properties
# proptest: FileFailurePersistence::SourceParallel set, but failed to find lib.rs or main.rs
# proptest: FileFailurePersistence::SourceParallel set, but failed to find lib.rs or main.rs
# proptest: Saving this and future failures in .../rust/tests/pg_properties.proptest-regressions
# ...
# thread 'postgres_stores_every_valid_piece' (635138) panicked at tests/pg_properties.rs:34:1:
# Test failed: assertion failed: `(left == right)`
#   left: `Err("number too large to fit in target type")`,
#  right: `Ok(Some(Piece { code: "AAA-000", name: "\u{16af0}", declared_value: 2147483648, status: Available }))` at tests/pg_properties.rs:49.
# minimal failing input: code = "AAA-000", name = "\u{16af0}", declared_value = 2147483648
# 	successes: 2
```

En el museo, `declared_value` es un `u32` y la columna es un `int` de PostgreSQL, que llega hasta `i32::MAX`. `validate_piece` aceptaba valores que la base de datos no puede guardar, y proptest redujo el valor exactamente a `i32::MAX + 1`. Busca el desajuste equivalente en tu dominio: tipos numéricos más grandes que su columna, textos más largos que un `varchar(n)`, valores que un `CHECK` rechaza.

El aviso `failed to find lib.rs or main.rs` no es un error: los tests de `tests/` no están junto a un `lib.rs`, y proptest guarda la semilla al lado del archivo, en `tests/pg_properties.proptest-regressions` ([teoría 01](../1-teoria/01-propiedades-con-proptest.md#el-archivo-de-regresiones)).

1. Corrige la validación. En el museo, una constante y un error nuevo en `src/piece.rs`:

   ```rust
   pub const MAX_DECLARED_VALUE: u32 = i32::MAX as u32;
   // ...
   if declared_value > MAX_DECLARED_VALUE {
       return Err(PieceError::ValueTooHigh);
   }
   ```

2. Añade dos tests de ejemplo en la frontera: el máximo se acepta y uno más se rechaza. La propiedad prueba valores al azar y casi nunca cae justo en la frontera.
3. Ejecuta `cargo test`: todo pasa. Sube `tests/pg_properties.proptest-regressions` al repositorio: proptest probará esa semilla primero en cada ejecución.
4. Anota en `calidad.md` la salida, la causa y el arreglo.

Si tu propiedad no encuentra nada, revisa la estrategia: ¿genera valores fuera de lo que tú esperarías? Si después de eso sigue pasando, ejecútala con `PROPTEST_CASES=1000` y anota en `calidad.md` qué cubre y por qué no encontró nada. El requisito de la semilla guardada pide al menos un fallo encontrado por proptest en algún momento, en esta propiedad o en las del Paso 3.

### Paso 5: un benchmark con criterion

Elige la función de tu dominio que recibe datos del cliente y cuyo coste depende de su tamaño: validar un texto, leer una lista, calcular sobre varios elementos. En el museo, `validate_piece` con nombres de 10, 120 y 10 000 caracteres: un nombre enorme no debería costar más que uno que ya se pasa del límite.

1. Escribe `benches/hot_path.rs` con un grupo de tamaños ([teoría 03](../1-teoria/03-benchmarks-con-criterion.md#grupos-cómo-crece-el-coste)):

   ```rust
   use criterion::{BenchmarkId, Criterion, criterion_group, criterion_main};
   use museum::validate_piece;
   use std::hint::black_box;

   fn validate(c: &mut Criterion) {
       let mut group = c.benchmark_group("validate_piece");
       for len in [10, 120, 10_000] {
           let name = "á".repeat(len);
           group.bench_with_input(BenchmarkId::from_parameter(len), &name, |b, name| {
               b.iter(|| validate_piece(black_box("MUS-001"), black_box(name), black_box(5000)))
           });
       }
       group.finish();
   }

   criterion_group!(benches, validate);
   criterion_main!(benches);
   ```

2. Regístralo en `Cargo.toml`:

   ```toml
   [[bench]]
   name = "hot_path"
   harness = false
   ```

3. Guarda la línea base:

   ```bash
   cargo bench --bench hot_path -- --save-baseline before
   # validate_piece/10       time:   [10.981 ns 11.121 ns 11.269 ns]
   # validate_piece/120      time:   [15.484 ns 15.828 ns 16.175 ns]
   # validate_piece/10000    time:   [575.07 ns 585.55 ns 596.61 ns]
   ```

   En el museo, `name.chars().count()` recorre el nombre entero: 586 ns con 10 000 caracteres, para acabar rechazándolo.

4. Optimiza y compara con `cargo bench --bench hot_path -- --baseline before`. Mide los tres tamaños: en el museo, el primer intento (`chars().nth(120)`) mejoró el nombre enorme un 91 % y empeoró el normal un 225 %. El segundo, que mira primero los bytes, dejó los normales igual y el enorme en 6 ns:

   ```text
   validate_piece/120      time:   [15.228 ns 15.589 ns 16.042 ns]
                           change: [−5.6240% −2.7278% +0.0236%] (p = 0.06 > 0.05)
                           No change in performance detected.
   validate_piece/10000    time:   [5.9522 ns 6.0681 ns 6.1974 ns]
                           change: [−98.992% −98.972% −98.948%] (p = 0.00 < 0.05)
                           Performance has improved.
   ```

5. Ejecuta `cargo test` después de optimizar: los tests y las propiedades protegen el comportamiento.
6. Anota en `calidad.md` qué mides, la salida de la línea base y la de la comparación. Si tu primer intento empeoró algo, anótalo también.

### Paso 6: la cobertura

1. Con PostgreSQL arrancado, ejecuta:

   ```bash
   cargo llvm-cov --summary-only
   # ...
   # TOTAL   ...   343   7   97.96%   ...
   cargo llvm-cov report --show-missing-lines
   # ...
   # Uncovered Lines:
   # .../rust/src/api.rs: 47
   # .../rust/src/pg.rs: 21
   ```

   La tabla real es ancha; la cifra que importa es la columna `Lines`, la penúltima con `%`. `report` no vuelve a ejecutar los tests: lee la última medición.

2. Mira cada línea sin ejecutar y decide: ¿falta un test, o es un camino que no merece uno (un `map_err` que solo se dispara si la base de datos se cae)? Anótalo en `calidad.md`, con la tabla por archivo.

La semana 41 exige un 85 %; esta semana basta con un 80 %.

### Paso 7: los mutantes

1. Ejecuta cargo-mutants sobre todo el crate:

   ```bash
   cargo mutants
   # Found 66 mutants to test
   # ok       Unmutated baseline in 29s build + 2s test
   #  INFO Auto-set test timeout to 20s
   # MISSED   src/piece.rs:36:19: replace > with == in validate_piece in 1s build + 3s test
   # MISSED   src/piece.rs:36:19: replace > with >= in validate_piece in 1s build + 2s test
   # MISSED   src/piece.rs:42:23: replace > with >= in validate_piece in 1s build + 2s test
   # 66 mutants tested in 4m: 3 missed, 51 caught, 12 unviable
   ```

   Cada mutante ejecuta la suite completa, con PostgreSQL y wiremock: cuenta con varios minutos. Para iterar sobre un archivo, `cargo mutants -f src/piece.rs`.

2. Para cada `MISSED`, mira el cambio (`mutants.out/diff/`) y decide:

   | Si... | Entonces |
   |---|---|
   | Hay una entrada con la que el mutante se comporta distinto | Escribe el test con esa entrada. En el museo, un nombre de 120 caracteres de 4 bytes ocupa justo 480 bytes, la frontera del atajo del Paso 5 |
   | El mutante hace lo mismo que el original con cualquier entrada | Es equivalente ([teoría 04](../1-teoria/04-cobertura-nextest-mutantes-y-clippy.md#mutantes-equivalentes)). Explica en `calidad.md` por qué, o reescribe el código para que no exista |

3. Vuelve a ejecutar `cargo mutants` hasta que no quede ningún `MISSED` sin analizar:

   ```text
   66 mutants tested in 2m: 54 caught, 12 unviable
   ```

   Los `unviable` no compilan (por ejemplo, devolver `Default::default()` de un tipo que no lo implementa) y no cuentan.

4. Anota en `calidad.md` la primera ejecución, una fila por superviviente (por qué sobrevivía y qué lo mata) y la ejecución final.

### Paso 8: clippy, nextest, verificar y entregar

1. Ejecuta clippy como la integración continua:

   ```bash
   cargo clippy --all-targets -- -D warnings
   ```

   Si avisa, corrígelo (`cargo clippy --fix --allow-dirty` aplica las sugerencias seguras) y vuelve a pasar los tests.

2. Ejecuta la suite con nextest, y los doctests aparte:

   ```bash
   cargo nextest run
   # ...
   #      Summary [   1.304s] 72 tests run: 72 passed, 0 skipped
   cargo test --doc
   # ...
   # test result: ok. 3 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
   ```

3. Completa la última sección de `calidad.md` y comprueba que no queda ningún TODO.
4. Comprueba los requisitos desde la carpeta del bootcamp:

   ```bash
   PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 40
   ```

   Tarda un par de minutos: compila la suite, la ejecuta, ejecuta clippy, los benchmarks en modo test y la cobertura.

5. Haz commit, push y abre el pull request de `week-40` a `main`.

## ✅ Requisitos verificables

Los comprueba `./scripts/verificar-semana.sh 40` ([checks.json](../checks.json)):

| Requisito | Comprobación |
|---|---|
| `cargo test` pasa con el PostgreSQL de `compose.yaml` arrancado (necesita Docker) | `docker compose up -d --wait` y `CI=true cargo test` |
| `cargo test` compila sin warnings, tampoco de dependencias sin usar | `cargo test --no-run` sin líneas `warning` |
| `cargo clippy --all-targets -- -D warnings` no da ningún aviso | El código de salida |
| No queda ningún `#[ignore]` en los tests | `grep` en `src/` y `tests/` |
| Al menos 6 tests en archivos con `proptest!` | `grep` en `src/` y `tests/` |
| Una propiedad de proptest guarda y lee contra PostgreSQL | Un archivo de `tests/` con `proptest!` y `PgPool` o `DATABASE_URL` |
| La semilla del fallo que encontró proptest está guardada | `proptest-regressions/*.txt` o `tests/*.proptest-regressions` |
| Hay un benchmark de criterion con `harness = false` y se ejecuta en modo test | `Cargo.toml`, `criterion_main!` en `benches/` y `cargo test --benches` |
| `llvm-tools-preview` está en `rust-toolchain.toml` y la cobertura de líneas llega al 80 % | `cargo llvm-cov --fail-under-lines 80` |
| `mutants.out` no se sube al repositorio | `.gitignore` de la raíz o de `rust/` |
| `calidad.md` documenta el fallo de proptest, la comparación de criterion, la cobertura y los mutantes, sin TODO | `minimal failing input`, `change:`, `llvm-cov` y `mutants tested` en `calidad.md` |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - Cobertura, mutantes y clippy](../2-practicas/lab-04-cobertura-mutantes-y-clippy/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 40](../README.md)
