# Proyecto Semanal - Suite Python con Estrategia de Mocking

## Objetivo

Diseñar una suite en `pytest` que use dobles de forma intencional para aislar las dependencias externas (API HTTP, base de datos, servicio de notificaciones) y validar el comportamiento de negocio.

## Contexto

Este proyecto es el entregable obligatorio de la semana.
Debes adaptar el starter a tu dominio asignado por el instructor.

El starter tiene dos tipos de dependencias, y cada una pide una técnica distinta:

| Dependencia | Cómo llega a `ItemService` | Técnica |
|---|---|---|
| `fetch_item_status` (API HTTP) | Importada a nivel de módulo en `item_service.py` | `patch` / `mocker.patch` en el target correcto |
| `ItemRepository` (BD) y `ExternalNotifier` | Inyectadas en el constructor | Dobles simples con `spec` o `autospec` |

Las implementaciones reales de `starter/gateways.py` lanzan `ConnectionError`: si un test las ejecuta, el doble no está donde crees.

## Requisitos

1. Implementar al menos 8 casos efectivos en total.
2. Incluir mínimo 3 casos de error usando `side_effect`.
3. Aplicar `patch` en el target correcto en al menos 2 pruebas (la API HTTP no se puede inyectar).
4. Incluir al menos 1 verificación de interacción con `assert_called_once_with` y 1 con `assert_not_called`.
5. Usar `spec` o `autospec` en los dobles de las dependencias inyectadas.
6. Justificar brevemente qué dependencias mockeas, con qué tipo de doble y por qué.

## Estructura

- `starter/`: plantilla con TODOs para implementar.
- `solution/`: referencia local del instructor (no versionada).

## Criterios mínimos

- Tests ejecutables con `uv run pytest`.
- Nombres con el formato `test_[context]_[expected]_when_[condition]`.
- Patrón AAA visible en los tests clave.
- Mocks no frágiles y alineados a decisiones de negocio.

## Ejecución sugerida

```bash
cd starter
uv sync
uv run pytest -q
```
