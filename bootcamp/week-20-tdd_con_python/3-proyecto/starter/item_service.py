# ============================================
# MÓDULO: ItemService (construido con TDD)
# ============================================

# NOTA PARA EL APRENDIZ:
# Adapta el modelo y las reglas a tu dominio asignado. Ejemplos neutrales
# (no los uses en tu entrega):
# - Museo: Piece + PieceService (registrar piezas, préstamos entre salas)
# - Planetario: Show + ShowService (funciones, aforo, venta de entradas)
# - Acuario: Tank + TankService (tanques, especies, capacidad en litros)
#
# Regla del proyecto: ninguna línea de ItemService se escribe sin un test que
# antes haya fallado por el motivo esperado.

from dataclasses import dataclass
from typing import Protocol


@dataclass(frozen=True)
class Item:
    # TODO: ajusta los campos a tu dominio
    id: int
    name: str
    quantity: int


class ItemRepository(Protocol):
    """Lo que ItemService necesita de la persistencia (tipado estructural)."""

    def save(self, item: Item) -> None: ...

    def get(self, item_id: int) -> Item | None: ...

    def list_all(self) -> list[Item]: ...


class InMemoryItemRepository:
    """Fake para los tests: cumple ItemRepository sin heredar de él."""

    def __init__(self) -> None:
        self._items: dict[int, Item] = {}

    def save(self, item: Item) -> None:
        self._items[item.id] = item

    def get(self, item_id: int) -> Item | None:
        return self._items.get(item_id)

    def list_all(self) -> list[Item]:
        return list(self._items.values())


class ItemService:
    def __init__(self, repository: ItemRepository) -> None:
        self._repository = repository

    def create_item(self, name: str, quantity: int) -> Item:
        # TODO (TDD): nombre obligatorio, cantidad >= 0, id incremental
        raise NotImplementedError

    def restock(self, item_id: int, amount: int) -> Item:
        # TODO (TDD): item inexistente, amount > 0, devuelve el item actualizado
        raise NotImplementedError

    def total_quantity(self) -> int:
        # TODO (TDD): suma de las cantidades de todos los items
        raise NotImplementedError
