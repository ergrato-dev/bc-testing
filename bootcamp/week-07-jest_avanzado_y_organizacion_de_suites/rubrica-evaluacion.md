# Rubrica de Evaluacion - Semana 07

> JavaScript Testing I: organizacion de suites, hooks y test doubles basicos

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

1. Cuando conviene usar `describe` anidado.
2. Diferencia entre `beforeAll` y `beforeEach`.
3. Diferencia entre `mock`, `stub` y `spy`.
4. Que ventaja da agrupar tests por metodo o comportamiento.
5. Por que el patron AAA mejora mantenibilidad.
6. Como ejecutar una sola suite desde terminal.
7. Que riesgos tiene el sobreuso de hooks globales.
8. Como detectar un test fragil por setup excesivo.
9. Cual es el rol de `jest.fn()` en aislamiento.
10. Que caracteriza un nombre de test profesional.

---

## Desempeno (40 pts)

### Ejercicio 01 - Organizacion de suite Jest (20 pts)

| Criterio | Pts |
|---|---|
| Estructura `describe` clara y escalable | 6 |
| Nombres `should ... when ...` consistentes | 4 |
| Patron AAA aplicado correctamente | 6 |
| Ejecuta la suite sin errores de sintaxis | 4 |
| **Total** | **20** |

### Ejercicio 02 - Hooks y mocks basicos (20 pts)

| Criterio | Pts |
|---|---|
| Uso correcto de hooks por alcance | 6 |
| Diferencia y aplica mock/stub/spy basico | 6 |
| Cubre happy path + caso invalido | 4 |
| Mantiene tests aislados e independientes | 4 |
| **Total** | **20** |

---

## Producto (30 pts)

### Proyecto semanal del dominio (suite modular)

| Criterio | Pts |
|---|---|
| Minimo 10 tests funcionales y ejecutables | 6 |
| Organizacion modular por comportamiento/metodo | 6 |
| Usa hooks de forma coherente | 5 |
| Incluye al menos 3 casos con doble de prueba | 5 |
| Nomenclatura profesional consistente | 4 |
| Evidencia de ejecucion con `yarn` o `pnpm` | 4 |
| **Total** | **30** |

---

## Penalizaciones

| Situacion | Penalizacion |
|---|---|
| Uso de `npm` en lugar de `pnpm`/`yarn` | -3 |
| Tests con nombres genericos (`test1`, `works`) | -2 c/u (max -8) |
| Tests sin assertion relevante | -2 c/u (max -8) |
| Dependencia de API/BD real en unit tests | -5 |
| Evidencia de copia de otro dominio | -30 |
