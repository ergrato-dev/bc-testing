# 02 - @WebMvcTest, MockMvc y MockMvcTester

> Lenguaje: **Java**

---

## 🎯 Objetivos

- Escribir un test de controlador con `@WebMvcTest`, `MockMvc` y `@MockitoBean`.
- Comprobar código HTTP, cabeceras y cuerpo JSON con `status()`, `header()` y `jsonPath()`.
- Escribir las mismas comprobaciones con `MockMvcTester` y AssertJ.
- Probar el contrato de errores: 400 de validación, 404, 409, 415 y 422 con `ProblemDetail`.

---

## De dónde partes

En la [semana 12](../../week-12-apis_rest_con_supertest/README.md) probaste una API Express con Supertest: `request(app).get('/api/...').expect(200)`. `MockMvc` es la misma idea en Spring: envía una petición al controlador sin abrir un puerto y te deja comprobar la respuesta. La diferencia es que aquí los servicios del controlador son mocks, porque el slice no los carga ([teoría 01](01-contexto-de-spring-y-slices.md#slices-arrancar-solo-una-parte)).

---

## Un test de controlador

```java
@WebMvcTest(BookController.class)           // 1. el slice web, solo con este controlador
class BookControllerTest {

    @Autowired
    private MockMvc mvc;                     // 2. lo crea el slice

    @MockitoBean
    private BookService service;             // 3. el servicio del controlador, como mock

    @Test
    void shouldRespond200WithTheBook() throws Exception {
        when(service.find("978-0307474728")).thenReturn(CIEN_ANOS);   // 4. Arrange

        mvc.perform(get("/api/books/978-0307474728"))                 // 5. Act
            .andExpect(status().isOk())                               // 6. Assert
            .andExpect(jsonPath("$.title").value("Cien años de soledad"));
    }
}
```

| Pieza | Qué hace |
|---|---|
| `@WebMvcTest(BookController.class)` | Arranca el slice web con ese controlador. Sin la clase entre paréntesis carga **todos** los controladores, y necesitarías un mock por cada servicio de cada uno |
| `MockMvc` | Envía peticiones al `DispatcherServlet`, el componente de Spring que elige el controlador ([diagrama de la teoría 04](04-springboottest-de-extremo-a-extremo.md)) |
| `get(...)`, `post(...)` | Construyen la petición. Vienen de `MockMvcRequestBuilders` |
| `status()`, `header()`, `jsonPath()`, `content()` | Comprueban la respuesta. Vienen de `MockMvcResultMatchers` |
| `throws Exception` | `perform` declara `Exception`; el test la deja pasar y JUnit la cuenta como error |

### jsonPath

`jsonPath` lee un valor del cuerpo con una expresión JSONPath:

| Expresión | Lee |
|---|---|
| `$.title` | El campo `title` del objeto raíz |
| `$[0].isbn` | El campo `isbn` del primer elemento de una lista |
| `$.length()` | El número de elementos de la lista raíz |
| `$.errors.email` | El campo `email` dentro del objeto `errors` |

### Peticiones con cuerpo

```java
mvc.perform(post("/api/books")
        .contentType(MediaType.APPLICATION_JSON)    // sin esto, Spring responde 415
        .content("""
            {"isbn": "978-0307474728", "title": "Cien años de soledad",
             "author": "Gabriel García Márquez", "year": 1967}
            """))
    .andExpect(status().isCreated())
    .andExpect(header().string("Location", "/api/books/978-0307474728"));

verify(service).add(CIEN_ANOS);   // el JSON llegó al servicio convertido en el record correcto
```

El cuerpo va en un *text block* (`"""`) de Java 15+, que no necesita escapar las comillas. El `verify` comprueba la otra mitad del trabajo del controlador: que convirtió el JSON en el objeto que el servicio espera.

---

## MockMvcTester: las mismas comprobaciones con AssertJ

Desde Spring Framework 6.2 existe `MockMvcTester`, que devuelve un resultado que se comprueba con `assertThat`, como en la [semana 25](../../week-25-junit_6_avanzado_y_assertj/README.md):

```java
@Autowired
private MockMvcTester mvc;

@Test
void shouldExplainWhichFieldIsWrong() {
    assertThat(mvc.post().uri("/api/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"name": "Ana Pérez", "email": "ana", "birthYear": 1995}
                """))
        .hasStatus(HttpStatus.BAD_REQUEST)
        .bodyJson().extractingPath("$.errors.email").isEqualTo("must be a valid email");
}
```

| | `MockMvc` | `MockMvcTester` |
|---|---|---|
| Petición | `mvc.perform(post("/x"))` | `mvc.post().uri("/x")` |
| Código | `.andExpect(status().isCreated())` | `.hasStatus(HttpStatus.CREATED)` |
| Campo JSON | `.andExpect(jsonPath("$.id").value("M-1"))` | `.bodyJson().extractingPath("$.id").isEqualTo("M-1")` |
| `throws Exception` | Sí | No |
| Mensaje de fallo | `Status expected:<422> but was:<400>` | `[HTTP status code] expected: 200 but was: 201` |

Los dos se configuran solos en `@WebMvcTest` y hacen lo mismo. `MockMvc` es el que encontrarás en casi todos los proyectos existentes; `MockMvcTester` encaja mejor con el AssertJ que ya usas. Esta semana practicas los dos: `MockMvc` en el [lab 01](../2-practicas/lab-01-webmvctest-y-mockmvc/README.md) y en el proyecto, `MockMvcTester` en el [lab 02](../2-practicas/lab-02-validacion-con-mockmvctester/README.md).

---

## El contrato de errores

Un cliente de la API necesita saber qué salió mal sin leer el código. Spring tiene un formato estándar para eso, `ProblemDetail` (RFC 9457), y un `@RestControllerAdvice` para traducir cada excepción a un código HTTP:

```java
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    ProblemDetail rejected(ValidationException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, e.getMessage());
    }
}
```

- **Qué:** una clase que recibe las excepciones que salen de cualquier controlador y responde con un `ProblemDetail`.
- **Para qué:** el controlador no tiene `try/catch`, y todas las rutas responden los errores igual.
- **Impacto:** heredar de `ResponseEntityExceptionHandler` añade respuestas `ProblemDetail` para los errores del propio Spring MVC: 400 si el JSON no se puede leer o falla `@Valid`, 415 si el `Content-Type` no es JSON, 405 si el método HTTP no existe para esa ruta.

`@WebMvcTest` carga los `@RestControllerAdvice` aunque no estén en los paréntesis, así que los tests del controlador prueban también el manejo de errores. Una respuesta de error real del proyecto, con la API arrancada en el puerto 18080 ([proyecto, paso 3](../3-proyecto/README.md#paso-3-el-controlador-y-los-errores)):

```bash
curl -s -X POST localhost:18080/api/pieces -H 'Content-Type: application/json' \
  -d '{"code":"pin-1","name":"Mona Lisa","declaredValue":900000}'
# {"detail":"invalid code: pin-1","instance":"/api/pieces","status":422,"title":"Unprocessable Content"}
```

### 400 o 422

| Código | Cuándo | Quién lo decide |
|---|---|---|
| 400 Bad Request | La petición no tiene la forma esperada: JSON roto, campo obligatorio vacío, número negativo | Spring, con `@Valid` y las anotaciones del record (`@NotBlank`, `@Positive`) |
| 422 Unprocessable Content | La forma es correcta, pero una regla del dominio la rechaza: código con formato inválido, valor duplicado | El dominio, con su excepción |
| 404 Not Found | Lo que se pide no existe | El dominio (`NoSuchElementException`) |
| 409 Conflict | La operación choca con el estado actual: pieza ya prestada, correo ya registrado | El dominio (`IllegalStateException`) |

Un test de 400 debe comprobar también que el servicio **no** se llamó: `verifyNoInteractions(service)`. Si la validación deja de funcionar, ese `verify` es lo que lo detecta.

### Los mensajes de validación dependen del idioma

```java
@Email String email                                       // el mensaje lo pone Hibernate Validator
@Email(message = "must be a valid email") String email    // el mensaje es siempre este
```

Sin `message`, Hibernate Validator elige el mensaje según el idioma que pide el cliente en la cabecera `Accept-Language`: `must be a well-formed email address` en inglés y `debe ser una dirección de correo electrónico con formato correcto` en español. Un test que compara ese texto pasa o falla según quién lo ejecute. El lab 02 lo comprueba.

---

## ✅ Resumen

- `@WebMvcTest(Controlador.class)` carga ese controlador, los advices, Jackson y la validación; los servicios van con `@MockitoBean`.
- `MockMvc` comprueba con `andExpect(...)`; `MockMvcTester`, con `assertThat(...)`.
- `jsonPath` lee campos del cuerpo; `content().json(...)` compara el cuerpo entero.
- Un `@RestControllerAdvice` traduce excepciones a `ProblemDetail`; 400 es forma, 422 es regla del dominio.
- Los tests de 400 comprueban que el servicio no se llamó.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 01 - El contexto de Spring en los tests y los slices](01-contexto-de-spring-y-slices.md) | [Teoría — 03 - El contrato JSON con @JsonTest](03-contrato-json-con-jsontest.md) → |

[↑ Semana 28](../README.md)
