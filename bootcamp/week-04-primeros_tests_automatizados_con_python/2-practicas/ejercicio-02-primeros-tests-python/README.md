# Ejercicio 02 — Primeros Tests Unitarios en Python

> **Semana 04 · Prácticas · Ejercicio 02** | Duración estimada: 2 h

---

## Objetivo

Construir una suite inicial con pytest para funciones puras aplicando:

- Nombres descriptivos (`test_[contexto]_[resultado]_when_[condicion]`)
- Patrón AAA
- Assertions claras
- Manejo de excepciones con `pytest.raises`

---

## Instrucciones

### Paso 1 — Preparar el entorno

```bash
cd starter
uv sync
uv run pytest -v
```

Todos los tests están comentados, así que pytest informa `no tests ran`. Revisa `src/user_utils.py` para conocer las funciones que vas a probar.

### Paso 2 — PASO 1: tests de `is_adult`

Abre `starter/tests/test_user_utils.py` y descomenta el bloque `PASO 1`. Cubre el límite de edad: 18 (adulto) y 17 (no adulto).

```bash
uv run pytest -v
```

### Paso 3 — PASO 2: tests de `calculate_discount`

Descomenta el bloque `PASO 2`: un caso válido con `==` y un porcentaje inválido verificado con `pytest.raises(ValueError, match="Invalid percent")`. Observa que en los tests de excepción Act y Assert van juntos dentro del `with`.

### Paso 4 — PASO 3: tests de `is_valid_email`

Descomenta el bloque `PASO 3`: un email válido y uno inválido.

```bash
uv run pytest -v
uv run pytest -k calculate_discount
```

Deben pasar los 6 tests; con `-k calculate_discount` solo se ejecutan los 2 de esa función.

### Paso 5 — Reto opcional

Añade por tu cuenta un test más por función (por ejemplo, `calculate_discount` con `percent = 0` o un email sin punto) siguiendo el mismo patrón AAA y de nombres.

---

## Resultado esperado

- 6 tests en verde (más los del reto, si lo haces)
- Happy path + casos inválidos
- AAA visible en cada test
