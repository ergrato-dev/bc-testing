# Lab 04 - Kafka y el contenedor compartido

## Objetivo

Probar un productor de Kafka contra un broker real: que el mensaje llega con su clave y que los eventos de un mismo pedido se leen en orden aunque el topic tenga varias particiones. Al final compartes un único broker entre dos clases de test con el patrón singleton.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
mvn test
# INFO  tc.apache/kafka-native:4.2.1 - Container apache/kafka-native:4.2.1 started in PT0.719824173S
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

La primera ejecución descarga la imagen `apache/kafka-native:4.2.1` (unos 210 MB en disco).

| Archivo | Qué hace |
|---|---|
| `OrderEvents` | Productor: `publish(orderId, status)` envía un mensaje con el id del pedido como clave y espera la confirmación del broker (`send(...).get()`, `acks=all`) |
| `TestConsumer` | Ayuda de test: lee un topic desde el principio hasta recibir N mensajes o agotar 10 s ([teoría 04](../../1-teoria/04-contenedores-compartidos-y-kafka.md#leer-lo-que-es-asíncrono)) |
| `OrderEventsTest` | `KafkaContainer` con `@Container static`, un test activo y el PASO 1 |
| `KafkaTestBase`, `PaymentEventsTest`, `ShippingEventsTest` | PASO 2, enteros comentados |

El `pom.xml` trae `kafka-clients` (4.2.1, gestionado por Spring Boot) y `testcontainers-kafka`. No hay Spring en este lab.

Lee el test activo: publica `CREATED` para `ORD-1` en el topic `orders` y comprueba que el consumidor recibe exactamente ese par clave-valor. `extracting(ConsumerRecord::key, ConsumerRecord::value)` convierte cada mensaje en una tupla, y `tuple("ORD-1", "CREATED")` es la tupla esperada.

En el log verás este aviso:

```text
WARN  org.apache.kafka.clients.NetworkClient - [Producer clientId=producer-1] The metadata response from the cluster reported a recoverable issue with correlation id 1 : {orders=UNKNOWN_TOPIC_OR_PARTITION}
```

El topic `orders` no existía; el broker lo crea al primer uso y el productor reintenta. No es un fallo ([teoría 04](../../1-teoria/04-contenedores-compartidos-y-kafka.md#la-primera-vez-que-se-usa-un-topic)).

## Paso a paso

### Paso 1: el orden por clave (PASO 1)

Descomenta los dos bloques del PASO 1 de `OrderEventsTest`: los `import` y el test.

El test hace tres cosas:

1. Crea el topic `orders-3p` con tres particiones usando `Admin`, el cliente de administración de Kafka. `new NewTopic("orders-3p", 3, (short) 1)` es nombre, particiones y réplicas; con un solo broker, una réplica.
2. Publica cinco eventos intercalados de dos pedidos.
3. Lee los cinco, los imprime y comprueba el orden **de cada pedido** con `filteredOn`.

```bash
mvn test
# partición 0 offset 0 ORD-A CREATED
# partición 0 offset 1 ORD-A PAID
# partición 0 offset 2 ORD-A SHIPPED
# partición 2 offset 0 ORD-B CREATED
# partición 2 offset 1 ORD-B CANCELLED
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

Cada clave fue a una sola partición y dentro de ella el orden se mantuvo. El *offset* es la posición del mensaje en su partición. El test no comprueba en qué partición cae cada pedido, porque eso lo decide un hash de la clave y no forma parte del contrato. Tampoco el orden entre pedidos, que Kafka no garantiza.

### Paso 2: un broker para varias clases (PASO 2)

Descomenta tres archivos enteros: `KafkaTestBase`, `PaymentEventsTest` y `ShippingEventsTest`.

`KafkaTestBase` es el singleton de la [teoría 04](../../1-teoria/04-contenedores-compartidos-y-kafka.md#el-contenedor-singleton): un campo `static final` y un bloque `static` que lo arranca. Las dos clases de test heredan de ella y no tienen ni `@Testcontainers` ni `@Container`.

```bash
mvn test
# INFO  tc.apache/kafka-native:4.2.1 - Container apache/kafka-native:4.2.1 started in PT0.643642742S
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.981 s -- in com.bootcamp.ShippingEventsTest
# INFO  tc.apache/kafka-native:4.2.1 - Container apache/kafka-native:4.2.1 started in PT0.729346744S
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.697 s -- in com.bootcamp.OrderEventsTest
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.657 s -- in com.bootcamp.PaymentEventsTest
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
```

Dos brokers para tres clases: uno el de `KafkaTestBase`, para `ShippingEventsTest` y `PaymentEventsTest`, y otro el de `OrderEventsTest`, que tiene su propio `@Container`. `PaymentEventsTest` no arrancó nada. Cada clase escribe en su propio topic (`payments`, `shipments`), así que compartir broker no mezcla sus mensajes.

## Experimentos

### Experimento 1: olvidar el bloque static

En `KafkaTestBase`, borra la línea `KAFKA.start();`.

```bash
mvn test
# [ERROR]   PaymentEventsTest.shouldPublishAPayment:13 » IllegalState Mapped port can only be obtained after the container is started
# [ERROR]   ShippingEventsTest.shouldPublishAShipment:13 » IllegalState Mapped port can only be obtained after the container is started
```

Sin `@Container`, nadie arranca el contenedor por ti. Deshaz el cambio.

### Experimento 2: esperar un mensaje que no llega

En el test activo de `OrderEventsTest`, pide dos mensajes en lugar de uno:

```java
List<ConsumerRecord<String, String>> records = TestConsumer.read(kafka.getBootstrapServers(), "orders", 2);
```

```bash
mvn test -Dtest=OrderEventsTest
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 18.74 s -- in com.bootcamp.OrderEventsTest
```

El test sigue pasando, porque la aserción solo mira el mensaje que llegó, pero la clase tarda 18.7 s en lugar de 2.7 s: `TestConsumer` esperó el plazo entero de 10 s a un segundo mensaje que nunca se publicó. Un plazo demasiado largo no rompe la suite: la hace lenta, y solo cuando algo va mal. Deshaz el cambio.

## Comprobación final

```bash
mvn test
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
```

Si un PASO no compila o no pasa, comprueba que descomentaste todos sus bloques, también los `import` marcados con su número, y que deshiciste los experimentos anteriores.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - @ServiceConnection con Spring Boot](../lab-03-serviceconnection-con-spring-boot/README.md) | [Proyecto — Proyecto Semanal - Los repositorios del dominio en PostgreSQL](../../3-proyecto/README.md) → |

[↑ Semana 29](../../README.md)
