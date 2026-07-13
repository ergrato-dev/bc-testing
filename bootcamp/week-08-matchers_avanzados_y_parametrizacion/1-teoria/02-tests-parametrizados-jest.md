# 02 - Tests Parametrizados con `test.each`

**Tipo**: JavaScript (Jest)

![Flujo de test.each](../0-assets/02-flujo-test-each.svg)

---

## Por que parametrizar

Cuando varios tests solo cambian datos de entrada/salida, `test.each` evita duplicacion y hace explicita la tabla de casos que el codigo debe cumplir.

---

## Sintaxis: array de arrays

Cada fila es un array posicional; los valores se desestructuran como argumentos de la funcion de test.

```javascript
test.each([
  [10, 2, 5],
  [9, 3, 3],
  [0, 1, 0],
])("should divide %i by %i and return %i", (a, b, expected) => {
  expect(divide(a, b)).toBe(expected);
});
```

Los placeholders `%i` (entero), `%s` (string) y `%d` (numero) en el titulo se reemplazan en orden con los valores de la fila, asi cada caso aparece con nombre propio en el reporte.

---

## Sintaxis: template literal con tabla

Para casos con muchas columnas o datos con nombre, la tabla en template literal es mas legible que arrays posicionales.

```javascript
test.each`
  ticketType   | basePrice | discount
  ${"general"} | ${20}     | ${0}
  ${"student"} | ${20}     | ${0.3}
  ${"senior"}  | ${20}     | ${0.5}
`("should apply $discount discount for $ticketType ticket", ({ ticketType, basePrice, discount }) => {
  expect(calculatePrice(ticketType, basePrice)).toBeCloseTo(basePrice * (1 - discount), 2);
});
```

Aqui los nombres de columna (`$ticketType`, `$discount`) se interpolan directo en el titulo del test, sin depender del orden posicional.

---

## Cuando usar cada sintaxis

| Sintaxis | Conviene cuando... |
|---|---|
| Array de arrays | Pocos parametros (2-4), tipos simples |
| Template literal | Muchas columnas, datos con nombre, mejor lectura tabular |

---

## Combinando con `describe.each`

`describe.each` parametriza un bloque completo, util cuando varios tests comparten el mismo dato variable (por ejemplo, distintos tipos de visita a un planetario).

```javascript
describe.each(["general", "student", "senior"])("visit type: %s", (ticketType) => {
  test("should generate a valid ticket id", () => {
    expect(generateTicket(ticketType).id).toBeDefined();
  });

  test("should assign the correct ticket type", () => {
    expect(generateTicket(ticketType).type).toBe(ticketType);
  });
});
```

Cada combinacion de `describe.each` crea su propio grupo en el reporte, asi los fallos se ubican por tipo de visita sin leer todo el archivo.

---

## Beneficios

1. Menos codigo repetido.
2. Cobertura de mas combinaciones rapidamente.
3. Errores mas faciles de detectar por fila de datos.
4. La tabla de casos documenta reglas de negocio (limites, descuentos, validaciones) en un solo lugar.

---

## Buenas practicas

- Nombra bien cada fila/caso: usa `%s`/`%i` o interpolacion `$campo`, nunca dejes el titulo generico.
- Manten pocas columnas por tabla; si crecen mucho, separa en varias tablas por escenario.
- Combina `test.each` con AAA de forma explicita: arrange de datos ya esta en la fila, solo act y assert van en el cuerpo.
- Incluye casos limite (cero, negativos, vacios) junto a los casos felices en la misma tabla.

---

## Errores frecuentes

- Mezclar array de arrays y template literal en el mismo archivo sin razon, dificulta la lectura.
- Usar `%s` para un numero (el output se ve como texto), preferir `%i`/`%d` segun el tipo.
- Tablas con 8+ columnas: senal de que el caso de prueba necesita dividirse.

---

## Regla practica

Si copias y pegas un test cambiando solo un par de valores, esa es la senal para pasar a `test.each`.
