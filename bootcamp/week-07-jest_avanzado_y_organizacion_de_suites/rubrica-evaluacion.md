# Rúbrica de Evaluación - Semana 07

> JavaScript Testing I: organización de suites, hooks y test doubles básicos

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

1. Cuándo conviene usar `describe` anidado.
2. Diferencia entre `beforeAll` y `beforeEach`.
3. Diferencia entre `mock`, `stub` y `spy`.
4. Qué ventaja da agrupar tests por método o comportamiento.
5. Por qué el patrón AAA mejora mantenibilidad.
6. Cómo ejecutar una sola suite desde terminal.
7. Qué riesgos tiene el sobreuso de hooks globales.
8. Cómo detectar un test frágil por setup excesivo.
9. Cuál es el rol de `jest.fn()` en aislamiento.
10. Qué caracteriza un nombre de test profesional.

---

## Desempeño (40 pts)

### Ejercicio 01 - Organización de suite Jest (20 pts)

| Criterio | Pts |
|---|---|
| Estructura `describe` clara y escalable | 6 |
| Nombres `should ... when ...` consistentes | 4 |
| Patrón AAA aplicado correctamente | 6 |
| Ejecuta la suite sin errores de sintaxis | 4 |
| **Total** | **20** |

### Ejercicio 02 - Hooks y mocks básicos (20 pts)

| Criterio | Pts |
|---|---|
| Uso correcto de hooks por alcance | 6 |
| Diferencia y aplica mock/stub/spy básico | 6 |
| Cubre happy path + caso inválido | 4 |
| Mantiene tests aislados e independientes | 4 |
| **Total** | **20** |

---

## Producto (30 pts)

### Proyecto semanal del dominio (suite modular)

| Criterio | Pts |
|---|---|
| Mínimo 10 tests funcionales y ejecutables | 6 |
| Organización modular por comportamiento/método | 6 |
| Usa hooks de forma coherente | 5 |
| Incluye al menos 3 casos con doble de prueba | 5 |
| Nomenclatura profesional consistente | 4 |
| Evidencia de ejecución con `pnpm` | 4 |
| **Total** | **30** |

---

## Penalizaciones

| Situación | Penalización |
|---|---|
| Uso de `npm` en lugar de `pnpm` | -3 |
| Tests con nombres genéricos (`test1`, `works`) | -2 c/u (max -8) |
| Tests sin assertion relevante | -2 c/u (max -8) |
| Dependencia de API/BD real en unit tests | -5 |
| Evidencia de copia de otro dominio | -30 |
