import invoice_service
from invoice_service import TaxCalculator, build_client_label, build_invoice_total


def test_build_invoice_total_uses_stubbed_tax_calculation(mocker):
    calculator_stub = mocker.Mock(spec=TaxCalculator)
    calculator_stub.calculate.return_value = 12.0

    result = build_invoice_total(100.0, calculator_stub)

    assert result == 112.0
    calculator_stub.calculate.assert_called_once_with(100.0)


def test_build_client_label_calls_normalize_function(mocker):
    normalize_spy = mocker.spy(invoice_service, "normalize_client_name")

    result = build_client_label("  ANA   PEREZ  ")

    assert result == "CLIENT::ana perez"
    assert normalize_spy.call_count == 1
    normalize_spy.assert_called_once_with("  ANA   PEREZ  ")
