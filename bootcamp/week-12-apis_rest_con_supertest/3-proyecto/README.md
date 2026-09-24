# Proyecto Semanal - API REST del Dominio Asignado

## Objetivo

Implementar y testear una API REST del dominio asignado usando Jest + Supertest.

## Contexto

Este proyecto es el entregable obligatorio de la semana. Debes adaptar el starter a tu dominio asignado por el instructor.

## Requisitos

1. Definir y probar al menos 3 endpoints (`GET`, `POST`, `GET by id`).
2. Validar contratos de éxito y error en cada endpoint crítico.
3. Incluir manejo de errores 400, 404, 409 y 500 (middleware de errores de Express 5, probado con un repositorio inyectado que lance).
4. Mantener tests con patrón AAA y nombres descriptivos.
5. Usar nombres técnicos en inglés y documentación en español.
6. Aislar cada test: crear la app con `createApp()` en `beforeEach` para que ningún test dependa de datos de otro.

## Estructura

- `starter/`: plantilla con TODOs para implementar.
- `solution/`: referencia local del instructor (no versionada).

## Criterios mínimos

- Mínimo 8 tests de endpoint.
- Mínimo 1 test por caso de error clave.
- Contrato JSON consistente de errores.
- Todos los tests en verde, también al ejecutarlos en orden aleatorio (`pnpm test --randomize`).

## Ejecución sugerida

```bash
cd starter
pnpm install
pnpm test
```
