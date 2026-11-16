# 02 - verify, matchers y ArgumentCaptor

> Lenguaje: **Java**

---

## 🎯 Objetivos

- Comprobar con `verify` que el servicio llamó a una dependencia, cuántas veces y con qué argumentos.
- Usar matchers sin mezclarlos con valores fijos.
- Recoger con un `ArgumentCaptor` un objeto que el servicio construye por dentro.
- Comprobar el orden de las llamadas con `InOrder`.

---

## Entradas y salidas indirectas

El diagrama de la [teoría 01](01-mocks-y-stubbing.md) muestra los dos papeles de un mock:

- **Entrada indirecta:** lo que el servicio **lee** de una dependencia (`findOverdue` devuelve una lista). Se controla con `when`.
- **Salida indirecta:** lo que el servicio **entrega** a una dependencia (`notifier.send(notice)`). Se comprueba con `verify`.

Un test que solo mira el valor devuelto no ve las salidas indirectas. En el lab 02, `notifyOverdue()` devuelve cuántos avisos envió; si comentas el `notifier.send(...)`, el método sigue devolviendo 2 y el test de ese valor sigue en verde.

---

## verify

```java
import static org.mockito.Mockito.verify;

verify(notifier).send(any(Notice.class));      // exactamente una vez
verify(notifier, times(2)).send(any());        // exactamente dos veces
verify(notifier, never()).send(any());         // ninguna
verify(notifier, atLeastOnce()).send(any());   // una o más
verify(loans).markNotified("L-1", TODAY);      // una vez, con estos argumentos
```

`verify` va **después** de la acción. Si no se cumple, el test falla y Mockito dice qué esperaba y qué pasó:

```text
Wanted but not invoked:
notifier.send(<any com.bootcamp.Notice>);
-> at com.bootcamp.OverdueNotifierTest.shouldSendOneNoticePerOverdueLoan(OverdueNotifierTest.java:73)
Actually, there were zero interactions with this mock.
```

```text
notifier.send(<any com.bootcamp.Notice>);
Wanted 3 times:
-> at com.bootcamp.OverdueNotifierTest.shouldSendOneNoticePerOverdueLoan(OverdueNotifierTest.java:73)
But was 2 times:
-> at com.bootcamp.OverdueNotifier.notifyOverdue(OverdueNotifier.java:27)
-> at com.bootcamp.OverdueNotifier.notifyOverdue(OverdueNotifier.java:27)
```

Con argumentos distintos, enseña todas las llamadas que recibió el mock:

```text
Argument(s) are different! Wanted:
loans.markNotified("L-2", 2026-03-17);
-> at com.bootcamp.OverdueNotifierTest.shouldMarkEachLoanAsNotifiedToday(OverdueNotifierTest.java:99)
Actual invocations have different arguments at position [1]:
loans.findOverdue(2026-03-16);
-> at com.bootcamp.OverdueNotifier.notifyOverdue(OverdueNotifier.java:24)
loans.markNotified("L-1", 2026-03-16);
-> at com.bootcamp.OverdueNotifier.notifyOverdue(OverdueNotifier.java:31)
loans.markNotified("L-2", 2026-03-16);
-> at com.bootcamp.OverdueNotifier.notifyOverdue(OverdueNotifier.java:31)
```

### Cuándo no verificar

Verifica las salidas que importan al negocio: el aviso enviado, el préstamo guardado. No verifiques las llamadas de lectura que ya cubre un stub (`verify(loans).findOverdue(TODAY)`): si el servicio no las hiciera, el resultado ya sería otro. Un test que verifica cada llamada se rompe en cuanto cambias cómo el servicio hace su trabajo, aunque el resultado siga siendo el mismo.

---

## Matchers

| Matcher | Acepta |
|---|---|
| `any()` | Cualquier valor, `null` incluido |
| `any(Notice.class)` | Cualquier `Notice` que no sea `null` |
| `anyString()`, `anyInt()` | Cualquier `String` o `int` |
| `eq(valor)` | Un valor igual (`equals`) |
| `argThat(n -> n.to().endsWith("@example.com"))` | Lo que cumpla la condición |

Sirven igual en `when` y en `verify`.

**Regla:** si un argumento es un matcher, todos lo son. Mezclar un valor fijo y un matcher falla:

```java
verify(loans).markNotified("L-1", any());   // mal
verify(loans).markNotified(eq("L-1"), any()); // bien
```

```text
InvalidUseOfMatchers
Invalid use of argument matchers!
2 matchers expected, 1 recorded:
...
When using matchers, all arguments have to be provided by matchers.
```

---

## ArgumentCaptor

El servicio crea el `Notice` dentro de `notifyOverdue()`. Para comprobar su texto, el test necesita ese objeto. Un **captor** lo recoge durante `verify`:

```java
@Captor
private ArgumentCaptor<Notice> notice;

@Test
void shouldWriteTheNoticeWithTheDaysLate() {
    when(loans.findOverdue(TODAY)).thenReturn(List.of(DUNE));

    overdueNotifier.notifyOverdue();

    verify(notifier).send(notice.capture());
    assertThat(notice.getValue())
        .isEqualTo(new Notice("ana@example.com", "Overdue: Dune", "\"Dune\" is 3 day(s) late."));
}
```

- `@Captor` lo crea `MockitoExtension`. Sin la extensión: `ArgumentCaptor.forClass(Notice.class)`.
- `capture()` ocupa el lugar del argumento dentro de `verify`.
- `getValue()` devuelve el último valor; `getAllValues()`, todos, en orden:

  ```java
  verify(notifier, times(2)).send(notice.capture());
  assertThat(notice.getAllValues())
      .extracting(Notice::to)
      .containsExactly("ana@example.com", "luis@example.com");
  ```

Si conoces el objeto entero y tiene `equals` (un `record`, por ejemplo), `verify(notifier).send(new Notice(...))` basta. El captor compensa cuando quieres comprobar solo algunos campos, varios valores o un objeto sin `equals`.

---

## Orden y llamadas de más

```java
InOrder inOrder = inOrder(notifier, loans);
inOrder.verify(notifier).send(any());
inOrder.verify(loans).markNotified("L-1", TODAY);
verifyNoMoreInteractions(notifier);
```

- `inOrder(...)` comprueba que las llamadas ocurrieron en ese orden, aunque haya otras entre medias.
- `verifyNoMoreInteractions(notifier)` falla si el mock recibió alguna llamada que el test no verificó. Úsalo con cuidado: tiene el mismo problema que verificar todo.
- `verifyNoInteractions(notifier)` falla si el mock recibió cualquier llamada.

Si el servicio marcara el préstamo antes de enviar el aviso:

```text
Verification in order failure
Wanted but not invoked:
loans.markNotified("L-1", 2026-03-16);
-> at com.bootcamp.OverdueNotifierTest.shouldMarkTheLoanOnlyAfterSendingTheNotice(OverdueNotifierTest.java:144)
Wanted anywhere AFTER following interaction:
notifier.send(
    Notice[to=ana@example.com, subject=Overdue: Dune, body="Dune" is 3 day(s) late.]
);
```

---

## ✅ Resumen

- `when` controla lo que entra al servicio; `verify` comprueba lo que sale.
- `times`, `never` y `atLeastOnce` cuentan llamadas; los mensajes de fallo enseñan las llamadas reales.
- Con matchers, todos los argumentos son matchers: `eq(...)` para los valores fijos.
- `ArgumentCaptor` recoge los objetos que el servicio construye; `InOrder` comprueba el orden.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 01 - Mocks y stubbing con Mockito](01-mocks-y-stubbing.md) | [Teoría — 03 - Strict stubbing, spies y mockStatic](03-strict-stubs-spies-y-static.md) → |

[↑ Semana 26](../README.md)
