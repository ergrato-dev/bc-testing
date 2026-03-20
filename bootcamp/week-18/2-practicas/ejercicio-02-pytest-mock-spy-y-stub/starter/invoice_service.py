class TaxCalculator:
    def calculate(self, subtotal: float) -> float:
        return round(subtotal * 0.16, 2)


def normalize_client_name(name: str) -> str:
    return " ".join(name.strip().lower().split())


def build_invoice_total(subtotal: float, calculator: TaxCalculator) -> float:
    tax = calculator.calculate(subtotal)
    return round(subtotal + tax, 2)


def build_client_label(name: str) -> str:
    normalized = normalize_client_name(name)
    return f"CLIENT::{normalized}"
