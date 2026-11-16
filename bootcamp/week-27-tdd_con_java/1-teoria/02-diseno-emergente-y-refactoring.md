# 02 - Diseño emergente y refactoring seguro

> Lenguaje: **Java**

---

## 🎯 Objetivos

- Ver cómo los tests deciden las clases de un programa: qué es un `enum`, qué es un `record` y qué es una clase.
- Elegir el siguiente test para que el código crezca en pasos pequeños.
- Refactorizar con la suite como red de seguridad, sin cambiar el comportamiento.
- Evitar el error de usar las constantes del código como valor esperado.

---

## De dónde partes

El diseño emergente y el refactor seguro los viste en la [semana 11](../../week-11-tdd_con_jest/1-teoria/02-micro-ciclos-y-diseno-emergente.md) y en la [semana 20](../../week-20-tdd_con_python/1-teoria/02-diseno-emergente-y-codigo-legado.md), donde un test pedía una `dataclass` o un `Protocol`. En Java los tipos son obligatorios: cada test que escribes antes de que exista el código te obliga a decidir **qué tipo** devuelve y **a quién** se lo pides. Ese es el diseño.

---

## El kata Game of Life

El [lab 02](../2-practicas/lab-02-game-of-life/README.md) implementa el Juego de la Vida de John Conway. Un tablero sin bordes; cada casilla está viva o muerta, y en cada generación:

| Célula | Vecinas vivas | Generación siguiente |
|---|---|---|
| Viva | 0 o 1 | Muere (soledad) |
| Viva | 2 o 3 | Sobrevive |
| Viva | 4 a 8 | Muere (superpoblación) |
| Muerta | Exactamente 3 | Nace |
| Muerta | Cualquier otro número | Sigue muerta |

Nadie escribe primero un diagrama de clases. Cada test pide una pieza y el compilador te dice que no existe.

### 1. Una regla pide un enum

```java
@ParameterizedTest
@ValueSource(ints = {0, 1})
void shouldDieWithFewerThanTwoLiveNeighbours(int liveNeighbours) {
    assertThat(Cell.ALIVE.next(liveNeighbours)).isEqualTo(Cell.DEAD);
}
```

El test escribe `Cell.ALIVE` antes de que exista `Cell`. Una célula solo puede estar en dos estados y el test le pide la regla a la propia célula: el tipo que encaja es un `enum` con un método.

```java
public enum Cell {
    ALIVE,
    DEAD;

    public Cell next(int liveNeighbours) {
        return DEAD;
    }
}
```

`return DEAD` es todo el Green: es lo mínimo que hace pasar el test. El siguiente test (sobrevive con 2 o 3) lo obliga a cambiar.

### 2. Una casilla pide un record

```java
@Test
void shouldHaveEightNeighboursAroundIt() {
    assertThat(new Position(5, 5).neighbours()).containsExactlyInAnyOrder(
        new Position(4, 4), new Position(4, 5), new Position(4, 6),
        new Position(5, 4), new Position(5, 6),
        new Position(6, 4), new Position(6, 5), new Position(6, 6));
}
```

El test compara posiciones con `containsExactlyInAnyOrder`, que usa `equals`. Dos posiciones con la misma fila y columna tienen que ser iguales: eso es un **valor**, y en Java un valor es un `record`, que genera `equals`, `hashCode` y `toString`.

```java
public record Position(int row, int column) {
    public Set<Position> neighbours() { ... }
}
```

### 3. El tablero pide una clase inmutable

```java
assertThat(new Generation(block).next().aliveCells()).isEqualTo(block);
```

`next()` devuelve **otra** generación en lugar de cambiar la actual. El test lo decidió al encadenar `next().next()` en el parpadeador. La clase guarda una copia con `Set.copyOf` para que nadie pueda cambiarla desde fuera.

| El test pide | Tipo | Por qué |
|---|---|---|
| Un estado entre unos pocos posibles | `enum` | Los valores están cerrados y cada uno puede tener comportamiento |
| Un dato que se compara por su contenido | `record` | `equals` y `hashCode` generados, inmutable |
| Algo con reglas y datos internos que no se exponen | `final class` | Controlas qué se ve y cómo se construye |
| Algo que habla con el exterior | `interface` | [Teoría 03](03-outside-in-con-mockito.md) |

---

## Elegir el siguiente test

El orden de los tests decide si el código crece en pasos pequeños o a saltos. En el lab 01 el orden es `1`, `2`, `3`, `4`, `6`, `8`, `9`: cada número obliga a generalizar una sola cosa.

| Test | Lo que obliga a cambiar |
|---|---|
| `of(2)` = `[2]` | Devolver algo cuando `n > 1` |
| `of(3)` = `[3]` | Devolver `n` en lugar de `2` |
| `of(4)` = `[2, 2]` | Dividir por 2 una vez (`if`) |
| `of(8)` = `[2, 2, 2]` | Dividir por 2 varias veces (`if` → `while`) |
| `of(9)` = `[3, 3]` | Probar otros divisores (un segundo `while`) |

Dos pistas para elegir:

- **El caso más simple que el código todavía no resuelve.** Si dudas entre dos, el que obliga a cambiar menos líneas.
- **Triangula.** Si un Green devuelve una constante (`return DEAD`), el siguiente test usa otro valor para que la constante deje de servir.

---

## Refactorizar con la suite como red de seguridad

Refactorizar es cambiar la estructura del código sin cambiar lo que hace. La suite es la que te dice que no cambió.

1. **Solo en verde.** Si la suite está en rojo, primero arregla el Red; no mezcles las dos cosas.
2. **Un cambio, una ejecución.** Extrae un método, ejecuta; renombra, ejecuta. Con `Shift+F10` en IntelliJ cuesta un segundo.
3. **Si la suite se pone en rojo, deshaz.** `Ctrl+Z` o `git restore`, y prueba un paso más pequeño.
4. **No añadas comportamiento.** Si mientras refactorizas ves un caso que falta, apúntalo como el siguiente test.

En el lab 02, después del último Green, `next()` tiene la regla, el conteo de vecinas y el estado de cada casilla en la misma expresión. El refactor saca dos métodos privados:

```java
private Cell cellAt(Position position) {
    return alive.contains(position) ? Cell.ALIVE : Cell.DEAD;
}

private int liveNeighboursOf(Position position) {
    return (int) position.neighbours().stream().filter(alive::contains).count();
}
```

La suite sigue en `Tests run: 24, Failures: 0`. Para comprobar que la red funciona, el lab mete un error a propósito durante el refactor (`== 3` cambiado por `>= 3` en la regla de las células muertas):

```bash
mvn test
# [ERROR]   CellTest.shouldStayDeadWithAnyOtherNumberOfLiveNeighbours:37
# [ERROR] Tests run: 24, Failures: 5, Errors: 0, Skipped: 0
```

### Los refactors de IntelliJ

Los atajos de la [teoría 01](01-ciclo-tdd-en-java.md#atajos-de-intellij-idea-para-tdd) hacen el cambio en todos los sitios a la vez y sin errores de escritura: `Ctrl+Alt+M` (extraer método), `Ctrl+Alt+V` (variable), `Ctrl+Alt+C` (constante), `Shift+F6` (renombrar). El catálogo completo con el nombre de cada refactor está en [refactoring.com](https://refactoring.com/catalog/).

---

## El valor esperado no sale del código

En el proyecto, un test del recargo máximo se escribió así:

```java
assertThat(ReturnFees.lateFee(31)).isEqualTo(ReturnFees.MAX_LATE_FEE);
```

Si alguien cambia `MAX_LATE_FEE = 1500` por `1550`, el test sigue en verde: compara el código consigo mismo. Comprobado con la suite del proyecto: con esa línea, el cambio pasa; con la línea corregida, falla.

```java
assertThat(ReturnFees.lateFee(31)).isEqualTo(1_500);
```

El valor esperado se escribe en el test, tal como lo diría la regla de negocio: "el recargo nunca pasa de 1.500".

---

## ✅ Resumen

- Cada test escrito antes del código decide un tipo: `enum`, `record`, `final class` o `interface`.
- El siguiente test es el caso más simple que el código todavía no resuelve.
- Refactoriza solo en verde, en pasos pequeños, ejecutando la suite después de cada uno.
- El valor esperado se escribe en el test, nunca se lee de una constante del código.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 01 - El ciclo Red → Green → Refactor en Java](01-ciclo-tdd-en-java.md) | [Teoría — 03 - Outside-in TDD con Mockito](03-outside-in-con-mockito.md) → |

[↑ Semana 27](../README.md)
