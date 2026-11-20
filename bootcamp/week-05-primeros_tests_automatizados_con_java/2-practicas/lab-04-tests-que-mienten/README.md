# Lab 04 - Tests que mienten

## Objetivo

Romper el código a propósito para comprobar si los tests lo notan, encontrar por qué cinco tests en verde no protegen nada y reescribirlos para que cada cambio peligroso los ponga en rojo.

## Tiempo estimado

50 minutos.

## Requisitos

- La [teoría 03](../../1-teoria/03-assertions-y-ejecucion.md).
- El proyecto `~/labs-semana-05` con git (lab 01).

## Punto de partida

```bash
cd ~/labs-semana-05
cp -r <ruta-del-bootcamp>/bootcamp/week-05-primeros_tests_automatizados_con_java/2-practicas/lab-04-tests-que-mienten/starter/. .
git add .
git commit -m "lab 04: punto de partida"
mvn test -Dtest=CartTest
```

```text
[INFO] Running com.bootcamp.CartTest
true
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.050 s -- in com.bootcamp.CartTest
```

El commit guarda el código tal como está, para poder volver a él después de romperlo. El `true` suelto lo imprime uno de los tests.

## Contexto

`src/main/java/com/bootcamp/Cart.java` calcula el carrito de la tienda del planetario: el total, el cupón `PLANETA10` (10 % de descuento) y los gastos de envío (gratis desde 30 €, como en la [semana 2](../../../week-02-casos_de_prueba_y_tecnicas_de_diseno/2-practicas/lab-03-tabla-de-decision/README.md)). Es el mismo carrito del [lab 04 de la semana 4](../../../week-04-primeros_tests_automatizados_con_python/2-practicas/lab-04-tests-que-mienten/README.md), en Java, con un `record` `Item` para cada línea. El código está bien. Los cinco tests pasan. ¿Sirven para algo?

Un test sirve si **falla cuando el código se rompe**. Para saberlo, lo rompes tú: cambias una línea, ejecutas los tests y miras si alguno se pone en rojo. A ese cambio se le llama **mutación**.

---

## Paso 1: cuatro mutaciones (15 min)

Haz cada mutación por separado: cambia la línea en `Cart.java`, guarda, ejecuta `mvn test -Dtest=CartTest` y anota cuántos tests fallan. Después deshaz el cambio:

```bash
git restore src/main/java/com/bootcamp/Cart.java
```

`git restore` devuelve el archivo a como estaba en el último commit.

| Mutación | Línea original | Línea mutada | Simula el bug |
|---|---|---|---|
| M1 | `total += item.price() * item.quantity();` | `total += item.price() + item.quantity();` | Operador equivocado |
| M2 | `throw new IllegalArgumentException("Invalid coupon");` | `return total;` | Cupón falso aceptado sin descuento |
| M3 | `return total * 0.9;` | `return total * 0.8;` | Descuento del 20 % en lugar del 10 % |
| M4 | `return total >= 30 ? 0 : 3.9;` | `return total > 30 ? 0 : 3.9;` | El pedido de 30,00 € paga envío |

`condición ? a : b` vale `a` si la condición se cumple y `b` si no: el `0 if total >= 30 else 3.9` de Python.

Con las cuatro, el resultado es el mismo:

```text
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] BUILD SUCCESS
```

Con la M1, el `true` del informe pasa a `false`, y aun así `BUILD SUCCESS`. Cuatro bugs, cero tests en rojo. La M4 es el bug de los 30,00 € que viste en el [lab 04 de la semana 2](../../../week-02-casos_de_prueba_y_tecnicas_de_diseno/2-practicas/lab-04-exploratorio-y-regresion/README.md): estos tests lo dejarían volver.

## Paso 2: por qué no fallan (10 min)

Lee los cinco tests de `CartTest.java` y escribe, en un comentario encima de cada uno, por qué pasa con el código roto. Pistas, una por test:

1. ¿Qué hace `System.out.println` con el resultado de `total == 25`? ([teoría 03](../../1-teoria/03-assertions-y-ejecucion.md#1-lo-que-el-compilador-frena-y-lo-que-no))
2. ¿Qué totales hacen pasar `assertTrue(total > 0)`?
3. Si `applyCoupon` no lanza nada, ¿se ejecuta el `catch`? ¿Y el `assertEquals` que hay dentro?
4. ¿De dónde sale `expected`? Si `applyCoupon` está mal, ¿cómo está `expected`?
5. El nombre habla de «30 o más». ¿Qué valor prueba?

En Python, el primer test escribía la comparación suelta. En Java eso no compila, así que el test miente de otra forma: imprime en lugar de comprobar.

## Paso 3: reescribir los tests (20 min)

Corrige cada test para que compruebe lo que dice su nombre:

- El esperado, escrito a mano: 25, 35, 45, con `assertEquals(esperado, resultado, 0.0001)`.
- El error, con `assertThrows` y `getMessage()`, como en el [lab 02](../lab-02-aaa-y-assertions/README.md#paso-2-calculatediscount-y-assertthrows-5-min) (añade `import static org.junit.jupiter.api.Assertions.assertThrows;` arriba).
- El envío, con los valores límite de la regla: 30 (gratis) y 29.99 (3.9). Cambia el nombre y el `@DisplayName` del quinto test para que digan el valor que prueba, y añade un sexto para el 29,99.

Ejecuta `mvn test -Dtest=CartTest`: los 6 pasan con el código sin mutar.

## Paso 4: repetir las mutaciones (5 min)

Vuelve a aplicar M1, M2, M3 y M4 una a una, con `git restore src/main/java/com/bootcamp/Cart.java` después de cada una. Ahora cada mutación pone al menos un test en rojo. Con la M4:

```text
[ERROR] Failures: 
[ERROR]   CartTest.shouldReturnFreeShippingWhenTotalIs30:54 expected: <0.0> but was: <3.9>
[INFO] 
[ERROR] Tests run: 6, Failures: 1, Errors: 0, Skipped: 0
```

Con la M2, el mensaje es el de `assertThrows` sin excepción: `Expected java.lang.IllegalArgumentException to be thrown, but nothing was thrown.`

Cuando las cuatro estén en rojo:

```bash
git add src/test/java/com/bootcamp/CartTest.java
git commit -m "lab 04: tests del carrito que detectan las cuatro mutaciones"
```

---

## Comprobación final

```bash
mvn test -Dtest=CartTest | grep "Tests run" | tail -1
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
git status --short src/main
# (nada: Cart.java está como en el commit)
grep -cE 'println|assertTrue|catch' src/test/java/com/bootcamp/CartTest.java
# 0
```

Repasa:

- [ ] Cada una de las cuatro mutaciones pone al menos un test en rojo.
- [ ] Todos los esperados están escritos a mano.
- [ ] El envío se prueba con 29,99 € y 30,00 €.

## Qué has aprendido

- Un test en verde no demuestra nada hasta que lo has visto fallar con el código roto.
- El compilador de Java frena alguna trampa, pero no todas: imprimir en vez de comprobar, `assertTrue(total > 0)`, `try/catch` sin comprobar que salta el error y el esperado calculado con el mismo código mienten igual que en JavaScript y Python.
- Romper el código a propósito y mirar qué tests caen es una técnica con nombre, *mutation testing*. Hay herramientas que hacen cientos de mutaciones solas; en Java la más usada es [PIT](https://pitest.org/).

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - De casos de prueba a tests](../lab-03-de-casos-a-tests/README.md) | [Proyecto — Proyecto - Primera suite de tests del dominio](../../3-proyecto/README.md) → |

[↑ Semana 05](../../README.md)
