# Proyecto Semanal - Suite Asíncrona de ItemService

## Objetivo

Construir una suite de tests asíncronos con Jest para un servicio de dominio adaptable.

## Contexto

Este proyecto es el entregable obligatorio de la semana. Debes adaptar el starter a tu dominio asignado por el instructor.

## Requisitos

1. Cubrir casos exitosos y errores asíncronos con `async/await`.
2. Usar `await expect(...).rejects` para errores.
3. Incluir al menos un escenario de retry con fake timers sobre `findByIdWithRetry` del starter.
4. Mantener patrón AAA en todos los tests.
5. Nombrar tests con formato: `should [expected] when [condition]`.

## Estructura

- `starter/`: plantilla con TODOs para implementar.
- `solution/`: referencia local del instructor (no versionada).

## Criterios mínimos

- Mínimo 8 tests.
- Mínimo 1 mock con `jest.fn()`.
- Mínimo 1 escenario de validación de timeout/retry.
- Todos los tests en verde con `pnpm test` (sin tests saltados con `.skip` ni `.only`).

## Ejecución sugerida

```bash
pnpm install
pnpm test
```
