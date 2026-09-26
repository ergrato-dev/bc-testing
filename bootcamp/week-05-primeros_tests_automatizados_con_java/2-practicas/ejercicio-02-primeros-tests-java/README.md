# Ejercicio 02 — Primeros Tests Unitarios en Java

> **Semana 05 · Prácticas · Ejercicio 02** | Duración estimada: 2 h

---

## Objetivo

Escribir una suite inicial en JUnit 5 aplicando:

- Patrón AAA
- Assertions básicas (`assertTrue`, `assertFalse`, `assertEquals`, `assertThrows`)
- `assertEquals` con delta para valores `double`
- Diferencia entre **failure** y **error** en el reporte de Surefire
- Assertions fluentes con AssertJ
- `@DisplayName` para legibilidad

---

## Instrucciones

### Paso 1 — Preparar

```bash
cd starter
mvn test
```

La primera ejecución descarga dependencias y termina con `Tests run: 0`.

### Paso 2 — PASO 1 a PASO 3: assertions básicas

Abre `starter/src/test/java/com/bootcamp/UserUtilsTest.java` y descomenta los bloques `PASO 1`, `PASO 2` y `PASO 3`, ejecutando `mvn test` después de cada uno. Deben quedar 6 tests en verde.

### Paso 3 — PASO 4: decimales con delta

Descomenta el `PASO 4` tal como está y ejecuta:

```text
[ERROR]   UserUtilsTest.shouldReturnDiscountedPriceWithinDeltaWhenPriceHasDecimals:121 expected: <15.992> but was: <15.991999999999999>
```

`19.99 - (19.99 * 20) / 100` no da exactamente `15.992` en aritmética de punto flotante. Cambia la assertion por `assertEquals(15.992, result, 0.0001)`: el tercer argumento (delta) es la diferencia máxima tolerada.

### Paso 4 — PASO 5: failure vs error

Descomenta el `PASO 5` y ejecuta:

```text
[ERROR] Errors:
[ERROR]   UserUtilsTest.shouldReturnFalseWhenEmailIsNull:137 » NullPointer Cannot invoke "String.contains(java.lang.CharSequence)" because "email" is null
[ERROR] Tests run: 10, Failures: 1, Errors: 1, Skipped: 0
```

(Si ya corregiste el `PASO 4`, verás `Failures: 0, Errors: 1`.)

La assertion nunca llegó a ejecutarse: el código de producción lanzó una excepción inesperada, y Surefire lo cuenta como **Error**, no como **Failure**. El test encontró un bug real: `isValidEmail(null)` revienta. Corrige `starter/src/main/java/com/bootcamp/UserUtils.java` para que devuelva `false` cuando el email es `null`.

### Paso 5 — PASO 6: AssertJ

Descomenta el `PASO 6`. Compara el estilo fluente de AssertJ con las assertions de JUnit:

| Intención | JUnit 5 | AssertJ |
|---|---|---|
| Igualdad | `assertEquals(80.0, result)` | `assertThat(result).isEqualTo(80.0)` |
| Excepción | `assertThrows(IllegalArgumentException.class, () -> ...)` | `assertThatThrownBy(() -> ...).isInstanceOf(IllegalArgumentException.class).hasMessage("...")` |

### Paso 6 — Suite completa

```text
Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Compara tu resultado con `solution/`.

---

## Resultado esperado

- 10 tests en verde
- Happy path + casos inválidos + caso `null`
- Estructura AAA consistente
- Puedes explicar por qué el `PASO 5` fue un error y no un failure
