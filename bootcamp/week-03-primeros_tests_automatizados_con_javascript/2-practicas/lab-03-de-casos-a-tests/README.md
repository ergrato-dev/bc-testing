# Lab 03 - De casos de prueba a tests

## Objetivo

Automatizar los casos de partición y valores límite del formulario del planetario de la semana 2, encontrar los bugs que esconde su implementación y documentarlos antes de arreglarlos.

## Tiempo estimado

70 minutos.

## Requisitos

- El [lab 02 de la semana 2](../../../week-02-casos_de_prueba_y_tecnicas_de_diseno/2-practicas/lab-02-particion-y-valores-limite/README.md): las reglas del formulario y su análisis.
- El proyecto `~/labs-semana-03` con los labs 01 y 02.

## Punto de partida

```bash
cd ~/labs-semana-03
cp -r <ruta-del-bootcamp>/bootcamp/week-03-primeros_tests_automatizados_con_javascript/2-practicas/lab-03-de-casos-a-tests/starter/. .
pnpm test visitor 2>&1 | grep '^Tests:'
# Tests:       2 passed, 2 total
```

## Contexto

El equipo de desarrollo ha terminado el formulario de registro del planetario: `validateVisitor` en `src/visitor.js`. Recibe un visitante y devuelve `"Visitante registrado"` o el mensaje del primer campo inválido. Las reglas son las de la semana 2:

| Campo | Regla | Mensaje si no se cumple |
|---|---|---|
| `name` | De 2 a 50 caracteres; solo letras (con tildes y ñ) y espacios | `El nombre debe tener de 2 a 50 caracteres` o `El nombre solo admite letras y espacios` |
| `age` | Número entero de 5 a 99 | `La edad debe ser un número entero de 5 a 99` |
| `schoolGroup` | `true` o `false`. Con `true`, la edad tiene que ser 17 o menos | `Los grupos escolares son para menores de 18 años` |

**No abras `src/visitor.js` hasta el paso 5.** Pruebas el formulario como en la semana 2, desde los requisitos y sin mirar el código: así los tests comprueban lo que se pidió, no lo que se programó.

---

## Paso 1: los dos tests de ejemplo (10 min)

Abre `tests/visitor.test.js`. Arriba hay un visitante válido:

```javascript
const validVisitor = { name: "Ana", age: 30, schoolGroup: false };
```

Y cada test crea el suyo cambiando un solo campo:

```javascript
const visitor = { ...validVisitor, name: "A" };
// { name: "A", age: 30, schoolGroup: false }
```

`...validVisitor` copia todos los campos del objeto (es el **operador de propagación**, *spread*), y `name: "A"` sustituye ese campo en la copia. `validVisitor` no cambia. Así cada test tiene una sola clase inválida y el resto válido, la regla de la [semana 2](../../../week-02-casos_de_prueba_y_tecnicas_de_diseno/1-teoria/02-tecnicas-de-diseno.md#una-clase-inválida-por-caso).

## Paso 2: los casos de name (15 min)

Escribe un test por fila, en el `describe("name")`, con el formato de los ejemplos. Pon el ID en un comentario encima de cada `it`, para saber de qué caso viene:

| ID | Técnica | Cambia | Esperado |
|---|---|---|---|
| TC-PLAN-REG-001 | Límite (vecino de fuera) | `name: "A"` | Mensaje de 2 a 50 caracteres (ya escrito) |
| TC-PLAN-REG-002 | Límite (mínimo) | `name: "Al"` | `Visitante registrado` |
| TC-PLAN-REG-003 | Límite (vecino de dentro) | `name: "Ana"` | `Visitante registrado` |
| TC-PLAN-REG-004 | Límite (vecino de dentro) | `name: "a".repeat(49)` | `Visitante registrado` |
| TC-PLAN-REG-005 | Límite (máximo) | `name: "a".repeat(50)` | `Visitante registrado` |
| TC-PLAN-REG-006 | Límite (vecino de fuera) | `name: "a".repeat(51)` | Mensaje de 2 a 50 caracteres |
| TC-PLAN-REG-007 | Partición (clase inválida) | `name: "Ana3"` | Mensaje de solo letras y espacios |
| TC-PLAN-REG-008 | Partición (clase válida) | `name: "José Núñez"` | `Visitante registrado` |

`"a".repeat(49)` crea un texto con 49 letras `a`: así no las cuentas a mano.

Ejecuta `pnpm test visitor`. **Si un test falla, no lo cambies todavía**: primero comprueba que el test dice lo que dice la tabla. Si es así, déjalo en rojo y sigue.

## Paso 3: los casos de age (15 min)

En el `describe("age")`:

| ID | Técnica | Cambia | Esperado |
|---|---|---|---|
| TC-PLAN-REG-009 | Partición (clase válida) | `age: 30` | `Visitante registrado` (ya escrito) |
| TC-PLAN-REG-010 | Límite (vecino de fuera) | `age: 4` | Mensaje de edad |
| TC-PLAN-REG-011 | Límite (mínimo) | `age: 5` | `Visitante registrado` |
| TC-PLAN-REG-012 | Límite (vecino de dentro) | `age: 6` | `Visitante registrado` |
| TC-PLAN-REG-013 | Límite (vecino de dentro) | `age: 98` | `Visitante registrado` |
| TC-PLAN-REG-014 | Límite (máximo) | `age: 99` | `Visitante registrado` |
| TC-PLAN-REG-015 | Límite (vecino de fuera) | `age: 100` | Mensaje de edad |
| TC-PLAN-REG-016 | Partición (no entero) | `age: 10.5` | Mensaje de edad |

## Paso 4: los casos de schoolGroup (10 min)

En el `describe("schoolGroup")`. Aquí cambian dos campos, porque la regla relaciona los dos:

| ID | Técnica | Cambia | Esperado |
|---|---|---|---|
| TC-PLAN-REG-017 | Límite (vecino de dentro) | `age: 16, schoolGroup: true` | `Visitante registrado` |
| TC-PLAN-REG-018 | Límite (máximo) | `age: 17, schoolGroup: true` | `Visitante registrado` |
| TC-PLAN-REG-019 | Límite (vecino de fuera) | `age: 18, schoolGroup: true` | Mensaje de grupos escolares |

Ejecuta la suite completa del lab:

```bash
pnpm test visitor 2>&1 | grep '^Tests:'
# Tests:       4 failed, 15 passed, 19 total
```

Si te salen otros números, compara tus tests con las tablas antes de seguir: un esperado mal copiado también da rojo.

## Paso 5: documentar los hallazgos (15 min)

Ahora sí, abre `src/visitor.js`. Por cada test en rojo, copia el bloque de `hallazgos.md` y rellénalo: el caso, el test, `Expected` y `Received` del informe, si falla el código o el test, y la línea de `src/visitor.js` que lo causa.

Un ejemplo de lo que verás:

```text
  ● validateVisitor › age › should reject the visitor when age is 10.5

    expect(received).toBe(expected) // Object.is equality

    Expected: "La edad debe ser un número entero de 5 a 99"
    Received: "Visitante registrado"
```

Fíjate en qué técnica encontró cada bug: los límites encuentran los `<` y `>=` mal puestos; la partición encuentra la regla que falta. Para el 10,5, JavaScript tiene `Number.isInteger(valor)`, que devuelve `true` solo con enteros.

## Paso 6: arreglar y volver a verde (5 min)

Corrige `src/visitor.js` hasta que pasen los 19 tests. No toques ningún test: si el hallazgo dice que el fallo es del código, se arregla el código.

```bash
git add .
git commit -m "lab 03: casos del planetario automatizados"
```

---

## Comprobación final

```bash
pnpm test visitor 2>&1 | grep '^Tests:'
# Tests:       19 passed, 19 total
grep -c '^## Hallazgo' hallazgos.md
# 4
grep -c TODO hallazgos.md tests/visitor.test.js
# hallazgos.md:0
# tests/visitor.test.js:0
```

Repasa:

- [ ] Cada test cambia solo los campos de su fila y lleva encima el ID del caso.
- [ ] Cada hallazgo nombra la línea del código que lo causa y la técnica que lo encontró.
- [ ] Ningún esperado se cambió para que el test pasara.

## Qué has aprendido

- Una tabla de partición y valores límite se convierte en tests casi línea a línea; el ID en un comentario mantiene la trazabilidad con el caso.
- Los valores límite encuentran comparaciones mal escritas; la partición, reglas que nadie programó.
- Escribiste 19 tests casi iguales. La [semana 8](../../../week-08-matchers_avanzados_y_parametrizacion/README.md) los convierte en una tabla con `test.each`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - AAA y matchers](../lab-02-aaa-y-matchers/README.md) | [Lab 04 - Tests que mienten](../lab-04-tests-que-mienten/README.md) → |

[↑ Semana 03](../../README.md)
