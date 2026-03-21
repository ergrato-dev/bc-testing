def calculate_total(price: float, is_premium: bool) -> float:
    if price <= 0:
        raise ValueError("price must be greater than zero")

    if is_premium:
        return round(price * 0.9, 2)

    return round(price, 2)
