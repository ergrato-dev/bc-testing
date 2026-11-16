# Lab 02 - El esquema con init.sql y con Flyway

## Objetivo

Crear las tablas de los tests de dos formas: con un script que ejecuta el contenedor (`withInitScript`) y con las migraciones de Flyway que usa producción. Pruebas las restricciones de la tabla por su SQLState, y ves con un error real por qué el script de test se queda atrás.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
mvn test
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

El código de producción es el repositorio de entradas de un cine, con JDBC y sin Spring:

| Archivo | Qué hace |
|---|---|
| `Ticket` | Record con `id`, `seat` (butaca) y `priceCents` |
| `TicketRepository` | `save(seat, priceCents)` con `INSERT ... RETURNING id` y `findBySeat(seat)`. Abre una conexión con `DriverManager` en cada llamada |
| `src/main/resources/db/migration/V1__create_tickets.sql` | Crea la tabla `tickets`, con `UNIQUE` en la butaca y `CHECK (price_cents > 0)` |
| `src/main/resources/db/migration/V2__add_sold_at.sql` | Añade la columna `sold_at` con la hora de la venta |
| `src/test/resources/schema.sql` | Copia a mano de V1 para `withInitScript`. No sabe nada de V2 |

Los tests:

| Archivo | Qué hay |
|---|---|
| `InitScriptTest` | Contenedor con `withInitScript("schema.sql")`, un test activo y el PASO 1 |
| `FlywayMigrationTest` | Contenedor sin esquema y un `@BeforeAll` que ejecuta Flyway. Los PASOS 2 y 3 son sus tests |

El `pom.xml` añade `flyway-core` y `flyway-database-postgresql` ([teoría 02](../../1-teoria/02-postgresql-esquema-y-migraciones.md#las-dependencias)). El driver de PostgreSQL va con `<scope>runtime</scope>`: el código solo importa `java.sql`, y el driver hace falta al ejecutar, no al compilar.

`FlywayMigrationTest` todavía no tiene ningún test, así que JUnit no lo ejecuta y no arranca su contenedor.

## Paso a paso

### Paso 1: las reglas de la tabla (PASO 1)

Descomenta los dos bloques del PASO 1 de `InitScriptTest`: el `import` de `assertThatThrownBy` y los dos tests.

```java
assertThatThrownBy(() -> tickets.save("B1", 0))
    .isInstanceOf(SQLException.class)
    .hasMessageContaining("tickets_price_cents_check")
    .extracting(error -> ((SQLException) error).getSQLState())
    .isEqualTo("23514");
```

- **`extracting(...)`** cambia lo que se comprueba: de la excepción, a su SQLState.
- **`23514`** es *check_violation* y **`23505`**, *unique_violation* ([teoría 02](../../1-teoria/02-postgresql-esquema-y-migraciones.md#las-restricciones-también-se-prueban)).

```bash
mvn test
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

Los tres tests comparten contenedor y no se limpian entre sí. Funcionan porque cada uno usa una butaca distinta (`A1`, `B1`, `C1`). Si dos usaran la misma, el resultado dependería del orden.

### Paso 2: el esquema de Flyway (PASO 2)

Descomenta los tres bloques del PASO 2 de `FlywayMigrationTest`: los dos de `import` y el de los dos tests.

Lee primero el `@BeforeAll`, que ya estaba activo ([teoría 02](../../1-teoria/02-postgresql-esquema-y-migraciones.md#migrar-en-el-test)): aplica las migraciones de `db/migration` al contenedor vacío y guarda el resultado.

- `shouldApplyBothMigrations` comprueba que se aplicaron dos migraciones y que el esquema quedó en la versión `2`.
- `shouldStampTheSaleTimeWithTheDefaultOfV2` guarda una entrada sin dar `sold_at` y comprueba que PostgreSQL rellenó la columna con su `DEFAULT now()`.

```bash
mvn test
# [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 3: la tabla de historial (PASO 3)

Descomenta el PASO 3. Usa los `import` del PASO 2.

```bash
mvn test
# 1 create tickets true
# 2 add sold at true
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

Es la tabla `flyway_schema_history`. La próxima vez que Flyway se ejecute contra esa base de datos, verá que la 1 y la 2 ya están y no hará nada.

## Experimentos

### Experimento 1: la copia desfasada

En `InitScriptTest`, añade al final del test activo una consulta a la columna que creó V2:

```java
try (var c = java.sql.DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
    c.createStatement().executeQuery("SELECT sold_at FROM tickets");
}
```

```bash
mvn test -Dtest=InitScriptTest
# [ERROR]   InitScriptTest.shouldFindATicketBySeatAfterSavingIt:35 » PSQL ERROR: column "sold_at" does not exist
```

En producción la columna existe; en el esquema de este test, no. Con Flyway la misma consulta funciona, porque el esquema sale de las mismas migraciones. Deshaz el cambio.

### Experimento 2: una migración rota

Crea `src/main/resources/db/migration/V3__add_row_label.sql` con un error de escritura (`COLUM` en lugar de `COLUMN`):

```sql
ALTER TABLE tickets ADD COLUM row_label text;
```

```bash
mvn test -Dtest=FlywayMigrationTest
# org.flywaydb.core.internal.exception.FlywayMigrateException:
# Failed to execute script V3__add_row_label.sql
# ----------------------------------------------
# SQL State  : 42601
# Error Code : 0
# Message    : ERROR: syntax error at or near "text"
#   Position: 41
```

`42601` es *syntax_error*. Un error en una migración se descubre en `mvn test`, no al desplegar. Borra el archivo.

### Experimento 3: sin el soporte de PostgreSQL

En el `pom.xml`, borra la dependencia `flyway-database-postgresql` entera.

```bash
mvn test -Dtest=FlywayMigrationTest
# org.flywaydb.core.api.FlywayException: No Flyway database plugin found to handle jdbc:postgresql://localhost:32793/test?loggerLevel=OFF. See https://help.red-gate.com/help/flyway-cli12/help_4.aspx?topic=reference/usage/database-troubleshooting for troubleshooting
```

Es el error que verás si copias un `pom.xml` de un tutorial anterior a Flyway 10. Deshaz el cambio.

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
| ← [Lab 01 - GenericContainer y PostgreSQLContainer](../lab-01-genericcontainer-y-postgresqlcontainer/README.md) | [Lab 03 - @ServiceConnection con Spring Boot](../lab-03-serviceconnection-con-spring-boot/README.md) → |

[↑ Semana 29](../../README.md)
