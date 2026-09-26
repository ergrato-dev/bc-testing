# Ejercicio 02 - Mutation Testing con mutmut

## Objetivo

Comprobar que una suite con 100% de branch coverage puede dejar pasar cambios en la lógica, encontrarlos con `mutmut` y matarlos con asserts más precisos. Al final quedan solo los mutantes equivalentes, y hay que explicar por qué.

## Tiempo estimado

105 minutos.

> ⚠️ mutmut necesita `fork`: en Windows ejecuta este ejercicio dentro de WSL.

## Preparación

```bash
cd starter
uv sync
```

`starter/src/loyalty/points.py`:

```python
def points_for(amount: float, is_member: bool) -> int:
    if amount <= 0:
        return 0
    points = int(amount // 10)
    if is_member and amount >= 100:
        points *= 2
    return points
```

`starter/tests/test_points.py` tiene una suite heredada de 4 tests activos y tres bloques PASO comentados.

## Paso a paso

### Paso 1: el coverage dice que todo está bien

```bash
uv run pytest --cov
```

```text
src/loyalty/points.py         7      0      4      0   100%
4 passed
```

### Paso 2: mutmut dice otra cosa

```bash
uv run mutmut run
uv run mutmut results
```

Resumen de `mutmut run`:

```text
13/13  🎉 6 🫥 0  ⏰ 0  🤔 0  🙁 7  🔇 0  🧙 0
```

`mutmut results`:

```text
    loyalty.points.x_points_for__mutmut_1: survived
    loyalty.points.x_points_for__mutmut_2: survived
    loyalty.points.x_points_for__mutmut_6: survived
    loyalty.points.x_points_for__mutmut_7: survived
    loyalty.points.x_points_for__mutmut_9: survived
    loyalty.points.x_points_for__mutmut_10: survived
    loyalty.points.x_points_for__mutmut_13: survived
```

### Paso 3: leer los sobrevivientes

Usa `uv run mutmut show loyalty.points.x_points_for__mutmut_7` (y los demás) para ver cada cambio:

| Mutante | Cambio |
|---|---|
| 1 | `amount <= 0` → `amount < 0` |
| 2 | `amount <= 0` → `amount <= 1` |
| 6 | `amount // 10` → `amount / 10` |
| 7 | `amount // 10` → `amount // 11` |
| 9 | `amount >= 100` → `amount > 100` |
| 10 | `amount >= 100` → `amount >= 101` |
| 13 | `points *= 2` → `points *= 3` |

Antes de seguir, decide para cada uno: ¿qué test lo mataría? ¿Hay alguno que ningún test pueda matar?

### Paso 4: valores exactos (mata el 7)

Descomenta el PASO 1 del test y vuelve a ejecutar `uv run mutmut run` y `uv run mutmut results`. `assert ... > 0` no distingue `// 10` de `// 11`; `points_for(250, False) == 25` sí (con `// 11` da 22). Sobreviven: 1, 2, 6, 9, 10 y 13.

### Paso 5: el bono con su valor (mata el 13)

Descomenta el PASO 2. Comparar un socio con un no socio (`>`) acepta cualquier multiplicador mayor que 1; `== 50` solo acepta el doble. Sobreviven: 1, 2, 6, 9 y 10.

### Paso 6: la frontera (mata el 9 y el 10)

Descomenta el PASO 3: una compra de exactamente 100 y otra de 99.99. Sobreviven: 1, 2 y 6.

### Paso 7: los equivalentes

Los tres que quedan no cambian el resultado para ninguna entrada:

- **1** (`< 0`): con `amount == 0`, la guarda original devuelve 0 y el mutante calcula `int(0 // 10)`, que también es 0.
- **2** (`<= 1`): cualquier compra entre 0 y 1 da 0 puntos en las dos versiones.
- **6** (`/` en vez de `//`): para importes positivos, `int(amount / 10)` y `int(amount // 10)` truncan al mismo entero.

Documéntalos en un comentario o en tu reporte. Borra los tres tests débiles de la suite heredada que los nuevos ya reemplazan y compara con `solution/tests/test_points.py`: 6 tests, 100% de branch coverage, 10 mutantes muertos y 3 equivalentes.

## Limpieza

`mutmut` crea la carpeta `mutants/` (una copia del proyecto para mutar). No se versiona; bórrala al terminar.
