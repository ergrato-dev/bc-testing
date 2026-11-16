# Lab 02 - verify y ArgumentCaptor

## Objetivo

Comprobar qué hace un servicio con sus dependencias: cuántas veces las llama, con qué argumentos y en qué orden. Vas a empezar con un test que sigue en verde aunque el servicio no envíe ningún aviso.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
mvn test
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

- `src/main/java/com/bootcamp/OverdueNotifier.java` busca los préstamos vencidos (`LoanRepository`), envía un aviso a cada socio (`Notifier`) y marca el préstamo como avisado. Devuelve cuántos avisos envió.
- `Notice` es el aviso: destinatario, asunto y texto. Lo construye el servicio, dentro de `notifyOverdue`.
- `src/test/java/com/bootcamp/OverdueNotifierTest.java` crea los dos mocks con `@Mock`, trae un test activo (PASO 0) y los PASOS 1 a 4 comentados. El servicio se crea en el `@BeforeEach` con un `Clock` fijo: `@InjectMocks` no sirve aquí porque el `Clock` no es un mock.

La [teoría 02](../../1-teoria/02-verify-y-captores.md) explica `verify` y los captores.

## Paso a paso

### Paso 1: experimento, un test que no ve nada (PASO 0)

En `OverdueNotifier.java`, comenta las cuatro líneas de `notifier.send(new Notice(...));` y ejecuta:

```bash
mvn test
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

El servicio ya no avisa a nadie y el test sigue en verde: solo mira el número devuelto. **Deja las líneas comentadas** para el paso 2.

### Paso 2: verify, times y never (PASO 1)

1. Borra el test del PASO 0.
2. Descomenta los imports marcados con `PASO 1` y los dos tests del PASO 1.

Con `notifier.send` todavía comentado:

```bash
mvn test
# [ERROR]   OverdueNotifierTest.shouldSendOneNoticePerOverdueLoan:NN
# Wanted but not invoked:
# notifier.send(<any com.bootcamp.Notice>);
# -> at com.bootcamp.OverdueNotifierTest.shouldSendOneNoticePerOverdueLoan(OverdueNotifierTest.java:NN)
# Actually, there were zero interactions with this mock.
# [ERROR] Tests run: 2, Failures: 1, Errors: 0, Skipped: 0
```

`NN` es el número de línea en tu archivo. Descomenta las cuatro líneas en `OverdueNotifier.java`:

```bash
mvn test
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

Experimento: cambia `times(2)` por `times(3)`.

```bash
mvn test
# notifier.send(<any com.bootcamp.Notice>);
# Wanted 3 times:
# -> at com.bootcamp.OverdueNotifierTest.shouldSendOneNoticePerOverdueLoan(OverdueNotifierTest.java:NN)
# But was 2 times:
# -> at com.bootcamp.OverdueNotifier.notifyOverdue(OverdueNotifier.java:27)
# -> at com.bootcamp.OverdueNotifier.notifyOverdue(OverdueNotifier.java:27)
```

Mockito dice cuántas llamadas hubo y desde qué línea del servicio. Vuelve a `times(2)`.

### Paso 3: argumentos exactos y matchers (PASO 2)

Descomenta el test del PASO 2.

```bash
mvn test
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

`verify(loans).markNotified("L-1", TODAY)` solo pasa si hubo una llamada con esos dos valores.

Experimento 1: cambia `TODAY` por `TODAY.plusDays(1)` en la línea de `"L-2"`.

```bash
mvn test
# Argument(s) are different! Wanted:
# loans.markNotified("L-2", 2026-03-17);
# -> at com.bootcamp.OverdueNotifierTest.shouldMarkEachLoanAsNotifiedToday(OverdueNotifierTest.java:NN)
# Actual invocations have different arguments at position [1]:
# loans.findOverdue(2026-03-16);
# loans.markNotified("L-1", 2026-03-16);
# loans.markNotified("L-2", 2026-03-16);
```

Mockito enseña todas las llamadas que recibió el mock. Deshaz el cambio.

Experimento 2: en la línea de `"L-1"`, cambia `TODAY` por `any()`.

```bash
mvn test
# [ERROR]   OverdueNotifierTest.shouldMarkEachLoanAsNotifiedToday:NN InvalidUseOfMatchers
# Invalid use of argument matchers!
# 2 matchers expected, 1 recorded:
# ...
# When using matchers, all arguments have to be provided by matchers.
```

Si un argumento es un matcher (`any()`), todos lo son: el valor fijo se escribe `eq("L-1")`. Deshaz el cambio.

### Paso 4: capturar el aviso (PASO 3)

`verify(notifier).send(any())` no dice nada del texto del aviso, y el test no puede crear ese `Notice` antes que el servicio. Un captor lo recoge al verificar.

1. Descomenta los imports marcados con `PASO 3`.
2. Descomenta el campo `notice` con `@Captor` y los dos tests del PASO 3.

```bash
mvn test
# [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

- `notice.capture()` va en el lugar del argumento dentro de `verify`.
- `notice.getValue()` devuelve el último aviso capturado; `getAllValues()`, todos, en el orden de las llamadas.

### Paso 5: el orden de las llamadas (PASO 4)

Si el servicio marcara el préstamo antes de enviar y el envío fallara, el socio no recibiría el aviso y nadie lo volvería a intentar. El orden importa.

Descomenta los imports marcados con `PASO 4` y el test del PASO 4.

```bash
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

Experimento: en `OverdueNotifier.java`, mueve la línea `loans.markNotified(loan.id(), today);` encima de `notifier.send(...)`.

```bash
mvn test
# Verification in order failure
# Wanted but not invoked:
# loans.markNotified("L-1", 2026-03-16);
# -> at com.bootcamp.OverdueNotifierTest.shouldMarkTheLoanOnlyAfterSendingTheNotice(OverdueNotifierTest.java:NN)
# Wanted anywhere AFTER following interaction:
# notifier.send(
#     Notice[to=ana@example.com, subject=Overdue: Dune, body="Dune" is 3 day(s) late.]
# );
```

Vuelve a dejar la línea debajo del envío. `verifyNoMoreInteractions(notifier)`, al final del test, falla si el mock recibió alguna llamada que el test no verificó.

## Comprobación

```bash
cd ../solution
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

## Preguntas de cierre

1. ¿Por qué el test del PASO 0 no detectó que no se enviaba ningún aviso?
2. ¿Cuándo usarías un `ArgumentCaptor` en lugar de `verify(...).send(new Notice(...))` con el objeto esperado?
3. ¿Qué riesgo tiene poner `verifyNoMoreInteractions` en todos los tests?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Del stub a mano al primer mock](../lab-01-primer-mock/README.md) | [Lab 03 - Strict stubs, spies y mockStatic](../lab-03-strict-stubs-spy-y-static/README.md) → |

[↑ Semana 26](../../README.md)
