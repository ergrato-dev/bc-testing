"""Reglas de préstamo de la biblioteca."""

MAX_RENEWALS = 2


def can_renew(renewals: int, reserved_by_other: bool) -> bool:
    """Un préstamo se renueva si nadie más reservó el libro y no se pasó del máximo."""
    if renewals < 0:
        raise ValueError("renewals must not be negative")
    if reserved_by_other:
        return False
    return renewals < MAX_RENEWALS


def loan_days(member_type: str) -> int:
    """Días de préstamo según el tipo de socio."""
    if member_type == "student":
        return 14
    if member_type == "teacher":
        return 30
    if member_type == "visitor":
        return 7
    raise ValueError(f"unknown member type: {member_type}")
