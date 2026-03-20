# Proyecto Semanal - Suite Combinada de Snapshots y Propiedades

## Objetivo

Construir una suite de calidad para el dominio asignado combinando:

- tests de ejemplo clasicos,
- snapshot tests con intencion,
- property-based tests con `fast-check`.

## Contexto

Este proyecto es el entregable obligatorio de la semana. Debes adaptar el starter a tu dominio asignado por el instructor.

## Requisitos

1. Incluir al menos 1 snapshot de payload estable y relevante.
2. Incluir al menos 2 propiedades invariantes de negocio.
3. Cubrir flujo feliz y validaciones de error.
4. Mantener patron AAA y nombres descriptivos.
5. Justificar brevemente por que cada snapshot/properties aporta valor.

## Estructura

- `starter/`: plantilla con TODOs para implementar.
- `solution/`: referencia local del instructor (no versionada).

## Criterios minimos

- Minimo 8 tests en total.
- Minimo 2 tests de ejemplo narrativos.
- Minimo 2 property-based tests con `fast-check`.
- Minimo 1 snapshot bien acotado.

## Ejecucion sugerida

```bash
yarn add -D fast-check
yarn test
```
