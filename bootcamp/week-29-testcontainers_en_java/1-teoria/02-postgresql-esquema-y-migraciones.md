# 02 - PostgreSQL, su esquema y las migraciones

> Lenguaje: **Java**

---

## 🎯 Objetivos

- Usar `PostgreSQLContainer` con la misma imagen que la aplicación en producción.
- Crear el esquema de los tests con un script (`withInitScript`) y explicar por qué se queda desfasado.
- Crear el esquema con las migraciones de Flyway, las mismas que usa producción.
- Leer los errores de una restricción de PostgreSQL y de una migración rota.

---

## PostgreSQLContainer

El módulo de PostgreSQL hace lo que en la [teoría 01](01-testcontainers-y-el-ciclo-de-vida.md#un-postgresql-a-mano-con-genericcontainer) escribiste a mano:

```java
@Container
static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.7-alpine");
```

Elige un usuario, una contraseña y una base de datos (`test` las tres), espera a que PostgreSQL acepte conexiones y te da la URL completa. En el [lab 01](../2-practicas/lab-01-genericcontainer-y-postgresqlcontainer/README.md) se imprime:

```text
jdbc:postgresql://localhost:32770/test?loggerLevel=OFF test/test
```

| Método | Para qué |
|---|---|
| `getJdbcUrl()` | La URL para `DriverManager`, Flyway o un `DataSource` |
| `getUsername()`, `getPassword()` | Las credenciales |
| `getDatabaseName()` | El nombre de la base de datos |

### La misma imagen que producción

El `compose.yaml` de tu proyecto (de la [semana 23](../../week-23-testing_de_bases_de_datos_en_python/3-proyecto/README.md)) arranca `postgres:18.7-alpine`. Los tests usan exactamente esa etiqueta. Si un día subes PostgreSQL a 18.8, cambias las dos a la vez y los tests te dicen si algo se rompió antes de que lo descubra producción. Si los tests usaran otra versión, un verde no garantizaría nada sobre la de producción.

---

## Un esquema con withInitScript

Una base de datos recién creada está vacía. La forma más rápida de tener tablas es un script SQL que el contenedor ejecuta al arrancar:

```java
@Container
static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.7-alpine")
    .withInitScript("schema.sql");
```

`schema.sql` se busca en el classpath, es decir, en `src/test/resources/`. En el [lab 02](../2-practicas/lab-02-esquema-con-init-sql-y-flyway/README.md):

```sql
CREATE TABLE tickets (
    id          bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    seat        text NOT NULL UNIQUE,
    price_cents int  NOT NULL CHECK (price_cents > 0)
);
```

### Las restricciones también se prueban

`UNIQUE` y `CHECK` son reglas que impone PostgreSQL, no tu código. Un repositorio en memoria no las tiene; con el motor real, puedes comprobar que están:

```java
assertThatThrownBy(() -> tickets.save("B1", 0))
    .isInstanceOf(SQLException.class)
    .hasMessageContaining("tickets_price_cents_check")
    .extracting(error -> ((SQLException) error).getSQLState())
    .isEqualTo("23514");
```

- **`tickets_price_cents_check`**: el nombre que PostgreSQL le da al `CHECK` si tú no le pones uno: tabla, columna y `check`.
- **SQLState**: un código de cinco caracteres que es el mismo en cualquier driver. `23514` es *check_violation* y `23505` es *unique_violation*. Comprobar el código es más robusto que comprobar el texto del mensaje, que depende del idioma del servidor.

### El problema: el script se queda atrás

El script de test es una **copia** del esquema. En producción el esquema evoluciona con migraciones; la copia solo cambia si alguien se acuerda. En el lab 02, la migración V2 añade una columna `sold_at` que la copia no tiene. Un test que la usa falla:

```text
PSQL ERROR: column "sold_at" does not exist
```

Peor aún es el caso contrario: el test pasa contra la copia y producción falla, porque la copia tiene algo que las migraciones no crean. Con dos fuentes del esquema, tarde o temprano se separan.

---

## Un esquema con Flyway

Flyway aplica migraciones: archivos SQL numerados que se ejecutan en orden, una sola vez cada uno. Es lo mismo que Alembic en la [semana 23](../../week-23-testing_de_bases_de_datos_en_python/1-teoria/04-migraciones-con-alembic.md), pero las migraciones son SQL escrito a mano, no generadas desde los modelos.

### Las migraciones

Van en `src/main/resources/db/migration/`, porque son de producción, y el nombre sigue un formato fijo:

```text
V1__create_tickets.sql      # V + versión + dos guiones bajos + descripción
V2__add_sold_at.sql
```

```sql
-- V2__add_sold_at.sql
ALTER TABLE tickets ADD COLUMN sold_at timestamptz NOT NULL DEFAULT now();
```

### Las dependencias

```xml
<dependency>
  <groupId>org.flywaydb</groupId>
  <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
  <groupId>org.flywaydb</groupId>
  <artifactId>flyway-database-postgresql</artifactId>
</dependency>
```

Desde Flyway 10, el soporte de cada base de datos va en un artefacto aparte. Sin `flyway-database-postgresql`, Flyway arranca pero no sabe hablar con PostgreSQL:

```text
org.flywaydb.core.api.FlywayException: No Flyway database plugin found to handle jdbc:postgresql://localhost:32793/test?loggerLevel=OFF.
```

### Migrar en el test

Sin Spring, llamas a Flyway tú mismo, una vez por clase, antes de los tests:

```java
@BeforeAll
static void migrate() {
    migration = Flyway.configure()
        .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
        .load()
        .migrate();
}
```

- **`Flyway.configure()`**: empieza la configuración. Por defecto, busca las migraciones en `classpath:db/migration`.
- **`.dataSource(url, usuario, contraseña)`**: la base de datos del contenedor.
- **`.migrate()`**: aplica las pendientes y devuelve un `MigrateResult` con cuántas aplicó (`migrationsExecuted`) y la versión final (`targetSchemaVersion`).

Ahora el esquema de los tests sale de los mismos archivos que el de producción. Si una migración tiene un error, falla aquí, no en el despliegue:

```text
org.flywaydb.core.internal.exception.FlywayMigrateException:
Failed to execute script V3__add_row_label.sql
----------------------------------------------
SQL State  : 42601
Error Code : 0
Message    : ERROR: syntax error at or near "text"
  Position: 41
```

### La tabla de historial

Flyway anota cada migración aplicada en `flyway_schema_history`. Así sabe cuáles faltan la próxima vez. En el lab 02 la lees desde un test:

```text
1 create tickets true
2 add sold at true
```

La descripción sale del nombre del archivo, con los guiones bajos cambiados por espacios.

### ¿Y Liquibase?

Liquibase resuelve el mismo problema. Sus migraciones (*changesets*) se escriben en XML, YAML o SQL dentro de un *changelog*, y Spring Boot también lo ejecuta al arrancar si está en el classpath. Para los tests da igual cuál uses: lo importante es que el esquema de los tests salga de las mismas migraciones que el de producción. El curso usa Flyway porque sus migraciones son SQL sin envoltorio.

---

## ✅ Resumen

- `PostgreSQLContainer` te da la URL y las credenciales; usa la misma etiqueta de imagen que tu `compose.yaml`.
- `withInitScript` es rápido de montar, pero es una copia del esquema que se queda desfasada.
- Con Flyway, los tests crean el esquema con las migraciones de producción: una sola fuente.
- Flyway 10 o posterior necesita `flyway-database-postgresql` para hablar con PostgreSQL.
- Las restricciones de la tabla (`CHECK`, `UNIQUE`, claves foráneas) se prueban con su SQLState.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 01 - Testcontainers y el ciclo de vida de un contenedor](01-testcontainers-y-el-ciclo-de-vida.md) | [Teoría — 03 - @ServiceConnection en Spring Boot](03-serviceconnection-en-spring-boot.md) → |

[↑ Semana 29](../README.md)
