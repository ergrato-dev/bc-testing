import pytest

from library.fees import late_fee


@pytest.mark.parametrize("days_late", [0, -2])
def test_late_fee_is_zero_when_returned_on_time(days_late):
    assert late_fee(days_late) == 0


def test_late_fee_charges_per_day_late():
    assert late_fee(3) == 150
