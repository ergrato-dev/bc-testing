# Semana 26 - Java Testing II: Mockito — Mocking en Java

> **Etapa 3 - Testing con Java** | Semana 26 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Configurar Mockito 5 con JUnit 6 en Maven, con Mockito cargado como agente de Java.
2. Sustituir las dependencias de un servicio por mocks y programar sus respuestas con `when` y `thenThrow`.
3. Comprobar las llamadas de un servicio con `verify`, matchers, `ArgumentCaptor` e `InOrder`.
4. Leer y corregir los errores de strict stubbing, y elegir entre `@Mock` y `@Spy`.
5. Hacer un mock de un método estático con `mockStatic` y explicar por qué suele indicar un problema de diseño.
6. Escribir tests con BDDMockito: `given`, `willReturn` y `then().should()`.

---

## Requisitos previos

- Semana 25: `@Nested`, extensiones de JUnit y AssertJ, y la suite Java de tu dominio.
- Semanas 10 y 18: dobles de prueba en Jest y en `unittest.mock` (aquí solo se ve la diferencia).
- JDK 21 y Maven 3.9 o superior. Comprueba con `java -version` y `mvn -v`.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Mocks y stubbing, `verify` y captores, strict stubs, spies y `mockStatic`, BDDMockito | 2.5 h |
| Prácticas | 4 labs: primer mock, `verify` y captores, strict stubs y spies, BDDMockito | 4 h |
| Proyecto | Dependencias del dominio aisladas con Mockito en el proyecto hilo conductor | 3 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Mocks y stubbing con Mockito](./1-teoria/01-mocks-y-stubbing.md)
2. [verify, matchers y ArgumentCaptor](./1-teoria/02-verify-y-captores.md)
3. [Strict stubbing, spies y mockStatic](./1-teoria/03-strict-stubs-spies-y-static.md)
4. [BDDMockito](./1-teoria/04-bddmockito.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Del stub a mano al primer mock](./2-practicas/lab-01-primer-mock/README.md)
- [Lab 02 - verify y ArgumentCaptor](./2-practicas/lab-02-verify-y-captores/README.md)
- [Lab 03 - Strict stubs, spies y mockStatic](./2-practicas/lab-03-strict-stubs-spy-y-static/README.md)
- [Lab 04 - BDDMockito](./2-practicas/lab-04-bddmockito/README.md)

### Proyecto

- [Aislar las dependencias del dominio con Mockito](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): rama `week-26` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 26
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-26-mockito_mocking_en_java/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-stubbing-y-verify.svg
|   `-- 02-mock-vs-spy.svg
|-- 1-teoria/
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-primer-mock/
|   |-- lab-02-verify-y-captores/
|   |-- lab-03-strict-stubs-spy-y-static/
|   `-- lab-04-bddmockito/
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

JDK 21, JUnit Jupiter 6.0.3, AssertJ 3.27.7, Mockito 5.23.0 (`mockito-core` y `mockito-junit-jupiter`), maven-dependency-plugin 3.11.0 y maven-surefire-plugin 3.5.6. Están fijadas en el `pom.xml` de cada carpeta y registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Un mock sin stub no falla: devuelve `0`, `false`, vacío o `null`. Si un test falla con un "no encontrado" o un cero que no esperas, revisa primero los stubs.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 25 - JUnit 6 Avanzado y AssertJ](../week-25-junit_6_avanzado_y_assertj/README.md) | [Semana 27 - TDD con Java](../week-27-tdd_con_java/README.md) |
