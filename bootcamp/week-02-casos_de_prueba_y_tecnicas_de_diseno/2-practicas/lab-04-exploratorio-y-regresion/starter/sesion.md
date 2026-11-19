# Sesión exploratoria y selección de suites

## Parte A - Sesión exploratoria

```text
Charter:   Explorar cómo date -d interpreta y calcula fechas
Con:       fechas límite: fin de mes, 29 de febrero, cambio de año,
           cambio de hora, sumas y restas de días y meses
Para:      descubrir entradas que acepte, rechace o calcule de forma inesperada
Duración:  30 minutos
```

Entorno (rellénalo antes de empezar):

- Sistema operativo: TODO
- Salida de `date --version | head -1`: TODO
- Salida de `date +%Z` (tu zona horaria): TODO

### Notas

Una fila por cada prueba. Copia la salida tal cual.

| Hora | Comando | Salida | ¿Esperado? |
|---|---|---|---|
| TODO | TODO | TODO | TODO |

(añade las filas que necesites; al menos 12)

### Hallazgos

Tres hallazgos de las notas: lo que esperabas, lo que pasó y si es un bug, un comportamiento documentado o una pregunta. Busca en el manual antes de decidir.

1. TODO
2. TODO
3. TODO

---

## Parte B - De hallazgo a caso guionizado

Convierte dos hallazgos en casos de prueba que cualquiera pueda repetir.

```text
ID:                 TC-DATE-001
Título:             TODO
Precondiciones:     TODO
Datos de prueba:    TODO
Pasos:
  1. TODO
Resultado esperado: TODO
Prioridad:          TODO
```

```text
ID:                 TC-DATE-002
Título:             TODO
Precondiciones:     TODO
Datos de prueba:    TODO
Pasos:
  1. TODO
Resultado esperado: TODO
Prioridad:          TODO
```

---

## Parte C - Smoke, sanity y regresión

La tienda online tiene estos casos:

| ID | Caso | Prioridad |
|---|---|---|
| TC-HOME-001 | La portada muestra los 12 productos destacados | Alta |
| TC-LOGIN-001 | Login con credenciales válidas redirige al panel | Alta |
| TC-LOGIN-004 | Login con contraseña incorrecta muestra el error | Media |
| TC-SEARCH-007 | Buscar un término sin resultados muestra el aviso | Baja |
| TC-CART-001 | Añadir un producto al carrito lo muestra en el carrito | Alta |
| TC-CART-011 | Añadir dos veces el mismo producto suma la cantidad | Media |
| TC-ENV-001 | Pedido de 29,99 € sin premium paga 3,90 € de envío | Media |
| TC-ENV-002 | Pedido de 30,00 € sin premium no paga envío | Media |
| TC-PAY-001 | Pago con tarjeta válida confirma el pedido | Alta |
| TC-PAY-003 | Pago con tarjeta caducada muestra el error | Media |
| TC-PWD-003 | Cambiar la contraseña con la actual incorrecta muestra el error | Media |

Situación: ayer se corrigió un bug por el que los pedidos de exactamente 30,00 € pagaban envío. Hoy hay una build nueva en el entorno de pruebas, y el viernes sale la release.

| Suite | Cuándo la ejecutas | Casos (IDs) | Por qué esos |
|---|---|---|---|
| Smoke | TODO | TODO | TODO |
| Sanity | TODO | TODO | TODO |
| Regresión | TODO | TODO | TODO |

El arreglo del bug de los 30,00 €, ¿añade algún caso a la regresión? ¿Cuál? TODO
