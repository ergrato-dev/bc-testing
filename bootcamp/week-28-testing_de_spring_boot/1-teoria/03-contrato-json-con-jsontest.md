# 03 - El contrato JSON con @JsonTest

> Lenguaje: **Java**

---

## 🎯 Objetivos

- Explicar por qué el JSON de una API es un contrato y qué lo rompe.
- Probar la serialización y la deserialización con `@JsonTest` y `JacksonTester`.
- Usar `@JsonProperty` y `@JsonIgnore` y comprobar su efecto.
- Comparar un JSON completo con un archivo de referencia.

---

## De dónde partes

En la [semana 13](../../week-13-snapshot_y_property_based_testing/README.md) usaste snapshots de Jest para fijar una salida y detectar cambios. Un test de `@JsonTest` con `isEqualToJson("archivo.json")` hace algo parecido con el JSON de un objeto, con una diferencia: el archivo lo escribes tú, a propósito, y no se regenera solo.

---

## El JSON es un contrato

El cliente de una API (un frontend, otra API, una app móvil) lee los campos por su nombre. Si `isbn` pasa a llamarse `bookIsbn`, el código Java compila, los tests del servicio pasan y el cliente deja de funcionar. Nada en el compilador te avisa.

Lo que puede cambiar el JSON sin tocar la lógica:

| Cambio | Efecto en el JSON |
|---|---|
| Renombrar un componente del record | Cambia el nombre del campo |
| `@JsonProperty("member")` | El campo se llama `member` aunque el componente sea `memberId` |
| `@JsonIgnore` | El campo no aparece |
| `spring.jackson.default-property-inclusion=non_null` en `application.properties` | Los campos `null` no aparecen |
| Cambiar un `LocalDate` por un `LocalDateTime` | `"2026-03-20"` pasa a `"2026-03-20T00:00:00"` |
| Un método sin componente, como `total()` en un record | No aparece: Jackson publica los componentes del record y los getters, y `total()` no es ninguno de los dos |

El último caso apareció en el proyecto del ejemplo: `ReturnNotice` calcula `total()`, pero el JSON no lo llevaba. La solución fue un record de respuesta, `ReturnResponse`, con `total` como componente.

---

## @JsonTest y JacksonTester

```java
@JsonTest                                       // 1. slice: solo Jackson, configurado como en la aplicación
class LoanJsonTest {

    @Autowired
    private JacksonTester<Loan> json;           // 2. serializa y deserializa objetos Loan

    @Test
    void shouldWriteTheDueDateAsAnIsoDate() throws Exception {
        assertThat(json.write(OPEN))            // 3. el JSON de OPEN
            .extractingJsonPathStringValue("$.dueDate")
            .isEqualTo("2026-03-20");
    }
}
```

| Pieza | Qué hace |
|---|---|
| `@JsonTest` | Arranca solo Jackson con las propiedades `spring.jackson.*` de `application.properties` |
| `JacksonTester<T>` | Escribe un `T` como JSON (`write`) y lee JSON como `T` (`parseObject`) |
| `assertThat(json.write(obj))` | Aserciones de AssertJ para JSON: `hasJsonPathStringValue`, `doesNotHaveJsonPath`, `extractingJsonPathNumberValue`, `isEqualToJson` |

¿Por qué no crear un `JsonMapper` a mano en un test unitario? Porque ese mapper no tendría la configuración de la aplicación. En el [lab 03](../2-practicas/lab-03-contrato-json-con-jsontest/README.md), `application.properties` dice que los `null` no se escriben; un mapper creado a mano los escribiría, y el test probaría un JSON que la API nunca envía.

### Comparar con un archivo

```java
assertThat(json.write(RETURNED)).isEqualToJson("returned-loan.json");
```

`isEqualToJson` busca el archivo junto a la clase de test, en `src/test/resources/com/bootcamp/returned-loan.json`. La comparación es **flexible**: no importa el orden de los campos ni los espacios. Si falta un campo o cambia un valor, falla:

```text
JSON Comparison failure:
Expected: isbn
     but none found
```

El archivo es la documentación del contrato: quien lo lee ve exactamente qué recibe el cliente.

### Leer JSON

```java
Loan loan = json.parseObject("""
    {"id": "L-2", "isbn": "978-0307474728", "member": "M-7", "status": "RETURNED", "branch": "Centro"}
    """);
```

Con la configuración de Spring Boot, un campo que el record no tiene (`branch`) se ignora: un cliente que envía campos de más no rompe la API. Un valor que no existe en un `enum` sí falla:

```text
Cannot deserialize value of type `com.bootcamp.Loan$Status` from String "LOST": not one of the values accepted for Enum class: [OPEN, RETURNED]
```

En `@WebMvcTest`, ese mismo error es el 400 que recibe el cliente ([teoría 02](02-webmvctest-y-mockmvc.md#el-contrato-de-errores)).

---

## Jackson 3

Spring Boot 4 usa Jackson 3. Las clases principales cambiaron de paquete (`tools.jackson.databind` en lugar de `com.fasterxml.jackson.databind`), pero las anotaciones no: `@JsonProperty` y `@JsonIgnore` siguen en `com.fasterxml.jackson.annotation`. Si copias código de un tutorial antiguo y el `import` de `ObjectMapper` no compila, es por eso.

---

## ✅ Resumen

- El JSON es un contrato con el cliente; renombrar un campo lo rompe sin que falle ningún test del dominio.
- `@JsonTest` carga Jackson con la configuración de la aplicación; `JacksonTester` escribe y lee.
- `isEqualToJson("archivo.json")` compara con un archivo de referencia, sin importar el orden ni los espacios.
- Los campos desconocidos se ignoran; los valores de `enum` desconocidos fallan.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 02 - @WebMvcTest, MockMvc y MockMvcTester](02-webmvctest-y-mockmvc.md) | [Teoría — 04 - @SpringBootTest de extremo a extremo](04-springboottest-de-extremo-a-extremo.md) → |

[↑ Semana 28](../README.md)
