# ============================================
# MÓDULO: reglas de inventario (código heredado con tests débiles)
# ============================================

# NOTA PARA EL APRENDIZ:
# Adapta estas reglas a tu dominio asignado (o sustitúyelas por el servicio que
# construiste con TDD en la semana 20). Ejemplos neutrales (no los uses en tu entrega):
# - Museo: estado de conservación de una pieza, coste de restauración, tipo de embalaje
# - Planetario: ocupación de una función, precio por grupo, tipo de proyección
# - Acuario: estado de un tanque, coste de alimento, tipo de transporte de especies


def stock_status(quantity: int, reorder_level: int) -> str:
    if quantity < 0:
        raise ValueError("quantity must be >= 0")
    if quantity == 0:
        return "out_of_stock"
    if quantity <= reorder_level:
        return "reorder"
    return "ok"


def restock_cost(unit_price: float, units: int) -> float:
    if units <= 0:
        raise ValueError("units must be > 0")
    cost = unit_price * units
    if units >= 50:
        cost *= 0.9
    return round(cost, 2)


def shipping_class(weight_kg: float, country: str, express: bool, fragile: bool) -> str:
    if weight_kg <= 0:
        raise ValueError("weight must be > 0")
    if country == "CO":
        if express:
            return "co-express"
        return "co-standard"
    if express and fragile:
        return "intl-express-fragile"
    if express:
        return "intl-express"
    if fragile:
        return "intl-fragile"
    return "intl-standard"
