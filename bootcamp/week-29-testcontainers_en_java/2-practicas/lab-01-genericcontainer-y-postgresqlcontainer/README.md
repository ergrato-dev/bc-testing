# Lab 01 - GenericContainer y PostgreSQLContainer

## Objetivo

Arrancar PostgreSQL desde un test de JUnit: primero a mano con `GenericContainer`, después con el módulo `PostgreSQLContainer`. Al final mides cuánto cuesta un contenedor por test frente a uno por clase, y lo reutilizas entre ejecuciones.

## Tiempo estimado

60 minutos.

## Preparación

Comprueba que Docker responde ([teoría 01](../../1-teoria/01-testcontainers-y-el-ciclo-de-vida.md#requisito-docker)):

```bash
docker version --format '{{.Server.Version}}'
# 29.9.3
```

```bash
cd starter
mvn test
# INFO  tc.testcontainers/ryuk:0.14.0 - Container testcontainers/ryuk:0.14.0 started in PT3.967257538S
# INFO  tc.postgres:18.7-alpine - Container postgres:18.7-alpine started in PT1.779386309S
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

La primera ejecución descarga las imágenes `postgres:18.7-alpine` y `testcontainers/ryuk:0.14.0` (unos 440 MB en disco una vez descomprimidas), además de las dependencias de Maven. Las siguientes ejecuciones tardan segundos.

El lab no tiene código de producción: los tests hablan con PostgreSQL por JDBC, sin Spring.

| Archivo | Qué hay |
|---|---|
| `pom.xml` | Hereda de `spring-boot-starter-parent` solo para las versiones. Trae `testcontainers-junit-jupiter`, `testcontainers-postgresql`, el driver `postgresql`, JUnit, AssertJ y Logback |
| `src/test/resources/logback-test.xml` | Muestra solo el log de los contenedores (`tc.*`) y los avisos |
| `GenericPostgresTest` | Un test activo y el PASO 1 |
| `ModulePostgresTest` | PASO 2, entero comentado |
| `ContainerPerTestTest` | PASO 3, entero comentado |

Lee `GenericPostgresTest` con la [teoría 01](../../1-teoria/01-testcontainers-y-el-ciclo-de-vida.md#un-postgresql-a-mano-con-genericcontainer) al lado: cada línea del contenedor está explicada allí. El test activo ejecuta `SHOW server_version` y comprueba que la respuesta es `18.7`, la versión de la imagen.

Para descomentar un PASO, selecciona sus líneas, también las de los `import` marcados con el mismo PASO, y usa `Ctrl+/`.

## Paso a paso

### Paso 1: el puerto del host (PASO 1)

Descomenta el PASO 1 de `GenericPostgresTest`.

```java
int hostPort = postgres.getMappedPort(5432);
System.out.println("5432 en el contenedor -> " + postgres.getHost() + ":" + hostPort);

assertThat(hostPort).isNotEqualTo(5432);
```

`getMappedPort(5432)` traduce el puerto interno al que Docker publicó en tu equipo.

```bash
mvn test
# 5432 en el contenedor -> localhost:32769
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

Tu puerto será otro: lo elige Docker en cada arranque. Ejecuta `mvn test` otra vez y compruébalo. Los dos tests usan el mismo contenedor, porque el campo es `static`: en el log aparece un solo `started in`.

Mientras se ejecuta la suite, mira los contenedores desde otra terminal:

```bash
docker ps --format '{{.Image}} {{.Ports}}'
# postgres:18.7-alpine 0.0.0.0:32861->5432/tcp, [::]:32861->5432/tcp
# testcontainers/ryuk:0.14.0 0.0.0.0:32860->8080/tcp, [::]:32860->8080/tcp
```

Al terminar la suite, `docker ps` ya no los muestra.

### Paso 2: el módulo de PostgreSQL (PASO 2)

Descomenta el archivo `ModulePostgresTest` entero.

```java
@Container
static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.7-alpine");
```

Una línea en lugar de seis: el módulo ya sabe qué variables de entorno, qué puerto y qué espera necesita PostgreSQL ([teoría 02](../../1-teoria/02-postgresql-esquema-y-migraciones.md#postgresqlcontainer)). El test imprime la URL y las credenciales que eligió:

```bash
mvn test
# jdbc:postgresql://localhost:32770/test?loggerLevel=OFF test/test
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

En el log hay dos `Container postgres:18.7-alpine started`: uno por clase.

### Paso 3: un contenedor por test (PASO 3)

Descomenta el archivo `ContainerPerTestTest` entero. El campo **no** es `static`, y el test se repite tres veces con `@RepeatedTest(3)`. Cada repetición crea una tabla `visits`, inserta una fila y comprueba que hay exactamente una.

```bash
mvn test
# contenedor 27d3f4e789d0
# contenedor dd404be948f5
# contenedor 4363d5579b06
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.912 s -- in com.bootcamp.ContainerPerTestTest
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

Tres ids distintos: tres contenedores. Compara el tiempo de la clase con el de `ModulePostgresTest`:

```text
Time elapsed: 1.918 s -- in com.bootcamp.ModulePostgresTest
Time elapsed: 5.912 s -- in com.bootcamp.ContainerPerTestTest
```

## Experimentos

### Experimento 1: static compartido

En `ContainerPerTestTest`, añade `static` al campo:

```java
static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.7-alpine");
```

```bash
mvn test -Dtest=ContainerPerTestTest
# contenedor f369ea9d4921
# contenedor f369ea9d4921
# contenedor f369ea9d4921
# [ERROR]   ContainerPerTestTest.shouldStartWithAnEmptyDatabase:28 » PSQL ERROR: relation "visits" already exists
# [ERROR]   ContainerPerTestTest.shouldStartWithAnEmptyDatabase:28 » PSQL ERROR: relation "visits" already exists
```

`-Dtest=ContainerPerTestTest` ejecuta solo esa clase. Ahora el contenedor es uno, y la segunda repetición encuentra la tabla de la primera. `static` es más rápido, pero los tests tienen que convivir con lo que dejan los demás. Deshaz el cambio.

### Experimento 2: sin @Testcontainers

En `GenericPostgresTest`, borra la línea `@Testcontainers`.

```bash
mvn test -Dtest=GenericPostgresTest
# [ERROR]   GenericPostgresTest.shouldAnswerWithTheVersionOfTheImage:29->jdbcUrl:47 » IllegalState Mapped port can only be obtained after the container is started
# [ERROR]   GenericPostgresTest.shouldPublishPostgresOnARandomHostPort:39 » IllegalState Mapped port can only be obtained after the container is started
```

`@Container` es solo una marca: sin la extensión que registra `@Testcontainers`, nadie la lee ni arranca el contenedor. Deshaz el cambio.

### Experimento 3: reutilizar entre ejecuciones

Este experimento usa el patrón singleton de la [teoría 04](../../1-teoria/04-contenedores-compartidos-y-kafka.md#withreusetrue-entre-ejecuciones). Cambia el campo de `ModulePostgresTest` por estas líneas y borra `@Testcontainers` y `@Container` de esa clase:

```java
static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.7-alpine").withReuse(true);

static {
    postgres.start();
}
```

Ejecuta la clase dos veces con la reutilización activada:

```bash
TESTCONTAINERS_REUSE_ENABLE=true mvn test -Dtest=ModulePostgresTest
# INFO  tc.postgres:18.7-alpine - Container postgres:18.7-alpine started in PT5.280800797S

TESTCONTAINERS_REUSE_ENABLE=true mvn test -Dtest=ModulePostgresTest
# INFO  tc.postgres:18.7-alpine - Reusing existing container (09c5da8ebc3c...) and not creating a new one
# INFO  tc.postgres:18.7-alpine - Container postgres:18.7-alpine started in PT0.275191223S
```

`TESTCONTAINERS_REUSE_ENABLE=true` delante del comando define la variable solo para esa ejecución. La segunda vez, el contenedor ya estaba allí. Sigue allí también después:

```bash
docker ps --filter label=org.testcontainers.hash --format '{{.ID}} {{.Image}} {{.Status}}'
# 09c5da8ebc3c postgres:18.7-alpine Up 14 seconds
```

Ryuk no borra los contenedores reutilizables. Bórralo tú y deshaz el cambio:

```bash
docker rm -f 09c5da8ebc3c
```

Usa tu id, el que muestra `docker ps`.

## Comprobación final

```bash
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
docker ps --filter label=org.testcontainers.hash -q
# (nada: no queda ningún contenedor reutilizable)
```

Si un PASO no compila o no pasa, comprueba que descomentaste todos sus bloques, también los `import` marcados con su número, y que deshiciste los experimentos anteriores.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - El esquema con init.sql y con Flyway](../lab-02-esquema-con-init-sql-y-flyway/README.md) → |

[↑ Semana 29](../../README.md)
