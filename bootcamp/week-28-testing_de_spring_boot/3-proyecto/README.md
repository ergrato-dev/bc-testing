# Proyecto Semanal - La API Spring Boot del dominio

> **🎯 ÚNICO ENTREGABLE**: este proyecto es el único entregable obligatorio de la semana.

## Objetivo

Tu carpeta `java/` pasa a ser una API REST con Spring Boot, sin tocar el dominio ni sus tests. Al terminar tendrá:

- los servicios de las semanas 26 y 27 como beans, creados en una clase `@Configuration` sin anotar el dominio;
- un controlador con sus errores en formato `ProblemDetail`;
- tres capas de tests nuevas: `@WebMvcTest` para el controlador, `@JsonTest` para el contrato JSON y `@SpringBootTest` para el flujo completo;
- una cuarta regla de ArchUnit que impide que Spring entre en el dominio.

Los repositorios siguen en memoria. En la [semana 29](../../week-29-testcontainers_en_java/3-proyecto/README.md) se cambian por PostgreSQL con Testcontainers.

## Dónde va

En la carpeta `java/` de tu repositorio `qa-<nombre>-<apellido>` ([proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)), sobre la entrega de la [semana 27](../../week-27-tdd_con_java/3-proyecto/README.md).

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

La API expone las operaciones que ya tienes. En el ejemplo del museo:

| Ruta | Operación del dominio | Respuesta correcta | Errores |
|---|---|---|---|
| `POST /api/pieces` | `PieceService.register` (semana 26) | 201 con `Location` | 400 forma, 422 regla |
| `GET /api/pieces/available` | `PieceService.available` | 200 con la lista | — |
| `POST /api/pieces/{code}/loan` | `PieceService.lend` (semana 26) | 200 con la pieza | 404, 409, 422, 503 |
| `POST /api/pieces/{code}/return` | `ReturnService.receive` (semana 27) | 200 con los cargos y su total | 400, 404 |

Una ruta por cada operación pública de tus servicios. Si tu dominio tiene más, añádelas; si tiene menos, al menos tres rutas con dos métodos HTTP distintos.

## Paso a paso

### Paso 1: rama y pom.xml de Spring Boot

1. Crea la rama:

   ```bash
   cd qa-<nombre>-<apellido>
   git switch main && git pull
   git switch -c week-28
   ```

2. Copia el starter:

   ```bash
   cp -r <ruta-al-bootcamp>/bootcamp/week-28-testing_de_spring_boot/3-proyecto/starter/. .
   ```

   | Archivo | Qué es |
   |---|---|
   | `java/pom.xml` | **Sustituye** al tuyo. Hereda de `spring-boot-starter-parent` 4.1.1 ([teoría 01](../1-teoria/01-contexto-de-spring-y-slices.md#el-pomxml-de-un-proyecto-spring-boot)) y conserva ArchUnit y el agente de Mockito. Si tenías otras dependencias, vuelve a añadirlas, sin `<version>` si Spring Boot las gestiona |
   | `java/src/main/java/com/bootcamp/QaApplication.java` | La clase `@SpringBootApplication`. Spring busca beans en `com.bootcamp` y en sus subpaquetes |
   | `java/src/main/resources/application.properties` | La configuración de la aplicación |
   | `java/src/main/java/com/bootcamp/web/ApiExceptionHandler.java` | El advice de errores, con un `TODO` que completas en el paso 3 |

3. Ejecuta:

   ```bash
   cd java
   mvn test
   # [INFO] Tests run: 57, Failures: 0, Errors: 0, Skipped: 0
   ```

   Los 57 tests de la semana 27 (los de tu dominio pueden ser otro número) siguen pasando: el parent trae las mismas versiones de JUnit, AssertJ y Mockito.

4. Commit:

   ```bash
   git add -A
   git commit -m "build: move java/ to Spring Boot 4.1.1"
   ```

### Paso 2: el dominio como beans, sin Spring dentro

El dominio sigue siendo Java plano. Spring lo conoce por tres caminos:

1. **Las implementaciones de las interfaces del dominio**, en `com.bootcamp.infrastructure`, con `@Repository` o `@Component`. En el ejemplo:

   | Clase | Implementa | Cómo |
   |---|---|---|
   | `InMemoryPieceRepository` | `PieceRepository` | Es el fake de `PieceServiceTest`: muévelo de `src/test` a `src/main/java/com/bootcamp/infrastructure/`, hazlo `public`, cambia el `HashMap` por un `ConcurrentHashMap` (Tomcat atiende varias peticiones a la vez) y ponle `@Repository`. `PieceServiceTest` solo necesita un `import` nuevo |
   | `InMemoryLoanRepository` | `LoanRepository` | Un `ConcurrentHashMap` de préstamos abiertos por código de pieza. La interfaz gana un método `open(Loan)` para poder crearlos |
   | `LogNotifier` | `Notifier` | Escribe los avisos en el log con SLF4J |
   | `ConfiguredInstitutionRegistry` | `InstitutionRegistry` | Lee las instituciones acreditadas de `application.properties` |

   ```java
   @Component
   public class ConfiguredInstitutionRegistry implements InstitutionRegistry {

       private final List<String> accredited;

       public ConfiguredInstitutionRegistry(@Value("${museum.accredited-institutions}") List<String> accredited) {
           this.accredited = List.copyOf(accredited);
       }

       @Override
       public boolean isAccredited(String institutionId) {
           return accredited.contains(institutionId);
       }
   }
   ```

   ```properties
   # application.properties
   spring.application.name=qa-java
   museum.accredited-institutions=MUS-PRADO,MUS-LOUVRE
   ```

2. **Los servicios**, en una clase `@Configuration` de `com.bootcamp.config`:

   ```java
   @Configuration
   public class ServiceConfig {

       @Bean
       Clock clock() {
           return Clock.systemDefaultZone();
       }

       @Bean
       PieceService pieceService(PieceRepository pieces, InstitutionRegistry registry, Notifier notifier) {
           return new PieceService(pieces, registry, notifier);
       }

       @Bean
       ReturnService returnService(PieceRepository pieces, LoanRepository loans, Notifier notifier, Clock clock) {
           return new ReturnService(pieces, loans, notifier, clock);
       }
   }
   ```

   - **Qué:** cada método `@Bean` crea un servicio con `new`, como hacían tus tests; Spring le pasa los parámetros buscando beans de esos tipos.
   - **Para qué:** `PieceService` y `ReturnService` no llevan `@Service` ni ninguna anotación de Spring. Sus tests unitarios no cambian y siguen sin arrancar Spring.
   - **Impacto:** el `Clock` también es un bean. En el paso 6 el test lo sustituye por uno fijo.

3. **Una regla de ArchUnit que lo protege.** Añádela a tu `ArchitectureTest`:

   ```java
   @ArchTest
   static final ArchRule domainDoesNotKnowSpring = noClasses()
       .that().resideInAPackage("com.bootcamp")
       .and().doNotHaveSimpleName("QaApplication")
       .should().dependOnClassesThat().resideInAPackage("org.springframework..")
       .because("Spring vive en web, config e infrastructure; el dominio se prueba sin arrancar Spring");
   ```

   `resideInAPackage("com.bootcamp")`, sin `..`, es el paquete exacto: el dominio. Los subpaquetes `web`, `config` e `infrastructure` sí pueden usar Spring.

```bash
mvn test
# [INFO] Tests run: 58, Failures: 0, Errors: 0, Skipped: 0
```

Comprueba la regla: pon `@Service` encima de `PieceService` y ejecuta. Falla `domainDoesNotKnowSpring`. Quítalo.

Commit: `feat: wire the domain as Spring beans`.

### Paso 3: el controlador y los errores

En `com.bootcamp.web`:

1. **Un record por cuerpo de petición**, con Bean Validation para la forma:

   ```java
   public record PieceRequest(@NotBlank String code, @NotBlank String name, @Positive int declaredValue) {
   }
   ```

   Las reglas del dominio (el formato del código, los duplicados) siguen en el servicio. La [teoría 02](../1-teoria/02-webmvctest-y-mockmvc.md#400-o-422) explica qué va en cada sitio.

2. **El controlador**, sin lógica: recibe, llama al servicio y responde.

   ```java
   @RestController
   @RequestMapping("/api/pieces")
   public class PieceController {

       private final PieceService pieces;
       private final ReturnService returns;

       public PieceController(PieceService pieces, ReturnService returns) {
           this.pieces = pieces;
           this.returns = returns;
       }

       @PostMapping
       public ResponseEntity<Piece> register(@Valid @RequestBody PieceRequest request) {
           Piece piece = pieces.register(request.code(), request.name(), request.declaredValue());
           return ResponseEntity.created(URI.create("/api/pieces/" + piece.code())).body(piece);
       }

       @PostMapping("/{code}/return")
       public ReturnResponse receive(@PathVariable String code, @Valid @RequestBody ReturnRequest request) {
           return ReturnResponse.from(returns.receive(code, request.condition()));
       }

       // GET /available y POST /{code}/loan, igual
   }
   ```

   `ReturnResponse` es un record de respuesta con `total`: `ReturnNotice.total()` es un método, no un componente, y Jackson no lo publica ([teoría 03](../1-teoria/03-contrato-json-con-jsontest.md#el-json-es-un-contrato)).

3. **El advice**: completa el `TODO` de `ApiExceptionHandler` con un `@ExceptionHandler` por cada excepción de tu dominio.

   | Excepción del ejemplo | Código |
   |---|---|
   | `ValidationException` | 422 `UNPROCESSABLE_CONTENT` |
   | `NoSuchElementException` | 404 `NOT_FOUND` |
   | `IllegalStateException` | 409 `CONFLICT` |
   | `RegistryUnavailableException` | 503 `SERVICE_UNAVAILABLE` |

4. **Arranca la API y pruébala a mano.** Sin opciones, Spring Boot escucha en el puerto 8080, que en muchos equipos ya usa otro programa. Arráncala en el 18080:

   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=18080
   # ... Tomcat started on port 18080 (http) with context path '/'
   # ... Started QaApplication in 1.108 seconds (process running for 1.32)
   ```

   `-Dspring-boot.run.arguments` pasa argumentos a la aplicación; `--server.port` sustituye el puerto sin tocar `application.properties`.

   En otra terminal:

   ```bash
   curl -i -X POST localhost:18080/api/pieces -H 'Content-Type: application/json' \
     -d '{"code":"PIN-001","name":"Mona Lisa","declaredValue":900000}'
   # HTTP/1.1 201
   # Location: /api/pieces/PIN-001
   # Content-Type: application/json
   # ...
   # {"code":"PIN-001","name":"Mona Lisa","declaredValue":900000,"status":"AVAILABLE"}

   curl -s -X POST localhost:18080/api/pieces -H 'Content-Type: application/json' \
     -d '{"code":"pin-1","name":"Mona Lisa","declaredValue":900000}'
   # {"detail":"invalid code: pin-1","instance":"/api/pieces","status":422,"title":"Unprocessable Content"}

   curl -s -X POST localhost:18080/api/pieces -H 'Content-Type: application/json' \
     -d '{"code":"PIN-002","name":"","declaredValue":900000}'
   # {"detail":"Invalid request content.","instance":"/api/pieces","status":400,"title":"Bad Request"}

   curl -s localhost:18080/api/pieces/available
   # [{"code":"PIN-001","name":"Mona Lisa","declaredValue":900000,"status":"AVAILABLE"}]
   ```

   Para la aplicación con `Ctrl+C`. Lo que acabas de comprobar a mano es lo que los pasos 4 a 6 convierten en tests.

Commit: `feat: expose the domain as a REST API`.

### Paso 4: el controlador con @WebMvcTest

Crea `src/test/java/com/bootcamp/web/PieceControllerTest.java` con `@WebMvcTest(PieceController.class)`, `MockMvc` y un `@MockitoBean` por cada servicio del controlador ([lab 01](../2-practicas/lab-01-webmvctest-y-mockmvc/README.md)).

Un test por cada fila de la tabla de rutas: la respuesta correcta y cada error. En el ejemplo, 11 tests en tres `@Nested`:

```java
@Test
void shouldRespond400WithoutCallingTheServiceWhenTheNameIsBlank() throws Exception {
    mvc.perform(post("/api/pieces")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"code": "PIN-001", "name": " ", "declaredValue": 900000}
                """))
        .andExpect(status().isBadRequest());

    verifyNoInteractions(pieces);
}

@Test
void shouldRespond422WithTheReasonWhenTheDomainRejectsThePiece() throws Exception {
    when(pieces.register(anyString(), anyString(), anyInt()))
        .thenThrow(new ValidationException("invalid code: pin-1"));

    mvc.perform(post("/api/pieces")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"code": "pin-1", "name": "Mona Lisa", "declaredValue": 900000}
                """))
        .andExpect(status().isUnprocessableContent())
        .andExpect(jsonPath("$.detail").value("invalid code: pin-1"));
}
```

Si una clase de tu dominio tiene métodos de paquete (en el ejemplo, `Piece.onLoan()`), el test, que está en `com.bootcamp.web`, no los ve: construye el objeto con su constructor.

```bash
mvn test
# [INFO] Tests run: 69, Failures: 0, Errors: 0, Skipped: 0
```

Commit: `test: cover the controller with WebMvcTest`.

### Paso 5: el contrato JSON con @JsonTest

Crea `src/test/java/com/bootcamp/web/JsonContractTest.java` con `@JsonTest` y un `JacksonTester` por cada tipo que viaja en la API ([lab 03](../2-practicas/lab-03-contrato-json-con-jsontest/README.md)). En el ejemplo, tres tests:

| Test | Qué fija |
|---|---|
| `shouldPublishTheTotalOfAReturn` | `ReturnResponse` publica `total` |
| `shouldWriteThePieceStatusAsText` | El JSON entero de una `Piece`, con el estado como texto (`isEqualToJson`) |
| `shouldReadTheConditionOfAReturn` | `{"condition": "DAMAGED"}` se lee como `Condition.DAMAGED` |

```bash
mvn test
# [INFO] Tests run: 72, Failures: 0, Errors: 0, Skipped: 0
```

Commit: `test: pin the JSON contract`.

### Paso 6: el flujo completo con @SpringBootTest

Crea `src/test/java/com/bootcamp/MuseumApiTest.java` (con el nombre de tu dominio) con `@SpringBootTest(webEnvironment = RANDOM_PORT)`, `@AutoConfigureRestTestClient` y el `Clock` sustituido con `@TestBean` ([lab 04](../2-practicas/lab-04-springboottest-y-resttestclient/README.md)):

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class MuseumApiTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 3, 20);

    @TestBean
    private Clock clock;

    static Clock clock() {
        return Clock.fixed(Instant.parse("2026-03-20T10:00:00Z"), ZoneOffset.UTC);
    }

    @Autowired
    private RestTestClient client;

    @Autowired
    private LoanRepository loans;

    @Test
    void shouldChargeTheLateDaysAndTheDamageWhenAPieceComesBackLate() {
        register("API-002", 10_000);
        client.post().uri("/api/pieces/API-002/loan")
            .body(Map.of("institutionId", "MUS-PRADO", "days", 30))
            .exchange()
            .expectStatus().isOk();
        loans.open(new Loan("API-002", "MUS-PRADO", TODAY.minusDays(3)));

        ReturnResponse response = client.post().uri("/api/pieces/API-002/return")
            .body(Map.of("condition", "DAMAGED"))
            .exchange()
            .expectStatus().isOk()
            .expectBody(ReturnResponse.class)
            .returnResult().getResponseBody();

        assertThat(response).isEqualTo(new ReturnResponse("MUS-PRADO", "API-002", 3, 150, 1_000, 1_150));
    }
}
```

En el ejemplo, `lend` no crea el préstamo en `LoanRepository` (así quedó en la semana 26), y el test lo abre con `loans.open(...)`, con un vencimiento de hace tres días. Si en tu dominio la operación principal ya crea el registro, no hace falta.

Tres tests: registrar y listar, el flujo de la operación principal y su cierre con sus cálculos, y un rechazo que depende de `application.properties` (en el ejemplo, una institución que no está en `museum.accredited-institutions`). Cada test usa sus propios códigos (`API-001`, `API-002`...) porque el contexto y sus repositorios se comparten ([teoría 04](../1-teoria/04-springboottest-de-extremo-a-extremo.md#tests-que-comparten-el-contexto)).

```bash
mvn test
# [INFO] Tests run: 75, Failures: 0, Errors: 0, Skipped: 0
```

El total depende de tu dominio; debe ser 70 o más, sin `Skipped`.

Commit: `test: cover the whole API with SpringBootTest`.

### Paso 7: comprobar que la suite detecta errores

Haz estos cambios en el código de producción, uno cada vez. Con cada uno, `mvn test` debe fallar; si alguno pasa en verde, falta un test. Deshaz cada cambio antes del siguiente.

| Cambio | Resultado en el ejemplo |
|---|---|
| Quita `@Valid` de `register` en el controlador | `PieceControllerTest.shouldRespond400...` falla con un `NullPointerException`: la petición llega al mock, que devuelve `null` |
| Cambia `HttpStatus.UNPROCESSABLE_CONTENT` por `HttpStatus.BAD_REQUEST` en el advice | Fallan un test de `PieceControllerTest` y uno de `MuseumApiTest`: `Status expected:<422> but was:<400>` |
| En `ReturnResponse.from`, pasa `notice.lateFee()` como `total` | Fallan `JsonContractTest` y `PieceControllerTest`: `JSON path "$.total" expected:<90150> but was:<150>` |
| Cambia la `Location` por `URI.create("/api/pieces")` | `Response header 'Location' expected:</api/pieces/PIN-001> but was:</api/pieces>` |

### Paso 8: verificar y entregar

Desde la raíz del bootcamp:

```bash
PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 28
```

Añade al `README.md` de tu repositorio la tabla de rutas de tu API (como la de "Tu dominio asignado") y un párrafo: qué prueba cada una de las tres clases de test nuevas y qué no prueba ninguna de ellas.

Sube la rama y abre un pull request a `main`.

## ✅ Requisitos verificables

Copia de [checks.json](../checks.json):

| Comprobación | Pista |
|---|---|
| La suite Java pasa con `mvn test` | Pasos 1 a 7 |
| El `pom.xml` usa `spring-boot-starter-parent` como parent | Paso 1 |
| Los errores de la API se responden con `ProblemDetail` | Paso 3 |
| Hay un test del controlador con `@WebMvcTest` y `@MockitoBean` | Paso 4 |
| Hay un test del contrato JSON con `@JsonTest` | Paso 5 |
| Hay un test de extremo a extremo con `@SpringBootTest` en un puerto aleatorio | Paso 6 |
| La suite tiene al menos 4 reglas `@ArchTest` | Paso 2 |
| No queda ningún `@Disabled` en `src/test` | Pasos 4 a 6 |
| Al menos 70 ejecuciones de test, sin fallos ni saltados | Paso 6 |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - @SpringBootTest y RestTestClient](../2-practicas/lab-04-springboottest-y-resttestclient/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 28](../README.md)
