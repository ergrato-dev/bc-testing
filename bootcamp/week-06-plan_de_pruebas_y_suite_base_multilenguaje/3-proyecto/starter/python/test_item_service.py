# ============================================
# TEST SUITE: ItemService (Python)
# ============================================
# TODO: crea `item_service.py` con la logica de tu dominio e impórtalo aqui.
# TODO: sustituye cada `pytest.skip` por un test AAA real (mismos TC que en test-plan.md).
import pytest


class TestItemService:
    # TODO: Configurar setup comun para los tests (fixture o setup_method)

    def test_create_item_when_payload_is_valid(self):  # TC-001
        pytest.skip("TODO: happy path")

    def test_create_item_raises_validation_error_when_name_is_empty(self):  # TC-002
        pytest.skip("TODO: validacion de nombre requerido")

    def test_create_item_raises_validation_error_when_amount_is_negative(self):  # TC-003
        pytest.skip("TODO: validacion de monto")
