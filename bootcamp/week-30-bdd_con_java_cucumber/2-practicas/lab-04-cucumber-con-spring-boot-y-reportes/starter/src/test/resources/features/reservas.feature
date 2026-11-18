# language: es
Característica: Reserva de salas
  Una sala se reserva por días completos. Si ya está reservada ese día, la API lo rechaza
  y dice quién la tiene.

  Escenario: Reservar una sala libre
    Cuando Luis reserva la sala Sur para el 2026-12-01
    Entonces la API responde 201
