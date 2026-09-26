# Glosario Semana 19 - Testing de APIs con Python

## A

- **AsyncClient**: cliente HTTP asíncrono de `httpx2`; sus métodos se esperan con `await` y se cierra con `aclose()`.
- **AsyncMock**: mock de `unittest.mock` cuyas llamadas devuelven corutinas; registra `await_count` y `await_args_list`.
- **asyncio_mode**: opción de `pytest-asyncio` (`strict` por defecto, o `auto`) que decide qué tests y fixtures `async` gestiona el plugin.

## B

- **Bearer Token**: esquema de autenticación que envía `Authorization: Bearer <token>` en cada request.

## C

- **Contract testing**: pruebas que verifican que consumidor y proveedor de un API cumplen el mismo contrato; Pact lo hace orientado al consumidor.

## G

- **gather (asyncio)**: ejecuta varias corutinas a la vez y devuelve sus resultados en orden; si una falla, propaga la excepción.

## H

- **handler (MockTransport)**: función que recibe un `httpx2.Request` y devuelve un `httpx2.Response` o lanza una excepción de transporte.
- **httpx2**: cliente HTTP síncrono y asíncrono para Python, continuación de `httpx`, mantenido por el equipo de Pydantic.

## M

- **MockTransport**: transporte de `httpx2` que responde con un handler en lugar de usar la red.
- **model_validate**: método de `pydantic` que convierte un diccionario en un modelo o lanza `ValidationError`.

## P

- **Pact**: herramienta de contract testing orientado al consumidor; genera un contrato desde los tests del consumidor y lo verifica contra el proveedor.
- **pytest-asyncio**: plugin que permite ejecutar tests y fixtures `async def` en pytest.

## R

- **responses**: librería que intercepta las llamadas de `requests` en los tests.

## T

- **TimeoutException**: clase base de `ConnectTimeout`, `ReadTimeout`, `WriteTimeout` y `PoolTimeout` en `httpx2`.
- **Transporte**: capa de `httpx2` que envía la request y devuelve la respuesta; inyectarla permite testear el cliente sin red.

## V

- **ValidationError**: excepción de `pydantic` cuando los datos no cumplen el modelo (campo faltante, tipo incorrecto).
