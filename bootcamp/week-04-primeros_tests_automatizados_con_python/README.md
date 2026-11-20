# Semana 04 - Primeros Tests Automatizados con Python

> **Etapa 0 - Fundamentos de Testing** | Semana 4 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Crear un proyecto de tests con uv, Python 3.14 y pytest, y explicar cada archivo de configuración.
2. Escribir tests como funciones `test_*` y clases `Test*`, ordenados en Arrange, Act y Assert y con nombres `test_<resultado>_when_<condición>`.
3. Elegir la comparación de cada valor: `==`, `is None`, `is True`, `pytest.approx` y `pytest.raises`.
4. Leer un informe de pytest y distinguir `FAILED` de `ERROR` y sus códigos de salida.
5. Traducir una suite de Jest a pytest y explicar en qué se comportan distinto.
6. Comprobar con mutaciones hechas a mano que un test en verde detecta los bugs.

---

## Requisitos previos

- La [semana 3](../week-03-primeros_tests_automatizados_con_javascript/README.md): su proyecto, sus labs y su teoría, que esta semana compara pieza a pieza.
- Una terminal (en Windows, WSL), git y un editor de texto. VS Code es el que usan los ejemplos.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | uv y pytest; estructura de un test; assertions, fallos y ejecución | 2.5 h |
| Prácticas | 4 labs: primer proyecto, AAA y asserts, casos a tests, tests que mienten | 4 h |
| Proyecto | La suite de la semana 3 en pytest | 3 h |
| Recursos | Lecturas y vídeos | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Python, uv y pytest](./1-teoria/01-setup-pytest.md)
2. [Estructura de un test en pytest](./1-teoria/02-estructura-test-pytest.md)
3. [Assertions, fallos y ejecución](./1-teoria/03-assertions-y-ejecucion.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Primer proyecto con pytest](./2-practicas/lab-01-primer-proyecto-pytest/README.md)
- [Lab 02 - AAA y asserts](./2-practicas/lab-02-aaa-y-asserts/README.md)
- [Lab 03 - De casos de prueba a tests](./2-practicas/lab-03-de-casos-a-tests/README.md)
- [Lab 04 - Tests que mienten](./2-practicas/lab-04-tests-que-mienten/README.md)

### Proyecto

- [La suite del dominio en pytest](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto (único entregable obligatorio): `~/proyecto-semana-04` con tus funciones, su suite y `mutaciones.md`, por el canal que indique el instructor. Antes de entregarlo:

  ```bash
  PROYECTO=~/proyecto-semana-04 ./scripts/verificar-semana.sh 04
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-04-primeros_tests_automatizados_con_python/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-ciclo-pytest.svg
|   `-- 02-patron-aaa-python.svg
|-- 1-teoria/
|   |-- 01-setup-pytest.md
|   |-- 02-estructura-test-pytest.md
|   `-- 03-assertions-y-ejecucion.md
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-primer-proyecto-pytest/
|   |-- lab-02-aaa-y-asserts/
|   |-- lab-03-de-casos-a-tests/
|   `-- lab-04-tests-que-mienten/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/
|       |-- .python-version
|       |-- pyproject.toml
|       |-- uv.lock
|       |-- mutaciones.md
|       |-- src/domain.py
|       `-- tests/test_domain.py
|-- 4-recursos/
|   |-- ebooks-free/README.md
|   |-- videografia/README.md
|   `-- webgrafia/README.md
`-- 5-glosario/
    `-- README.md
```

---

## Versiones de la semana

| Herramienta | Versión | Dónde se fija |
|---|---|---|
| Python | 3.14 | `.python-version` y `requires-python` |
| uv | 0.13 o posterior | La instalas tú |
| pytest | 9.1.1 | `[dependency-groups] dev` de `pyproject.toml` |

Las versiones de todo el curso están en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

La [semana 5](../week-05-primeros_tests_automatizados_con_java/README.md) escribe la misma suite en Java con JUnit 6. Guarda tus dos proyectos: compararás los tres lenguajes.

---

## Navegación

| ← Semana anterior | Siguiente → |
|---|---|
| [Semana 03 - Primeros Tests Automatizados con JavaScript](../week-03-primeros_tests_automatizados_con_javascript/README.md) | [Semana 05 - Primeros Tests Automatizados con Java](../week-05-primeros_tests_automatizados_con_java/README.md) |
