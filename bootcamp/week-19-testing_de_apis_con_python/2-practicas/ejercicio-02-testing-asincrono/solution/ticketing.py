import asyncio

import httpx2


class TicketingClient:
    def __init__(self, base_url: str, transport: httpx2.AsyncBaseTransport | None = None) -> None:
        self._client = httpx2.AsyncClient(base_url=base_url, timeout=2.0, transport=transport)

    async def get_available_seats(self, date: str) -> int:
        response = await self._client.get("/availability", params={"date": date})
        response.raise_for_status()
        return response.json()["available_seats"]

    async def aclose(self) -> None:
        await self._client.aclose()


async def total_available_seats(client: TicketingClient, dates: list[str]) -> int:
    # Las consultas se lanzan en paralelo y se esperan todas juntas.
    counts = await asyncio.gather(*(client.get_available_seats(date) for date in dates))
    return sum(counts)
