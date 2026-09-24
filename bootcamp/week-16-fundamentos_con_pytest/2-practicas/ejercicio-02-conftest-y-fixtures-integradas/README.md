# Ejercicio 02 - conftest.py, autouse y fixtures integradas

## Objetivo

Compartir fixtures entre dos archivos de test con un `conftest.py` real, decidir cuándo una fixture `autouse` está justificada y usar las fixtures integradas `tmp_path`, `monkeypatch` y `capsys` sobre el catálogo de piezas de un Museo.

## Tiempo estimado

90 minutos.

## Preparación

Requiere Python 3.14 y `uv` (instalación explicada en la semana 04).

```bash
cd starter
uv sync
uv run pytest
```

Con todo comentado verás `no tests ran`. Revisa la estructura:

```text
starter/
|-- pyproject.toml          # [tool.pytest]: pythonpath, testpaths y addopts
|-- exhibit_export.py       # exporta y carga piezas en JSON
|-- museum_settings.py      # lee MUSEUM_NAME e imprime un resumen
`-- tests/
    |-- conftest.py         # fixtures compartidas (PASO 1 y 2)
    |-- test_exhibit_export.py    # PASO 3, 4 y 5
    `-- test_museum_settings.py   # PASO 6 y 7
```

`pyproject.toml` ya trae `testpaths = ["tests"]` (pytest busca solo en `tests/`) y `addopts = ["-ra"]` (resumen de todo lo que no sea `passed` al final de cada ejecución).

## Paso a paso

### Paso 1: Fixture compartida en `conftest.py`

Abre `starter/tests/conftest.py` y descomenta el PASO 1. Los tests no importan `sample_exhibits`: pytest descubre el `conftest.py` de la carpeta y ofrece sus fixtures a todos los archivos que cuelgan de ella. Compruébalo:

```bash
uv run pytest --fixtures
```

Al final de la salida aparece `sample_exhibits` con el texto de su docstring.

### Paso 2: Fixture `autouse` con criterio

Descomenta el PASO 2 en el mismo `conftest.py`. `clean_museum_env` borra `MUSEUM_NAME` antes de **cada** test sin que ningún test la pida. Está justificada porque protege a toda la suite de una variable que puede existir en la máquina de quien ejecuta los tests; no uses `autouse` para preparar datos que solo necesitan algunos tests.

### Paso 3: `tmp_path` + `monkeypatch.setattr`

Abre `starter/tests/test_exhibit_export.py` y descomenta el PASO 3. `tmp_path` entrega un directorio nuevo por test y `monkeypatch.setattr` reemplaza `exhibit_export.today` para que el nombre del archivo no dependa del día en que se ejecuta. Ambos cambios se deshacen solos al terminar el test.

### Paso 4: Ida y vuelta con archivos temporales

Descomenta el PASO 4: exporta y vuelve a cargar las piezas en `tmp_path`, sin tocar ningún archivo real del proyecto.

### Paso 5: Ruta de error con un archivo preparado

Descomenta el PASO 5. El test escribe en `tmp_path` un JSON que no es una lista y verifica el `ValueError`.

### Paso 6: Variables de entorno con `monkeypatch`

Abre `starter/tests/test_museum_settings.py` y descomenta el PASO 6. El primer test funciona gracias a la fixture `autouse` del PASO 2; el segundo fija el valor con `monkeypatch.setenv`. Pruébalo con la variable definida en tu terminal:

```bash
MUSEUM_NAME="Otro museo" uv run pytest
```

Los tests siguen en verde porque `clean_museum_env` la elimina antes de cada test.

### Paso 7: Fixture de `conftest.py` en otro archivo + `capsys`

Descomenta el PASO 7. `sample_exhibits` se usa ahora desde un segundo archivo sin duplicarla, y `capsys.readouterr()` captura lo que imprimió `print_summary`.

### Paso 8: Comparar con la solución

Compara con `solution/tests/` y ejecuta `uv run pytest --setup-show tests/test_museum_settings.py` para ver cómo `clean_museum_env` se prepara antes de cada test aunque ningún test la nombre.

## Comando sugerido

```bash
uv run pytest -v
```
