# 03 - @ServiceConnection en Spring Boot

> Lenguaje: **Java**

---

## 🎯 Objetivos

- Conectar el `DataSource` de Spring Boot a un contenedor con `@DynamicPropertySource` y con `@ServiceConnection`.
- Declarar el contenedor una sola vez, como bean, en una clase `@TestConfiguration`.
- Probar un repositorio con el slice `@JdbcTest` y su rollback por test.
- Reconocer cuándo un test de `@JdbcTest` no tiene rollback.

---

## De dónde partes

En la [semana 28](../../week-28-testing_de_spring_boot/1-teoria/01-contexto-de-spring-y-slices.md) viste que Spring Boot crea los beans a partir de la configuración de `application.properties`. Con PostgreSQL, el `DataSource` se crea con tres propiedades:

```properties
spring.datasource.url=jdbc:postgresql://127.0.0.1:55432/warehouse
spring.datasource.username=warehouse
spring.datasource.password=warehouse
```

En los tests, esa base de datos no existe: la URL buena es la del contenedor, y su puerto solo se conoce cuando el contenedor ya arrancó. Hay que pasarle a Spring la URL del contenedor antes de que cree el `DataSource`.

---

## Con @DynamicPropertySource

La forma general, que funciona con cualquier propiedad:

```java
@JdbcTest
@Import(StockRepository.class)
@Testcontainers
class StockRepositoryTest {

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.7-alpine");

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
```

- **`@JdbcTest`**: el slice de JDBC. Arranca el `DataSource`, `JdbcClient`, las transacciones y Flyway, pero no la web ni tus `@Service`. Por eso el repositorio se añade con `@Import`.
- **`@DynamicPropertySource`**: un método `static` que añade propiedades al contexto. Recibe *suppliers* (`postgres::getJdbcUrl`), no valores: Spring los llama cuando el contenedor ya está arrancado.

Sin esas propiedades, `@JdbcTest` intenta sustituir el `DataSource` por una base de datos embebida, no encuentra ninguna en el classpath y el contexto no arranca:

```text
Caused by: java.lang.IllegalStateException: Failed to replace DataSource with an embedded database for tests. If you want an embedded database please put a supported one on the classpath or tune the replace attribute of @AutoConfigureTestDatabase.
```

En Spring Boot 4, `@AutoConfigureTestDatabase` (que va dentro de `@JdbcTest`) tiene `replace = NON_TEST` por defecto: solo sustituye el `DataSource` si su conexión no viene de un test. Una conexión de `@DynamicPropertySource` o de `@ServiceConnection` cuenta como de test, así que no hace falta escribir `@AutoConfigureTestDatabase(replace = NONE)`, como verás en muchos tutoriales de Spring Boot 2.

---

## Con @ServiceConnection

Para los servicios que Spring Boot conoce, entre ellos PostgreSQL, Kafka, Redis y MongoDB, hay un atajo:

```java
@Container
@ServiceConnection
static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.7-alpine");
```

`@ServiceConnection` le dice a Spring Boot: "la conexión de este tipo de servicio sale de este contenedor". Spring Boot sabe qué propiedades necesita un `PostgreSQLContainer` y las rellena él. Las tres líneas de `@DynamicPropertySource` desaparecen. Va en el artefacto `spring-boot-testcontainers`.

| | `@DynamicPropertySource` | `@ServiceConnection` |
|---|---|---|
| Qué servicios | Cualquiera, también uno tuyo con `GenericContainer` | Los que Spring Boot conoce |
| Qué escribes | Cada propiedad con su nombre | Nada más que la anotación |
| Si te equivocas en el nombre de una propiedad | Se ignora sin error | No hay nombres que escribir |

Usa `@ServiceConnection` siempre que puedas, y `@DynamicPropertySource` para lo demás.

---

## El contenedor como bean

Copiar el campo del contenedor en cada clase de test repite la imagen en muchos sitios. Spring Boot permite declararlo una vez, como un bean más:

```java
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgres() {
        return new PostgreSQLContainer("postgres:18.7-alpine");
    }
}
```

- **`@TestConfiguration`**: una clase de configuración que solo existe en los tests y que se añade con `@Import`.
- **`proxyBeanMethods = false`**: Spring no envuelve la clase en un proxy. Aquí ningún método `@Bean` llama a otro, así que el proxy no sirve de nada y arrancar es un poco más rápido.
- Ni `@Testcontainers` ni `@Container`: Spring arranca el contenedor al crear el bean y lo para al cerrar el contexto.

Cada clase de test lo usa con una línea:

```java
@JdbcTest
@Import({TestcontainersConfiguration.class, StockRepository.class})
class RestockTest {
```

Como el contenedor es un bean, vive tanto como el contexto. Dos clases con la misma configuración comparten contexto (lo viste en la [semana 28](../../week-28-testing_de_spring_boot/1-teoria/01-contexto-de-spring-y-slices.md#el-contexto-se-reutiliza)) y por tanto contenedor. En el [lab 03](../2-practicas/lab-03-serviceconnection-con-spring-boot/README.md), `ReservationTest` arranca PostgreSQL y `RestockTest`, que va después, ni siquiera lo arranca:

```text
INFO  tc.postgres:18.7-alpine - Container postgres:18.7-alpine started in PT1.743944347S
[INFO] Tests run: 1, ... Time elapsed: 7.459 s -- in com.bootcamp.ReservationTest
...
[INFO] Tests run: 1, ... Time elapsed: 0.030 s -- in com.bootcamp.RestockTest
```

---

## Flyway arranca solo

Con `spring-boot-starter-flyway` en el `pom.xml`, Spring Boot ejecuta las migraciones de `db/migration` al crear el contexto, antes de que ningún test se ejecute. Es lo mismo que pasa al arrancar la aplicación en producción, sin una línea de código en los tests. Al arrancar la API del proyecto contra el PostgreSQL de `compose.yaml`, el log lo muestra:

```text
Migrating schema "public" to version "1 - create pieces and loans"
Successfully applied 1 migration to schema "public", now at version v1 (execution time 00:00.015s)
```

Y al arrancarla otra vez:

```text
Schema "public" is up to date. No migration necessary.
```

---

## El rollback de @JdbcTest

`@JdbcTest` es `@Transactional`: cada test se ejecuta dentro de una transacción que Spring deshace al terminar. Lo que un test inserta no lo ve el siguiente, aunque compartan contenedor. Es el rollback por test que en la [semana 23](../../week-23-testing_de_bases_de_datos_en_python/1-teoria/02-fixtures-y-rollback-por-test.md) montaste a mano con una transacción externa.

`@SpringBootTest` no es `@Transactional`. En el test de extremo a extremo del proyecto, cada petición HTTP confirma sus cambios con un commit, y los datos se quedan para los tests siguientes. Por eso cada test usa códigos de pieza distintos.

### La trampa: los tests heredados

En el proyecto, los tests del repositorio se escriben una vez en una clase abstracta (un *test de contrato*) y los heredan dos subclases: la del fake en memoria y la de PostgreSQL. La primera ejecución falló así:

```text
Expecting actual:
  [Piece[code=CON-010, name=Tapiz, declaredValue=12000, status=ON_LOAN],
    Piece[code=CON-001, name=Astrolabio, declaredValue=40000, status=AVAILABLE],
    Piece[code=CON-002, name=Ánfora, declaredValue=8000, status=IN_RESTORATION]]
...
but the following elements were unexpected:
  [Piece[code=CON-010, name=Tapiz, declaredValue=12000, status=ON_LOAN]]
```

`CON-010` la había insertado otra clase de test: no hubo rollback. Al imprimir `TestTransaction.isActive()` en cada test de esa clase, el resultado fue este:

```text
PROBE tx=false     # los tres tests heredados de la clase abstracta
PROBE tx=false
PROBE tx=false
PROBE tx=true      # el único test declarado en la subclase con @JdbcTest
```

Spring busca `@Transactional` en el método y en la clase que **declara** el método. Los tests heredados están declarados en la clase abstracta, que no tiene la anotación, así que se ejecutan sin transacción y confirman sus datos. La solución es poner `@Transactional` también en la clase abstracta. Con el fake en memoria no hace nada, porque ese test no arranca Spring.

---

## ✅ Resumen

- `@DynamicPropertySource` pasa cualquier propiedad del contenedor al contexto; `@ServiceConnection` lo hace solo para los servicios que Spring Boot conoce.
- En Spring Boot 4, `@JdbcTest` no sustituye una conexión que viene de un test: no hace falta `replace = NONE`.
- Un contenedor declarado como `@Bean` en una `@TestConfiguration` se escribe una vez y se comparte entre las clases que comparten contexto.
- Flyway migra al crear el contexto, igual que en producción.
- `@JdbcTest` deshace cada test, pero solo los métodos declarados en una clase con `@Transactional`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 02 - PostgreSQL, su esquema y las migraciones](02-postgresql-esquema-y-migraciones.md) | [Teoría — 04 - Contenedores compartidos, reutilización y Kafka](04-contenedores-compartidos-y-kafka.md) → |

[↑ Semana 29](../README.md)
