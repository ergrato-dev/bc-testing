# ============================================
# MÓDULO: ItemClient
# Cliente HTTP del API de tu dominio
# ============================================

# NOTA PARA EL APRENDIZ:
# Adapta este cliente a tu dominio asignado (nombre del recurso, campos del modelo
# y reglas). Ejemplos neutrales (no los uses en tu entrega):
# - Museo: /pieces (title, artist, year)
# - Planetario: /shows (title, starts_at, capacity)
# - Acuario: /tanks (name, liters, species_count)
#
# El transporte se inyecta para que los tests usen httpx2.MockTransport y nunca
# salgan a la red.

import httpx2
from pydantic import BaseModel


class Item(BaseModel):
    # TODO: ajusta los campos a tu dominio
    id: int
    name: str
    quantity: int


class ItemApiError(Exception):
    """Error base del cliente."""


class ItemNotFoundError(ItemApiError):
    """El recurso no existe (HTTP 404)."""


class ItemRejectedError(ItemApiError):
    """El API rechazó los datos enviados (HTTP 422)."""


class ItemApiUnavailableError(ItemApiError):
    """El API no responde o falla (timeout o HTTP 5xx)."""


class ItemClient:
    def __init__(
        self,
        base_url: str,
        token: str,
        transport: httpx2.BaseTransport | None = None,
        timeout: float = 2.0,
    ) -> None:
        self._client = httpx2.Client(
            base_url=base_url,
            headers={"Authorization": f"Bearer {token}"},
            timeout=timeout,
            transport=transport,
        )

    def _send(self, method: str, path: str, **kwargs) -> httpx2.Response:
        try:
            response = self._client.request(method, path, **kwargs)
        except httpx2.TimeoutException as error:
            raise ItemApiUnavailableError("item API timed out") from error
        if response.status_code >= 500:
            raise ItemApiUnavailableError(f"item API returned {response.status_code}")
        return response

    def get_item(self, item_id: int) -> Item:
        response = self._send("GET", f"/items/{item_id}")
        if response.status_code == 404:
            raise ItemNotFoundError(f"item {item_id} not found")
        response.raise_for_status()
        return Item.model_validate(response.json())

    def list_items(self, min_quantity: int = 0) -> list[Item]:
        response = self._send("GET", "/items", params={"min_quantity": min_quantity})
        response.raise_for_status()
        return [Item.model_validate(raw) for raw in response.json()]

    def create_item(self, name: str, quantity: int) -> Item:
        response = self._send("POST", "/items", json={"name": name, "quantity": quantity})
        if response.status_code == 422:
            raise ItemRejectedError(response.json()["detail"])
        response.raise_for_status()
        return Item.model_validate(response.json())
