# Semana 05 - Primeros Tests Automatizados con Java

> **Etapa 0 - Fundamentos de Testing** | Semana 5 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Crear un proyecto Maven con JDK 21, JUnit 6 y AssertJ, y explicar cada parte de su `pom.xml`.
2. Escribir tests como métodos `@Test` con `@DisplayName`, agrupados en clases `@Nested`, ordenados en Arrange, Act y Assert y con nombres `should<Resultado>When<Condición>`.
3. Elegir la comprobación de cada valor: `assertEquals` (con delta para decimales), `assertNull`, `assertTrue`, `assertThrows` y sus equivalentes en AssertJ.
4. Leer un informe de Surefire y distinguir `Failure`, `Error` y un error de compilación.
5. Traducir una suite de pytest a JUnit y explicar en qué se comportan distinto.
6. Comprobar con mutaciones hechas a mano que un test en verde detecta los bugs.

---

## Requisitos previos

- Las semanas [3](../week-03-primeros_tests_automatizados_con_javascript/README.md) y [4](../week-04-primeros_tests_automatizados_con_python/README.md): sus proyectos, sus labs y su teoría, que esta semana compara pieza a pieza.
- Una terminal (en Windows, WSL), git y un editor de texto. VS Code es el que usan los ejemplos.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | JDK, Maven y JUnit 6; estructura de un test; assertions, fallos y ejecución | 2.5 h |
| Prácticas | 4 labs: primer proyecto, AAA y assertions, casos a tests, tests que mienten | 4 h |
| Proyecto | La suite de la semana 4 en JUnit | 3 h |
| Recursos | Lecturas y vídeos | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [JDK, Maven y JUnit 6](./1-teoria/01-setup-junit.md)
2. [Estructura de un test en JUnit 6](./1-teoria/02-estructura-test-junit.md)
3. [Assertions, fallos y ejecución](./1-teoria/03-assertions-y-ejecucion.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Primer proyecto con Maven](./2-practicas/lab-01-primer-proyecto-maven/README.md)
- [Lab 02 - AAA y assertions](./2-practicas/lab-02-aaa-y-assertions/README.md)
- [Lab 03 - De casos de prueba a tests](./2-practicas/lab-03-de-casos-a-tests/README.md)
- [Lab 04 - Tests que mienten](./2-practicas/lab-04-tests-que-mienten/README.md)

### Proyecto

- [La suite del dominio en JUnit](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto (único entregable obligatorio): `~/proyecto-semana-05` con tus métodos, su suite y `mutaciones.md`, por el canal que indique el instructor. Antes de entregarlo:

  ```bash
  PROYECTO=~/proyecto-semana-05 ./scripts/verificar-semana.sh 05
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-05-primeros_tests_automatizados_con_java/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-ciclo-junit.svg
|   `-- 02-patron-aaa-java.svg
|-- 1-teoria/
|   |-- 01-setup-junit.md
|   |-- 02-estructura-test-junit.md
|   `-- 03-assertions-y-ejecucion.md
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-primer-proyecto-maven/
|   |-- lab-02-aaa-y-assertions/
|   |-- lab-03-de-casos-a-tests/
|   `-- lab-04-tests-que-mienten/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/
|       |-- pom.xml
|       |-- mutaciones.md
|       |-- src/main/java/com/bootcamp/Domain.java
|       `-- src/test/java/com/bootcamp/DomainTest.java
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
| JDK | 21 | `maven.compiler.release` del `pom.xml` |
| Maven | 3.9.16 | La instalas tú |
| JUnit (Jupiter) | 6.0.3 | `<dependency>` de `junit-jupiter` |
| AssertJ | 3.27.7 | `<dependency>` de `assertj-core` |
| maven-surefire-plugin | 3.5.6 | `<plugin>` del `pom.xml` |

Las versiones de todo el curso están en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Con esta semana tienes la misma funcionalidad probada en JavaScript, Python y Java. La [semana 6](../week-06-plan_de_pruebas_y_suite_base_multilenguaje/README.md) cierra la etapa con un plan de pruebas y las tres suites: guarda los tres proyectos.

---

## Navegación

| ← Semana anterior | Siguiente → |
|---|---|
| [Semana 04 - Primeros Tests Automatizados con Python](../week-04-primeros_tests_automatizados_con_python/README.md) | [Semana 06 - Cierre Etapa 0: Plan de Pruebas y Suite Base Multilenguaje](../week-06-plan_de_pruebas_y_suite_base_multilenguaje/README.md) |
