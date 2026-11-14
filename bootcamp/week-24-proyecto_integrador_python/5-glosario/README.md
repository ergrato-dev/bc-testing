# Glosario Semana 24 - Proyecto integrador Python

## A

- **`--append`**: opción de `coverage run` que suma la medición al `.coverage` existente en lugar de empezar de cero.

## C

- **capa**: grupo de tests con la misma clase de dependencias y el mismo coste: unitaria, API, repositorio o BDD.
- **cobertura combinada**: una sola cifra de cobertura que suma lo que miden varios programas, como pytest y Behave.
- **`coverage run`**: ejecuta un programa de Python midiendo qué líneas y ramas se ejecutan.

## D

- **deselected**: test que pytest recoge pero no ejecuta porque no cumple la expresión de `-m`.
- **`--durations`**: opción de pytest que lista los pasos más lentos, separados en *setup*, *call* y *teardown*.

## F

- **`fail_under`**: umbral de coverage; por debajo, `coverage report` sale con código 2.
- **`--format=markdown`**: opción de `coverage report` que imprime la tabla en Markdown.

## H

- **hook**: función con un nombre que pytest reconoce y llama en un momento concreto, como `pytest_collection_modifyitems`.

## R

- **reporte de testing**: documento con el estado de la suite medido en una ejecución real: capas, cobertura, tests lentos, riesgos y próximos pasos.

## S

- **`source`**: opción de coverage que limita la medición a los paquetes de la aplicación.
- **`--strict-markers`**: opción de pytest que convierte en error una marca no registrada.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | — |

[↑ Semana 24](../README.md)
