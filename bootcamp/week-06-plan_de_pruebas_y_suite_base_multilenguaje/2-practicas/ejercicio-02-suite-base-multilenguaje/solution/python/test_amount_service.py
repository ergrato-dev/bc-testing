from amount_service import is_valid_amount


def test_should_return_true_when_amount_is_positive():
    result = is_valid_amount(10)
    assert result is True


def test_should_return_false_when_amount_is_negative():
    result = is_valid_amount(-1)
    assert result is False


def test_should_return_true_when_amount_is_zero():
    result = is_valid_amount(0)
    assert result is True
