def split_bill(total_cents: int, people: int) -> list[int]:
    """Reparte una cuenta en partes iguales para `people` personas.

    Si no se puede repartir exacto, los primeros pagan un centavo más.
    """
    if people < 1:
        raise ValueError("people must be at least 1")
    share, remainder = divmod(total_cents, people)
    return [share + 1] * remainder + [share] * (people - remainder)
