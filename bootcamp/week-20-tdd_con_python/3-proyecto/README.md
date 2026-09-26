# Proyecto Semanal - Servicio del Dominio con TDD

> **🎯 ÚNICO ENTREGABLE**: Este proyecto es el **único entregable obligatorio** para aprobar la semana.

## Objetivo

Construir con TDD un servicio de tu dominio: cada regla nace de un test que primero falló por el motivo esperado. El diseño (`dataclass`, `Protocol`, fake en memoria) ya está esbozado en el starter; la lógica la escribes tú, ciclo a ciclo.

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

## Punto de partida

`starter/item_service.py` contiene:

| Elemento | Estado |
|---|---|
| `Item` (`dataclass(frozen=True)`) | Listo; adapta los campos a tu dominio |
| `ItemRepository` (`Protocol`) | Listo |
| `InMemoryItemRepository` (fake) | Listo |
| `ItemService.create_item`, `restock`, `total_quantity` | `raise NotImplementedError`: los implementas con TDD |

```bash
cd starter
uv sync
uv run pytest -q
uv run mypy .
```

Al empezar verás `3 skipped` y `Success: no issues found`.

## Ejemplos de adaptación por dominio

- **Museo**: `Piece`, registrar piezas, prestarlas entre salas, total de piezas expuestas.
- **Planetario**: `Show`, crear funciones, vender entradas sin superar el aforo, entradas vendidas en el día.
- **Acuario**: `Tank`, registrar tanques, añadir especies sin superar la capacidad, litros totales.

## Requisitos

1. Al menos 15 tests; ninguna línea de `ItemService` sin un test que antes haya fallado.
2. Al menos 5 ciclos Red-Green-Refactor con evidencia (ver abajo).
3. Reglas de validación con su test de error (`pytest.raises(..., match=...)`).
4. Dos propiedades con `@given`, por ejemplo: "el total es la suma de lo registrado" y "reponer nunca disminuye la cantidad". Crea el servicio dentro del test: `@given` con la fixture `service` falla con `FailedHealthCheck`.
5. `uv run pytest -q` y `uv run mypy .` en verde al final de cada ciclo, y ningún test en `skipped` en la entrega.
6. Nombres con el patrón `test_[context]_[expected]_when_[condition]`.

## Evidencia del ciclo Red-Green-Refactor

Opción recomendada: un commit por fase.

```text
test(item): red - create_item raises value error when name is blank
feat(item): green - validate name in create_item
refactor(item): extract name normalization
```

Si no usas git, entrega la salida de `uv run pytest -q --tb=line` de cada fase. En Red debe verse el fallo esperado (`NotImplementedError`, `AssertionError`, `DID NOT RAISE`), no un error de import ni de sintaxis.

## Entregables

1. `starter/item_service.py` y `starter/test_item_service.py` adaptados a tu dominio.
2. Evidencia de los ciclos.
3. Un párrafo en tu README: qué regla expresó cada propiedad y si alguna encontró un caso que tus ejemplos no cubrían.

> `solution/` del proyecto no se publica en el repositorio.
