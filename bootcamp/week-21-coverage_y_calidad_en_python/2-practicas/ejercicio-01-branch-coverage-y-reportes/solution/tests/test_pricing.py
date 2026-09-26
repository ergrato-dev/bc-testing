import pytest

from tickets.pricing import ticket_price


# ============================================
# PASO 1: Dos tests que ejecutan todas las líneas
# ============================================
def test_ticket_price_combines_child_and_member_discounts_when_member_is_a_child() -> None:
    assert ticket_price(8, is_member=True) == 5


def test_ticket_price_raises_value_error_when_age_is_negative() -> None:
    with pytest.raises(ValueError, match="age must be >= 0"):
        ticket_price(-1, is_member=False)


# ============================================
# PASO 2: Las ramas que faltaban
# ============================================
def test_ticket_price_returns_general_price_when_adult_is_not_a_member() -> None:
    assert ticket_price(30, is_member=False) == 20


def test_ticket_price_returns_child_price_when_child_is_not_a_member() -> None:
    assert ticket_price(8, is_member=False) == 10
