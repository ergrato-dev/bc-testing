import pytest

from loyalty.points import points_for


# ============================================
# Suite heredada: 100% de branch coverage.
# ¿Detecta cambios en la lógica? mutmut lo dirá.
# ============================================
def test_points_for_returns_zero_when_amount_is_zero() -> None:
    assert points_for(0, is_member=False) == 0


def test_points_for_gives_points_when_customer_buys() -> None:
    assert points_for(55, is_member=False) > 0


def test_points_for_gives_more_points_when_member_buys_a_lot() -> None:
    assert points_for(250, is_member=True) > points_for(250, is_member=False)


def test_points_for_gives_no_bonus_when_member_buys_little() -> None:
    assert points_for(40, is_member=True) == points_for(40, is_member=False)


# ============================================
# PASO 1: Valores exactos en lugar de `> 0`
# ============================================
# Descomenta las siguientes líneas:
# @pytest.mark.parametrize(("amount", "expected"), [(55, 5), (250, 25)])
# def test_points_for_gives_one_point_per_10_when_customer_is_not_a_member(amount: float, expected: int) -> None:
#     assert points_for(amount, is_member=False) == expected


# ============================================
# PASO 2: El bono con su valor exacto
# ============================================
# Descomenta las siguientes líneas:
# def test_points_for_doubles_points_when_member_buys_250() -> None:
#     assert points_for(250, is_member=True) == 50


# ============================================
# PASO 3: La frontera del bono
# ============================================
# Descomenta las siguientes líneas:
# def test_points_for_doubles_points_when_member_buys_exactly_100() -> None:
#     assert points_for(100, is_member=True) == 20


# def test_points_for_gives_no_bonus_when_member_buys_just_below_100() -> None:
#     assert points_for(99.99, is_member=True) == 9
