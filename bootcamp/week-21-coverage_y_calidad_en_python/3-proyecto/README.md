# Proyecto Semanal - Calidad de la Suite Python

> **🎯 ÚNICO ENTREGABLE**: Este proyecto es el **único entregable obligatorio** para aprobar la semana.

## Objetivo

Llevar una suite heredada a un nivel de calidad medible: branch coverage de al menos 90%, mutantes vivos revisados y clasificados, y complejidad dentro del umbral. Todo documentado en `quality-report-python.md`.

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

## Punto de partida

`starter/src/inventory/rules.py` tiene tres reglas de inventario ya implementadas y `starter/tests/test_rules.py` una suite débil que pasa en verde. `pyproject.toml` ya configura los tres umbrales:

```bash
cd starter
uv sync
uv run pytest --cov
uv run ruff check src
uv run mutmut run
```

Estado inicial real:

```text
src/inventory/rules.py         29     13     22      7    49%   17, 19, 25, 28, 34, 38-45
FAIL Required test coverage of 90.0% not reached. Total coverage: 49.02%
C901 `shipping_class` is too complex (7 > 5)
```

Adapta las reglas a tu dominio (o sustitúyelas por el servicio que construiste con TDD en la semana 20).

> ⚠️ En Windows ejecuta `mutmut` dentro de WSL.

## Ejemplos de adaptación por dominio

- **Museo**: estado de conservación de una pieza, coste de restauración con descuento por lote, tipo de embalaje para préstamos.
- **Planetario**: ocupación de una función, precio por grupo, tipo de proyección según sala y formato.
- **Acuario**: estado de un tanque, coste de alimento por volumen, tipo de transporte de especies.

## Requisitos

1. Branch coverage de al menos 90% (`uv run pytest --cov` termina en verde).
2. `uv run mutmut run`: todo sobreviviente revisado. Los matables se matan con tests; los equivalentes se justifican en el reporte.
3. `uv run ruff check src` en verde con `max-complexity = 5`, refactorizando `shipping_class` (o tu equivalente) con la suite en verde antes y después.
4. Asserts precisos: valores exactos, fronteras y `pytest.raises(..., match=r"^...$")` cuando verifiques mensajes.
5. `quality-report-python.md` completo (la plantilla está en `starter/`).

## Entregables

1. `starter/src/` y `starter/tests/` adaptados a tu dominio.
2. `starter/quality-report-python.md` con los números de antes y después y la clasificación de los mutantes.

> `solution/` del proyecto no se publica en el repositorio.
