from src.math_utils import add, is_even


# Test activo que falla por diseño: add tiene un bug en src/math_utils.py.
# Lee el informe y corrígelo en el PASO 1 (src/math_utils.py).
def test_returns_5_when_adding_2_and_3() -> None:
    # Arrange
    a = 2
    b = 3

    # Act
    result = add(a, b)

    # Assert
    assert result == 5


# ============================================
# PASO 2: Test booleano simple
# ============================================
# Descomenta las siguientes líneas:
# def test_returns_true_when_number_is_even() -> None:
#     # Arrange
#     number = 10
#
#     # Act
#     result = is_even(number)
#
#     # Assert
#     assert result


# ============================================
# PASO 3: Test para número impar
# ============================================
# Descomenta las siguientes líneas:
# def test_returns_false_when_number_is_odd() -> None:
#     # Arrange
#     number = 7
#
#     # Act
#     result = is_even(number)
#
#     # Assert
#     assert not result
