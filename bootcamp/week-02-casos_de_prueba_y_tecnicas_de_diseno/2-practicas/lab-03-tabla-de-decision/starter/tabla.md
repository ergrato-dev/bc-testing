# Gastos de envío de la tienda online

## Reglas, tal como las escribió el equipo de negocio

1. Los pedidos de 30 € o más tienen el envío gratis.
2. Los clientes premium tienen siempre el envío estándar gratis.
3. El envío urgente cuesta 9,90 €. Los clientes premium lo pagan a 4,90 €.
4. Si no hay envío gratis, el estándar cuesta 3,90 €.

---

## Parte A - Condiciones y acciones

| Condición | Valores posibles |
|---|---|
| TODO | S / N |
| TODO | S / N |
| TODO | S / N |

Acción (lo que el sistema decide): TODO

---

## Parte B - Tabla completa

Escribe en cada celda de **Envío** el importe, o `?` si las reglas no lo deciden o se contradicen.

| Condición | R1 | R2 | R3 | R4 | R5 | R6 | R7 | R8 |
|---|---|---|---|---|---|---|---|---|
| TODO | S | S | S | S | N | N | N | N |
| TODO | S | S | N | N | S | S | N | N |
| TODO | S | N | S | N | S | N | S | N |
| **Envío** | TODO | TODO | TODO | TODO | TODO | TODO | TODO | TODO |

Regla o reglas que aplican en cada columna:

- R1: TODO
- R2: TODO
- R3: TODO
- R4: TODO
- R5: TODO
- R6: TODO
- R7: TODO
- R8: TODO

---

## Parte C - Preguntas para negocio

Una pregunta por cada `?` de la tabla. Di qué reglas chocan y qué dos respuestas son posibles.

1. TODO
2. TODO

---

## Parte D - Tabla simplificada

Con las respuestas que tú elijas para la parte C (escribe cuáles), junta las reglas con el mismo resultado y marca con `-` la condición que no importa.

Respuestas supuestas: TODO

| Condición | TODO | TODO | TODO | TODO | TODO |
|---|---|---|---|---|---|
| TODO | TODO | TODO | TODO | TODO | TODO |
| TODO | TODO | TODO | TODO | TODO | TODO |
| TODO | TODO | TODO | TODO | TODO | TODO |
| **Envío** | TODO | TODO | TODO | TODO | TODO |

(quita o añade columnas según te salgan)

---

## Parte E - Los casos

Un caso por columna de la parte D. Donde la condición sea el importe, usa un valor límite: 29,99 € o 30,00 €.

| ID | Regla | Premium | Importe | Urgente | Envío esperado |
|---|---|---|---|---|---|
| TC-ENV-001 | TODO | TODO | TODO | TODO | TODO |

(añade las filas que necesites)

Si negocio añade una cuarta condición de sí o no (por ejemplo, «dirección en una isla»), ¿cuántas reglas tendría la tabla completa? TODO
