from unittest.mock import patch

import pytest

from order_service import confirm_order


@patch("order_service.PaymentGateway")
def test_confirm_order_returns_confirmed_when_gateway_approves(gateway_cls):
    # Arrange
    gateway_instance = gateway_cls.return_value
    gateway_instance.charge.return_value = {"status": "approved"}

    # Act
    result = confirm_order("ORD-101", 150)

    # Assert
    assert result == "confirmed"
    gateway_instance.charge.assert_called_once_with("ORD-101", 150)


@patch("gateways.PaymentGateway")
def test_confirm_order_calls_real_gateway_when_patch_targets_definition_module(gateway_cls):
    # Arrange: target INCORRECTO (donde se define, no donde se usa)
    gateway_cls.return_value.charge.return_value = {"status": "approved"}

    # Act / Assert: order_service conserva su referencia al PaymentGateway real
    with pytest.raises(ConnectionError, match="sin red en los tests"):
        confirm_order("ORD-100", 150)
    gateway_cls.assert_not_called()


@patch("order_service.PaymentGateway")
def test_confirm_order_propagates_timeout_error_when_gateway_times_out(gateway_cls):
    # Arrange
    gateway_instance = gateway_cls.return_value
    gateway_instance.charge.side_effect = TimeoutError("gateway timeout")

    # Act / Assert
    with pytest.raises(TimeoutError, match="gateway timeout"):
        confirm_order("ORD-102", 200)


@patch("order_service.PaymentGateway", autospec=True)
def test_confirm_order_returns_rejected_when_autospec_gateway_declines(gateway_cls):
    # Arrange: autospec copia la firma real de PaymentGateway
    gateway_instance = gateway_cls.return_value
    gateway_instance.charge.return_value = {"status": "declined"}

    # Act
    result = confirm_order("ORD-103", 80)

    # Assert
    assert result == "rejected"
    gateway_instance.charge.assert_called_once_with("ORD-103", 80)
