# Semana 28 - Java Testing IV: Testing de Spring Boot

> **Etapa 3 - Testing con Java** | Semana 28 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Explicar qué arranca cada anotación de test de Spring Boot y elegir la mínima que prueba lo que necesitas.
2. Probar un controlador con `@WebMvcTest`, `MockMvc`, `MockMvcTester` y `@MockitoBean`.
3. Probar el contrato de errores de una API: validación (400), reglas del dominio (409, 422) y `ProblemDetail`.
4. Fijar el contrato JSON con `@JsonTest` y `JacksonTester`.
5. Probar la aplicación entera con `@SpringBootTest`, `RestTestClient` y un `Clock` sustituido con `@TestBean`.
6. Convertir el dominio de las semanas 26 y 27 en una API REST sin poner Spring dentro del dominio.

---

## Requisitos previos

- Semana 27: el servicio construido con TDD, el `Clock` inyectado y las reglas de ArchUnit.
- Semana 26: Mockito (`when`, `verify`, `verifyNoInteractions`).
- Semana 25: AssertJ y `@Nested`.
- Semana 12: probar una API REST con Supertest (aquí solo se ve la diferencia).
- JDK 21 y Maven 3.9 o superior. Comprueba con `java -version` y `mvn -v`.
- `curl`, para probar la API a mano en el proyecto.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Contexto y slices, `@WebMvcTest` y MockMvc, `@JsonTest`, `@SpringBootTest` | 2.5 h |
| Prácticas | 4 labs: MockMvc, validación con MockMvcTester, contrato JSON, aplicación entera | 4 h |
| Proyecto | La API Spring Boot del dominio con tres capas de tests nuevas | 3 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [El contexto de Spring en los tests y los slices](./1-teoria/01-contexto-de-spring-y-slices.md)
2. [@WebMvcTest, MockMvc y MockMvcTester](./1-teoria/02-webmvctest-y-mockmvc.md)
3. [El contrato JSON con @JsonTest](./1-teoria/03-contrato-json-con-jsontest.md)
4. [@SpringBootTest de extremo a extremo](./1-teoria/04-springboottest-de-extremo-a-extremo.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - @WebMvcTest y MockMvc](./2-practicas/lab-01-webmvctest-y-mockmvc/README.md)
- [Lab 02 - Validación y errores con MockMvcTester](./2-practicas/lab-02-validacion-con-mockmvctester/README.md)
- [Lab 03 - El contrato JSON con @JsonTest](./2-practicas/lab-03-contrato-json-con-jsontest/README.md)
- [Lab 04 - @SpringBootTest y RestTestClient](./2-practicas/lab-04-springboottest-y-resttestclient/README.md)

### Proyecto

- [La API Spring Boot del dominio](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): rama `week-28` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 28
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-28-testing_de_spring_boot/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-slices-de-spring-boot.svg
|   `-- 02-mockmvc-y-servidor-real.svg
|-- 1-teoria/
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-webmvctest-y-mockmvc/
|   |-- lab-02-validacion-con-mockmvctester/
|   |-- lab-03-contrato-json-con-jsontest/
|   `-- lab-04-springboottest-y-resttestclient/
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

Spring Boot 4.1.1 (`spring-boot-starter-parent`), que gestiona Spring Framework 7.0.9, Jackson 3.1.5, Hibernate Validator 9.1.3, JUnit Jupiter 6.0.3, AssertJ 3.27.7, Mockito 5.23.0 y maven-surefire-plugin 3.5.6. Fuera del parent: maven-dependency-plugin 3.11.0 y ArchUnit 1.5.1. Están registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Antes de escribir un `@SpringBootTest`, pregúntate si un test unitario o un slice prueban lo mismo. La aplicación entera prueba que las piezas encajan; los detalles de cada pieza se prueban más rápido, y fallan con un mensaje más claro, más abajo en la pirámide.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 27 - TDD con Java](../week-27-tdd_con_java/README.md) | Semana 29 - Próximamente |
