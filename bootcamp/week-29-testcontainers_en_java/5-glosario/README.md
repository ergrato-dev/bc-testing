# Glosario Semana 29 - Testcontainers en Java

## A

- **`acks=all`**: configuración del productor de Kafka. El broker confirma el mensaje cuando lo han guardado todas las réplicas.
- **`Admin`**: cliente de administración de Kafka. Crea y borra topics, entre otras cosas.
- **`@AutoConfigureTestDatabase`**: incluida en `@JdbcTest`. Decide si sustituir el `DataSource` por una base de datos embebida; en Spring Boot 4, por defecto solo lo hace si la conexión no viene de un test (`replace = NON_TEST`).

## B

- **Bootstrap servers**: la dirección inicial a la que se conectan el productor y el consumidor de Kafka. Con Testcontainers, `kafka.getBootstrapServers()`.
- **Broker**: el servidor de Kafka que guarda los mensajes de los topics.

## C

- **Clave (Kafka)**: valor opcional de cada mensaje. Los mensajes con la misma clave van a la misma partición y se leen en el orden en que se escribieron.
- **`@Container`**: marca un campo que la extensión de Testcontainers arranca antes de los tests y para después. Con `static`, uno por clase; sin `static`, uno por test.
- **Consumidor**: el cliente de Kafka que lee mensajes de un topic con `poll`.
- **Contenedor singleton**: un contenedor en un campo `static` que se arranca una vez en un bloque `static` y comparten todas las clases que lo usan.

## D

- **`DockerImageName`**: el nombre de una imagen con su etiqueta, por ejemplo `postgres:18.7-alpine`.
- **`@DynamicPropertySource`**: método `static` de un test que añade propiedades al contexto de Spring con valores que solo se conocen en ejecución, como el puerto de un contenedor.

## E

- **Estrategia de espera (`waitingFor`)**: cómo sabe Testcontainers que el servicio del contenedor está listo: un mensaje en el log, un puerto abierto o una respuesta HTTP.

## F

- **Flyway**: herramienta de migraciones. Aplica en orden los archivos `V<n>__<descripción>.sql` que todavía no se aplicaron.
- **`flyway_schema_history`**: tabla donde Flyway anota cada migración aplicada, con su versión y si terminó bien.

## G

- **`GenericContainer`**: la clase de Testcontainers que arranca cualquier imagen de Docker. Los módulos, como `PostgreSQLContainer`, heredan de ella.

## H

- **H2**: base de datos escrita en Java que se ejecuta dentro de la JVM. El curso no la usa: no es PostgreSQL.

## J

- **`@JdbcTest`**: slice de Spring Boot para probar código JDBC. Arranca el `DataSource`, `JdbcClient`, las transacciones y Flyway, y deshace cada test con un rollback.
- **`JdbcClient`**: API de Spring para ejecutar SQL con parámetros por nombre: `sql(...)`, `param(...)`, `query(...)`, `update()`.

## K

- **`KafkaContainer`**: el módulo de Testcontainers para las imágenes `apache/kafka` y `apache/kafka-native`.

## L

- **Liquibase**: herramienta de migraciones alternativa a Flyway, con *changesets* en XML, YAML o SQL.

## M

- **Migración**: script versionado que cambia el esquema de la base de datos. Se aplica una sola vez y nunca se edita después.

## O

- **Offset**: posición de un mensaje dentro de su partición de Kafka.
- **`ON CONFLICT`**: cláusula de PostgreSQL que decide qué hacer cuando un `INSERT` choca con una clave única: no hacer nada o actualizar la fila (*upsert*).

## P

- **Partición**: cada una de las partes en que se divide un topic de Kafka. El orden solo se garantiza dentro de una partición.
- **Puerto publicado (`getMappedPort`)**: el puerto aleatorio del host que Docker conecta con el puerto interno del contenedor.

## R

- **Reutilización (`withReuse(true)`)**: un contenedor que sobrevive a la JVM y se reutiliza en la siguiente ejecución. Solo con `testcontainers.reuse.enable=true` en el equipo.
- **Ryuk**: contenedor auxiliar de Testcontainers que borra los contenedores de una ejecución cuando la JVM de los tests termina.

## S

- **`@ServiceConnection`**: anotación de Spring Boot que conecta un servicio (PostgreSQL, Kafka…) al contenedor marcado, sin escribir sus propiedades.
- **SQLState**: código de error de cinco caracteres, igual en cualquier driver: `23505` es una clave única repetida y `23514`, un `CHECK` incumplido.

## T

- **`@TestConfiguration`**: clase de configuración de Spring que solo existe en los tests y se añade con `@Import`.
- **Test de contrato**: clase de tests abstracta que define lo que cualquier implementación de una interfaz debe cumplir. La heredan el fake y la implementación real.
- **Testcontainers**: librería que arranca contenedores de Docker desde los tests y los borra al terminar.
- **`@Testcontainers`**: registra en la clase la extensión de JUnit que lee los campos `@Container`.
- **Topic**: canal con nombre de Kafka donde los productores escriben y los consumidores leen.

## W

- **`withInitScript`**: hace que `PostgreSQLContainer` ejecute un script SQL del classpath al arrancar.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | — |

[↑ Semana 29](../README.md)
