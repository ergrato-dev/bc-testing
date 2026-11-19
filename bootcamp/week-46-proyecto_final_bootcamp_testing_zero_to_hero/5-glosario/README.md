# Glosario - Semana 46

| Término | Definición |
|---|---|
| Estrategia de testing | Documento que dice cómo se prueba un sistema y por qué: riesgos, capas, dónde corre cada prueba, criterios y decisiones. Dura lo que dura el repositorio. |
| Plan de pruebas | Documento que dice qué casos prueban qué requisitos de una entrega (semana 6). |
| Inventario de suites | Tabla con cada suite, sus capas, sus tests, su cobertura y su tiempo, sacada de una ejecución real. |
| Capa | Nivel de prueba según lo que el test usa: unitaria, API, integración con base de datos, E2E, rendimiento. |
| Riesgo del sistema | Algo que puede salir mal y que importa al usuario. Si aparece en varias suites, es un solo riesgo. |
| Riesgo abierto | Un riesgo que ninguna capa detecta todavía. Se escribe con su próximo paso. |
| Criterio de salida | Condición con un número y un comando que decide si una versión sale. |
| Umbral | El mínimo de un criterio, como el 85 % de cobertura, escrito en el proyecto de cada suite. |
| Decisión (ADR) | Registro de una elección con su contexto, la decisión, las alternativas y las consecuencias. |
| Badge | Imagen que GitHub genera con el estado del último run de un workflow en la rama principal. |
| `coverageThreshold` | Clave de configuración de Jest con el mínimo de cobertura; con `--coverage`, Jest falla si no se cumple. |
| Etiqueta (`git tag`) | Nombre fijo para un commit, como `v1.0.0`. La anotada (`-a`) lleva autor, fecha y mensaje. |
| Subshell | Copia de la shell que ejecuta lo que va entre paréntesis; su `cd` no afecta a las líneas siguientes. |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | — |

[↑ Semana 46](../README.md)
