# Lab 03 - @ServiceConnection con Spring Boot

## Objetivo

Probar un repositorio de Spring Boot con `@JdbcTest` contra PostgreSQL: primero conectando el contenedor con `@DynamicPropertySource`, después con `@ServiceConnection`, y al final con el contenedor declarado una vez como bean y compartido entre clases.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
mvn test
# INFO  tc.postgres:18.7-alpine - Container postgres:18.7-alpine started in PT1.909724082S
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

El código de producción es el stock de un almacén:

| Archivo | Qué hace |
|---|---|
| `WarehouseApplication` | La clase `@SpringBootApplication` |
| `StockRepository` | `@Repository` con `JdbcClient`: `add(sku, quantity)`, `quantity(sku)` y `reserve(sku, quantity)` |
| `application.properties` | La URL de un PostgreSQL de desarrollo en `127.0.0.1:55432`. Los tests no la usan |
| `db/migration/V1__create_stock.sql` | Tabla `stock` con `CHECK (quantity >= 0)` |

`JdbcClient` es la API de Spring para SQL: `sql(...)` recibe la consulta con parámetros por nombre (`:sku`), `param(...)` les da valor, y `update()`, `query(...).optional()` o `query(...).list()` la ejecutan.

Dos consultas son de PostgreSQL y no tienen equivalente en un `Map`:

- `add` usa `INSERT ... ON CONFLICT (sku) DO UPDATE SET quantity = stock.quantity + EXCLUDED.quantity`: si el SKU ya existe, suma en lugar de fallar. `EXCLUDED` es la fila que se intentó insertar.
- `reserve` usa `UPDATE ... WHERE sku = :sku AND quantity >= :quantity`: si no hay unidades suficientes, no actualiza ninguna fila y el método devuelve `false`.

El `pom.xml` trae `spring-boot-starter-jdbc`, `spring-boot-starter-flyway` con `flyway-database-postgresql`, y para los tests `spring-boot-starter-jdbc-test` (`@JdbcTest`), `spring-boot-testcontainers` (`@ServiceConnection`) y los dos módulos de Testcontainers.

## Paso a paso

### Paso 0: lee el test activo

Abre `StockRepositoryTest` con la [teoría 03](../../1-teoria/03-serviceconnection-en-spring-boot.md#con-dynamicpropertysource) al lado. Tiene tres piezas:

1. `@JdbcTest` arranca solo la parte JDBC del contexto: `DataSource`, `JdbcClient`, transacciones y Flyway.
2. `@Import(StockRepository.class)` añade el repositorio, que `@JdbcTest` no carga.
3. `@DynamicPropertySource` le pasa al contexto la URL del contenedor.

El test suma dos entradas del mismo SKU y espera 8. Funciona porque Flyway creó la tabla al arrancar el contexto: no hay ningún `withInitScript`.

### Paso 1: la reserva y el rollback (PASO 1)

Descomenta el PASO 1 de `StockRepositoryTest`.

```bash
mvn test
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

Los tres tests usan el mismo SKU, `SKU-1`, con cantidades distintas, y el mismo contenedor. Aun así ninguno ve las filas de otro: `@JdbcTest` ejecuta cada test en una transacción y la deshace al terminar ([teoría 03](../../1-teoria/03-serviceconnection-en-spring-boot.md#el-rollback-de-jdbctest)). Compáralo con el lab 02, donde cada test necesitaba su propia butaca.

### Paso 2: @ServiceConnection (PASO 2)

Descomenta el archivo `ServiceConnectionTest` entero.

```java
@Container
@ServiceConnection
static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.7-alpine");
```

Es la misma clase que `StockRepositoryTest` sin `@DynamicPropertySource`. Spring Boot lee la conexión del contenedor.

```bash
mvn test
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 3: el contenedor como bean (PASO 3)

Descomenta tres archivos enteros: `TestcontainersConfiguration`, `RestockTest` y `ReservationTest`.

`TestcontainersConfiguration` declara el contenedor como `@Bean` con `@ServiceConnection` ([teoría 03](../../1-teoria/03-serviceconnection-en-spring-boot.md#el-contenedor-como-bean)). Las dos clases de test lo importan y no tienen ni `@Testcontainers` ni campo de contenedor.

```bash
mvn test
# INFO  tc.postgres:18.7-alpine - Container postgres:18.7-alpine started in PT1.743944347S
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 7.459 s -- in com.bootcamp.ReservationTest
# INFO  tc.postgres:18.7-alpine - Container postgres:18.7-alpine started in PT1.72090795S
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.423 s -- in com.bootcamp.StockRepositoryTest
# INFO  tc.postgres:18.7-alpine - Container postgres:18.7-alpine started in PT1.69334155S
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.439 s -- in com.bootcamp.ServiceConnectionTest
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.030 s -- in com.bootcamp.RestockTest
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

El orden de las clases puede ser otro en tu equipo. Cuenta los `started in`: son tres para cuatro clases. `RestockTest` no arrancó ningún contenedor y tardó 30 ms, porque reutilizó el contexto de `ReservationTest`, y el contenedor es un bean de ese contexto. Las otras dos clases tienen su propio campo `@Container`, y por eso cada una arranca el suyo.

## Experimentos

### Experimento 1: sin la conexión del contenedor

En `StockRepositoryTest`, borra el método `datasource` con su `@DynamicPropertySource`.

```bash
mvn test -Dtest=StockRepositoryTest
# Caused by: java.lang.IllegalStateException: Failed to replace DataSource with an embedded database for tests. If you want an embedded database please put a supported one on the classpath or tune the replace attribute of @AutoConfigureTestDatabase.
```

Sin una conexión de test, `@JdbcTest` intenta poner una base de datos embebida en lugar de la de `application.properties` y no encuentra ninguna. El error sale varias veces en la cadena de `Caused by`, una por cada bean que dependía del `DataSource`. Deshaz el cambio.

### Experimento 2: otro contexto, otro contenedor

En `RestockTest`, cambia `@JdbcTest` por `@SpringBootTest` (`import org.springframework.boot.test.context.SpringBootTest;`).

```bash
mvn test
# INFO  tc.postgres:18.7-alpine - Container postgres:18.7-alpine started in PT1.782140089S
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.285 s -- in com.bootcamp.RestockTest
```

`RestockTest` ya no comparte configuración con `ReservationTest`: arranca su propio contexto y, con él, su propio PostgreSQL. Además, `@SpringBootTest` no deshace los tests: la fila de `SKU-NEW` queda confirmada en ese contenedor. Deshaz el cambio.

## Comprobación final

```bash
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

Si un PASO no compila o no pasa, comprueba que descomentaste todos sus bloques, también los `import` marcados con su número, y que deshiciste los experimentos anteriores.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - El esquema con init.sql y con Flyway](../lab-02-esquema-con-init-sql-y-flyway/README.md) | [Lab 04 - Kafka y el contenedor compartido](../lab-04-kafka-y-contenedor-compartido/README.md) → |

[↑ Semana 29](../../README.md)
