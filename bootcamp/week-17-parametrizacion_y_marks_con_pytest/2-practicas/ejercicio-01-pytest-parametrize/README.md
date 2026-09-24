# Ejercicio 01 - Parametrize con Casos de Negocio

## Objetivo

Aplicar `@pytest.mark.parametrize` para cubrir casos de comportamiento, borde y error sin duplicar tests.

## Tiempo estimado

90 minutos.

## Paso a paso

Revisa `starter/shipping_rules.py`: el envío cuesta 9.99 por debajo de 50, 4.99 desde 50 y es gratis desde 100.

### Paso 1: Caso base parametrizado

Abre `starter/test_shipping_rules.py` y descomenta el PASO 1: un valor dentro de cada tramo, con `ids` legibles.

### Paso 2: Casos borde de ambos límites

Descomenta el PASO 2. Cubre los dos límites del cálculo, justo por debajo y justo en el valor: 49.99/50 (tramo intermedio) y 99.99/100 (envío gratis).

### Paso 3: Parametrizar el error esperado

Descomenta el PASO 3 para entradas negativas con `pytest.raises`.

### Paso 4: Seleccionar casos con `-k`

Ejecuta toda la suite y después solo los casos de frontera. `-k` filtra por nombre del test, incluidos los `ids` entre corchetes:

```bash
cd starter
uv sync
uv run pytest -v
uv run pytest -v -k "threshold"
uv run pytest -v -k "threshold and not free"
```

Resultado esperado: 8 tests en total; `-k "threshold"` selecciona 4 y `-k "threshold and not free"` selecciona 2 (`below-mid-tier-threshold` y `mid-tier-threshold`).

### Paso 5: Revisar solución

Compara con `solution/test_shipping_rules.py`.
