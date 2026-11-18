# Lab 03 - Clientes de reqwest con wiremock

## Objetivo

Probar un cliente HTTP escrito con reqwest contra un servidor simulado con wiremock: una respuesta correcta, los estados de error, un JSON roto, la petición que el cliente debe enviar y un servidor que no contesta a tiempo. Es el cliente `openlib` del [lab 02 de la semana 34](../../../week-34-http_bd_e_integracion_en_go/2-practicas/lab-02-clientes-con-httptest-newserver/README.md), en Rust.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
cargo test
#    Compiling tokio v1.54.1
#    Compiling reqwest v0.13.5
#    Compiling wiremock v0.6.5
#    Compiling lab03 v0.1.0 (.../starter)
#     Finished `test` profile [unoptimized + debuginfo] target(s) in 12.03s
# ...
# running 0 tests
# ...
```

Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `Cargo.toml` | El paquete `lab03`: reqwest sin TLS (`default-features = false`) y con `json`, y serde. wiremock, tokio y serde_json en `[dev-dependencies]` |
| `Cargo.lock` | Las versiones exactas |
| `src/lib.rs` | `Client::lookup`, que pide `GET {base_url}/isbn/{isbn}`, y `LookupError`. Al final, los tests comentados por PASOS |

Lee `lookup`:

| Respuesta del catálogo | Resultado |
|---|---|
| `200` con un libro en JSON | `Ok(Book)` |
| `200` con un cuerpo que no es un libro | `Err(Invalid(...))` |
| `404` | `Err(NotFound)` |
| Cualquier otro estado | `Err(Unavailable(estado))` |
| Nada antes del plazo del cliente | `Err(Timeout)` |

El cliente recibe la URL base en `Client::new`. En producción es la del catálogo real; en los tests, la del servidor de wiremock. Es la misma costura que en Go: sin ella, no podrías apuntar el cliente a otro sitio.

## Paso a paso

### Paso 1: un servidor simulado con una respuesta fija (PASO 1)

1. Descomenta el bloque `// PASO 1`.
2. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 1 test
   # test tests::lookup_reads_the_book ... ok
   # ...
   ```

| Línea | Qué hace |
|---|---|
| `MockServer::start().await` | Arranca un servidor HTTP en `127.0.0.1` y un puerto libre |
| `Mock::given(method("GET")).and(path(...))` | Este mock acepta los `GET` a esa ruta |
| `.respond_with(ResponseTemplate::new(200).set_body_json(json!(...)))` | Responde `200` con ese JSON y `Content-Type: application/json` |
| `.mount(&server).await` | Registra el mock en el servidor |
| `client(&server)` | Un cliente con `server.uri()` como URL base y un plazo de 200 ms |

El servidor se para cuando `server` sale de su ámbito, al final del test.

### Paso 2: estados de error y un JSON roto (PASO 2)

1. Descomenta el bloque `// PASO 2`.
2. Ejecuta `cargo test`: pasan 4.

`respond` monta un servidor que responde lo mismo a cualquier `GET` y hace la consulta. Cada test solo cambia la respuesta. `broken_json_is_invalid` usa `matches!` porque el texto del error lo escribe reqwest y no interesa fijarlo; el `"{got:?}"` del final es el mensaje del `assert!` si falla.

### Paso 3: la petición que el servidor debe recibir (PASO 3)

1. Descomenta el bloque `// PASO 3`.
2. Ejecuta `cargo test`: pasan 5.

Este mock solo acepta peticiones con la cabecera `accept: application/json`, y `.expect(1)` exige que llegue exactamente una. wiremock lo comprueba al destruir el servidor, al final del test. En Go hacía falta un contador en el handler de prueba y un `if` al final.

### Paso 4: un servidor que no contesta a tiempo (PASO 4)

1. Descomenta el bloque `// PASO 4`.
2. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 6 tests
   # ...
   # test tests::slow_server_times_out ... ok
   #
   # test result: ok. 6 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.20s
   ```

`set_delay` hace que wiremock espere 2 s antes de responder. El cliente se rinde a los 200 ms y `classify` convierte el error de reqwest en `Timeout` gracias a `e.is_timeout()`. La suite tarda 0.20 s: este test espera de verdad. El reloj en pausa del [lab 01](../lab-01-async-con-tokio-test/README.md) no sirve aquí, porque la respuesta viaja por un socket real.

## Experimentos

### Experimento 1: la cabecera que se olvida

1. En `lookup`, borra la línea `.header("Accept", "application/json")`.
2. Ejecuta:

   ```bash
   cargo test sends_accept
   # thread 'tests::sends_accept_header_once' (368257) panicked at /home/.../wiremock-0.6.5/src/mock_server/exposed_server.rs:367:17:
   # Verifications failed:
   # - Mock #0.
   # 	Expected range of matching incoming requests: == 1
   # 	Number of matched incoming requests: 0
   #
   # Received requests:
   # - Request #1
   # 	GET http://localhost/isbn/9788491050278
   # accept: */*
   # host: 127.0.0.1:38783
   ```

   Fíjate en lo que **no** falla: el `assert_eq!` del test pasa. Sin la cabecera, ningún mock acepta la petición, wiremock responde `404`, y el cliente devuelve `NotFound`, que es justo lo que el test esperaba. Lo detecta `expect(1)`, que además enseña la petición que llegó: `accept: */*`, el valor que reqwest pone por defecto.

3. Deshaz el cambio.

### Experimento 2: un 404 tratado como caída

1. En `lookup`, borra la línea `StatusCode::NOT_FOUND => Err(LookupError::NotFound),`.
2. Ejecuta:

   ```bash
   cargo test
   # test tests::not_found_is_its_own_error ... FAILED
   # test tests::sends_accept_header_once ... FAILED
   # ...
   # thread 'tests::not_found_is_its_own_error' (368575) panicked at src/lib.rs:122:9:
   # assertion `left == right` failed
   #   left: Err(Unavailable(404))
   #  right: Err(NotFound)
   # ...
   # test result: FAILED. 4 passed; 2 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.20s
   ```

   Un ISBN que no existe y un catálogo caído son cosas distintas para quien llama: el primero se le dice al usuario, el segundo se reintenta. El test que separa los dos casos evita que se confundan.

3. Deshaz el cambio.

### Experimento 3: el plazo del cliente

1. En `client`, cambia `Duration::from_millis(200)` por `Duration::from_secs(5)`.
2. Ejecuta:

   ```bash
   cargo test slow
   # thread 'tests::slow_server_times_out' (382036) panicked at src/lib.rs:166:9:
   # assertion `left == right` failed
   #   left: Err(Invalid("error decoding response body for url (http://127.0.0.1:33631/isbn/9788491050278)"))
   #  right: Err(Timeout)
   # ...
   # test result: FAILED. 0 passed; 1 failed; 0 ignored; 0 measured; 5 filtered out; finished in 2.00s
   ```

   Ahora el cliente espera los 2 s del servidor y recibe un `200` sin cuerpo: el error es `Invalid`, no `Timeout`. El test tarda 2 s.

3. Deshaz el cambio.

Un cliente sin plazo esperaría lo que el servidor quisiera. El test de `set_delay` es el que comprueba que el plazo existe.

## Comprobación final

```bash
cargo test
# ...
# test result: ok. 6 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.20s
# ...
```

## Preguntas de cierre

1. ¿Qué responde wiremock a una petición que ningún mock acepta? ¿Qué riesgo tiene para un test que espera un `404`?
2. ¿Cuándo comprueba wiremock un `expect(1)`? Compáralo con el `times(1)` de mockall ([semana 38](../../../week-38-traits_dobles_y_parametrizacion_en_rust/1-teoria/02-mocks-con-mockall.md#los-dos-fallos-de-un-mock)).
3. ¿Por qué el test del plazo espera de verdad, aunque el lab 01 consiguió esperas instantáneas?
4. ¿Qué diferencia hay entre simular el servicio con wiremock y sustituir el cliente por un mock de un trait?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Rutas de axum con `oneshot`](../lab-02-rutas-axum-con-oneshot/README.md) | [Lab 04 - PostgreSQL con `#[sqlx::test]` y testcontainers](../lab-04-postgresql-con-sqlx-test-y-testcontainers/README.md) → |

[↑ Semana 39](../../README.md)
