def split_bill(total_cents: int, people: int) -> list[int]:
    """Reparte una cuenta en partes iguales para `people` personas."""
    share = total_cents / people
    return [share] * people
