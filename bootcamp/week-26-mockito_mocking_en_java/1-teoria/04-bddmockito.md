# 04 - BDDMockito

> Lenguaje: **Java**

---

## 🎯 Objetivos

- Escribir tests de Mockito con el vocabulario dado / cuando / entonces.
- Traducir cada llamada de Mockito a su equivalente de BDDMockito.

---

## El problema de when

Los tests de la semana siguen el patrón AAA (Arrange, Act, Assert). En BDD, el que viste con behave en la [semana 22](../../week-22-bdd_con_python_behave/README.md), esas tres partes se llaman **given**, **when** y **then**. Con Mockito, el choque se ve en la primera línea:

```java
when(accounts.hasUnpaidFines("M-001")).thenReturn(false);   // es la preparación, no la acción
```

`when` aparece en la parte "given" del test. BDDMockito es una clase de `mockito-core` (no hace falta otra dependencia) que da a los mismos métodos nombres que encajan con cada parte.

---

## Equivalencias

| Mockito | BDDMockito |
|---|---|
| `when(m.metodo()).thenReturn(v)` | `given(m.metodo()).willReturn(v)` |
| `when(m.metodo()).thenThrow(e)` | `given(m.metodo()).willThrow(e)` |
| `doReturn(v).when(spy).metodo()` | `willReturn(v).given(spy).metodo()` |
| `verify(m).metodo()` | `then(m).should().metodo()` |
| `verify(m, never()).metodo()` | `then(m).should(never()).metodo()` |
| `verifyNoInteractions(m)` | `then(m).shouldHaveNoInteractions()` |
| `verifyNoMoreInteractions(m)` | `then(m).shouldHaveNoMoreInteractions()` |
| `inOrder.verify(m).metodo()` | `then(m).should(inOrder).metodo()` |

```java
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
```

---

## Un test completo

```java
@Test
@DisplayName("should lend a copy to a member without fines")
void shouldLendACopyToAMemberWithoutFines() {
    // given
    given(accounts.hasUnpaidFines("M-001")).willReturn(false);
    given(inventory.reserveCopy(DUNE)).willReturn(true);

    // when
    Checkout checkout = service.checkout("M-001", DUNE);

    // then
    assertThat(checkout).isEqualTo(new Checkout("M-001", DUNE));
    then(notifier).should().send("M-001", "You borrowed " + DUNE);
}
```

Cuando la acción y la comprobación van juntas (una excepción), se suele escribir `// when / then`:

```java
// given
given(accounts.hasUnpaidFines("M-002")).willReturn(true);

// when / then
assertThatThrownBy(() -> service.checkout("M-002", DUNE))
    .isInstanceOf(IllegalStateException.class)
    .hasMessage("member has unpaid fines: M-002");
then(inventory).shouldHaveNoInteractions();
```

---

## Mismo motor, mismos mensajes

BDDMockito solo cambia los nombres. Un fallo de `then(...).should()` da el mismo mensaje que `verify`:

```text
[ERROR]   CheckoutServiceTest.shouldLendACopyToAMemberWithoutFines:59
Argument(s) are different! Wanted:
notifier.send(
    "M-001",
    "You borrowed Dune"
);
```

Strict stubs, matchers y captores funcionan igual. Puedes mezclar las dos formas en el mismo archivo, pero elige una por suite para que todos los tests se lean igual.

---

## ✅ Resumen

- `given(...).willReturn(...)` prepara; `then(mock).should()` comprueba.
- Cada método de Mockito tiene su equivalente en BDDMockito, en la misma dependencia.
- Elige una de las dos formas por suite.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 03 - Strict stubbing, spies y mockStatic](03-strict-stubs-spies-y-static.md) | [Índice de labs](../2-practicas/README.md) → |

[↑ Semana 26](../README.md)
