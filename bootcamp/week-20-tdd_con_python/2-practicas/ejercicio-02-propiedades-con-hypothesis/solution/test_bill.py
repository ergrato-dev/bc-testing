import pytest
from hypothesis import example, given
from hypothesis import strategies as st

from bill import split_bill

# ============================================
# PASO 1: Tests basados en ejemplos
# ============================================
@pytest.mark.parametrize(
    ("total_cents", "people", "expected"),
    [
        (1000, 4, [250, 250, 250, 250]),
        (900, 3, [300, 300, 300]),
        (500, 1, [500]),
    ],
)
def test_split_bill_returns_equal_shares_when_total_divides_evenly(
    total_cents: int, people: int, expected: list[int]
) -> None:
    assert split_bill(total_cents, people) == expected


# ============================================
# PASO 2: Propiedad - no se pierde ni se inventa dinero
# ============================================
@given(
    total_cents=st.integers(min_value=0, max_value=1_000_000),
    people=st.integers(min_value=1, max_value=50),
)
def test_split_bill_shares_add_up_to_total_for_any_bill(total_cents: int, people: int) -> None:
    shares = split_bill(total_cents, people)

    assert sum(shares) == total_cents


# ============================================
# PASO 3: Propiedad - reparto justo
# ============================================
@given(
    total_cents=st.integers(min_value=0, max_value=1_000_000),
    people=st.integers(min_value=1, max_value=50),
)
@example(total_cents=0, people=3)
def test_split_bill_gives_one_share_per_person_differing_by_at_most_one_cent(total_cents: int, people: int) -> None:
    shares = split_bill(total_cents, people)

    assert len(shares) == people
    assert max(shares) - min(shares) <= 1


# ============================================
# PASO 4: Entrada inválida
# ============================================
def test_split_bill_raises_value_error_when_people_is_zero() -> None:
    with pytest.raises(ValueError, match="at least 1"):
        split_bill(1000, 0)
