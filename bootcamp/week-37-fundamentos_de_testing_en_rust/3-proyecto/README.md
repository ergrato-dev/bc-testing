# Proyecto Semanal - Las reglas del dominio en Rust

> **🎯 ÚNICO ENTREGABLE**: este proyecto es el único entregable obligatorio de la semana.

## Objetivo

Empezar la carpeta `rust/` del proyecto hilo conductor: portar a Rust las reglas de tu dominio que portaste a Go en la [semana 32](../../week-32-fundamentos_de_testing_en_go/3-proyecto/README.md) (validaciones y cálculos) y probarlas con los tres tipos de test de esta semana:

- unitarios en `mod tests`, con errores esperados y al menos un test que devuelve `Result`;
- tests de integración en `tests/` con `pretty_assertions`;
- doctests en la API pública;
- una comparativa de la suite Go y la suite Rust.

## Dónde va

En tu repositorio `qa-<nombre>-<apellido>` ([proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)) empieza esta semana la carpeta `rust/`, que crecerá hasta la semana 41 con traits, una API con axum, PostgreSQL y fuzzing.

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

## Paso a paso

### Paso 1: el paquete

1. Crea la rama de la semana:

   ```bash
   cd qa-<nombre>-<apellido>
   git switch main && git pull
   git switch -c week-37
   ```

2. Copia el starter:

   ```bash
   cp -rn <ruta-al-bootcamp>/bootcamp/week-37-fundamentos_de_testing_en_rust/3-proyecto/starter/. .
   ```

   Con `-n`, `cp` no sobrescribe nada que ya exista. El starter trae:

   | Archivo | Qué es |
   |---|---|
   | `rust/Cargo.toml` | El paquete `domain`, con `pretty_assertions = "=1.4.1"` en `[dev-dependencies]` |
   | `rust/rust-toolchain.toml` | La versión de Rust del proyecto |
   | `rust/src/lib.rs` | La librería vacía, con un TODO, y dos tests con `#[ignore = "TODO: ..."]` |
   | `rust/comparativa.md` | La plantilla de la comparativa |

3. Lee `rust/rust-toolchain.toml`:

   ```toml
   [toolchain]
   channel = "1.100.0"
   components = ["rustfmt", "clippy"]
   ```

   **Qué:** fija la versión de Rust de la carpeta `rust/`. **Para qué:** que tú, tus compañeros y el CI compilen con el mismo compilador. **Impacto:** la primera vez que ejecutes `cargo` dentro de `rust/`, `rustup` instala esa versión si no la tienes:

   ```bash
   cd rust
   cargo test
   # info: syncing channel updates for 1.100.0-x86_64-unknown-linux-gnu
   # info: latest update on 2026-11-12 for version 1.100.0 (0ef1bc386 2026-11-09)
   # info: downloading 5 components
   # warn: the missing active toolchain `1.100.0-x86_64-unknown-linux-gnu` has been auto-installed
   # ...
   ```

### Paso 2: el nombre del paquete

En `rust/Cargo.toml`, cambia `name = "domain"` por el nombre de tu dominio en inglés, en minúsculas y con `_` en lugar de guiones. Es el nombre con el que `tests/` y los doctests importan tu código: `use museum::...`. Por ejemplo, para un museo:

```bash
sed -i 's/^name = "domain"$/name = "museum"/' Cargo.toml
cargo test
# ...
# running 2 tests
# test tests::calculation ... ignored, TODO: tests de un cálculo del dominio (paso 4)
# test tests::validate ... ignored, TODO: tests de la validación de tu entidad (paso 3)
#
# test result: ok. 0 passed; 0 failed; 2 ignored; 0 measured; 0 filtered out; finished in 0.00s
# ...
```

Los dos tests salen como ignorados hasta que los sustituyas. `cargo` crea `Cargo.lock` y la carpeta `target/`: sube `Cargo.lock` al repositorio y añade `rust/target/` a tu `.gitignore`.

### Paso 3: la validación

1. Abre tu validación de Go de la semana 32: `ValidatePiece` o el nombre que le diste.
2. Pórtala a una función que devuelve `Result<(), TuError>`. Cada motivo de rechazo es una variante de un `enum`:

   ```rust
   #[derive(Debug, PartialEq)]
   pub enum PieceError {
       InvalidCode,
       NameRequired,
       NameTooLong,
       ZeroValue,
   }

   pub fn validate_piece(code: &str, name: &str, declared_value: u32) -> Result<(), PieceError>
   ```

   `#[derive(Debug, PartialEq)]` es lo que deja comparar el error con `assert_eq!` ([teoría 02](../1-teoria/02-panics-result-y-asserts.md#un-err-esperado)). Elige tipos sin signo (`u32`) para lo que no puede ser negativo: así un valor negativo no compila y no necesita test.

3. Sustituye el test `validate` por un `#[test]` por caso: el caso válido, cada motivo de rechazo y las fronteras (la longitud máxima exacta y una más, el valor 0 y el 1). Un test por caso da a cada uno su nombre en la salida, como un subtest de Go.
4. **Añade un nombre con tildes en la longitud máxima.** En Rust, `str::len()` cuenta bytes, como `len()` en Go. Si la validación usa `name.len()`, este test falla:

   ```bash
   cargo test bytes
   # ---- piece::tests::counts_characters_not_bytes stdout ----
   #
   # thread 'piece::tests::counts_characters_not_bytes' (127270) panicked at src/piece.rs:103:9:
   # assertion failed: `(left == right)`
   #
   # Diff < left / right > :
   # <Err(
   # <    NameTooLong,
   # >Ok(
   # >    (),
   #  )
   ```

   Cuenta caracteres con `name.chars().count()`.

5. Si separas una comprobación en una función privada, como la forma del código, pruébala en `mod tests` con un bucle sobre un array de casos. El mensaje del `assert_eq!` tiene que decir qué caso falla, porque el bucle se para en el primero:

   ```rust
   for (code, want) in cases {
       assert_eq!(is_code(code), want, "is_code({code:?})");
   }
   ```

   ```text
   assertion failed: `(left == right)`: is_code("MUS_001")
   ```

   `{code:?}` imprime el valor con comillas. Si dos casos fallan, solo verás el primero: por eso los casos con nombre van en tests separados.

Referencia: [lab 01](../2-practicas/lab-01-primer-test-con-cargo-test/README.md), [lab 02](../2-practicas/lab-02-panics-result-y-diffs/README.md) y [lab 04](../2-practicas/lab-04-integracion-y-doctests/README.md).

### Paso 4: los cálculos

1. Porta los cálculos que no dependen de un repositorio ni de la hora. En el museo son `late_fee`, `damage_fee` y `loan_premium`. Si tu dominio tiene un cálculo con fechas, déjalo en Go: la librería estándar de Rust no tiene fechas de calendario.
2. Sustituye el test `calculation` por tus tests de cálculos: el caso normal, las fronteras de cada tope o mínimo, y el error si lo hay (`assert_eq!(loan_premium(5000, 0), Err(ZeroDays))`).
3. **Prueba el valor máximo de cada entrada numérica** (`u32::MAX`). Una multiplicación que en Go con `int` de 64 bits no desbordaba puede hacerlo con `u32`: en modo debug, el test entra en pánico con `attempt to multiply with overflow`. Arréglalo con `saturating_mul` o calculando en `u64` ([lab 01](../2-practicas/lab-01-primer-test-con-cargo-test/README.md#paso-4-el-caso-que-el-tipo-no-elimina-paso-4)).
4. Al menos un test devuelve `Result` y usa `?`:

   ```rust
   #[test]
   fn premium_per_mille_per_day() -> Result<(), ZeroDays> {
       assert_eq!(loan_premium(200_000, 30)?, 6000);
       Ok(())
   }
   ```

5. Añade `use pretty_assertions::assert_eq;` debajo de `use super::*;` en cada `mod tests`.

Entre la validación y los cálculos, la suite debe sumar **al menos 15 tests unitarios**. Si el archivo crece, puedes separar cada parte en un módulo: `src/piece.rs` y `src/fees.rs`, declarados en `src/lib.rs` con `mod piece;` y reexportados con `pub use piece::*;` para que se sigan importando como `museum::validate_piece`.

### Paso 5: tests de integración

1. Crea `rust/tests/<tu-dominio>.rs` (en el museo, `tests/museum.rs`). Importa tu crate por su nombre: `use museum::{...};`.
2. Escribe **al menos dos tests** que usen la API pública como lo haría otro programa: un flujo que combine varias funciones, como validar una pieza y calcular su seguro, o los cargos de una devolución.
3. Si una función devuelve una estructura, compárala entera con el `assert_eq!` de `pretty_assertions`:

   ```rust
   use pretty_assertions::assert_eq;

   #[test]
   fn late_and_damaged_return() {
       let want = ReturnCharges { late_fee: 150, damage_fee: 500, total: 650 };
       assert_eq!(return_charges(5000, 3, Condition::Damaged), want);
   }
   ```

Si un test de `tests/` no compila porque una función es privada, decide si debe ser pública. No la hagas `pub` solo para el test.

Referencia: [lab 04](../2-practicas/lab-04-integracion-y-doctests/README.md).

### Paso 6: doctests

1. Documenta con `///` las funciones públicas principales.
2. Escribe **al menos dos doctests**, por ejemplo la validación con un caso válido y uno rechazado, y un cálculo.
3. Al menos uno usa `?`, con la línea oculta al final:

   ```rust
   /// ```
   /// use museum::{loan_premium, ZeroDays};
   ///
   /// let premium = loan_premium(200_000, 30)?;
   /// assert_eq!(premium, 6000);
   /// # Ok::<(), ZeroDays>(())
   /// ```
   ```

4. Genera la documentación y comprueba que los ejemplos se leen bien: `cargo doc --no-deps --open`.

Referencia: [lab 04](../2-practicas/lab-04-integracion-y-doctests/README.md#paso-4-un-doctest-con--paso-4).

### Paso 7: la comparativa

Completa `rust/comparativa.md`:

- qué funciones portaste y cuántos tests tiene cada suite;
- el test de la misma regla en Go y en Rust, uno debajo de otro;
- la tabla con dónde va el test, el caso con nombre, cómo ejecutar uno, el error esperado, la caja negra y la documentación ejecutable;
- los tests de tu suite Go que en Rust no hacen falta porque el tipo no deja escribir ese caso;
- al menos un caso que apareció al portar a Rust y que no tenías en Go.

No dejes ningún `TODO`.

### Paso 8: verificar y entregar

```bash
cd rust
cargo test
# ...
cargo test --lib 2>&1 | grep -cE '^test .* [.]{3} ok$'
# 26
cargo test --test '*' 2>&1 | grep -cE '^test .* [.]{3} ok$'
# 3
cargo test --doc 2>&1 | grep -cE ' - .* [.]{3} ok$'
# 3
```

Los números son los del museo; los tuyos dependen de tu dominio. `--test '*'` ejecuta todos los archivos de `tests/`. Comprueba también que no hay warnings: un test sin `#[test]` solo da un warning y no se ejecuta ([lab 01](../2-practicas/lab-01-primer-test-con-cargo-test/README.md#experimento-2-una-función-sin-test)).

```bash
cargo test --no-run 2>&1 | grep -c '^warning'
# 0
cargo fmt --check
```

`cargo test --no-run` compila los tests sin ejecutarlos. `cargo fmt --check` no imprime nada si el código sigue el formato estándar; si imprime diferencias, `cargo fmt` las aplica. Desde la raíz del bootcamp:

```bash
PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 37
```

Haz commit, sube la rama y abre un pull request a `main`.

## ✅ Requisitos verificables

Copia de [checks.json](../checks.json):

| Comprobación | Pista |
|---|---|
| `cargo test` pasa: unitarios, integración y doctests | Paso 8 |
| `cargo test` compila sin warnings (ningún test sin `#[test]`) | Paso 8 |
| No queda ningún `#[ignore]` en los tests | Pasos 3 y 4 |
| Al menos 15 tests unitarios que pasan | Pasos 3 y 4 |
| Al menos un test devuelve `Result` y usa `?` | Paso 4 |
| Al menos 2 tests de integración en `tests/` que pasan | Paso 5 |
| Los tests usan el `assert_eq!` de `pretty_assertions` | Paso 5 |
| Al menos 2 doctests que pasan | Paso 6 |
| `comparativa.md` compara Go y Rust, sin TODO | Paso 7 |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - Integración y doctests](../2-practicas/lab-04-integracion-y-doctests/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 37](../README.md)
