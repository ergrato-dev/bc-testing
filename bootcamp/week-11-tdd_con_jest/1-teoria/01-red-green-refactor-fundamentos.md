# 01 - Fundamentos del Ciclo Red-Green-Refactor

> **Lenguaje:** JavaScript (Jest)

![Ciclo Red-Green-Refactor](../0-assets/01-ciclo-red-green-refactor.svg)

---

## Objetivo

Dominar el ciclo base de TDD y su impacto en calidad y diseno.

---

## Fases del ciclo

1. **Red**: escribir un test que falle por la razon correcta.
2. **Green**: implementar el minimo codigo para que pase.
3. **Refactor**: mejorar estructura sin cambiar comportamiento.

---

## Ejemplo minimo

```javascript
test("should apply 10 percent discount when user is premium", () => {
  const result = calculateDiscount(100, "premium");
  expect(result).toBe(90);
});
```

- En **Red**, `calculateDiscount` aun no existe o no cumple.
- En **Green**, implementas logica minima.
- En **Refactor**, limpias nombres, duplicacion y legibilidad.

---

## Mini-kata: formatear el contador de un show de planetario

El planetario necesita mostrar el tiempo restante de una proyeccion como `mm:ss`. Recibe segundos totales y debe devolver un string con dos digitos por unidad. Vamos a recorrer el ciclo completo, un test a la vez.

### Paso 1 - Red

Escribimos el primer test antes de que exista implementacion:

```javascript
// duration.test.js
const { formatDuration } = require("./duration");

test("should format 65 seconds as 01:05", () => {
  expect(formatDuration(65)).toBe("01:05");
});
```

Al correr `jest`, falla con `formatDuration is not a function`. Esa es la razon correcta: no hay implementacion, no un typo ni un test mal escrito.

### Paso 2 - Green (implementacion minima, sin pulir)

```javascript
// duration.js
function formatDuration(totalSeconds) {
  let m = Math.floor(totalSeconds / 60).toString();
  let s = (totalSeconds % 60).toString();
  if (m.length < 2) m = "0" + m;
  if (s.length < 2) s = "0" + s;
  return m + ":" + s;
}

module.exports = { formatDuration };
```

El test pasa. La implementacion funciona pero es ruidosa: concatenacion manual y padding repetido. No se toca todavia, primero hay evidencia en verde.

### Paso 3 - Refactor (con el test como red de proteccion)

```javascript
// duration.js (despues)
function formatDuration(totalSeconds) {
  const pad = (unit) => String(unit).padStart(2, "0");
  const minutes = Math.floor(totalSeconds / 60);
  const seconds = totalSeconds % 60;
  return `${pad(minutes)}:${pad(seconds)}`;
}

module.exports = { formatDuration };
```

Se corre `jest` de nuevo: sigue en verde. El comportamiento externo (`formatDuration(65) === "01:05"`) no cambio, solo la estructura interna. Eso es un refactor legitimo.

---

## Regla de oro

Si el test no falla primero, no tienes evidencia de que protege el comportamiento.

---

## Errores comunes

- Escribir varios tests antes de correr el primero.
- Implementar de mas en Green (por ejemplo, manejar horas cuando ningun test lo pide todavia).
- Refactorizar cambiando comportamiento sin nuevos tests.
- Saltar el paso de correr la suite despues del refactor, asumiendo que "se ve bien".
