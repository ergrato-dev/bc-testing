# Ejercicio 02 - Marks y Selección de Suite

## Objetivo

Organizar pruebas con marks propios (`smoke`, `regression`, `slow`) y de pytest (`xfail`), registrarlos en `pyproject.toml` con modo estricto y ejecutar subconjuntos con `-m` y `-k`.

## Tiempo estimado

90 minutos.

## Paso a paso

Revisa `starter/order_health.py`: `is_order_valid` rechaza pedidos con total negativo o sin artículos.

### Paso 1: Registrar marks en `pyproject.toml`

Abre `starter/pyproject.toml` y descomenta el PASO 1 dentro de `[tool.pytest]`. `markers` registra los marks propios y `strict = true` convierte un mark mal escrito (por ejemplo `@pytest.mark.smok`) en un error de colección en lugar de un simple aviso.

> No crees un `pytest.ini`: si existe, pytest 9 lo usa y **ignora** la configuración de `pyproject.toml`.

### Paso 2: Mark `smoke`

Abre `starter/test_order_health.py` y descomenta el PASO 2: el camino crítico (pedido válido) queda marcado como `smoke`.

### Paso 3: Mark `regression` con `pytest.param`

Descomenta el PASO 3. Cada fila usa `pytest.param(..., id=...)` para dar un nombre legible al caso. La fila `zero-total` documenta un bug conocido (un pedido con total 0 se acepta) con `marks=pytest.mark.xfail(..., strict=True)`: el caso se reporta como `XFAIL` y, si alguien corrige el bug, pasa a `XPASS(strict)` y falla, avisando de que hay que quitar el mark.

### Paso 4: Mark `slow`

Descomenta el PASO 4. El test valida un lote de 5 millones de pedidos y lleva `slow` y `regression`: es el único que excluye el filtro `not slow`.

### Paso 5: Ejecutar subconjuntos

```bash
cd starter
uv sync
uv run pytest -v
uv run pytest -v -m smoke
uv run pytest -v -m "not slow"
uv run pytest -v -m "regression and not slow"
uv run pytest -v -k "negative or missing"
```

Resultado esperado (6 tests en total):

| Comando | Seleccionados | Resultado |
|---|---:|---|
| `pytest -v` | 6 | `5 passed, 1 xfailed` |
| `pytest -v -m smoke` | 1 | `1 passed, 5 deselected` |
| `pytest -v -m "not slow"` | 5 | `4 passed, 1 deselected, 1 xfailed` |
| `pytest -v -m "regression and not slow"` | 4 | `3 passed, 2 deselected, 1 xfailed` |
| `pytest -v -k "negative or missing"` | 2 | `2 passed, 4 deselected` |

`-m` filtra por marks; `-k` filtra por nombre del test, incluidos los ids de `pytest.param` (`negative-total`, `missing-items`).

### Paso 6: Revisar solución

Compara con `solution/pyproject.toml` y `solution/test_order_health.py`.
