# Ejercicio 01 - Cliente HTTP con MockTransport

## Objetivo

Probar un cliente HTTP síncrono sin salir a la red: simular respuestas 200, 404, 503 y timeouts con `httpx2.MockTransport`, verificar la request enviada (ruta y Bearer Token) y detectar un contrato roto con `pydantic`. Dos de los tests encuentran bugs reales del starter.

## Tiempo estimado

90 minutos.

## Paso a paso

Abre `starter/catalog_client.py`. `CatalogClient` recibe un `transport` opcional: en producción es `None` (red real) y en los tests es un `MockTransport`. `get_piece` devuelve un modelo `Piece` de `pydantic`.

En `starter/test_catalog_client.py` ya están activas dos fixtures:

- `sent_requests`: lista donde se guarda cada request que envía el cliente.
- `make_client(handler)`: crea el cliente con un `MockTransport` que registra la request y responde con tu `handler`.

```bash
cd starter
uv sync
uv run pytest -q
```

La primera ejecución termina con `no tests ran`.

### Paso 1: Happy path y request enviada

Descomenta el PASO 1 (dos tests). El primero verifica el modelo devuelto, el método y la ruta `/api/pieces/7` (el `base_url` incluye `/api`). El segundo verifica el header `Authorization: Bearer test-token`, que es como se prueba la autenticación de un cliente.

### Paso 2: 404

Descomenta el PASO 2. El cliente traduce el 404 a `PieceNotFoundError`. Pasa en verde.

### Paso 3: 503 con cuerpo HTML

Descomenta el PASO 3 y ejecuta `uv run pytest -q --tb=line`. Falla:

```text
E   json.decoder.JSONDecodeError: Expecting value: line 1 column 1 (char 0)
```

El starter solo revisa el 404 y luego llama a `response.json()` sobre una página HTML. Corrige `get_piece` para que cualquier código `>= 500` lance `CatalogUnavailableError(f"catalog returned {response.status_code}")`, y llama a `response.raise_for_status()` antes de leer el JSON.

### Paso 4: Timeout

Descomenta el PASO 4. El handler lanza `httpx2.ReadTimeout` en lugar de responder. Falla:

```text
E   httpx2.ReadTimeout: read timed out
```

La excepción de `httpx2` se escapa del cliente. Envuelve la llamada en `try/except httpx2.TimeoutException` y lanza `CatalogUnavailableError("catalog timed out")` con `raise ... from error`.

### Paso 5: Contrato roto

Descomenta el PASO 5. El API responde 200 pero sin el campo `year`, y `Piece.model_validate` lanza `pydantic.ValidationError`. El test documenta que `year` es obligatorio para tu cliente.

### Paso 6: Revisar la solución

```text
6 passed
```

Compara `starter/catalog_client.py` con `solution/catalog_client.py`.

## Comprueba que los tests protegen el cliente

Aplica cada cambio en `solution/catalog_client.py`, ejecuta `uv run pytest -q` y deshazlo:

| Cambio | Test que falla |
|---|---|
| `"Authorization": token` (sin `Bearer`) | `test_get_piece_sends_bearer_token_when_calling_api` |
| `f"/piece/{piece_id}"` | `test_get_piece_returns_piece_when_api_responds_200` |
| `status_code > 503` | `test_get_piece_raises_unavailable_when_api_responds_503_with_html` |
| `except httpx2.ConnectTimeout` | `test_get_piece_raises_unavailable_when_request_times_out` |
