# Lab 04 - Cucumber con Spring Boot y reportes

## Objetivo

Ejecutar escenarios contra una API de Spring Boot arrancada en un puerto aleatorio, con steps que hablan HTTP mediante `RestTestClient`; generar el reporte HTML y el JSON, y filtrar escenarios por tags.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
mvn test
# Escenario: Reservar una sala libre                     # classpath:features/reservas.feature:6
#   ✔ Cuando Luis reserva la sala Sur para el 2026-12-01 # com.bootcamp.BookingSteps.someoneBooks(java.lang.String,java.lang.String,java.time.LocalDate)
#   ✔ Entonces la API responde 201                       # com.bootcamp.BookingSteps.theApiAnswers(int)
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

El código de producción es una API de reservas de salas:

| Archivo | Qué hace |
|---|---|
| `RoomsApplication` | La aplicación Spring Boot |
| `Booking` | `record` con sala, fecha y quién reserva |
| `BookingStore` | `@Repository` en memoria. `book(...)` guarda si la sala está libre ese día; si no, devuelve la reserva que ya había. `onDate(date)` y `clear()` |
| `BookingController` | `POST /api/bookings`: `201` con la reserva, o `409` con un `ProblemDetail` que dice quién tiene la sala. `GET /api/bookings?date=2026-12-01`: las reservas del día, por sala |

Y los tests:

| Archivo | Qué hace |
|---|---|
| `CucumberSpringConfiguration` | `@SpringBootTest(webEnvironment = RANDOM_PORT)` y `@AutoConfigureRestTestClient`: la API en un puerto libre y un cliente HTTP apuntando a él ([semana 28](../../../week-28-testing_de_spring_boot/README.md)) |
| `RunCucumberTest` | La suite, sin `@ConfigurationParameter`: la configuración está en `junit-platform.properties` |
| `junit-platform.properties` | `cucumber.glue` y `cucumber.plugin=pretty` ([teoría 04](../../1-teoria/04-reportes-tags-y-documentacion-viva.md#dónde-se-configura-cucumber)) |
| `ParameterTypes` | `{isoDate}`: una fecha `2026-12-01` como `LocalDate`, para cualquier clase de steps |
| `BookingSteps` | Recibe `RestTestClient` y `BookingStore`. El `@Before` vacía el almacén; `post(...)` guarda la última respuesta en un campo |

El `pom.xml` es el del lab 03 con `spring-boot-starter-webmvc` y `spring-boot-starter-webmvc-test` en lugar de los starters sin web.

## Paso a paso

### Paso 0: un step que habla HTTP

Lee `someoneBooks` y `post` en `BookingSteps`. El paso `Luis reserva la sala Sur para el 2026-12-01` acaba en un `POST` real, con el JSON que Jackson crea a partir del `Booking`. El paso siguiente comprueba el código de la respuesta guardada. Aquí basta un campo de la clase de steps porque solo hay una clase; en el proyecto, con varias, la respuesta irá en un bean `@ScenarioScope` ([teoría 03](../../1-teoria/03-estado-y-cucumber-spring.md#el-estado-compartido-scenarioscope)).

El hook `@Before` llama a `store.clear()`: la API y su almacén viven lo que el contexto, es decir, toda la ejecución.

### Paso 1: el feature completo (PASO 1)

1. Sustituye el contenido de `reservas.feature` por este:

   ```gherkin
   # language: es
   Característica: Reserva de salas
     Una sala se reserva por días completos. Si ya está reservada ese día, la API lo rechaza
     y dice quién la tiene.

     Antecedentes:
       Dado estas reservas:
         | sala  | fecha      | reservada por |
         | Norte | 2026-12-01 | Ana           |

     Escenario: Reservar una sala libre
       Cuando Luis reserva la sala Sur para el 2026-12-01
       Entonces la API responde 201
       Y las reservas del 2026-12-01 son:
         | sala  | fecha      | reservada por |
         | Norte | 2026-12-01 | Ana           |
         | Sur   | 2026-12-01 | Luis          |

     Escenario: Una sala ocupada no se reserva dos veces
       Cuando Luis reserva la sala Norte para el 2026-12-01
       Entonces la API responde 409
       Y el motivo es "Norte already booked on 2026-12-01 by Ana"

     Escenario: La misma sala, otro día
       Cuando Luis reserva la sala Norte para el 2026-12-02
       Entonces la API responde 201
       Y las reservas del 2026-12-02 son:
         | sala  | fecha      | reservada por |
         | Norte | 2026-12-02 | Luis          |
   ```

2. Descomenta todos los bloques del PASO 1 de `BookingSteps`: cuatro de `import`, el `@DataTableType` y tres steps.

   ```bash
   mvn test
   # [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
   ```

El mismo `@DataTableType` sirve para preparar (`estas reservas:` hace un `POST` por fila) y para comprobar (`las reservas del ... son:` compara con lo que devuelve el `GET`). Los escenarios no tocan `BookingStore` salvo en el `@Before`: todo pasa por la API, como lo haría un cliente.

### Paso 2: los reportes (PASO 2)

1. En `junit-platform.properties`, cambia la línea `cucumber.plugin=pretty` por:

   ```properties
   cucumber.plugin=pretty, html:target/cucumber-reports/cucumber.html, json:target/cucumber-reports/cucumber.json
   ```

   y el comentario de encima por `# pretty: los escenarios en la consola. html: reporte para personas. json: para otras herramientas`.

2. Ejecuta y comprueba los archivos:

   ```bash
   mvn test
   # [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
   ls -lh target/cucumber-reports
   # -rw-r--r-- 1 ... 944K ... cucumber.html
   # -rw-r--r-- 1 ... 5.3K ... cucumber.json
   ```

3. Abre `target/cucumber-reports/cucumber.html` en el navegador (`xdg-open`, `open` o `start`, según tu sistema). Despliega un escenario: cada paso con su tabla, y arriba la descripción de la característica. Es lo que puede leer alguien que no abre el código.

### Paso 3: tags (PASO 3)

1. Añade los tags al feature: `@reservas` en la línea anterior a `Característica:`, y `@smoke` en la línea anterior a cada uno de los dos primeros escenarios, con su misma indentación:

   ```gherkin
   # language: es
   @reservas
   Característica: Reserva de salas
   ...
     @smoke
     Escenario: Reservar una sala libre
   ...
     @smoke
     Escenario: Una sala ocupada no se reserva dos veces
   ```

2. Ejecuta con y sin filtro:

   ```bash
   mvn test -Dcucumber.filter.tags="@smoke"
   # [WARNING] Tests run: 3, Failures: 0, Errors: 0, Skipped: 1, Time elapsed: 2.510 s -- in com.bootcamp.RunCucumberTest

   mvn test -Dcucumber.filter.tags="not @smoke"
   # [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 2

   mvn test -Dcucumber.filter.tags="@smoke and @reservas"
   # [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 1
   ```

Lo que el filtro deja fuera cuenta como saltado. `@reservas` está en la característica, así que lo heredan los tres escenarios: `@smoke and @reservas` da lo mismo que `@smoke`. En la salida de `pretty`, cada escenario muestra sus tags encima:

```text
@reservas @smoke
Escenario: Una sala ocupada no se reserva dos veces            # classpath:features/reservas.feature:22
```

## Experimentos

### Experimento 1: sin limpiar

1. Comenta la línea `store.clear();` del hook `@Before` y ejecuta:

   ```bash
   mvn test
   # [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
   ```

   Todo pasa, aunque las reservas se arrastran de un escenario a otro. En el segundo y el tercero, los `Antecedentes` intentan reservar otra vez Norte para el día 1 y la API responde `409`, pero `estas reservas:` no mira la respuesta.

2. Haz que el paso de preparación compruebe lo que hace. En `theseBookings`, cambia `bookings.forEach(this::post);` por:

   ```java
   for (Booking booking : bookings) {
       post(booking);
       theApiAnswers(201);
   }
   ```

   ```bash
   mvn test
   # [ERROR] com.bootcamp.RunCucumberTest.Una sala ocupada no se reserva dos veces -- Time elapsed: 0.037 s <<< FAILURE!
   # org.opentest4j.AssertionFailedError:
   #
   # expected: 201
   #  but was: 409
   # ...
   # [ERROR] Tests run: 3, Failures: 2, Errors: 0, Skipped: 0
   ```

   Ahora el problema se ve donde está: en la preparación. Un paso `Dado` que no comprueba su resultado puede dejar el escenario en un estado distinto del que dice el feature, y los escenarios pasan por casualidad.

3. Descomenta `store.clear();`: vuelve a pasar todo. Deja la comprobación en `theseBookings`; es una mejora.

### Experimento 2: un fallo en el reporte

Cambia el motivo esperado del segundo escenario por `"Norte already booked on 2026-12-01 by Luis"`, ejecuta y abre el reporte HTML. El escenario aparece en rojo con el mensaje de AssertJ debajo del paso que falló. El JSON tiene el mismo resultado con `"status":"failed"`:

```bash
grep -o '"status":"failed"' target/cucumber-reports/cucumber.json
# "status":"failed"
```

Deshaz el cambio.

### Experimento 3: Failsafe

Sigue la [teoría 04](../../1-teoria/04-reportes-tags-y-documentacion-viva.md#surefire-o-failsafe): renombra la suite a `RunCucumberIT` (archivo y clase), añade `maven-failsafe-plugin` al `pom.xml` y ejecuta `mvn test` y después `mvn verify`. Con `mvn test` no se ejecuta ningún escenario; con `mvn verify` los ejecuta Failsafe. Deshaz el cambio.

## Comprobación final

```bash
mvn test
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - Estado compartido con cucumber-spring](../lab-03-estado-compartido-con-cucumber-spring/README.md) | [Proyecto — Proyecto Semanal - Los flujos del dominio en Cucumber](../../3-proyecto/README.md) → |

[↑ Semana 30](../../README.md)
