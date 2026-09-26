# Proyecto Semanal - Suite del Cliente HTTP del Dominio

> **🎯 ÚNICO ENTREGABLE**: Este proyecto es el **único entregable obligatorio** para aprobar la semana.

## Objetivo

Construir la suite de tests del cliente HTTP que consume el API de tu dominio, sin salir a la red: respuestas simuladas con `httpx2.MockTransport`, verificación de la request enviada y validación del contrato con `pydantic`.

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

## Punto de partida

`starter/item_client.py` es un cliente genérico ya implementado:

| Método | Request | Respuestas que traduce |
|---|---|---|
| `get_item(item_id)` | `GET /items/{id}` | 200 → `Item`, 404 → `ItemNotFoundError` |
| `list_items(min_quantity)` | `GET /items?min_quantity=...` | 200 → `list[Item]` |
| `create_item(name, quantity)` | `POST /items` con JSON | 201 → `Item`, 422 → `ItemRejectedError` |
| Cualquiera | | 5xx o timeout → `ItemApiUnavailableError` |

Todas las requests envían `Authorization: Bearer <token>`.

1. Adapta el recurso (`/items`), el modelo `Item` y los nombres a tu dominio.
2. Escribe la suite en `starter/test_item_client.py`, sustituyendo cada `pytest.skip`.

```bash
cd starter
uv sync
uv run pytest -q
```

Al empezar verás `3 skipped`.

## Ejemplos de adaptación por dominio

- **Museo**: `/pieces`, modelo `Piece(id, title, artist, year)`, filtro por año.
- **Planetario**: `/shows`, modelo `Show(id, title, starts_at, capacity)`, filtro por aforo mínimo.
- **Acuario**: `/tanks`, modelo `Tank(id, name, liters, species_count)`, filtro por capacidad.

## Requisitos

1. Al menos 10 tests efectivos.
2. Happy path de los tres métodos, verificando la request enviada: ruta, query params (`list_items`) y cuerpo JSON (`create_item`).
3. Un test del header `Authorization`.
4. Errores: 404, 422 (con el mensaje del API), 5xx con cuerpo no JSON y timeout.
5. Contrato: un campo obligatorio ausente y un tipo incorrecto → `pydantic.ValidationError`.
6. Ningún test sale a la red y ninguno queda en `skipped`.
7. Nombres con el patrón `test_[context]_[expected]_when_[condition]` y patrón AAA visible.

## Entregables

1. `starter/item_client.py` adaptado a tu dominio.
2. `starter/test_item_client.py` completo.
3. Salida de `uv run pytest -q` con todos los tests en verde.
4. Un párrafo en tu README explicando qué detectarían tus tests de contrato y qué quedaría fuera sin contract testing (Pact).

> `solution/` del proyecto no se publica en el repositorio.
