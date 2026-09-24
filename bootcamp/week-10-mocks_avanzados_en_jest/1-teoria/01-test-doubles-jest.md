# 01 - Test Doubles en Jest: Mock, Stub y Spy

> **Lenguaje:** JavaScript (Jest)

![Mapa de test doubles](../0-assets/01-test-doubles-mapa.svg)

---

## Objetivo

Entender qué problema resuelve cada tipo de test double y elegirlo con criterio.

---

## Qué es un test double

Un test double es una versión controlada de una dependencia real. Su función principal es aislar la unidad bajo prueba para validar comportamiento sin depender de sistemas externos.

---

## Diferencias clave

| Tipo | Propósito | Ejemplo en Jest |
|---|---|---|
| Mock | Reemplazar y verificar interacciones | `const fn = jest.fn()` |
| Stub | Retornar datos predecibles | `fn.mockResolvedValue(data)` |
| Spy | Observar una función real sin reescribir toda la dependencia | `jest.spyOn(obj, "method")` |

---

## Ejemplo rápido

```javascript
const taxCalculator = {
  getRate(country) {
    if (country === "PE") return 0.18;
    return 0.2;
  },
};

function priceWithTax(amount, country) {
  return amount * (1 + taxCalculator.getRate(country));
}

test("should ask the tax rate of the country when pricing", () => {
  const rateSpy = jest.spyOn(taxCalculator, "getRate");

  const price = priceWithTax(100, "PE");

  expect(price).toBeCloseTo(118);
  expect(rateSpy).toHaveBeenCalledWith("PE");
  rateSpy.mockRestore();
});
```

El spy se verifica después de ejecutar la unidad bajo prueba (`priceWithTax`). Si el test llamara a `taxCalculator.getRate` directamente y luego verificara el spy, solo comprobaría su propia llamada: una tautología que pasa aunque `priceWithTax` deje de usar el calculador.

---

## Regla práctica de elección

1. Si solo necesitas respuestas controladas: usa stub.
2. Si necesitas validar cuántas veces o con qué argumentos se invoca: usa mock.
3. Si quieres observar una implementación existente sin perder su lógica por defecto: usa spy.

---

## Errores frecuentes

- Mockear la función que realmente quieres validar.
- No restaurar spies y contaminar otros tests.
- Assertar demasiados detalles internos que cambian con refactors.
