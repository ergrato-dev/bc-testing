# Proyecto Semanal - Suite Combinada de Snapshots y Propiedades

## Objetivo

Construir una suite de calidad para el dominio asignado combinando:

- tests de ejemplo clásicos,
- snapshot tests con intención,
- property-based tests con `fast-check`.

## Contexto

Este proyecto es el entregable obligatorio de la semana. Debes adaptar el starter a tu dominio asignado por el instructor.

## Requisitos

1. Incluir al menos 1 snapshot de payload estable y relevante.
2. Incluir al menos 2 propiedades invariantes de `paginate` (por ejemplo: conservación de elementos y orden, tamaño máximo de página, número de páginas).
3. Cubrir flujo feliz y validaciones de error (`buildPublicItem` y `paginate` lanzan errores ante entradas inválidas).
4. Mantener patrón AAA y nombres descriptivos.
5. Justificar brevemente por qué cada snapshot/properties aporta valor.

## Estructura

- `starter/`: plantilla con TODOs para implementar.
- `solution/`: referencia local del instructor (no versionada).

## Criterios mínimos

- Mínimo 8 tests en total.
- Mínimo 2 tests de ejemplo narrativos.
- Mínimo 2 property-based tests con `fast-check`.
- Mínimo 1 snapshot bien acotado.

## Ejecución sugerida

```bash
pnpm install
pnpm test
```
