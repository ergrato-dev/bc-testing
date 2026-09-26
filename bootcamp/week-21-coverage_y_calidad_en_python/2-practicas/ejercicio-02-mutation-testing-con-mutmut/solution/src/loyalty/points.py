def points_for(amount: float, is_member: bool) -> int:
    """Un punto por cada 10 de compra; los socios duplican los puntos desde 100."""
    if amount <= 0:
        return 0
    points = int(amount // 10)
    if is_member and amount >= 100:
        points *= 2
    return points
