from behave import given, then, when


# ============================================
# PASO 1: Tabla de datos en Antecedentes
# ============================================
# Descomenta las siguientes líneas:
# @given("los talleres:")
# def step_workshops(context):
#     for row in context.table:
#         context.registry.add_workshop(row["taller"], int(row["cupos"]))


# ============================================
# PASO 2: Inscripción con cupo
# ============================================
# Descomenta las siguientes líneas:
# @when('"{person}" se inscribe en "{workshop}"')
# def step_enroll(context, person, workshop):
#     context.result = context.registry.enroll(workshop, person)


# @then('la inscripción queda "{status}"')
# def step_status(context, status):
#     assert context.result == status, f"se obtuvo {context.result!r}"


# @then('el taller "{workshop}" tiene {seats:d} cupos libres')
# def step_free_seats(context, workshop, seats):
#     assert context.registry.free_seats(workshop) == seats


# ============================================
# PASO 3: Lista de espera
# ============================================
# Descomenta las siguientes líneas:
# @given('que "{person}" se inscribió en "{workshop}"')
# def step_existing_enrollment(context, person, workshop):
#     context.registry.enroll(workshop, person)


# @then('la lista de espera de "{workshop}" es "{people}"')
# def step_waitlist(context, workshop, people):
#     assert context.registry.waitlist(workshop) == people.split(", ")
