import pytest

from order_health import is_order_valid


# ============================================
# PASO 2: Mark smoke
# ============================================
# @pytest.mark.smoke
# def test_is_order_valid_returns_true_when_order_has_items_and_positive_total():
#     # Arrange
#     total, has_items = 50, True

#     # Act
#     result = is_order_valid(total=total, has_items=has_items)

#     # Assert
#     assert result is True


# ============================================
# PASO 3: Mark regression + pytest.param con id y marks
# ============================================
# @pytest.mark.regression
# @pytest.mark.parametrize(
#     "total,has_items,expected",
#     [
#         pytest.param(10, True, True, id="valid"),
#         pytest.param(-1, True, False, id="negative-total"),
#         pytest.param(10, False, False, id="missing-items"),
#         pytest.param(
#             0,
#             True,
#             False,
#             id="zero-total",
#             marks=pytest.mark.xfail(
#                 reason="bug conocido: un pedido con total 0 se acepta", strict=True
#             ),
#         ),
#     ],
# )
# def test_is_order_valid_returns_expected_when_order_data_varies(
#     total, has_items, expected
# ):
#     # Arrange: los datos llegan desde parametrize

#     # Act
#     result = is_order_valid(total=total, has_items=has_items)

#     # Assert
#     assert result is expected


# ============================================
# PASO 4: Mark slow
# ============================================
# @pytest.mark.slow
# @pytest.mark.regression
# def test_is_order_valid_accepts_every_order_when_batch_is_large():
#     # Arrange: lote grande que encarece la ejecución
#     totals = range(1, 5_000_001)

#     # Act
#     all_valid = all(is_order_valid(total=total, has_items=True) for total in totals)

#     # Assert
#     assert all_valid is True
