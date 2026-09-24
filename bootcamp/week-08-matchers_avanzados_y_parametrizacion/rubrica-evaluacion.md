# Rúbrica de Evaluación - Semana 08

> JavaScript Testing II: matchers avanzados y parametrización en Jest

---

## Distribución de Puntos

| Tipo de Evidencia | Peso | Puntos |
|---|---|---|
| Conocimiento | 30% | 30 pts |
| Desempeño | 40% | 40 pts |
| Producto | 30% | 30 pts |
| **Total** | **100%** | **100 pts** |

**Mínimo por componente**: 70% (21/30 - 28/40 - 21/30)

---

## Conocimiento (30 pts)

Cuestionario de 10 preguntas (3 pts c/u):

1. Cuándo usar `toEqual` frente a `toStrictEqual`.
2. Diferencia entre `toContain` y `toContainEqual`.
3. En que casos usar `toMatchObject`.
4. Ventaja principal de `test.each`.
5. Cómo mejorar legibilidad en assertions de objetos complejos.
6. Cuál es el riesgo de duplicar tests casi idénticos.
7. Cómo interpretar salida de error en un matcher fallido.
8. Qué rol cumple AAA cuando se usa parametrización.
9. Cómo nombrar una fila de `test.each` para que sea expresiva.
10. Qué errores comunes aparecen al mezclar many assertions en un solo test.

---

## Desempeño (40 pts)

### Ejercicio 01 - Matchers avanzados (20 pts)

| Criterio | Pts |
|---|---|
| Usa matchers adecuados por tipo de dato | 6 |
| Diferencia correctamente igualdad profunda y estricta | 6 |
| Mantiene assertions legibles y enfocadas | 4 |
| Ejecuta la suite sin errores de sintaxis | 4 |
| **Total** | **20** |

### Ejercicio 02 - Parametrización con `test.each` (20 pts)

| Criterio | Pts |
|---|---|
| Implementa tabla de casos clara | 6 |
| Reduce duplicación sin perder claridad | 6 |
| Incluye casos válidos, inválidos y límite | 4 |
| Mantiene nomenclatura profesional | 4 |
| **Total** | **20** |

---

## Producto (30 pts)

### Proyecto semanal del dominio (suite parametrizada)

| Criterio | Pts |
|---|---|
| Mínimo 10 tests funcionales y ejecutables | 6 |
| Usa `test.each` en al menos 2 bloques | 6 |
| Matchers apropiados y consistentes | 5 |
| Incluye validaciones y edge cases | 5 |
| Nombres de test claros y profesionales | 4 |
| Evidencia de ejecución con `pnpm` | 4 |
| **Total** | **30** |

---

## Penalizaciones

| Situación | Penalización |
|---|---|
| Uso de `npm` en lugar de `pnpm` | -3 |
| Tests duplicados sin parametrización | -2 c/u (max -8) |
| Assertions ambiguas o débiles | -2 c/u (max -8) |
| Dependencia de recursos externos en unit tests | -5 |
| Evidencia de copia de otro dominio | -30 |
