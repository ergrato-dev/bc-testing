"""Dependencias externas reales (simuladas). En los tests unitarios nunca deben ejecutarse."""


def fetch_item_status(item_id: str) -> dict:
    """Consulta una API HTTP de inventario. Respuesta esperada: {"blocked": bool}."""
    raise ConnectionError(f"GET https://inventory.example/items/{item_id}: sin red en los tests")


class ItemRepository:
    """Acceso a base de datos."""

    def save(self, item_id: str, status: str) -> None:
        raise ConnectionError("base de datos no disponible en los tests")


class ExternalNotifier:
    """Envío de notificaciones a un servicio externo."""

    def send(self, item_id: str, status: str) -> None:
        raise ConnectionError("servicio de notificaciones no disponible en los tests")
