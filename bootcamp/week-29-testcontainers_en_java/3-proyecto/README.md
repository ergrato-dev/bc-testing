# Proyecto Semanal - Los repositorios del dominio en PostgreSQL

> **🎯 ÚNICO ENTREGABLE**: este proyecto es el único entregable obligatorio de la semana.

## Objetivo

Los repositorios de tu API dejan de guardar en memoria y pasan a PostgreSQL. Al terminar, tu carpeta `java/` tendrá:

- el esquema en migraciones de Flyway, con las reglas del dominio repetidas como restricciones de la tabla;
- repositorios con `JdbcClient` en `infrastructure`;
- los tests contra la misma imagen de PostgreSQL que `compose.yaml`, conectada con `@ServiceConnection`;
- **tests de contrato**: los mismos tests para el fake en memoria y para el repositorio de PostgreSQL. Si los dos pasan, los tests unitarios de las semanas 26 y 27, que usan el fake, siguen diciendo la verdad;
- una quinta regla de ArchUnit que deja el SQL dentro de `infrastructure`.

Y la API guardará los datos de verdad: sobreviven a un reinicio.

## Dónde va

En la carpeta `java/` de tu repositorio `qa-<nombre>-<apellido>` ([proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)), sobre la entrega de la [semana 28](../../week-28-testing_de_spring_boot/3-proyecto/README.md). Usa el `compose.yaml` de la raíz del repositorio, el de la [semana 23](../../week-23-testing_de_bases_de_datos_en_python/3-proyecto/README.md).

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

En el ejemplo del museo hay dos repositorios: `PieceRepository` (piezas) y `LoanRepository` (préstamos abiertos). Haz lo mismo con cada interfaz de repositorio de tu dominio: una tabla o más por repositorio, una clase `Jdbc...Repository` y un test de contrato.

## Paso a paso

### Paso 1: rama, starter y base de datos

1. Crea la rama:

   ```bash
   cd qa-<nombre>-<apellido>
   git switch main && git pull
   git switch -c week-29
   ```

2. Copia el starter:

   ```bash
   cp -r <ruta-al-bootcamp>/bootcamp/week-29-testcontainers_en_java/3-proyecto/starter/. .
   ```

   | Archivo | Qué es |
   |---|---|
   | `java/pom.xml` | **Sustituye** al tuyo. Añade `spring-boot-starter-jdbc`, `spring-boot-starter-flyway`, `flyway-database-postgresql` y el driver; para los tests, `spring-boot-starter-jdbc-test`, `spring-boot-testcontainers` y `testcontainers-postgresql`. Si añadiste dependencias propias, vuelve a ponerlas |
   | `java/src/main/resources/application.properties` | **Sustituye** al tuyo. Añade las tres propiedades del `DataSource` con el PostgreSQL de `compose.yaml`. Si en tu `compose.yaml` cambiaste usuario, contraseña o base de datos, cámbialos aquí también, y vuelve a poner tus propiedades propias |
   | `java/src/test/java/com/bootcamp/TestcontainersConfiguration.java` | El contenedor de PostgreSQL de los tests, como bean con `@ServiceConnection` ([teoría 03](../1-teoria/03-serviceconnection-en-spring-boot.md#el-contenedor-como-bean)) |

3. Comprueba que la imagen de `TestcontainersConfiguration` es la misma que la de `compose.yaml`:

   ```bash
   grep image compose.yaml
   #     image: postgres:18.7-alpine
   grep POSTGRES_IMAGE java/src/test/java/com/bootcamp/TestcontainersConfiguration.java
   #     static final String POSTGRES_IMAGE = "postgres:18.7-alpine";
   #         return new PostgreSQLContainer(POSTGRES_IMAGE);
   ```

   Si no coinciden, cambia la del test. La comprobación `same-image` de [checks.json](../checks.json) lo vigila.

4. Ejecuta la suite **sin** arrancar el PostgreSQL de `compose.yaml`:

   ```bash
   cd java
   mvn test
   # Caused by: org.postgresql.util.PSQLException: Connection to 127.0.0.1:55432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.
   # [ERROR]   MuseumApiTest.shouldChargeTheLateDaysAndTheDamageWhenAPieceComesBackLate » IllegalState Failed to load ApplicationContext for [WebMergedContextConfig
   # [ERROR] Tests run: 75, Failures: 0, Errors: 3, Skipped: 0
   ```

   Falla el test de `@SpringBootTest`. Con `spring-boot-starter-jdbc` y Flyway en el `pom.xml`, la aplicación entera necesita una base de datos al arrancar, y busca la de `application.properties`. Los slices `@WebMvcTest` y `@JsonTest` no cargan el `DataSource`, así que siguen pasando.

   Podrías arrancar `docker compose up` y la suite pasaría, pero dependería de una base de datos que alguien tiene que arrancar a mano y que guarda los datos de la ejecución anterior. El paso 2 lo resuelve.

### Paso 2: PostgreSQL en el test de extremo a extremo

En tu clase de `@SpringBootTest` (en el ejemplo, `MuseumApiTest`), importa la configuración del contenedor:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@AutoConfigureRestTestClient
class MuseumApiTest {
```

`@Import` es `org.springframework.context.annotation.Import`.

```bash
mvn test
# ... tc.postgres:18.7-alpine : Container postgres:18.7-alpine started in PT1.733736576S
# ... DbValidate : No migrations found. Are your locations set up correctly?
# [INFO] Tests run: 75, Failures: 0, Errors: 0, Skipped: 0
```

Ahora el contexto del test se conecta al contenedor, no a `application.properties`. El aviso de Flyway es correcto: todavía no hay migraciones, y los repositorios siguen en memoria. Actualiza también el Javadoc de la clase: ya no usa repositorios en memoria, y como `@SpringBootTest` no deshace nada, cada test sigue necesitando datos propios ([teoría 03](../1-teoria/03-serviceconnection-en-spring-boot.md#el-rollback-de-jdbctest)).

Commit:

```bash
git add java/pom.xml java/src/main/resources/application.properties java/src/test
git commit -m "test(java): run the end-to-end test against PostgreSQL with Testcontainers"
```

### Paso 3: el esquema en una migración

Crea `java/src/main/resources/db/migration/V1__create_pieces_and_loans.sql` (con el nombre de tus tablas). Cada regla que el dominio ya comprueba se repite en la tabla:

```sql
-- Las mismas reglas que el dominio, repetidas en la base de datos: si un error del código
-- se las salta, PostgreSQL rechaza la fila.
CREATE TABLE pieces (
    code           text PRIMARY KEY,
    name           text NOT NULL,
    declared_value int  NOT NULL CHECK (declared_value > 0),
    status         text NOT NULL CHECK (status IN ('AVAILABLE', 'ON_LOAN', 'IN_RESTORATION'))
);

-- Un préstamo abierto por pieza: la clave primaria es la pieza. Al cerrarlo se borra la fila.
CREATE TABLE loans (
    piece_code     text PRIMARY KEY REFERENCES pieces (code),
    institution_id text NOT NULL,
    due_date       date NOT NULL
);
```

| Del dominio | En la tabla |
|---|---|
| `register` rechaza un valor declarado `<= 0` (semana 26) | `CHECK (declared_value > 0)` |
| `register` rechaza un código repetido | `PRIMARY KEY` |
| Un `enum` de Java (`Piece.Status`) | `text` con `CHECK (status IN (...))`: el nombre del valor, no su posición |
| Un préstamo es de una pieza que existe | `REFERENCES pieces (code)`, la clave foránea |
| Una pieza tiene como mucho un préstamo abierto | `PRIMARY KEY (piece_code)` |

Guardar el `enum` por su nombre, y no por su posición (`ordinal`), evita que reordenar los valores en Java cambie el significado de las filas guardadas.

```bash
mvn test
# ... DbValidate : Successfully validated 1 migration (execution time 00:00.019s)
# ... DbMigrate  : Migrating schema "public" to version "1 - create pieces and loans"
# ... DbMigrate  : Successfully applied 1 migration to schema "public", now at version v1 (execution time 00:00.017s)
# [INFO] Tests run: 75, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 4: los repositorios con JdbcClient

1. Crea `JdbcPieceRepository` en `com.bootcamp.infrastructure`:

   ```java
   @Repository
   public class JdbcPieceRepository implements PieceRepository {

       private final JdbcClient jdbc;

       public JdbcPieceRepository(JdbcClient jdbc) {
           this.jdbc = jdbc;
       }

       @Override
       public Optional<Piece> findByCode(String code) {
           return jdbc.sql("SELECT code, name, declared_value, status FROM pieces WHERE code = :code")
               .param("code", code)
               .query(JdbcPieceRepository::toPiece)
               .optional();
       }

       @Override
       public List<Piece> findAll() {
           return jdbc.sql("SELECT code, name, declared_value, status FROM pieces")
               .query(JdbcPieceRepository::toPiece)
               .list();
       }

       /** Inserta la pieza o, si el código ya existe, la sustituye: lo mismo que put() en un Map. */
       @Override
       public void save(Piece piece) {
           jdbc.sql("""
                   INSERT INTO pieces (code, name, declared_value, status)
                   VALUES (:code, :name, :declaredValue, :status)
                   ON CONFLICT (code) DO UPDATE
                   SET name = EXCLUDED.name, declared_value = EXCLUDED.declared_value, status = EXCLUDED.status
                   """)
               .param("code", piece.code())
               .param("name", piece.name())
               .param("declaredValue", piece.declaredValue())
               .param("status", piece.status().name())
               .update();
       }

       private static Piece toPiece(ResultSet row, int rowNumber) throws SQLException {
           return new Piece(
               row.getString("code"),
               row.getString("name"),
               row.getInt("declared_value"),
               Piece.Status.valueOf(row.getString("status")));
       }
   }
   ```

   - **`JdbcClient`** lo crea Spring Boot a partir del `DataSource` ([lab 03](../2-practicas/lab-03-serviceconnection-con-spring-boot/README.md)).
   - **`save` con `ON CONFLICT ... DO UPDATE`**: el repositorio en memoria hacía `map.put`, que inserta o sustituye. El servicio de la semana 26 llama a `save` tanto para registrar como para cambiar el estado, así que el repositorio de PostgreSQL tiene que hacer lo mismo. `EXCLUDED` es la fila que se intentó insertar.
   - **`toPiece`** convierte una fila en un `Piece`. `Piece.Status.valueOf` lee el `enum` por su nombre.

2. Crea `JdbcLoanRepository` igual: `findOpenLoan` con un `SELECT`, `open` con `INSERT ... ON CONFLICT (piece_code) DO UPDATE` y `close` con `DELETE`. La fecha se lee con `row.getObject("due_date", LocalDate.class)`: el driver convierte `date` en `LocalDate` sin pasar por `java.sql.Date`.

3. Mueve los repositorios en memoria de `src/main` a `src/test`, en el mismo paquete:

   ```bash
   mkdir -p src/test/java/com/bootcamp/infrastructure
   git mv src/main/java/com/bootcamp/infrastructure/InMemoryPieceRepository.java src/test/java/com/bootcamp/infrastructure/
   git mv src/main/java/com/bootcamp/infrastructure/InMemoryLoanRepository.java src/test/java/com/bootcamp/infrastructure/
   ```

   Quítales `@Repository` y su `import`, y cambia su Javadoc: ahora son fakes para los tests. Siguen haciendo falta, porque los tests unitarios del dominio (en el ejemplo, `PieceServiceTest`) usan `InMemoryPieceRepository`.

   Si dejas los dos repositorios en `src/main`, la aplicación tiene dos beans para la misma interfaz y el contexto no arranca:

   ```text
   Caused by: org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type 'com.bootcamp.PieceRepository' available: expected single matching bean but found 2: inMemoryPieceRepository,jdbcPieceRepository
   ```

```bash
mvn test
# [INFO] Tests run: 75, Failures: 0, Errors: 0, Skipped: 0
```

El total no cambia, pero el test de extremo a extremo ya guarda en PostgreSQL.

Commit:

```bash
git add java/src
git commit -m "feat(java): store pieces and loans in PostgreSQL with Flyway and JdbcClient"
```

### Paso 5: el contrato de cada repositorio

Un test de contrato es una clase abstracta con los tests que **cualquier** implementación de la interfaz tiene que pasar. Cada implementación la hereda y solo dice qué objeto probar.

1. Crea `src/test/java/com/bootcamp/infrastructure/PieceRepositoryContract.java`:

   ```java
   /**
    * Lo que cualquier PieceRepository tiene que cumplir. Lo heredan el fake en memoria y el de
    * PostgreSQL: si los dos pasan, los tests unitarios que usan el fake prueban lo mismo que
    * pasaría con la base de datos.
    */
   abstract class PieceRepositoryContract {

       static final Piece ASTROLABE = new Piece("CON-001", "Astrolabio", 40_000, Piece.Status.AVAILABLE);
       static final Piece AMPHORA = new Piece("CON-002", "Ánfora", 8_000, Piece.Status.IN_RESTORATION);

       protected abstract PieceRepository repository();

       @Test
       void shouldReturnEmptyForAnUnknownCode() {
           assertThat(repository().findByCode("CON-404")).isEmpty();
       }

       @Test
       void shouldFindAPieceAfterSavingIt() {
           repository().save(ASTROLABE);

           assertThat(repository().findByCode("CON-001")).contains(ASTROLABE);
       }

       @Test
       void shouldReplaceThePieceWhenSavingTheSameCodeAgain() {
           Piece lent = new Piece("CON-001", "Astrolabio", 40_000, Piece.Status.ON_LOAN);
           repository().save(ASTROLABE);
           repository().save(lent);

           assertThat(repository().findByCode("CON-001")).contains(lent);
           assertThat(repository().findAll()).hasSize(1);
       }

       @Test
       void shouldListEverySavedPiece() {
           repository().save(ASTROLABE);
           repository().save(AMPHORA);

           assertThat(repository().findAll()).containsExactlyInAnyOrder(ASTROLABE, AMPHORA);
       }
   }
   ```

   `repository()` es abstracto: cada subclase devuelve su implementación. Los tests solo usan la interfaz `PieceRepository`.

2. Crea `LoanRepositoryContract` con tres tests: sin préstamo abierto, encontrar el préstamo tras `open` y olvidarlo tras `close`. Necesita además un gancho, porque en PostgreSQL un préstamo exige que su pieza exista (la clave foránea) y en memoria no:

   ```java
   /** En PostgreSQL un préstamo necesita su pieza (clave foránea). El fake la ignora. */
   protected void givenPiece(Piece piece) {
   }

   @BeforeEach
   void savePieceOnLoan() {
       givenPiece(new Piece("CON-010", "Tapiz", 12_000, Piece.Status.ON_LOAN));
   }
   ```

   El método no hace nada por defecto; la subclase de PostgreSQL lo sobrescribe en el paso 6.

3. Crea `InMemoryRepositoriesTest`, que pasa los dos contratos a los fakes con dos clases `@Nested` ([semana 25](../../week-25-junit_6_avanzado_y_assertj/README.md)):

   ```java
   @DisplayName("Repositorios en memoria")
   class InMemoryRepositoriesTest {

       @Nested
       class Pieces extends PieceRepositoryContract {

           private final PieceRepository repository = new InMemoryPieceRepository();

           @Override
           protected PieceRepository repository() {
               return repository;
           }
       }

       @Nested
       class Loans extends LoanRepositoryContract {

           private final LoanRepository repository = new InMemoryLoanRepository();

           @Override
           protected LoanRepository repository() {
               return repository;
           }
       }
   }
   ```

   JUnit crea una instancia de la clase de test por cada test, así que cada test recibe un fake vacío.

```bash
mvn test
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.006 s -- in com.bootcamp.infrastructure.InMemoryRepositoriesTest$Loans
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.003 s -- in com.bootcamp.infrastructure.InMemoryRepositoriesTest$Pieces
# [INFO] Tests run: 82, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 6: el contrato contra PostgreSQL

1. Crea `JdbcPieceRepositoryTest`:

   ```java
   /** El contrato contra PostgreSQL real. @JdbcTest deshace cada test con un rollback. */
   @JdbcTest
   @Import({TestcontainersConfiguration.class, JdbcPieceRepository.class, JdbcLoanRepository.class})
   @DisplayName("JdbcPieceRepository con PostgreSQL")
   class JdbcPieceRepositoryTest extends PieceRepositoryContract {

       @Autowired
       private JdbcPieceRepository repository;

       @Override
       protected PieceRepository repository() {
           return repository;
       }

       @Test
       void shouldRejectANonPositiveDeclaredValueEvenIfTheCodeLetsItThrough() {
           Piece invalid = new Piece("CON-003", "Moneda", 0, Piece.Status.AVAILABLE);

           assertThatThrownBy(() -> repository.save(invalid))
               .isInstanceOf(DataIntegrityViolationException.class)
               .hasMessageContaining("pieces_declared_value_check");
       }
   }
   ```

   - **`@Import` con los dos repositorios**, aunque esta clase solo usa uno: así `JdbcLoanRepositoryTest` tiene exactamente la misma configuración, comparte el contexto y comparte el contenedor ([teoría 03](../1-teoria/03-serviceconnection-en-spring-boot.md#el-contenedor-como-bean)).
   - **El test propio** prueba algo que el fake no tiene: la restricción `CHECK`. Spring traduce el error de PostgreSQL a `DataIntegrityViolationException`, la excepción de Spring para cualquier restricción incumplida.

2. Crea `JdbcLoanRepositoryTest` con la misma cabecera, que sobrescribe `givenPiece` para guardar la pieza con `JdbcPieceRepository`, y un test propio para la clave foránea: abrir un préstamo de una pieza que no existe lanza `DataIntegrityViolationException` con `loans_piece_code_fkey`.

3. Ejecuta:

   ```bash
   mvn test
   # [ERROR]   JdbcPieceRepositoryTest>PieceRepositoryContract.shouldListEverySavedPiece:48
   # [ERROR]   JdbcPieceRepositoryTest>PieceRepositoryContract.shouldReplaceThePieceWhenSavingTheSameCodeAgain:40
   # [ERROR] Tests run: 91, Failures: 2, Errors: 0, Skipped: 0
   ```

   El mensaje de `shouldListEverySavedPiece` muestra una pieza de más, `CON-010`, que no guardó este test sino `JdbcLoanRepositoryTest`. Es la trampa de la [teoría 03](../1-teoria/03-serviceconnection-en-spring-boot.md#la-trampa-los-tests-heredados): los tests heredados del contrato no se ejecutan en la transacción de `@JdbcTest` y confirman sus datos. Si en tu equipo `JdbcPieceRepositoryTest` se ejecuta antes que la clase de préstamos, la suite pasa, y fallará otro día con otro orden. Es el peor tipo de fallo.

4. Añade `@Transactional` (`org.springframework.transaction.annotation.Transactional`) a las dos clases de contrato, y explícalo en su Javadoc:

   ```java
   /**
    * ...
    * <p>@Transactional no hace nada con el fake. Con PostgreSQL hace falta aquí: el @Transactional que
    * trae @JdbcTest solo cubre los métodos declarados en la subclase, no los heredados de esta.
    */
   @Transactional
   abstract class PieceRepositoryContract {
   ```

   ```bash
   mvn test
   # [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.145 s -- in JdbcLoanRepository con PostgreSQL
   # [INFO] Tests run: 91, Failures: 0, Errors: 0, Skipped: 0
   ```

Commit:

```bash
git add java/src/test
git commit -m "test(java): run the repository contracts against the fakes and PostgreSQL"
```

### Paso 7: el SQL se queda en infrastructure

Añade una quinta regla a `ArchitectureTest`:

```java
@ArchTest
static final ArchRule onlyInfrastructureTalksToTheDatabase = noClasses()
    .that().resideOutsideOfPackage("com.bootcamp.infrastructure..")
    .should().dependOnClassesThat().resideInAnyPackage("org.springframework.jdbc..", "java.sql..")
    .because("el SQL vive en los repositorios de infrastructure; el resto habla con sus interfaces");
```

```bash
mvn test
# [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.204 s -- in com.bootcamp.ArchitectureTest
# [INFO] Tests run: 92, Failures: 0, Errors: 0, Skipped: 0
```

Para ver que la regla funciona, añade un campo `private JdbcClient shortcut;` al controlador y ejecuta:

```bash
mvn test -Dtest=ArchitectureTest
# Field <com.bootcamp.web.PieceController.shortcut> has type <org.springframework.jdbc.core.simple.JdbcClient> in (PieceController.java:0)
# [ERROR] Tests run: 5, Failures: 1, Errors: 0, Skipped: 0
```

Bórralo. Commit:

```bash
git add java/src/test
git commit -m "test(java): keep SQL inside the infrastructure package"
```

### Paso 8: la API con el PostgreSQL de compose.yaml

1. Arranca la base de datos desde la raíz del repositorio:

   ```bash
   docker compose up -d --wait
   #  Container qa-<nombre>-<apellido>-db-1 Healthy
   ```

   `--wait` espera a que el `healthcheck` del `compose.yaml` diga que PostgreSQL acepta conexiones.

2. Arranca la API en otra terminal. El puerto 18080 es para no chocar con otros servicios de tu equipo ([semana 28](../../week-28-testing_de_spring_boot/3-proyecto/README.md)):

   ```bash
   cd java
   mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=18080
   # ... Migrating schema "public" to version "1 - create pieces and loans"
   # ... Successfully applied 1 migration to schema "public", now at version v1 (execution time 00:00.015s)
   # ... Started QaApplication in 2.091 seconds (process running for 2.363)
   ```

3. Registra una pieza:

   ```bash
   curl -s -i -X POST localhost:18080/api/pieces -H 'Content-Type: application/json' \
     -d '{"code":"PIN-001","name":"Mona Lisa","declaredValue":900000}' | grep -v '^Date'
   # HTTP/1.1 201
   # Location: /api/pieces/PIN-001
   # Content-Type: application/json
   # Transfer-Encoding: chunked
   #
   # {"code":"PIN-001","name":"Mona Lisa","declaredValue":900000,"status":"AVAILABLE"}
   ```

4. Para la API con `Ctrl+C`, vuelve a arrancarla con el mismo comando y consulta:

   ```bash
   # ... Schema "public" is up to date. No migration necessary.
   curl -s localhost:18080/api/pieces/available
   # [{"code":"PIN-001","name":"Mona Lisa","declaredValue":900000,"status":"AVAILABLE"}]
   curl -s -X POST localhost:18080/api/pieces -H 'Content-Type: application/json' \
     -d '{"code":"PIN-001","name":"Otra","declaredValue":5}'
   # {"detail":"duplicate code: PIN-001","instance":"/api/pieces","status":422,"title":"Unprocessable Content"}
   ```

   En la semana 28 la lista estaba vacía después de reiniciar. Ahora la pieza sigue ahí, y el dominio la usa para rechazar el código repetido.

5. Mira las tablas directamente:

   ```bash
   docker compose exec db psql -U museum -c "SELECT code, status FROM pieces" -c "SELECT version, description, success FROM flyway_schema_history"
   #   code   |  status
   # ---------+-----------
   #  PIN-001 | AVAILABLE
   # (1 row)
   #
   #  version |       description       | success
   # ---------+-------------------------+---------
   #  1       | create pieces and loans | t
   # (1 row)
   ```

   `docker compose exec db psql ...` ejecuta `psql` dentro del contenedor del servicio `db`. Las tablas de Python de la semana 23 están en la misma base de datos y no chocan: tienen otros nombres.

6. Para la API y la base de datos:

   ```bash
   docker compose down
   ```

   Sin `-v`, el volumen con los datos se conserva para la próxima vez.

### Paso 9: lo que el fake no ve

Cada cambio de esta tabla rompe el código. Hazlos uno a uno, ejecuta `mvn test`, anota qué test falla y deshazlo.

| Cambio | Qué falla |
|---|---|
| Quita `ON CONFLICT ... DO UPDATE` de `JdbcPieceRepository.save` | `JdbcPieceRepositoryTest>PieceRepositoryContract.shouldReplaceThePieceWhenSavingTheSameCodeAgain » DuplicateKey` y el test de extremo a extremo con un 500. `InMemoryRepositoriesTest$Pieces` sigue en verde |
| Quita `CHECK (declared_value > 0)` de la migración | `shouldRejectANonPositiveDeclaredValueEvenIfTheCodeLetsItThrough` |
| Quita `@Transactional` de `PieceRepositoryContract` | Los tests del contrato de PostgreSQL, según el orden de las clases (paso 6) |
| Cambia la imagen de `TestcontainersConfiguration` a `postgres:17.6-alpine` | Ningún test: la comprobación `same-image` de `verificar-semana.sh` |

La primera fila es la razón de esta semana: con el fake en memoria, ese error llegaba a producción sin que fallara ningún test. Así falló:

```text
[ERROR]   MuseumApiTest.shouldChargeTheLateDaysAndTheDamageWhenAPieceComesBackLate:68 Status expected:<200 OK> but was:<500 INTERNAL_SERVER_ERROR>
[ERROR]   JdbcPieceRepositoryTest>PieceRepositoryContract.shouldReplaceThePieceWhenSavingTheSameCodeAgain:42 » DuplicateKey PreparedStatementCallback; SQL [INSERT INTO pieces (code, name, declared_va...
[ERROR] Tests run: 92, Failures: 1, Errors: 1, Skipped: 0
```

### Paso 10: verificar y entregar

1. Verifica desde la carpeta del bootcamp (Docker tiene que estar arrancado; el PostgreSQL de `compose.yaml`, no):

   ```bash
   PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 29
   ```

2. Añade al `README.md` de tu repositorio una sección "Base de datos" con: cómo arrancar PostgreSQL (`docker compose up -d --wait`), qué migraciones hay y qué prueba cada clase de test de repositorio.

3. Sube la rama y abre el pull request:

   ```bash
   git push -u origin week-29
   gh pr create --base main --title "Semana 29: repositorios en PostgreSQL con Testcontainers" --body "Repositorios con JdbcClient y Flyway, tests de contrato para el fake y para PostgreSQL, y quinta regla de ArchUnit."
   ```

   En la descripción, cuenta un cambio de la tabla del paso 9 y qué test lo detectó.

## ✅ Requisitos verificables

Copia de [checks.json](../checks.json). Los comprueba `verificar-semana.sh 29`:

| Id | Qué se comprueba |
|---|---|
| `java-suite` | La suite Java pasa con `mvn test` (necesita Docker) |
| `flyway-migration` | Hay al menos una migración de Flyway en `src/main/resources/db/migration` |
| `jdbc-repositories` | Los repositorios de producción usan `JdbcClient` y ninguno guarda en memoria |
| `service-connection` | Los tests conectan PostgreSQL con `@ServiceConnection` |
| `same-image` | Los tests usan la misma imagen de PostgreSQL que `compose.yaml` |
| `no-h2` | El `pom.xml` no usa H2 |
| `contract-tests` | Hay tests de contrato que comparten el fake en memoria y el repositorio JDBC |
| `arch-rules` | La suite tiene al menos 5 reglas `@ArchTest` |
| `no-disabled` | No queda ningún `@Disabled` en `src/test` |
| `test-count` | Al menos 85 ejecuciones de test, sin fallos ni saltados |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - Kafka y el contenedor compartido](../2-practicas/lab-04-kafka-y-contenedor-compartido/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 29](../README.md)
