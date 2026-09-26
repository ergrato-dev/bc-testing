# Ejercicio 02 - Testing Asíncrono

## Objetivo

Probar un cliente `httpx2.AsyncClient` y una función que lanza consultas en paralelo con `asyncio.gather`: tests `async` en modo `strict` de `pytest-asyncio`, una fixture asíncrona con teardown y `AsyncMock`. El PASO 3 encuentra un `await` olvidado en el starter.

## Tiempo estimado

90 minutos.

## Paso a paso

Abre `starter/ticketing.py`:

- `TicketingClient.get_available_seats(date)` hace `GET /availability?date=...`.
- `total_available_seats(client, dates)` suma los asientos de varias fechas.

`starter/pyproject.toml` fija `asyncio_mode = "strict"`. En `starter/test_ticketing.py` ya está activa la fixture `seats_client`, decorada con `@pytest_asyncio.fixture`: crea el cliente con un handler asíncrono y lo cierra (`aclose`) después de cada test.

```bash
cd starter
uv sync
uv run pytest -q
```

### Paso 1: Tests asíncronos en modo strict

Descomenta el PASO 1 (dos tests). Cada uno lleva `@pytest.mark.asyncio`. Para ver por qué, quita temporalmente el marcador de uno de ellos y ejecuta:

```text
async def functions are not natively supported.
You need to install a suitable plugin for your async framework, for example:
  - anyio
  - pytest-asyncio
...
1 failed
```

En modo `strict` el plugin ignora los tests sin marcador. Vuelve a poner el marcador.

### Paso 2: Error HTTP en un cliente asíncrono

Descomenta el PASO 2. El handler responde 500 y `raise_for_status()` lanza `httpx2.HTTPStatusError`. Este test crea su propio cliente porque necesita otro handler, así que lo cierra él mismo con `await client.aclose()`.

### Paso 3: `AsyncMock` y el `await` olvidado

Descomenta el PASO 3. `AsyncMock(spec=TicketingClient)` sustituye al cliente: cada llamada devuelve una corutina que, al esperarse, entrega el siguiente valor de `side_effect`. Ejecuta `uv run pytest -q --tb=line`:

```text
E   TypeError: unsupported operand type(s) for +: 'int' and 'coroutine'
<sys>:0: RuntimeWarning: coroutine 'AsyncMockMixin._execute_mock_call' was never awaited
```

El starter crea las corutinas pero nunca las espera, y `sum` intenta sumar corutinas. Corrige `total_available_seats` con `await asyncio.gather(...)` (y el `import asyncio`).

### Paso 4: Un fallo dentro de `gather`

Descomenta el PASO 4. El segundo valor del `side_effect` es una excepción: `gather` la propaga y el test la verifica con `pytest.raises(TimeoutError, match="2026-10-02")`.

### Paso 5: Probar el modo `auto`

Copia la carpeta `solution` a una ubicación temporal. En la copia, cambia `asyncio_mode` a `"auto"`, borra todos los `@pytest.mark.asyncio` y cambia `@pytest_asyncio.fixture` por `@pytest.fixture`. La suite sigue en verde:

```text
5 passed
```

Esa es la diferencia entre modos: en `auto` el plugin gestiona todo lo `async`; en `strict` solo lo que marcas.

## Comprueba que los tests protegen el código

Aplica cada cambio en `solution/ticketing.py`, ejecuta `uv run pytest -q` y deshazlo:

| Cambio | Tests que fallan |
|---|---|
| `params={"day": date}` | Los dos del PASO 1 |
| Borrar `response.raise_for_status()` | PASO 2 |
| `return sum(counts[:-1])` | PASO 3 |
| `client.get_available_seats(dates[0])` dentro del `gather` | PASO 3 |
