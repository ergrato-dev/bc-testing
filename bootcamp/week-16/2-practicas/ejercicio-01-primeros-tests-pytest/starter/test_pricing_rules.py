import pytest

from pricing_rules import calculate_total

# ============================================
# PASO 1: Caso base
# ============================================
# def test_calculate_total_returns_same_price_when_customer_is_not_premium():
#     # Arrange
#     price = 100
#
#     # Act
#     result = calculate_total(price=price, is_premium=False)
#
#     # Assert
#     assert result == 100


# ============================================
# PASO 2: Regla premium
# ============================================
# def test_calculate_total_applies_discount_when_customer_is_premium():
#     # Arrange
#     price = 100
#
#     # Act
#     result = calculate_total(price=price, is_premium=True)
#
#     # Assert
#     assert result == 90


# ============================================
# PASO 3: Error esperado
# ============================================
# def test_calculate_total_raises_error_when_price_is_invalid():
#     with pytest.raises(ValueError, match="price must be greater than zero"):
#         calculate_total(price=0, is_premium=False)
