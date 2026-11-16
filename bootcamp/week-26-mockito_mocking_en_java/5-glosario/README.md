# Glosario Semana 26 - Mockito

## A

- **Agente de Java (`-javaagent`)**: programa que la JVM carga antes que cualquier clase. Mockito lo necesita para modificar clases ya cargadas.
- **`ArgumentCaptor`**: objeto de Mockito que recoge los argumentos de una llamada durante `verify`. Con `@Captor` lo crea `MockitoExtension`.

## B

- **BDDMockito**: clase de `mockito-core` con los mismos métodos de Mockito con nombres de BDD: `given`, `willReturn`, `then().should()`.

## D

- **`doReturn`**: forma de preparar un stub sin llamar al método. Obligatoria en spies; también sirve para métodos `void` (`doThrow`, `doNothing`).

## E

- **Entrada indirecta**: valor que el servicio lee de una dependencia. Se controla con un stub.

## F

- **Fake**: doble con una implementación que funciona de verdad pero es más simple que la real, como un repositorio en memoria.

## I

- **`@InjectMocks`**: crea el objeto bajo prueba pasando los mocks del test a su constructor.
- **Inline mock maker**: el creador de mocks por defecto desde Mockito 5. Permite hacer mocks de clases `final` y métodos estáticos.
- **`InOrder`**: objeto de Mockito que comprueba el orden de las llamadas entre uno o más mocks.

## L

- **`lenient()`**: marca un stub como permisivo: no da error si no se usa.

## M

- **Matcher**: argumento que acepta un conjunto de valores (`any()`, `anyString()`, `eq(...)`). Si un argumento es un matcher, todos deben serlo.
- **`@Mock`**: campo que `MockitoExtension` llena con un mock nuevo antes de cada test.
- **`mockStatic`**: crea un mock de los métodos estáticos de una clase, válido hasta que se cierra.
- **`MockitoExtension`**: extensión de JUnit (`mockito-junit-jupiter`) que crea mocks, captores y spies, y aplica strict stubbing.

## P

- **`PotentialStubbingProblem`**: error de strict stubbing: el servicio llamó a un método con stub, pero con otros argumentos.

## R

- **Respuesta por defecto**: lo que devuelve un mock sin stub: `0`, `false`, `Optional.empty()`, colección vacía o `null`.

## S

- **Salida indirecta**: llamada que el servicio hace a una dependencia. Se comprueba con `verify`.
- **`@Spy`**: doble que envuelve un objeto real: ejecuta su código y registra las llamadas.
- **Strict stubbing**: modo de `MockitoExtension` que convierte en error los stubs sin usar o con otros argumentos.
- **Stub**: respuesta programada para una llamada: `when(...).thenReturn(...)`.

## U

- **`UnnecessaryStubbingException`**: error de strict stubbing: un stub que el test nunca usó.

## V

- **`verify`**: comprueba que el mock recibió una llamada, cuántas veces y con qué argumentos.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | — |

[↑ Semana 26](../README.md)
