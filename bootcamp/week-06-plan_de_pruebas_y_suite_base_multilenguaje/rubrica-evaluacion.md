# Rúbrica de Evaluación - Semana 06

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Plan de pruebas, prioridad por riesgo, trazabilidad hasta el test, criterios de salida comprobables, `set -euo pipefail` y diferencias entre lenguajes |
| Desempeño | 40% | Labs 01 y 02: plan del planetario y `verify.sh` con los huecos cerrados |
| Producto | 30% | Proyecto con las tres suites del dominio, su plan y `verify.sh` en verde, y la presentación |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica para qué sirve cada una de las nueve secciones del plan y por qué se declara lo que queda fuera.
- Calcula la prioridad de un riesgo con la tabla de impacto y probabilidad, y justifica ambos valores.
- Explica qué significan ✅ y — en la matriz, y distingue un caso sin test, un test sin caso y una regla sin caso.
- Convierte un criterio de salida vago («cuando esté listo») en uno comprobable.
- Explica por qué un script sin `set -e` puede terminar con 0 aunque falle una suite, y qué añade `-o pipefail` en `mvn test | grep`.
- Explica por qué una mutación en el código de JavaScript no pone en rojo pytest ni JUnit.
- Da un ejemplo de entrada que tiene veredictos distintos en JavaScript y Python, y explica por qué.

### Niveles

- **Alto (27-30):** responde con ejemplos de su propio dominio y relaciona cada sección del plan con una comprobación de `verify.sh`.
- **Medio (21-26):** comprende los conceptos con alguna confusión menor (por ejemplo, una prioridad que no sale de la tabla).
- **Bajo (<21):** cree que tres suites en verde prueban lo mismo, o no distingue un criterio comprobable de uno vago.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: `plan-de-pruebas.md` del planetario con alcance, cuatro riesgos con su prioridad, una matriz de 10 casos con ✅ y —, el motivo del — de TC-PLAN-010, criterios de salida comprobables y los seis huecos; tres suites en verde y dos commits.
- Lab 02: `verify.sh` con los cuatro bloques descomentados; la mutación que termina con 0 sin `set -e` y con 1 con él; los seis huecos encontrados por el script y cerrados; la diferencia de `5.0` en la sección 7; la evidencia en la sección 9 y `./verify.sh` terminando con 0.

### Niveles

- **Alto (36-40):** los dos labs completos, con cada valor de impacto y probabilidad justificado y cada hueco cerrado con el test, no solo con el ID.
- **Medio (28-35):** los dos labs completos, con algún riesgo sin justificar o algún criterio de salida que no se puede comprobar.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- `PROYECTO=~/proyecto-semana-06 ./scripts/verificar-semana.sh 06` pasa las 11 comprobaciones.
- Las reglas son las de la semana 2 más las que implementan las funciones, y lo que no tiene función está fuera del alcance con su motivo.
- Cada riesgo tiene impacto y probabilidad justificados, y la prioridad de cada caso es la de su riesgo.
- Cada test de los tres lenguajes lleva el ID de un caso de la matriz, y cada — tiene su motivo en la sección 7.
- La sección 8 recoge los huecos encontrados y lo que se hizo con cada uno.
- La presentación de 5 minutos muestra el riesgo principal, los huecos y una diferencia entre lenguajes con datos del plan.

### Niveles

- **Alto (27-30):** todo lo anterior, con al menos una diferencia de veredicto entre lenguajes encontrada al comparar y resuelta como bug o como pregunta abierta.
- **Medio (21-26):** pasan las comprobaciones, pero los riesgos no se justifican o la matriz solo recoge los casos que ya tenían test.
- **Bajo (<21):** no pasan las comprobaciones.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto - Plan de pruebas y suite base en tres lenguajes](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 06](README.md)
