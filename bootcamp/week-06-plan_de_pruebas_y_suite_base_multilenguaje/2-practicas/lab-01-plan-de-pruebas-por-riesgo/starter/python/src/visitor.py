"""Registro de visitantes del planetario (reglas R1 a R4 de plan-de-pruebas.md)."""

import re

NAME_PATTERN = re.compile(r"[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+")


def validate_visitor(name: str, age: int, school_group: bool) -> str:
    """Devuelve "Visitante registrado" o el mensaje de la primera regla que no se cumple."""
    if len(name) < 2 or len(name) > 50:
        return "El nombre debe tener de 2 a 50 caracteres"
    if not NAME_PATTERN.fullmatch(name):
        return "El nombre solo admite letras y espacios"
    if not isinstance(age, int) or age < 5 or age > 99:
        return "La edad debe ser un número entero de 5 a 99"
    if school_group and age >= 18:
        return "Los grupos escolares son para menores de 18 años"
    return "Visitante registrado"
