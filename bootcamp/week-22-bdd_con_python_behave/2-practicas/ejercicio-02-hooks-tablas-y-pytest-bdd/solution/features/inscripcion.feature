# language: es
Característica: Inscripción a talleres
  Para aprovechar los cupos disponibles
  Como participante
  Quiero inscribirme en un taller o quedar en lista de espera

  Antecedentes:
    Dado los talleres:
      | taller  | cupos |
      | Python  | 2     |
      | Testing | 1     |

  @smoke
  Escenario: Inscribirse en un taller con cupo
    Cuando "Ana" se inscribe en "Python"
    Entonces la inscripción queda "enrolled"
    Y el taller "Python" tiene 1 cupos libres

  Escenario: Taller lleno manda a lista de espera
    Dado que "Ana" se inscribió en "Testing"
    Cuando "Luis" se inscribe en "Testing"
    Entonces la inscripción queda "waitlisted"
    Y la lista de espera de "Testing" es "Luis"
