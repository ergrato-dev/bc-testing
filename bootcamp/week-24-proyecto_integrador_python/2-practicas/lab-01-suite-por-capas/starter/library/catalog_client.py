import httpx2


class BookNotFoundError(Exception):
    pass


class CatalogClient:
    def __init__(self, base_url: str, transport: httpx2.AsyncBaseTransport | None = None) -> None:
        self._client = httpx2.AsyncClient(base_url=base_url, timeout=2.0, transport=transport)

    async def get_title(self, isbn: str) -> str:
        response = await self._client.get(f"/books/{isbn}")
        if response.status_code == 404:
            raise BookNotFoundError(isbn)
        response.raise_for_status()
        return response.json()["title"]

    async def aclose(self) -> None:
        await self._client.aclose()
