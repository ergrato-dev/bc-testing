# Lab 03 - Estado compartido con cucumber-spring

## Objetivo

Repartir los steps en dos clases que comparten el pedido del escenario con un bean `@ScenarioScope`, ver cómo un singleton de Spring arrastra datos de un escenario a otro y limpiarlo con hooks.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
mvn test
# Mockito is currently self-attaching to enable the inline-mock-maker. ...
# WARNING: A Java agent has been loaded dynamically (...byte-buddy-agent-1.18.11.jar)
# ...
# Escenario: Un pedido nuevo queda pendiente # classpath:features/pago.feature:5
#   ✔ Dado un pedido de 40000 pesos          # com.bootcamp.OrderSteps.anOrderOf(int)
#   ✔ Entonces hay 1 pedido pendiente        # com.bootcamp.OrderSteps.thereArePendingOrders(int)
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

Los avisos de Mockito salen porque `spring-boot-starter-test` lo trae y Spring lo inicializa. Este lab no usa Mockito: ignóralos (en la [semana 26](../../../week-26-mockito_mocking_en_java/README.md) viste cómo cargarlo como agente para que no salgan).

El código de producción es un servicio de pedidos:

| Archivo | Qué hace |
|---|---|
| `ShopApplication` | La clase `@SpringBootApplication`. No es una aplicación web |
| `Order` | `record` con número, importe y estado (`PENDING` o `PAID`) |
| `OrderService` | `@Service` que guarda los pedidos en un `Map`. `create(amount)`, `pay(id, payment)` (el pago tiene que ser exacto y una sola vez), `find(id)`, `pending()` y `clear()` |

Y los tests:

| Archivo | Qué hace |
|---|---|
| `CucumberSpringConfiguration` | `@CucumberContextConfiguration` con `@SpringBootTest` ([teoría 03](../../1-teoria/03-estado-y-cucumber-spring.md#cucumbercontextconfiguration)) |
| `ScenarioState` | `@Component @ScenarioScope` con el pedido y el error del escenario, en campos `private` con *getters* y *setters* |
| `OrderSteps` | Recibe `OrderService` y `ScenarioState` por el constructor. Crea pedidos y cuenta los pendientes |
| `PaymentSteps` | Comentado entero: el PASO 1 |
| `logback-test.xml` | Deja el registro en `WARN` para que la salida de Cucumber se lea sin el arranque de Spring |

El `pom.xml` añade a los del lab 01 `spring-boot-starter`, `spring-boot-starter-test` y `cucumber-spring`.

## Paso a paso

### Paso 0: quién crea las clases de steps

Abre `OrderSteps`. No tiene `@Component` y su constructor pide dos beans: con `cucumber-spring`, Cucumber le pide la instancia a Spring, y Spring se los pasa. `ScenarioState` sí tiene `@Component`: lo encuentra el escaneo de componentes de `ShopApplication`, que llega al paquete `com.bootcamp` de los tests. Sus campos son `private` y se usan con métodos (`state.setOrder(...)`); el experimento 3 enseña por qué.

### Paso 1: la segunda clase de steps (PASO 1)

1. Añade al final del feature:

   ```gherkin
     Escenario: Pagar el importe exacto
       Dado un pedido de 40000 pesos
       Cuando pago 40000 pesos
       Entonces el pago se acepta
       Y el pedido está pagado
       Y hay 0 pedidos pendientes

     Escenario: Un pago incompleto se rechaza
       Dado un pedido de 40000 pesos
       Cuando pago 30000 pesos
       Entonces el pago se rechaza con "payment 30000 does not match amount 40000"
       Y hay 1 pedido pendiente
   ```

2. Descomenta `PaymentSteps` entero (todo menos la línea `package`) y el bloque del PASO 1 de `OrderSteps`.

3. Ejecuta:

   ```bash
   mvn test
   # [ERROR] com.bootcamp.RunCucumberTest.Pagar el importe exacto -- Time elapsed: 0.031 s <<< FAILURE!
   # java.lang.AssertionError:
   #
   # Expected size: 0 but was: 1 in:
   # [Order[id=1, amount=40000, status=PENDING]]
   # ...
   # [ERROR] com.bootcamp.RunCucumberTest.Un pago incompleto se rechaza -- Time elapsed: 0.017 s <<< FAILURE!
   # java.lang.AssertionError:
   #
   # Expected size: 1 but was: 2 in:
   # [Order[id=1, amount=40000, status=PENDING],
   #     Order[id=3, amount=40000, status=PENDING]]
   # [ERROR] Tests run: 3, Failures: 2, Errors: 0, Skipped: 0
   ```

El estado compartido funciona: `PaymentSteps` paga el pedido que creó `OrderSteps`, porque las dos clases reciben el mismo `ScenarioState`, y el pago se acepta. Lo que falla es el recuento. `Order[id=1]` es el pedido del primer escenario: `OrderService` es un singleton y el contexto de Spring es el mismo para los tres escenarios ([teoría 03](../../1-teoria/03-estado-y-cucumber-spring.md#lo-que-guarda-un-singleton)).

### Paso 2: limpiar antes de cada escenario (PASO 2)

Descomenta los dos bloques del PASO 2 de `OrderSteps`: el `import` de `Before` y el hook `clearOrders`.

```bash
mvn test
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

El `@Before` vacía el servicio antes de cada escenario. Los números de pedido siguen subiendo (`clear()` no reinicia el contador), y por eso ningún paso compara un número de pedido concreto.

### Paso 3: un hook para los fallos (PASO 3)

1. Añade al final del feature:

   ```gherkin
     Escenario: Un pedido no se paga dos veces
       Dado un pedido de 15000 pesos
       Y pago 15000 pesos
       Cuando pago 15000 pesos
       Entonces el pago se rechaza con "order already paid"
   ```

2. Descomenta los tres bloques del PASO 3 de `OrderSteps`: los `import` de `After` y `Scenario` y el hook `logPendingOrders`.

   ```bash
   mvn test
   # [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
   ```

El `@After` recibe el `Scenario` y, si falló, escribe los pedidos pendientes con `scenario.log(...)`. No se ve nada porque no falla nada: lo verás en el experimento 2.

## Experimentos

### Experimento 1: sin @CucumberContextConfiguration

Borra la anotación `@CucumberContextConfiguration` de `CucumberSpringConfiguration`.

```bash
mvn test
# io.cucumber.core.exception.CompositeCucumberException: There were 4 exceptions. The details are in the stacktrace below.
# 	Suppressed: io.cucumber.core.backend.CucumberBackendException: Please annotate a glue class with some context configuration.
```

Sin esa clase, `cucumber-spring` no sabe cómo crear el contexto y no puede crear ninguna clase de steps. Deshaz el cambio.

### Experimento 2: el log de un escenario fallido

Vuelve a comentar el hook `clearOrders` del PASO 2 (deja su `import`) y ejecuta:

```bash
mvn test
# ...
#         	at ✽.hay 0 pedidos pendientes(classpath:features/pago.feature:14)
#         pending orders: [Order[id=1, amount=40000, status=PENDING]]
# ...
#         	at ✽.hay 1 pedido pendiente(classpath:features/pago.feature:20)
#         pending orders: [Order[id=1, amount=40000, status=PENDING], Order[id=3, amount=40000, status=PENDING]]
```

El texto de `scenario.log(...)` sale en la consola debajo del error, y en el lab 04 verás que también queda en el reporte HTML. Descomenta otra vez el hook.

### Experimento 3: el proxy de @ScenarioScope

1. Añade esta línea al principio del hook `clearOrders`:

   ```java
   System.out.println("PROBE " + state.getClass().getSimpleName() + " " + state.getOrder());
   ```

   ```bash
   mvn test | grep PROBE
   # PROBE ScenarioState$$SpringCGLIB$$0 null
   # PROBE ScenarioState$$SpringCGLIB$$0 null
   # PROBE ScenarioState$$SpringCGLIB$$0 null
   # PROBE ScenarioState$$SpringCGLIB$$0 null
   ```

   `OrderSteps` no tiene un `ScenarioState`, sino un proxy que Spring crea una vez. Cada llamada a un método va a la instancia del escenario actual, que al empezar está vacía: `getOrder()` da `null` en los cuatro escenarios.

2. Añade a `ScenarioState` un campo `public Order last;`. En `anOrderOf`, debajo de `state.setOrder(...)`, añade `state.last = state.getOrder();`. En la línea `PROBE`, cambia `state.getOrder()` por `state.last`:

   ```bash
   mvn test | grep PROBE
   # PROBE ScenarioState$$SpringCGLIB$$0 null
   # PROBE ScenarioState$$SpringCGLIB$$0 Order[id=1, amount=40000, status=PENDING]
   # PROBE ScenarioState$$SpringCGLIB$$0 Order[id=2, amount=40000, status=PENDING]
   # PROBE ScenarioState$$SpringCGLIB$$0 Order[id=3, amount=40000, status=PENDING]
   ```

   Un campo se lee y se escribe en el proxy, que vive toda la ejecución: cada escenario ve el pedido del anterior. Los tests siguen pasando, y por eso esta trampa es peligrosa ([teoría 03](../../1-teoria/03-estado-y-cucumber-spring.md#la-trampa-el-proxy)).

Deshaz los cambios.

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
| ← [Lab 02 - DataTable y tipos de parámetro](../lab-02-datatable-y-tipos-de-parametro/README.md) | [Lab 04 - Cucumber con Spring Boot y reportes](../lab-04-cucumber-con-spring-boot-y-reportes/README.md) → |

[↑ Semana 30](../../README.md)
