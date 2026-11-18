# 02 - Tablas, tipos de parámetro y esquemas

> Lenguaje: **Java**

---

## 🎯 Objetivos

- Recibir una tabla de Gherkin en un step como `DataTable`, como lista de objetos o como mapa.
- Convertir filas en objetos con `@DataTableType`.
- Definir un tipo de parámetro propio con `@ParameterType`.
- Usar `Antecedentes` y `Esquema del escenario` sin repetir pasos.

---

## De dónde partes

En la [semana 22](../../week-22-bdd_con_python_behave/1-teoria/02-behave-steps-context-y-hooks.md#tablas-de-datos) leías una tabla con `context.table` y recorrías sus filas como diccionarios. En Java la tabla llega como parámetro del método, y Cucumber puede convertirla antes de entregarla: el método declara el tipo que quiere y Cucumber hace el resto.

---

## La tabla tal cual: DataTable

```gherkin
Cuando añado al carrito:
  | producto | cantidad |
  | Café     | 2        |
  | Taza     | 1        |
```

```java
@When("añado al carrito:")
public void iAddToTheCart(DataTable table) {
    for (Map<String, String> row : table.asMaps()) {
        cart.add(row.get("producto"), Integer.parseInt(row.get("cantidad")));
    }
}
```

Una tabla es siempre el último parámetro del método. La expresión no lleva nada para ella: los dos puntos del final son texto del paso. `asMaps()` devuelve una lista con un mapa por fila; las claves son la cabecera. Los valores son siempre `String`: la conversión es tuya.

`DataTable` tiene otros métodos (`asLists()`, `cells()`, `column(0)`), pero si la tabla representa objetos del dominio hay una forma mejor.

---

## Filas como objetos: @DataTableType

```gherkin
Dado el catálogo:
  | producto | precio  |
  | Café     | $12.500 |
  | Taza     | $30.000 |
```

```java
@DataTableType
public Product product(Map<String, String> row) {
    return new Product(row.get("producto"), pesos(row.get("precio")));
}

@Given("el catálogo:")
public void theCatalog(List<Product> products) {
    cart = new Cart(products);
}
```

- **Qué:** un método de una clase del glue que convierte una fila (un `Map` con la cabecera como claves) en un objeto.
- **Para qué:** que los steps reciban `List<Product>` y no repitan la conversión.
- **Impacto:** el tipo vale para cualquier step de cualquier clase. Si no lo registras, el step falla al convertir:

```text
io.cucumber.core.exception.CucumberException:
Could not convert arguments for step [el catálogo:] defined at 'com.bootcamp.CartSteps.theCatalog(java.util.List<com.bootcamp.Product>)'.
It appears you did not register a data table type.
```

La misma conversión sirve para comprobar. Si el resultado es una lista de `record`, compárala entera con la tabla esperada:

```java
@Then("el carrito tiene estas líneas:")
public void theCartHasTheseLines(List<CartLine> expected) {
    assertThat(cart.lines()).containsExactlyElementsOf(expected);
}
```

Si una línea no coincide, AssertJ dice cuál falta y cuál sobra ([lab 02](../2-practicas/lab-02-datatable-y-tipos-de-parametro/README.md#experimento-2-una-línea-que-no-coincide)).

### Dos columnas sin cabecera: Map

Una tabla de dos columnas se puede recibir como `Map`: la primera columna es la clave y la segunda el valor, ya convertido al tipo que declares.

```gherkin
Entonces la devolución cobra:
  | días de retraso | 3    |
  | recargo         | 150  |
  | daño            | 1000 |
  | total           | 1150 |
```

```java
@Then("la devolución cobra:")
public void theReturnCharges(Map<String, Long> expected) { ... }
```

Va bien para un resultado con varios campos con nombre, como el del [proyecto](../3-proyecto/README.md#paso-5-la-devolución).

---

## Tipos de parámetro propios

`{int}` no reconoce `$12.500`. Un `@ParameterType` enseña a Cucumber un tipo nuevo:

```java
@ParameterType("\\$\\d{1,3}(?:\\.\\d{3})*")
public Integer pesos(String text) {
    return Integer.parseInt(text.replace("$", "").replace(".", ""));
}

@Then("el total es {pesos}")
public void theTotalIs(int expected) { ... }
```

| Pieza | Qué es |
|---|---|
| La expresión regular | Qué texto reconoce: un `$` y cifras en grupos de tres separados por puntos |
| El nombre del método | El nombre del tipo en las expresiones: `{pesos}`. Se puede poner otro con `name = "..."` |
| El valor devuelto | Lo que recibe el step |

Como `@DataTableType`, un tipo de parámetro es global al glue: defínelo una vez, en la clase de steps que lo usa o en una clase solo para tipos, y úsalo en todas. Si el texto del paso no casa con la expresión regular, el paso no casa con el step y queda indefinido.

Un tipo propio también hace los pasos más legibles cuando el valor es una palabra del negocio:

```java
@ParameterType(name = "estado", value = "en buen estado|dañada")
public Condition condition(String text) {
    return text.equals("dañada") ? Condition.DAMAGED : Condition.GOOD;
}

@When("{word} devuelve la pieza {word} {estado}")
public void theInstitutionReturns(String institution, String code, Condition condition) { ... }
```

El feature dice `MUS-PRADO devuelve la pieza MUS-002 dañada`, no `con condition DAMAGED`.

---

## Antecedentes

`Antecedentes` son los pasos que se repiten al principio de todos los escenarios de un feature. Cucumber los ejecuta antes de cada escenario:

```gherkin
Antecedentes:
  Dado el catálogo:
    | producto | precio  |
    | Café     | $12.500 |
    | Taza     | $30.000 |
    | Molino   | $95.000 |

Escenario: Por debajo de $100.000 no hay descuento
  Cuando añado 2 Café al carrito
  ...
```

Úsalos para el contexto que el lector necesita para entender los escenarios, no para esconder preparación técnica: eso va en un hook ([teoría 03](03-estado-y-cucumber-spring.md#hooks)).

## Esquema del escenario

Funciona igual que en la [semana 22](../../week-22-bdd_con_python_behave/1-teoria/01-bdd-y-gherkin.md#scenario-outline-fronteras-en-una-tabla): un escenario con `<marcadores>` y una tabla `Ejemplos` con una fila por ejecución. Cada fila es un test aparte en la salida de Maven:

```gherkin
Esquema del escenario: Café gratis según los sellos
  Dado una tarjeta con <sellos> sellos
  Entonces el café gratis está "<disponible>"

  Ejemplos:
    | sellos | disponible    |
    | 9      | no disponible |
    | 10     | disponible    |
    | 25     | disponible    |
```

Antes de convertir, Cucumber sustituye el marcador por el texto de la fila. `<sellos>` se convierte en `9` y el paso queda `una tarjeta con 9 sellos`, que casa con `{int}`.

---

## ✅ Resumen

- Una tabla llega como último parámetro del step: `DataTable`, `List<T>` o `Map<K, V>`.
- `@DataTableType` convierte filas en objetos; sin él, `List<Product>` no se puede convertir.
- `@ParameterType` define `{pesos}`, `{estado}` o cualquier tipo del negocio con una expresión regular.
- Los tipos y las tablas convertidas son globales al glue.
- `Antecedentes` repite contexto de negocio; `Esquema del escenario` repite un escenario con otros datos.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 01 - Cucumber en Java](01-cucumber-en-java.md) | [Teoría — 03 - Estado entre steps y cucumber-spring](03-estado-y-cucumber-spring.md) → |

[↑ Semana 30](../README.md)
