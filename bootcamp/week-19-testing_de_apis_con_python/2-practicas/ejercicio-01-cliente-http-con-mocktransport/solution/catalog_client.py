import httpx2
from pydantic import BaseModel


class Piece(BaseModel):
    id: int
    title: str
    artist: str
    year: int


class CatalogError(Exception):
    """Error base del cliente del catálogo."""


class PieceNotFoundError(CatalogError):
    """La pieza no existe (HTTP 404)."""


class CatalogUnavailableError(CatalogError):
    """El catálogo no responde o falla (timeout o HTTP 5xx)."""


class CatalogClient:
    def __init__(
        self,
        base_url: str,
        token: str,
        transport: httpx2.BaseTransport | None = None,
        timeout: float = 2.0,
    ) -> None:
        # El transporte se inyecta: en producción es el de red, en los tests un MockTransport.
        self._client = httpx2.Client(
            base_url=base_url,
            headers={"Authorization": f"Bearer {token}"},
            timeout=timeout,
            transport=transport,
        )

    def get_piece(self, piece_id: int) -> Piece:
        try:
            response = self._client.get(f"/pieces/{piece_id}")
        except httpx2.TimeoutException as error:
            raise CatalogUnavailableError("catalog timed out") from error

        if response.status_code == 404:
            raise PieceNotFoundError(f"piece {piece_id} not found")
        if response.status_code >= 500:
            raise CatalogUnavailableError(f"catalog returned {response.status_code}")
        response.raise_for_status()
        return Piece.model_validate(response.json())
