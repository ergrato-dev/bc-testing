# Carrito de la tienda del planetario.


def cart_total(items: list[dict]) -> float:
    return sum(item["price"] * item["quantity"] for item in items)


def apply_coupon(total: float, code: str) -> float:
    if code == "PLANETA10":
        return total * 0.9
    raise ValueError("Invalid coupon")


def shipping_cost(total: float) -> float:
    return 0 if total >= 30 else 3.9
