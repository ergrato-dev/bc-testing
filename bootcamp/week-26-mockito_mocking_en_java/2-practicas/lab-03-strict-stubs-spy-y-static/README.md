# Lab 03 - Strict stubs, spies y mockStatic

## Objetivo

Provocar los dos errores de strict stubbing de `MockitoExtension`, controlar la fecha de un método estático con `mockStatic` y cambiar un mock por un spy para que una clase real calcule la tarifa. Vas a ver por qué un `mockStatic` sin `try` rompe otros tests.

## Tiempo estimado

55 minutos.

## Preparación

```bash
cd starter
mvn test
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

- `src/main/java/com/bootcamp/RenewalService.java` renueva un préstamo 14 días más, como mucho dos veces. Rechaza el préstamo si ya venció, y para saber la fecha de hoy llama a `LegacyCalendar.today()`, un método **estático**.
- `FeeTable` es una clase real: la primera renovación es gratis y la segunda cuesta 300.
- `src/test/java/com/bootcamp/RenewalServiceTest.java` trae dos tests activos que no llegan a mirar la fecha. `FeeTable` es un `@Mock` (PASO 0) y los PASOS 1 a 3 están comentados.

La [teoría 03](../../1-teoria/03-strict-stubs-spies-y-static.md) explica cada concepto.

## Paso a paso

### Paso 1: experimento, un stub que sobra

En `shouldRejectAThirdRenewal`, añade debajo del `when` que ya tiene:

```java
when(loans.findById("L-9")).thenReturn(Optional.empty());
```

```bash
mvn test
# [ERROR]   RenewalServiceTest.shouldRejectAThirdRenewal » UnnecessaryStubbing
# Unnecessary stubbings detected.
# Clean & maintainable test code requires zero unnecessary code.
# Following stubbings are unnecessary (click to navigate to relevant line of code):
#   1. -> at com.bootcamp.RenewalServiceTest.shouldRejectAThirdRenewal(RenewalServiceTest.java:NN)
# Please remove unnecessary stubbings or use 'lenient' strictness. More info: javadoc for UnnecessaryStubbingException class.
```

El servicio nunca pide `"L-9"` en ese test. Un stub que nadie usa confunde a quien lee el test, y `MockitoExtension` lo trata como un error. Borra la línea.

### Paso 2: experimento, un stub con otro argumento

En `shouldRejectALoanThatDoesNotExist`, cambia `service.renew("L-9")` por `service.renew("L-8")`. El stub sigue siendo para `"L-9"`.

```bash
mvn test
# [ERROR]   RenewalServiceTest.shouldRejectALoanThatDoesNotExist:NN
# Expecting actual throwable to be an instance of:
#   java.util.NoSuchElementException
# but was:
#   org.mockito.exceptions.misusing.PotentialStubbingProblem:
# Strict stubbing argument mismatch. Please check:
#  - this invocation of 'findById' method:
#     loans.findById("L-8");
#     -> at com.bootcamp.RenewalService.renew(RenewalService.java:21)
#  - has following stubbing(s) with different arguments:
#     1. loans.findById("L-9"); stubbed with: [Returns: Optional.empty]
```

Sin strict stubbing, `findById("L-8")` habría devuelto `Optional.empty()` por defecto y el test pasaría por casualidad. Vuelve a `"L-9"`.

### Paso 3: la fecha con mockStatic (PASO 1)

Descomenta los imports marcados con `PASO 1` y los dos tests del PASO 1.

```bash
mvn test
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
```

Dentro del `try`, `LegacyCalendar.today()` devuelve `TODAY` (16 de marzo de 2026). Al cerrar el bloque, el método vuelve a devolver la fecha real.

Experimento: en `shouldExtendTheDueDateByTwoWeeks`, sustituye la línea del `try (...) {` por estas dos:

```java
MockedStatic<LegacyCalendar> calendar = mockStatic(LegacyCalendar.class);
{
```

```bash
mvn test
# [ERROR]   RenewalServiceTest.shouldRejectALoanThatIsAlreadyOverdue:NN Mockito
# For com.bootcamp.LegacyCalendar, static mocking is already registered in the current thread
#
# To create a new mock, the existing static mock registration must be deregistered
# [ERROR] Tests run: 4, Failures: 0, Errors: 1, Skipped: 0
```

Falla **otro** test: el mock estático sigue registrado cuando termina el primero. Cuál falla depende del orden de ejecución. Vuelve a poner el `try`.

### Paso 4: experimento, el mock no calcula

Descomenta el test del PASO 2, `shouldChargeTheSecondRenewalWithTheRealFeeTable`. **No** toques todavía los campos.

```bash
mvn test
# [ERROR]   RenewalServiceTest.shouldChargeTheSecondRenewalWithTheRealFeeTable:NN
# expected: 300
#  but was: 0
```

`fees` es un `@Mock`: su `renewalFee` no ejecuta el código de `FeeTable` y devuelve `0`, el valor por defecto de un `int`.

### Paso 5: el spy (PASO 2)

1. Borra el bloque `PASO 0 (se borra en el PASO 2)`: el `@Mock` de `FeeTable`.
2. Descomenta el import marcado con `PASO 2` y el campo `@Spy` del PASO 2.

```bash
mvn test
# [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

Un spy envuelve un objeto real: ejecuta el código de verdad y, además, registra las llamadas. Por eso el test puede comprobar el 300 que calcula `FeeTable` y también `verify(fees).renewalFee(2)`.

### Paso 6: doReturn en un spy (PASO 3)

Descomenta el import marcado con `PASO 3` y el test del PASO 3.

```bash
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

`doReturn(150).when(fees).renewalFee(2)` cambia una sola respuesta del spy; el resto de `FeeTable` sigue siendo real. En un spy se usa `doReturn` y no `when(fees.renewalFee(2))`, porque esa segunda forma ejecuta el método real al preparar el stub (la teoría 03 enseña el caso en que eso lanza una excepción).

## Comprobación

```bash
cd ../solution
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

## Preguntas de cierre

1. ¿Qué te habría pasado en el paso 2 si el test no usara `MockitoExtension`?
2. Cuatro de los seis tests necesitan `mockStatic`. ¿Qué cambiarías en `RenewalService` para no necesitarlo? (Pista: el lab 03 de la [semana 25](../../../week-25-junit_6_avanzado_y_assertj/2-practicas/lab-03-extensiones-y-tempdir/README.md).)
3. ¿Cuándo preferirías un spy a un mock?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - verify y ArgumentCaptor](../lab-02-verify-y-captores/README.md) | [Lab 04 - BDDMockito](../lab-04-bddmockito/README.md) → |

[↑ Semana 26](../../README.md)
