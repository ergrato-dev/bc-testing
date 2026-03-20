def calculate_shipping(total: float) -> float:
    if total < 0:
        raise ValueError("total must be non-negative")

    if total >= 100:
        return 0

    if total >= 50:
        return 4.99

    return 9.99
