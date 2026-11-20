# Glosario - Semana 09

| Término | Definición |
|---|---|
| `.rejects` | Modificador de `expect` que espera a que la promesa se rechace y aplica el matcher al error. Lleva `await` delante |
| `.resolves` | Modificador de `expect` que espera a que la promesa se resuelva y aplica el matcher a su valor. Lleva `await` delante |
| `advanceTimersByTimeAsync(ms)` | Mueve el reloj falso `ms` milisegundos y ejecuta los timers que vencen, dejando pasar el código pendiente de cada `await` |
| `async` | Palabra que hace que una función devuelva siempre una promesa: su `return` la resuelve y su `throw` la rechaza |
| `await` | Espera a que una promesa termine: da su valor o lanza su error en esa línea |
| Backoff | Espera entre reintentos que crece en cada uno: 1 s, 2 s, 4 s... |
| `cause` | Campo de un `Error` con el error original que lo provocó: `new Error(mensaje, { cause: error })` |
| Debounce | Esperar a que algo deje de pasar (alguien escribiendo) durante un tiempo antes de actuar. Cada llamada reinicia la espera |
| `expect` suelto | Un `expect` de una promesa que el test no espera ni devuelve: se ejecuta cuando el test ya terminó |
| `expect.assertions(n)` | Hace fallar el test si no se ejecutan exactamente `n` `expect` |
| Fake timers | El reloj falso de Jest: sustituye `setTimeout`, `setInterval` y `Date` por versiones que solo avanzan cuando el test lo pide |
| `mockRejectedValue(error)` | Hace que un `jest.fn()` devuelva una promesa rechazada con `error` en cada llamada |
| `mockResolvedValue(valor)` | Hace que un `jest.fn()` devuelva una promesa resuelta con `valor` en cada llamada |
| Promesa | Objeto que representa un valor que llegará más tarde. Está pendiente, resuelta o rechazada |
| Rechazo sin manejar | Una promesa rechazada que nadie espera. Node termina el proceso con código 1 |
| Reintento | Volver a intentar una operación que falló, como enviar un aviso |
| `runAllTimersAsync()` | Ejecuta todos los timers pendientes del reloj falso, también los que se crean mientras tanto |
| `setTimeout(función, ms)` | Ejecuta la función al cabo de `ms` milisegundos |
| `testTimeout` | Tiempo máximo de un test, 5000 ms por defecto. Se cambia con `--testTimeout=ms` |
| `useFakeTimers()` y `useRealTimers()` | Activan y desactivan el reloj falso. Van en `beforeEach` y `afterEach` |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | [Semana 10 - JavaScript Testing IV: Mocks Avanzados en Jest](../../week-10-mocks_avanzados_en_jest/README.md) → |

[↑ Semana 09](../README.md)
