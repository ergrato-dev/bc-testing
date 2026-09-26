from unittest.mock import AsyncMock, call

import httpx2
import pytest
import pytest_asyncio

from ticketing import TicketingClient, total_available_seats

BASE_URL = "https://tickets.test"


@pytest_asyncio.fixture
async def seats_client():
    """Cliente con transporte simulado; se cierra al terminar cada test (teardown asíncrono)."""

    async def handler(request: httpx2.Request) -> httpx2.Response:
        seats_by_date = {"2026-10-01": 12, "2026-10-02": 0}
        date = request.url.params["date"]
        return httpx2.Response(200, json={"date": date, "available_seats": seats_by_date[date]})

    client = TicketingClient(BASE_URL, transport=httpx2.MockTransport(handler))
    yield client
    await client.aclose()


# ============================================
# PASO 1: Test asíncrono en modo strict
# ============================================
@pytest.mark.asyncio
async def test_get_available_seats_returns_count_when_date_has_seats(seats_client):
    # Act
    seats = await seats_client.get_available_seats("2026-10-01")

    # Assert
    assert seats == 12


@pytest.mark.asyncio
async def test_get_available_seats_returns_zero_when_date_is_sold_out(seats_client):
    # Act
    seats = await seats_client.get_available_seats("2026-10-02")

    # Assert
    assert seats == 0


# ============================================
# PASO 2: Error HTTP en un cliente asíncrono
# ============================================
@pytest.mark.asyncio
async def test_get_available_seats_raises_http_error_when_api_responds_500():
    # Arrange
    async def failing_handler(request: httpx2.Request) -> httpx2.Response:
        return httpx2.Response(500, json={"detail": "boom"})

    client = TicketingClient(BASE_URL, transport=httpx2.MockTransport(failing_handler))

    # Act / Assert
    with pytest.raises(httpx2.HTTPStatusError, match="500"):
        await client.get_available_seats("2026-10-01")
    await client.aclose()


# ============================================
# PASO 3: AsyncMock para aislar el cliente
# ============================================
@pytest.mark.asyncio
async def test_total_available_seats_sums_all_dates_when_client_answers():
    # Arrange
    client = AsyncMock(spec=TicketingClient)
    client.get_available_seats.side_effect = [5, 7, 3]
    dates = ["2026-10-01", "2026-10-02", "2026-10-03"]

    # Act
    total = await total_available_seats(client, dates)

    # Assert
    assert total == 15
    assert client.get_available_seats.await_count == 3
    assert client.get_available_seats.await_args_list == [call(date) for date in dates]


# ============================================
# PASO 4: Un fallo dentro de gather se propaga
# ============================================
@pytest.mark.asyncio
async def test_total_available_seats_propagates_error_when_one_date_times_out():
    # Arrange
    client = AsyncMock(spec=TicketingClient)
    client.get_available_seats.side_effect = [5, TimeoutError("date 2026-10-02 timed out"), 0]

    # Act / Assert
    with pytest.raises(TimeoutError, match="2026-10-02"):
        await total_available_seats(client, ["2026-10-01", "2026-10-02", "2026-10-03"])
