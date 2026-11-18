# 01 - `#[tokio::test]` y tiempo controlado

> Lenguaje: **Rust**

Una función `async` de Rust no hace nada hasta que algo la ejecuta: devuelve un futuro, y hace falta un *runtime* que lo lleve hasta el final. En Python ese papel lo hacía el event loop de asyncio, que pytest-asyncio arrancaba por ti ([semana 19](../../week-19-testing_de_apis_con_python/1-teoria/03-testing-asincrono.md)). En Rust el runtime más usado es **tokio**, y `#[tokio::test]` crea uno para cada test.

---

## 🎯 Objetivos

- Escribir tests async con `#[tokio::test]` y saber qué runtime crean.
- Elegir entre el runtime de un hilo y `flavor = "multi_thread"`.
- Probar esperas, reintentos y plazos sin esperar de verdad, con `start_paused` y `tokio::time::advance`.

---

## Instalar tokio

Para los tests basta con tokio en `[dev-dependencies]`. Si la librería también usa el reloj de tokio, como en el [lab 01](../2-practicas/lab-01-async-con-tokio-test/README.md), va además en `[dependencies]`:

```bash
cargo add tokio@=1.54.1 --features macros,rt,time
cargo add --dev tokio@=1.54.1 --features macros,rt,rt-multi-thread,time,test-util
```

| Feature | Qué activa |
|---|---|
| `macros` | `#[tokio::test]`, `#[tokio::main]` y `tokio::join!` |
| `rt` | El runtime de un hilo, el que usa `#[tokio::test]` por defecto |
| `rt-multi-thread` | El runtime de varios hilos, para `flavor = "multi_thread"` |
| `time` | `sleep`, `timeout` e `Instant` |
| `test-util` | `start_paused`, `pause` y `advance`: el reloj de los tests. Solo en `[dev-dependencies]` |

tokio está dividido en features para que cada programa compile solo lo que usa. Cargo une las de las dos secciones al compilar los tests.

---

## 1. `#[tokio::test]`

```rust
#[tokio::test]
async fn first_attempt_succeeds() {
    let got = with_retry(3, || async { Ok::<_, String>("ficha") }).await;

    assert_eq!(got, Ok("ficha"));
}
```

La macro convierte el test en un `#[test]` normal que crea un runtime y ejecuta el cuerpo dentro con `block_on`. Cada test tiene su propio runtime: un test no hereda las tareas ni el reloj de otro.

Con `#[test]` solo, el compilador no sabe quién ejecutaría el futuro, y se niega:

```text
error: async functions cannot be used for tests
  --> src/lib.rs:77:5
   |
77 |       async fn first_attempt_succeeds() {
   |       ^----
   |       |
   |  _____`async` because of this
```

| Qué | Go (S32) | Python (S19) | Rust |
|---|---|---|---|
| Test de código concurrente | Un `func TestX(t *testing.T)` normal: las goroutines no necesitan nada especial | `async def test_x()` con `@pytest.mark.asyncio` | `async fn` con `#[tokio::test]` |
| Quién ejecuta el código async | El runtime de Go, siempre presente | El event loop que crea pytest-asyncio | El runtime de tokio que crea la macro |

---

## 2. Un hilo o varios

Por defecto, `#[tokio::test]` crea un runtime de **un solo hilo** (`current_thread`): todas las tareas se turnan en el hilo del test. Es lo más rápido de arrancar y hace los tests deterministas.

Algunas operaciones necesitan el runtime de varios hilos. La más común es `tokio::task::block_in_place`, que avisa a tokio de que un cálculo va a ocupar el hilo un rato para que mueva las otras tareas a otro. En un runtime de un hilo no hay adónde moverlas:

```text
thread 'tests::checksum_needs_a_multi_thread_runtime' (270388) panicked at src/lib.rs:63:5:
can call blocking only when running on the multi-threaded runtime
```

El test lo pide con `flavor`:

```rust
#[tokio::test(flavor = "multi_thread", worker_threads = 2)]
async fn checksum_needs_a_multi_thread_runtime() {
```

| Opción | Qué / Para qué / Impacto |
|---|---|
| `flavor = "multi_thread"` | Un runtime con varios hilos, como el de `#[tokio::main]` en producción. Hace falta para `block_in_place` y para provocar carreras entre tareas. Necesita la feature `rt-multi-thread` |
| `worker_threads = 2` | Cuántos hilos. Sin él, tokio crea uno por núcleo |

Si tu código funciona en un hilo, deja el test en un hilo: con varios, el orden de las tareas cambia de una ejecución a otra.

---

## 3. El reloj en pausa

Un reintento que espera 1 s y luego 2 s haría que su test tardara 3 s. Con diez tests así, la suite tarda medio minuto en esperar. tokio puede parar su reloj:

```rust
#[tokio::test(start_paused = true)]
async fn retries_wait_one_then_two_seconds() {
    // ...
    let start = Instant::now();
    let got = with_retry(3, op).await;

    assert_eq!(got, Ok(3));
    assert_eq!(start.elapsed(), Duration::from_secs(3));
}
```

Con `start_paused = true`, el reloj de tokio arranca parado. Cuando todas las tareas están esperando un `sleep`, tokio adelanta el reloj hasta el siguiente que vence: la espera no ocurre, pero el tiempo medido es exactamente el que habría pasado. El test pasa en `0.00s` y `elapsed()` vale `3s` justos.

Sin `start_paused`, el mismo test espera de verdad y la comparación exacta falla:

```text
assertion `left == right` failed
  left: 3.002472055s
 right: 3s
test result: FAILED. 0 passed; 1 failed; 0 ignored; 0 measured; 5 filtered out; finished in 3.00s
```

Es el `testing/synctest` de Go ([semana 35](../../week-35-concurrencia_y_calidad_en_go/1-teoria/01-race-detector-y-synctest.md#3-synctest-tiempo-virtual)): el reloj avanza solo cuando todo está bloqueado. La diferencia es qué reloj se para. synctest para el reloj de `time` dentro de la burbuja; tokio solo para **su** reloj: `tokio::time::sleep`, `timeout` e `Instant`. Un `std::thread::sleep` o un `std::time::Instant` siguen el reloj real. Por eso el código del lab usa `tokio::time::Instant`.

Dos restricciones:

- `start_paused` solo funciona con el runtime de un hilo. Con varios, el test no compila:

  ```text
  error: The `start_paused` option requires the `current_thread` runtime flavor. Use `#[tokio::test(flavor = "current_thread")]`
  ```

- Con el reloj parado, `timeout` y `tokio::join!` siguen funcionando, y el tiempo medido dice cuánto habrían durado. Dos consultas de 2 s en un `join!` miden 2 s, no 4: así se prueba que se ejecutan a la vez.

---

## 4. Mover el reloj a mano: `advance`

`start_paused` adelanta el reloj cuando el test espera. Para probar algo que mira la hora sin esperar, como una reserva que caduca a los 15 minutos, el test mueve el reloj él mismo:

```rust
#[tokio::test(start_paused = true)]
async fn hold_expires_after_fifteen_minutes() {
    let mut holds = Holds::default();
    holds.hold("MUS-001");

    tokio::time::advance(HOLD - Duration::from_secs(1)).await;
    assert!(holds.is_held("MUS-001"));

    tokio::time::advance(Duration::from_secs(1)).await;
    assert!(!holds.is_held("MUS-001"));
}
```

El test comprueba la frontera exacta: un segundo antes sigue reservada y en el segundo 900 caduca. Con el reloj real no podrías situarte en ese segundo.

`advance` sin el reloj parado entra en pánico, con un mensaje que apunta al código de tokio, no a tu test:

```text
thread 'tests::hold_expires_after_fifteen_minutes' (270530) panicked at /home/.../tokio-1.54.1/src/time/clock.rs:271:9:
time is not frozen
```

Si en lugar de `start_paused` prefieres parar el reloj a mitad del test, `tokio::time::pause()` hace lo mismo desde ese punto.

---

## ✅ Resumen

- `#[tokio::test]` crea un runtime por test, de un hilo por defecto. `#[test]` no acepta funciones `async`.
- `flavor = "multi_thread"` para lo que necesita varios hilos, como `block_in_place`.
- `start_paused = true` para el reloj: las esperas no ocurren, pero el tiempo medido es exacto. Solo con un hilo.
- `tokio::time::advance` para situarte en un instante concreto, como el último segundo de una reserva.
- El reloj de tokio no para `std::thread::sleep` ni `std::time::Instant`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Semana 39 - Async, APIs y BD en Rust](../README.md) | [Teoría — 02 - APIs con axum y clientes con wiremock](02-axum-oneshot-y-wiremock.md) → |

[↑ Semana 39](../README.md)
