# Lab 02 - Kata Game of Life

## Objetivo

Implementar con TDD el Juego de la Vida de Conway y dejar que los tests decidan las clases: un `enum` para la célula, un `record` para la casilla y una clase inmutable para el tablero. Al final refactorizas con la suite como red y metes un error a propósito para ver que la red lo atrapa.

## Tiempo estimado

75 minutos.

## Preparación

```bash
cd starter
mvn test
# [INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0
```

- `src/main/java/com/bootcamp/` está vacío. **Todas las clases las creas tú**, cuando un test las pida.
- `src/test/java/com/bootcamp/` trae tres clases de test con once PASOS comentados: `CellTest` (PASOS 1 a 5), `PositionTest` (PASO 6) y `GenerationTest` (PASOS 7 a 11).

Las reglas del juego y el porqué de cada tipo están en la [teoría 02](../../1-teoria/02-diseno-emergente-y-refactoring.md).

## Paso a paso

### Paso 1: una célula viva con menos de dos vecinas muere (PASO 1)

Descomenta el PASO 1 de `CellTest`.

```bash
mvn test
# [ERROR] COMPILATION ERROR :
# [ERROR] .../CellTest.java:[NN,63] cannot find symbol
# [ERROR] .../CellTest.java:[NN,24] package Cell does not exist
```

Red de compilación: `Cell` no existe. En IntelliJ, pon el cursor sobre `Cell` y pulsa `Alt+Enter` → **Create enum 'Cell'**. Si no usas IntelliJ, crea `src/main/java/com/bootcamp/Cell.java` a mano.

Green:

```java
package com.bootcamp;

public enum Cell {
    ALIVE,
    DEAD;

    public Cell next(int liveNeighbours) {
        return DEAD;
    }
}
```

```bash
mvn test
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

Un `@ParameterizedTest` con `@ValueSource(ints = {0, 1})` cuenta como dos ejecuciones.

### Paso 2: con dos o tres vecinas sobrevive (PASO 2)

```bash
mvn test
# [ERROR]   CellTest.shouldSurviveWithTwoOrThreeLiveNeighbours:NN
# [ERROR]   CellTest.shouldSurviveWithTwoOrThreeLiveNeighbours:NN
# [ERROR] Tests run: 4, Failures: 2, Errors: 0, Skipped: 0
```

```text
expected: ALIVE
 but was: DEAD
```

Green:

```java
public Cell next(int liveNeighbours) {
    if (liveNeighbours == 2 || liveNeighbours == 3) {
        return ALIVE;
    }
    return DEAD;
}
```

### Paso 3: con más de tres vecinas muere (PASO 3)

```bash
mvn test
# [INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
```

Test de confirmación: el `return DEAD` del final ya cubre la superpoblación.

### Paso 4: una célula muerta con tres vecinas nace (PASO 4)

```bash
mvn test
# [INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
```

También pasa, pero **por casualidad**: `next` todavía no mira si la célula está viva o muerta, y con 3 vecinas devuelve `ALIVE` para las dos. El paso 5 lo destapa.

### Paso 5: con cualquier otro número sigue muerta (PASO 5)

```bash
mvn test
# [ERROR]   CellTest.shouldStayDeadWithAnyOtherNumberOfLiveNeighbours:NN
# [ERROR] Tests run: 18, Failures: 1, Errors: 0, Skipped: 0
```

```text
expected: DEAD
 but was: ALIVE
```

Una célula muerta con 2 vecinas "sobrevive". Green: la regla de las muertas, primero.

```java
public Cell next(int liveNeighbours) {
    if (this == DEAD) {
        return liveNeighbours == 3 ? ALIVE : DEAD;
    }
    if (liveNeighbours == 2 || liveNeighbours == 3) {
        return ALIVE;
    }
    return DEAD;
}
```

```bash
mvn test
# [INFO] Tests run: 18, Failures: 0, Errors: 0, Skipped: 0
```

Refactor: con la suite en verde, cambia el método por un `switch` que trate los dos estados igual:

```java
public Cell next(int liveNeighbours) {
    return switch (this) {
        case ALIVE -> liveNeighbours == 2 || liveNeighbours == 3 ? ALIVE : DEAD;
        case DEAD -> liveNeighbours == 3 ? ALIVE : DEAD;
    };
}
```

Ejecuta otra vez: `Tests run: 18, Failures: 0`. El `switch` sobre un `enum` no necesita `default`: si algún día añades un tercer estado, el compilador te avisa de que falta su caso.

### Paso 6: las ocho vecinas de una casilla (PASO 6)

Descomenta el PASO 6 de `PositionTest`. Red de compilación: `Position` no existe. Créala como `record` (la teoría 02 explica por qué):

```java
package com.bootcamp;

import java.util.HashSet;
import java.util.Set;

/** Casilla del tablero. El tablero no tiene bordes: las coordenadas pueden ser negativas. */
public record Position(int row, int column) {

    public Set<Position> neighbours() {
        Set<Position> neighbours = new HashSet<>();
        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dColumn = -1; dColumn <= 1; dColumn++) {
                if (dRow != 0 || dColumn != 0) {
                    neighbours.add(new Position(row + dRow, column + dColumn));
                }
            }
        }
        return neighbours;
    }
}
```

`dRow` y `dColumn` van de -1 a 1; la condición salta el `(0, 0)`, que es la propia casilla.

```bash
mvn test
# [INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 7: un tablero vacío sigue vacío (PASO 7)

Descomenta el PASO 7 de `GenerationTest`. Red de compilación: falta `Generation`. Green mínimo:

```java
package com.bootcamp;

import java.util.Set;

public final class Generation {

    private final Set<Position> alive;

    public Generation(Set<Position> alive) {
        this.alive = Set.copyOf(alive);
    }

    public Set<Position> aliveCells() {
        return alive;
    }

    public Generation next() {
        return new Generation(Set.of());
    }
}
```

`Set.copyOf` guarda una copia que no se puede modificar: quien creó la generación no puede cambiarla después.

### Paso 8: una célula sola muere (PASO 8)

```bash
mvn test
# [INFO] Tests run: 21, Failures: 0, Errors: 0, Skipped: 0
```

Confirmación: `next()` siempre devuelve un tablero vacío, y eso es lo correcto aquí.

### Paso 9: un bloque no cambia (PASO 9)

```bash
mvn test
# [ERROR]   GenerationTest.shouldKeepABlockUnchanged:NN
# [ERROR] Tests run: 22, Failures: 1, Errors: 0, Skipped: 0
```

```text
expected:
  [Position[row=1, column=1],
      Position[row=0, column=1],
      Position[row=0, column=0],
      Position[row=1, column=0]]
 but was:
  []
```

El orden de las cuatro posiciones puede cambiar en cada ejecución: un `Set` no tiene orden. Green: cada célula viva sobrevive o muere según sus vecinas vivas.

```java
public Generation next() {
    return new Generation(alive.stream()
        .filter(position -> {
            int liveNeighbours = (int) position.neighbours().stream().filter(alive::contains).count();
            return Cell.ALIVE.next(liveNeighbours) == Cell.ALIVE;
        })
        .collect(Collectors.toSet()));
}
```

Añade `import java.util.stream.Collectors;`. `alive::contains` es una referencia a método: equivale a `p -> alive.contains(p)`.

### Paso 10: el parpadeador gira (PASO 10)

Tres células en fila se convierten en tres en columna.

```text
Expecting actual:
  [Position[row=1, column=1]]
to contain exactly in any order:
  [Position[row=0, column=1],
    Position[row=1, column=1],
    Position[row=2, column=1]]
but could not find the following elements:
  [Position[row=0, column=1], Position[row=2, column=1]]
```

Faltan los nacimientos: el código solo mira las células vivas, y las que nacen están muertas. Green: las candidatas son las vivas **y** sus vecinas.

```java
public Generation next() {
    Set<Position> candidates = new HashSet<>(alive);
    alive.forEach(position -> candidates.addAll(position.neighbours()));
    return new Generation(candidates.stream()
        .filter(position -> {
            Cell cell = alive.contains(position) ? Cell.ALIVE : Cell.DEAD;
            int liveNeighbours = (int) position.neighbours().stream().filter(alive::contains).count();
            return cell.next(liveNeighbours) == Cell.ALIVE;
        })
        .collect(Collectors.toSet()));
}
```

Añade `import java.util.HashSet;`.

```bash
mvn test
# [INFO] Tests run: 23, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 11: el parpadeador vuelve tras dos generaciones (PASO 11)

```bash
mvn test
# [INFO] Tests run: 24, Failures: 0, Errors: 0, Skipped: 0
```

Confirmación: `next().next()` funciona porque cada `next()` devuelve una generación nueva.

### Paso 12: refactor

El `filter` de `next()` mezcla tres ideas. Con la suite en verde, extrae dos métodos privados (`Ctrl+Alt+M` en IntelliJ), uno cada vez, y ejecuta `mvn test` después de cada extracción:

```java
public Generation next() {
    Set<Position> candidates = new HashSet<>(alive);
    alive.forEach(position -> candidates.addAll(position.neighbours()));
    return new Generation(candidates.stream()
        .filter(position -> cellAt(position).next(liveNeighboursOf(position)) == Cell.ALIVE)
        .collect(Collectors.toSet()));
}

private Cell cellAt(Position position) {
    return alive.contains(position) ? Cell.ALIVE : Cell.DEAD;
}

private int liveNeighboursOf(Position position) {
    return (int) position.neighbours().stream().filter(alive::contains).count();
}
```

### Paso 13: experimento, un error durante el refactor

Imagina que, al pasar `Cell` a `switch` en el paso 5, escribiste `>=` en lugar de `==`:

```java
case DEAD -> liveNeighbours >= 3 ? ALIVE : DEAD;
```

```bash
mvn test
# [ERROR]   CellTest.shouldStayDeadWithAnyOtherNumberOfLiveNeighbours:NN   (5 veces, de 4 a 8 vecinas)
# [ERROR] Tests run: 24, Failures: 5, Errors: 0, Skipped: 0
```

Ahora cambia `||` por `&&` en la condición de `Position.neighbours()`:

```bash
mvn test
# [ERROR]   GenerationTest.shouldBringTheBlinkerBackAfterTwoGenerations:NN
# [ERROR]   GenerationTest.shouldKeepABlockUnchanged:NN
# [ERROR]   GenerationTest.shouldTurnAHorizontalBlinkerIntoAVerticalOne:NN
# [ERROR]   PositionTest.shouldHaveEightNeighboursAroundIt:NN
# [ERROR] Tests run: 24, Failures: 4, Errors: 0, Skipped: 0
```

Los dos errores los detecta la suite en segundos. Deshaz los dos cambios.

## Comprobación

```bash
cd ../solution
mvn test
# [INFO] Tests run: 24, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

## Preguntas de cierre

1. ¿Qué test decidió que `Position` fuera un `record` y no una clase normal?
2. El paso 4 pasó "por casualidad". ¿Qué habría pasado si te hubieras saltado el paso 5?
3. En el paso 13, el error de `Position` lo detectaron tests de `GenerationTest`. ¿Por qué es bueno tener también `PositionTest`?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Kata Prime Factors](../lab-01-prime-factors/README.md) | [Lab 03 - Outside-in con Mockito](../lab-03-outside-in-con-mockito/README.md) → |

[↑ Semana 27](../../README.md)
