from amount_service import is_valid_amount


def test_is_valid_amount_returns_true_when_amount_is_positive() -> None:
    # Arrange
    amount = 10

    # Act
    result = is_valid_amount(amount)

    # Assert
    assert result is True


def test_is_valid_amount_returns_false_when_amount_is_negative() -> None:
    # Arrange
    amount = -1

    # Act
    result = is_valid_amount(amount)

    # Assert
    assert result is False


def test_is_valid_amount_returns_true_when_amount_is_zero() -> None:
    # Arrange
    amount = 0

    # Act
    result = is_valid_amount(amount)

    # Assert
    assert result is True
