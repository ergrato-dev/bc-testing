import pytest
from pytest_bdd import given, parsers, scenarios, then, when

from workshops import WorkshopRegistry

# ============================================
# PASO 4: El mismo feature con pytest-bdd
# ============================================
# Descomenta las siguientes líneas:
# scenarios("../features/inscripcion.feature")


# @pytest.fixture
# def registry() -> WorkshopRegistry:
#     # Fixture de pytest: una instancia nueva por escenario, igual que before_scenario.
#     return WorkshopRegistry()


# @given("los talleres:")
# def workshops(registry, datatable):
#     header, *rows = datatable
#     for row in rows:
#         values = dict(zip(header, row))
#         registry.add_workshop(values["taller"], int(values["cupos"]))


# @given(parsers.parse('que "{person}" se inscribió en "{workshop}"'))
# def existing_enrollment(registry, person, workshop):
#     registry.enroll(workshop, person)


# @when(parsers.parse('"{person}" se inscribe en "{workshop}"'), target_fixture="result")
# def enroll(registry, person, workshop):
#     return registry.enroll(workshop, person)


# @then(parsers.parse('la inscripción queda "{status}"'))
# def status_is(result, status):
#     assert result == status


# @then(parsers.parse('el taller "{workshop}" tiene {seats:d} cupos libres'))
# def free_seats(registry, workshop, seats):
#     assert registry.free_seats(workshop) == seats


# @then(parsers.parse('la lista de espera de "{workshop}" es "{people}"'))
# def waitlist_is(registry, workshop, people):
#     assert registry.waitlist(workshop) == people.split(", ")
