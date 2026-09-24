import pytest

from session_registry import SessionRegistry, load_show_catalog

# ============================================
# PASO 1: Fixture con yield (setup + teardown)
# ============================================
# @pytest.fixture
# def registry():
#     # Setup: se ejecuta antes de cada test
#     registry = SessionRegistry()
#     registry.open()
#     yield registry
#     # Teardown: se ejecuta después de cada test, aunque el test falle
#     registry.close()
#
#
# def test_registry_has_no_sessions_when_just_opened(registry):
#     # Arrange: la fixture ya abrió el registro
#
#     # Act
#     names = registry.session_names()
#
#     # Assert
#     assert registry.is_open is True
#     assert names == []


# ============================================
# PASO 2: Una fixture que usa otra fixture
# ============================================
# @pytest.fixture
# def registry_with_show(registry):
#     registry.add_session("Sistema solar", seats=10)
#     return registry
#
#
# def test_reserve_reduces_available_seats_when_quantity_fits(registry_with_show):
#     # Arrange: la fixture ya creó la sesión con 10 asientos
#
#     # Act
#     registry_with_show.reserve("Sistema solar", 3)
#
#     # Assert
#     assert registry_with_show.available("Sistema solar") == 7


# ============================================
# PASO 3: Aislamiento del scope function
# ============================================
# def test_available_seats_start_full_when_fixture_is_function_scoped(registry_with_show):
#     # Arrange: cada test recibe un registro nuevo, sin las reservas de otros tests
#
#     # Act
#     seats = registry_with_show.available("Sistema solar")
#
#     # Assert
#     assert seats == 10


# ============================================
# PASO 4: Rutas de error con fixtures
# ============================================
# def test_reserve_raises_error_when_quantity_exceeds_seats(registry_with_show):
#     # Act + Assert
#     with pytest.raises(ValueError, match="not enough seats"):
#         registry_with_show.reserve("Sistema solar", 11)
#
#
# def test_add_session_raises_error_when_registry_is_closed(registry):
#     # Arrange
#     registry.close()
#
#     # Act + Assert
#     with pytest.raises(RuntimeError, match="registry is closed"):
#         registry.add_session("Auroras", seats=5)


# ============================================
# PASO 5: Fixture de scope module (solo lectura)
# ============================================
# @pytest.fixture(scope="module")
# def show_catalog():
#     # Se crea una sola vez para todo el archivo
#     return load_show_catalog()
#
#
# def test_catalog_lists_solar_system_seats_when_loaded(show_catalog):
#     # Act
#     seats = show_catalog["Sistema solar"]
#
#     # Assert
#     assert seats == 40
#
#
# def test_registry_contains_every_show_when_catalog_is_added(registry, show_catalog):
#     # Arrange
#     for name, seats in show_catalog.items():
#         registry.add_session(name, seats)
#
#     # Act
#     names = registry.session_names()
#
#     # Assert
#     assert names == ["Agujeros negros", "Auroras", "Sistema solar"]


# ============================================
# PASO 6: Fixture de scope class que usa una de scope module
# ============================================
# @pytest.fixture(scope="class")
# def largest_show(show_catalog):
#     # Una fixture puede usar otras de scope igual o más amplio, nunca más estrecho
#     return max(show_catalog, key=show_catalog.get)
#
#
# class TestLargestShow:
#     def test_largest_show_is_solar_system_when_catalog_is_loaded(self, largest_show):
#         # Assert
#         assert largest_show == "Sistema solar"
#
#     def test_largest_show_is_sold_out_when_all_seats_are_reserved(
#         self, registry, show_catalog, largest_show
#     ):
#         # Arrange
#         registry.add_session(largest_show, show_catalog[largest_show])
#
#         # Act
#         registry.reserve(largest_show, show_catalog[largest_show])
#
#         # Assert
#         assert registry.available(largest_show) == 0
