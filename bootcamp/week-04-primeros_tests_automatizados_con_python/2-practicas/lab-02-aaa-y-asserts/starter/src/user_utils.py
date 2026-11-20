def is_adult(age: int) -> bool:
    return age >= 18


def calculate_discount(price: float, percent: int) -> float:
    if percent < 0 or percent > 100:
        raise ValueError("Invalid percent")
    return price - (price * percent) / 100


def is_valid_email(email: str) -> bool:
    return "@" in email and "." in email


def create_user(name: str, email: str) -> dict:
    return {
        "name": name.strip(),
        "email": email.lower(),
        "active": True,
    }


def find_user_by_email(users: list[dict], email: str) -> dict | None:
    for user in users:
        if user["email"] == email:
            return user
    return None
