# Semana 22 - Python Testing VII: BDD con Behave

> **Etapa 2 - Testing con Python** | Semana 22 de 24

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Explicar qué es BDD y cómo nace un escenario de una conversación sobre reglas de negocio.
2. Escribir features en Gherkin (en español con `# language: es`): `Característica`, `Antecedentes`, `Escenario`, `Esquema del escenario` y `Ejemplos`.
3. Automatizarlos con Behave: step definitions con parámetros, `context`, tablas y tags.
4. Controlar el estado entre escenarios con hooks en `environment.py`.
5. Ejecutar el mismo feature con pytest-bdd y elegir herramienta según el equipo.

---

## Requisitos previos

- Semana 17: `parametrize` (el Scenario Outline es su equivalente en Gherkin).
- Semana 16: fixtures (pytest-bdd guarda el estado en fixtures).

---

## Distribución del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | BDD y Gherkin, Behave (steps, context, hooks), pytest-bdd | 2.5 h |
| Prácticas | Primer feature con Behave; hooks, tablas, tags y pytest-bdd | 3 h |
| Proyecto | Suite BDD del flujo principal del dominio | 2 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [BDD y Gherkin](./1-teoria/01-bdd-y-gherkin.md)
2. [Behave: steps, context y hooks](./1-teoria/02-behave-steps-context-y-hooks.md)
3. [pytest-bdd y cuándo usar BDD](./1-teoria/03-pytest-bdd-y-cuando-usar-bdd.md)

### Prácticas

- [Ejercicio 01 - Primer feature con Behave](./2-practicas/ejercicio-01-primer-feature-con-behave/)
- [Ejercicio 02 - Hooks, tablas, tags y pytest-bdd](./2-practicas/ejercicio-02-hooks-tablas-y-pytest-bdd/)

### Proyecto

- [Proyecto semanal: Suite BDD del flujo principal del dominio](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): al menos 3 features con varios escenarios y un Scenario Outline, ejecutables con `uv run behave`.

---

## Estructura de Carpetas

```text
week-22-bdd_con_python_behave/
|-- README.md
|-- rubrica-evaluacion.md
|-- 0-assets/
|   |-- 01-de-la-conversacion-al-feature.svg
|   |-- 02-estructura-y-hooks-behave.svg
|   `-- 03-behave-vs-pytest-bdd.svg
|-- 1-teoria/
|   |-- 01-bdd-y-gherkin.md
|   |-- 02-behave-steps-context-y-hooks.md
|   `-- 03-pytest-bdd-y-cuando-usar-bdd.md
|-- 2-practicas/
|   |-- ejercicio-01-primer-feature-con-behave/
|   `-- ejercicio-02-hooks-tablas-y-pytest-bdd/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/
|-- 4-recursos/
|   |-- ebooks-free/README.md
|   |-- videografia/README.md
|   `-- webgrafia/README.md
`-- 5-glosario/
    `-- README.md
```

---

## Versiones de la semana

`behave` 1.3.3, `pytest` 9.1.1 y `pytest-bdd` 8.1.0, fijadas en el `pyproject.toml` de cada carpeta.

> pytest-bdd 8.1.0 (su última versión, de diciembre de 2024) funciona con pytest 9 pero emite `PytestRemovedIn10Warning`: el ejercicio 02 filtra ese aviso y la teoría 03 explica el riesgo.

---

## Nota Importante

Un feature que solo lee el equipo técnico es un test con otra sintaxis. BDD aporta cuando los ejemplos se acuerdan con negocio y el feature se convierte en documentación que no puede quedar desactualizada.

---

## Navegación

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 21 - Coverage y calidad en Python](../week-21-coverage_y_calidad_en_python/README.md) | Semana 23 - Próximamente |
