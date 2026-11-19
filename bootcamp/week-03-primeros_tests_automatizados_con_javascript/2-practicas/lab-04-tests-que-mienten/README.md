# Lab 04 - Tests que mienten

## Objetivo

Romper el código a propósito para comprobar si los tests lo notan, encontrar por qué cinco tests en verde no protegen nada y reescribirlos para que cada cambio peligroso los ponga en rojo.

## Tiempo estimado

50 minutos.

## Requisitos

- La [teoría 03](../../1-teoria/03-matchers-y-ejecucion.md).
- El proyecto `~/labs-semana-03` con git (lab 01).

## Punto de partida

```bash
cd ~/labs-semana-03
cp -r <ruta-del-bootcamp>/bootcamp/week-03-primeros_tests_automatizados_con_javascript/2-practicas/lab-04-tests-que-mienten/starter/. .
git add .
git commit -m "lab 04: punto de partida"
pnpm test cart
```

```text
PASS tests/cart.test.js
  cart
    ✓ should return 25 when cart has a 10 euro item and a 15 euro item (1 ms)
    ✓ should return 35 when cart has two 10 euro items and a 15 euro item (1 ms)
    ✓ should throw an error when coupon is unknown (1 ms)
    ✓ should return 45 when coupon PLANETA10 is applied to 50
    ✓ should return free shipping when total is 30 or more

Tests:       5 passed, 5 total
```

El commit guarda el código tal como está, para poder volver a él después de romperlo.

## Contexto

`src/cart.js` calcula el carrito de la tienda del planetario: el total, el cupón `PLANETA10` (10 % de descuento) y los gastos de envío (gratis desde 30 €, como en la [semana 2](../../../week-02-casos_de_prueba_y_tecnicas_de_diseno/2-practicas/lab-03-tabla-de-decision/README.md)). El código está bien. Los cinco tests pasan. ¿Sirven para algo?

Un test sirve si **falla cuando el código se rompe**. Para saberlo, lo rompes tú: cambias una línea, ejecutas los tests y miras si alguno se pone en rojo. A ese cambio se le llama **mutación**.

---

## Paso 1: cuatro mutaciones (15 min)

Haz cada mutación por separado: cambia la línea en `src/cart.js`, guarda, ejecuta `pnpm test cart` y anota cuántos tests fallan. Después deshaz el cambio:

```bash
git restore src/cart.js
```

`git restore` devuelve el archivo a como estaba en el último commit.

| Mutación | Línea original | Línea mutada | Simula el bug |
|---|---|---|---|
| M1 | `sum + item.price * item.quantity` | `sum + item.price + item.quantity` | Operador equivocado |
| M2 | `throw new Error("Invalid coupon");` | `return total;` | Cupón falso aceptado sin descuento |
| M3 | `return total * 0.9;` | `return total * 0.8;` | Descuento del 20 % en lugar del 10 % |
| M4 | `return total >= 30 ? 0 : 3.9;` | `return total > 30 ? 0 : 3.9;` | El pedido de 30,00 € paga envío |

Con las cuatro, el resultado es el mismo:

```text
Tests:       5 passed, 5 total
```

Cuatro bugs, cero tests en rojo. La M4 es el bug de los 30,00 € que viste en el [lab 04 de la semana 2](../../../week-02-casos_de_prueba_y_tecnicas_de_diseno/2-practicas/lab-04-exploratorio-y-regresion/README.md): estos tests lo dejarían volver.

## Paso 2: por qué no fallan (10 min)

Lee los cinco tests de `tests/cart.test.js` y escribe, en un comentario encima de cada uno, por qué pasa con el código roto. Pistas, una por test:

1. ¿Qué pasa con el resultado de `total == 25`? ([teoría 03](../../1-teoria/03-matchers-y-ejecucion.md#1-aserción-frente-a-comparación))
2. ¿Qué números cumplen `toBeTruthy`?
3. Si `applyCoupon` no lanza nada, ¿se ejecuta el `catch`? ¿Y el `expect` que hay dentro?
4. ¿De dónde sale `expected`? Si `applyCoupon` está mal, ¿cómo está `expected`?
5. El nombre habla de «30 o más». ¿Qué valor prueba?

## Paso 3: reescribir los tests (20 min)

Corrige cada test para que compruebe lo que dice su nombre:

- El esperado, escrito a mano: 25, 35, 45.
- El error, con `expect(() => ...).toThrow("Invalid coupon")`.
- El envío, con los valores límite de la regla: 30 (gratis) y 29.99 (3.9). Cambia el nombre del quinto test para que diga el valor que prueba, y añade un sexto para el 29,99.

Ejecuta `pnpm test cart`: los 6 pasan con el código sin mutar.

## Paso 4: repetir las mutaciones (5 min)

Vuelve a aplicar M1, M2, M3 y M4 una a una, con `git restore src/cart.js` después de cada una. Ahora cada mutación pone al menos un test en rojo. Con la M4:

```text
    ✕ should return free shipping when total is 30 (2 ms)
Tests:       1 failed, 5 passed, 6 total
```

Cuando las cuatro estén en rojo:

```bash
git add tests/cart.test.js
git commit -m "lab 04: tests del carrito que detectan las cuatro mutaciones"
```

---

## Comprobación final

```bash
pnpm test cart 2>&1 | grep '^Tests:'
# Tests:       6 passed, 6 total
git status --short src
# (nada: src/cart.js está como en el commit)
grep -cE 'toBeTruthy|==|catch' tests/cart.test.js
# 0
```

Repasa:

- [ ] Cada una de las cuatro mutaciones pone al menos un test en rojo.
- [ ] Todos los esperados están escritos a mano.
- [ ] El envío se prueba con 29,99 € y 30,00 €.

## Qué has aprendido

- Un test en verde no demuestra nada hasta que lo has visto fallar con el código roto.
- Los cuatro patrones que mienten: sin `expect`, `toBeTruthy` con números, `try/catch` sin comprobar que salta el error y el esperado calculado con el mismo código.
- Romper el código a propósito y mirar qué tests caen es una técnica con nombre, *mutation testing*. En la [semana 21](../../../week-21-coverage_y_calidad_en_python/README.md) una herramienta hace cientos de mutaciones sola.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - De casos de prueba a tests](../lab-03-de-casos-a-tests/README.md) | [Proyecto — Proyecto - Primera suite de tests del dominio](../../3-proyecto/README.md) → |

[↑ Semana 03](../../README.md)
