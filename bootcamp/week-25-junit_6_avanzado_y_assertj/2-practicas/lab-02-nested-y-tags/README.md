# Lab 02 - @Nested y @Tag

## Objetivo

Reorganizar la suite de un préstamo de biblioteca en contextos `@Nested`, cada uno con su `@BeforeEach`, y ejecutar solo una parte de la suite con `@Tag`. Vas a comprobar qué pasa si olvidas `@Nested`.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
mvn test
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

- `src/main/java/com/bootcamp/Loan.java` es un préstamo con fecha de devolución. Se puede renovar dos veces (una semana cada vez) y devolver una sola vez.
- `src/test/java/com/bootcamp/LoanTest.java` crea el préstamo en un `@BeforeEach`, trae tres tests activos (PASO 0) y los PASOS 1 a 4 comentados.

La [teoría 02](../../1-teoria/02-nested-y-tags.md) explica `@Nested` y `@Tag`.

## Paso a paso

### Paso 1: leer el problema (PASO 0)

Los tres tests del PASO 0 empiezan con la misma línea, `loan.returnOn(DUE.plusDays(3))`, y llevan el contexto en el nombre: `returnedShould...`. Es el síntoma de un contexto que pide su propia clase.

### Paso 2: el contexto "activo" (PASO 1)

1. Descomenta el import marcado con `PASO 1`.
2. Descomenta la clase `WhenActive` del PASO 1. Selecciona el bloque entero y usa el atajo de comentar de tu editor (`Ctrl+/` en VS Code e IntelliJ): quita un solo nivel de `//`, así que los bloques de dentro marcados con `PASO 2` y `PASO 4` siguen comentados.

```bash
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

`WhenActive` usa el campo `loan` de la clase de fuera: por eso no es `static`.

### Paso 3: un contexto dentro de otro (PASO 2)

Dentro de `WhenActive`, descomenta la clase `AfterTwoRenewals`.

```bash
mvn test
# [INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
```

Para `shouldRejectAThirdRenewal`, JUnit ejecuta primero `createLoan()` (de `LoanTest`) y después `renewTwice()` (de `AfterTwoRenewals`). El diagrama de la teoría 02 muestra ese orden.

### Paso 4: el Arrange repetido pasa a un @BeforeEach (PASO 3)

1. Borra los tres tests del PASO 0.
2. Descomenta la clase `WhenReturnedThreeDaysLate` del PASO 3, sin los bloques de `PASO 4`.

```bash
mvn test
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.006 s -- in after two renewals
# [INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.089 s -- in when active
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.009 s -- in when returned three days late
# [INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.112 s -- in Loan
# [INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
```

Surefire usa el `@DisplayName` de cada clase. Los recuentos por clase son raros (la teoría 02 explica por qué); fíjate en el total: 8.

### Paso 5: experimento, olvidar @Nested

Borra la línea `@Nested` de encima de `@DisplayName("when returned three days late")` y ejecuta:

```bash
mvn test
# WARNING: TestEngine with ID 'junit-jupiter' encountered a non-critical issue during test discovery:
# (1) [WARNING] Inner class 'com.bootcamp.LoanTest$WhenReturnedThreeDaysLate' looks like it was intended to be a test class but will not be executed. It must be static or annotated with @Nested.
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

`BUILD SUCCESS` con cuatro tests menos. Vuelve a poner `@Nested`.

### Paso 6: etiquetas (PASO 4)

1. Descomenta el import marcado con `PASO 4`.
2. Descomenta las dos líneas `@Tag("smoke")`.

Ejecuta solo los tests `smoke` y, después, todos menos ellos:

```bash
mvn test -Dgroups=smoke
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
mvn test -DexcludedGroups=smoke
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

Prueba también con una etiqueta mal escrita:

```bash
mvn test -Dgroups=smok
# [INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

Ningún error: el filtro no encontró tests. Revisa siempre el total.

## Comprobación

```bash
cd ../solution
mvn test
# [INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

## Preguntas de cierre

1. ¿Por qué la clase `@Nested` no puede ser `static` en este lab?
2. Un test de `AfterTwoRenewals` modifica `loan`. ¿Por qué ese cambio no llega al siguiente test?
3. ¿Qué etiquetas pondrías en la suite de tu dominio y qué comando ejecutarías antes de cada commit?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Tests parametrizados](../lab-01-tests-parametrizados/README.md) | [Lab 03 - @TempDir y extensiones](../lab-03-extensiones-y-tempdir/README.md) → |

[↑ Semana 25](../../README.md)
