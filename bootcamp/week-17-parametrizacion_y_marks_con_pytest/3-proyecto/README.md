# Proyecto Semanal - Suite Python Segmentada por Riesgo

## Objetivo

Construir una suite Python con `pytest` usando parametrización y marks para mejorar cobertura, legibilidad y trazabilidad.

## Contexto

Este proyecto es el entregable obligatorio de la semana. Debes adaptar el starter a tu dominio asignado por el instructor.

## Requisitos

1. Implementar al menos 8 casos parametrizados útiles, con `ids` o `pytest.param(..., id=...)` legibles.
2. Registrar los marks `smoke`, `regression` y `slow` en `[tool.pytest]` de `starter/pyproject.toml`, con `strict = true`.
3. Usar al menos un mark integrado (`skipif` o `xfail(strict=True)`) con un `reason` justificado.
4. Ejecutar y documentar al menos dos comandos de selección con `-m` y uno con `-k`.
5. Incluir casos de comportamiento, borde y error.
6. Mantener el patrón AAA y nombres `test_[context]_[expected]_when_[condition]`.

## Estructura

- `starter/`: plantilla con TODOs para implementar.
- `solution/`: referencia local del instructor (no versionada).

## Criterios mínimos

- Mínimo 8 casos parametrizados efectivos.
- Mínimo 3 casos de error o validación.
- Marks registrados y consistentes con la estrategia de ejecución.
- Evidencia de corridas segmentadas con `-m` y `-k`.

## Ejecución sugerida

```bash
cd starter
uv sync
uv run pytest -v
uv run pytest -m smoke
uv run pytest -m "not slow"
```
