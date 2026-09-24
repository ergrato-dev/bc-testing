from src.math_utils import add, is_even


# Test activo que falla por diseño: add tiene un bug en src/math_utils.py.
# Lee el mensaje de error y corrígelo en el PASO 1 (src/math_utils.py).
def test_add_returns_five_when_inputs_are_two_and_three() -> None:
    # Arrange
    a = 2
    b = 3

    # Act
    result = add(a, b)

    # Assert
    assert result == 5


# ============================================
# PASO 2: Test booleano para número par
# ============================================
# Descomenta las siguientes líneas:
# def test_is_even_returns_true_when_number_is_even() -> None:
#     # Arrange
#     value = 10
#
#     # Act
#     result = is_even(value)
#
#     # Assert
#     assert result is True


# ============================================
# PASO 3: Test booleano para número impar
# ============================================
# Descomenta las siguientes líneas:
# def test_is_even_returns_false_when_number_is_odd() -> None:
#     # Arrange
#     value = 7
#
#     # Act
#     result = is_even(value)
#
#     # Assert
#     assert result is False
