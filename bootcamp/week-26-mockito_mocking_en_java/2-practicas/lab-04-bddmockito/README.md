# Lab 04 - BDDMockito

## Objetivo

Reescribir los tests de un servicio de préstamos con el vocabulario de BDDMockito (`given`, `willReturn`, `willThrow`, `then(...).should()`) para que cada test se lea en tres bloques: dado, cuando, entonces.

## Tiempo estimado

55 minutos.

## Preparación

```bash
cd starter
mvn test
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

- `src/main/java/com/bootcamp/CheckoutService.java` presta un ejemplar: comprueba que el socio no tiene multas (`MemberAccounts`), aparta un ejemplar (`Inventory`) y avisa al socio (`Notifier`).
- `src/test/java/com/bootcamp/CheckoutServiceTest.java` trae tres mocks con `@Mock`, dos tests escritos con `when` y `verify` (PASO 0) y los PASOS 1 a 4 comentados.

La [teoría 04](../../1-teoria/04-bddmockito.md) muestra la equivalencia entre las dos formas.

## Paso a paso

### Paso 1: leer el problema (PASO 0)

Lee `shouldLendACopyWithWhenAndVerify`. La primera línea dice `when(...)`, pero pertenece a la preparación del test (Arrange, o "given" en BDD), no a la acción. En un test con varios stubs, leer `when` arriba y `verify` abajo obliga a pensar en dos vocabularios.

### Paso 2: given y then().should() (PASO 1)

1. Borra los dos tests del PASO 0 y los tres imports marcados con `PASO 0`.
2. Descomenta los imports marcados con `PASO 1` y el test del PASO 1. Sus líneas `// given`, `// when` y `// then` llevan doble comentario: después de descomentar el bloque siguen siendo comentarios.

```bash
mvn test
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

- `given(mock.metodo(...)).willReturn(valor)` es lo mismo que `when(...).thenReturn(...)`.
- `then(mock).should().metodo(...)` es lo mismo que `verify(mock).metodo(...)`.

Experimento: en la última línea del test, cambia `"You borrowed " + DUNE` por `"You borrowed Dune"`.

```bash
mvn test
# [ERROR]   CheckoutServiceTest.shouldLendACopyToAMemberWithoutFines:NN
# Argument(s) are different! Wanted:
# notifier.send(
#     "M-001",
#     "You borrowed Dune"
# );
```

Es el mismo mensaje del lab 02: BDDMockito solo cambia los nombres, el mecanismo es el de siempre. Deshaz el cambio.

### Paso 3: lo que no debe pasar (PASO 2)

Descomenta los dos tests del PASO 2.

```bash
mvn test
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

- `then(notifier).should(never()).send(anyString(), anyString())` falla si hubo algún aviso.
- `then(inventory).shouldHaveNoInteractions()` falla si el mock recibió **cualquier** llamada: un socio con multas no debe apartar ningún ejemplar.

### Paso 4: una dependencia que falla (PASO 3)

Descomenta el test del PASO 3.

```bash
mvn test
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
```

`given(...).willThrow(...)` es el `thenThrow` del lab 01. El test comprueba que, si el inventario no responde, el socio no recibe un aviso de un préstamo que no se hizo.

### Paso 5: el orden (PASO 4)

Descomenta los imports marcados con `PASO 4` y el test del PASO 4.

```bash
mvn test
# [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

`then(accounts).should(inOrder)` es el `inOrder.verify(accounts)` del lab 02.

## Comprobación

```bash
cd ../solution
mvn test
# [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

## Preguntas de cierre

1. ¿Qué ganas al escribir `given` en lugar de `when` en la preparación del test?
2. ¿Qué diferencia hay entre `should(never()).send(...)` y `shouldHaveNoInteractions()`?
3. ¿Usarías las dos formas (`when` y `given`) en la misma suite? ¿Por qué?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - Strict stubs, spies y mockStatic](../lab-03-strict-stubs-spy-y-static/README.md) | [Proyecto — Proyecto Semanal - Aislar las dependencias del dominio con Mockito](../../3-proyecto/README.md) → |

[↑ Semana 26](../../README.md)
