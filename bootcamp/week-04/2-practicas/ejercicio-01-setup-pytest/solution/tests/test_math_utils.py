from src.math_utils import add, is_even


def test_add_returns_five_when_inputs_are_two_and_three() -> None:
    # Arrange
    a = 2
    b = 3

    # Act
    result = add(a, b)

    # Assert
    assert result == 5


def test_is_even_returns_true_for_even_number() -> None:
    # Arrange
    value = 10

    # Act
    result = is_even(value)

    # Assert
    assert result is True


def test_is_even_returns_false_for_odd_number() -> None:
    # Arrange
    value = 7

    # Act
    result = is_even(value)

    # Assert
    assert result is False
