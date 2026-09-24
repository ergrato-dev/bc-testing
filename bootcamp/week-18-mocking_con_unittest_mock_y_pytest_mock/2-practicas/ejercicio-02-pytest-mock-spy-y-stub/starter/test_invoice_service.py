from unittest.mock import call

import invoice_service
from invoice_service import (
    InvoiceMailer,
    TaxCalculator,
    build_client_label,
    build_invoice_total,
    send_invoices,
)


# ============================================
# PASO 1: Stub (solo return_value, sin verificar interacción)
# ============================================
# def test_build_invoice_total_returns_subtotal_plus_tax_when_calculator_is_stubbed(mocker):
#     # Arrange: stub = solo respuestas predefinidas, sin verificar la interacción
#     calculator_stub = mocker.create_autospec(TaxCalculator, instance=True)
#     calculator_stub.calculate.return_value = 12.0
#
#     # Act
#     result = build_invoice_total(100.0, calculator_stub)
#
#     # Assert: solo el resultado observable
#     assert result == 112.0


# ============================================
# PASO 2: Mock (verificar la interacción con assert_called_once_with)
# ============================================
# def test_send_invoices_sends_total_once_when_invoice_has_positive_amount(mocker):
#     # Arrange
#     calculator_stub = mocker.create_autospec(TaxCalculator, instance=True)
#     calculator_stub.calculate.return_value = 16.0
#     mailer_mock = mocker.create_autospec(InvoiceMailer, instance=True)
#
#     # Act
#     sent = send_invoices({"INV-1": 100.0}, calculator_stub, mailer_mock)
#
#     # Assert: mock = verificamos la interacción
#     assert sent == 1
#     mailer_mock.send.assert_called_once_with("INV-1", 116.0)


# ============================================
# PASO 3: assert_not_called cuando no debe haber interacción
# ============================================
# def test_send_invoices_sends_nothing_when_all_totals_are_zero(mocker):
#     # Arrange
#     calculator_stub = mocker.create_autospec(TaxCalculator, instance=True)
#     calculator_stub.calculate.return_value = 0.0
#     mailer_mock = mocker.create_autospec(InvoiceMailer, instance=True)
#
#     # Act
#     sent = send_invoices({"INV-0": 0.0}, calculator_stub, mailer_mock)
#
#     # Assert
#     assert sent == 0
#     mailer_mock.send.assert_not_called()


# ============================================
# PASO 4: call_args_list y call_args para varias llamadas
# ============================================
# def test_send_invoices_sends_each_invoice_in_order_when_batch_has_several(mocker):
#     # Arrange
#     calculator_stub = mocker.create_autospec(TaxCalculator, instance=True)
#     calculator_stub.calculate.side_effect = [16.0, 32.0]
#     mailer_mock = mocker.create_autospec(InvoiceMailer, instance=True)
#
#     # Act
#     send_invoices({"INV-1": 100.0, "INV-2": 200.0}, calculator_stub, mailer_mock)
#
#     # Assert
#     assert mailer_mock.send.call_args_list == [call("INV-1", 116.0), call("INV-2", 232.0)]
#     assert mailer_mock.send.call_args.args == ("INV-2", 232.0)


# ============================================
# PASO 5: Spy sobre la implementación real
# ============================================
# def test_build_client_label_calls_real_normalize_function_when_spied(mocker):
#     # Arrange: spy = la implementación real se ejecuta y además se registra
#     normalize_spy = mocker.spy(invoice_service, "normalize_client_name")
#
#     # Act
#     result = build_client_label("  ANA   PEREZ  ")
#
#     # Assert
#     assert result == "CLIENT::ana perez"
#     normalize_spy.assert_called_once_with("  ANA   PEREZ  ")
#     assert normalize_spy.spy_return == "ana perez"


# ============================================
# PASO 6: monkeypatch para variables de entorno
# ============================================
# def test_build_client_label_uses_env_prefix_when_variable_is_set(monkeypatch):
#     # Arrange: monkeypatch basta para variables de entorno (no hay llamadas que verificar)
#     monkeypatch.setenv("CLIENT_LABEL_PREFIX", "MUSEO")
#
#     # Act
#     result = build_client_label("Ana Perez")
#
#     # Assert
#     assert result == "MUSEO::ana perez"
