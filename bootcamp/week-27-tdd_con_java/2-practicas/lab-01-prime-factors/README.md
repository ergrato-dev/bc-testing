# Lab 01 - Kata Prime Factors

## Objetivo

Escribir con TDD un método que descompone un número en factores primos: `PrimeFactors.of(12)` devuelve `[2, 2, 3]`. Vas a ver cómo siete tests sencillos llevan a un algoritmo de seis líneas sin que lo diseñes de antemano, y a reconocer un test de confirmación.

## Tiempo estimado

55 minutos.

## Preparación

```bash
cd starter
mvn test
# [INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0
```

- `src/main/java/com/bootcamp/PrimeFactors.java` tiene el método `of(int n)`, que de momento lanza `UnsupportedOperationException`. **Este archivo lo escribes tú.**
- `src/test/java/com/bootcamp/PrimeFactorsTest.java` trae nueve PASOS comentados. Descomenta uno cada vez.

La [teoría 01](../../1-teoria/01-ciclo-tdd-en-java.md) explica las dos clases de Red y el test de confirmación. En cada paso, ejecuta el test **antes** de escribir el Green y comprueba que falla con el mensaje indicado.

## Paso a paso

### Paso 1: uno no tiene factores (PASO 1)

Descomenta el PASO 1.

```bash
mvn test
# [ERROR]   PrimeFactorsTest.shouldReturnNoFactorsForOne:NN » UnsupportedOperation TODO: PrimeFactors.of
# [ERROR] Tests run: 1, Failures: 0, Errors: 1, Skipped: 0
```

Es un `Error`, no un `Failure`: el método lanzó una excepción. Green: devuelve una lista vacía.

```java
import java.util.ArrayList;
import java.util.List;

public static List<Integer> of(int n) {
    return new ArrayList<>();
}
```

```bash
mvn test
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

`new ArrayList<>()` y no `List.of()`: los pasos siguientes añaden elementos a esta lista, y `List.of()` es inmutable.

### Paso 2: el primer primo (PASO 2)

Descomenta el PASO 2 y ejecuta.

```text
Expecting actual:
  []
to contain exactly (and in same order):
  [2]
but could not find the following elements:
  [2]
```

Green:

```java
List<Integer> factors = new ArrayList<>();
if (n > 1) {
    factors.add(2);
}
return factors;
```

Añadir un `2` fijo parece hacer trampa, pero es lo mínimo que pasa el test. El paso 3 lo obliga a generalizar (la teoría 02 llama a esto triangular).

### Paso 3: otro primo (PASO 3)

```text
Expecting actual:
  [2]
to contain exactly (and in same order):
  [3]
```

Green: cambia `factors.add(2)` por `factors.add(n)`.

```bash
mvn test
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 4: un factor repetido (PASO 4)

```text
Expecting actual:
  [4]
to contain exactly (and in same order):
  [2, 2]
```

Green: si `n` es par, apunta un `2` y divide.

```java
List<Integer> factors = new ArrayList<>();
if (n % 2 == 0) {
    factors.add(2);
    n /= 2;
}
if (n > 1) {
    factors.add(n);
}
return factors;
```

`%` es el resto de la división: `n % 2 == 0` significa "n es par". `n /= 2` es `n = n / 2`.

### Paso 5: dos factores distintos (PASO 5)

```bash
mvn test
# [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

Pasa a la primera: es un **test de confirmación**. Comprueba que el test funciona rompiendo el código a propósito: comenta la línea `n /= 2;`.

```bash
mvn test
# [ERROR]   PrimeFactorsTest.shouldFactorFour:NN
# [ERROR]   PrimeFactorsTest.shouldFactorSix:NN
# [ERROR]   PrimeFactorsTest.shouldFactorTwo:NN
# [ERROR] Tests run: 5, Failures: 3, Errors: 0, Skipped: 0
```

`shouldFactorSix` falla, así que el test comprueba algo. Vuelve a poner la línea.

### Paso 6: tres factores repetidos (PASO 6)

```text
Expecting actual:
  [2, 4]
to contain exactly (and in same order):
  [2, 2, 2]
```

Green: cambia el primer `if` por `while`. Ahora divide por 2 mientras puede.

```bash
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 7: un divisor que no es 2 (PASO 7)

```text
Expecting actual:
  [9]
to contain exactly (and in same order):
  [3, 3]
```

Green: el `2` fijo se convierte en una variable que va subiendo.

```java
List<Integer> factors = new ArrayList<>();
int divisor = 2;
while (n > 1) {
    while (n % divisor == 0) {
        factors.add(divisor);
        n /= divisor;
    }
    divisor++;
}
return factors;
```

```bash
mvn test
# [INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
```

Refactor: el `if (n > 1) factors.add(n)` del final ya no hace falta, porque el bucle exterior sigue hasta que `n` llega a 1. Si lo dejaste, bórralo y ejecuta otra vez.

Fíjate en que nunca decidiste el algoritmo: cada test cambió una sola cosa (`if` → `while`, `2` → `divisor`).

### Paso 8: números más grandes (PASO 8)

Descomenta el PASO 8 y sus imports. Es un `@ParameterizedTest` con `@MethodSource` (lo viste en la [semana 25](../../../week-25-junit_6_avanzado_y_assertj/1-teoria/01-tests-parametrizados.md)).

```bash
mvn test
# [INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
```

Otro test de confirmación, esta vez a propósito: `101` es primo, `1024` es `2` elevado a 10 y `9 699 690` es el producto de los ocho primeros primos. Documentan que el algoritmo funciona más allá de los casos pequeños.

### Paso 9: entradas no válidas (PASO 9)

Descomenta el PASO 9 y su import.

```bash
mvn test
# [ERROR]   PrimeFactorsTest.shouldRejectNumbersBelowOne:NN
# [ERROR]   PrimeFactorsTest.shouldRejectNumbersBelowOne:NN
# [ERROR] Tests run: 13, Failures: 2, Errors: 0, Skipped: 0
```

```text
Expecting code to raise a throwable.
```

Con `0` o `-6`, el bucle no se ejecuta y el método devuelve `[]` sin quejarse. Green: una guarda al principio del método.

```java
if (n < 1) {
    throw new IllegalArgumentException("n must be 1 or greater: " + n);
}
```

```bash
mvn test
# [INFO] Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
```

## Comprobación

```bash
cd ../solution
mvn test
# [INFO] Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

Compara tu `PrimeFactors.java` con el de `solution/`.

## Preguntas de cierre

1. En el paso 2, ¿por qué `factors.add(2)` es un Green válido aunque sea "trampa"?
2. ¿Qué habría pasado si el paso 4 hubiera sido `of(8)` en lugar de `of(4)`?
3. El paso 5 pasó a la primera. ¿Cómo supiste que el test no estaba mal?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Kata Game of Life](../lab-02-game-of-life/README.md) → |

[↑ Semana 27](../../README.md)
