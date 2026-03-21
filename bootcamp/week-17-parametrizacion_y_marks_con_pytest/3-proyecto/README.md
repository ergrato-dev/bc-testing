# Proyecto Semanal - Suite Python Segmentada por Riesgo

## Objetivo

Construir una suite Python con `pytest` usando parametrizacion y marks para mejorar cobertura, legibilidad y trazabilidad.

## Contexto

Este proyecto es el entregable obligatorio de la semana. Debes adaptar el starter a tu dominio asignado por el instructor.

## Requisitos

1. Implementar al menos 6 casos parametrizados utiles.
2. Definir marks minimos: `smoke`, `regression`, `slow` (si aplica).
3. Ejecutar y documentar al menos dos comandos de seleccion (`-m`).
4. Incluir casos de comportamiento, borde y error.
5. Mantener patron AAA y nombres descriptivos en `snake_case`.

## Estructura

- `starter/`: plantilla con TODOs para implementar.
- `solution/`: referencia local del instructor (no versionada).

## Criterios minimos

- Minimo 8 tests/casos efectivos en total.
- Minimo 3 casos de error o validacion.
- Marks consistentes con estrategia de ejecucion.
- Evidencia de corridas segmentadas con `pytest -m`.

## Ejecucion sugerida

```bash
pytest -m smoke
pytest -m "not slow"
```
