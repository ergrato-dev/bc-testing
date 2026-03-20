# Proyecto Semanal - Suite Python con Estrategia de Mocking

## Objetivo

Diseñar una suite en `pytest` que use mocks de forma intencional para aislar dependencias y validar comportamiento de negocio.

## Contexto

Este proyecto es el entregable obligatorio de la semana.
Debes adaptar el starter a tu dominio asignado por el instructor.

## Requisitos

1. Implementar al menos 8 casos efectivos en total.
2. Incluir minimo 3 casos de error usando `side_effect`.
3. Aplicar `patch` en el target correcto en al menos 2 pruebas.
4. Incluir al menos 1 escenario con `spy` o verificacion de interaccion.
5. Justificar brevemente que dependencias mockeas y por que.

## Estructura

- `starter/`: plantilla con TODOs para implementar.
- `solution/`: referencia local del instructor (no versionada).

## Criterios minimos

- Tests ejecutables con `pytest`.
- Nombres descriptivos en snake_case.
- Patron AAA visible en los tests clave.
- Mocks no fragiles y alineados a decisiones de negocio.

## Ejecucion sugerida

```bash
pytest -q
pytest -k "service or policy"
```
