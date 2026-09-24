"""Servicio de items con almacenamiento en un archivo JSON."""

import json
import os
from pathlib import Path

DEFAULT_ITEM_LIMIT = 100


def create_item(payload: dict) -> dict:
    name = payload.get("name", "").strip() if isinstance(payload, dict) else ""
    quantity = payload.get("quantity") if isinstance(payload, dict) else None

    if not name:
        raise ValueError("name is required")

    if not isinstance(quantity, int) or quantity < 0:
        raise ValueError("quantity must be a non-negative integer")

    return {
        "name": name,
        "quantity": quantity,
        "status": "available" if quantity > 0 else "empty",
    }


def item_limit() -> int:
    """Máximo de items por almacén, configurable con la variable ITEM_LIMIT."""
    return int(os.environ.get("ITEM_LIMIT", DEFAULT_ITEM_LIMIT))


class ItemStore:
    """Almacén que carga items desde `path` al abrirse y los guarda al cerrarse."""

    def __init__(self, path: Path) -> None:
        self.path = path
        self.is_open = False
        self._items: list[dict] = []

    def open(self) -> None:
        if self.path.exists():
            self._items = json.loads(self.path.read_text(encoding="utf-8"))
        self.is_open = True

    def close(self) -> None:
        if self.is_open:
            self.path.write_text(json.dumps(self._items, ensure_ascii=False), encoding="utf-8")
        self.is_open = False

    def add(self, payload: dict) -> dict:
        if not self.is_open:
            raise RuntimeError("store is closed")
        if len(self._items) >= item_limit():
            raise ValueError("item limit reached")
        item = create_item(payload)
        self._items.append(item)
        return item

    def all(self) -> list[dict]:
        return list(self._items)
