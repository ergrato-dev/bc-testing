# language: es
Característica: Carrito de la tienda
  Desde $100.000 de subtotal, el carrito descuenta un 10 %.

  Antecedentes:
    Dado el catálogo:
      | producto | precio  |
      | Café     | $12.500 |
      | Taza     | $30.000 |
      | Molino   | $95.000 |

  Escenario: Por debajo de $100.000 no hay descuento
    Cuando añado 2 Café al carrito
    Y añado 1 Taza al carrito
    Entonces el total es $55.000
