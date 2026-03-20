def evaluate_item_policy(quantity: int, is_blocked: bool) -> str:
    if quantity < 0:
        raise ValueError("quantity must be non-negative")

    if is_blocked:
        return "blocked"

    if quantity == 0:
        return "empty"

    return "available"
