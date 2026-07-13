# 02 - Micro-ciclos TDD y Diseno Emergente

> **Lenguaje:** JavaScript (Jest)

![Micro-pasos TDD](../0-assets/02-micro-pasos-tdd.svg)
![Arbol de decision del siguiente test](../0-assets/03-arbol-decision-siguiente-test.svg)

---

## Objetivo

Reducir riesgo con pasos pequenos y decisiones guiadas por comportamiento.

---

## Por que micro-ciclos

Los micro-ciclos permiten feedback rapido, menor complejidad y deteccion temprana de problemas de diseno.

---

## Secuencia recomendada

1. Agrega un caso simple (happy path).
2. Hazlo pasar con implementacion minima.
3. Agrega un borde (input invalido).
4. Ajusta implementacion y refactoriza.
5. Repite.

---

## Patron de crecimiento

- De ejemplo concreto a regla general.
- De un escenario a variaciones controladas.
- De implementacion directa a abstracciones necesarias.

---

## Senales de buen diseno emergente

- Funciones pequenas y con responsabilidad clara.
- Menor acoplamiento entre modulos.
- Pruebas faciles de leer y mantener.

---

## Fake it till you make it

Tecnica para el primer test: en Green, devuelves una constante que satisface el unico caso conocido, sin construir logica que ningun test exige aun. El proximo test obliga a generalizar.

```javascript
function admissionCategory(age) {
  return "infantil";
}
```

No es pereza, es evitar diseno especulativo: la logica real la va a pedir el siguiente test, no la imaginacion del autor.

---

## Triangulacion: cada test empuja el diseno

Ejemplo: `admissionCategory(age)` debe clasificar visitantes de un planetario en `"infantil"`, `"adulto"` o `"senior"`.

### Test 1 - fake it

```javascript
test("should classify age 10 as infantil", () => {
  expect(admissionCategory(10)).toBe("infantil");
});
```

Implementacion minima (constante, ver seccion anterior). Pasa, pero es obviamente incompleta.

### Test 2 - fuerza logica real

```javascript
test("should classify age 30 as adulto", () => {
  expect(admissionCategory(30)).toBe("adulto");
});
```

La constante ya no alcanza. Un solo `if` no basta con dos categorias, asi que aparece la primera condicion real:

```javascript
function admissionCategory(age) {
  if (age < 18) return "infantil";
  return "adulto";
}
```

### Test 3 - triangula el borde que falta

```javascript
test("should classify age 65 as senior", () => {
  expect(admissionCategory(65)).toBe("senior");
});
```

Ahora el diseno se completa con el tercer caso, sin anticiparlo antes de tiempo:

```javascript
function admissionCategory(age) {
  if (age < 18) return "infantil";
  if (age < 60) return "adulto";
  return "senior";
}
```

Tres tests, tres decisiones de diseno, cada una motivada por un caso concreto y no por especulacion. Eso es triangulacion: el conjunto de tests "acorrala" la implementacion hasta que la regla general emerge sola.

---

## Errores frecuentes

- Escribir la regla general completa en el primer Green, sin dejar que los tests la motiven.
- Agregar categorias o parametros que ningun test pide todavia.
- Triangular con casos redundantes que no agregan una decision de diseno nueva.
