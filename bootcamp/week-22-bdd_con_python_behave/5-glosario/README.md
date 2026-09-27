# Glosario Semana 22 - BDD con Python

## A

- **Antecedentes (Background)**: pasos comunes que se ejecutan antes de cada escenario del feature.

## B

- **BDD (Behavior Driven Development)**: práctica que acuerda el comportamiento con ejemplos concretos entre negocio, desarrollo y testing, y los automatiza.
- **Behave**: herramienta de BDD para Python que ejecuta features de Gherkin con step definitions.

## C

- **Característica (Feature)**: archivo `.feature` que agrupa los escenarios de una funcionalidad.
- **context**: objeto de Behave donde los steps comparten estado; lo asignado en un escenario se descarta al terminar.

## D

- **datatable**: argumento de pytest-bdd con la tabla del paso como lista de listas.

## E

- **Escenario (Scenario)**: ejemplo concreto de comportamiento con `Dado`, `Cuando` y `Entonces`.
- **Esquema del escenario (Scenario Outline)**: escenario que se ejecuta una vez por fila de `Ejemplos`.
- **environment.py**: archivo de Behave con los hooks (`before_scenario`, `after_feature`...).

## G

- **Gherkin**: lenguaje estructurado para escribir features; admite varios idiomas (`# language: es`).

## H

- **Hook**: función que Behave ejecuta en un momento del ciclo (antes o después de todo, de un feature, de un escenario o de un paso).

## P

- **pytest-bdd**: plugin que ejecuta features de Gherkin como tests de pytest, con fixtures en lugar de `context`.

## S

- **Snippet**: plantilla de step definition que Behave propone para un paso sin definir.
- **Step definition**: función de Python enlazada a una frase de Gherkin mediante un patrón (`{hour:d}`).

## T

- **Tag**: etiqueta (`@smoke`) para filtrar escenarios (`--tags=smoke` en Behave, `-m smoke` en pytest-bdd).
- **target_fixture**: opción de pytest-bdd que convierte lo que devuelve un paso en una fixture para los pasos siguientes.
- **Tres amigos**: reunión de negocio, desarrollo y testing para acordar ejemplos antes de programar.
