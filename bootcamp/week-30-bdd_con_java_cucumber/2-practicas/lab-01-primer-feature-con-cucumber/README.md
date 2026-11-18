# Lab 01 - Primer feature con Cucumber

## Objetivo

Ejecutar un feature en español con Cucumber y JUnit Platform, completar sus steps a partir de los fragmentos que sugiere Cucumber y usar las Cucumber Expressions: `{int}`, `{string}`, texto opcional y un esquema del escenario.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
mvn test
# Escenario: Un café suma un sello      # classpath:features/tarjeta_de_sellos.feature:5
#   ✔ Dado una tarjeta nueva            # com.bootcamp.StampCardSteps.aNewCard()
#   ✔ Cuando compro 1 café              # com.bootcamp.StampCardSteps.iBuyCoffees(int)
#   ✔ Entonces la tarjeta tiene 1 sello # com.bootcamp.StampCardSteps.theCardHasStamps(int)
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

El código de producción es la tarjeta de sellos de una cafetería:

| Archivo | Qué hace |
|---|---|
| `StampCard` | `stamp(coffees)` suma un sello por café; `hasFreeCoffee()` es cierto desde 10 sellos; `redeem()` descuenta 10 sellos o lanza `IllegalStateException("not enough stamps: N")` |
| `RunCucumberTest` | La suite: motor `cucumber`, features del paquete `features`, glue `com.bootcamp` y plugin `pretty` |
| `features/tarjeta_de_sellos.feature` | El feature, con un escenario |
| `StampCardSteps` | Los steps, con los de los PASOS comentados |

El `pom.xml` importa `cucumber-bom` 8.1.0 y trae `cucumber-java`, `cucumber-junit-platform-engine`, `junit-platform-suite` y AssertJ ([teoría 01](../../1-teoria/01-cucumber-en-java.md#las-dependencias)).

En este lab, los PASOS tienen dos partes: el Gherkin que añades al final del feature, que está en este README, y el código Java, comentado en `StampCardSteps`.

## Paso a paso

### Paso 0: lee el feature y la suite

Abre `tarjeta_de_sellos.feature` y `RunCucumberTest` con la [teoría 01](../../1-teoria/01-cucumber-en-java.md#la-suite) al lado. Comprueba en la salida de `mvn test` que cada paso dice qué método lo ejecutó: `Cuando compro 1 café` casa con `@When("compro {int} café(s)")` y el método recibe `1`.

### Paso 1: un paso que no existe (PASO 1)

1. Añade este escenario al final del feature, con una línea en blanco antes:

   ```gherkin
     Escenario: Con 10 sellos hay café gratis
       Dado una tarjeta con 9 sellos
       Cuando compro 1 café
       Entonces la tarjeta tiene 10 sellos
       Y tengo un café gratis
   ```

2. Ejecuta antes de tocar Java:

   ```bash
   mvn test
   # Escenario: Con 10 sellos hay café gratis # classpath:features/tarjeta_de_sellos.feature:11
   #   ■ Dado una tarjeta con 9 sellos
   #   ↷ Cuando compro 1 café                 # com.bootcamp.StampCardSteps.iBuyCoffees(int)
   #   ↷ Entonces la tarjeta tiene 10 sellos  # com.bootcamp.StampCardSteps.theCardHasStamps(int)
   #   ↷ Y tengo un café gratis
   # io.cucumber.junit.platform.engine.UndefinedStepException:
   # The step 'una tarjeta con 9 sellos' and 1 other step(s) are undefined.
   # You can implement these steps using the snippet(s) below:
   #
   # @Dado("una tarjeta con {int} sellos")
   # public void una_tarjeta_con_sellos(Integer int1) {
   # ...
   # @Entonces("tengo un café gratis")
   # ...
   # [ERROR] Tests run: 2, Failures: 0, Errors: 1, Skipped: 0
   ```

   Dos pasos no tienen step: `■` marca el primero y `↷` los que no se ejecutaron por su culpa.

3. Descomenta los dos bloques del PASO 1 en `StampCardSteps`. Compáralos con los fragmentos: la anotación es `@Given` en lugar de `@Dado`, el método tiene nombre en inglés y la expresión dice `sello(s)`, así que el mismo step sirve para `con 1 sello` y para `con 9 sellos`.

   ```bash
   mvn test
   # [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
   ```

`Y tengo un café gratis` casa con un `@Then` aunque empiece por `Y`: la palabra clave no cuenta ([teoría 01](../../1-teoria/01-cucumber-en-java.md#la-palabra-clave-no-cuenta)).

### Paso 2: canjear y fallar (PASO 2)

1. Añade al final del feature:

   ```gherkin
     Escenario: Canjear el café gratis descuenta 10 sellos
       Dado una tarjeta con 12 sellos
       Cuando canjeo un café gratis
       Entonces la tarjeta tiene 2 sellos
       Pero no tengo un café gratis

     Escenario: No se puede canjear sin sellos suficientes
       Dado una tarjeta con 9 sellos
       Cuando intento canjear un café gratis
       Entonces la cafetería responde "not enough stamps: 9"
   ```

2. Descomenta todos los bloques del PASO 2: el `import` de `catchThrowable`, el campo `error` y cuatro steps.

   ```bash
   mvn test
   # [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
   ```

El último escenario no deja que la excepción haga fallar el paso: `intento canjear` la captura con `catchThrowable` de AssertJ y la guarda en el campo `error`, y el paso siguiente comprueba su mensaje. `{string}` captura el texto entre comillas, sin ellas. Son dos steps distintos, `canjeo` e `intento canjear`, porque en el primero un error sí debe hacer fallar el escenario.

### Paso 3: esquema del escenario (PASO 3)

1. Añade al final del feature:

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

2. Descomenta el PASO 3.

   ```bash
   mvn test
   # Esquema del escenario: Café gratis según los sellos # classpath:features/tarjeta_de_sellos.feature:33
   #   ✔ Dado una tarjeta con 9 sellos                   # com.bootcamp.StampCardSteps.aCardWithStamps(int)
   #   ✔ Entonces el café gratis está "no disponible"    # com.bootcamp.StampCardSteps.theFreeCoffeeIs(java.lang.String)
   # ...
   # [INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
   ```

Cuatro escenarios y tres ejemplos: siete tests. Cada fila de `Ejemplos` es un test.

## Experimentos

### Experimento 1: cero sellos

Añade la fila `| 0      | no disponible |` como primera fila de los `Ejemplos`, debajo de la cabecera.

```bash
mvn test
# [ERROR] com.bootcamp.RunCucumberTest.Example #1.1 -- Time elapsed: 0.008 s <<< ERROR!
# java.lang.IllegalArgumentException: coffees must be positive
# 	at com.bootcamp.StampCard.stamp(StampCard.java:12)
# 	at com.bootcamp.StampCardSteps.aCardWithStamps(StampCardSteps.java:24)
# 	at ✽.una tarjeta con 0 sellos(classpath:features/tarjeta_de_sellos.feature:28)
```

El step `una tarjeta con {int} sellos` prepara la tarjeta llamando a `stamp(0)`, y el dominio no admite cero cafés. La traza dice qué línea del feature falló (`✽.una tarjeta con 0 sellos`). No es un error del dominio sino del step: una tarjeta con 0 sellos es una tarjeta nueva. Deshaz el cambio.

### Experimento 2: dos steps para el mismo paso

Añade a `StampCardSteps` otro step con la expresión `"una tarjeta con {int} sellos"` (sin el `(s)`) y cuerpo vacío.

```bash
mvn test
# io.cucumber.core.runner.AmbiguousStepDefinitionsException:
# "una tarjeta con 9 sellos" matches more than one step definition:
#   "una tarjeta con {int} sello(s)" in com.bootcamp.StampCardSteps.aCardWithStamps(int)
#   "una tarjeta con {int} sellos" in com.bootcamp.StampCardSteps.duplicate(int)
```

Cucumber no elige entre dos steps que casan: todos los escenarios que usan ese paso fallan. Deshaz el cambio.

### Experimento 3: un escenario que falla

En el segundo escenario, cambia `la tarjeta tiene 10 sellos` por `la tarjeta tiene 11 sellos`.

```bash
mvn test
# [ERROR] com.bootcamp.RunCucumberTest.Con 10 sellos hay café gratis -- Time elapsed: 0.015 s <<< FAILURE!
# org.opentest4j.AssertionFailedError:
#
# expected: 11
#  but was: 10
# 	at com.bootcamp.StampCardSteps.theCardHasStamps(StampCardSteps.java:44)
# 	at ✽.la tarjeta tiene 11 sellos(classpath:features/tarjeta_de_sellos.feature:13)
```

Una aserción que falla es un `FAILURE`; un paso indefinido o una excepción inesperada, un `ERROR`. Deshaz el cambio.

## Comprobación final

```bash
mvn test
# [INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
```

Si un PASO no compila o no pasa, comprueba que descomentaste todos sus bloques, también los `import` marcados con su número, que el Gherkin está indentado con dos espacios como el resto del feature y que deshiciste los experimentos.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - DataTable y tipos de parámetro](../lab-02-datatable-y-tipos-de-parametro/README.md) → |

[↑ Semana 30](../../README.md)
