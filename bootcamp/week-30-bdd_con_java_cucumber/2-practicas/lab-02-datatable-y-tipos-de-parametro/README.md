# Lab 02 - DataTable y tipos de parámetro

## Objetivo

Pasar tablas de Gherkin a los steps de tres formas (`DataTable`, lista de objetos con `@DataTableType` y comparación de una lista entera) y leer precios como `$12.500` con un `@ParameterType`.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
mvn test
# Escenario: Por debajo de $100.000 no hay descuento # classpath:features/carrito.feature:12
#   ✔ Dado el catálogo:                              # com.bootcamp.CartSteps.theCatalog(java.util.List)
#       | producto | precio  |
#       | Café     | $12.500 |
#       | Taza     | $30.000 |
#       | Molino   | $95.000 |
#   ✔ Cuando añado 2 Café al carrito                 # com.bootcamp.CartSteps.iAdd(int,java.lang.String)
#   ✔ Y añado 1 Taza al carrito                      # com.bootcamp.CartSteps.iAdd(int,java.lang.String)
#   ✔ Entonces el total es $55.000                   # com.bootcamp.CartSteps.theTotalIs(int)
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

El código de producción es el carrito de una tienda:

| Archivo | Qué hace |
|---|---|
| `Product` | `record` con nombre y precio en pesos |
| `CartLine` | `record` con producto, cantidad e importe |
| `Cart` | Se crea con el catálogo. `add(product, quantity)` acumula; `lines()`, `subtotal()`, `discount()` (10 % desde 100.000) y `total()` |

La suite y el `pom.xml` son los del lab 01. Como allí, cada PASO tiene Gherkin que añades al final del feature y código Java comentado en `CartSteps`.

## Paso a paso

### Paso 0: lee lo que ya funciona

Abre `carrito.feature` y `CartSteps` con la [teoría 02](../../1-teoria/02-tablas-tipos-y-esquemas.md) al lado:

1. `Antecedentes` declara el catálogo una vez para todos los escenarios. Cucumber lo ejecuta antes de cada uno: en la salida aparece como primer paso del escenario.
2. El step `el catálogo:` recibe `List<Product>`. La conversión la hace el método `product`, marcado con `@DataTableType`: recibe cada fila como `Map` con la cabecera como claves.
3. `{pesos}` lo define el método `pesos`, con `@ParameterType`. Reconoce `$55.000` y devuelve `55000`. `product` lo reutiliza para la columna `precio`.
4. `añado {int} {word} al carrito` usa `{word}`: el nombre del producto es una palabra.

### Paso 1: la tabla tal cual (PASO 1)

1. Añade al final del feature:

   ```gherkin
     Escenario: El subtotal suma las líneas
       Cuando añado al carrito:
         | producto | cantidad |
         | Café     | 2        |
         | Taza     | 1        |
       Entonces el subtotal es $55.000
       Y el total es $55.000
   ```

2. Descomenta todos los bloques del PASO 1: el `import` de `DataTable` y dos steps.

   ```bash
   mvn test
   # [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
   ```

`iAddToTheCart` recibe un `DataTable` y recorre `asMaps()`: un mapa por fila, con valores `String` que convierte con `Integer.parseInt`. Sirve, pero la conversión se queda en el step. Para pedidos de este tipo, de dos columnas sueltas, es suficiente.

### Paso 2: comparar una lista entera (PASO 2)

1. Añade al final del feature:

   ```gherkin
     Escenario: El mismo producto se acumula en una línea
       Cuando añado 2 Café al carrito
       Y añado 1 Taza al carrito
       Y añado 1 Café al carrito
       Entonces el carrito tiene estas líneas:
         | producto | cantidad | importe |
         | Café     | 3        | $37.500 |
         | Taza     | 1        | $30.000 |
   ```

2. Descomenta los dos bloques del PASO 2: el `@DataTableType` de `CartLine` y el step que compara.

   ```bash
   mvn test
   # [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
   ```

La tabla esperada se convierte en `List<CartLine>` y se compara entera con `containsExactlyElementsOf`. Como `CartLine` es un `record`, dos líneas son iguales si lo son sus tres campos.

### Paso 3: el descuento (PASO 3)

1. Añade al final del feature:

   ```gherkin
     Escenario: Desde $100.000 hay un 10 % de descuento
       Cuando añado 1 Molino al carrito
       Y añado 1 Café al carrito
       Entonces el subtotal es $107.500
       Y el descuento es $10.750
       Y el total es $96.750
   ```

2. Descomenta el PASO 3.

   ```bash
   mvn test
   # [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
   ```

Los importes se leen como en un ticket. Con `{int}` el paso diría `el descuento es 10750`.

## Experimentos

### Experimento 1: sin @DataTableType

Borra la anotación `@DataTableType` del método `product` (deja el método).

```bash
mvn test
# [ERROR] com.bootcamp.RunCucumberTest.Por debajo de $100.000 no hay descuento -- Time elapsed: 0.063 s <<< ERROR!
# io.cucumber.core.exception.CucumberException:
# Could not convert arguments for step [el catálogo:] defined at 'com.bootcamp.CartSteps.theCatalog(java.util.List<com.bootcamp.Product>)'.
# It appears you did not register a data table type.
```

Fallan todos los escenarios, porque todos empiezan por los `Antecedentes`. Deshaz el cambio.

### Experimento 2: una línea que no coincide

En el escenario del PASO 2, cambia la fila de `Café` de la tabla esperada por `| Café     | 2        | $25.000 |`.

```bash
mvn test
# [ERROR] com.bootcamp.RunCucumberTest.El mismo producto se acumula en una línea -- Time elapsed: 0.032 s <<< FAILURE!
# org.opentest4j.AssertionFailedError:
#
# Expecting actual:
#   [CartLine[product=Café, quantity=3, amount=37500],
#     CartLine[product=Taza, quantity=1, amount=30000]]
# to contain exactly (and in same order):
#   [CartLine[product=Café, quantity=2, amount=25000],
#     CartLine[product=Taza, quantity=1, amount=30000]]
# but some elements were not found:
#   [CartLine[product=Café, quantity=2, amount=25000]]
# and others were not expected:
#   [CartLine[product=Café, quantity=3, amount=37500]]
```

AssertJ dice qué línea falta y cuál sobra. Con tres `assertThat` sueltos, uno por celda, el primer fallo escondería los demás. Deshaz el cambio.

### Experimento 3: un precio que {pesos} no reconoce

1. En `Antecedentes`, cambia `$12.500` por `$12500` y ejecuta:

   ```bash
   mvn test
   # [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
   ```

   Todo pasa. El `@DataTableType` llama al método `pesos(...)` directamente, sin pasar por la expresión regular: quita el `$` y los puntos, y `12500` se lee bien.

2. Ahora cambia también `Entonces el total es $55.000` (el primer escenario) por `Entonces el total es $55000`:

   ```bash
   mvn test
   # [ERROR]   RunCucumberTest.Por debajo de $100.000 no hay descuento » UndefinedStep The step 'el total es $55000' is undefined.
   # [ERROR] Tests run: 4, Failures: 0, Errors: 1, Skipped: 0
   ```

   En el texto de un paso, la expresión regular sí cuenta: `$55000` no tiene el punto de los miles, el paso no casa con `{pesos}` y queda indefinido.

Deshaz los dos cambios.

## Comprobación final

```bash
mvn test
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
```

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Primer feature con Cucumber](../lab-01-primer-feature-con-cucumber/README.md) | [Lab 03 - Estado compartido con cucumber-spring](../lab-03-estado-compartido-con-cucumber-spring/README.md) → |

[↑ Semana 30](../../README.md)
