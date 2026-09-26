# ============================================
# TEST SUITE: ItemClient (Python)
# ============================================

# NOTA PARA EL APRENDIZ:
# Adapta item_client.py y esta suite a tu dominio asignado.
# Ningún test debe salir a la red: crea el cliente con
# transport=httpx2.MockTransport(handler), donde handler recibe un httpx2.Request
# y devuelve un httpx2.Response (o lanza httpx2.ReadTimeout para simular un timeout).
#
# TODO: sustituye cada `pytest.skip` por un test real con Arrange / Act / Assert
#       y nombres con el patrón test_[context]_[expected]_when_[condition].

import pytest

# TODO: importa httpx2 y lo que necesites de item_client

# TODO: fixture que registre las requests enviadas y fixture/factory que cree
#       el ItemClient con un MockTransport


# ============================================
# BLOQUE 1: Happy path y request enviada
# ============================================
# TODO: 1. get_item devuelve el modelo cuando el API responde 200
# TODO: 2. list_items envía el query param min_quantity
# TODO: 3. create_item envía el JSON correcto y el header Authorization


def test_get_item_happy_path() -> None:
    pytest.skip("TODO: get_item con respuesta 200")


# ============================================
# BLOQUE 2: Errores HTTP
# ============================================
# TODO: 1. 404 -> ItemNotFoundError
# TODO: 2. 422 -> ItemRejectedError con el mensaje del API
# TODO: 3. 5xx con cuerpo HTML -> ItemApiUnavailableError
# TODO: 4. Timeout -> ItemApiUnavailableError


def test_get_item_not_found() -> None:
    pytest.skip("TODO: get_item con respuesta 404")


# ============================================
# BLOQUE 3: Contrato
# ============================================
# TODO: 1. Payload sin un campo obligatorio -> pydantic.ValidationError
# TODO: 2. Campo con tipo incorrecto -> pydantic.ValidationError


def test_get_item_contract_broken() -> None:
    pytest.skip("TODO: payload que no cumple el modelo")
