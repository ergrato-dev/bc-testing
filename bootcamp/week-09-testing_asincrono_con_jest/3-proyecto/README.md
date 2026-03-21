# Proyecto Semanal - Suite Asincrona de ItemService

## Objetivo

Construir una suite de tests asincronos con Jest para un servicio de dominio adaptable.

## Contexto

Este proyecto es el entregable obligatorio de la semana. Debes adaptar el starter a tu dominio asignado por el instructor.

## Requisitos

1. Cubrir casos exitosos y errores asincronos con `async/await`.
2. Usar `await expect(...).rejects` para errores.
3. Incluir al menos un escenario de retry con timers.
4. Mantener patron AAA en todos los tests.
5. Nombrar tests con formato: `should [expected] when [condition]`.

## Estructura

- `starter/`: plantilla con TODOs para implementar.
- `solution/`: referencia local del instructor (no versionada).

## Criterios minimos

- Minimo 8 tests.
- Minimo 1 mock con `jest.fn()`.
- Minimo 1 escenario de validacion de timeout/retry.
- Cobertura sugerida: >= 85% en archivos del proyecto.

## Ejecucion sugerida

```bash
yarn test
```
