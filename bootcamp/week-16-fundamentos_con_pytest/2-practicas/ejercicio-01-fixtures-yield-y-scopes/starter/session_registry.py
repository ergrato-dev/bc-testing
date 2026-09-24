"""Registro en memoria de las funciones (sesiones) del Planetario."""


class SessionRegistry:
    def __init__(self) -> None:
        self.is_open = False
        self._seats: dict[str, int] = {}

    def open(self) -> None:
        self.is_open = True

    def close(self) -> None:
        self._seats.clear()
        self.is_open = False

    def add_session(self, name: str, seats: int) -> None:
        self._ensure_open()
        if seats <= 0:
            raise ValueError("seats must be greater than zero")
        self._seats[name] = seats

    def reserve(self, name: str, quantity: int) -> None:
        self._ensure_open()
        if name not in self._seats:
            raise ValueError(f"unknown session: {name}")
        if quantity > self._seats[name]:
            raise ValueError("not enough seats")
        self._seats[name] -= quantity

    def available(self, name: str) -> int:
        return self._seats.get(name, 0)

    def session_names(self) -> list[str]:
        return sorted(self._seats)

    def _ensure_open(self) -> None:
        if not self.is_open:
            raise RuntimeError("registry is closed")


def load_show_catalog() -> dict[str, int]:
    """Simula una carga costosa (leer un archivo grande o llamar a una API)."""
    return {"Sistema solar": 40, "Auroras": 30, "Agujeros negros": 25}
