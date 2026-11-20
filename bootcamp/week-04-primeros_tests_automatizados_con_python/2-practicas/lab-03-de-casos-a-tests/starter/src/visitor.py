# Validación del formulario de registro del planetario (semana 02, lab 02).
# Devuelve "Visitante registrado" o el mensaje del primer campo inválido.

import re

NAME_PATTERN = re.compile(r"[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+")


def validate_visitor(visitor: dict) -> str:
    name = visitor["name"]
    age = visitor["age"]
    school_group = visitor["school_group"]

    if len(name) < 3 or len(name) > 50:
        return "El nombre debe tener de 2 a 50 caracteres"
    if not NAME_PATTERN.fullmatch(name):
        return "El nombre solo admite letras y espacios"
    if not isinstance(age, (int, float)) or age < 5 or age >= 99:
        return "La edad debe ser un número entero de 5 a 99"
    if school_group and age >= 17:
        return "Los grupos escolares son para menores de 18 años"
    return "Visitante registrado"
