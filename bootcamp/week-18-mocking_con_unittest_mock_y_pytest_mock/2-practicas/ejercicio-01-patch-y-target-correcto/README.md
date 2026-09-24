# Ejercicio 01 - Patch y Target Correcto

## Objetivo

Aprender a parchear donde el símbolo se **usa** (no donde se define), comprobar qué pasa cuando el target es incorrecto y proteger el contrato del doble con `autospec=True`.

## Tiempo estimado

90 minutos.

## Paso a paso

Abre `starter/order_service.py` y observa cómo se importa la dependencia:

```python
from gateways import PaymentGateway
```

`order_service` guarda su propia referencia a `PaymentGateway`. El `PaymentGateway` real de `starter/gateways.py` simula una llamada HTTP que falla con `ConnectionError`: si un test lo ejecuta, sabrás que el patch no surtió efecto.

### Paso 1: Patch en el namespace donde se usa

Abre `starter/test_order_service.py` y descomenta el PASO 1. El target es `order_service.PaymentGateway`, el nombre que busca `confirm_order` en tiempo de ejecución. `gateway_cls.return_value` es la instancia que crea `PaymentGateway()` dentro del servicio.

### Paso 2: Patch en el target incorrecto

Descomenta el PASO 2. Parchea `gateways.PaymentGateway`, el módulo donde se **define** la clase. Si escribieras este test esperando `"confirmed"`, obtendrías esta salida real:

```text
self = <gateways.PaymentGateway object at 0x7963fd1e5e80>, order_id = 'ORD-100'
amount = 150

    def charge(self, order_id: str, amount: float) -> dict:
>       raise ConnectionError(f"POST https://payments.example/charges/{order_id}: sin red en los tests")
E       ConnectionError: POST https://payments.example/charges/ORD-100: sin red en los tests

gateways.py:5: ConnectionError
=========================== short test summary info ============================
FAILED test_order_service.py::test_confirm_order_returns_confirmed_when_gateway_approves - ConnectionError: POST https://payments.example/charges/ORD-100: sin red en los tests
1 failed in 0.01s
```

`self` es un `gateways.PaymentGateway` real, no un `MagicMock`. El test del PASO 2 documenta ese comportamiento: espera el `ConnectionError` del gateway real y verifica con `gateway_cls.assert_not_called()` que el mock nunca se usó. Compáralo con el PASO 1: la única diferencia es el string del target.

### Paso 3: Simular un error de infraestructura

Descomenta el PASO 3. `side_effect = TimeoutError(...)` hace que `charge` lance la excepción y `pytest.raises(..., match=...)` verifica tipo y mensaje.

### Paso 4: `autospec=True` para respetar la firma real

Descomenta el PASO 4. Con `autospec=True`, el mock copia la firma de `PaymentGateway.charge`. Para ver el beneficio, cambia temporalmente la firma real en `starter/gateways.py`:

```python
def charge(self, order_id: str, amount: float, currency: str) -> dict:
```

Y ejecuta `uv run pytest -q --tb=no -rfp -k "not definition_module"`. Salida real:

```text
..F                                                                      [100%]
=========================== short test summary info ============================
FAILED test_order_service.py::test_confirm_order_returns_rejected_when_autospec_gateway_declines - TypeError: missing a required argument: 'currency'
PASSED test_order_service.py::test_confirm_order_returns_confirmed_when_gateway_approves
PASSED test_order_service.py::test_confirm_order_propagates_timeout_error_when_gateway_times_out
1 failed, 2 passed, 1 deselected in 0.01s
```

Los tests sin `autospec` siguen en verde aunque el servicio ya no respeta la API real (falso positivo). Solo el test con `autospec` detecta el cambio de contrato. Deshaz el cambio en `gateways.py` antes de continuar.

### Paso 5: Revisar la solución

Compara con `solution/test_order_service.py`.

## Comandos sugeridos

```bash
cd starter
uv sync
uv run pytest -q
```
