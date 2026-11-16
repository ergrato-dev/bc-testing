# Glosario Semana 28 - Testing de Spring Boot

## A

- **`@AutoConfigureMockMvc`**: añadida a `@SpringBootTest`, configura `MockMvc` y `MockMvcTester` contra la aplicación entera, sin servidor.
- **`@AutoConfigureRestTestClient`**: añadida a `@SpringBootTest`, configura un `RestTestClient` con la dirección del servidor del test.

## B

- **Bean**: objeto que crea y gestiona el contexto de Spring, y que recibe sus dependencias por el constructor.
- **Bean Validation**: estándar de Jakarta para validar objetos con anotaciones (`@NotBlank`, `@Email`, `@Positive`). Su implementación es Hibernate Validator.

## C

- **Caché de contextos**: Spring guarda cada contexto que arranca y lo reutiliza en las clases de test con la misma configuración.
- **Contexto (`ApplicationContext`)**: contenedor que crea los beans de una aplicación Spring y los conecta entre sí.
- **Contrato JSON**: los nombres, tipos y formatos de los campos que una API envía y recibe. El cliente depende de ellos.

## D

- **`@DirtiesContext`**: marca un test o una clase para que Spring descarte su contexto al terminar. Cada uso cuesta un arranque.
- **`DispatcherServlet`**: componente de Spring MVC que recibe cada petición y la envía al método del controlador que corresponde.

## J

- **`JacksonTester`**: utilidad de `@JsonTest` que escribe objetos como JSON y lee JSON como objetos, con aserciones de AssertJ.
- **`@JsonTest`**: slice que arranca solo Jackson con la configuración `spring.jackson.*` de la aplicación.
- **`jsonPath`**: expresión que lee un valor de un JSON, como `$.title` o `$[0].isbn`.

## M

- **`MockMvc`**: cliente de test que envía peticiones al `DispatcherServlet` sin abrir un puerto. Comprueba con `andExpect(...)`.
- **`MockMvcTester`**: versión de `MockMvc` con aserciones de AssertJ (`assertThat(...).hasStatus(...)`). Desde Spring Framework 6.2.
- **`@MockitoBean`**: pone un mock de Mockito en el contexto en lugar del bean de ese tipo. Sustituye a `@MockBean`, que no existe en Spring Boot 4.

## P

- **Parent de Spring Boot (`spring-boot-starter-parent`)**: `pom.xml` del que hereda el proyecto y que fija las versiones de todas las librerías que Spring Boot gestiona.
- **`ProblemDetail`**: cuerpo de error estándar (RFC 9457) con `title`, `status`, `detail` e `instance`. Su tipo de contenido es `application/problem+json`.

## R

- **`RANDOM_PORT`**: entorno web de `@SpringBootTest` que arranca Tomcat en un puerto libre distinto en cada ejecución.
- **`ResponseEntityExceptionHandler`**: clase base de Spring para un `@RestControllerAdvice` que ya responde con `ProblemDetail` a los errores de Spring MVC (400, 405, 415...).
- **`@RestControllerAdvice`**: clase que recibe las excepciones de todos los controladores y las convierte en respuestas HTTP.
- **`RestTestClient`**: cliente HTTP de test de Spring Framework 7. Con `RANDOM_PORT`, envía peticiones reales al servidor del test.

## S

- **Slice**: test que arranca solo los beans de una capa, como `@WebMvcTest` o `@JsonTest`.
- **`@SpringBootTest`**: arranca la aplicación entera en un test.
- **Starter**: dependencia de Spring Boot que agrupa las librerías de una tarea, como `spring-boot-starter-webmvc`.

## T

- **`@TestBean`**: sustituye un bean del contexto por el objeto que devuelve un método `static` del test con el nombre del campo.

## W

- **`@WebMvcTest`**: slice que arranca un controlador, sus advices, Jackson y la validación, sin servicios ni repositorios.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | — |

[↑ Semana 28](../README.md)
