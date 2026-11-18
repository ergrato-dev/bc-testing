# Lab 01 - Async con `#[tokio::test]`

## Objetivo

Probar código async del museo con `#[tokio::test]`: reintentos con pausa, consultas con límite de tiempo, consultas en paralelo, reservas que caducan y un cálculo que necesita el runtime de varios hilos. Las esperas no ocurren: el reloj de tokio está en pausa.

## Tiempo estimado

55 minutos.

## Preparación

```bash
cd starter
cargo test
#    Compiling tokio v1.54.1
#    Compiling lab01 v0.1.0 (.../starter)
#     Finished `test` profile [unoptimized + debuginfo] target(s) in 3.98s
# ...
# running 0 tests
# ...
```

La primera vez, `cargo` descarga tokio antes de compilar. Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `Cargo.toml` | El paquete `lab01`. tokio aparece dos veces: en `[dependencies]` con el reloj (`time`) y en `[dev-dependencies]` con lo que solo usan los tests, como `test-util` ([teoría 01](../../1-teoria/01-tokio-test-y-tiempo-controlado.md#instalar-tokio)) |
| `Cargo.lock` | Las versiones exactas de tokio y de sus dependencias |
| `src/lib.rs` | `with_retry`, `within`, `lookup`, `Holds` y `checksum`. Al final, los tests comentados por PASOS |

Lee `with_retry` antes de empezar: entre intentos espera 1 s, luego 2 s, luego 4 s. Con tres intentos, si los dos primeros fallan, espera 3 s en total.

## Paso a paso

### Paso 1: un test async (PASO 1)

1. Descomenta el bloque `// PASO 1`.
2. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 1 test
   # test tests::first_attempt_succeeds ... ok
   # ...
   ```

| Pieza | Qué hace |
|---|---|
| `#[tokio::test]` | Crea un runtime de tokio de un hilo y ejecuta el test dentro |
| `async fn first_attempt_succeeds()` | El test es async: puede usar `.await` |
| `|| async { Ok::<_, String>("ficha") }` | La operación que se reintenta: un closure que devuelve un futuro. `Ok::<_, String>` dice el tipo del error, que el compilador no puede deducir |

### Paso 2: el reloj en pausa (PASO 2)

1. Descomenta el bloque `// PASO 2`.
2. Ejecuta `cargo test`: pasan 2, en `0.00s`.

El test falla dos veces y acierta a la tercera. Con `start_paused = true`, el reloj de tokio está parado: cuando el código espera en `sleep`, tokio adelanta el reloj en lugar de esperar. `start.elapsed()` mide `3s` justos, y el test lo comprueba con `assert_eq!`.

`Instant` es `tokio::time::Instant`, no `std::time::Instant`: solo el reloj de tokio se para ([teoría 01](../../1-teoria/01-tokio-test-y-tiempo-controlado.md#3-el-reloj-en-pausa)).

### Paso 3: límites de tiempo y consultas en paralelo (PASO 3)

1. Descomenta el bloque `// PASO 3`.
2. Ejecuta `cargo test`: pasan 4.

- `slow_lookup_times_out`: `within` da 5 s a una consulta que tarda 10. `timeout` vence primero y el test recibe el `Err`, sin esperar los 5 s.
- `lookups_run_concurrently`: `tokio::join!` espera dos consultas de 2 s a la vez. El tiempo medido es 2 s, no 4: así se comprueba que van en paralelo. Con el reloj real, el test tardaría 2 s y la comparación exacta fallaría por milisegundos.

### Paso 4: mover el reloj a mano (PASO 4)

1. Descomenta el bloque `// PASO 4`.
2. Ejecuta `cargo test`: pasan 5.

`Holds` no espera nada: mira cuánto tiempo ha pasado desde la reserva. El test mueve el reloj con `tokio::time::advance` hasta un segundo antes de los 15 minutos (sigue reservada) y luego un segundo más (caducada). Es la frontera exacta, que con el reloj real no podrías alcanzar.

### Paso 5: el runtime de varios hilos (PASO 5)

1. Descomenta el bloque `// PASO 5`.
2. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 6 tests
   # test tests::first_attempt_succeeds ... ok
   # test tests::hold_expires_after_fifteen_minutes ... ok
   # test tests::retries_wait_one_then_two_seconds ... ok
   # test tests::lookups_run_concurrently ... ok
   # test tests::checksum_needs_a_multi_thread_runtime ... ok
   # test tests::slow_lookup_times_out ... ok
   #
   # test result: ok. 6 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
   ```

`checksum` usa `tokio::task::block_in_place`, que solo funciona en el runtime de varios hilos. El test lo pide con `flavor = "multi_thread", worker_threads = 2`. El orden de los tests en la salida cambia de una ejecución a otra: se ejecutan en paralelo.

## Experimentos

### Experimento 1: `#[test]` en una función async

1. En `first_attempt_succeeds`, cambia `#[tokio::test]` por `#[test]`.
2. Ejecuta `cargo test`:

   ```text
   error: async functions cannot be used for tests
     --> src/lib.rs:77:5
      |
   77 |       async fn first_attempt_succeeds() {
   ```

   `#[test]` no sabe ejecutar un futuro. Es el `async def functions are not natively supported` de pytest sin pytest-asyncio ([semana 19](../../../week-19-testing_de_apis_con_python/1-teoria/03-testing-asincrono.md#pytest-asyncio-strict-frente-a-auto)), con una diferencia: pytest lo descubría al ejecutar el test, y Rust al compilar.

3. Deshaz el cambio.

### Experimento 2: el reloj real

1. En `retries_wait_one_then_two_seconds`, cambia `#[tokio::test(start_paused = true)]` por `#[tokio::test]`.
2. Ejecuta:

   ```bash
   cargo test retries
   # thread 'tests::retries_wait_one_then_two_seconds' (365756) panicked at src/lib.rs:103:9:
   # assertion `left == right` failed
   #   left: 3.002448139s
   #  right: 3s
   # ...
   # test result: FAILED. 0 passed; 1 failed; 0 ignored; 0 measured; 5 filtered out; finished in 3.00s
   ```

   El test espera los 3 s de verdad, y el tiempo medido no es exacto: unos milisegundos más, distintos en cada ejecución. Con el reloj real solo podrías comprobar un intervalo (`>= 3s`), y cada test así suma segundos a la suite.

3. Deshaz el cambio.

### Experimento 3: `block_in_place` en un hilo

1. En `checksum_needs_a_multi_thread_runtime`, cambia la línea del atributo por `#[tokio::test]`.
2. Ejecuta:

   ```bash
   cargo test checksum
   # thread 'tests::checksum_needs_a_multi_thread_runtime' (366172) panicked at src/lib.rs:63:5:
   # can call blocking only when running on the multi-threaded runtime
   ```

   La línea 63 es la de `block_in_place`, dentro de `checksum`. En producción, `#[tokio::main]` usa el runtime de varios hilos y el código funciona; el test, con su runtime de un hilo, no. Si un test falla con este mensaje, el código está bien: al test le falta `flavor`.

3. Ahora pon `#[tokio::test(flavor = "multi_thread", start_paused = true)]` y ejecuta `cargo test`:

   ```text
   error: The `start_paused` option requires the `current_thread` runtime flavor. Use `#[tokio::test(flavor = "current_thread")]`
      --> src/lib.rs:148:59
   ```

   El reloj en pausa solo existe en el runtime de un hilo. Si un test necesita las dos cosas, sepáralo en dos.

4. Deshaz el cambio.

### Experimento 4: `advance` con el reloj en marcha

1. En `hold_expires_after_fifteen_minutes`, cambia `#[tokio::test(start_paused = true)]` por `#[tokio::test]`.
2. Ejecuta:

   ```bash
   cargo test hold
   # thread 'tests::hold_expires_after_fifteen_minutes' (366366) panicked at /home/.../tokio-1.54.1/src/time/clock.rs:271:9:
   # time is not frozen
   ```

   La ubicación es el código de tokio, no tu test: el nombre del test está en la cabecera del pánico. `advance` solo mueve un reloj parado.

3. Deshaz el cambio.

## Comprobación final

```bash
cargo test
# ...
# test result: ok. 6 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
# ...
```

## Preguntas de cierre

1. ¿Qué hace `#[tokio::test]` que `#[test]` no hace?
2. ¿Por qué el código del lab usa `tokio::time::Instant` y no `std::time::Instant`?
3. ¿Qué diferencia hay entre `start_paused = true` y `tokio::time::advance`? ¿Cuándo usarías cada uno?
4. Compara el reloj en pausa de tokio con `testing/synctest` de Go ([semana 35](../../../week-35-concurrencia_y_calidad_en_go/1-teoria/01-race-detector-y-synctest.md#3-synctest-tiempo-virtual)): ¿qué reloj para cada uno?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Rutas de axum con `oneshot`](../lab-02-rutas-axum-con-oneshot/README.md) → |

[↑ Semana 39](../../README.md)
