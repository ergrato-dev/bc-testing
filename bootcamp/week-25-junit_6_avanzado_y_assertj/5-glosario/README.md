# Glosario Semana 25 - JUnit 6 avanzado y AssertJ

## A

- **`argumentSet`**: método de JUnit que crea un caso de `@MethodSource` con un nombre propio.
- **`assertAll()`**: método de `SoftAssertions` que lanza todos los fallos anotados. Sin él, el test pasa aunque haya fallos.
- **`assertSoftly`**: forma de usar soft assertions con una lambda; llama a `assertAll()` al cerrarla.

## C

- **Conversión implícita**: JUnit convierte el texto de `@CsvSource` o `@ValueSource` al tipo del parámetro (`int`, `enum`, `LocalDate`...).
- **`@CsvSource`**: fuente de argumentos donde cada texto es una fila y cada columna, un parámetro.

## E

- **`@EnumSource`**: fuente que ejecuta el test una vez por cada valor de un `enum`.
- **Expresión de etiquetas**: filtro con `&`, `|` y `!` sobre los `@Tag`, por ejemplo `slow & !smoke`.
- **`@ExtendWith`**: registra una extensión de forma declarativa; JUnit crea la instancia.
- **Extensión**: código que JUnit llama en puntos concretos del ciclo de vida de un test.
- **`extracting`**: método de AssertJ que transforma cada elemento de una colección antes de comprobarla.

## G

- **`groups` / `excludedGroups`**: propiedades de Surefire que eligen qué etiquetas ejecutar o excluir (`-Dgroups=smoke`).

## M

- **`@MethodSource`**: fuente que lee los casos de un método `static` que devuelve `Stream<Arguments>`.

## N

- **`@Nested`**: marca una clase interna, no `static`, como grupo de tests con su propio contexto.
- **`@NullAndEmptySource`**: fuente que añade `null` y un valor vacío (`""`, lista vacía).

## P

- **`@ParameterizedTest`**: test que se ejecuta una vez por cada caso de su fuente de argumentos.
- **`ParameterResolver`**: interfaz de extensión que crea los parámetros de un método de test.

## Q

- **`quoteTextArguments`**: atributo de `@ParameterizedTest` en JUnit 6; con `false`, los argumentos de texto salen sin comillas en el nombre de la ejecución.

## R

- **`@RegisterExtension`**: registra una extensión de forma programática, desde un campo que tú creas y configuras.

## S

- **Soft assertion**: aserción que anota el fallo y deja seguir al test; todos los fallos se informan al final.
- **`SoftAssertionsExtension`**: extensión de AssertJ que inyecta un `SoftAssertions` y llama a `assertAll()` al terminar cada test.

## T

- **`@Tag`**: etiqueta un test o una clase para poder filtrarlo al ejecutar.
- **`@TempDir`**: directorio temporal que JUnit crea antes del test y borra al terminar.

## V

- **`@ValueSource`**: fuente de un solo argumento por ejecución (`ints`, `strings`...).

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | [Semana 26 - Java Testing II: Mockito — Mocking en Java](../../week-26-mockito_mocking_en_java/README.md) → |

[↑ Semana 25](../README.md)
