import pytest

from shipping_rules import calculate_shipping


@pytest.mark.parametrize(
    "total,expected",
    [
        (20, 9.99),
        (70, 4.99),
    ],
    ids=["regular-shipping", "mid-tier-shipping"],
)
def test_calculate_shipping_returns_expected_value(total, expected):
    assert calculate_shipping(total) == expected


@pytest.mark.parametrize(
    "total,expected",
    [
        (99.99, 4.99),
        (100, 0),
    ],
    ids=["below-free-shipping-threshold", "free-shipping-threshold"],
)
def test_calculate_shipping_handles_free_shipping_boundary(total, expected):
    assert calculate_shipping(total) == expected


@pytest.mark.parametrize(
    "invalid_total", [-1, -0.01], ids=["negative-int", "negative-float"]
)
def test_calculate_shipping_raises_error_when_total_is_negative(invalid_total):
    with pytest.raises(ValueError, match="total must be non-negative"):
        calculate_shipping(invalid_total)
