# ============================================
# TEST SUITE heredada: pasa en verde, pero ¿protege las reglas?
# ============================================
# TODO: sube el branch coverage a 90% o más (fail_under ya está configurado)
# TODO: ejecuta mutmut, clasifica cada sobreviviente y mata los matables
# TODO: refactoriza shipping_class hasta que `ruff check src` pase, con la suite en verde

import pytest

from inventory.rules import restock_cost, shipping_class, stock_status


def test_stock_status_returns_ok_when_quantity_is_high() -> None:
    assert stock_status(40, reorder_level=10) == "ok"


def test_stock_status_raises_value_error_when_quantity_is_negative() -> None:
    with pytest.raises(ValueError):
        stock_status(-1, reorder_level=10)


def test_restock_cost_returns_a_cost_when_units_are_positive() -> None:
    assert restock_cost(10.0, units=5) > 0


def test_shipping_class_returns_a_class_when_order_is_local() -> None:
    assert shipping_class(2.0, "CO", express=True, fragile=False).startswith("co")
