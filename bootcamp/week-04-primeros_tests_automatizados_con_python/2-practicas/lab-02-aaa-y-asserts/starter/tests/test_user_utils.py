import pytest

from src.user_utils import (
    calculate_discount,
    create_user,
    find_user_by_email,
    is_adult,
    is_valid_email,
)

# ============================================
# PASO 1: tests de is_adult (descomenta)
# ============================================
# def test_returns_true_when_age_is_18() -> None:
#     # Arrange
#     age = 18
#
#     # Act
#     result = is_adult(age)
#
#     # Assert
#     assert result is True
#
#
# def test_returns_false_when_age_is_17() -> None:
#     # Arrange
#     age = 17
#
#     # Act
#     result = is_adult(age)
#
#     # Assert
#     assert result is False


# ============================================
# PASO 2: tests de calculate_discount (descomenta)
# ============================================
# def test_returns_80_when_price_is_100_and_percent_is_20() -> None:
#     # Arrange
#     price = 100
#     percent = 20
#
#     # Act
#     result = calculate_discount(price, percent)
#
#     # Assert
#     assert result == 80
#
#
# def test_raises_value_error_when_percent_is_greater_than_100() -> None:
#     # Arrange
#     price = 100
#     percent = 120
#
#     # Act + Assert
#     with pytest.raises(ValueError, match="Invalid percent"):
#         calculate_discount(price, percent)


# ============================================
# PASO 3: tests de is_valid_email (descomenta)
# ============================================
# def test_returns_true_when_email_has_an_at_sign_and_a_dot() -> None:
#     # Arrange
#     email = "ana@example.com"
#
#     # Act
#     result = is_valid_email(email)
#
#     # Assert
#     assert result is True
#
#
# def test_returns_false_when_email_has_no_at_sign() -> None:
#     # Arrange
#     email = "anaexample.com"
#
#     # Act
#     result = is_valid_email(email)
#
#     # Assert
#     assert result is False


# ============================================
# PASO 4: create_user con == (escríbelo tú)
# ============================================
# TODO: un test que llame a create_user("  Ana Pérez ", "ANA@Example.com")
# y compruebe con == el diccionario completo que devuelve.


# ============================================
# PASO 5: find_user_by_email con == e is None (escríbelos tú)
# ============================================
# TODO: un test que busque un email que está en la lista (==).
# TODO: un test que busque un email que no está (is None).


# ============================================
# PASO 6: calculate_discount con decimales (escríbelo tú)
# ============================================
# TODO: un test con precio 29.90 y 10 % que use pytest.approx.
