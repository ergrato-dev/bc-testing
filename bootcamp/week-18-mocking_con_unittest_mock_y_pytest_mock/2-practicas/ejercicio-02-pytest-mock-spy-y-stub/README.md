# Ejercicio 02 - pytest-mock: Stub, Mock y Spy

## Objetivo

Usar el fixture `mocker` para distinguir en la práctica los tres dobles de la semana, verificar llamadas con `assert_not_called`, `call_args` y `call_args_list`, y decidir cuándo basta `monkeypatch`.

| Doble | Qué hace | Qué verifica el test |
|---|---|---|
| Stub | Devuelve respuestas predefinidas (`return_value`, `side_effect`) | Solo el resultado observable |
| Mock | Devuelve respuestas y registra llamadas | La interacción (`assert_called_once_with`, `assert_not_called`, `call_args_list`) |
| Spy | Envuelve la implementación real (`mocker.spy`) | Que se llamó, sin cambiar el comportamiento |

## Tiempo estimado

90 minutos.

## Paso a paso

Abre `starter/invoice_service.py`. `send_invoices` calcula el total de cada factura con un `TaxCalculator` y la envía con un `InvoiceMailer` si el total es mayor que cero. Ambos colaboradores se inyectan. `build_client_label` lee el prefijo de la variable de entorno `CLIENT_LABEL_PREFIX`.

### Paso 1: Stub

Abre `starter/test_invoice_service.py` y descomenta el PASO 1. `mocker.create_autospec(TaxCalculator, instance=True)` crea un doble con la misma firma que la clase real; `return_value` fija la respuesta. El test solo comprueba el total: no verifica cuántas veces se llamó a `calculate`, porque eso no forma parte del comportamiento que se prueba.

### Paso 2: Mock

Descomenta el PASO 2. Enviar la factura es un efecto secundario sin valor de retorno, así que la única forma de comprobarlo es verificar la interacción con `mailer_mock.send.assert_called_once_with(...)`. El calculador sigue siendo un stub.

### Paso 3: `assert_not_called`

Descomenta el PASO 3. Con un total de cero no debe enviarse nada: `assert_not_called()` falla si hubo cualquier llamada.

### Paso 4: `call_args_list` y `call_args`

Descomenta el PASO 4. `side_effect` con una lista devuelve un valor distinto en cada llamada. `call_args_list` guarda todas las llamadas (se comparan con `call(...)`) y `call_args` solo la última.

### Paso 5: Spy

Descomenta el PASO 5. `mocker.spy` ejecuta la función real `normalize_client_name` y además registra la llamada; `spy_return` guarda lo que devolvió. Se espía `invoice_service.normalize_client_name` porque ese es el nombre que usa `build_client_label`.

### Paso 6: `monkeypatch` para variables de entorno

Descomenta el PASO 6. `monkeypatch.setenv` cambia la variable solo durante el test. No se necesita `mocker.patch` porque no hay llamadas que verificar: solo se reemplaza un valor.

### Paso 7: Revisar la solución

Compara con `solution/test_invoice_service.py`.

## Comandos sugeridos

```bash
cd starter
uv sync
uv run pytest -q
uv run pytest -k send_invoices
```
