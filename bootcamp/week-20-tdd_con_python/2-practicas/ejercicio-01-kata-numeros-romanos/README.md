# Ejercicio 01 - Kata de Números Romanos

## Objetivo

Construir `to_roman(number)` con TDD en micro-ciclos: **test (Red) → ejecutar y leer el fallo → código mínimo (Green) → refactor**. Al final, una propiedad con `hypothesis` y `mypy --strict` protegen el diseño que emergió de los tests.

## Tiempo estimado

90 minutos.

## Preparación

```bash
cd starter
uv sync
uv run pytest -q
```

`starter/roman.py` solo tiene la firma y `raise NotImplementedError`. Los tests están comentados por PASO en `starter/test_roman.py`. El código de cada Green **lo escribes tú** en `roman.py`; este README te da el mínimo por si te atascas.

> Regla: después de cada Red ejecuta `uv run pytest -q --tb=line` y comprueba que falla **por el motivo esperado**. Si un Red pasa en verde, el test no está probando nada nuevo.

## Ciclos

### Ciclo 1 - PASO 1: `to_roman(1) == "I"`

Red:

```text
E   NotImplementedError
```

Green mínimo: `return "I"`.

### Ciclo 2 - PASO 2: `to_roman(3) == "III"`

Red:

```text
E   AssertionError: assert 'I' == 'III'
```

Green mínimo: `return "I" * number`.

### Ciclo 3 - PASO 3: `to_roman(5) == "V"`

Red:

```text
E   AssertionError: assert 'IIIII' == 'V'
```

Green: el patrón pide una tabla de símbolos que se recorre de mayor a menor.

```python
SYMBOLS = ((5, "V"), (1, "I"))


def to_roman(number: int) -> str:
    result = ""
    for value, symbol in SYMBOLS:
        while number >= value:
            result += symbol
            number -= value
    return result
```

### Ciclo 4 - PASO 4: `to_roman(4) == "IV"`

Red:

```text
E   AssertionError: assert 'IIII' == 'IV'
```

Green: la notación sustractiva es solo otra entrada de la tabla: `SYMBOLS = ((5, "V"), (4, "IV"), (1, "I"))`. El diseño de la tabla ya absorbe el caso sin un `if`.

### Ciclo 5 - PASO 5: el resto de símbolos

El PASO 5 es un `parametrize` con 8 casos (`9`, `14`, `40`, `90`, `400`, `1994`, `2026`, `3999`). Red:

```text
E   AssertionError: assert 'VIV' == 'IX'
E   AssertionError: assert 'VVIV' == 'XIV'
E   AssertionError: assert 'VVVVVVVV' == 'XL'
...
8 failed, 4 passed
```

Green: completa la tabla con los 13 símbolos, de `(1000, "M")` a `(1, "I")`, incluidos `CM`, `CD`, `XC`, `XL` e `IX`. `12 passed`.

### Ciclo 6 - PASO 6: fuera de rango

Los romanos clásicos van de 1 a 3999. Red (para `0`, `-1` y `4000`):

```text
E   Failed: DID NOT RAISE ValueError
```

Green: una guarda al inicio.

```python
if not 1 <= number <= 3999:
    raise ValueError(f"number must be between 1 and 3999, got {number}")
```

`15 passed`.

### Refactor

Con la suite en verde, mejora el diseño sin cambiar el comportamiento:

1. Renombra `SYMBOLS` a `ROMAN_SYMBOLS` y anota su tipo: `tuple[tuple[int, str], ...]`.
2. Sustituye el `while` por `count, number = divmod(number, value)` y `result += symbol * count`.

Ejecuta `uv run pytest -q` y `uv run mypy .` (`[tool.mypy] strict = true` en `pyproject.toml`). Las dos deben terminar en verde: `15 passed` y `Success: no issues found`.

### PASO 7: una propiedad que protege el diseño

Descomenta el PASO 7. `@given(st.integers(min_value=1, max_value=3999))` genera cientos de números y verifica dos propiedades: solo se usan símbolos romanos y ningún símbolo se repite 4 veces. `16 passed`.

Para ver qué protege, borra temporalmente `(4, "IV")` de la tabla y ejecuta `uv run pytest -q --tb=short -k never`:

```text
E   AssertionError: assert ['I'] == []
...
E   Failing test case: test_to_roman_never_repeats_a_symbol_four_times_for_any_valid_number(
E       number=464,
```

El número cambia en cada ejecución (en las pruebas del bootcamp salieron 24, 94, 164, 464 y 544), pero siempre termina en 4. Hypothesis reduce el ejemplo (shrinking), aunque no garantiza encontrar el mínimo. Restaura `(4, "IV")`.

Compara tu resultado con `solution/roman.py`.
