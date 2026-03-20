def create_item(payload: dict) -> dict:
    name = payload.get("name", "").strip() if isinstance(payload, dict) else ""
    quantity = payload.get("quantity") if isinstance(payload, dict) else None

    if not name:
        raise ValueError("name is required")

    if not isinstance(quantity, int) or quantity < 0:
        raise ValueError("quantity must be a non-negative integer")

    return {
        "name": name,
        "quantity": quantity,
        "status": "available" if quantity > 0 else "empty",
    }
