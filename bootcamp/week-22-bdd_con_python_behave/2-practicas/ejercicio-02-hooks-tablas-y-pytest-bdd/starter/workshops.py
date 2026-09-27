class RegistrationError(Exception):
    """Una regla de negocio impide la operación."""


class WorkshopRegistry:
    def __init__(self) -> None:
        self._capacity: dict[str, int] = {}
        self._enrolled: dict[str, list[str]] = {}
        self._waitlist: dict[str, list[str]] = {}

    def add_workshop(self, name: str, capacity: int) -> None:
        if name in self._capacity:
            raise RegistrationError(f"workshop {name} already exists")
        self._capacity[name] = capacity
        self._enrolled[name] = []
        self._waitlist[name] = []

    def enroll(self, workshop: str, person: str) -> str:
        if person in self._enrolled[workshop] or person in self._waitlist[workshop]:
            raise RegistrationError(f"{person} is already registered in {workshop}")
        if len(self._enrolled[workshop]) < self._capacity[workshop]:
            self._enrolled[workshop].append(person)
            return "enrolled"
        self._waitlist[workshop].append(person)
        return "waitlisted"

    def free_seats(self, workshop: str) -> int:
        return self._capacity[workshop] - len(self._enrolled[workshop])

    def waitlist(self, workshop: str) -> list[str]:
        return list(self._waitlist[workshop])
