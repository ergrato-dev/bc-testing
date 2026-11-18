# Lab 02 - Rutas de axum con `oneshot`

## Objetivo

Probar la API del catálogo del museo, escrita con axum, sin abrir un puerto: enviar cada petición con `oneshot`, leer el estado y el cuerpo JSON, comprobar lo que el handler cambia y lo que axum rechaza antes de llegar a él.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
cargo test
#    Compiling tokio v1.54.1
#    Compiling axum v0.8.9
#    Compiling lab02 v0.1.0 (.../starter)
#     Finished `test` profile [unoptimized + debuginfo] target(s) in 7.72s
# ...
# running 0 tests
# ...
```

Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `Cargo.toml` | El paquete `lab02`: axum y serde en `[dependencies]`; tokio, tower, http-body-util y serde_json en `[dev-dependencies]` ([teoría 02](../../1-teoria/02-axum-oneshot-y-wiremock.md#las-dependencias)) |
| `Cargo.lock` | Las versiones exactas |
| `src/lib.rs` | `app(catalog)`, el router con `GET /pieces/{code}` y `POST /pieces`, sobre un catálogo en memoria. Al final, los tests comentados por PASOS |

Lee `app` y sus dos handlers:

| Ruta | Respuesta |
|---|---|
| `GET /pieces/{code}` | `200` con la pieza en JSON, o `404` |
| `POST /pieces` con una pieza en JSON | `201` con la pieza; `409` si el código ya existe; `422` si `declaredValue` es `0` |

`Catalog` es un `Arc<Mutex<HashMap<...>>>`: un mapa compartido por todas las peticiones. Es el estado del router (`with_state`), y cada handler lo recibe con el extractor `State`.

## Paso a paso

### Paso 1: una petición sin abrir un puerto (PASO 1)

1. Descomenta el bloque `// PASO 1`.
2. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 1 test
   # test tests::get_returns_the_piece ... ok
   # ...
   ```

| Línea | Qué hace |
|---|---|
| `use tower::ServiceExt;` | Trae el método `oneshot` al ámbito. Es de un trait: sin el `use`, no existe |
| `Request::get("/pieces/MUS-001").body(Body::empty())` | Construye la petición. `body` cierra el builder; un `GET` lleva el cuerpo vacío |
| `app(catalog()).oneshot(req).await` | Envía la petición al router entero y espera la respuesta. Consume el router |
| `res.into_body().collect().await` | Junta las partes del cuerpo. `collect` viene de `http_body_util::BodyExt` |
| `json!({ ... })` | El JSON esperado, escrito como JSON. Se compara con el de la respuesta como `serde_json::Value` |

### Paso 2: lo que no existe (PASO 2)

1. Descomenta el bloque `// PASO 2`.
2. Ejecuta `cargo test`: pasan 3.

- Un código que no está en el catálogo: el handler devuelve `Err(StatusCode::NOT_FOUND)` y axum lo convierte en la respuesta.
- `DELETE` sobre una ruta que solo tiene `GET`: responde el router, no el handler, con `405` y la cabecera `Allow: GET,HEAD`. axum añade `HEAD` a toda ruta `get`.

### Paso 3: un POST que cambia el estado (PASO 3)

1. Descomenta el bloque `// PASO 3`.
2. Ejecuta `cargo test`: pasan 5.

`post_creates_the_piece` le pasa al router un clon del catálogo, `catalog.clone()`. Como es un `Arc`, el clon apunta al mismo mapa: después de la petición, el test mira dentro y encuentra la pieza nueva. Comprobar solo el `201` no bastaría: un handler que respondiera `201` sin guardar nada pasaría el test.

`r#"..."#` es una cadena en bruto: dentro, las comillas no se escapan. Útil para escribir JSON.

### Paso 4: el JSON que axum rechaza antes del handler (PASO 4)

1. Descomenta el bloque `// PASO 4`.
2. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 8 tests
   # test tests::get_unknown_piece_is_404 ... ok
   # test tests::missing_content_type_is_415 ... ok
   # test tests::get_returns_the_piece ... ok
   # test tests::malformed_json_is_400 ... ok
   # test tests::missing_field_is_422 ... ok
   # test tests::post_creates_the_piece ... ok
   # test tests::wrong_method_is_405 ... ok
   # test tests::post_existing_code_is_409 ... ok
   #
   # test result: ok. 8 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
   ```

Ningún handler del lab devuelve `400` ni `415`. Los da el extractor `Json` antes de llamar a `create_piece` ([teoría 02](../../1-teoria/02-axum-oneshot-y-wiremock.md#2-lo-que-axum-responde-por-ti)):

| Test | Petición | Código |
|---|---|---|
| `malformed_json_is_400` | `{"code": ` | `400`: no es JSON |
| `missing_field_is_422` | Sin `declaredValue` | `422`: es JSON, pero no es una `Piece` |
| `missing_content_type_is_415` | Sin `Content-Type: application/json` | `415` |

El `422` de un campo que falta y el `422` de `declaredValue: 0` tienen el mismo código pero vienen de sitios distintos: el primero de axum y el segundo de tu handler.

## Experimentos

### Experimento 1: sin el trait

1. Borra la línea `use tower::ServiceExt;`.
2. Ejecuta `cargo test`:

   ```text
   error[E0599]: no method named `oneshot` found for struct `Router<S>` in the current scope
      --> src/lib.rs:90:34
       |
    90 |         let res = app(catalog()).oneshot(req).await.unwrap();
       |                                  ^^^^^^^
   ...
       = help: items from traits can only be used if the trait is in scope
   ```

   Un método de un trait solo existe donde el trait está en el ámbito. `oneshot` es de `tower::ServiceExt`, no de `Router`; el compilador te dice dónde está.

3. Deshaz el cambio.

### Experimento 2: un campo que cambia de nombre

1. Borra la línea `#[serde(rename_all = "camelCase")]` de `Piece`.
2. Ejecuta:

   ```bash
   cargo test get_returns
   # thread 'tests::get_returns_the_piece' (367023) panicked at src/lib.rs:98:9:
   # assertion `left == right` failed
   #   left: Object {"code": String("MUS-001"), "declared_value": Number(200000), "name": String("Ánfora romana")}
   #  right: Object {"code": String("MUS-001"), "declaredValue": Number(200000), "name": String("Ánfora romana")}
   ```

   La API ha cambiado su contrato: un cliente que leía `declaredValue` deja de encontrarlo. El test lo ve porque compara contra un `json!` escrito a mano. Con `cargo test` verás que fallan también dos tests de `POST` con `left: 422`: el extractor ya no entiende `declaredValue`.

3. Deshaz el cambio.

### Experimento 3: el mismo router dos veces

1. Al final de `post_creates_the_piece`, después del último `assert_eq!`, añade:

   ```rust
   let api = app(catalog);
   api.oneshot(post(body)).await.unwrap();
   let again = api.oneshot(post(body)).await.unwrap();
   assert_eq!(again.status(), StatusCode::CONFLICT);
   ```

2. Ejecuta `cargo test`:

   ```text
   error[E0382]: use of moved value: `api`
      --> src/lib.rs:146:21
       |
   144 |         let api = app(catalog);
       |             --- move occurs because `api` has type `Router`, which does not implement the `Copy` trait
   145 |         api.oneshot(post(body)).await.unwrap();
       |         --- value moved here
   146 |         let again = api.oneshot(post(body)).await.unwrap();
       |                     ^^^ value used here after move
       |
   help: consider cloning the value if the performance cost is acceptable
       |
   145 |         api.clone().oneshot(post(body)).await.unwrap();
       |            ++++++++
   ```

   `oneshot` se queda con el router. El compilador propone la solución.

3. Aplícala: cambia el primer `api.oneshot` por `api.clone().oneshot` y ejecuta `cargo test post_creates`. Pasa: el segundo `POST` responde `409`, porque el clon comparte el catálogo.
4. Deshaz el cambio.

## Comprobación final

```bash
cargo test
# ...
# test result: ok. 8 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
# ...
```

## Preguntas de cierre

1. ¿Qué parte de la aplicación ejecuta `oneshot`? Compáralo con `httptest.NewRecorder` de Go ([semana 34](../../../week-34-http_bd_e_integracion_en_go/1-teoria/01-httptest-handlers-y-clientes.md#1-handlers-con-newrecorder)).
2. ¿Por qué el test compara el cuerpo con `json!` y no lo convierte en una `Piece`?
3. ¿Qué códigos de estado da axum sin que tu código los devuelva? ¿Por qué merece la pena probarlos?
4. ¿Por qué `post_creates_the_piece` mira dentro del catálogo además de comprobar el `201`?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Async con `#[tokio::test]`](../lab-01-async-con-tokio-test/README.md) | [Lab 03 - Clientes de reqwest con wiremock](../lab-03-clientes-reqwest-con-wiremock/README.md) → |

[↑ Semana 39](../../README.md)
