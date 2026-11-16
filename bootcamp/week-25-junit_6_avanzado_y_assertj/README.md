# Semana 25 - Java Testing I: JUnit 6 Avanzado y AssertJ

> **Etapa 3 - Testing con Java** | Semana 25 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Sustituir tests repetidos por tests parametrizados con `@CsvSource`, `@ValueSource`, `@EnumSource` y `@MethodSource`, y nombrar cada ejecución.
2. Organizar una suite por contextos con `@Nested` y ejecutar solo una parte con `@Tag`.
3. Probar código que escribe archivos con `@TempDir`, y escribir y registrar una extensión de JUnit.
4. Escribir aserciones legibles con AssertJ sobre colecciones, `Optional` y objetos.
5. Comprobar varios campos de un resultado de una vez con soft assertions.

---

## Requisitos previos

- Semana 5: JUnit, Maven y las primeras aserciones de AssertJ.
- Semana 6: la suite Java de tu dominio (la refactorizas en el proyecto).
- Semanas 8 y 17: parametrización en Jest y pytest (aquí solo se ve la diferencia).
- JDK 21 y Maven 3.9 o superior. Comprueba con `java -version` y `mvn -v`.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Tests parametrizados, `@Nested` y `@Tag`, `@TempDir` y extensiones, AssertJ y soft assertions | 2.5 h |
| Prácticas | 4 labs: parametrizados, `@Nested`/`@Tag`, extensiones, AssertJ | 4 h |
| Proyecto | Suite Java del dominio en el proyecto hilo conductor | 3 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Tests parametrizados con JUnit 6](./1-teoria/01-tests-parametrizados.md)
2. [Organizar la suite con @Nested y @Tag](./1-teoria/02-nested-y-tags.md)
3. [@TempDir y extensiones de JUnit](./1-teoria/03-tempdir-y-extensiones.md)
4. [AssertJ a fondo y soft assertions](./1-teoria/04-assertj-y-soft-assertions.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Tests parametrizados](./2-practicas/lab-01-tests-parametrizados/README.md)
- [Lab 02 - @Nested y @Tag](./2-practicas/lab-02-nested-y-tags/README.md)
- [Lab 03 - @TempDir y extensiones](./2-practicas/lab-03-extensiones-y-tempdir/README.md)
- [Lab 04 - AssertJ y soft assertions](./2-practicas/lab-04-assertj-y-soft-assertions/README.md)

### Proyecto

- [Suite Java del dominio con JUnit 6 y AssertJ](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): rama `week-25` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 25
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-25-junit_6_avanzado_y_assertj/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-ciclo-de-vida-nested.svg
|   `-- 02-hard-vs-soft-assertions.svg
|-- 1-teoria/
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-tests-parametrizados/
|   |-- lab-02-nested-y-tags/
|   |-- lab-03-extensiones-y-tempdir/
|   `-- lab-04-assertj-y-soft-assertions/
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

JDK 21, JUnit Jupiter 6.0.3, AssertJ 3.27.7 y maven-surefire-plugin 3.5.6. Están fijadas en el `pom.xml` de cada carpeta y registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Un test parametrizado o una clase `@Nested` mal escritos no siempre dan error: a veces simplemente no se ejecutan. Compara siempre el `Tests run:` final con el número que esperabas.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 24 - Proyecto Integrador: Etapa 2 (Python)](../week-24-proyecto_integrador_python/README.md) | [Semana 26 - Mockito — Mocking en Java](../week-26-mockito_mocking_en_java/README.md) |
