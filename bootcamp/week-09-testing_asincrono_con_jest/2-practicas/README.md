# Prácticas - Semana 09

Tres labs (4 h en total) sobre un mismo proyecto, `~/labs-semana-09`: el servicio de reservas de butacas del planetario, ahora con una taquilla que responde con promesas y un aviso que se reintenta. El lab 01 crea la carpeta; los otros dos siguen sobre lo que dejó el anterior. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Tests que esperan](lab-01-tests-que-esperan/README.md) | Cinco tests en verde con una ejecución que falla: un `.then` y dos `.rejects` sin esperar, y un `await` olvidado en el código | 80 min |
| [Lab 02 - Errores asíncronos](lab-02-errores-asincronos/README.md) | Un `toThrow` que no ve el error de una función `async`, un `try/catch` que pasa sin comprobar nada y un error de la taquilla que el servicio se traga | 80 min |
| [Lab 03 - Reintentos con fake timers](lab-03-reintentos-con-fake-timers/README.md) | El reloj falso de Jest, los reintentos comprobados al milisegundo y un rechazo que llega mientras avanza el reloj | 80 min |

Cada lab termina con una comprobación (`pnpm test` con `grep`), un commit y una lista de repaso.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 03 - Fake timers: controlar el tiempo](../1-teoria/03-fake-timers.md) | [Lab 01 - Tests que esperan](lab-01-tests-que-esperan/README.md) → |

[↑ Semana 09](../README.md)
