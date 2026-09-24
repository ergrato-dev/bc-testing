# 02 - Interpretar Métricas Sin Autoengaño

## Objetivo

Leer reportes de cobertura con pensamiento crítico para evitar decisiones basadas solo en porcentajes.

![Coverage vs confianza](../0-assets/02-coverage-vs-confidence.svg)

---

## Errores comunes

1. **Confundir cantidad con calidad**: 90% no implica suite robusta.
2. **Optimizar solo lo fácil**: subir líneas tocando código trivial.
3. **Ignorar branches**: los defectos suelen vivir en rutas alternativas.
4. **No considerar criticidad funcional**: una rama de pago pesa más que un helper de formato.

---

## Preguntas útiles al leer coverage

- ¿Qué reglas de negocio importantes aún no están verificadas?
- ¿Qué errores esperados no tienen test?
- ¿Qué tests solo validan el happy path?
- ¿Qué módulos tienen mucha cobertura pero assertions débiles?

---

## Señales de buena interpretación

- Se incorpora al menos un test por caso borde relevante.
- Se cubren rutas de fallo con mensajes/errores concretos.
- Se evita inflar números con tests redundantes.
- Las mejoras se relacionan con incidentes reales o riesgos conocidos.

---

## Ejemplo: 100% de líneas, bug real sin detectar

`lines` y `statements` cuentan si una línea se ejecutó, no si se ejecutó con **todas** las combinaciones de condiciones que importan. Un ternario anidado en una sola línea puede quedar "cubierto" con solo dos de sus tres rutas posibles.

```javascript
// pricing.js - calcula precio de entrada al Acuario
function calculateTicketPrice(basePrice, isMember, hasGroupDiscount) {
  const discount = isMember && hasGroupDiscount
    ? 1.3 // bug: debería ser 0.3 (30%), no 130%
    : isMember || hasGroupDiscount
      ? 0.15
      : 0;

  return basePrice * (1 - discount);
}

module.exports = { calculateTicketPrice };
```

```javascript
// pricing.test.js
const { calculateTicketPrice } = require("./pricing");

test("should apply 15% off when visitor is member only", () => {
  expect(calculateTicketPrice(100, true, false)).toBe(85);
});

test("should charge full price when visitor has no discounts", () => {
  expect(calculateTicketPrice(100, false, false)).toBe(100);
});
```

Estos dos tests ejecutan la única sentencia `return` de la función en cada corrida, así que `statements`, `lines` y `functions` marcan **100%**. Nunca se llama con `isMember = true` y `hasGroupDiscount = true` a la vez, que es la rama con el bug: `calculateTicketPrice(100, true, true)` devuelve `-30` (precio negativo) en producción.

`branches` es la única métrica que delata el hueco: el reporte marca la rama `isMember && hasGroupDiscount` como no ejecutada, aunque `lines` diga 100%. Un test que combine ambos flags rompe inmediatamente y expone el typo.

---

## Archivos invisibles: `collectCoverageFrom` y `coverageThreshold`

Por defecto, Jest solo mide los archivos que **algún test importa**. Un módulo sin ningún test no aparece en el reporte, así que el porcentaje global sale alto justamente porque falta lo peor. Supongamos que junto a `pricing.js` existe `refund.js` (reembolsos del Acuario) y nadie le escribió tests. Con `pnpm test:coverage` sin configuración:

```text
------------|---------|----------|---------|---------|-------------------
File        | % Stmts | % Branch | % Funcs | % Lines | Uncovered Line #s
------------|---------|----------|---------|---------|-------------------
All files   |     100 |     87.5 |     100 |     100 |
 pricing.js |     100 |     87.5 |     100 |     100 | 2
------------|---------|----------|---------|---------|-------------------
```

`refund.js` no existe para el reporte. La solución es declarar que archivos **deben** medirse y un umbral mínimo que haga fallar el comando:

```javascript
// jest.config.js (también vale la clave "jest" en package.json)
module.exports = {
  // Todos los .js de la raíz cuentan, los importe un test o no.
  collectCoverageFrom: ["*.js", "!*.test.js", "!jest.config.js"],
  // Si una métrica queda por debajo, Jest termina con código de salida 1.
  coverageThreshold: {
    global: { branches: 80, lines: 80 },
  },
};
```

Salida real con esa configuración:

```text
------------|---------|----------|---------|---------|-------------------
File        | % Stmts | % Branch | % Funcs | % Lines | Uncovered Line #s
------------|---------|----------|---------|---------|-------------------
All files   |   42.85 |       70 |      50 |   42.85 |
 pricing.js |     100 |     87.5 |     100 |     100 | 2
 refund.js  |       0 |        0 |       0 |       0 | 2-9
------------|---------|----------|---------|---------|-------------------
Jest: Coverage for branches (70%) does not meet "global" threshold (80%)
Jest: Coverage for lines (42.85%) does not meet "global" threshold (80%)
```

El 100% de líneas era un autoengaño: el proyecto real está al 42.85%. Con el umbral, `pnpm test:coverage` falla en local y en CI hasta que alguien cubra `refund.js` o baje el umbral de forma explícita y justificada.

Los patrones de `collectCoverageFrom` son globs relativos a la raíz del proyecto; en proyectos con carpeta `src/` lo habitual es `["src/**/*.js", "!src/**/*.test.js"]`. `coverageThreshold` acepta también claves por ruta (por ejemplo `"./src/pricing.js": { branches: 100 }`) para exigir más en módulos críticos.

---

## Leer el reporte HTML de Istanbul (rojo / amarillo / verde)

Al correr `pnpm test:coverage`, Jest genera `coverage/lcov-report/index.html`. Abrirlo en el navegador muestra el árbol de archivos con porcentajes; entrar a un archivo muestra el código fuente coloreado línea por línea.

| Color | Significado |
|---|---|
| Verde | Sentencia y todas sus ramas se ejecutaron al menos una vez. |
| Amarillo | La línea se ejecutó, pero **una rama** de una condición no (ej. un `if` sin `else`, o un lado de un ternario/`&&`/`??`). |
| Rojo | La sentencia, función o rama nunca se ejecutó. |

Además de los colores, Istanbul anota marcadores directamente sobre el código:

- `I` (if): el bloque `if` correspondiente no se evaluó con ese valor.
- `E` (else): la rama `else` (implícita o explícita) nunca se tomó.
- Números pequeños junto a una línea: cuántas veces se ejecutó (útil para detectar tests redundantes que repiten el mismo camino).

En el ejemplo anterior, la línea del ternario aparece en **amarillo**, con un marcador sobre la rama `isMember && hasGroupDiscount ? 1.3` indicando que nunca se tomó ese camino, aunque la línea en si se pinte como ejecutada.

### Regla práctica al leer el reporte

1. No cierres una revisión de coverage mirando solo el resumen (`% Stmts`, `% Branch`) de la carpeta raíz: entra a los módulos críticos.
2. Prioriza amarillos y rojos en archivos con reglas de negocio antes que en utilidades.
3. Un archivo 100% verde en `lines` pero con amarillos en `branches` sigue teniendo huecos reales.

---

## Mini checklist operativo

- [ ] Configurar `collectCoverageFrom` para que ningún archivo quede fuera del reporte.
- [ ] Definir `coverageThreshold` para que el comando falle si la cobertura baja.
- [ ] Revisar `branches` primero en módulos críticos.
- [ ] Agregar pruebas de error/validación.
- [ ] Refactorizar tests frágiles antes de agregar más cantidad.
- [ ] Repetir ejecución para verificar estabilidad (no flaky).
- [ ] Abrir el reporte HTML y revisar amarillos/rojos en módulos de alto riesgo, no solo el resumen global.
