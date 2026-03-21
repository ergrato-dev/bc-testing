import pytest

from user_status import build_user_status

# ============================================
# PASO 1: Crear fixture base
# ============================================
# @pytest.fixture
# def sample_user():
#     return {"name": " Ada ", "is_active": True, "role": "mentor"}


# ============================================
# PASO 2: Reusar fixture
# ============================================
# def test_build_user_status_trims_name(sample_user):
#     result = build_user_status(sample_user)
#     assert result["name"] == "Ada"


# ============================================
# PASO 3: Reusar fixture en otro comportamiento
# ============================================
# def test_build_user_status_keeps_role(sample_user):
#     result = build_user_status(sample_user)
#     assert result["role"] == "mentor"


# ============================================
# PASO 4: Ruta de error
# ============================================
# def test_build_user_status_raises_error_when_name_is_missing():
#     with pytest.raises(ValueError, match="name is required"):
#         build_user_status({"is_active": True})
