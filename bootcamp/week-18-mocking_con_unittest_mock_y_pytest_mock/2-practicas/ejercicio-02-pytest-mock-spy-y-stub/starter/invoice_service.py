import os


class TaxCalculator:
    def calculate(self, subtotal: float) -> float:
        return round(subtotal * 0.16, 2)


class InvoiceMailer:
    """Cliente de correo real (simulado). En un test unitario nunca debe ejecutarse."""

    def send(self, invoice_id: str, total: float) -> None:
        raise ConnectionError("SMTP no disponible en los tests")


def normalize_client_name(name: str) -> str:
    return " ".join(name.strip().lower().split())


def build_invoice_total(subtotal: float, calculator: TaxCalculator) -> float:
    tax = calculator.calculate(subtotal)
    return round(subtotal + tax, 2)


def send_invoices(subtotals: dict[str, float], calculator: TaxCalculator, mailer: InvoiceMailer) -> int:
    sent = 0
    for invoice_id, subtotal in subtotals.items():
        total = build_invoice_total(subtotal, calculator)
        if total > 0:
            mailer.send(invoice_id, total)
            sent += 1
    return sent


def build_client_label(name: str) -> str:
    prefix = os.environ.get("CLIENT_LABEL_PREFIX", "CLIENT")
    normalized = normalize_client_name(name)
    return f"{prefix}::{normalized}"
