from src.user_utils import is_adult, calculate_discount, is_valid_email
import pytest


# ============================================
# PASO 1: Tests de is_adult
# ============================================
# Descomenta las siguientes lineas:
# def test_is_adult_returns_true_when_age_is_18() -> None:
#     result = is_adult(18)
#     assert result is True

# def test_is_adult_returns_false_when_age_is_17() -> None:
#     result = is_adult(17)
#     assert result is False


# ============================================
# PASO 2: Tests de calculate_discount
# ============================================
# Descomenta las siguientes lineas:
# def test_calculate_discount_returns_80_when_price_100_and_percent_20() -> None:
#     result = calculate_discount(100, 20)
#     assert result == 80

# def test_calculate_discount_raises_error_when_percent_is_invalid() -> None:
#     with pytest.raises(ValueError, match="Invalid percent"):
#         calculate_discount(100, 120)


# ============================================
# PASO 3: Tests de is_valid_email
# ============================================
# Descomenta las siguientes lineas:
# def test_is_valid_email_returns_true_when_email_has_valid_format() -> None:
#     result = is_valid_email("ana@example.com")
#     assert result is True

# def test_is_valid_email_returns_false_when_email_is_invalid() -> None:
#     result = is_valid_email("anaexamplecom")
#     assert result is False
