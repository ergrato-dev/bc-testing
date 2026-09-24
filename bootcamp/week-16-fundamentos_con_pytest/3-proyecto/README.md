# Proyecto Semanal - Suite Python con fixtures para el dominio asignado

## Objetivo

Construir la base de la suite pytest de tu dominio para toda la etapa 2, organizando el setup con fixtures `yield`, un `conftest.py` compartido y fixtures integradas (`tmp_path`, `monkeypatch`).

## Contexto

Este proyecto es el entregable obligatorio de la semana. El starter trae una implementación genérica de items (`item_service.py`): `create_item` valida datos y `ItemStore` guarda items en un archivo JSON, debe abrirse antes de usarse y cerrarse al terminar, y lee su límite de la variable de entorno `ITEM_LIMIT`. Adáptala a tu dominio asignado por el instructor (por ejemplo, Museo: piezas; Planetario: funciones; Acuario: especies) y escribe la suite decidiendo tú los casos de prueba.

## Preparación

Requiere Python 3.14 y `uv` (instalación explicada en la semana 04).

```bash
cd starter
uv sync
uv run pytest
```

Con el starter sin tocar verás `no tests ran`. Los archivos de `starter/tests/` contienen TODOs con los requisitos que debes cubrir, no los tests ni sus nombres.

## Requisitos

1. `conftest.py` con fixtures usadas desde **los dos** archivos de test (`test_create_item.py` y `test_item_store.py`).
2. Al menos una fixture con `yield` que prepare el almacén y lo cierre en el teardown.
3. Uso de `tmp_path` para que ningún test escriba en archivos reales del proyecto.
4. Uso de `monkeypatch` para controlar `ITEM_LIMIT` (u otra variable o atributo de tu dominio).
5. Al menos 8 tests de comportamiento, de los cuales al menos 2 validan errores con `pytest.raises` y `match`.
6. Patrón AAA con comentarios y nombres `test_[context]_[expected]_when_[condition]`.
7. `autouse` solo si lo justificas en el docstring de la fixture.

## Estructura

- `starter/`: implementación genérica y archivos de test con TODOs.
- `solution/`: referencia local del instructor (no versionada).

## Criterios mínimos

- Mínimo 8 tests en total y 2 de error.
- `conftest.py` compartido por los dos archivos de test.
- Al menos 1 fixture con `yield` y teardown.
- `tmp_path` y `monkeypatch` usados en al menos un test cada uno.
- `uv run pytest` en verde y `uv run pytest --setup-show` legible: cada fixture con el scope que corresponde.

## Ejecución sugerida

```bash
uv run pytest -v
uv run pytest --setup-show
uv run pytest --fixtures tests
```
