# language: es
Característica: Reserva de salas de reuniones
  Para no chocar con otros equipos
  Como integrante de un equipo
  Quiero reservar una sala por horas

  Antecedentes:
    Dado un calendario de salas vacío

  Escenario: Reservar una sala libre
    Cuando "Ana" reserva la sala "Azul" a las 9
    Entonces la sala "Azul" queda reservada a las 9 por "Ana"

  Escenario: No se puede reservar una sala ocupada
    Dado que "Ana" reservó la sala "Azul" a las 9
    Cuando "Luis" intenta reservar la sala "Azul" a las 9
    Entonces la reserva se rechaza con el mensaje "room Azul is already booked at 9"
    Y la sala "Azul" queda reservada a las 9 por "Ana"

  Esquema del escenario: Solo se reserva en horario de apertura
    Cuando "Ana" intenta reservar la sala "Verde" a las <hora>
    Entonces la reserva queda <resultado>

    Ejemplos:
      | hora | resultado |
      | 7    | rechazada |
      | 8    | aceptada  |
      | 17   | aceptada  |
      | 18   | rechazada |
