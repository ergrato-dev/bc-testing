# Rubrica de Evaluacion - Semana 09

> JavaScript Testing III: testing asincrono con Jest

---

## Distribucion de Puntos

| Tipo de Evidencia | Peso | Puntos |
|---|---|---|
| Conocimiento | 30% | 30 pts |
| Desempeno | 40% | 40 pts |
| Producto | 30% | 30 pts |
| **Total** | **100%** | **100 pts** |

**Minimo por componente**: 70% (21/30 - 28/40 - 21/30)

---

## Conocimiento (30 pts)

Cuestionario de 10 preguntas (3 pts c/u):

1. Diferencia entre retornar Promise y usar async/await en tests.
2. Cuando usar `await expect(promise).resolves`.
3. Cuando usar `await expect(promise).rejects`.
4. Error tipico al olvidar `await` en un test asincrono.
5. Como funcionan fake timers en Jest.
6. Cuando conviene `advanceTimersByTime`.
7. Como testear una funcion con retry por timeout.
8. Que significa que un test asincrono sea flaky.
9. Como limpiar timers y mocks entre casos.
10. Como leer un fallo asincrono en stack trace.

---

## Desempeno (40 pts)

### Ejercicio 01 - Promesas y async/await (20 pts)

| Criterio | Pts |
|---|---|
| Tests asincronos esperan correctamente la promesa | 6 |
| Diferencia casos `resolves` y `rejects` | 6 |
| Nombres descriptivos y AAA claro | 4 |
| Suite ejecuta sin errores de sincronizacion | 4 |
| **Total** | **20** |

### Ejercicio 02 - Fake timers y reintentos (20 pts)

| Criterio | Pts |
|---|---|
| Configura y restaura fake timers correctamente | 6 |
| Valida reintentos con control de tiempo | 6 |
| Cubre caso exitoso y fallo final | 4 |
| Mantiene tests deterministas | 4 |
| **Total** | **20** |

---

## Producto (30 pts)

### Proyecto semanal del dominio (suite asincrona)

| Criterio | Pts |
|---|---|
| Minimo 10 tests funcionales y ejecutables | 6 |
| Incluye al menos 4 tests asincronos robustos | 6 |
| Usa `resolves/rejects` cuando corresponde | 5 |
| Controla timers/retries si aplica | 5 |
| Nomenclatura profesional y legibilidad | 4 |
| Evidencia de ejecucion con `yarn` o `pnpm` | 4 |
| **Total** | **30** |

---

## Penalizaciones

| Situacion | Penalizacion |
|---|---|
| Uso de `npm` en lugar de `pnpm`/`yarn` | -3 |
| Tests asincronos sin `await/return` correcto | -2 c/u (max -8) |
| Tests flaky por timers mal gestionados | -2 c/u (max -8) |
| Dependencia de servicios externos reales | -5 |
| Evidencia de copia de otro dominio | -30 |
