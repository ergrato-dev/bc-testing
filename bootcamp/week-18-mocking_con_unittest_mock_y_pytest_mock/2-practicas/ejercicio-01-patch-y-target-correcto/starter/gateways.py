class PaymentGateway:
    def charge(self, order_id: str, amount: float) -> dict:
        return {"status": "approved", "order_id": order_id, "amount": amount}
