# 03 - @TempDir y extensiones de JUnit

> Lenguaje: **Java**

---

## 🎯 Objetivos

- Probar código que escribe archivos sin ensuciar el proyecto, con `@TempDir`.
- Entender qué es una extensión de JUnit y registrarla con `@ExtendWith` o con `@RegisterExtension`.
- Escribir una extensión mínima que inyecta parámetros en los tests.

---

## @TempDir: un directorio desechable por test

Un test que escribe en una ruta fija deja el archivo ahí cuando termina, y el siguiente test (o la siguiente ejecución) lo encuentra:

```java
Path file = Path.of("overdue.csv");   // se crea en la carpeta desde la que ejecutas mvn
report.writeTo(file, LOANS);
```

Con `@TempDir`, JUnit crea un directorio vacío antes del test y lo borra con todo su contenido al terminar. Es el equivalente de la fixture `tmp_path` de pytest ([semana 16](../../week-16-fundamentos_con_pytest/1-teoria/03-conftest-autouse-y-fixtures-integradas.md)).

Se puede pedir como **parámetro** del test:

```java
@Test
@DisplayName("should write only the overdue loans")
void shouldWriteOnlyTheOverdueLoans(@TempDir Path dir) throws IOException {
    Path file = dir.resolve("overdue.csv");

    report.writeTo(file, LOANS);

    assertThat(Files.readAllLines(file))
        .containsExactly("9780000000001,2026-03-10", "9780000000003,2026-03-15");
}
```

O como **campo**, cuando varios tests lo usan:

```java
@TempDir
Path reportsDir;

@Test
@DisplayName("should create the missing parent directories")
void shouldCreateTheMissingParentDirectories() throws IOException {
    Path file = reportsDir.resolve("2026/03/overdue.csv");

    report.writeTo(file, LOANS);

    assertThat(file).exists();
}
```

1. El campo no puede ser `final`: JUnit lo asigna antes de cada test.
2. Cada test recibe un directorio distinto, aunque sea el mismo campo.
3. El tipo puede ser `Path` o `File`. Usa `Path`: es la API moderna de `java.nio.file`.

---

## Qué es una extensión

`@TempDir` funciona gracias a una **extensión**: código que JUnit llama en momentos concretos de la vida de un test. JUnit trae algunas y tú puedes escribir las tuyas. Las más usadas implementan una de estas interfaces:

| Interfaz | JUnit la llama | Uso típico |
|---|---|---|
| `ParameterResolver` | Al ver un parámetro en un método de test | Inyectar un objeto: un `Clock`, un `Path` |
| `BeforeEachCallback` / `AfterEachCallback` | Antes o después de cada test | Preparar y limpiar un recurso |
| `BeforeAllCallback` / `AfterAllCallback` | Una vez por clase | Arrancar un servidor o un contenedor |
| `TestExecutionExceptionHandler` | Cuando un test lanza una excepción | Añadir información al fallo |

En la [semana 23](../../week-23-testing_de_bases_de_datos_en_python/1-teoria/02-fixtures-y-rollback-por-test.md) una fixture de pytest preparaba y limpiaba la base de datos. En JUnit, eso es una extensión con callbacks. En la semana 26 usarás `MockitoExtension` y en la 29 la extensión de Testcontainers.

---

## Una extensión mínima: un reloj fijo

El código que depende de la fecha de hoy recibe un `java.time.Clock`. En los tests, un `Clock.fixed(...)` hace que "hoy" sea siempre el mismo día. Esta extensión inyecta ese reloj en cualquier parámetro de tipo `Clock`:

```java
public final class FixedClockExtension implements ParameterResolver {

    private final LocalDate today;

    public FixedClockExtension() {
        this(LocalDate.of(2026, 3, 16));
    }

    private FixedClockExtension(LocalDate today) {
        this.today = today;
    }

    public static FixedClockExtension on(LocalDate today) {
        return new FixedClockExtension(today);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == Clock.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameter, ExtensionContext context) {
        return Clock.fixed(today.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC);
    }
}
```

1. `supportsParameter` responde si esta extensión sabe crear ese parámetro.
2. `resolveParameter` lo crea.
3. El constructor sin argumentos fija el 16 de marzo de 2026; `on(...)` permite elegir otra fecha.

### Registrarla con @ExtendWith

`@ExtendWith` registra la extensión de forma **declarativa**: JUnit crea la instancia con el constructor sin argumentos.

```java
@ExtendWith(FixedClockExtension.class)
class ReturnReminderTest {

    @Test
    @DisplayName("should say due today on the due date")
    void shouldSayDueTodayOnTheDueDate(Clock clock) {
        LoanRecord loan = new LoanRecord("9780000000001", LocalDate.of(2026, 3, 16));

        assertThat(new ReturnReminder(clock).messageFor(loan)).isEqualTo("due today");
    }
}
```

Sin la extensión, nadie sabe crear el `Clock` y el test da error, no fallo:

```text
org.junit.jupiter.api.extension.ParameterResolutionException: No ParameterResolver registered for parameter [java.time.Clock arg0] in method [void com.bootcamp.ReturnReminderTest.shouldSayDueTomorrowTheDayBefore(java.time.Clock)].
```

### Registrarla con @RegisterExtension

`@RegisterExtension` la registra de forma **programática**: tú creas la instancia en un campo, así que puedes configurarla.

```java
class ReturnReminderAtYearEndTest {

    @RegisterExtension
    static final FixedClockExtension NEW_YEARS_EVE = FixedClockExtension.on(LocalDate.of(2026, 12, 31));

    @Test
    @DisplayName("should count the days across the new year")
    void shouldCountTheDaysAcrossTheNewYear(Clock clock) {
        LoanRecord loan = new LoanRecord("9780000000001", LocalDate.of(2027, 1, 7));

        assertThat(new ReturnReminder(clock).messageFor(loan)).isEqualTo("due in 7 days");
    }
}
```

El campo es `static` para que la extensión exista antes de crear la instancia de la clase de test. Con un campo de instancia no podría resolver los parámetros de `@BeforeAll` ni participar en los callbacks de clase.

| | `@ExtendWith(X.class)` | `@RegisterExtension` |
|---|---|---|
| Quién crea la instancia | JUnit, con el constructor sin argumentos | Tú, en un campo |
| Configurable | No | Sí: fecha, puerto, ruta... |
| Dónde se pone | Clase, método o parámetro | Campo |

### Dos extensiones para el mismo parámetro

Si registras dos extensiones que resuelven `Clock`, JUnit no elige una:

```text
org.junit.jupiter.api.extension.ParameterResolutionException: Discovered multiple competing ParameterResolvers for parameter [java.time.Clock arg0] in method [void com.bootcamp.ReturnReminderAtYearEndTest.shouldCountTheDaysAcrossTheNewYear(java.time.Clock)]: com.bootcamp.FixedClockExtension@732d0d24, com.bootcamp.FixedClockExtension@3370f42
```

Ocurre, por ejemplo, si una clase `@Nested` registra su propia extensión y la clase de fuera ya tenía otra: las extensiones se heredan hacia dentro.

---

## ✅ Resumen

- `@TempDir` da a cada test un directorio vacío que JUnit borra al terminar; úsalo como parámetro o como campo (no `final`).
- Una extensión es código que JUnit llama en puntos del ciclo de vida; `ParameterResolver` inyecta parámetros.
- `@ExtendWith` registra una extensión con su configuración por defecto; `@RegisterExtension` registra una instancia que tú configuras.
- Dos resolvers para el mismo tipo de parámetro dan `ParameterResolutionException`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 02 - Organizar la suite con @Nested y @Tag](02-nested-y-tags.md) | [Teoría — 04 - AssertJ a fondo y soft assertions](04-assertj-y-soft-assertions.md) → |

[↑ Semana 25](../README.md)
