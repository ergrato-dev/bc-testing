# Lab 03 - Outside-in con Mockito

## Objetivo

Construir de fuera hacia dentro un servicio que envía un correo a cada socio de la biblioteca que tiene que devolver un libro mañana. Empiezas por el test del servicio: sus mocks deciden qué interfaces existen. Después bajas un nivel y haces los textos del correo con TDD clásico, sin mocks.

## Tiempo estimado

65 minutos.

## Preparación

```bash
cd starter
mvn test
# [INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0
```

- `src/main/java/com/bootcamp/Loan.java` es lo único que existe: `record Loan(String id, String memberId, String bookTitle, LocalDate dueDate)`.
- `src/test/java/com/bootcamp/ReminderServiceTest.java` trae los PASOS 1, 2, 5 y 6; `ReminderTextTest.java`, los PASOS 3 y 4.
- El `pom.xml` es el de la [semana 26](../../../week-26-mockito_mocking_en_java/1-teoria/01-mocks-y-stubbing.md): Mockito 5 cargado como agente.

La [teoría 03](../../1-teoria/03-outside-in-con-mockito.md) explica outside-in y el `Clock`.

## Paso a paso

### Paso 1: el test del servicio (PASO 1)

Descomenta el PASO 1 de `ReminderServiceTest`: los mocks, el reloj, el `@BeforeEach` y el primer test. Léelo antes de ejecutar: el test ya decide que el servicio se llama `ReminderService`, que recibe tres dependencias y un `Clock`, y que pregunta por los préstamos del **17** de marzo cuando hoy es **16**.

```bash
mvn test
# [ERROR] COMPILATION ERROR :
# [ERROR] .../ReminderServiceTest.java:[NN,13] cannot find symbol
# [ERROR] .../ReminderServiceTest.java:[NN,13] cannot find symbol
# [ERROR] .../ReminderServiceTest.java:[NN,13] cannot find symbol
# [ERROR] .../ReminderServiceTest.java:[NN,28] cannot find symbol
```

Crea cada tipo que falta con `Alt+Enter` (o a mano), con **solo** lo que el test usa:

```java
public interface LoanRepository {
    List<Loan> findDueOn(LocalDate date);
}

public interface MemberDirectory {
    Optional<String> emailOf(String memberId);
}

public interface Mailer {
    void send(Email email);
}

public record Email(String to, String subject, String body) {
}
```

`Email` lleva `subject` y `body` aunque el test todavía no los mire: un correo sin asunto ni cuerpo no tiene sentido, y el paso 5 los comprueba.

Y el servicio, con un método que todavía no hace nada:

```java
public class ReminderService {

    private final LoanRepository loans;
    private final MemberDirectory members;
    private final Mailer mailer;
    private final Clock clock;

    public ReminderService(LoanRepository loans, MemberDirectory members, Mailer mailer, Clock clock) {
        this.loans = loans;
        this.members = members;
        this.mailer = mailer;
        this.clock = clock;
    }

    public void remindLoansDueTomorrow() {
    }
}
```

```bash
mvn test
# [ERROR]   ReminderServiceTest.shouldEmailTheMemberOfEachLoanDueTomorrow:NN
# [ERROR] Tests run: 1, Failures: 1, Errors: 0, Skipped: 0
```

```text
Wanted but not invoked:
mailer.send(<Capturing argument: Email>);
-> at com.bootcamp.ReminderServiceTest.shouldEmailTheMemberOfEachLoanDueTomorrow(ReminderServiceTest.java:NN)
Actually, there were zero interactions with this mock.
```

Ahora sí es un Red de aserción.

### Paso 2: experimento, el día equivocado

Escribe este Green, que tiene un error a propósito:

```java
public void remindLoansDueTomorrow() {
    LocalDate today = LocalDate.now(clock);
    for (Loan loan : loans.findDueOn(today)) {
        String address = members.emailOf(loan.memberId()).orElseThrow();
        mailer.send(new Email(address, "", ""));
    }
}
```

```bash
mvn test
# [ERROR]   ReminderServiceTest.shouldEmailTheMemberOfEachLoanDueTomorrow:NN » PotentialStubbingProblem
```

```text
Strict stubbing argument mismatch. Please check:
 - this invocation of 'findDueOn' method:
    loans.findDueOn(2026-03-16);
 - has following stubbing(s) with different arguments:
    1. loans.findDueOn(2026-03-17); stubbed with: [Returns: [Loan[id=L-1, memberId=M-7, bookTitle=Rayuela, dueDate=2026-03-17]]]
```

El servicio pidió los préstamos de hoy. Gracias a `Clock.fixed`, "hoy" es siempre el 16 de marzo y el error se ve en el mensaje. Arréglalo:

```java
LocalDate tomorrow = LocalDate.now(clock).plusDays(1);
for (Loan loan : loans.findDueOn(tomorrow)) {
```

```bash
mvn test
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

El asunto y el cuerpo vacíos (`""`) son a propósito: los textos son el siguiente nivel.

### Paso 3: un socio sin correo (PASO 2)

Descomenta el PASO 2.

```bash
mvn test
# [ERROR]   ReminderServiceTest.shouldSkipMembersWithoutAnEmailAddress:NN » NoSuchElement No value present
# [ERROR] Tests run: 2, Failures: 0, Errors: 1, Skipped: 0
```

`orElseThrow()` lanza la excepción cuando el `Optional` está vacío. Green: envía solo si hay dirección.

```java
for (Loan loan : loans.findDueOn(tomorrow)) {
    members.emailOf(loan.memberId())
        .ifPresent(address -> mailer.send(new Email(address, "", "")));
}
```

```bash
mvn test
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 4: bajar un nivel, el asunto (PASO 3)

El servicio ya decide **a quién** escribir. **Qué** escribir es otra responsabilidad, sin dependencias: se prueba sin mocks. Descomenta el PASO 3 de `ReminderTextTest`.

Red de compilación: falta `ReminderText`. Créala con un método que devuelve `""`:

```java
public final class ReminderText {

    private ReminderText() {
    }

    public static String subjectFor(Loan loan) {
        return "";
    }
}
```

```text
expected: "Recordatorio: devuelve "Rayuela" mañana"
 but was: ""
```

Green:

```java
public static String subjectFor(Loan loan) {
    return "Recordatorio: devuelve \"" + loan.bookTitle() + "\" mañana";
}
```

`\"` escribe unas comillas dentro de un `String`.

### Paso 5: el cuerpo (PASO 4)

Descomenta el PASO 4. Red de compilación: falta `bodyFor`. Green:

```java
private static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

public static String bodyFor(Loan loan) {
    return "El préstamo " + loan.id() + " vence el " + DAY.format(loan.dueDate()) + ".";
}
```

`DateTimeFormatter.ofPattern("dd/MM/yyyy")` escribe la fecha como `17/03/2026`. Importa `java.time.format.DateTimeFormatter`.

```bash
mvn test
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 6: el servicio usa los textos (PASO 5)

Descomenta el PASO 5 de `ReminderServiceTest`.

```text
expected: Email[to=ana@example.com, subject=Recordatorio: devuelve "Rayuela" mañana, body=El préstamo L-1 vence el 17/03/2026.]
 but was: Email[to=ana@example.com, subject=, body=]
```

Green: cambia `new Email(address, "", "")` por:

```java
new Email(address, ReminderText.subjectFor(loan), ReminderText.bodyFor(loan))
```

```bash
mvn test
# [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

El test del servicio usa `ReminderText` de verdad, no un mock: es una clase sin dependencias ([teoría 03](../../1-teoria/03-outside-in-con-mockito.md#qué-no-se-sustituye-con-un-mock)).

### Paso 7: un correo que falla no detiene los demás (PASO 6)

Descomenta el PASO 6. El test usa `doThrow(...).when(mailer).send(...)` porque `send` es `void` (lo viste en la semana 26). Red de compilación: falta la excepción.

```java
public class MailerUnavailableException extends RuntimeException {

    public MailerUnavailableException(String message) {
        super(message);
    }
}
```

```bash
mvn test
# [ERROR]   ReminderServiceTest.shouldKeepSendingWhenOneEmailFails:NN » MailerUnavailable timeout
# [ERROR] Tests run: 6, Failures: 0, Errors: 1, Skipped: 0
```

El primer envío falla y la excepción corta el bucle: Luis no recibe su correo. Green: captura la excepción en cada envío. Saca el envío a un método privado para que el `try` no ensucie el bucle:

```java
public void remindLoansDueTomorrow() {
    LocalDate tomorrow = LocalDate.now(clock).plusDays(1);
    for (Loan loan : loans.findDueOn(tomorrow)) {
        members.emailOf(loan.memberId()).ifPresent(address -> send(loan, address));
    }
}

private void send(Loan loan, String address) {
    try {
        mailer.send(new Email(address, ReminderText.subjectFor(loan), ReminderText.bodyFor(loan)));
    } catch (MailerUnavailableException e) {
        // Un correo que falla no detiene los demás; se reintenta en la próxima ejecución.
    }
}
```

```bash
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

## Comprobación

```bash
cd ../solution
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

## Preguntas de cierre

1. ¿Qué métodos tendría `LoanRepository` si la hubieras escrito antes que el test? ¿Cuántos usa el servicio?
2. ¿Por qué `ReminderText` no es un mock en el paso 6?
3. Sin `Clock`, ¿cómo habrías probado el paso 2? (Pista: el lab 03 de la [semana 26](../../../week-26-mockito_mocking_en_java/2-practicas/lab-03-strict-stubs-spy-y-static/README.md).)

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Kata Game of Life](../lab-02-game-of-life/README.md) | [Lab 04 - Fitness functions con ArchUnit](../lab-04-archunit/README.md) → |

[↑ Semana 27](../../README.md)
