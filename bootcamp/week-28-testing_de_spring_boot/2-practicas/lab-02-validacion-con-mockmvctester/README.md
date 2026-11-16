# Lab 02 - Validación y errores con MockMvcTester

## Objetivo

Probar el contrato de errores de una API con `MockMvcTester`: el 400 de `@Valid` con un mensaje por campo, el JSON mal escrito, el `Content-Type` equivocado y una regla del dominio que responde 409. Por el camino descubres por qué los mensajes de validación necesitan su propio texto.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
mvn test
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

El código de producción es el registro de socios de la biblioteca:

| Clase | Qué hace |
|---|---|
| `MemberRequest` | Record del cuerpo de `POST /api/members`, con `@NotBlank`, `@Email`, `@Min` y `@Max`. Cada restricción lleva su `message` |
| `MemberController` | `POST /api/members` con `@Valid @RequestBody MemberRequest`: responde 201 con `Location` |
| `MemberService` | `@Service` en memoria. Lanza `DuplicateMemberException` si el correo ya existe |
| `ApiExceptionHandler` | Hereda de `ResponseEntityExceptionHandler`, añade un mapa `errors` al 400 de `@Valid` y convierte `DuplicateMemberException` en 409 |

El `pom.xml` añade `spring-boot-starter-validation`, que trae Hibernate Validator: sin él, `@Valid` no hace nada.

Lee el test activo, `shouldRespond201WithTheNewMember`, con la tabla de `MockMvc` y `MockMvcTester` de la [teoría 02](../../1-teoria/02-webmvctest-y-mockmvc.md#mockmvctester-las-mismas-comprobaciones-con-assertj) al lado. No lleva `throws Exception`.

Experimento antes de empezar: cambia `.hasStatus(HttpStatus.CREATED)` por `.hasStatus(HttpStatus.OK)`.

```bash
mvn test
# [ERROR]   MemberControllerTest.shouldRespond201WithTheNewMember:37 [HTTP status code]
# expected: 200
#  but was: 201
```

El mensaje de fallo es de AssertJ, con la descripción `[HTTP status code]`. Deshaz el cambio.

## Paso a paso

### Paso 1: @Valid para la petición antes del servicio (PASO 1)

Descomenta el PASO 1 y su `import`.

El nombre vacío incumple `@NotBlank`. Spring lanza `MethodArgumentNotValidException` antes de entrar en el método del controlador, y `ResponseEntityExceptionHandler` responde 400. `verifyNoInteractions(service)` comprueba que el servicio no se llamó.

```bash
mvn test
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 2: qué campo falló (PASO 2)

Descomenta el PASO 2.

`bodyJson().extractingPath("$.errors.email")` lee el mensaje del campo `email` dentro del mapa que añade `ApiExceptionHandler.handleMethodArgumentNotValid`.

```bash
mvn test
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

Experimento 1: en `ApiExceptionHandler.java`, borra el método `handleMethodArgumentNotValid` entero (desde `@Override` hasta su `}`).

```bash
mvn test
# [ERROR]   MemberControllerTest.shouldExplainWhichFieldIsWrong:62 [JSON content]
# Expecting:
#   "{"detail":"Invalid request content.","instance":"/api/members","status":400,"title":"Bad Request"}"
# To match JSON path:
```

Sin el método, el 400 sigue llegando, pero su cuerpo solo dice `Invalid request content.`. El cliente no sabe qué campo corregir. Deshaz el cambio.

Experimento 2: en `MemberRequest.java`, quita el mensaje de `@Email`: deja `@Email String email` en lugar de `@Email(message = "must be a valid email") String email`.

```bash
mvn test
# [ERROR]   MemberControllerTest.shouldExplainWhichFieldIsWrong:62
# expected: "must be a valid email"
#  but was: "must be a well-formed email address"
```

Sin `message`, el texto lo pone Hibernate Validator. Ahora añade al test del PASO 2 la cabecera de idioma, después del `.content(...)`:

```java
                    """)
                .header("Accept-Language", "es"))
```

```bash
mvn test
# [ERROR]   MemberControllerTest.shouldExplainWhichFieldIsWrong:63
# expected: "must be a valid email"
#  but was: "debe ser una dirección de correo electrónico con formato correcto"
```

El mismo test, otro idioma, otro texto. Vuelve a poner el `message` en `@Email` y ejecuta con la cabecera todavía puesta: pasa, porque un `message` escrito a mano no se traduce. Por eso cada restricción de `MemberRequest` lleva el suyo. Deshaz el cambio del test.

### Paso 3: todos los errores a la vez (PASO 3)

Descomenta el PASO 3.

El cuerpo tiene dos campos mal: `name` vacío y `birthYear` 1850. `asMap()` convierte el JSON de `errors` en un `Map` de AssertJ:

- `containsOnlyKeys("name", "birthYear")` comprueba que fallaron esos dos y ninguno más. `email` es correcto y no debe aparecer.
- `containsEntry("birthYear", "must be 1900 or later")` comprueba el mensaje de uno.

```bash
mvn test
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
```

Bean Validation comprueba todas las restricciones antes de responder. Un formulario puede marcar todos los campos con error de una vez.

### Paso 4: JSON mal escrito (PASO 4)

Descomenta el PASO 4.

El cuerpo `{"name": "Ana Pérez",` está cortado. Jackson no puede leerlo y Spring responde 400 con `detail` = `Failed to read request`. Este 400 no lleva `errors`: no llegó a haber campos que validar.

```bash
mvn test
# [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 5: Content-Type equivocado (PASO 5)

Descomenta el PASO 5.

El controlador solo acepta JSON. Con `Content-Type: text/plain`, Spring responde 415 Unsupported Media Type sin intentar leer el cuerpo.

```bash
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 6: una regla del dominio (PASO 6)

Descomenta el PASO 6 y su `import`.

El cuerpo es válido, pero el servicio dice que el correo ya existe. `ApiExceptionHandler.duplicate` lo convierte en 409 con el mensaje del dominio en `detail`. La diferencia con el PASO 1: la forma era correcta y quien rechaza es el dominio ([teoría 02](../../1-teoria/02-webmvctest-y-mockmvc.md#400-o-422)).

```bash
mvn test
# [INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
```

## Comprobación final

```bash
mvn test
# [INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
```

Si un PASO no compila o no pasa, comprueba que descomentaste todos sus bloques, también los `import` marcados con su número, y que deshiciste los experimentos anteriores.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - @WebMvcTest y MockMvc](../lab-01-webmvctest-y-mockmvc/README.md) | [Lab 03 - El contrato JSON con @JsonTest](../lab-03-contrato-json-con-jsontest/README.md) → |

[↑ Semana 28](../../README.md)
