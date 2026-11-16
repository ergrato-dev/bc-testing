# Prácticas - Semana 28

Cuatro labs guiados (4 h en total). Cada uno trae un `starter/` con el código de producción completo y los tests comentados por PASOS. Con todos los PASOS descomentados, el lab queda resuelto. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

Los cuatro labs son de la misma biblioteca, pero cada uno es un proyecto Maven independiente con solo lo que necesita.

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - @WebMvcTest y MockMvc](lab-01-webmvctest-y-mockmvc/README.md) | Slice web, `@MockitoBean`, `status`, `jsonPath`, `content().json`, `print()`, el error de un bean que falta | 60 min |
| [Lab 02 - Validación y errores con MockMvcTester](lab-02-validacion-con-mockmvctester/README.md) | `MockMvcTester`, `@Valid`, mensajes por campo, 400, 409 y 415 con `ProblemDetail`, `Accept-Language` | 60 min |
| [Lab 03 - El contrato JSON con @JsonTest](lab-03-contrato-json-con-jsontest/README.md) | `JacksonTester`, fechas ISO, `@JsonProperty`, `@JsonIgnore`, `non_null`, archivo de referencia | 45 min |
| [Lab 04 - @SpringBootTest y RestTestClient](lab-04-springboottest-y-resttestclient/README.md) | `RANDOM_PORT`, `RestTestClient`, `@TestBean` con un reloj mutable, estado compartido, caché de contextos | 75 min |

## Antes de empezar

```bash
java -version    # openjdk version "21..." o superior
mvn -v           # Apache Maven 3.9 o superior
```

Cada lab se ejecuta desde su `starter/`:

```bash
cd lab-NN-.../starter
mvn test
```

La primera ejecución del lab 01 descarga Spring Boot 4.1.1 y sus dependencias (unos 60 MB). Los demás labs reutilizan lo descargado.

Para descomentar un PASO, selecciona sus líneas y usa el atajo de comentar de tu editor (`Ctrl+/` en VS Code e IntelliJ): quita un nivel de `//`. Las líneas con doble comentario (`// //`) son comentarios de verdad y se quedan con uno. Un PASO puede tener varios bloques en el mismo archivo (sus `import`, un campo, un test): descomenta todos los que lleven su número.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 04 - @SpringBootTest de extremo a extremo](../1-teoria/04-springboottest-de-extremo-a-extremo.md) | [Lab 01 - @WebMvcTest y MockMvc](lab-01-webmvctest-y-mockmvc/README.md) → |

[↑ Semana 28](../README.md)
