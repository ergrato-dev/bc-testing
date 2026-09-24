class PaymentGateway:
    """Cliente HTTP real (simulado). En un test unitario nunca debe ejecutarse."""

    def charge(self, order_id: str, amount: float) -> dict:
        raise ConnectionError(f"POST https://payments.example/charges/{order_id}: sin red en los tests")
