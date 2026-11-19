# Registro de visitantes del planetario

## Reglas

| Campo | Regla |
|---|---|
| `nombre` | De 2 a 50 caracteres; solo letras (con tildes y ñ) y espacios |
| `edad` | Número entero de 5 a 99 |
| `grupoEscolar` | Sí o No. Con Sí, la edad tiene que ser 17 o menos |

Con un dato inválido, el formulario no se envía y bajo el campo aparece un mensaje. Con todos válidos, aparece «Visitante registrado».

---

## Parte A - Partición de equivalencia

### `nombre`

| Clase | Condición | Valor | Tipo |
|---|---|---|---|
| N1 | 2 a 50 caracteres, solo letras y espacios | TODO | Válida |
| N2 | TODO | TODO | Inválida |
| N3 | TODO | TODO | Inválida |
| N4 | TODO | TODO | Inválida |
| N5 | TODO | TODO | Inválida |
| N6 | TODO | TODO | Inválida |

### `edad`

| Clase | Condición | Valor | Tipo |
|---|---|---|---|
| E1 | TODO | TODO | Válida |
| E2 | TODO | TODO | Inválida |
| E3 | TODO | TODO | Inválida |
| E4 | TODO | TODO | Inválida |
| E5 | TODO | TODO | Inválida |

### `grupoEscolar` combinado con `edad`

| Clase | Condición | Valores | Tipo |
|---|---|---|---|
| G1 | TODO | TODO | Válida |
| G2 | TODO | TODO | Válida |
| G3 | TODO | TODO | Inválida |

---

## Parte B - Valores límite (3 valores)

### `edad`, rango 5 a 99

| Valor | Posición | Esperado |
|---|---|---|
| TODO | Vecino de fuera del mínimo | TODO |
| TODO | Límite mínimo | TODO |
| TODO | Vecino de dentro del mínimo | TODO |
| TODO | Vecino de dentro del máximo | TODO |
| TODO | Límite máximo | TODO |
| TODO | Vecino de fuera del máximo | TODO |

### `nombre`, longitud 2 a 50

| Longitud | Dato | Esperado |
|---|---|---|
| TODO | TODO | TODO |
| TODO | TODO | TODO |
| TODO | TODO | TODO |
| TODO | TODO | TODO |
| TODO | TODO | TODO |
| TODO | TODO | TODO |

### `edad` con `grupoEscolar` = Sí, máximo 17

| Valor | Posición | Esperado |
|---|---|---|
| TODO | Vecino de dentro del máximo | TODO |
| TODO | Límite máximo | TODO |
| TODO | Vecino de fuera del máximo | TODO |

---

## Parte C - Suite mínima

Una fila por caso. Cada caso dice qué clases o límites cubre. Una sola clase inválida por caso.

| ID | Cubre | nombre | edad | grupoEscolar | Esperado |
|---|---|---|---|---|---|
| TC-PLAN-REG-001 | N1, E1, G2 | TODO | TODO | No | Visitante registrado |
| TC-PLAN-REG-002 | TODO | TODO | TODO | TODO | TODO |

(añade las filas que necesites)

---

## Parte D - Conteo

| | Número |
|---|---|
| Clases de la parte A | TODO |
| Valores límite de la parte B | TODO |
| Casos de la suite (parte C) | TODO |

¿Por qué la suite tiene menos casos que la suma de clases y valores límite? TODO
