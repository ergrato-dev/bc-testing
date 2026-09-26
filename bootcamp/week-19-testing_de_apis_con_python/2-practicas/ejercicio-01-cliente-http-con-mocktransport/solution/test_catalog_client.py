import httpx2
import pytest
from pydantic import ValidationError

from catalog_client import CatalogClient, CatalogUnavailableError, Piece, PieceNotFoundError

BASE_URL = "https://catalog.test/api"
PIECE_PAYLOAD = {"id": 7, "title": "La noche estrellada", "artist": "Vincent van Gogh", "year": 1889}


@pytest.fixture
def sent_requests() -> list[httpx2.Request]:
    return []


@pytest.fixture
def make_client(sent_requests):
    """Crea un CatalogClient cuyo transporte responde con `handler` y registra cada request."""

    def _make(handler) -> CatalogClient:
        def recording_handler(request: httpx2.Request) -> httpx2.Response:
            sent_requests.append(request)
            return handler(request)

        return CatalogClient(BASE_URL, token="test-token", transport=httpx2.MockTransport(recording_handler))

    return _make


# ============================================
# PASO 1: Happy path y request enviada
# ============================================
def test_get_piece_returns_piece_when_api_responds_200(make_client, sent_requests):
    # Arrange
    client = make_client(lambda request: httpx2.Response(200, json=PIECE_PAYLOAD))

    # Act
    piece = client.get_piece(7)

    # Assert
    assert piece == Piece(**PIECE_PAYLOAD)
    assert sent_requests[0].method == "GET"
    assert sent_requests[0].url.path == "/api/pieces/7"


def test_get_piece_sends_bearer_token_when_calling_api(make_client, sent_requests):
    # Arrange
    client = make_client(lambda request: httpx2.Response(200, json=PIECE_PAYLOAD))

    # Act
    client.get_piece(7)

    # Assert
    assert sent_requests[0].headers["Authorization"] == "Bearer test-token"


# ============================================
# PASO 2: 404 se traduce a una excepción del dominio
# ============================================
def test_get_piece_raises_not_found_when_api_responds_404(make_client):
    # Arrange
    client = make_client(lambda request: httpx2.Response(404, json={"detail": "Not Found"}))

    # Act / Assert
    with pytest.raises(PieceNotFoundError, match="piece 99 not found"):
        client.get_piece(99)


# ============================================
# PASO 3: 5xx con cuerpo HTML
# ============================================
def test_get_piece_raises_unavailable_when_api_responds_503_with_html(make_client):
    # Arrange: un proxy caído suele responder HTML, no JSON
    client = make_client(
        lambda request: httpx2.Response(503, text="<html><body>Service Unavailable</body></html>")
    )

    # Act / Assert
    with pytest.raises(CatalogUnavailableError, match="catalog returned 503"):
        client.get_piece(7)


# ============================================
# PASO 4: Timeout
# ============================================
def test_get_piece_raises_unavailable_when_request_times_out(make_client):
    # Arrange
    def timeout_handler(request: httpx2.Request) -> httpx2.Response:
        raise httpx2.ReadTimeout("read timed out", request=request)

    client = make_client(timeout_handler)

    # Act / Assert
    with pytest.raises(CatalogUnavailableError, match="timed out"):
        client.get_piece(7)


# ============================================
# PASO 5: Contrato roto (falta un campo)
# ============================================
def test_get_piece_raises_validation_error_when_payload_misses_year(make_client):
    # Arrange
    payload_without_year = {key: value for key, value in PIECE_PAYLOAD.items() if key != "year"}
    client = make_client(lambda request: httpx2.Response(200, json=payload_without_year))

    # Act / Assert
    with pytest.raises(ValidationError, match="year"):
        client.get_piece(7)
