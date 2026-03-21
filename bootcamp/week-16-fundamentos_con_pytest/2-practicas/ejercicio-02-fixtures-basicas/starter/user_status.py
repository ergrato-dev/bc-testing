def build_user_status(user: dict) -> dict:
    if "name" not in user or not user["name"]:
        raise ValueError("name is required")

    return {
        "name": user["name"].strip(),
        "is_active": bool(user.get("is_active", False)),
        "role": user.get("role", "student"),
    }
