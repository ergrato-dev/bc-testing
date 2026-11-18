# language: es
Característica: Multas por retraso
  La biblioteca cobra 50 por día de retraso, hasta un máximo de 1000.

  Esquema del escenario: Multa según los días de retraso
    Cuando devuelvo un libro con <días> días de retraso
    Entonces pago una multa de <multa>

    Ejemplos:
      | días | multa |
      | 1    | 50    |
      | 19   | 950   |
      | 20   | 1000  |
      | 30   | 1000  |
