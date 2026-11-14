# Lab 02 - Coverage combinado y reporte

## Objetivo

Medir con una sola cifra la cobertura de pytest y de Behave, hacer que la suite falle por debajo del 85 % y escribir el reporte de testing con los datos de esa ejecución.

## Tiempo estimado

75 minutos.

## Preparación

El código es la solución del [lab 01](../lab-01-suite-por-capas/README.md): las capas ya están marcadas. Docker debe estar en marcha.

```bash
cd starter
uv sync
```

Qué cambia respecto al lab 01:

- **`pyproject.toml`:** añade `coverage==7.17.3` al grupo `dev`. La configuración de coverage está comentada (PASO 1).
- **`testing-report-python.md`:** la plantilla del reporte, con `TODO` en cada sección.

En la [semana 21](../../../week-21-coverage_y_calidad_en_python/README.md) mediste con `pytest --cov`. Aquí usas `coverage` directamente, porque también tiene que medir Behave, que no es un plugin de pytest.

## Paso a paso

### Paso 1: medir sin configurar

```bash
uv run coverage run -m pytest -q
# 7 passed in 4.65s
uv run coverage report
# Name                                       Stmts   Miss  Cover
# --------------------------------------------------------------
# library/__init__.py                            0      0   100%
# library/catalog_client.py                     14      0   100%
# library/fees.py                                9      1    89%
# library/models.py                             10      0   100%
# library/repository.py                         20      0   100%
# tests/api/test_catalog_client.py              20      0   100%
# tests/conftest.py                              6      0   100%
# tests/repository/conftest.py                  21      0   100%
# tests/repository/test_book_repository.py      13      0   100%
# tests/unit/test_fees.py                        7      0   100%
# --------------------------------------------------------------
# TOTAL                                        120      1    99%
```

- **`coverage run -m pytest`:** ejecuta pytest como `python -m pytest`, pero midiendo qué líneas se ejecutan. Guarda el resultado en `.coverage`.
- **`coverage report`:** lee `.coverage` e imprime la tabla.

El 99 % engaña: la mitad de las líneas medidas son de los tests, y un test siempre se ejecuta entero.

### Paso 2: qué medir y cuándo fallar (PASO 1)

En `pyproject.toml`, descomenta las dos secciones de coverage:

| Opción | Qué | Para qué |
|---|---|---|
| `source = ["library"]` | Mide solo el paquete de la aplicación | La cifra habla del código, no de los tests |
| `branch = true` | Mide también las ramas de cada `if` | Un `if` sin `else` probado solo por un lado cuenta como parcial |
| `show_missing = true` | Añade la columna `Missing` | Ves qué líneas faltan sin abrir el HTML |
| `fail_under = 85` | `coverage report` sale con código 2 por debajo del 85 % | El umbral rompe la verificación en lugar de quedar como un número |

Vuelve a medir:

```bash
uv run coverage run -m pytest -q
# 7 passed in 4.52s
uv run coverage report
# Name                        Stmts   Miss Branch BrPart  Cover   Missing
# -----------------------------------------------------------------------
# library/__init__.py             0      0      0      0   100%
# library/catalog_client.py      14      0      2      0   100%
# library/fees.py                 9      1      4      1    85%   10
# library/models.py              10      0      0      0   100%
# library/repository.py          20      0      0      0   100%
# -----------------------------------------------------------------------
# TOTAL                          53      1      6      1    97%
```

La línea 10 de `fees.py` es `return MAX_FEE`, el tope de la multa. Ningún test de pytest la prueba.

### Paso 3: añadir Behave a la misma medición

El tope sí lo prueba `features/multas.feature`, con 20 y 30 días de retraso:

```bash
uv run coverage run --append -m behave
# ...
uv run coverage report
# Name                        Stmts   Miss Branch BrPart  Cover   Missing
# -----------------------------------------------------------------------
# library/__init__.py             0      0      0      0   100%
# library/catalog_client.py      14      0      2      0   100%
# library/fees.py                 9      0      4      0   100%
# library/models.py              10      0      0      0   100%
# library/repository.py          20      0      0      0   100%
# -----------------------------------------------------------------------
# TOTAL                          53      0      6      0   100%
```

`--append` suma la ejecución de Behave al `.coverage` que dejó pytest. Sin `--append`, `coverage run` empieza de cero y la tabla solo mostraría lo que cubre Behave.

### Paso 4: ver fallar el umbral

Mide sin la capa de repositorio, como si no tuvieras Docker:

```bash
uv run coverage run -m pytest -q -m "not repository"
# 5 passed, 2 deselected in 0.39s
uv run coverage report
# ...
# library/repository.py          20     10      0      0    50%   14, 17-23, 26-27
# -----------------------------------------------------------------------
# TOTAL                          53     11      6      1    80%
# Coverage failure: total of 80 is less than fail-under=85
echo $?
# 2
```

El umbral pide la suite completa. Por eso la verificación del proyecto ejecuta todas las capas.

### Paso 5: los datos del reporte

Vuelve a medir todo y saca la tabla en Markdown:

```bash
uv run coverage run -m pytest -q
uv run coverage run --append -m behave
uv run coverage report --format=markdown
# | Name                       |    Stmts |     Miss |   Branch |   BrPart |    Cover |   Missing |
# |--------------------------- | -------: | -------: | -------: | -------: | -------: | --------: |
# | library/\_\_init\_\_.py    |        0 |        0 |        0 |        0 |     100% |           |
# ...
# | **TOTAL**                  |   **53** |    **0** |    **6** |    **0** | **100%** |           |
```

Y los tests más lentos:

```bash
uv run pytest -q --durations=3
# .......                                                                  [100%]
# ============================= slowest 3 durations ==============================
# 2.86s setup    tests/repository/test_book_repository.py::test_list_available_skips_books_without_stock
# 0.41s teardown tests/unit/test_fees.py::test_late_fee_charges_per_day_late
# 0.01s call     tests/repository/test_book_repository.py::test_list_available_skips_books_without_stock
# 7 passed in 3.56s
```

`--durations=3` separa las fases: *setup* (fixtures antes del test), *call* (el test) y *teardown* (limpieza). El *setup* del primer test de repositorio incluye arrancar el contenedor, y el *teardown* del último test incluye pararlo, porque la fixture `engine` es de sesión.

### Paso 6: escribir el reporte

Rellena `testing-report-python.md` sustituyendo cada `TODO`:

1. **Suite por capas:** una fila por capa con los tests y el tiempo del Paso 4 del lab 01.
2. **Cobertura:** la tabla del Paso 5 y una frase sobre qué capa cubre qué.
3. **Tests más lentos:** la salida de `--durations=3`, explicada.
4. **Riesgos y huecos:** lo que la suite no prueba. Por ejemplo, el cliente no tiene test de timeout.
5. **Próximos pasos:** los tests que añadirías primero.

Compáralo con `solution/testing-report-python.md`.

## Comprobación

```bash
cd ../solution
uv sync
uv run coverage run -m pytest -q
uv run coverage run --append -m behave
uv run coverage report --fail-under=85
# ...
# TOTAL                          53      0      6      0   100%
```

## Preguntas de cierre

1. ¿Qué cifra darías en el reporte si solo midieras pytest? ¿Qué riesgo tiene esa cifra?
2. El tope de la multa solo lo prueba Behave. ¿Lo dejarías así o añadirías un test unitario? ¿Por qué?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Una suite por capas](../lab-01-suite-por-capas/README.md) | [Proyecto — Proyecto Integrador - Suite Python completa del dominio](../../3-proyecto/README.md) → |

[↑ Semana 24](../../README.md)
