# 03 - PostgreSQL con `#[sqlx::test]` y testcontainers

> Lenguaje: **Rust**

Para probar un repositorio contra PostgreSQL hay que resolver dos cosas: de dónde sale el servidor y cómo empieza cada test con la base de datos limpia. En Go, testcontainers-go arrancaba el servidor y cada test hacía rollback de su transacción ([semana 34](../../week-34-http_bd_e_integracion_en_go/1-teoria/03-testcontainers-go-y-postgresql.md#3-aislar-cada-test)). En Rust, **sqlx** trae `#[sqlx::test]`, que da a cada test **una base de datos nueva**; y **testcontainers-rs** arranca el servidor desde el propio test.

![El ciclo de una base de datos de #[sqlx::test]](../0-assets/02-ciclo-de-sqlx-test.svg)

---

## 🎯 Objetivos

- Escribir consultas con sqlx sin necesitar la base de datos al compilar.
- Probar un repositorio con `#[sqlx::test]`: migraciones, fixtures y la base que queda cuando un test falla.
- Arrancar PostgreSQL desde un test con testcontainers-rs y su módulo `postgres`.
- Elegir entre los dos.

---

## Las dependencias

```bash
cargo add sqlx@=0.9.1 --no-default-features --features runtime-tokio,postgres,migrate,macros
cargo add --dev tokio@=1.54.1 --features macros,rt-multi-thread
cargo add --dev testcontainers-modules@=0.15.0 --features postgres
```

| Feature de sqlx | Para qué |
|---|---|
| `runtime-tokio` | sqlx es async y necesita un runtime: el de tokio |
| `postgres` | El driver de PostgreSQL. Las otras bases de datos no se compilan |
| `migrate` | Las migraciones: `sqlx::migrate!()` y las de `#[sqlx::test]` |
| `macros` | `#[sqlx::test]`, `sqlx::migrate!()` y `#[derive(FromRow)]` |

`testcontainers-modules` trae los módulos de cada servicio y, dentro, el crate `testcontainers` en la versión con la que funcionan (0.27.3 para 0.15.0). Se importa desde `testcontainers_modules::testcontainers`, así que no lo añadas aparte: dos versiones distintas no se entienden entre sí.

---

## 1. Consultas sin la base de datos al compilar

```rust
pub async fn find(pool: &PgPool, code: &str) -> Result<Option<Piece>, sqlx::Error> {
    sqlx::query_as("SELECT code, name, declared_value FROM pieces WHERE code = $1")
        .bind(code)
        .fetch_optional(pool)
        .await
}
```

| Pieza | Qué hace |
|---|---|
| `PgPool` | Un pool de conexiones. Se clona barato: los clones comparten las conexiones |
| `query_as` | Ejecuta la consulta y convierte cada fila en un tipo con `#[derive(FromRow)]` o en una tupla |
| `query` / `query_scalar` | Sin filas que convertir (`INSERT`, `UPDATE`) / una sola columna (`count(*)`) |
| `.bind(code)` | El valor de `$1`. Va aparte de la consulta: nunca se pega al texto del SQL |
| `fetch_optional` / `fetch_one` / `fetch_all` | Cero o una fila / exactamente una / todas |

sqlx tiene también las macros `query!` y `query_as!`, que comprueban la consulta contra la base de datos **al compilar**. Para eso necesitan `DATABASE_URL` en cada compilación, o una caché generada con `cargo sqlx prepare`. Esta semana no se usan: la consulta se comprueba al ejecutar los tests, que es lo que ya hacen.

---

## 2. `#[sqlx::test]`

`#[sqlx::test]` necesita un PostgreSQL al que conectarse: el de `compose.yaml` ([semana 23](../../week-23-testing_de_bases_de_datos_en_python/3-proyecto/README.md)). Lee su URL de la variable `DATABASE_URL`, que Cargo pone por ti si la escribes en `.cargo/config.toml`, junto a `Cargo.toml`:

```toml
# Variables de entorno para cargo test: #[sqlx::test] lee DATABASE_URL para
# conectarse al PostgreSQL de compose.yaml y crear en él una base por test.
[env]
DATABASE_URL = "postgres://museum:museum@127.0.0.1:55432/museum"
```

Con el servidor arrancado (`docker compose up -d --wait`), un test recibe un `PgPool` como argumento:

```rust
#[sqlx::test]
async fn saves_and_finds(pool: PgPool) {
    save(&pool, &amphora()).await.unwrap();

    let got = find(&pool, "MUS-001").await.unwrap();

    assert_eq!(got, Some(amphora()));
}
```

Para cada test, sqlx:

1. Se conecta a `DATABASE_URL` y **crea una base de datos nueva**.
2. Aplica las migraciones de `migrations/`, junto a `Cargo.toml`. Los archivos se llaman `<versión>_<descripción>.sql`, como `0001_pieces.sql`, y se aplican en orden de versión.
3. Ejecuta el test con un pool conectado a esa base.
4. Si el test pasa, borra la base. **Si falla, la deja** para que puedas mirar qué había, hasta que el mismo test vuelva a ejecutarse.

Ningún test ve los datos de otro, y los tests se ejecutan en paralelo.

| Aislamiento | Python (S23) y Go (S34) | `#[sqlx::test]` |
|---|---|---|
| Qué se deshace | La transacción del test: rollback al final | La base entera: se borra al final |
| Coste por test | Casi nada | Crear y migrar una base: unas decenas de milisegundos |
| El test puede hacer `COMMIT` | No: rompería el aislamiento | Sí |
| Tras un error esperado de PostgreSQL | La transacción queda abortada (`25P02`) y hace falta un savepoint | El pool sigue funcionando |

### La base de un test que falla

Cuando un test falla, la salida no dice dónde está su base. sqlx guarda la lista en la tabla `_sqlx_test.databases` de la base principal:

```bash
docker compose exec db psql -U museum -c "select db_name, test_path from _sqlx_test.databases"
#                              db_name                             |        test_path
# -----------------------------------------------------------------+-------------------------
#  _sqlx_test_gi9w685_zkhKBFXeRoMMIPeMFB69Zq3ddA8MbR9uXMgkn_Dxs_Ce | pieces::saves_and_finds
# (1 row)
```

El nombre sale del nombre del test: el mismo test usa siempre la misma base. Cuando ese test vuelve a ejecutarse, sqlx borra la base vieja y crea una nueva; ejecutar otros tests no la toca.

### Fixtures

Un test que necesita datos de partida los pide con `fixtures`:

```rust
#[sqlx::test(fixtures("pieces"))]
async fn lists_valuable_pieces(pool: PgPool) {
```

sqlx ejecuta `fixtures/pieces.sql` después de las migraciones. La ruta es relativa al archivo del test: para `tests/pieces.rs`, es `tests/fixtures/pieces.sql`. Se lee al compilar, así que un nombre equivocado es un error de compilación:

```text
error: couldn't read `tests/fixtures/piece.sql`: No such file or directory (os error 2)
  --> tests/pieces.rs:59:1
```

### Cuando no hay servidor

Sin `DATABASE_URL`, cada test entra en pánico al empezar:

```text
DATABASE_URL must be set: EnvVar(NotPresent)
```

Con la URL, pero con PostgreSQL parado, cada test espera 30 s a que el pool consiga una conexión y luego falla:

```text
failed to connect to setup test database: PoolTimedOut
test result: FAILED. 0 passed; 5 failed; 0 ignored; 0 measured; 0 filtered out; finished in 30.00s
```

Si la suite tarda medio minuto en fallar, mira primero si el contenedor está arrancado.

---

## 3. testcontainers-rs

testcontainers-rs arranca el contenedor desde el test, como testcontainers-go ([semana 34](../../week-34-http_bd_e_integracion_en_go/1-teoria/03-testcontainers-go-y-postgresql.md#1-el-módulo-postgres)): no hace falta `compose.yaml` ni `DATABASE_URL`.

```rust
use testcontainers_modules::postgres::Postgres;
use testcontainers_modules::testcontainers::ImageExt;
use testcontainers_modules::testcontainers::runners::AsyncRunner;

#[tokio::test]
async fn saves_and_finds_in_a_container() {
    let container = Postgres::default()
        .with_tag("18.7-alpine")
        .start()
        .await
        .unwrap();
    let port = container.get_host_port_ipv4(5432).await.unwrap();
    let url = format!("postgres://postgres:postgres@127.0.0.1:{port}/postgres");
    let pool = PgPool::connect(&url).await.unwrap();
    sqlx::migrate!().run(&pool).await.unwrap();
    // ...
}
```

| Pieza | Qué / Para qué / Impacto |
|---|---|
| `Postgres::default()` | La imagen `postgres` con usuario, contraseña y base `postgres`, y una espera hasta que acepta conexiones |
| `.with_tag("18.7-alpine")` | La misma versión que `compose.yaml`. Sin ella, el módulo usa la suya, que puede no coincidir con la de producción |
| `.start()` | Arranca el contenedor. Viene del trait `AsyncRunner`, que hay que importar |
| `get_host_port_ipv4(5432)` | El puerto del equipo al que Docker conectó el 5432 del contenedor: uno libre, distinto en cada ejecución |
| `sqlx::migrate!()` | Mete en el binario las migraciones de `migrations/` al compilar y `run` las aplica |

Cuando `container` sale de su ámbito, al final del test, testcontainers-rs borra el contenedor. Un contenedor por test es lento (unos 3 s cada uno en el [lab 04](../2-practicas/lab-04-postgresql-con-sqlx-test-y-testcontainers/README.md)). Para compartir uno entre tests haría falta guardarlo en una variable estática, y Rust no tiene un `TestMain` que lo pare al final.

---

## 4. ¿Cuál usar?

| | `#[sqlx::test]` | testcontainers-rs |
|---|---|---|
| Servidor | Uno ya arrancado (`compose.yaml` o un servicio de CI) | Lo arranca el test |
| Aislamiento | Una base nueva por test, gratis | Lo montas tú: un contenedor por test o transacciones |
| Velocidad | Decenas de milisegundos por test | Segundos por contenedor |
| Qué necesita el que ejecuta | `docker compose up` antes de `cargo test` | Solo Docker |

El proyecto de esta semana usa `#[sqlx::test]` con el PostgreSQL de `compose.yaml`: aísla cada test sin código propio y es rápido. testcontainers-rs es la opción cuando la suite no puede depender de un servidor que alguien arranca antes, o cuando necesitas un servicio sin un equivalente a `#[sqlx::test]`.

---

## ✅ Resumen

- `sqlx::query` con `.bind` no necesita la base de datos al compilar; `query!` sí.
- `#[sqlx::test]` crea una base por test en el PostgreSQL de `DATABASE_URL`, aplica `migrations/` y la borra si el test pasa. Si falla, la deja: búscala en `_sqlx_test.databases`.
- Las fixtures se leen al compilar, con rutas relativas al archivo del test.
- Sin servidor, `#[sqlx::test]` tarda 30 s en fallar con `PoolTimedOut`.
- testcontainers-rs arranca PostgreSQL desde el test y lo borra al terminar. Importa `testcontainers` desde `testcontainers_modules`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 02 - APIs con axum y clientes con wiremock](02-axum-oneshot-y-wiremock.md) | [Índice de labs](../2-practicas/README.md) → |

[↑ Semana 39](../README.md)
