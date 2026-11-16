# Lab 01 - @WebMvcTest y MockMvc

## Objetivo

Probar un controlador REST con `@WebMvcTest` y `MockMvc`: códigos HTTP, parámetros, cuerpo JSON de entrada y de salida, y el 404 que produce un `@RestControllerAdvice`. Al final rompes el slice a propósito para ver qué pasa cuando falta un bean.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
mvn test
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

La primera ejecución descarga Spring Boot y tarda unos minutos; las siguientes, segundos.

El código de producción está completo. Es una API de catálogo de una biblioteca:

| Clase | Qué hace |
|---|---|
| `Book` | Record con `isbn`, `title`, `author` y `year` |
| `BookService` | `@Service` con los libros en memoria: `find(isbn)`, `byAuthor(author)` y `add(book)` |
| `BookController` | `GET /api/books/{isbn}`, `GET /api/books?author=...` y `POST /api/books` |
| `LibraryExceptionHandler` | `@RestControllerAdvice`: convierte `BookNotFoundException` en un 404 con `ProblemDetail` |
| `LibraryApplication` | La clase `@SpringBootApplication` que arranca la aplicación |

`src/test/java/com/bootcamp/BookControllerTest.java` trae un test activo, `shouldRespond200WithTheBook`, y los PASOS 1 a 4 comentados. Lee el test activo con la [teoría 02](../../1-teoria/02-webmvctest-y-mockmvc.md#un-test-de-controlador) al lado: cada línea está explicada allí.

Para descomentar un PASO, selecciona sus líneas, también las de los `import` marcados con el mismo PASO, y usa `Ctrl+/`.

## Paso a paso

### Paso 1: el 404 lo produce el advice (PASO 1)

Descomenta el PASO 1.

```java
when(service.find("000")).thenThrow(new BookNotFoundException("000"));

mvc.perform(get("/api/books/000"))
    .andExpect(status().isNotFound())
    .andExpect(jsonPath("$.detail").value("book not found: 000"));
```

El mock lanza la excepción; el controlador no la captura; `LibraryExceptionHandler` la convierte en 404. `@WebMvcTest(BookController.class)` no nombra el advice, pero lo carga igual: el slice incluye todos los `@RestControllerAdvice`.

```bash
mvn test
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 2: un parámetro de consulta (PASO 2)

Descomenta el PASO 2: dos tests y el `import java.util.List`.

- `param("author", "Gabriel García Márquez")` añade `?author=...` a la petición. MockMvc codifica los espacios y las tildes por ti.
- `$.length()` cuenta los elementos de la lista y `$[0].isbn` lee el primero.
- El segundo test no programa el mock: sin `author`, Spring responde 400 antes de llamar al controlador, porque `@RequestParam` es obligatorio por defecto.

```bash
mvn test
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 3: un POST con cuerpo JSON (PASO 3)

Descomenta el PASO 3 y sus cuatro `import`.

El test envía un JSON, comprueba el 201 y la cabecera `Location`, y verifica con qué objeto se llamó al servicio. `verify(service).add(CIEN_ANOS)` funciona porque `Book` es un record: dos records con los mismos valores son `equals`.

```bash
mvn test
# [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

Experimento: en el cuerpo del POST, cambia `"year": 1967` por `"year": 1968`.

```bash
mvn test
# [ERROR]   BookControllerTest.shouldRespond201AndPassTheBodyToTheService:72 » Servlet Request processing failed: java.lang.NullPointerException: Cannot invoke "com.bootcamp.Book.isbn()" because "saved" is null
# [ERROR] Tests run: 5, Failures: 0, Errors: 1, Skipped: 0
```

El JSON llegó como un `Book` con `year` 1968, distinto de `CIEN_ANOS`. El mock no tenía respuesta para ese libro y devolvió `null`, el valor por defecto de Mockito, y el controlador falló al leer `saved.isbn()`. Es un `Errors`, no un `Failures`: el test no llegó a ninguna aserción ([semana 27](../../../week-27-tdd_con_java/1-teoria/01-ciclo-tdd-en-java.md)). Deshaz el cambio.

### Paso 4: el cuerpo entero (PASO 4)

Descomenta el PASO 4 y su `import`.

`content().json(...)` compara el cuerpo completo con un JSON esperado, sin importar el orden de los campos ni los espacios.

```bash
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

Experimento 1: en el JSON esperado del PASO 4, borra `, "year": 1967`. Ejecuta: el test **sigue pasando**. La comparación por defecto es flexible: comprueba que lo esperado está en la respuesta, pero acepta campos de más.

Experimento 2: con el `year` todavía borrado, añade el modo estricto como segundo argumento y su `import`:

```java
import org.springframework.test.json.JsonCompareMode;
```

```java
            .andExpect(content().json("""
                {"isbn": "978-0307474728", "title": "Cien años de soledad",
                 "author": "Gabriel García Márquez"}
                """, JsonCompareMode.STRICT));
```

```bash
mvn test
# [ERROR]   BookControllerTest.shouldRespondWithTheWholeBookAsJson:91
# Unexpected: year
```

En modo estricto, un campo que no esperabas también es un fallo. Úsalo cuando el contrato diga que no puede haber más campos; el flexible, cuando solo te importan algunos. Deshaz los dos cambios.

### Paso 5: qué pasa sin el @MockitoBean

Experimento: comenta la línea `@MockitoBean` del test.

```bash
mvn test
# ... No qualifying bean of type 'com.bootcamp.BookService' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {}
# [ERROR] Tests run: 6, Failures: 0, Errors: 6, Skipped: 0
```

Fallan los seis tests con el mismo error, porque el contexto no arranca. `BookController` pide un `BookService` en su constructor y el slice web no carga servicios ([teoría 01](../../1-teoria/01-contexto-de-spring-y-slices.md#qué-es-el-contexto)). Cuando veas `No qualifying bean of type` en un `@WebMvcTest`, falta un `@MockitoBean`. Deshaz el cambio.

### Paso 6: ver la petición y la respuesta

Añade este `import` y un `.andDo(print())` después del `perform` del PASO 1:

```java
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
```

```java
        mvc.perform(get("/api/books/000"))
            .andDo(print())
            .andExpect(status().isNotFound())
```

```bash
mvn test -Dtest='BookControllerTest#shouldRespond404WhenTheBookDoesNotExist'
```

```text
MockHttpServletRequest:
      HTTP Method = GET
      Request URI = /api/books/000
...
Handler:
             Type = com.bootcamp.BookController
           Method = com.bootcamp.BookController#find(String)
...
Resolved Exception:
             Type = com.bootcamp.BookNotFoundException
...
MockHttpServletResponse:
           Status = 404
     Content type = application/problem+json
             Body = {"detail":"book not found: 000","instance":"/api/books/000","status":404,"title":"Not Found"}
```

`print()` muestra qué método atendió la petición y qué excepción se resolvió: lo que necesitas cuando un test de controlador falla y no sabes por qué. El `Content type` es `application/problem+json`, el tipo de `ProblemDetail`. Quita el `print()` al terminar: llena la salida.

## Comprobación final

```bash
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

Si un PASO no compila o no pasa, comprueba que descomentaste todos sus bloques, también los `import` marcados con su número, y que deshiciste los experimentos anteriores.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Validación y errores con MockMvcTester](../lab-02-validacion-con-mockmvctester/README.md) → |

[↑ Semana 28](../../README.md)
