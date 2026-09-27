# Rúbrica de Evaluación - Semana 22

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | BDD, Gherkin, Behave, hooks y pytest-bdd |
| Desempeño | 40% | Ejercicios guiados con los bugs corregidos |
| Producto | 30% | Suite BDD del flujo principal del dominio |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica BDD como acuerdo de ejemplos, no como herramienta, y su relación con TDD.
- Distingue un escenario declarativo de uno imperativo.
- Explica el alcance de `context` y de cada hook, y por qué el estado mutable va en `before_scenario`.
- Compara cómo viaja el estado en Behave (`context`) y en pytest-bdd (fixtures).

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo correcto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** confunde BDD con escribir tests en Gherkin.

---

## 2) Desempeño (40%)

### Criterios

- Ejercicio 01: convierte los snippets en steps con parámetros y corrige el bug de la hora de cierre que encuentra el Scenario Outline.
- Ejercicio 02: explica por qué `before_feature` rompe el segundo escenario y lo corrige con `before_scenario`.
- Filtra escenarios con tags en Behave y en pytest-bdd.

### Niveles

- **Alto (36-40):** ejercicios completos con cada fallo explicado.
- **Medio (28-35):** ejercicios completos sin explicar alguno de los fallos.
- **Bajo (<28):** ejercicios incompletos o steps con valores literales.

---

## 3) Producto (30%)

### Criterios

- Al menos 3 features con 2 o más escenarios cada una, en Gherkin declarativo.
- Un `Esquema del escenario` con fronteras, un `Antecedentes` con tabla y un tag `@smoke` funcional.
- Steps reutilizables con parámetros; estado nuevo por escenario.
- `uv run behave` en verde sin pasos `undefined`.

### Niveles

- **Alto (27-30):** features que negocio podría leer y validar; steps reutilizados.
- **Medio (21-26):** cumple lo mínimo con escenarios imperativos o steps duplicados.
- **Bajo (<21):** menos de 3 features o suite en rojo.

---

## Condiciones de aprobación

1. Obtener mínimo 70% en cada tipo de evidencia.
2. Entregar proyecto ejecutable y coherente con el dominio asignado.
3. Mantener originalidad del trabajo y convenciones del bootcamp.
