import httpx2
import pytest
import pytest_asyncio

from library.catalog_client import BookNotFoundError, CatalogClient


@pytest_asyncio.fixture
async def catalog():
    async def handler(request: httpx2.Request) -> httpx2.Response:
        if request.url.path == "/books/9780000000001":
            return httpx2.Response(200, json={"title": "Dune"})
        return httpx2.Response(404)

    client = CatalogClient("https://catalog.test", transport=httpx2.MockTransport(handler))
    yield client
    await client.aclose()


@pytest.mark.asyncio
async def test_get_title_returns_title_from_catalog(catalog):
    assert await catalog.get_title("9780000000001") == "Dune"


@pytest.mark.asyncio
async def test_get_title_raises_when_book_is_unknown(catalog):
    with pytest.raises(BookNotFoundError):
        await catalog.get_title("9789999999999")
