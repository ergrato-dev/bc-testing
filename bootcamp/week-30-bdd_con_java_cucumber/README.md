# Semana 30 - Java Testing VI: BDD con Java (Cucumber)

> **Etapa 3 - Testing con Java** | Semana 30 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Ejecutar features de Gherkin en español con Cucumber y JUnit Platform desde `mvn test`.
2. Escribir step definitions con Cucumber Expressions y tipos de parámetro propios.
3. Pasar tablas a los steps como objetos con `@DataTableType`.
4. Compartir estado entre clases de steps con `cucumber-spring` y `@ScenarioScope`, y limpiar con hooks lo que vive entre escenarios.
5. Ejecutar escenarios contra la aplicación Spring Boot completa, con PostgreSQL en un contenedor y un reloj controlado.
6. Generar reportes HTML y JSON, filtrar por tags y escribir features que sirvan como documentación.

---

## Requisitos previos

- Semana 22: BDD, Gherkin y behave (aquí solo se ve la diferencia).
- Semana 27: el doble bucle de TDD.
- Semanas 28 y 29: la API Spring Boot del dominio, `RestTestClient` y PostgreSQL con Testcontainers.
- JDK 21 y Maven 3.9 o superior. Comprueba con `java -version` y `mvn -v`.
- Docker en marcha para el proyecto (los labs no lo necesitan). Comprueba con `docker version --format '{{.Server.Version}}'`.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Cucumber en Java, tablas y tipos de parámetro, estado y `cucumber-spring`, reportes y tags | 2.5 h |
| Prácticas | 4 labs: primer feature, DataTable, estado compartido, Spring Boot y reportes | 4 h |
| Proyecto | Los flujos del dominio en tres features contra la API completa | 3 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Cucumber en Java](./1-teoria/01-cucumber-en-java.md)
2. [Tablas, tipos de parámetro y esquemas](./1-teoria/02-tablas-tipos-y-esquemas.md)
3. [Estado entre steps y cucumber-spring](./1-teoria/03-estado-y-cucumber-spring.md)
4. [Reportes, tags y documentación viva](./1-teoria/04-reportes-tags-y-documentacion-viva.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Primer feature con Cucumber](./2-practicas/lab-01-primer-feature-con-cucumber/README.md)
- [Lab 02 - DataTable y tipos de parámetro](./2-practicas/lab-02-datatable-y-tipos-de-parametro/README.md)
- [Lab 03 - Estado compartido con cucumber-spring](./2-practicas/lab-03-estado-compartido-con-cucumber-spring/README.md)
- [Lab 04 - Cucumber con Spring Boot y reportes](./2-practicas/lab-04-cucumber-con-spring-boot-y-reportes/README.md)

### Proyecto

- [Los flujos del dominio en Cucumber](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): rama `week-30` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo, con Docker en marcha:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 30
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-30-bdd_con_java_cucumber/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-de-la-linea-al-metodo.svg
|   `-- 02-vida-de-cada-objeto.svg
|-- 1-teoria/
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-primer-feature-con-cucumber/
|   |-- lab-02-datatable-y-tipos-de-parametro/
|   |-- lab-03-estado-compartido-con-cucumber-spring/
|   `-- lab-04-cucumber-con-spring-boot-y-reportes/
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

Cucumber 8.1.0 (`cucumber-bom`: `cucumber-java`, `cucumber-spring` y `cucumber-junit-platform-engine`), con `junit-platform-suite` en la versión que gestiona Spring Boot 4.1.1, además de las versiones de las semanas 28 y 29. Maven Failsafe 3.5.6, también gestionado por Spring Boot, aparece en la teoría 04 y en un experimento. Están registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Cucumber no sustituye a los tests de las semanas anteriores: los usa de base. Las reglas sueltas siguen en los tests unitarios, los códigos HTTP en los de controlador y el SQL en los de repositorio. Un feature cuenta un flujo entero con las palabras del negocio, y por eso encuentra lo que se pierde entre las piezas: en el proyecto, un préstamo que la API nunca abría aunque cada servicio pasaba sus tests.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 29 - Testcontainers en Java](../week-29-testcontainers_en_java/README.md) | Semana 31 - Próximamente |
