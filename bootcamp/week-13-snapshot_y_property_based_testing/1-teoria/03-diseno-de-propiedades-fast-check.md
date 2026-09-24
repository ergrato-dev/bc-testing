# 03 - Diseño de Propiedades con fast-check

> **Lenguaje:** JavaScript (Jest + fast-check)

![Mapa de diseño de invariantes](../0-assets/03-invariant-design-map.svg)
![Flujo de shrinking](../0-assets/04-shrinking-counterexample-flow.svg)

---

## Objetivo

Definir propiedades que representen reglas de negocio reales y útiles.

---

## Checklist para una buena propiedad

1. Expresa una regla universal del dominio.
2. Tiene input generado con restricciones coherentes.
3. Tiene oráculo claro para validar salida.
4. Falla con mensaje interpretable.

---

## Patrones útiles

- Idempotencia: aplicar dos veces equivale a una.
- Conservación: una magnitud se mantiene (ej. longitud, suma total).
- Orden: salida debe permanecer ordenada bajo criterio.
- Límites: resultado dentro de rango permitido.

---

## Ejemplo completo: generadores combinados

Función bajo prueba — calcula el precio final de una entrada de museo aplicando un descuento por edad, sin dejarlo nunca negativo:

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

Para textos, `fc.string()` por defecto casi nunca genera tabs ni saltos de línea. Si la regla trata sobre whitespace, conviene un generador que lo incluya de forma explícita con la opción `unit`:

```javascript
// Strings hechos solo con estas unidades: letras y whitespace real.
const hallName = fc.string({ unit: fc.constantFrom("a", "B", " ", "\t", "\n") });

function slugifyHallName(name) {
  return name.trim().toLowerCase().replace(/\s+/g, "-");
}

test("should never leave whitespace in hall slug", () => {
  fc.assert(
    fc.property(hallName, (name) => {
      const slug = slugifyHallName(name);
      expect(slug).not.toMatch(/\s/);
    }),
  );
});
```

---

## Interpretar un contraejemplo simplificado

Supongamos una versión con bug que solo reemplaza espacios (`/ +/g`) en lugar de cualquier whitespace (`/\s+/g`):

```javascript
function slugifyHallName(name) {
  return name.trim().toLowerCase().replace(/ +/g, "-");
}
```

Con la misma propiedad, Jest muestra (salida real, recortada):

```text
FAIL ./slug.test.js
  ● should never leave whitespace in hall slug

    Property failed after 1 tests
    { seed: 1987581203, path: "0:1:7:7", endOnFailure: true }
    Counterexample: ["a\na"]
    Shrunk 3 time(s)

    ...

    Cause:
        expect(received).not.toMatch(expected)

        Expected pattern: not /\s/
        Received string:      "a
        a"
```

Cómo leerlo:

- `Counterexample: ["a\na"]` es el array de argumentos de la propiedad (aquí uno solo, `name`). Ya está reducido: es el input más simple que fast-check encontró que sigue fallando, no el string aleatorio original.
- `Shrunk 3 time(s)`: fast-check partió del primer input que falló y lo simplificó 3 veces.
- `seed` y `path` permiten reproducir exactamente la misma corrida: `fc.assert(property, { seed: 1987581203, path: "0:1:7:7" })`. El `seed` cambia en cada ejecución, por eso hay que copiarlo del fallo.
- `Cause` es el `expect` que falló dentro de la propiedad: el slug conserva un salto de línea. El contraejemplo apunta directo al bug (el regex no cubre `\n`).

---

## Recomendación

Combina tests de ejemplo (casos narrativos) con propiedades (cobertura amplia de entradas). Mejora la confianza sin depender solo de snapshots o de ejemplos puntuales.

---

## Errores frecuentes

- Generadores sin restricciones (`fc.integer()` sin `min`/`max`) cuando el dominio real tiene límites conocidos: genera ruido irrelevante.
- Ignorar el `seed` del fallo y no poder reproducirlo en la siguiente corrida.
- Escribir el oráculo con la misma formula que la implementación (la propiedad nunca podría fallar).

---

## Regla práctica

Ante un fallo, lee primero el `Counterexample` shrunkeado: es el caso más simple posible, no ruido aleatorio.
