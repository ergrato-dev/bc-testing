import pytest
from hypothesis import given
from hypothesis import strategies as st

from roman import to_roman

# ============================================
# PASO 1: Red - el caso más simple
# ============================================
def test_to_roman_returns_i_when_number_is_1() -> None:
    assert to_roman(1) == "I"


# ============================================
# PASO 2: Red - repetir un símbolo
# ============================================
def test_to_roman_repeats_i_when_number_is_3() -> None:
    assert to_roman(3) == "III"


# ============================================
# PASO 3: Red - un símbolo nuevo
# ============================================
def test_to_roman_returns_v_when_number_is_5() -> None:
    assert to_roman(5) == "V"


# ============================================
# PASO 4: Red - notación sustractiva
# ============================================
def test_to_roman_uses_subtractive_notation_when_number_is_4() -> None:
    assert to_roman(4) == "IV"


# ============================================
# PASO 5: Red - el resto de símbolos
# ============================================
@pytest.mark.parametrize(
    ("number", "expected"),
    [
        (9, "IX"),
        (14, "XIV"),
        (40, "XL"),
        (90, "XC"),
        (400, "CD"),
        (1994, "MCMXCIV"),
        (2026, "MMXXVI"),
        (3999, "MMMCMXCIX"),
    ],
)
def test_to_roman_converts_number_when_it_needs_several_symbols(number: int, expected: str) -> None:
    assert to_roman(number) == expected


# ============================================
# PASO 6: Red - fuera de rango
# ============================================
@pytest.mark.parametrize("number", [0, -1, 4000])
def test_to_roman_raises_value_error_when_number_is_out_of_range(number: int) -> None:
    with pytest.raises(ValueError, match="between 1 and 3999"):
        to_roman(number)


# ============================================
# PASO 7: Propiedad - ningún símbolo se repite 4 veces
# ============================================
@given(st.integers(min_value=1, max_value=3999))
def test_to_roman_never_repeats_a_symbol_four_times_for_any_valid_number(number: int) -> None:
    roman = to_roman(number)

    repeated_four_times = [symbol for symbol in "IXCM" if symbol * 4 in roman]
    assert set(roman) <= set("IVXLCDM")
    assert repeated_four_times == []
