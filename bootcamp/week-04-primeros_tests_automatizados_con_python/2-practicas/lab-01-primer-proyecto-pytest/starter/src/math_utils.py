def add(a: int, b: int) -> int:
    # ============================================
    # PASO 1: Corrige el bug (rojo → verde)
    # ============================================
    # El test test_returns_5_when_adding_2_and_3 falla porque aquí se resta.
    # Borra la línea `return a - b` y descomenta la línea correcta:
    # return a + b
    return a - b


def is_even(n: int) -> bool:
    return n % 2 == 0
