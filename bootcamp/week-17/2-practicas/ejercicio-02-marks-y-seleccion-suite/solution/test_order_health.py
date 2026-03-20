import pytest

from order_health import is_order_valid


@pytest.mark.smoke
def test_is_order_valid_returns_true_for_valid_order():
    assert is_order_valid(total=50, has_items=True) is True


@pytest.mark.regression
@pytest.mark.parametrize(
    "total,has_items,expected",
    [
        (10, True, True),
        (-1, True, False),
        (10, False, False),
    ],
    ids=["valid", "negative-total", "missing-items"],
)
def test_is_order_valid_behaviour_matrix(total, has_items, expected):
    assert is_order_valid(total=total, has_items=has_items) is expected
