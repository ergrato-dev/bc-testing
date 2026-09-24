# ============================================
# TEST SUITE: ItemService (Python)
# ============================================
# TODO: crea `item_service.py` con la lógica de tu dominio e impórtalo aquí.
# TODO: sustituye cada `pytest.skip` por un test AAA real (mismos TC que en test-plan.md).
import pytest


def test_create_item_when_payload_is_valid() -> None:  # TC-001
    pytest.skip("TODO: happy path")


def test_create_item_raises_validation_error_when_name_is_empty() -> None:  # TC-002
    pytest.skip("TODO: validación de nombre requerido")


def test_create_item_raises_validation_error_when_amount_is_negative() -> None:  # TC-003
    pytest.skip("TODO: validación de monto")
