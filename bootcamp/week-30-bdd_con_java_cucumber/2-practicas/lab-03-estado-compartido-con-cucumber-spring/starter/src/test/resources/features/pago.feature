# language: es
Característica: Pago de pedidos
  Un pedido se paga una sola vez y por su importe exacto.

  Escenario: Un pedido nuevo queda pendiente
    Dado un pedido de 40000 pesos
    Entonces hay 1 pedido pendiente

