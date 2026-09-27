from behave import given, then, when

from booking import BookingError, RoomCalendar


# ============================================
# PASO 1: El contexto común (Antecedentes)
# ============================================
# Descomenta las siguientes líneas:
# @given("un calendario de salas vacío")
# def step_empty_calendar(context):
#     context.calendar = RoomCalendar()
#     context.error = None


# ============================================
# PASO 2: Reservar una sala libre
# ============================================
# Descomenta las siguientes líneas:
# @when('"{person}" reserva la sala "{room}" a las {hour:d}')
# def step_book(context, person, room, hour):
#     context.calendar.book(room, hour, person)


# @then('la sala "{room}" queda reservada a las {hour:d} por "{person}"')
# def step_booked_by(context, room, hour, person):
#     assert context.calendar.booked_by(room, hour) == person


# ============================================
# PASO 3: Sala ocupada
# ============================================
# Descomenta las siguientes líneas:
# @given('que "{person}" reservó la sala "{room}" a las {hour:d}')
# def step_existing_booking(context, person, room, hour):
#     context.calendar.book(room, hour, person)


# @when('"{person}" intenta reservar la sala "{room}" a las {hour:d}')
# def step_try_book(context, person, room, hour):
#     try:
#         context.calendar.book(room, hour, person)
#     except BookingError as error:
#         context.error = error


# @then('la reserva se rechaza con el mensaje "{message}"')
# def step_rejected_with(context, message):
#     assert context.error is not None, "la reserva se aceptó"
#     assert str(context.error) == message


# ============================================
# PASO 4: Scenario Outline del horario
# ============================================
# Descomenta las siguientes líneas:
# @then("la reserva queda aceptada")
# def step_accepted(context):
#     assert context.error is None, f"la reserva se rechazó: {context.error}"


# @then("la reserva queda rechazada")
# def step_rejected(context):
#     assert context.error is not None, "la reserva se aceptó"
