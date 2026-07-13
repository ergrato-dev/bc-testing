# 01 - Plan de Pruebas y Trazabilidad

**Tipo**: Transversal (JS / Python / Java)

![Ciclo del plan de pruebas](../0-assets/01-plan-pruebas-ciclo.svg)

## Que es un plan de pruebas

Un plan de pruebas es un documento operativo que responde:

- Que se va a probar.
- Que no se va a probar.
- Como se ejecutara.
- Quien participa.
- Cuando se considera completado.

Sin este documento, el equipo prueba lo que recuerda o lo que le parece importante ese dia. Con el, la cobertura es una decision explicita, no un accidente.

## Estructura minima recomendada

1. **Alcance del modulo** — que funcionalidad entra y cual queda fuera de esta ronda.
2. **Supuestos y restricciones** — que se asume verdadero (ej. base de datos ya migrada) y que limita el trabajo (ej. sin acceso a ambiente de staging).
3. **Riesgos funcionales y tecnicos** — que puede fallar y que tan grave seria.
4. **Estrategia de pruebas** — mezcla de manual/automatizada y en que capa (unit, integracion, end-to-end).
5. **Criterios de entrada y salida** — cuando se puede empezar a ejecutar y cuando se declara terminado.
6. **Matriz de trazabilidad** — el mapa que conecta cada requerimiento con su evidencia de prueba.

## Alcance: que entra y que no

El alcance mal definido es la causa mas comun de un plan inutil. No basta con decir "se prueba el modulo de reservas"; hay que declarar limites.

Ejemplo para un sistema de reservas de turnos en un planetario:

**Dentro de alcance**:

- Creacion de una reserva con datos validos.
- Rechazo de reservas con cupo agotado.
- Calculo de disponibilidad por franja horaria.

**Fuera de alcance**:

- Integracion con pasarela de pago (se prueba en otra suite).
- Notificaciones por correo (modulo separado, semana futura).
- Carga masiva de eventos administrativos.

Declarar el "fuera de alcance" evita que el equipo asuma cobertura donde no la hay.

## Ejemplo de matriz de trazabilidad

| Requirement ID | Caso de prueba | Tipo | Prioridad | Estado |
|---|---|---|---|---|
| REQ-001 | TC-001 crear reserva valida | Unit | Alta | Pendiente |
| REQ-002 | TC-002 rechaza reserva sin cupo | Unit | Alta | Pendiente |
| REQ-003 | TC-003 rechaza franja horaria invalida | Unit | Alta | Pendiente |
| REQ-004 | TC-004 calcula disponibilidad restante | Integration | Media | Pendiente |

Cada fila es una promesa: "este requerimiento tiene evidencia de que fue probado". Una fila sin caso de prueba es un requerimiento sin garantia.

## Como enlazar un caso de prueba a un requerimiento

La trazabilidad no es un ID puesto porque si. Sigue una convencion simple:

1. Cada requerimiento tiene un identificador unico (`REQ-XXX`).
2. Cada caso de prueba referencia el requerimiento que valida (`TC-XXX` -> `REQ-XXX`).
3. Un requerimiento puede tener varios casos de prueba (positivo, negativo, borde).
4. Un caso de prueba deberia validar un solo requerimiento — si valida dos, es candidato a dividirse.

El nombre del test tambien es trazabilidad. Sin importar el lenguaje, el nombre debe reflejar el `REQ` que cubre:

| Lenguaje | Convencion de nombre | Ejemplo para REQ-002 |
|---|---|---|
| JavaScript (Jest) | `should` + comportamiento esperado | `should reject booking when capacity is full` |
| Python (pytest) | `test_` + snake_case descriptivo | `test_rejects_booking_when_capacity_is_full` |
| Java (JUnit 5) | `@DisplayName` + metodo camelCase | `rejectsBookingWhenCapacityIsFull` |

El lenguaje cambia, la trazabilidad hacia `REQ-002` no.

## Criterios de entrada y salida

### Entrada

- Reglas de negocio acordadas.
- Ambiente local de pruebas disponible.
- Datos de prueba ficticios preparados.

### Salida

- 100% de casos criticos ejecutados.
- Sin bloqueantes abiertos.
- Evidencia de resultados consolidada.

## Errores frecuentes cuando el plan es muy vago

- **Alcance ambiguo**: "probar el modulo completo" sin listar que casos concretos aplica.
- **Sin matriz de trazabilidad**: la suite pasa en verde pero nadie sabe que requerimiento cubre cada test.
- **Criterios de salida subjetivos**: "cuando este listo" en vez de un numero o condicion verificable.
- **Riesgos no priorizados**: todos los casos con la misma prioridad "alta" no priorizan nada.
- **Plan que nadie actualiza**: el documento queda desincronizado del codigo despues del primer cambio de requerimiento.

## Error comun

Confundir "ejecutar tests" con "tener estrategia de calidad". Una suite sin trazabilidad puede pasar en verde y aun asi dejar riesgos sin cubrir.

## Regla practica

Si no puedes responder "¿que requerimiento valida este test?" en menos de cinco segundos, la trazabilidad esta rota. Arreglala antes de agregar mas tests.
