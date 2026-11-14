# language: es
Característica: Multa por devolver tarde un libro
  Para que los socios devuelvan los libros a tiempo
  Como bibliotecaria
  Quiero calcular la multa según los días de retraso

  Esquema del escenario: la multa crece por día hasta un máximo
    Dado un préstamo devuelto con <dias> días de retraso
    Cuando calculo la multa
    Entonces la multa es <multa>

    Ejemplos:
      | dias | multa |
      | 0    | 0     |
      | 3    | 150   |
      | 20   | 1000  |
      | 30   | 1000  |
