# Lab 01 - Una suite por capas

## Objetivo

Ejecutar cada capa de una suite Python por separado: la lógica pura, el cliente HTTP, el repositorio contra PostgreSQL y los escenarios de Behave. Las capas se seleccionan con marcas que se ponen solas según la carpeta del test.

## Tiempo estimado

75 minutos.

## Preparación

Docker debe estar en marcha: la capa de repositorio arranca PostgreSQL con Testcontainers, como en la [semana 23](../../../week-23-testing_de_bases_de_datos_en_python/README.md).

```bash
cd starter
uv sync
uv run pytest -q
# .......                                                                  [100%]
# 7 passed in 3.64s
```

Qué hay en `starter/`, un sistema de biblioteca:

| Archivo | Capa | Qué es |
|---|---|---|
| `library/fees.py` | Unitaria | `late_fee(days_late)`: 50 por día de retraso, hasta un máximo de 1000 |
| `library/catalog_client.py` | API | Cliente async de `httpx2` que pide el título de un libro al catálogo |
| `library/models.py`, `library/repository.py` | Repositorio | Modelo `Book` y `BookRepository` con SQLAlchemy |
| `tests/unit/`, `tests/api/`, `tests/repository/` | | Un archivo de tests por capa |
| `features/multas.feature` | BDD | Un esquema del escenario con la multa |
| `pyproject.toml` | | Las marcas, comentadas (PASO 1) |
| `tests/conftest.py` | | El *hook* que marca los tests, comentado (PASO 2) |

## Paso a paso

### Paso 1: recorrer las capas

Abre un test de cada carpeta y relaciónalo con la semana donde lo viste:

- `tests/unit/test_fees.py`: `parametrize` ([semana 17](../../../week-17-parametrizacion_y_marks_con_pytest/README.md)).
- `tests/api/test_catalog_client.py`: fixture async con `MockTransport` ([semana 19](../../../week-19-testing_de_apis_con_python/README.md)).
- `tests/repository/conftest.py`: contenedor de PostgreSQL y rollback con savepoint ([semana 23](../../../week-23-testing_de_bases_de_datos_en_python/README.md)).

Intenta ejecutar solo la capa unitaria:

```bash
uv run pytest -q -m unit
# 7 deselected in 0.33s
echo $?
# 5
```

Ningún test tiene la marca `unit`, así que pytest los deselecciona todos y sale con código 5 (no se ejecutó ningún test).

### Paso 2: registrar una marca por capa (PASO 1)

En `pyproject.toml`, descomenta `addopts` y `markers`. Ahora pytest conoce las marcas `unit`, `api` y `repository`, y `--strict-markers` rechaza cualquier otra.

Comprueba qué hace `--strict-markers` con una marca mal escrita. Añade `@pytest.mark.unti` encima de `test_late_fee_charges_per_day_late` y ejecuta:

```bash
uv run pytest -q -m unit
# ...
# ERROR tests/unit/test_fees.py - Failed: 'unti' not found in `markers` configu...
# !!!!!!!!!!!!!!!!!!!! Interrupted: 1 error during collection !!!!!!!!!!!!!!!!!!!!
# 4 deselected, 1 error in 0.35s
```

Quita la marca antes de seguir.

### Paso 3: marcar cada test según su carpeta (PASO 2)

En `tests/conftest.py`, descomenta `LAYERS` y `pytest_collection_modifyitems`.

- **`pytest_collection_modifyitems(items)`:** *hook* que pytest llama después de recoger los tests. `items` es la lista de tests.
- **`item.path.parent.name`:** el nombre de la carpeta del archivo, por ejemplo `api`.
- **`item.add_marker(layer)`:** le pone la marca, igual que `@pytest.mark.api` escrito a mano.

La [teoría 01](../../1-teoria/01-la-suite-python-completa.md) explica por qué el *hook* es mejor que escribir la marca en cada test.

### Paso 4: ejecutar cada capa

```bash
uv run pytest -q -m unit
# 3 passed, 4 deselected in 0.25s
uv run pytest -q -m api
# 2 passed, 5 deselected in 0.25s
uv run pytest -q -m repository
# 2 passed, 5 deselected in 3.67s
uv run pytest -q -m "not repository"
# 5 passed, 2 deselected in 0.31s
```

Fíjate en los tiempos. Con `-m "not repository"` no se arranca el contenedor: la fixture `engine` solo se crea si algún test la pide. Es la ejecución rápida para mientras programas; la suite completa se ejecuta antes del commit.

`--collect-only` cuenta los tests de una capa sin ejecutarlos:

```bash
uv run pytest --collect-only -q -m unit | tail -1
# 3/7 tests collected (4 deselected) in 0.23s
```

### Paso 5: la capa BDD

Los escenarios de Behave no los ejecuta pytest:

```bash
uv run behave
# ...
# 1 feature passed, 0 failed, 0 skipped
# 4 scenarios passed, 0 failed, 0 skipped
# 12 steps passed, 0 failed, 0 skipped
```

El esquema del escenario tiene cuatro filas en `Ejemplos`, así que Behave cuenta cuatro escenarios.

## Comprobación

```bash
cd ../solution
uv sync
uv run pytest -q -m "not repository"
# 5 passed, 2 deselected in 0.31s
uv run pytest -q
# 7 passed in 5.13s
```

## Preguntas de cierre

1. ¿Qué capa ejecutarías en cada guardado de archivo y cuál solo antes del commit? ¿Por qué?
2. Si creas `tests/contract/test_schema.py`, ¿qué marca recibe? ¿Qué tendrías que cambiar para que reciba `contract`?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Coverage combinado y reporte](../lab-02-coverage-combinado-y-reporte/README.md) → |

[↑ Semana 24](../../README.md)
