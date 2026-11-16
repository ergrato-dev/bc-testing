# Semana 29 - Java Testing V: Testcontainers en Java

> **Etapa 3 - Testing con Java** | Semana 29 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Arrancar servicios reales en los tests con Testcontainers y elegir cuánto vive cada contenedor.
2. Crear el esquema de los tests con las mismas migraciones de Flyway que producción, y probar las restricciones de la tabla.
3. Conectar Spring Boot a un contenedor con `@ServiceConnection` y probar repositorios con `@JdbcTest`.
4. Compartir contenedores entre clases sin compartir datos por accidente, y reutilizarlos en local.
5. Probar un productor de Kafka contra un broker real.
6. Pasar los repositorios del dominio a PostgreSQL con tests de contrato que también pasa el fake en memoria.

---

## Requisitos previos

- Semana 28: la API Spring Boot del dominio, los slices y la caché de contextos.
- Semana 23: PostgreSQL con Testcontainers en Python y el `compose.yaml` del proyecto (aquí solo se ve la diferencia).
- Semana 25: `@Nested`.
- JDK 21 y Maven 3.9 o superior. Comprueba con `java -version` y `mvn -v`.
- Docker en marcha. Comprueba con `docker version --format '{{.Server.Version}}'`.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Ciclo de vida, PostgreSQL y migraciones, `@ServiceConnection`, contenedores compartidos y Kafka | 2.5 h |
| Prácticas | 4 labs: GenericContainer, init.sql y Flyway, Spring Boot, Kafka | 4 h |
| Proyecto | Los repositorios del dominio en PostgreSQL con tests de contrato | 3 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Testcontainers y el ciclo de vida de un contenedor](./1-teoria/01-testcontainers-y-el-ciclo-de-vida.md)
2. [PostgreSQL, su esquema y las migraciones](./1-teoria/02-postgresql-esquema-y-migraciones.md)
3. [@ServiceConnection en Spring Boot](./1-teoria/03-serviceconnection-en-spring-boot.md)
4. [Contenedores compartidos, reutilización y Kafka](./1-teoria/04-contenedores-compartidos-y-kafka.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - GenericContainer y PostgreSQLContainer](./2-practicas/lab-01-genericcontainer-y-postgresqlcontainer/README.md)
- [Lab 02 - El esquema con init.sql y con Flyway](./2-practicas/lab-02-esquema-con-init-sql-y-flyway/README.md)
- [Lab 03 - @ServiceConnection con Spring Boot](./2-practicas/lab-03-serviceconnection-con-spring-boot/README.md)
- [Lab 04 - Kafka y el contenedor compartido](./2-practicas/lab-04-kafka-y-contenedor-compartido/README.md)

### Proyecto

- [Los repositorios del dominio en PostgreSQL](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): rama `week-29` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo, con Docker en marcha:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 29
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-29-testcontainers_en_java/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-ciclo-de-vida-de-un-contenedor.svg
|   `-- 02-contenedores-compartidos.svg
|-- 1-teoria/
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-genericcontainer-y-postgresqlcontainer/
|   |-- lab-02-esquema-con-init-sql-y-flyway/
|   |-- lab-03-serviceconnection-con-spring-boot/
|   `-- lab-04-kafka-y-contenedor-compartido/
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

Spring Boot 4.1.1 (`spring-boot-starter-parent`), que gestiona Testcontainers 2.0.5, Flyway 12.4.0, el driver de PostgreSQL 42.7.13 y `kafka-clients` 4.2.1, además de las versiones de la semana 28. Imágenes de Docker: `postgres:18.7-alpine` (la misma que `compose.yaml`) y `apache/kafka-native:4.2.1`; Testcontainers añade `testcontainers/ryuk:0.14.0`. Fuera del parent siguen maven-dependency-plugin 3.11.0 y ArchUnit 1.5.1. Están registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Un fake en memoria es rápido y sirve para los tests del dominio, pero no ejecuta SQL. Esta semana no lo tiras: lo pones a prueba. Si pasa los mismos tests de contrato que el repositorio de PostgreSQL, los tests que lo usan siguen diciendo la verdad. Lo que el fake no puede comprobar (las restricciones de la tabla, el `ON CONFLICT`, las migraciones) se prueba contra el PostgreSQL real.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 28 - Testing de Spring Boot](../week-28-testing_de_spring_boot/README.md) | Semana 30 - Próximamente |
