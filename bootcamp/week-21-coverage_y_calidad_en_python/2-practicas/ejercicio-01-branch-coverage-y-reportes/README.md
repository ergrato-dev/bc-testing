# Ejercicio 01 - Branch Coverage, Exclusiones y Umbral

## Objetivo

Configurar `pytest-cov` paso a paso y ver cómo cambia el reporte: line coverage que parece suficiente, branch coverage que revela los casos que faltan, exclusión del código sin lógica y un umbral que hace fallar la suite.

## Tiempo estimado

75 minutos.

## Preparación

```bash
cd starter
uv sync
```

`starter/src/tickets/pricing.py` calcula el precio de una entrada: 20 general, 10 para menores de 12 y 5 de descuento para socios. Al final tiene un bloque `if __name__ == "__main__":` para probarla a mano. `starter/pyproject.toml` todavía no tiene configuración de coverage.

## Paso a paso

### Paso 1: line coverage

Descomenta el PASO 1 de `starter/tests/test_pricing.py` (dos tests) y ejecuta:

```bash
uv run pytest --cov=tickets --cov-report=term-missing
```

```text
Name                      Stmts   Miss  Cover   Missing
-------------------------------------------------------
src/tickets/__init__.py       0      0   100%
src/tickets/pricing.py       11      1    91%   14
-------------------------------------------------------
TOTAL                        11      1    91%
```

Toda la lógica "está cubierta": solo falta la línea 14, el `print` del bloque `__main__`.

### Paso 2: branch coverage

Añade al final de `pyproject.toml`:

```toml
[tool.coverage.run]
source = ["src"]
branch = true
```

Con `source` configurado ya no hace falta indicar el paquete. Ejecuta `uv run pytest --cov --cov-report=term-missing`:

```text
Name                      Stmts   Miss Branch BrPart  Cover   Missing
---------------------------------------------------------------------
src/tickets/__init__.py       0      0      0      0   100%
src/tickets/pricing.py       11      1      8      3    79%   6->8, 8->10, 14
---------------------------------------------------------------------
TOTAL                        11      1      8      3    79%
```

Interpreta `6->8` y `8->10` con el código abierto: ¿qué edad y qué tipo de cliente no se probaron nunca? Anota la respuesta antes de seguir.

### Paso 3: excluir el código sin lógica

Añade:

```toml
[tool.coverage.report]
show_missing = true
exclude_also = ['if __name__ == "__main__":']
```

`uv run pytest --cov`:

```text
src/tickets/pricing.py        9      0      6      2    87%   6->8, 8->10
```

El bloque `__main__` desaparece del cálculo (`Stmts` baja de 11 a 9). Solo quedan las dos ramas de la lógica.

### Paso 4: un umbral que falla

Añade `fail_under = 90` a `[tool.coverage.report]` y ejecuta:

```bash
uv run pytest --cov
echo $?
```

```text
FAIL Required test coverage of 90.0% not reached. Total coverage: 86.67%
2 passed in 0.01s
1
```

Los dos tests pasan, pero el código de salida es 1: en CI el job quedaría en rojo.

### Paso 5: las ramas que faltaban

Descomenta el PASO 2 del test (un adulto no socio y un niño no socio) y ejecuta `uv run pytest --cov`:

```text
src/tickets/pricing.py        9      0      6      0   100%
Required test coverage of 90.0% reached. Total coverage: 100.00%
4 passed
```

### Paso 6: reportes HTML y XML

```bash
uv run pytest --cov --cov-report=html --cov-report=xml
```

```text
Coverage HTML written to dir htmlcov
Coverage XML written to file coverage.xml
```

Abre `htmlcov/index.html` en el navegador. Para verlo en rojo, vuelve a comentar el PASO 2, regenera el HTML y busca las ramas parciales marcadas en amarillo en `pricing.py`. `coverage.xml` es el archivo que leen SonarQube y los servicios de CI.

Compara tu `pyproject.toml` con `solution/pyproject.toml`.
