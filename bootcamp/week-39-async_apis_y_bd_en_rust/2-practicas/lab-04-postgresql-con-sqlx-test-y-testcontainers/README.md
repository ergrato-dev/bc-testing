# Lab 04 - PostgreSQL con `#[sqlx::test]` y testcontainers

## Objetivo

Probar un repositorio de piezas escrito con sqlx contra PostgreSQL de dos maneras: con `#[sqlx::test]`, que da a cada test una base de datos nueva en el PostgreSQL de `compose.yaml`, y con testcontainers-rs, que arranca el servidor desde el propio test. Ver qué deja `#[sqlx::test]` cuando un test falla.

## Tiempo estimado

65 minutos.

## Preparación

Necesitas Docker en marcha. El PostgreSQL de este lab usa el puerto `55432`, el mismo que el `compose.yaml` de tu proyecto: si tienes el del proyecto arrancado, páralo antes con `docker compose down` en la raíz de tu repositorio.

1. Arranca PostgreSQL:

   ```bash
   cd starter
   docker compose up -d --wait
   # ...
   #  Container starter-db-1 Started
   #  Container starter-db-1 Waiting
   #  Container starter-db-1 Healthy
   ```

   `--wait` espera a que el `healthcheck` de `compose.yaml` diga que PostgreSQL acepta conexiones ([semana 29](../../../week-29-testcontainers_en_java/3-proyecto/README.md)).

2. Compila:

   ```bash
   cargo test
   #    Compiling tokio v1.54.1
   #    Compiling sqlx v0.9.1
   #    Compiling lab04 v0.1.0 (.../starter)
   #     Finished `test` profile [unoptimized + debuginfo] target(s) in 37.63s
   # ...
   # running 0 tests
   # ...
   ```

Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `Cargo.toml` | El paquete `lab04`: sqlx con `runtime-tokio`, `postgres`, `migrate` y `macros` ([teoría 03](../../1-teoria/03-sqlx-test-y-testcontainers.md#las-dependencias)); tokio y testcontainers-modules en `[dev-dependencies]` |
| `Cargo.lock` | Las versiones exactas |
| `compose.yaml` | PostgreSQL 18.7 en `127.0.0.1:55432`, usuario, contraseña y base `museum`. El de la [semana 23](../../../week-23-testing_de_bases_de_datos_en_python/3-proyecto/README.md) |
| `.cargo/config.toml` | `DATABASE_URL` apuntando a ese PostgreSQL. Cargo la pone en el entorno de `cargo test` |
| `migrations/0001_pieces.sql` | La tabla `pieces`, con un `CHECK (declared_value > 0)` |
| `src/lib.rs` | `save`, `find`, `count` y `worth_more_than` |
| `tests/pieces.rs` | Los tests de `#[sqlx::test]`, comentados (PASOS 1 a 3) |
| `tests/fixtures/pieces.sql` | Tres piezas para el PASO 3 |
| `tests/container.rs` | El test con testcontainers, comentado (PASO 4) |

Las consultas de `src/lib.rs` usan `sqlx::query` y `.bind`: se comprueban al ejecutarse, y el proyecto compila sin la base de datos ([teoría 03](../../1-teoria/03-sqlx-test-y-testcontainers.md#1-consultas-sin-la-base-de-datos-al-compilar)).

## Paso a paso

### Paso 1: una base de datos para el test (PASO 1)

1. Descomenta el bloque `// PASO 1` de `tests/pieces.rs`.
2. Ejecuta:

   ```bash
   cargo test --test pieces
   # ...
   # running 1 test
   # test saves_and_finds ... ok
   #
   # test result: ok. 1 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.36s
   ```

`#[sqlx::test]` hace el trabajo que en Go hacían `TestMain` y la transacción por test ([semana 34](../../../week-34-http_bd_e_integracion_en_go/1-teoria/03-testcontainers-go-y-postgresql.md#3-aislar-cada-test)):

| Qué | Quién lo hace |
|---|---|
| Conectarse a PostgreSQL | sqlx, con `DATABASE_URL` |
| Crear una base de datos para este test | sqlx |
| Aplicar `migrations/0001_pieces.sql` | sqlx: busca `migrations/` junto a `Cargo.toml` |
| Dar al test un pool conectado a esa base | sqlx: el argumento `pool: PgPool` |
| Borrar la base al terminar | sqlx, si el test pasa |

El test no lleva `#[tokio::test]`: `#[sqlx::test]` ya crea el runtime.

### Paso 2: cada test empieza de cero (PASO 2)

1. Descomenta el bloque `// PASO 2`.
2. Ejecuta `cargo test --test pieces`: pasan 4.

`saves_and_finds` y `save_twice_updates` guardan los dos la pieza `MUS-001`, y `unknown_code_is_none` espera no encontrarla. Pasan los tres, en paralelo, porque cada uno tiene su base.

`schema_rejects_zero_value` prueba el esquema, no el código Rust: `save` no valida nada y es la tabla la que rechaza el `0`. El test comprueba el código de error de PostgreSQL, `23514` (`check_violation`), con `as_database_error()`. Después del error, el test podría seguir usando el pool: no hay ninguna transacción abortada, como pasaba en Go.

### Paso 3: datos de partida con fixtures (PASO 3)

1. Descomenta el bloque `// PASO 3`.
2. Ejecuta `cargo test --test pieces`: pasan 5.

`fixtures("pieces")` ejecuta `tests/fixtures/pieces.sql` después de las migraciones, solo para ese test. Es el `factory_boy` de la [semana 23](../../../week-23-testing_de_bases_de_datos_en_python/1-teoria/03-datos-con-faker-y-factory-boy.md) escrito en SQL: cómodo para un conjunto fijo de datos, pero cada test que lo usa depende de todas sus filas.

### Paso 4: un contenedor por test (PASO 4)

1. Descomenta el bloque `// PASO 4` de `tests/container.rs`.
2. Ejecuta:

   ```bash
   cargo test --test container
   # ...
   # running 1 test
   # test saves_and_finds_in_a_container ... ok
   #
   # test result: ok. 1 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 6.63s
   ```

   La primera vez, Docker descarga la imagen `postgres:18.7-alpine` si no la tenía y el test tarda más.

Este test no usa `compose.yaml` ni `DATABASE_URL`: arranca su propio PostgreSQL en un puerto libre, aplica las migraciones con `sqlx::migrate!()` y borra el contenedor al terminar, cuando `container` sale de su ámbito. Compara: 5 tests de `#[sqlx::test]` en 0.6 s frente a uno solo en varios segundos.

## Experimentos

### Experimento 1: la base de un test que falla

1. En `saves_and_finds`, cambia `assert_eq!(got, Some(amphora()));` por `assert_eq!(got, None);`.
2. Ejecuta:

   ```bash
   cargo test --test pieces saves_and
   # thread 'saves_and_finds' (368782) panicked at tests/pieces.rs:22:5:
   # assertion `left == right` failed
   #   left: Some(Piece { code: "MUS-001", name: "Ánfora romana", declared_value: 200000 })
   #  right: None
   # ...
   # test result: FAILED. 0 passed; 1 failed; 0 ignored; 0 measured; 4 filtered out; finished in 0.88s
   ```

3. Busca la base que dejó el test:

   ```bash
   docker compose exec db psql -U museum -c "select db_name, test_path from _sqlx_test.databases"
   #                              db_name                             |        test_path
   # -----------------------------------------------------------------+-------------------------
   #  _sqlx_test_gi9w685_zkhKBFXeRoMMIPeMFB69Zq3ddA8MbR9uXMgkn_Dxs_Ce | pieces::saves_and_finds
   # (1 row)
   ```

4. Mira qué había dentro cuando falló (copia el `db_name` de tu salida):

   ```bash
   docker compose exec db psql -U museum -d _sqlx_test_gi9w685_zkhKBFXeRoMMIPeMFB69Zq3ddA8MbR9uXMgkn_Dxs_Ce -c "select * from pieces"
   #   code   |     name      | declared_value
   # ---------+---------------+----------------
   #  MUS-001 | Ánfora romana |         200000
   # (1 row)
   ```

   Con la transacción por test de Go, esta fila habría desaparecido con el rollback.

5. Deshaz el cambio y ejecuta `cargo test --test pieces saves_and`. Al volver a ejecutarse, el test borra su base vieja: la consulta del punto 3 ya no devuelve filas.

### Experimento 2: una fixture que no existe

1. Cambia `fixtures("pieces")` por `fixtures("piece")`.
2. Ejecuta `cargo test --test pieces`:

   ```text
   error: couldn't read `tests/fixtures/piece.sql`: No such file or directory (os error 2)
     --> tests/pieces.rs:59:1
      |
   59 | #[sqlx::test(fixtures("piece"))]
      | ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
   ```

   La fixture se lee al compilar y su ruta es relativa al archivo del test. Un test en `src/lib.rs` la buscaría en `src/fixtures/`.

3. Deshaz el cambio.

### Experimento 3: sin servidor

1. Para PostgreSQL:

   ```bash
   docker compose stop
   ```

2. Ejecuta:

   ```bash
   cargo test --test pieces
   # thread 'schema_rejects_zero_value' (293324) panicked at /home/.../sqlx-core-0.9.1/src/testing/mod.rs:227:14:
   # failed to connect to setup test database: PoolTimedOut
   # ...
   # test result: FAILED. 0 passed; 5 failed; 0 ignored; 0 measured; 0 filtered out; finished in 30.00s
   ```

   Cada test espera 30 s a que el pool consiga una conexión. El test de `tests/container.rs` no se entera: no usa este servidor.

3. Vuelve a arrancarlo con `docker compose start`.

4. Ahora renombra `.cargo/config.toml` a `.cargo/config.bak` y ejecuta `cargo test --test pieces`:

   ```text
   thread 'lists_valuable_pieces' (294674) panicked at /home/.../sqlx-postgres-0.9.1/src/testing/mod.rs:93:44:
   DATABASE_URL must be set: EnvVar(NotPresent)
   ```

   Sin la URL, sqlx no sabe dónde crear las bases. Vuelve a llamarlo `config.toml`.

## Comprobación final

```bash
cargo test
# ...
#      Running tests/container.rs (...)
# ...
# test result: ok. 1 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 3.03s
#      Running tests/pieces.rs (...)
# ...
# test result: ok. 5 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.72s
# ...
```

Al terminar, para y borra el contenedor de PostgreSQL con `docker compose down`.

## Preguntas de cierre

1. ¿Qué hace `#[sqlx::test]` antes de ejecutar el cuerpo del test? ¿Qué hace después, si pasa y si falla?
2. Compara el aislamiento de `#[sqlx::test]` con la transacción por test de Go. ¿Qué puede hacer un test con el primero que no puede con la segunda?
3. ¿Por qué `schema_rejects_zero_value` prueba el esquema y no el código Rust?
4. ¿Cuándo elegirías testcontainers-rs en lugar de `#[sqlx::test]`?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - Clientes de reqwest con wiremock](../lab-03-clientes-reqwest-con-wiremock/README.md) | [Proyecto — Proyecto Semanal - La API del dominio en Rust, sobre PostgreSQL](../../3-proyecto/README.md) → |

[↑ Semana 39](../../README.md)
