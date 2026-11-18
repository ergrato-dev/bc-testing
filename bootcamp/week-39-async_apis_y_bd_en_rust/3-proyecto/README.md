# Proyecto Semanal - La API del dominio en Rust, sobre PostgreSQL

> **🎯 ÚNICO ENTREGABLE**: este proyecto es el único entregable obligatorio de la semana.

## Objetivo

Llevar a Rust la API de tu dominio que escribiste en Go en la [semana 34](../../week-34-http_bd_e_integracion_en_go/3-proyecto/README.md), sobre el servicio de la [semana 38](../../week-38-traits_dobles_y_parametrizacion_en_rust/3-proyecto/README.md):

- los almacenes y el servicio externo como traits async;
- la API con axum, probada con `oneshot` y los dobles de `tests/common`;
- el cliente del servicio externo con reqwest, probado contra wiremock;
- los almacenes en PostgreSQL con sqlx, probados con `#[sqlx::test]`.

## Dónde va

En la carpeta `rust/` de tu repositorio `qa-<nombre>-<apellido>` ([proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)). Usa el `compose.yaml` de la raíz del repositorio, el de la [semana 23](../../week-23-testing_de_bases_de_datos_en_python/3-proyecto/README.md).

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

## Paso a paso

### Paso 1: la rama y el starter

1. Crea la rama de la semana:

   ```bash
   cd qa-<nombre>-<apellido>
   git switch main && git pull
   git switch -c week-39
   ```

2. Copia el starter:

   ```bash
   cp -rn <ruta-al-bootcamp>/bootcamp/week-39-async_apis_y_bd_en_rust/3-proyecto/starter/. .
   ```

   Con `-n`, `cp` no sobrescribe nada de las semanas 37 y 38. El starter trae:

   | Archivo | Qué es |
   |---|---|
   | `rust/.cargo/config.toml` | `DATABASE_URL` apuntando al PostgreSQL de `compose.yaml`. Si en tu `compose.yaml` cambiaste usuario, contraseña, base o puerto, cámbialos aquí también |
   | `rust/src/api.rs` | El módulo de la API, vacío, con un TODO |
   | `rust/src/registry.rs` | El módulo del cliente HTTP, vacío, con un TODO |
   | `rust/src/pg.rs` | El módulo de los almacenes en PostgreSQL, vacío, con un TODO |
   | `rust/tests/api.rs` | Dos tests con `#[ignore = "TODO: ..."]` |
   | `rust/tests/registry.rs` | Un test con `#[ignore = "TODO: ..."]` |
   | `rust/tests/pg.rs` | Dos tests con `#[ignore = "TODO: ..."]` |

   En el museo, el servicio externo es el registro de instituciones acreditadas. Si en tu dominio es otro (un servicio de pagos, un catálogo, un validador), renombra `registry.rs` con su nombre.

### Paso 2: las dependencias y los módulos

1. Añade las dependencias ([teoría 02](../1-teoria/02-axum-oneshot-y-wiremock.md#las-dependencias) y [teoría 03](../1-teoria/03-sqlx-test-y-testcontainers.md#las-dependencias)):

   ```bash
   cd rust
   cargo add axum@=0.8.9
   cargo add serde@=1.0.229 --features derive
   cargo add sqlx@=0.9.1 --no-default-features --features runtime-tokio,postgres,migrate,macros
   cargo add reqwest@=0.13.5 --no-default-features --features json
   cargo add --dev tokio@=1.54.1 --features macros,rt-multi-thread
   cargo add --dev tower@=0.5.3 --features util
   cargo add --dev http-body-util@=0.1.5 serde_json@=1.0.154 wiremock@=0.6.5
   ```

2. Declara los módulos nuevos en `src/lib.rs` y reexpórtalos:

   ```rust
   mod api;
   mod fees;
   mod lending;
   mod pg;
   mod piece;
   mod registry;

   pub use api::*;
   pub use fees::*;
   pub use lending::*;
   pub use pg::*;
   pub use piece::*;
   pub use registry::*;
   ```

3. Ejecuta:

   ```bash
   cargo test
   # warning: unused import: `api::*`
   # warning: unused import: `pg::*`
   # warning: unused import: `registry::*`
   # ...
   # warning: unused dependency `axum`
   # warning: unused dependency `reqwest`
   # warning: unused dependency `sqlx`
   # ...
   #      Running tests/api.rs (...)
   #
   # running 2 tests
   # test get_endpoint ... ignored, TODO: GET con oneshot, estado y JSON comparado con json! (paso 5)
   # test write_endpoint ... ignored, TODO: la operación principal, sus rechazos con rstest, el 500, el 400 y el 405 (paso 5)
   # ...
   ```

   Rust 1.100 avisa también de las dependencias que el código no usa todavía. Los warnings desaparecen a medida que llenas los módulos.

### Paso 3: los traits async

Los almacenes van a hablar con PostgreSQL y el registro con un servicio HTTP: sus métodos pasan a ser async. En `src/lending.rs`:

1. Declara cada método que espera algo como `impl Future<Output = ...> + Send`, y añade `Send + Sync` a cada trait ([teoría 02](../1-teoria/02-axum-oneshot-y-wiremock.md#3-un-servicio-con-traits-async-en-axum)):

   ```rust
   pub trait PieceStore: Send + Sync {
       fn find_by_code(
           &self,
           code: &str,
       ) -> impl Future<Output = Result<Option<Piece>, String>> + Send;
       fn save(&self, piece: &Piece) -> impl Future<Output = Result<(), String>> + Send;
   }

   pub trait Registry: Send + Sync {
       fn is_accredited(&self, institution: &str) -> impl Future<Output = Result<bool, String>> + Send;
   }
   ```

   Con una base de datos y una red de por medio, las consultas pueden fallar: `find_by_code` devuelve ahora `Result<Option<Piece>, String>` e `is_accredited`, `Result<bool, String>`. Añade a tu error una variante para el servicio externo (`LendError::Registry(String)`).

   Un método que no espera a nadie puede quedarse síncrono. En el museo, `Notifier::send` deja el aviso en una cola: solo gana `Send + Sync`, y el `mock!` de la semana 38 sigue sirviendo sin cambios.

2. Convierte el método del servicio en `async fn` y añade `.await` a cada llamada:

   ```rust
   pub async fn lend(&self, code: &str, institution: &str, days: u32) -> Result<Piece, LendError> {
       let mut piece = self
           .pieces
           .find_by_code(code)
           .await
           .map_err(LendError::Store)?
           .ok_or(LendError::UnknownPiece)?;
       // ...
   ```

3. Actualiza `tests/common/mod.rs`:
   - Los fakes cambian `RefCell` por `Arc<Mutex<...>>` y derivan `Clone`: `pub struct FakePieces(pub Arc<Mutex<HashMap<String, Piece>>>);`. `Mutex` porque axum comparte el servicio entre hilos; `Arc` para que el test se quede un clon del fake que ve el mismo mapa.
   - Sus métodos pasan a `async fn` y leen con `.lock().unwrap()` en lugar de `.borrow()`.
   - El stub devuelve `Ok(...)`.

4. Actualiza `tests/lending.rs`: `#[tokio::test]` y `async fn` en cada test, `.await` en cada `lend`, y `.lock().unwrap()` en lugar de `.borrow()`. En los casos de rstest, `#[tokio::test]` va debajo de los `#[case]`:

   ```rust
   #[rstest]
   #[case::unknown_piece("MUS-999", Status::Available, "MNAC", 30, LendError::UnknownPiece)]
   // ...
   #[tokio::test]
   async fn rejects(
   ```

5. Ejecuta `cargo test --test lending`: pasan los mismos 7 tests de la semana 38.

### Paso 4: la API

Copia la API Go de la [semana 34](../../week-34-http_bd_e_integracion_en_go/3-proyecto/README.md#paso-3-la-api): las mismas rutas y los mismos códigos. En el museo:

| Ruta | Respuesta |
|---|---|
| `GET /api/pieces/{code}` | `200` con la pieza, o `404` |
| `POST /api/pieces/{code}/loan` con `{"institutionId": "MALI", "days": 30}` | `200` con la pieza prestada |

| Error | Código |
|---|---|
| `UnknownPiece` | `404` |
| `NotAvailable` | `409` |
| `NotAccredited`, `ZeroDays` | `422` |
| `Store`, `Registry`, `Notify` | `500`, con `{"error": "internal error"}`: no enseñes el error interno |

En `src/api.rs`:

1. `pub fn router(service) -> Router`, genérico como el servicio. El servicio va en un `Arc` como estado del router:

   ```rust
   pub fn router<P, L, R, N>(service: LendingService<P, L, R, N>) -> Router
   where
       P: PieceStore + 'static,
       L: LoanStore + 'static,
       R: Registry + 'static,
       N: Notifier + 'static,
   {
       Router::new()
           .route("/api/pieces/{code}", get(get_piece::<P, L, R, N>))
           .route("/api/pieces/{code}/loan", post(lend::<P, L, R, N>))
           .with_state(Arc::new(service))
   }
   ```

   `'static` dice que los tipos no contienen referencias prestadas: axum guarda el estado mientras viva el router.

2. Un handler por ruta, que devuelve `Result<Json<Piece>, LendError>`. El cuerpo del `POST` se lee con un `struct` con `#[derive(Deserialize)]` y `#[serde(rename_all = "camelCase")]`.
3. `status_for(&LendError) -> StatusCode`, la tabla de códigos en una sola función, como en Go.
4. `impl IntoResponse for LendError`: el código de `status_for` y `{"error": "..."}`. Con esto, axum convierte cualquier `Err` de un handler en la respuesta.
5. Añade `#[derive(Serialize)]` a los tipos que devuelve la API, con los nombres de la API Java: `#[serde(rename_all = "camelCase")]` en `Piece` (`declaredValue`) y `#[serde(rename_all = "SCREAMING_SNAKE_CASE")]` en `Status` (`"ON_LOAN"`).

### Paso 5: los tests de la API

En `tests/api.rs`, sustituye los dos tests del starter. Con los dobles de `tests/common` ([lab 02](../2-practicas/lab-02-rutas-axum-con-oneshot/README.md)):

1. Una función `api(status, notifier)` que construye el router con la pieza en ese estado y devuelve también un clon del fake, para mirar dentro después de la petición.
2. Una función `send(app, req) -> (StatusCode, Value)` con `oneshot`, `collect` y `serde_json::from_slice`.
3. Los tests:
   - **GET**: `200` y el cuerpo comparado con `json!`, campo por campo.
   - **GET de lo que no existe**: `404` y `{"error": "UnknownPiece"}`.
   - **POST correcto**: el mock con `times(1)`, el `200`, el estado en el cuerpo y en el fake.
   - **POST rechazado**: un `#[case]` por cada código de error, con el mock en `never()`. Cada caso comprueba el código y que el fake quedó como estaba.
   - **500**: el notificador falla y el cuerpo es `{"error": "internal error"}`.
   - **400**: un JSON mal formado.
   - **405**: un `GET` sobre la ruta del préstamo, con `Allow: POST`.

```bash
cargo test --test api
# ...
# running 11 tests
# test get_unknown_piece_is_404 ... ok
# test get_returns_the_piece ... ok
# test lend_rejects::case_1_unknown_piece ... ok
# test internal_errors_are_hidden ... ok
# test get_on_the_loan_route_is_405 ... ok
# test lend_rejects::case_2_on_loan ... ok
# test lend_rejects::case_3_in_restoration ... ok
# test lend_rejects::case_4_not_accredited ... ok
# test unreadable_json_is_400 ... ok
# test lend_rejects::case_5_zero_days ... ok
# test lend_returns_the_lent_piece ... ok
#
# test result: ok. 11 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
```

### Paso 6: el cliente del servicio externo

1. En `src/registry.rs`, escribe el cliente con reqwest ([lab 03](../2-practicas/lab-03-clientes-reqwest-con-wiremock/README.md)): un `struct` con la URL base y un `reqwest::Client` con plazo, y la implementación del trait. Decide qué significa cada respuesta y escríbelo en el comentario del método. En el museo, `HttpRegistry` pide `GET {base_url}/institutions/{id}`:

   | Respuesta | Resultado |
   |---|---|
   | `200` con `{"accredited": true}` o `false` | `Ok(true)` u `Ok(false)` |
   | `404` | `Ok(false)`: el registro no conoce la institución |
   | Cualquier otro estado, o ninguna respuesta en el plazo | `Err(...)`: no se presta sin saberlo |

2. En `tests/registry.rs`, un test por fila con wiremock, más uno con `set_delay` para el plazo. Una función que monte el servidor con `expect(1)` en la ruta exacta: sin ese `expect`, un cliente que pidiera una ruta equivocada recibiría el `404` de wiremock y el test del `404` pasaría.

```bash
cargo test --test registry
# ...
# running 5 tests
# test unknown_institution_is_not_accredited ... ok
# test registry_failure_is_an_error ... ok
# test accredited_institution ... ok
# test institution_without_accreditation ... ok
# test slow_registry_is_an_error ... ok
#
# test result: ok. 5 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.20s
```

### Paso 7: los almacenes en PostgreSQL

1. Copia la migración de tu suite Go de la [semana 34](../../week-34-http_bd_e_integracion_en_go/3-proyecto/README.md#paso-5-el-almacén-en-postgresql) a `rust/migrations/`, sin cambiar ni una línea. En el museo:

   ```bash
   mkdir migrations
   cp ../go/pgstore/migrations/001_pieces_and_loans.sql migrations/
   ```

   El nombre `001_pieces_and_loans.sql` ya sigue el formato de sqlx, `<versión>_<descripción>.sql`. El esquema es el mismo para Java, Go y Rust.

2. En `src/pg.rs`, un `PgStore` con un `PgPool` que implemente los traits de los almacenes con `sqlx::query` y `.bind` ([lab 04](../2-practicas/lab-04-postgresql-con-sqlx-test-y-testcontainers/README.md)). Las implementaciones se escriben con `async fn`. En el museo:
   - `#[derive(Clone)]`: el servicio recibe un `PgStore` para cada almacén, clonados del mismo pool.
   - `find_by_code` lee la fila como una tupla y la convierte en `Piece`. `Status::parse` convierte el texto de la columna en el `enum`, y `Status::as_str` hace lo contrario al guardar.
   - `save` es un `INSERT ... ON CONFLICT (code) DO UPDATE SET status = EXCLUDED.status`.
   - `open` deja que PostgreSQL calcule la fecha de devolución: `VALUES ($1, $2, current_date + $3)`. Así Rust no necesita fechas de calendario.
   - `declared_value` es `u32` en Rust e `int` en PostgreSQL, que no tiene enteros sin signo: conviértelo con `i32::try_from` al guardar y `u32::try_from` al leer.
   - Los errores de sqlx se convierten en `String` con `.map_err(|e| e.to_string())`.

### Paso 8: los tests con `#[sqlx::test]`

1. Arranca PostgreSQL desde la raíz del repositorio:

   ```bash
   docker compose up -d --wait
   ```

2. En `tests/pg.rs`, sustituye los dos tests del starter. Cada test recibe `pool: PgPool` y crea su `PgStore { pool }`:
   - **Guardar y leer** cada valor del `enum` de estado: comprueba que el texto que se escribe es el que el `CHECK` de la tabla admite.
   - **Actualizar** una fila existente.
   - **Lo que no existe** devuelve `Ok(None)`.
   - **Lo que la tabla rechaza**: un `declared_value` de `0`. El error menciona la restricción, `pieces_declared_value_check`.
   - **La fecha de devolución**: después de `open`, `SELECT due_date - current_date FROM loans` vale los días del préstamo.
   - **Clave foránea y duplicado** en `loans`: `loans_piece_code_fkey` y `loans_pkey`.
   - **El servicio completo**: `LendingService` con `PgStore` en los dos almacenes, el stub del registro y el mock del notificador. Después del préstamo, la pieza está `ON_LOAN` en la tabla y hay una fila en `loans`.

```bash
cargo test --test pg
# ...
# running 8 tests
# test save_updates_the_status ... ok
# test open_rejects_unknown_piece ... ok
# test lend_with_postgres ... ok
# test saves_and_finds_every_status ... ok
# test schema_rejects_zero_value ... ok
# test open_sets_the_due_date ... ok
# test unknown_piece_is_none ... ok
# test open_rejects_a_second_loan ... ok
#
# test result: ok. 8 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 1.03s
```

Ninguno de estos tests necesita limpiar: cada uno tiene su base. Si uno falla, busca su base en `_sqlx_test.databases` ([teoría 03](../1-teoria/03-sqlx-test-y-testcontainers.md#la-base-de-un-test-que-falla)).

### Paso 9: verificar y entregar

1. Añade a `dobles.md` una sección con las piezas reales de esta semana: qué prueba cada archivo de tests, contra qué (`oneshot`, wiremock, `#[sqlx::test]`) y qué detectaría cada uno que los dobles no pueden detectar.
2. Ejecuta la suite completa:

   ```bash
   CI=true cargo test
   # ...
   #      Running tests/api.rs (...)
   # test result: ok. 11 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
   #      Running tests/lending.rs (...)
   # test result: ok. 7 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
   # ...
   #      Running tests/pg.rs (...)
   # test result: ok. 8 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 1.07s
   #      Running tests/registry.rs (...)
   # test result: ok. 5 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.20s
   # ...
   ```

3. Comprueba los requisitos desde la carpeta del bootcamp:

   ```bash
   PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 39
   ```

4. Haz commit, push y abre el pull request de `week-39` a `main`.

## ✅ Requisitos verificables

Los comprueba `./scripts/verificar-semana.sh 39` ([checks.json](../checks.json)):

| Requisito | Comprobación |
|---|---|
| `cargo test` pasa con el PostgreSQL de `compose.yaml` arrancado (necesita Docker) | `docker compose up -d --wait` y `CI=true cargo test` |
| `cargo test` compila sin warnings, tampoco de dependencias sin usar | `cargo test --no-run` sin líneas `warning` |
| No queda ningún `#[ignore]` en los tests | `grep` en `src/` y `tests/` |
| Al menos 3 métodos de traits async declarados como `impl Future + Send` | `grep` en `src/` |
| Al menos 10 tests con `#[tokio::test]` | `grep` en `tests/` |
| Los tests de la API usan `oneshot` y el JSON se compara con `json!` | `grep` en `tests/` |
| La API responde un 500 sin enseñar el error interno, y un test lo comprueba | `internal error` en `src/` y en `tests/` |
| El cliente externo se prueba con wiremock y al menos un `expect` | `MockServer::start` y `.expect(N)` en `tests/` |
| El esquema está en `rust/migrations/` y `DATABASE_URL` en `rust/.cargo/config.toml` | Los archivos existen |
| Al menos 5 tests con `#[sqlx::test]` | `grep` en `tests/` |
| `dobles.md` explica wiremock y `#[sqlx::test]`, sin TODO | `grep` en `dobles.md` |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - PostgreSQL con `#[sqlx::test]` y testcontainers](../2-practicas/lab-04-postgresql-con-sqlx-test-y-testcontainers/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 39](../README.md)
