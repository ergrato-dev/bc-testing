"""Configuración del Museo leída de variables de entorno."""

import os

DEFAULT_MUSEUM_NAME = "Museo sin nombre"


def get_museum_name() -> str:
    return os.environ.get("MUSEUM_NAME", DEFAULT_MUSEUM_NAME)


def print_summary(exhibits: list[dict]) -> None:
    print(f"{get_museum_name()}: {len(exhibits)} piezas")
    for exhibit in exhibits:
        print(f"- {exhibit['title']} ({exhibit['room']})")
