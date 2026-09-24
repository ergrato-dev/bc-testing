# Ejercicio 01 - Fixtures con yield, composición y scopes

## Objetivo

Ir más allá del setup con funciones helper de la semana 04: preparar y limpiar estado con fixtures `yield`, componer fixtures y elegir el scope (`function`, `module`, `class`) observando con `--setup-show` cuándo se crea y se destruye cada una.

## Tiempo estimado

90 minutos.

## Preparación

Requiere Python 3.14 y `uv` (instalación explicada en la semana 04).

```bash
cd starter
uv sync
uv run pytest
```

Con todo comentado verás `no tests ran`. Abre `starter/test_session_registry.py` y revisa `starter/session_registry.py`: `SessionRegistry` guarda las funciones del Planetario y sus asientos libres, y debe abrirse antes de usarse y cerrarse al terminar.

## Paso a paso

### Paso 1: Fixture con `yield` (setup + teardown)

Descomenta el PASO 1. Todo lo que va antes de `yield` es setup; lo que va después es teardown y se ejecuta aunque el test falle. Ejecuta con `--setup-show` para ver el orden real:

```bash
uv run pytest --setup-show
```

Verás `SETUP F registry`, el test y `TEARDOWN F registry`.

### Paso 2: Una fixture que usa otra fixture

Descomenta el PASO 2. `registry_with_show` recibe `registry` como parámetro: pytest resuelve la cadena y aplica el teardown de `registry` cuando termina el test.

### Paso 3: Aislamiento del scope `function`

Descomenta el PASO 3. El test del PASO 2 reservó 3 asientos, pero este test vuelve a ver 10: cada test recibe su propia instancia de las fixtures de scope `function` (el valor por defecto).

### Paso 4: Rutas de error con fixtures

Descomenta el PASO 4. Los errores esperados usan `pytest.raises` igual que en la semana 04; lo nuevo es que el escenario llega preparado desde las fixtures.

### Paso 5: Fixture de scope `module`

Descomenta el PASO 5. `show_catalog` simula una carga costosa y de solo lectura, así que se crea una sola vez por archivo. Ejecuta de nuevo:

```bash
uv run pytest --setup-show
```

Comprueba que `SETUP M show_catalog` aparece una sola vez, mientras que `SETUP F registry` aparece en cada test.

### Paso 6: Fixture de scope `class`

Descomenta el PASO 6. `largest_show` (scope `class`) usa `show_catalog` (scope `module`): una fixture puede depender de otras de scope igual o más amplio. Si intentas lo contrario (por ejemplo, que `show_catalog` pida `registry`), pytest responde con un `ScopeMismatch` y marca el test como **error**, no como fallo.

### Paso 7: Comparar con la solución

Compara con `solution/test_session_registry.py` y ejecuta `uv run pytest --setup-show` en ambas carpetas: el orden de SETUP y TEARDOWN debe coincidir.

## Comando sugerido

```bash
uv run pytest -v --setup-show
```
