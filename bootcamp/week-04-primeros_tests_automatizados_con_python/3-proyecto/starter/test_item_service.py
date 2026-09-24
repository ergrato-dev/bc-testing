# ============================================
# TEST SUITE: ItemService
# Servicio para gestión de elementos del dominio
# ============================================

# NOTA PARA EL APRENDIZ:
# Adapta esta suite a tu dominio asignado (mismas funciones que en la semana 03).
# Ejemplos neutrales (no los uses en tu entrega):
# - Museo: calculate_ticket_price, is_visit_slot_available
# - Planetario: calculate_show_price, is_seat_available
# - Acuario: calculate_group_price, is_tank_capacity_valid
#
# TODO: crea `item_service.py` junto a este archivo con las funciones de tu dominio
#       e impórtalas aquí, por ejemplo: from item_service import create_item
# TODO: sustituye cada `pytest.skip` por un test real con Arrange / Act / Assert
#       y renombra cada test con el patrón test_[contexto]_[resultado]_when_[condicion].

import pytest


# ============================================
# BLOQUE 1: Creación
# ============================================
# TODO: 1. Caso válido (happy path)
# TODO: 2. Campo requerido faltante
# TODO: 3. Tipo de dato incorrecto


def test_create_happy_path() -> None:
    pytest.skip("TODO: caso válido de creación")


# ============================================
# BLOQUE 2: Cálculo
# ============================================
# TODO: 1. Cálculo base correcto
# TODO: 2. Borde inferior (edge case)
# TODO: 3. Borde superior (edge case)


def test_calculate_base_case() -> None:
    pytest.skip("TODO: cálculo base del dominio")


# ============================================
# BLOQUE 3: Validación
# ============================================
# TODO: 1. Valor permitido
# TODO: 2. Valor no permitido


def test_validate_allowed_value() -> None:
    pytest.skip("TODO: validación de un valor permitido")
