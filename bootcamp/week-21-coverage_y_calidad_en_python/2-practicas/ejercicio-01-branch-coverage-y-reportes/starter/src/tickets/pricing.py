def ticket_price(age: int, is_member: bool) -> int:
    """Precio de una entrada: 20 general, 10 para menores de 12 y 5 de descuento para socios."""
    if age < 0:
        raise ValueError("age must be >= 0")
    price = 20
    if age < 12:
        price = 10
    if is_member:
        price -= 5
    return price


if __name__ == "__main__":
    print(ticket_price(8, is_member=True))
