# ============================================
# TEST SUITE: ItemService (TDD)
# ============================================

# NOTA PARA EL APRENDIZ:
# Escribe cada test ANTES del código que lo hace pasar y guarda evidencia de
# cada fase (ver README). Sustituye cada `pytest.skip` por tests reales.
#
# Con @given no uses fixtures de alcance function (como `service`): hypothesis
# ejecuta el cuerpo muchas veces con la misma fixture y lo rechaza con
# FailedHealthCheck. En los tests de propiedades crea el servicio dentro del test.

import pytest

from item_service import InMemoryItemRepository, ItemService


@pytest.fixture
def service() -> ItemService:
    return ItemService(InMemoryItemRepository())


# ============================================
# BLOQUE 1: create_item
# ============================================
# TODO: happy path, nombre vacío, cantidad negativa, ids incrementales


def test_create_item_happy_path(service: ItemService) -> None:
    pytest.skip("TODO: primer ciclo Red-Green-Refactor")


# ============================================
# BLOQUE 2: restock
# ============================================
# TODO: suma la cantidad, item inexistente, amount <= 0, el item guardado cambia


def test_restock_happy_path(service: ItemService) -> None:
    pytest.skip("TODO: ciclo de restock")


# ============================================
# BLOQUE 3: total_quantity y propiedades (hypothesis)
# ============================================
# TODO: total de una lista vacía y de varios items
# TODO: propiedad 1 - total_quantity es la suma de las cantidades creadas
# TODO: propiedad 2 - restock nunca disminuye la cantidad


def test_total_quantity_property() -> None:
    pytest.skip("TODO: propiedad con @given")
