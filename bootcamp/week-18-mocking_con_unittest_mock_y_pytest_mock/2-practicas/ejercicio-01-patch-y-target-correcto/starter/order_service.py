from gateways import PaymentGateway


def confirm_order(order_id: str, amount: float) -> str:
    gateway = PaymentGateway()
    result = gateway.charge(order_id, amount)

    if result["status"] == "approved":
        return "confirmed"

    return "rejected"
