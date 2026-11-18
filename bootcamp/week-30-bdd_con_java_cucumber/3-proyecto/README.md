# Proyecto Semanal - Los flujos del dominio en Cucumber

> **🎯 ÚNICO ENTREGABLE**: este proyecto es el único entregable obligatorio de la semana.

## Objetivo

Tu API ya tiene tests unitarios, de controlador, de repositorio y de extremo a extremo. Esta semana describes en Gherkin los flujos de negocio principales del dominio y los ejecutas contra la aplicación completa. Al terminar, tu carpeta `java/` tendrá:

- al menos **tres features en español**, uno por flujo de negocio, con al menos 12 escenarios entre todos;
- steps repartidos en varias clases que comparten la última respuesta con un bean `@ScenarioScope`;
- tablas convertidas con `@DataTableType` y al menos un `Esquema del escenario`;
- los escenarios contra la API en un puerto aleatorio y PostgreSQL en un contenedor, con un reloj que los escenarios mueven;
- el reporte HTML y el JSON de Cucumber en `target/cucumber-reports/`.

Y quizá algo más: en el museo, el feature de devolución encontró un hueco que ningún test había visto.

## Dónde va

En la carpeta `java/` de tu repositorio `qa-<nombre>-<apellido>` ([proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)), sobre la entrega de la [semana 29](../../week-29-testcontainers_en_java/3-proyecto/README.md).

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

El ejemplo es el museo, con tres flujos: registrar piezas, prestarlas y recibir su devolución. Elige los tres flujos principales de tu dominio, los que contarías a alguien de negocio en cinco minutos, y escribe un feature para cada uno. Los steps del museo te sirven de modelo; las frases y las tablas tienen que ser las de tu dominio.

## Paso a paso

### Paso 1: rama, starter y la suite vacía

1. Crea la rama:

   ```bash
   cd qa-<nombre>-<apellido>
   git switch main && git pull
   git switch -c week-30
   ```

2. Copia el starter:

   ```bash
   cp -r <ruta-al-bootcamp>/bootcamp/week-30-bdd_con_java_cucumber/3-proyecto/starter/. .
   ```

   | Archivo | Qué es |
   |---|---|
   | `java/pom.xml` | **Sustituye** al tuyo. Añade el BOM de Cucumber 8.1.0 en `dependencyManagement` y, para los tests, `cucumber-java`, `cucumber-spring`, `cucumber-junit-platform-engine` y `junit-platform-suite` ([teoría 01](../1-teoria/01-cucumber-en-java.md#las-dependencias)). Si añadiste dependencias propias, vuelve a ponerlas |
   | `java/src/test/resources/junit-platform.properties` | El glue (`com.bootcamp.bdd`), los plugins `pretty`, `html` y `json`, y los nombres largos de los tests ([teoría 04](../1-teoria/04-reportes-tags-y-documentacion-viva.md#dónde-se-configura-cucumber)) |
   | `java/src/test/java/com/bootcamp/bdd/RunCucumberTest.java` | La suite: los features del paquete `features` |
   | `java/src/test/java/com/bootcamp/bdd/CucumberSpringConfiguration.java` | La aplicación en un puerto aleatorio, con el PostgreSQL de `TestcontainersConfiguration` (semana 29), `RestTestClient` y un `TestClock` como `@Primary` ([teoría 03](../1-teoria/03-estado-y-cucumber-spring.md#con-la-aplicación-completa)) |
   | `java/src/test/java/com/bootcamp/bdd/TestClock.java` | Un `Clock` que los escenarios ponen en una fecha (`setToday`) y adelantan (`advanceDays`) |
   | `java/src/test/java/com/bootcamp/bdd/ScenarioState.java` | Bean `@ScenarioScope` con el código y el cuerpo de la última respuesta. `record(...)` la guarda; `expectStatus(...)` la comprueba y, si falla, enseña el cuerpo |

   Si tu clase con `@SpringBootApplication` no está en `com.bootcamp`, cambia el paquete de las clases de `bdd` para que quede por debajo del suyo: el escaneo de componentes tiene que encontrar `ScenarioState`.

3. Ejecuta la suite (con Docker en marcha):

   ```bash
   cd java
   mvn test
   # [ERROR] com.bootcamp.bdd.RunCucumberTest -- Time elapsed: 0.005 s <<< ERROR!
   # org.junit.platform.suite.engine.NoTestsDiscoveredException: Suite [com.bootcamp.bdd.RunCucumberTest] did not discover any tests
   # [ERROR] Tests run: 93, Failures: 0, Errors: 1, Skipped: 0
   ```

   Es el fallo esperado: la suite existe, pero aún no hay features. Una suite vacía es un error y no un aviso: así, si un día los features desaparecen o cambian de carpeta, el build lo dice.

### Paso 2: lo común a todos los features

Crea `java/src/test/java/com/bootcamp/bdd/ApiSteps.java` con lo que todos los features usan: el hook que limpia la base de datos y pone el reloj en una fecha conocida, los pasos del reloj y los que comprueban la respuesta.

```java
package com.bootcamp.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import org.springframework.jdbc.core.simple.JdbcClient;

/** Lo común a todos los features: base de datos limpia, el reloj y la respuesta de la API. */
public class ApiSteps {

    private final JdbcClient jdbc;
    private final TestClock clock;
    private final ScenarioState state;

    public ApiSteps(JdbcClient jdbc, TestClock clock, ScenarioState state) {
        this.jdbc = jdbc;
        this.clock = clock;
        this.state = state;
    }

    /** @SpringBootTest no deshace nada: cada escenario empieza con las tablas vacías. */
    @Before
    public void startWithAnEmptyMuseum() {
        jdbc.sql("TRUNCATE loans, pieces").update();
        clock.setToday(LocalDate.of(2026, 3, 20));
    }

    @ParameterType("\\d{4}-\\d{2}-\\d{2}")
    public LocalDate isoDate(String text) {
        return LocalDate.parse(text);
    }

    @Given("hoy es {isoDate}")
    public void todayIs(LocalDate today) {
        clock.setToday(today);
    }

    @When("pasa(n) {int} día(s)")
    public void daysGoBy(int days) {
        clock.advanceDays(days);
    }

    @Then("la API responde {int}")
    public void theApiAnswers(int status) {
        state.expectStatus(status);
    }

    @Then("el motivo es {string}")
    public void theReasonIs(String detail) {
        assertThat(state.body()).contains("\"detail\":\"" + detail + "\"");
    }
}
```

- `TRUNCATE loans, pieces` vacía las dos tablas del museo de una vez. Pon las tablas de tu dominio: todas las que escriban tus escenarios. Si no las pones todas, un escenario verá filas de otro ([teoría 03](../1-teoria/03-estado-y-cucumber-spring.md#lo-que-guarda-un-singleton)).
- El reloj se pone cada vez en el 20 de marzo de 2026: así ningún escenario depende del día en que se ejecuta. Si tu dominio no usa fechas, borra `todayIs`, `daysGoBy`, `isoDate` y la línea del reloj del hook.
- `el motivo es {string}` busca el `detail` del `ProblemDetail` que devuelve tu `ApiExceptionHandler` desde la semana 28.

Aún no hay features, así que el resultado de `mvn test` no cambia.

### Paso 3: el primer feature

Crea la carpeta `java/src/test/resources/features/` y, en ella, el feature del primer flujo. En el museo, el registro de piezas:

```gherkin
# language: es
Característica: Registro de piezas
  El museo registra cada pieza con un código de tres letras, un guion y tres cifras, un nombre y
  su valor declarado. Una pieza recién registrada queda disponible para préstamo.

  Escenario: Una pieza registrada queda disponible
    Cuando registro la pieza "MUS-001" "Astrolabio" con un valor declarado de 250000
    Entonces la API responde 201
    Y las piezas disponibles son:
      | código  | nombre     | valor declarado | estado    |
      | MUS-001 | Astrolabio | 250000          | AVAILABLE |

  Escenario: Las piezas disponibles salen ordenadas por código
    Dado estas piezas registradas:
      | código  | nombre     | valor declarado |
      | MUS-003 | Tapiz      | 12000           |
      | MUS-001 | Astrolabio | 250000          |
      | MUS-002 | Códice     | 90000           |
    Entonces las piezas disponibles son:
      | código  | nombre     | valor declarado |
      | MUS-001 | Astrolabio | 250000          |
      | MUS-002 | Códice     | 90000           |
      | MUS-003 | Tapiz      | 12000           |

  Escenario: Un código no se registra dos veces
    Dado estas piezas registradas:
      | código  | nombre     | valor declarado |
      | MUS-001 | Astrolabio | 250000          |
    Cuando registro la pieza "MUS-001" "Otra pieza" con un valor declarado de 1000
    Entonces la API responde 422
    Y el motivo es "duplicate code: MUS-001"

  Esquema del escenario: Datos que el museo rechaza
    Cuando registro la pieza "<código>" "<nombre>" con un valor declarado de <valor>
    Entonces la API responde 422
    Y el motivo es "<motivo>"
    Y no hay piezas disponibles

    Ejemplos:
      | código   | nombre     | valor | motivo                          |
      | MUS-1    | Astrolabio | 1000  | invalid code: MUS-1             |
      | mus-001  | Astrolabio | 1000  | invalid code: mus-001           |
      | MUSEO-01 | Astrolabio | 1000  | invalid code: MUSEO-01          |
```

Fíjate en cómo está escrito ([teoría 04](../1-teoria/04-reportes-tags-y-documentacion-viva.md#documentación-viva)):

- La descripción de la `Característica` cuenta la regla con palabras del negocio.
- Ningún paso menciona URLs, JSON ni nombres de campos. `la API responde 422` es la excepción: el código HTTP es parte del contrato de la API.
- El `Esquema del escenario` recorre los datos que el museo rechaza, uno por fila.

Los steps, en `java/src/test/java/com/bootcamp/bdd/PieceSteps.java`:

```java
package com.bootcamp.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import com.bootcamp.Piece;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.servlet.client.RestTestClient;

public class PieceSteps {

    private final RestTestClient client;
    private final ScenarioState state;

    public PieceSteps(RestTestClient client, ScenarioState state) {
        this.client = client;
        this.state = state;
    }

    /** Una fila con columnas código, nombre y valor declarado. El estado es opcional. */
    @DataTableType
    public Piece piece(Map<String, String> row) {
        String status = row.getOrDefault("estado", "AVAILABLE");
        return new Piece(row.get("código"), row.get("nombre"), Integer.parseInt(row.get("valor declarado")),
            Piece.Status.valueOf(status));
    }

    @Given("estas piezas registradas:")
    public void thesePieces(List<Piece> pieces) {
        for (Piece piece : pieces) {
            register(piece.code(), piece.name(), piece.declaredValue());
            state.expectStatus(201);
        }
    }

    @When("registro la pieza {string} {string} con un valor declarado de {int}")
    public void iRegister(String code, String name, int declaredValue) {
        register(code, name, declaredValue);
    }

    @Then("las piezas disponibles son:")
    public void theAvailablePiecesAre(List<Piece> expected) {
        List<Piece> available = client.get().uri("/api/pieces/available")
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<Piece>>() { })
            .returnResult().getResponseBody();
        assertThat(available).containsExactlyElementsOf(expected);
    }

    @Then("no hay piezas disponibles")
    public void thereAreNoAvailablePieces() {
        theAvailablePiecesAre(List.of());
    }

    private void register(String code, String name, int declaredValue) {
        state.record(client.post().uri("/api/pieces")
            .body(Map.of("code", code, "name", name, "declaredValue", declaredValue))
            .exchange()
            .expectBody(String.class)
            .returnResult());
    }
}
```

- El `@DataTableType` convierte cada fila en un `Piece`, el `record` que devuelve la API. Sirve para preparar (`estas piezas registradas:`) y para comprobar (`las piezas disponibles son:`). La columna `estado` es opcional y vale `AVAILABLE` si falta.
- `estas piezas registradas:` comprueba que cada registro devuelve `201`. Un paso `Dado` que no comprueba lo que hace deja escenarios que pasan por casualidad ([lab 04, experimento 1](../2-practicas/lab-04-cucumber-con-spring-boot-y-reportes/README.md#experimento-1-sin-limpiar)).
- Cada petición se guarda con `state.record(...)` para que `ApiSteps` la compruebe.

```bash
mvn test
# Esquema del escenario: Datos que el museo rechaza                                # classpath:features/registro_de_piezas.feature:41
#   ✔ Cuando registro la pieza "MUS-1" "Astrolabio" con un valor declarado de 1000 # com.bootcamp.bdd.PieceSteps.iRegister(java.lang.String,java.lang.String,int)
#   ✔ Entonces la API responde 422                                                 # com.bootcamp.bdd.ApiSteps.theApiAnswers(int)
#   ✔ Y el motivo es "invalid code: MUS-1"                                         # com.bootcamp.bdd.ApiSteps.theReasonIs(java.lang.String)
#   ✔ Y no hay piezas disponibles                                                  # com.bootcamp.bdd.PieceSteps.thereAreNoAvailablePieces()
# ...
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.733 s -- in com.bootcamp.bdd.RunCucumberTest
# [INFO] Tests run: 98, Failures: 0, Errors: 0, Skipped: 0
```

Tres escenarios y tres ejemplos: seis tests más. El valor `0` no está en los ejemplos a propósito: lo rechaza `@Positive` en `PieceRequest` con un `400`, antes de llegar al dominio. Esas validaciones de `@Valid` son detalle de la API y las prueban los tests de `@WebMvcTest` de la semana 28; el feature cuenta las reglas del museo.

### Paso 4: el segundo feature

El préstamo, en `prestamo.feature`:

```gherkin
# language: es
Característica: Préstamo de piezas
  Una pieza disponible se presta a una institución acreditada. Mientras está prestada no se
  puede volver a prestar ni aparece entre las disponibles.

  Antecedentes:
    Dado estas piezas registradas:
      | código  | nombre     | valor declarado |
      | MUS-001 | Astrolabio | 250000          |
      | MUS-002 | Códice     | 90000           |

  Escenario: Prestar una pieza a una institución acreditada
    Cuando presto la pieza MUS-001 a MUS-PRADO por 30 días
    Entonces la API responde 200
    Y las piezas disponibles son:
      | código  | nombre | valor declarado |
      | MUS-002 | Códice | 90000           |

  Escenario: Una pieza prestada no se presta otra vez
    Dado la pieza MUS-001 está prestada a MUS-PRADO por 30 días
    Cuando presto la pieza MUS-001 a MUS-LOUVRE por 10 días
    Entonces la API responde 409
    Y el motivo es "piece already on loan: MUS-001"

  Escenario: Solo se presta a instituciones acreditadas
    Cuando presto la pieza MUS-001 a MUS-DESCONOCIDO por 30 días
    Entonces la API responde 422
    Y el motivo es "institution not accredited: MUS-DESCONOCIDO"

  Escenario: Una pieza que no existe no se presta
    Cuando presto la pieza MUS-999 a MUS-PRADO por 30 días
    Entonces la API responde 404
    Y el motivo es "unknown piece: MUS-999"
```

Los `Antecedentes` registran dos piezas para todos los escenarios. `Dado la pieza MUS-001 está prestada...` y `Cuando presto la pieza...` hacen la misma petición, pero el `Dado` comprueba que salió bien y el `Cuando` deja la comprobación al `Entonces`. Los steps, en `LoanSteps.java`:

```java
package com.bootcamp.bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import java.util.Map;
import org.springframework.test.web.servlet.client.RestTestClient;

public class LoanSteps {

    private final RestTestClient client;
    private final ScenarioState state;

    public LoanSteps(RestTestClient client, ScenarioState state) {
        this.client = client;
        this.state = state;
    }

    @Given("la pieza {word} está prestada a {word} por {int} días")
    public void thePieceIsOnLoan(String code, String institution, int days) {
        lend(code, institution, days);
        state.expectStatus(200);
    }

    @When("presto la pieza {word} a {word} por {int} días")
    public void iLend(String code, String institution, int days) {
        lend(code, institution, days);
    }

    private void lend(String code, String institution, int days) {
        state.record(client.post().uri("/api/pieces/{code}/loan", code)
            .body(Map.of("institutionId", institution, "days", days))
            .exchange()
            .expectBody(String.class)
            .returnResult());
    }
}
```

```bash
mvn test
# [INFO] Tests run: 102, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 5: la devolución

El tercer flujo recorre los otros dos: registrar, prestar, dejar pasar el tiempo y devolver. En `devolucion.feature`:

```gherkin
# language: es
Característica: Devolución de piezas
  La institución devuelve la pieza prestada. Por cada día de retraso se cobran 50, con un tope
  de 1500. Si vuelve dañada, se cobra el 10 % de su valor declarado y la pieza pasa a
  restauración en lugar de quedar disponible.

  Antecedentes:
    Dado hoy es 2026-03-20
    Y estas piezas registradas:
      | código  | nombre | valor declarado |
      | MUS-002 | Códice | 10000           |
    Y la pieza MUS-002 está prestada a MUS-PRADO por 30 días

  Escenario: Devolución a tiempo y en buen estado
    Cuando pasan 30 días
    Y MUS-PRADO devuelve la pieza MUS-002 en buen estado
    Entonces la devolución cobra:
      | días de retraso | 0 |
      | recargo         | 0 |
      | daño            | 0 |
      | total           | 0 |
    Y las piezas disponibles son:
      | código  | nombre | valor declarado |
      | MUS-002 | Códice | 10000           |

  Escenario: Una pieza dañada pasa a restauración
    Cuando pasan 10 días
    Y MUS-PRADO devuelve la pieza MUS-002 dañada
    Entonces la devolución cobra:
      | días de retraso | 0    |
      | recargo         | 0    |
      | daño            | 1000 |
      | total           | 1000 |
    Y no hay piezas disponibles

  Esquema del escenario: El recargo por retraso tiene un tope
    Cuando pasan <días> días
    Y MUS-PRADO devuelve la pieza MUS-002 en buen estado
    Entonces la devolución cobra:
      | días de retraso | <retraso> |
      | recargo         | <recargo> |
      | daño            | 0         |
      | total           | <recargo> |

    Ejemplos:
      | días | retraso | recargo |
      | 31   | 1       | 50      |
      | 33   | 3       | 150     |
      | 60   | 30      | 1500    |
      | 90   | 60      | 1500    |

  Escenario: Una pieza que no está prestada no se devuelve
    Cuando MUS-PRADO devuelve la pieza MUS-999 en buen estado
    Entonces la API responde 404
    Y el motivo es "no open loan for piece: MUS-999"
```

- `pasan 30 días` mueve el `TestClock`. Con un `Clock.fixed` no se podría.
- Los cargos van en una tabla de dos columnas, que el step recibe como `Map<String, Long>` ([teoría 02](../1-teoria/02-tablas-tipos-y-esquemas.md#dos-columnas-sin-cabecera-map)).
- `{estado}` es un tipo de parámetro propio: el feature dice `dañada`, no `DAMAGED` ([teoría 02](../1-teoria/02-tablas-tipos-y-esquemas.md#tipos-de-parámetro-propios)).

Los steps, en `ReturnSteps.java`. `JsonMapper` es el conversor de JSON de Jackson 3 que Spring Boot ya tiene como bean:

```java
package com.bootcamp.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import com.bootcamp.Condition;
import com.bootcamp.web.ReturnResponse;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Map;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.json.JsonMapper;

public class ReturnSteps {

    private final RestTestClient client;
    private final JsonMapper json;
    private final ScenarioState state;

    public ReturnSteps(RestTestClient client, JsonMapper json, ScenarioState state) {
        this.client = client;
        this.json = json;
        this.state = state;
    }

    /** {estado} reconoce "en buen estado" y "dañada" y los convierte en el Condition de la API. */
    @ParameterType(name = "estado", value = "en buen estado|dañada")
    public Condition condition(String text) {
        return text.equals("dañada") ? Condition.DAMAGED : Condition.GOOD;
    }

    @When("{word} devuelve la pieza {word} {estado}")
    public void theInstitutionReturns(String institution, String code, Condition condition) {
        state.record(client.post().uri("/api/pieces/{code}/return", code)
            .body(Map.of("condition", condition))
            .exchange()
            .expectBody(String.class)
            .returnResult());
    }

    @Then("la devolución cobra:")
    public void theReturnCharges(Map<String, Long> expected) {
        state.expectStatus(200);
        ReturnResponse charges = json.readValue(state.body(), ReturnResponse.class);
        assertThat(Map.of(
            "días de retraso", charges.lateDays(),
            "recargo", (long) charges.lateFee(),
            "daño", (long) charges.damageFee(),
            "total", (long) charges.total()))
            .isEqualTo(expected);
    }
}
```

```bash
mvn test
# Escenario: Devolución a tiempo y en buen estado              # classpath:features/devolucion.feature:14
#   ✔ Dado hoy es 2026-03-20                                   # com.bootcamp.bdd.ApiSteps.todayIs(java.time.LocalDate)
#   ✔ Y estas piezas registradas:                              # com.bootcamp.bdd.PieceSteps.thesePieces(java.util.List)
#   ✔ Y la pieza MUS-002 está prestada a MUS-PRADO por 30 días # com.bootcamp.bdd.LoanSteps.thePieceIsOnLoan(java.lang.String,java.lang.String,int)
#   ✔ Cuando pasan 30 días                                     # com.bootcamp.bdd.ApiSteps.daysGoBy(int)
#   ✔ Y MUS-PRADO devuelve la pieza MUS-002 en buen estado     # com.bootcamp.bdd.ReturnSteps.theInstitutionReturns(java.lang.String,java.lang.String,com.bootcamp.Condition)
#   ✘ Entonces la devolución cobra:                            # com.bootcamp.bdd.ReturnSteps.theReturnCharges(java.util.Map)
#         org.opentest4j.AssertionFailedError: [response body: {"detail":"no open loan for piece: MUS-002","instance":"/api/pieces/MUS-002/return","status":404,"title":"Not Found"}]
#         expected: 200
#          but was: 404
# ...
# [ERROR] Tests run: 109, Failures: 6, Errors: 0, Skipped: 0
```

Fallan seis de los siete escenarios de devolución: todos los que devuelven una pieza prestada. La API presta la pieza (`200`), pero al devolverla dice que no tiene ningún préstamo abierto.

No es un error del feature. `PieceService.lend` guarda la pieza como prestada y avisa a la institución, pero nunca llama a `loans.open(...)`. Ningún test lo había visto:

- Los tests unitarios de `lend` (semanas 26 y 27) comprueban lo que `lend` hace, no lo que le falta.
- Los de `ReturnService` (`PieceReturnTest`) preparan el préstamo abierto con un mock de `LoanRepository`.
- `MuseumApiTest` (semana 28) abre el préstamo a mano con `loans.open(...)` después del préstamo por HTTP.

El escenario de devolución es el primer test que recorre préstamo y devolución por la API, como un usuario. Es lo que BDD aporta: describir el flujo entero con las palabras del negocio saca a la luz los huecos entre piezas que, cada una por separado, funcionan.

En tu dominio puede que no aparezca ningún hueco. Si aparece, no cambies el feature para que pase: arregla el código, como en el paso 6.

### Paso 6: el arreglo, con TDD

El escenario dice qué falla; el arreglo se hace con un ciclo de TDD como los de la [semana 27](../../week-27-tdd_con_java/README.md), con un test unitario.

1. **Red.** En `PieceLendingTest`, añade el repositorio de préstamos y un reloj. `@InjectMocks` pasa al constructor los `@Mock` y también los `@Spy`, así que un `Clock.fixed` real llega como reloj:

   ```java
   @Mock
   private LoanRepository loans;

   /** @InjectMocks también pasa los @Spy al constructor: un reloj real, parado el 20 de marzo. */
   @Spy
   private Clock clock = Clock.fixed(Instant.parse("2026-03-20T10:00:00Z"), ZoneOffset.UTC);
   ```

   y el test:

   ```java
   @Test
   @DisplayName("should open the loan until today plus the days")
   void shouldOpenTheLoanUntilTodayPlusTheDays() {
       // given
       given(pieces.findByCode("MUS-001")).willReturn(Optional.of(ASTROLABE));
       given(registry.isAccredited("INST-7")).willReturn(true);

       // when
       service.lend("MUS-001", "INST-7", 30);

       // then
       then(loans).should().open(new Loan("MUS-001", "INST-7", LocalDate.of(2026, 4, 19)));
   }
   ```

   Los `import` nuevos: `java.time.Clock`, `java.time.Instant`, `java.time.LocalDate`, `java.time.ZoneOffset` y `org.mockito.Spy`.

   ```bash
   mvn test -Dtest=PieceLendingTest
   # [ERROR] com.bootcamp.PieceLendingTest.shouldOpenTheLoanUntilTodayPlusTheDays -- Time elapsed: 0.580 s <<< FAILURE!
   # Wanted but not invoked:
   # loans.open(
   #     Loan[pieceCode=MUS-001, institutionId=INST-7, dueDate=2026-04-19]
   # );
   # -> at com.bootcamp.PieceLendingTest.shouldOpenTheLoanUntilTodayPlusTheDays(PieceLendingTest.java:86)
   # Actually, there were zero interactions with this mock.
   ```

2. **Green.** `PieceService` recibe el repositorio de préstamos y el reloj, y `lend` abre el préstamo después de guardar la pieza:

   ```java
   public PieceService(PieceRepository pieces, LoanRepository loans, InstitutionRegistry registry,
                       Notifier notifier, Clock clock) {
       this.pieces = pieces;
       this.loans = loans;
       this.registry = registry;
       this.notifier = notifier;
       this.clock = clock;
   }
   ```

   ```java
   Piece lent = piece.onLoan();
   pieces.save(lent);
   loans.open(new Loan(code, institutionId, LocalDate.now(clock).plusDays(days)));
   notifier.send(new LoanNotice(institutionId, code, days, premium));
   ```

   Con sus dos campos (`private final LoanRepository loans;` y `private final Clock clock;`) y los `import` de `java.time.Clock` y `java.time.LocalDate`. La fecha sale de `LocalDate.now(clock)`, nunca de `LocalDate.now()`: la regla de ArchUnit de la semana 27 lo comprueba.

3. El constructor cambió, así que hay que cambiar quien lo llama:

   - `ServiceConfig`:

     ```java
     @Bean
     PieceService pieceService(PieceRepository pieces, LoanRepository loans, InstitutionRegistry registry,
                               Notifier notifier, Clock clock) {
         return new PieceService(pieces, loans, registry, notifier, clock);
     }
     ```

   - `PieceServiceTest`, que crea el servicio a mano con los fakes:

     ```java
     service = new PieceService(new InMemoryPieceRepository(), new InMemoryLoanRepository(),
         mock(InstitutionRegistry.class), mock(Notifier.class), Clock.systemUTC());
     ```

     con los `import` de `com.bootcamp.infrastructure.InMemoryLoanRepository` y `java.time.Clock`.

4. Ejecuta todo:

   ```bash
   mvn test
   # [INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.124 s -- in com.bootcamp.bdd.RunCucumberTest
   # [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.945 s -- in com.bootcamp.ArchitectureTest
   # [INFO] Tests run: 110, Failures: 0, Errors: 0, Skipped: 0
   ```

   Es el doble bucle de la semana 27: el escenario (bucle externo) falló primero; el test unitario (bucle interno) guio el arreglo, y el escenario pasó sin tocarlo.

5. Añade el ciclo al final de tu `TDD-LOG.md`:

   ```markdown
   | 18 | `shouldOpenTheLoanUntilTodayPlusTheDays` | `Wanted but not invoked: loans.open(...)`: el escenario de devolución de la semana 30 respondía 404 porque `lend` nunca abría el préstamo | `PieceService` recibe `LoanRepository` y `Clock`, y `lend` abre el préstamo hasta hoy más los días | — | `fix: open the loan when lending a piece` |
   ```

`MuseumApiTest` sigue llamando a `loans.open(...)` después del préstamo. Ya no hace falta para abrirlo, pero sigue haciendo falta para cambiar el vencimiento a tres días antes de hoy: `open` es un *upsert* (semana 29). Puedes dejarlo así.

### Paso 7: el reporte y los tags

1. Abre el reporte:

   ```bash
   xdg-open target/cucumber-reports/cucumber.html   # macOS: open; Windows: start
   ```

   Léelo como lo leería alguien de negocio. Si un escenario no se entiende sin abrir el código, reescríbelo.

2. Marca con `@smoke` un escenario de cada feature, el que cuenta el camino feliz, y ejecuta solo esos:

   ```bash
   mvn test -Dtest=RunCucumberTest -Dcucumber.filter.tags="@smoke"
   # [INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 14
   ```

   Los que no tienen el tag salen como `Skipped`. `-Dtest=RunCucumberTest` ejecuta solo la suite de Cucumber; sin él, Maven ejecuta también el resto de tests.

3. Comprueba para qué sirve la estrategia `long`. Rompe un ejemplo a propósito: en el esquema de `devolucion.feature`, cambia el recargo de la fila de `31` días de `50` a `51`. Ejecuta `mvn test -Dtest=RunCucumberTest` dos veces: con la línea `cucumber.junit-platform.naming-strategy=long` de `junit-platform.properties` y con ella comentada.

   ```bash
   # Con naming-strategy=long:
   # [ERROR] com.bootcamp.bdd.RunCucumberTest.Devolución de piezas - El recargo por retraso tiene un tope - Ejemplos - Example #1.1 -- Time elapsed: 0.055 s <<< FAILURE!
   # [ERROR] Tests run: 17, Failures: 1, Errors: 0, Skipped: 0
   # Con la línea comentada:
   # [ERROR] Tests run: 16, Failures: 1, Errors: 0, Skipped: 0
   ```

   Sin nombres largos, el ejemplo que falla se llama `Example #1.1`, igual que el primer ejemplo de `registro_de_piezas.feature`, y Surefire cuenta los dos como uno ([teoría 04](../1-teoria/04-reportes-tags-y-documentacion-viva.md#el-nombre-de-cada-test)). Deja la línea descomentada y el `50`.

### Paso 8: verificar y entregar

1. Verifica desde la carpeta del bootcamp (Docker tiene que estar arrancado; el PostgreSQL de `compose.yaml`, no):

   ```bash
   PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 30
   ```

2. Añade al `README.md` de tu repositorio una sección "Escenarios BDD" con: cómo ejecutarlos (`mvn test -Dtest=RunCucumberTest`), dónde queda el reporte HTML y una línea por feature que diga qué flujo describe.

3. Sube la rama y abre el pull request:

   ```bash
   git push -u origin week-30
   gh pr create --base main --title "Semana 30: flujos del dominio en Cucumber" --body "Tres features en español contra la API completa, con PostgreSQL en un contenedor y reloj controlado."
   ```

   En la descripción, cuenta si algún escenario encontró un hueco y cómo lo arreglaste. Si no encontró ninguno, cuenta qué escenario te costó más escribir de forma declarativa.

## ✅ Requisitos verificables

Copia de [checks.json](../checks.json). Los comprueba `verificar-semana.sh 30`:

| Id | Qué se comprueba |
|---|---|
| `java-suite` | La suite Java pasa con `mvn test` (necesita Docker) |
| `three-features` | Hay al menos 3 archivos `.feature` en `src/test/resources` |
| `spanish-gherkin` | Todos los features están escritos en español (`# language: es`) |
| `suite-runner` | Una clase `@Suite` ejecuta el motor de Cucumber |
| `cucumber-spring` | Los escenarios arrancan Spring Boot con `@CucumberContextConfiguration` |
| `scenario-scope` | Las clases de steps comparten estado con un bean `@ScenarioScope` |
| `data-tables` | Los steps convierten tablas con `@DataTableType` |
| `scenario-outline` | Al menos un feature usa Esquema del escenario con Ejemplos |
| `reports` | `mvn test` deja el reporte HTML y el JSON de Cucumber en `target/cucumber-reports` |
| `scenario-count` | Al menos 12 escenarios ejecutados (los ejemplos de cada esquema cuentan) |
| `no-disabled` | No queda ningún `@Disabled` en `src/test` |
| `test-count` | Al menos 100 ejecuciones de test, sin fallos ni saltados |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - Cucumber con Spring Boot y reportes](../2-practicas/lab-04-cucumber-con-spring-boot-y-reportes/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 30](../README.md)
