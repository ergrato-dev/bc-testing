from src.cart import apply_coupon, cart_total, shipping_cost

# Los cinco tests pasan. Ninguno prueba lo que dice su nombre.


def test_returns_25_when_cart_has_a_10_euro_item_and_a_15_euro_item() -> None:
    items = [
        {"price": 10, "quantity": 1},
        {"price": 15, "quantity": 1},
    ]

    total = cart_total(items)

    total == 25


def test_returns_35_when_cart_has_two_10_euro_items_and_a_15_euro_item() -> None:
    items = [
        {"price": 10, "quantity": 2},
        {"price": 15, "quantity": 1},
    ]

    total = cart_total(items)

    assert total


def test_raises_value_error_when_coupon_is_unknown() -> None:
    try:
        apply_coupon(50, "FALSO")
    except ValueError as error:
        assert str(error) == "Invalid coupon"


def test_returns_45_when_coupon_planeta10_is_applied_to_50() -> None:
    expected = apply_coupon(50, "PLANETA10")

    result = apply_coupon(50, "PLANETA10")

    assert result == expected


def test_returns_free_shipping_when_total_is_30_or_more() -> None:
    result = shipping_cost(100)

    assert result == 0
