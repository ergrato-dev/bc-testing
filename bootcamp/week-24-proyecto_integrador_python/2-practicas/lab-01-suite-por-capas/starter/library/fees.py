DAILY_FEE = 50
MAX_FEE = 1000


def late_fee(days_late: int) -> int:
    if days_late <= 0:
        return 0
    fee = days_late * DAILY_FEE
    if fee > MAX_FEE:
        return MAX_FEE
    return fee
