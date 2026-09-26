# Ejercicio 02 - Lo que los Ejemplos no Ven: mypy y hypothesis

## Objetivo

Partir de una función que **pasa todos sus tests de ejemplo** y encontrar sus bugs con dos herramientas que complementan TDD: `mypy --strict` (errores de tipo) y `hypothesis` (propiedades que deben cumplirse para cualquier entrada).

## Tiempo estimado

90 minutos.

## Preparación

```bash
cd starter
uv sync
```

`starter/bill.py` reparte una cuenta en centavos entre varias personas:

```python
def split_bill(total_cents: int, people: int) -> list[int]:
    share = total_cents / people
    return [share] * people
```

## Paso a paso

### Paso 1: Los ejemplos pasan

Descomenta el bloque PASO 1 de `starter/test_bill.py` (tres casos con `parametrize`) y ejecuta `uv run pytest -q`:

```text
3 passed
```

Los tres ejemplos dividen exacto. `[250.0, 250.0, 250.0, 250.0] == [250, 250, 250, 250]` es `True` en Python, así que el test no ve que la función devuelve `float`.

### Paso 2: mypy encuentra el primer bug

`pyproject.toml` activa `[tool.mypy] strict = true`. Ejecuta `uv run mypy .`:

```text
bill.py:4: error: List item 0 has incompatible type "float"; expected "int"  [list-item]
Found 1 error in 1 file (checked 2 source files)
```

La firma promete `list[int]` y `/` produce `float`. Cambia `/` por `//` y vuelve a ejecutar mypy hasta ver `Success: no issues found`. Los tests de ejemplo siguen en verde.

### Paso 3: la propiedad de la suma

Descomenta el bloque PASO 2 del test. `@given` genera cuentas entre 0 y 1 000 000 centavos y entre 1 y 50 personas, y verifica que la suma de las partes es el total. `uv run pytest -q --tb=short`:

```text
E   assert 0 == 1
E    +  where 0 = sum([0, 0])
E   Failing test case: test_split_bill_shares_add_up_to_total_for_any_bill(
E       total_cents=1,
E       people=2,
E   )
```

Hypothesis encontró una cuenta que no se divide exacto y la **redujo** (shrinking) al caso más simple: 1 centavo entre 2 personas. `//` descarta el resto: se pierde dinero.

Un arreglo tentador es sumar el resto a la última persona:

```python
share, remainder = divmod(total_cents, people)
shares = [share] * people
shares[-1] += remainder
return shares
```

Con eso el PASO 2 pasa. Déjalo así y sigue.

### Paso 4: la propiedad del reparto justo

Descomenta el bloque PASO 3: cada persona tiene una parte y ninguna paga más de un centavo que otra. `@example(total_cents=0, people=3)` fija un caso límite que se ejecuta siempre, además de los generados. Falla:

```text
E   assert (2 - 0) <= 1
E    +  where 2 = max([0, 0, 2])
E    +  and   0 = min([0, 0, 2])
E   Failing test case: test_split_bill_gives_one_share_per_person_differing_by_at_most_one_cent(
E       total_cents=2,
E       people=3,
E   )
```

El arreglo anterior cumplía la suma pero no era justo. Reparte el resto de a un centavo:

```python
share, remainder = divmod(total_cents, people)
return [share + 1] * remainder + [share] * (people - remainder)
```

### Paso 5: entrada inválida

Descomenta el bloque PASO 4 y ejecuta. Falla con:

```text
E   ZeroDivisionError: division by zero
```

El test espera `ValueError("... at least 1 ...")`. Añade la guarda `if people < 1: raise ValueError("people must be at least 1")` antes del `divmod`.

### Paso 6: todo en verde

```bash
uv run pytest -q
uv run mypy .
```

```text
6 passed
Success: no issues found in 2 source files
```

Compara con `solution/bill.py`.

## Qué te llevas

| Herramienta | Qué encontró | Por qué los ejemplos no lo vieron |
|---|---|---|
| `mypy --strict` | `float` donde la firma promete `int` | `250.0 == 250` es `True` |
| Propiedad de la suma | Se pierde el resto | Todos los ejemplos dividían exacto |
| Propiedad del reparto justo | Una persona paga todo el resto | El arreglo cumplía la suma |
| Test de entrada inválida | `ZeroDivisionError` en vez de `ValueError` | Ningún ejemplo usaba 0 personas |
