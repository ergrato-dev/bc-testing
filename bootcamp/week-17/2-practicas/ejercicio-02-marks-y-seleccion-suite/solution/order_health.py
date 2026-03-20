def is_order_valid(total: float, has_items: bool) -> bool:
    if total < 0:
        return False

    if not has_items:
        return False

    return True
