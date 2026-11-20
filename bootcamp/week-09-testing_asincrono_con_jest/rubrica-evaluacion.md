# Rúbrica de Evaluación - Semana 09

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Promesas, `async` y `await`, lo que hace Jest con un `expect` suelto, `.resolves` y `.rejects`, `expect.assertions` y fake timers |
| Desempeño | 40% | Labs 01 a 03: los tests del servicio que esperan, los errores de `cancel` y los reintentos con el reloj falso |
| Producto | 30% | El aviso del dominio asíncrono con reintentos, y su suite de JS esperando cada promesa sin tiempo real |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica qué devuelve una función `async` y qué hace `await` con una promesa resuelta y con una rechazada.
- Explica por qué un `.then` o un `expect(...).rejects` sin `await` ni `return` deja el resumen en verde y el código de salida en 1.
- Explica por qué `expect(() => f()).toThrow()` no ve el error de una función `async`, y por qué un `try/catch` sin `expect.assertions` puede pasar sin comprobar nada.
- Explica por qué un test que espera tiempo real es un problema y qué hace el reloj falso.
- Explica por qué `advanceTimersByTime` no ve un reintento que `advanceTimersByTimeAsync` sí ve, y por qué un rechazo durante `runAllTimersAsync` hace fallar el test.

### Niveles

- **Alto (27-30):** responde con ejemplos propios y predice, antes de ejecutar, cuántas llamadas habrá a cada milisegundo.
- **Medio (21-26):** comprende los conceptos con alguna confusión menor (por ejemplo, entre `return` y `await` en un test).
- **Bajo (<21):** no distingue una promesa de su valor o deja `expect` de promesas sin esperar.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: el error tras el resumen explicado (el 4 de otro test); el `.then` y los dos `.rejects` esperados; el `await` olvidado encontrado con `Promise { <pending> }` y corregido; el límite de las últimas butacas con `.resolves`.
- Lab 02: `cancel` comprobado con `.rejects` por mensaje, clase y código; el `try/catch` demostrado con `expect.assertions` y reescrito; el error tragado corregido con `cause`; los dos fallos de `cause` comparados.
- Lab 03: la espera real medida; el reloj falso en `beforeEach` y `afterEach`; los dos reintentos comprobados al milisegundo; el bug de la espera encontrado y corregido; el rechazo preparado antes de mover el reloj; 15 tests en menos de un segundo.

### Niveles

- **Alto (36-40):** los tres labs completos, explicando cada fallo antes de arreglarlo.
- **Medio (28-35):** los tres labs completos, con algún paso hecho sin entender por qué fallaba.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- `PROYECTO=... ./scripts/verificar-semana.sh 09` pasa las 14 comprobaciones.
- El notificador devuelve una promesa, y el servicio la espera con `await sendWithRetry(` dentro de `notify`.
- Cada test del servicio espera lo que comprueba; los errores se comprueban con `await expect(...).rejects`.
- Los reintentos se prueban con fake timers, en los límites (999 y 1000 ms; 2999 y 3000 ms), y la suite pasa con `--testTimeout=1000`.
- El plan tiene la regla nueva, sus tres casos con su motivo en la sección 7, y en la sección 8 lo que pasó con los tests de la semana 8.

### Niveles

- **Alto (27-30):** todo lo anterior, con un hallazgo propio al volver asíncrono el servicio (un test que pasaba sin esperar, un error tragado).
- **Medio (21-26):** pasan las comprobaciones, pero algún caso no mira el límite del tiempo o algún nombre aún dice `should throw`.
- **Bajo (<21):** no pasan las comprobaciones.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto - Suite asíncrona con Jest](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 09](README.md)
