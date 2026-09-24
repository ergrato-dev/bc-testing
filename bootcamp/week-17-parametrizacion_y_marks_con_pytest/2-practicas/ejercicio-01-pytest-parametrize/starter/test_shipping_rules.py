import pytest

from shipping_rules import calculate_shipping


# ============================================
# PASO 1: Parametrize base
# ============================================
# @pytest.mark.parametrize(
#     "total,expected",
#     [
#         (20, 9.99),
#         (70, 4.99),
#     ],
#     ids=["regular-shipping", "mid-tier-shipping"],
# )
# def test_calculate_shipping_returns_tier_cost_when_total_is_inside_tier(
#     total, expected
# ):
#     # Arrange: los datos llegan desde parametrize

#     # Act
#     result = calculate_shipping(total)

#     # Assert
#     assert result == expected


# ============================================
# PASO 2: Casos borde de ambos límites
# ============================================
# @pytest.mark.parametrize(
#     "total,expected",
#     [
#         (49.99, 9.99),
#         (50, 4.99),
#         (99.99, 4.99),
#         (100, 0),
#     ],
#     ids=[
#         "below-mid-tier-threshold",
#         "mid-tier-threshold",
#         "below-free-shipping-threshold",
#         "free-shipping-threshold",
#     ],
# )
# def test_calculate_shipping_changes_tier_when_total_crosses_threshold(
#     total, expected
# ):
#     # Arrange: los datos llegan desde parametrize

#     # Act
#     result = calculate_shipping(total)

#     # Assert
#     assert result == expected


# ============================================
# PASO 3: Error esperado parametrizado
# ============================================
# @pytest.mark.parametrize(
#     "invalid_total", [-1, -0.01], ids=["negative-int", "negative-float"]
# )
# def test_calculate_shipping_raises_value_error_when_total_is_negative(invalid_total):
#     # Arrange: el total inválido llega desde parametrize

#     # Act / Assert
#     with pytest.raises(ValueError, match="total must be non-negative"):
#         calculate_shipping(invalid_total)
