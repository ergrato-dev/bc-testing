from unittest.mock import patch

from order_service import confirm_order

# ============================================
# PASO 1: Patch en namespace de uso
# ============================================
# @patch("order_service.PaymentGateway")
# def test_confirm_order_returns_confirmed_when_gateway_approves(gateway_cls):
#     gateway_instance = gateway_cls.return_value
#     gateway_instance.charge.return_value = {"status": "approved"}
#
#     result = confirm_order("ORD-101", 150)
#
#     assert result == "confirmed"
#     gateway_instance.charge.assert_called_once_with("ORD-101", 150)


# ============================================
# PASO 2: Simular error de infraestructura
# ============================================
# @patch("order_service.PaymentGateway")
# def test_confirm_order_propagates_timeout_error(gateway_cls):
#     gateway_instance = gateway_cls.return_value
#     gateway_instance.charge.side_effect = TimeoutError("gateway timeout")
#
#     try:
#         confirm_order("ORD-102", 200)
#         assert False, "Expected TimeoutError"
#     except TimeoutError as error:
#         assert str(error) == "gateway timeout"
