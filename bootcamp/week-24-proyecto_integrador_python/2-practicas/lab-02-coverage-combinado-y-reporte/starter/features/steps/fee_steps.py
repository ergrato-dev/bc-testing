from behave import given, then, when

from library.fees import late_fee


@given("un préstamo devuelto con {days:d} días de retraso")
def step_loan_returned_late(context, days):
    context.days_late = days


@when("calculo la multa")
def step_compute_fee(context):
    context.fee = late_fee(context.days_late)


@then("la multa es {fee:d}")
def step_fee_is(context, fee):
    assert context.fee == fee
