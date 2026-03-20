# Rubrica de Evaluacion - Semana 08

> JavaScript Testing II: matchers avanzados y parametrizacion en Jest

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

1. Cuando usar `toEqual` frente a `toStrictEqual`.
2. Diferencia entre `toContain` y `toContainEqual`.
3. En que casos usar `toMatchObject`.
4. Ventaja principal de `test.each`.
5. Como mejorar legibilidad en assertions de objetos complejos.
6. Cual es el riesgo de duplicar tests casi identicos.
7. Como interpretar salida de error en un matcher fallido.
8. Que rol cumple AAA cuando se usa parametrizacion.
9. Como nombrar una fila de `test.each` para que sea expresiva.
10. Que errores comunes aparecen al mezclar many assertions en un solo test.

---

## Desempeno (40 pts)

### Ejercicio 01 - Matchers avanzados (20 pts)

| Criterio | Pts |
|---|---|
| Usa matchers adecuados por tipo de dato | 6 |
| Diferencia correctamente igualdad profunda y estricta | 6 |
| Mantiene assertions legibles y enfocadas | 4 |
| Ejecuta la suite sin errores de sintaxis | 4 |
| **Total** | **20** |

### Ejercicio 02 - Parametrizacion con `test.each` (20 pts)

| Criterio | Pts |
|---|---|
| Implementa tabla de casos clara | 6 |
| Reduce duplicacion sin perder claridad | 6 |
| Incluye casos validos, invalidos y limite | 4 |
| Mantiene nomenclatura profesional | 4 |
| **Total** | **20** |

---

## Producto (30 pts)

### Proyecto semanal del dominio (suite parametrizada)

| Criterio | Pts |
|---|---|
| Minimo 10 tests funcionales y ejecutables | 6 |
| Usa `test.each` en al menos 2 bloques | 6 |
| Matchers apropiados y consistentes | 5 |
| Incluye validaciones y edge cases | 5 |
| Nombres de test claros y profesionales | 4 |
| Evidencia de ejecucion con `yarn` o `pnpm` | 4 |
| **Total** | **30** |

---

## Penalizaciones

| Situacion | Penalizacion |
|---|---|
| Uso de `npm` en lugar de `pnpm`/`yarn` | -3 |
| Tests duplicados sin parametrizacion | -2 c/u (max -8) |
| Assertions ambiguas o debiles | -2 c/u (max -8) |
| Dependencia de recursos externos en unit tests | -5 |
| Evidencia de copia de otro dominio | -30 |
