# Proyecto Semanal - API REST del Dominio Asignado

## Objetivo

Implementar y testear una API REST del dominio asignado usando Jest + Supertest.

## Contexto

Este proyecto es el entregable obligatorio de la semana. Debes adaptar el starter a tu dominio asignado por el instructor.

## Requisitos

1. Definir y probar al menos 3 endpoints (`GET`, `POST`, `GET by id`).
2. Validar contratos de exito y error en cada endpoint critico.
3. Incluir manejo de errores 400, 404, 409 y 500 (middleware de errores de Express 5, probado con un repositorio inyectado que lance).
4. Mantener tests con patron AAA y nombres descriptivos.
5. Usar nombres tecnicos en ingles y documentacion en espanol.
6. Aislar cada test: crear la app con `createApp()` en `beforeEach` para que ningun test dependa de datos de otro.

## Estructura

- `starter/`: plantilla con TODOs para implementar.
- `solution/`: referencia local del instructor (no versionada).

## Criterios minimos

- Minimo 8 tests de endpoint.
- Minimo 1 test por caso de error clave.
- Contrato JSON consistente de errores.
- Todos los tests en verde, tambien al ejecutarlos en orden aleatorio (`pnpm test --randomize`).

## Ejecucion sugerida

```bash
cd starter
pnpm install
pnpm test
```
