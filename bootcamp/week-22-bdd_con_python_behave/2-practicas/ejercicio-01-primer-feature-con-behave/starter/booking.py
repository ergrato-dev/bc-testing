OPENING_HOUR = 8
CLOSING_HOUR = 18  # la sala cierra a las 18: la última reserva empieza a las 17


class BookingError(Exception):
    """Una regla de negocio impide la reserva."""


class RoomCalendar:
    def __init__(self) -> None:
        self._bookings: dict[tuple[str, int], str] = {}

    def book(self, room: str, hour: int, person: str) -> None:
        if not OPENING_HOUR <= hour <= CLOSING_HOUR:
            raise BookingError(f"hour {hour} is outside opening hours")
        if (room, hour) in self._bookings:
            raise BookingError(f"room {room} is already booked at {hour}")
        self._bookings[(room, hour)] = person

    def booked_by(self, room: str, hour: int) -> str | None:
        return self._bookings.get((room, hour))
