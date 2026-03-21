from src.user_utils import is_adult, calculate_discount, is_valid_email
import pytest


def test_is_adult_returns_true_when_age_is_18() -> None:
    # Arrange
    age = 18

    # Act
    result = is_adult(age)

    # Assert
    assert result is True


def test_is_adult_returns_false_when_age_is_17() -> None:
    # Arrange
    age = 17

    # Act
    result = is_adult(age)

    # Assert
    assert result is False


def test_calculate_discount_returns_80_when_price_100_and_percent_20() -> None:
    # Arrange
    price = 100
    percent = 20

    # Act
    result = calculate_discount(price, percent)

    # Assert
    assert result == 80


def test_calculate_discount_raises_error_when_percent_is_invalid() -> None:
    # Arrange
    price = 100
    invalid_percent = 120

    # Act + Assert
    with pytest.raises(ValueError, match="Invalid percent"):
        calculate_discount(price, invalid_percent)


def test_is_valid_email_returns_true_when_email_has_valid_format() -> None:
    # Arrange
    email = "ana@example.com"

    # Act
    result = is_valid_email(email)

    # Assert
    assert result is True


def test_is_valid_email_returns_false_when_email_is_invalid() -> None:
    # Arrange
    email = "anaexamplecom"

    # Act
    result = is_valid_email(email)

    # Assert
    assert result is False
