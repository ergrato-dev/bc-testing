# 02 - Interpretar Metricas Sin Autoengano

## Objetivo

Leer reportes de cobertura con pensamiento critico para evitar decisiones basadas solo en porcentajes.

![Coverage vs confianza](../0-assets/02-coverage-vs-confidence.svg)

---

## Errores comunes

1. **Confundir cantidad con calidad**: 90% no implica suite robusta.
2. **Optimizar solo lo facil**: subir lineas tocando codigo trivial.
3. **Ignorar branches**: los defectos suelen vivir en rutas alternativas.
4. **No considerar criticidad funcional**: una rama de pago pesa mas que un helper de formato.

---

## Preguntas utiles al leer coverage

- Que reglas de negocio importantes aun no estan verificadas?
- Que errores esperados no tienen test?
- Que tests solo validan el happy path?
- Que modulos tienen mucha cobertura pero assertions debiles?

---

## Señales de buena interpretacion

- Se incorpora al menos un test por caso borde relevante.
- Se cubren rutas de fallo con mensajes/errores concretos.
- Se evita inflar numeros con tests redundantes.
- Las mejoras se relacionan con incidentes reales o riesgos conocidos.

---

## Ejemplo: 100% de lineas, bug real sin detectar

`lines` y `statements` cuentan si una linea se ejecuto, no si se ejecuto con **todas** las combinaciones de condiciones que importan. Un ternario anidado en una sola linea puede quedar "cubierto" con solo dos de sus tres rutas posibles.

```javascript
// pricing.js - calcula precio de entrada al Acuario
function calculateTicketPrice(basePrice, isMember, hasGroupDiscount) {
  const discount = isMember && hasGroupDiscount
    ? 1.3 // bug: deberia ser 0.3 (30%), no 130%
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

test("member alone gets 15% off", () => {
  expect(calculateTicketPrice(100, true, false)).toBe(85);
});

test("no discounts applies full price", () => {
  expect(calculateTicketPrice(100, false, false)).toBe(100);
});
```

Estos dos tests ejecutan la unica sentencia `return` de la funcion en cada corrida, asi que `statements`, `lines` y `functions` marcan **100%**. Nunca se llama con `isMember = true` y `hasGroupDiscount = true` a la vez, que es la rama con el bug: `calculateTicketPrice(100, true, true)` devuelve `-30` (precio negativo) en produccion.

`branches` es la unica metrica que delata el hueco: el reporte marca la rama `isMember && hasGroupDiscount` como no ejecutada, aunque `lines` diga 100%. Un test que combine ambos flags rompe inmediatamente y expone el typo.

---

## Leer el reporte HTML de Istanbul (rojo / amarillo / verde)

Al correr `yarn test --coverage`, Jest genera `coverage/lcov-report/index.html`. Abrirlo en el navegador muestra el arbol de archivos con porcentajes; entrar a un archivo muestra el codigo fuente coloreado linea por linea.

| Color | Significado |
|---|---|
| Verde | Sentencia y todas sus ramas se ejecutaron al menos una vez. |
| Amarillo | La linea se ejecuto, pero **una rama** de una condicion no (ej. un `if` sin `else`, o un lado de un ternario/`&&`/`??`). |
| Rojo | La sentencia, funcion o rama nunca se ejecuto. |

Ademas de los colores, Istanbul anota marcadores directamente sobre el codigo:

- `I` (if): el bloque `if` correspondiente no se evaluo con ese valor.
- `E` (else): la rama `else` (implicita o explicita) nunca se tomo.
- Numeros pequeños junto a una linea: cuantas veces se ejecuto (util para detectar tests redundantes que repiten el mismo camino).

En el ejemplo anterior, la linea del ternario aparece en **amarillo**, con un marcador sobre la rama `isMember && hasGroupDiscount ? 1.3` indicando que nunca se tomo ese camino, aunque la linea en si se pinte como ejecutada.

### Regla practica al leer el reporte

1. No cierres una revision de coverage mirando solo el resumen (`% Stmts`, `% Branch`) de la carpeta raiz: entra a los modulos criticos.
2. Prioriza amarillos y rojos en archivos con reglas de negocio antes que en utilidades.
3. Un archivo 100% verde en `lines` pero con amarillos en `branches` sigue teniendo huecos reales.

---

## Mini checklist operativo

- [ ] Revisar `branches` primero en modulos criticos.
- [ ] Agregar pruebas de error/validacion.
- [ ] Refactorizar tests fragiles antes de agregar mas cantidad.
- [ ] Repetir ejecucion para verificar estabilidad (no flaky).
- [ ] Abrir el reporte HTML y revisar amarillos/rojos en modulos de alto riesgo, no solo el resumen global.
