def add(a: int, b: int) -> int:
    # ============================================
    # PASO 1: Corrige el bug (rojo → verde)
    # ============================================
    # El test test_add_returns_five_when_inputs_are_two_and_three falla porque aquí se resta.
    # Borra la línea `return a - b` y descomenta la línea correcta:
    # return a + b
    return a - b


def is_even(n: int) -> bool:
    return n % 2 == 0
