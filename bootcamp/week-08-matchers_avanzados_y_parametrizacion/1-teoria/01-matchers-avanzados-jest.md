# 01 - Matchers Avanzados en Jest

**Tipo**: JavaScript (Jest)

![Mapa de matchers avanzados](../0-assets/01-mapa-matchers-avanzados.svg)

---

## Objetivo

Elegir el matcher correcto para expresar mejor la intencion del test, en vez de forzar `toBe`/`toEqual` para todo.

---

## Matchers de igualdad y estructura

- `toEqual`: compara estructura y valores (ignora `undefined` en propiedades).
- `toStrictEqual`: compara estructura, valores y tipos de forma estricta (distingue `undefined` explicito, tipos de clase, arrays dispersos).
- `toContain`: valida inclusion de un valor primitivo en un string o array.
- `toContainEqual`: valida inclusion por igualdad profunda en arrays de objetos.
- `toMatchObject`: valida parcialmente propiedades de un objeto, sin exigir coincidencia total.

```javascript
const exhibit = { id: 12, name: "Sala del Sistema Solar", capacity: 40 };

expect(exhibit).toMatchObject({ name: "Sala del Sistema Solar" });
expect([{ id: 1 }, { id: 2 }]).toContainEqual({ id: 2 });
expect(["planetario", "acuario"]).toContain("planetario");
```

## Matchers numericos

- `toBeCloseTo(numero, decimales)`: compara flotantes evitando errores de precision.
- `toBeGreaterThan` / `toBeGreaterThanOrEqual`: compara limites inferiores.
- `toBeLessThan` / `toBeLessThanOrEqual`: compara limites superiores.

```javascript
test("should calculate ticket discount with float precision", () => {
  const price = calculateDiscount(15.0, 0.1);

  expect(price).toBeCloseTo(13.5, 2);
  expect(price).toBeGreaterThan(0);
});
```

## Matchers de excepciones

- `toThrow()`: valida que la funcion lance cualquier error.
- `toThrow("mensaje")`: valida que el mensaje contenga ese texto.
- `toThrow(ClaseDeError)`: valida el tipo de error lanzado.

```javascript
function bookSeat(showId, seats) {
  if (seats <= 0) throw new RangeError("seats must be greater than zero");
  return { showId, seats };
}

test("should throw RangeError when seats is zero", () => {
  expect(() => bookSeat(1, 0)).toThrow(RangeError);
  expect(() => bookSeat(1, 0)).toThrow("seats must be greater than zero");
});
```

El matcher siempre envuelve una funcion (`() => bookSeat(...)`), nunca la llamada directa: Jest necesita ejecutarla dentro del `expect` para capturar la excepcion.

## Matchers de propiedades y forma

- `toHaveProperty("ruta.anidada", valor?)`: valida existencia (y opcionalmente valor) de una propiedad, incluso anidada.
- `toHaveLength(n)`: valida longitud de arrays o strings.

```javascript
const visit = { visitor: { name: "Ana", ticket: { type: "vip" } }, stops: ["domo", "observatorio"] };

expect(visit).toHaveProperty("visitor.ticket.type", "vip");
expect(visit.stops).toHaveLength(2);
```

## Preview: matchers asimetricos

`expect.any(Tipo)` y `expect.arrayContaining([...])` validan forma sin fijar valores exactos, utiles en tests asincronos donde algun campo cambia en cada ejecucion (timestamps, ids generados). Se retoma en la Semana 09.

```javascript
expect(createVisit()).toEqual({
  id: expect.any(Number),
  stops: expect.arrayContaining(["domo"]),
});
```

---

## Tabla comparativa

| Matcher | Uso principal | Cuidado |
|---|---|---|
| `toEqual` | Igualdad profunda de estructura | Ignora `undefined` |
| `toStrictEqual` | Igualdad profunda estricta | Puede romper tests "flexibles" |
| `toContain` | Valor primitivo en coleccion | No sirve para objetos |
| `toContainEqual` | Objeto en array por igualdad | Mas lento que `toContain` |
| `toMatchObject` | Coincidencia parcial | No detecta propiedades extra |
| `toBeCloseTo` | Comparacion de flotantes | Definir decimales relevantes |
| `toThrow` | Validar excepciones | Siempre envolver en funcion |
| `toHaveProperty` | Propiedad anidada | Usar ruta con puntos |

---

## Errores frecuentes

- Usar `toEqual` cuando se necesita distinguir tipos (`toStrictEqual`).
- Comparar flotantes con `toBe` en vez de `toBeCloseTo`.
- Llamar la funcion fuera del `expect` al probar `toThrow`, lo que rompe el test antes de la assertion.

---

## Regla practica

Un matcher preciso reduce falsos positivos y mejora lectura en revisiones: si el nombre del matcher describe la intencion, el test se explica solo.

![Seleccion de matcher](../0-assets/03-seleccion-matchers.svg)
