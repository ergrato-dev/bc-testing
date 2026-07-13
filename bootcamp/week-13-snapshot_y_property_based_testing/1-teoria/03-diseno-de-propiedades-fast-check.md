# 03 - Diseno de Propiedades con fast-check

> **Lenguaje:** JavaScript (Jest + fast-check)

![Mapa de diseno de invariantes](../0-assets/03-invariant-design-map.svg)
![Flujo de shrinking](../0-assets/04-shrinking-counterexample-flow.svg)

---

## Objetivo

Definir propiedades que representen reglas de negocio reales y utiles.

---

## Checklist para una buena propiedad

1. Expresa una regla universal del dominio.
2. Tiene input generado con restricciones coherentes.
3. Tiene oraculo claro para validar salida.
4. Falla con mensaje interpretable.

---

## Patrones utiles

- Idempotencia: aplicar dos veces equivale a una.
- Conservacion: una magnitud se mantiene (ej. longitud, suma total).
- Orden: salida debe permanecer ordenada bajo criterio.
- Limites: resultado dentro de rango permitido.

---

## Ejemplo completo: generadores combinados

Funcion bajo prueba — calcula el precio final de una entrada de museo aplicando un descuento por edad, sin dejarlo nunca negativo:

```javascript
function applyAgeDiscount(basePrice, age) {
  const discount = age < 12 || age >= 65 ? 0.5 : 0;
  return Math.max(0, Math.round(basePrice * (1 - discount)));
}
```

Propiedad: el precio final nunca debe ser negativo ni mayor al precio base.

```javascript
const fc = require("fast-check");

test("should keep final ticket price within valid bounds", () => {
  fc.assert(
    fc.property(fc.integer({ min: 0, max: 100_000 }), fc.integer({ min: 0, max: 120 }), (basePrice, age) => {
      const finalPrice = applyAgeDiscount(basePrice, age);

      expect(finalPrice).toBeGreaterThanOrEqual(0);
      expect(finalPrice).toBeLessThanOrEqual(basePrice);
    })
  );
});
```

Con `fc.string()` para validar, por ejemplo, que una funcion de slug nunca produce espacios:

```javascript
function slugifyHallName(name) {
  return name.trim().toLowerCase().replace(/\s+/g, "-");
}

test("should never leave whitespace in hall slug", () => {
  fc.assert(
    fc.property(fc.string(), (name) => {
      const slug = slugifyHallName(name);
      expect(slug).not.toMatch(/\s/);
    })
  );
});
```

---

## Interpretar un contraejemplo simplificado

Si una propiedad falla, fast-check imprime el input minimizado (shrunk) que la rompe, no el primer input aleatorio que fallo:

```text
Property failed after 12 tests
{ seed: 384712048, path: "3:2:1", endOnFailure: true }
Counterexample: [50, 65]
Shrunk 4 time(s)
Got error: expected 0 to be less than or equal to 50
```

Como leerlo:

- `Counterexample: [50, 65]` son los argumentos exactos (`basePrice=50`, `age=65`) que rompen la propiedad — ya reducidos al caso minimo, no el original aleatorio.
- `Shrunk 4 time(s)`: fast-check partio de un input mas grande/complejo y lo simplifico 4 veces buscando el minimo que sigue fallando; `seed` permite reproducir la misma corrida con `{ seed: 384712048 }`.
- El mensaje `expected ... to be less than or equal to ...` viene del `expect` que fallo dentro de la propiedad: revisa esa asercion primero, no todo el generador.

---

## Recomendacion

Combina tests de ejemplo (casos narrativos) con propiedades (cobertura amplia de entradas). Mejora la confianza sin depender solo de snapshots o de ejemplos puntuales.

---

## Errores frecuentes

- Generadores sin restricciones (`fc.integer()` sin `min`/`max`) cuando el dominio real tiene limites conocidos: genera ruido irrelevante.
- Ignorar el `seed` del fallo y no poder reproducirlo en la siguiente corrida.
- Escribir el oraculo con la misma formula que la implementacion (la propiedad nunca podria fallar).

---

## Regla practica

Ante un fallo, lee primero el `Counterexample` shrunkeado: es el caso mas simple posible, no ruido aleatorio.
