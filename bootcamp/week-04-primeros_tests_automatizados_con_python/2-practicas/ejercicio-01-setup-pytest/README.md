# Ejercicio 01 — Setup y Primera Ejecución con pytest

> **Semana 04 · Prácticas · Ejercicio 01** | Duración estimada: 1.5 h

---

## Objetivo

Configurar pytest por primera vez y recorrer el ciclo mínimo:

1. Test en rojo (falla)
2. Corrección mínima
3. Test en verde (pasa)

---

## Instrucciones

### Paso 1 — Revisar estructura

Abre la carpeta `starter/`. Encontrarás:

- `src/math_utils.py`
- `tests/test_math_utils.py`
- `pyproject.toml` (dependencias y configuración `[tool.pytest]`)

### Paso 2 — Instalar dependencias

Desde la terminal:

```bash
cd starter
uv sync
```

`uv sync` crea `.venv` e instala pytest 9.1.1 según `pyproject.toml`.

### Paso 3 — Ejecutar tests (rojo)

```bash
uv run pytest -v
```

Verás un test fallando por diseño: `test_add_returns_five_when_inputs_are_two_and_three` está activo en `tests/test_math_utils.py`, pero `add` tiene un bug. Lee el mensaje de error (`assert -1 == 5`): es un `FAILED` por assertion.

### Paso 4 — PASO 1: corregir la función (verde)

Abre `starter/src/math_utils.py` y sigue el bloque `PASO 1`: sustituye la resta por la suma. Ejecuta `uv run pytest -v` y comprueba que el test pasa a verde.

### Paso 5 — PASO 2 y PASO 3: añadir tests de `is_even`

Abre `starter/tests/test_math_utils.py` y descomenta los bloques `PASO 2` (número par) y `PASO 3` (número impar). Observa la estructura AAA de cada test.

### Paso 6 — Ejecutar de nuevo hasta ver todo en verde

```bash
uv run pytest -v
uv run pytest -k is_even
```

Deben pasar los 3 tests; con `-k is_even` solo se ejecutan los 2 de `is_even`.

---

## Resultado esperado

- Entorno pytest funcional con `uv`
- Comprensión de un fallo de assertion
- Corrección mínima para pasar los tests
