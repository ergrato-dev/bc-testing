# Lab 03 - El contrato JSON con @JsonTest

## Objetivo

Fijar con tests el JSON de un préstamo: el formato de las fechas, los nombres de los campos, lo que no se publica y lo que se ignora al leer. Después cambias el record a propósito y ves qué test lo detecta.

## Tiempo estimado

45 minutos.

## Preparación

```bash
cd starter
mvn test
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

Este lab no tiene controlador. Solo hay un record y la configuración de Jackson:

| Archivo | Qué contiene |
|---|---|
| `src/main/java/com/bootcamp/Loan.java` | Record con `id`, `isbn`, `memberId` (publicado como `member`), `dueDate`, `returnedOn`, `lateFee` (`BigDecimal`), `status` (`enum`) y `librarianNote` (con `@JsonIgnore`) |
| `src/main/resources/application.properties` | `spring.jackson.default-property-inclusion=non_null`: los campos `null` no se escriben |
| `src/test/resources/com/bootcamp/returned-loan.json` | El JSON de referencia de un préstamo devuelto |

El `pom.xml` es más corto que el de los labs anteriores: `spring-boot-starter-jackson` y `spring-boot-starter-jackson-test`, sin Spring MVC ni Mockito.

El test activo, `shouldWriteTheIsbn`, escribe el préstamo abierto `OPEN` y lee su `isbn` ([teoría 03](../../1-teoria/03-contrato-json-con-jsontest.md#jsontest-y-jacksontester)).

## Paso a paso

### Paso 1: las fechas (PASO 1)

Descomenta el PASO 1.

Un `LocalDate` sale como texto ISO 8601, `"2026-03-20"`. Es lo que Spring Boot configura por defecto; sin esa configuración, algunas versiones de Jackson escribían la fecha como una lista de números (`[2026,3,20]`).

```bash
mvn test
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 2: nombres y campos ocultos (PASO 2)

Descomenta el PASO 2.

Tres comprobaciones sobre el mismo JSON:

- `hasJsonPathStringValue("$.member")`: `@JsonProperty("member")` renombra `memberId`.
- `doesNotHaveJsonPath("$.memberId")`: el nombre del componente ya no aparece.
- `doesNotHaveJsonPath("$.librarianNote")`: `@JsonIgnore` deja fuera la nota interna, aunque tenga valor.

```bash
mvn test
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 3: la configuración de la aplicación (PASO 3)

Descomenta el PASO 3.

`OPEN` tiene `returnedOn` a `null`, y el JSON no lo lleva, por la propiedad de `application.properties`.

```bash
mvn test
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
```

Experimento: comenta la propiedad en `application.properties` poniendo `#` delante.

```bash
mvn test
# [ERROR]   LoanJsonTest.shouldOmitTheReturnDateOfAnOpenLoan:46 Expecting no JSON path "$.returnedOn"
```

`@JsonTest` lee `application.properties` igual que la aplicación. Un `JsonMapper` creado a mano en el test no lo haría, y el test pasaría con un JSON que la API no envía. Deshaz el cambio.

### Paso 4: el contrato completo (PASO 4)

Descomenta el PASO 4.

`isEqualToJson("returned-loan.json")` compara el JSON de `RETURNED` con el archivo de `src/test/resources/com/bootcamp/`. Ábrelo: es exactamente lo que recibe el cliente.

```bash
mvn test
# [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

Experimento: en `Loan.java`, renombra el campo en el JSON sin tocar el código que lo usa:

```java
    @JsonProperty("bookIsbn") String isbn,
```

```bash
mvn test
# [ERROR]   LoanJsonTest.shouldMatchTheContractOfAReturnedLoan:51 JSON Comparison failure:
# Expected: isbn
#      but none found
# ...
# [ERROR]   LoanJsonTest.shouldWriteTheIsbn:28
# expected: "978-0307474728"
#  but was: null
# [ERROR] Tests run: 5, Failures: 2, Errors: 0, Skipped: 0
```

El código Java compila y no cambió ninguna lógica, pero el cliente que lee `isbn` dejaría de funcionar. Dos tests lo detectan: el del archivo y el del campo concreto. Deshaz el cambio.

### Paso 5: leer JSON (PASO 5)

Descomenta el PASO 5 y su `import`.

- El primer test lee un JSON con un campo de más, `branch`, que `Loan` no tiene. Se ignora, y el resultado es igual a `RETURNED` (los records comparan por valor).
- El segundo lee `"status": "LOST"`, que no existe en `Loan.Status`. Falla, y el test comprueba el mensaje:

```text
Cannot deserialize value of type `com.bootcamp.Loan$Status` from String "LOST": not one of the values accepted for Enum class: [OPEN, RETURNED]
```

```bash
mvn test
# [INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
```

El test comprueba solo la parte del mensaje que dice qué valor falló (`from String "LOST": not one of the values accepted`), no la lista de valores: si mañana se añade un estado nuevo, el test sigue siendo válido.

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
| ← [Lab 02 - Validación y errores con MockMvcTester](../lab-02-validacion-con-mockmvctester/README.md) | [Lab 04 - @SpringBootTest y RestTestClient](../lab-04-springboottest-y-resttestclient/README.md) → |

[↑ Semana 28](../../README.md)
