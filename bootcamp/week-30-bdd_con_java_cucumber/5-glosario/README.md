# Glosario Semana 30 - BDD con Java (Cucumber)

## A

- **`@After`**: hook que Cucumber ejecuta después de cada escenario, aunque falle. Puede recibir `Scenario`.
- **Antecedentes** (*Background*): pasos que se ejecutan al principio de cada escenario de un feature.

## B

- **`@Before`**: hook que Cucumber ejecuta antes de cada escenario, también antes de los `Antecedentes`. Se usa para limpiar lo que vive más que un escenario.
- **BOM** (*bill of materials*): un `pom` que solo fija versiones. `cucumber-bom` se importa en `dependencyManagement` para que todos los módulos de Cucumber tengan la misma.

## C

- **Cucumber Expression**: el texto de `@Given`, `@When` o `@Then`, con parámetros como `{int}` o `{string}`, texto opcional `(s)` y alternativas `a/b`.
- **`@CucumberContextConfiguration`**: marca la única clase del glue que dice cómo se crea el contexto de Spring de los escenarios. Va junto a `@SpringBootTest`.
- **`cucumber-junit-platform-engine`**: el motor de Cucumber para JUnit Platform. Hace que Surefire y Failsafe ejecuten features.
- **`cucumber-spring`**: módulo de inyección de dependencias que crea las clases de steps como beans de Spring.

## D

- **`DataTable`**: la tabla de un paso, tal cual. `asMaps()` devuelve una lista con un mapa por fila.
- **`@DataTableType`**: método del glue que convierte una fila de tabla en un objeto. Permite recibir `List<Product>` en un step.
- **Documentación viva**: features declarativos que describen el sistema y se ejecutan; si el sistema cambia y el feature no, el escenario falla.

## E

- **Ejemplos** (*Examples*): la tabla de un esquema del escenario. Cada fila es un test.
- **Esquema del escenario** (*Scenario Outline*): un escenario con `<marcadores>` que se ejecuta una vez por fila de `Ejemplos`.

## F

- **Failsafe**: plugin de Maven para tests de integración. Ejecuta las clases `*IT` en `mvn verify`.
- **Fragmento** (*snippet*): el código que Cucumber sugiere para un paso indefinido.

## G

- **Glue**: el código que une el Gherkin con Java: clases de steps, hooks, tipos de parámetro y la clase de configuración. Se indica con `cucumber.glue`.

## J

- **`junit-platform.properties`**: archivo de `src/test/resources` con la configuración de JUnit Platform y de sus motores, Cucumber incluido.

## N

- **Naming strategy**: cómo nombra el motor de Cucumber cada test. Con `long`, el nombre lleva feature, escenario y ejemplo, y Surefire cuenta bien los fallos.

## P

- **Paso ambiguo**: una línea del feature con la que casan dos steps. El escenario falla con `AmbiguousStepDefinitionsException`.
- **Paso indefinido**: una línea del feature con la que no casa ningún step. El escenario falla con `UndefinedStepException` y Cucumber sugiere el fragmento.
- **`@ParameterType`**: método del glue que define un tipo para las expresiones, como `{pesos}` o `{isoDate}`, con una expresión regular.
- **Plugin** (de Cucumber): lo que informa de los resultados: `pretty`, `html:archivo`, `json:archivo`, `summary`.
- **Proxy de ámbito**: la subclase que Spring inyecta en lugar de un bean `@ScenarioScope`. Pasa cada llamada a un método a la instancia del escenario actual; sus campos no.

## S

- **`Scenario`**: parámetro opcional de un hook. Da el nombre, los tags y el estado del escenario, y `log(...)`.
- **`@ScenarioScope`**: ámbito de `cucumber-spring`: una instancia del bean por escenario. Se usa para compartir estado entre clases de steps.
- **Step definition**: método anotado con `@Given`, `@When` o `@Then` que ejecuta un paso del feature.
- **`@Suite`**: anotación de `junit-platform-suite`. La clase que la lleva le dice a JUnit Platform qué motor y qué features ejecutar.

## T

- **Tag**: palabra con `@` encima de una característica, escenario o tabla de ejemplos. `cucumber.filter.tags` filtra por tags con `and`, `or` y `not`.
- **`TestClock`**: el `Clock` de test del proyecto, que los escenarios ponen en una fecha y adelantan días.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | — |

[↑ Semana 30](../README.md)
