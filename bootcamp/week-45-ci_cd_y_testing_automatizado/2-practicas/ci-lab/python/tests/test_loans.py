import pytest

from library.loans import can_renew, loan_days


@pytest.mark.parametrize(
    ("renewals", "reserved", "expected"),
    [(0, False, True), (1, False, True), (2, False, False), (0, True, False)],
)
def test_can_renew(renewals: int, reserved: bool, expected: bool) -> None:
    assert can_renew(renewals, reserved) is expected


def test_can_renew_rejects_negative_renewals() -> None:
    with pytest.raises(ValueError):
        can_renew(-1, False)


@pytest.mark.parametrize(("member", "days"), [("student", 14), ("teacher", 30)])
def test_loan_days(member: str, days: int) -> None:
    assert loan_days(member) == days
