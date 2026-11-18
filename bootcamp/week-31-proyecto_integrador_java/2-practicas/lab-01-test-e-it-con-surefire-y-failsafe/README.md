# Lab 01 - Test e IT con Surefire y Failsafe

## Objetivo

Separar una suite Java en dos fases: los tests unitarios, que Surefire ejecuta en `mvn test`, y los de integración, que Failsafe ejecuta en `mvn verify`. Ejecutar cada fase por separado y leer qué pasa cuando un test de integración falla.

## Tiempo estimado

75 minutos.

## Preparación

Docker debe estar en marcha: `BookRepositoryIT` arranca PostgreSQL con Testcontainers, como en la [semana 29](../../../week-29-testcontainers_en_java/README.md).

```bash
cd starter
mvn test
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.137 s -- in com.bootcamp.LateFeesTest
# Mockito is currently self-attaching to enable the inline-mock-maker. ...
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.646 s -- in com.bootcamp.LoanServiceTest
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.232 s -- in com.bootcamp.RunCucumberTest
# [INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

El aviso de Mockito es el de la [semana 26](../../../week-26-mockito_mocking_en_java/README.md): este lab no carga Mockito como agente. El lab 02 sí lo hace.

Qué hay en `starter/`, una API de biblioteca con Spring Boot:

| Archivo | Qué es |
|---|---|
| `LateFees` | `lateFee(daysLate)`: 50 por día de retraso, hasta 1000. Lanza `IllegalArgumentException` con días negativos |
| `BookRepository` | Libros en PostgreSQL con `JdbcClient`. La tabla la crea `schema.sql` |
| `LoanService` | `checkout(isbn)`: presta un ejemplar y descuenta uno del stock |
| `LibraryController` | `GET /fees?daysLate=N` y `POST /books/{isbn}/checkout` |
| `LateFeesTest`, `LoanServiceTest` | Tests unitarios: JUnit con AssertJ y Mockito |
| `RunCucumberTest`, `FeeSteps`, `multas.feature` | Un esquema del escenario que llama directamente a `LateFees` |
| `LibraryControllerIT` | `@WebMvcTest` con `MockMvcTester` y `LoanService` simulado |
| `BookRepositoryIT` | `@JdbcTest` contra PostgreSQL 18.7 en un contenedor |
| `pom.xml` | Sin Failsafe: el PASO 1 lo añade |

## Paso a paso

### Paso 0: lo que no se ejecuta

Cuenta las clases de test:

```bash
ls src/test/java/com/bootcamp/
# BookRepositoryIT.java  FeeSteps.java  LateFeesTest.java  LibraryControllerIT.java  LoanServiceTest.java  RunCucumberTest.java
```

`mvn test` ejecutó tres clases: `LateFeesTest`, `LoanServiceTest` y `RunCucumberTest`. Las dos `*IT` no aparecen. Prueba con la fase siguiente:

```bash
mvn verify
# [INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

Los mismos 10 tests. Surefire solo ejecuta las clases que terminan en `Test` (también `Tests`, `TestCase` o que empiezan por `Test`), y nadie ejecuta las `*IT`. Maven no avisa: el build está en verde y seis tests no se han ejecutado nunca.

### Paso 1: declarar Failsafe (PASO 1)

1. En `pom.xml`, sustituye el comentario `<!-- PASO 1: ... -->` por:

   ```xml
         <!-- Failsafe ejecuta los *IT en mvn verify. Spring Boot fija su versión y sus goals -->
         <plugin>
           <groupId>org.apache.maven.plugins</groupId>
           <artifactId>maven-failsafe-plugin</artifactId>
         </plugin>
   ```

   No hace falta versión ni `<executions>`: `spring-boot-starter-parent` ya los define ([teoría 01](../../1-teoria/01-la-suite-java-completa.md#dos-fases-surefire-y-failsafe)).

2. Ejecuta:

   ```bash
   mvn verify
   # [INFO] --- surefire:3.5.6:test (default-test) @ lab-01 ---
   # [INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
   # [INFO] --- failsafe:3.5.6:integration-test (default) @ lab-01 ---
   # [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 7.391 s -- in com.bootcamp.BookRepositoryIT
   # [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.122 s -- in com.bootcamp.LibraryControllerIT
   # [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
   # [INFO] --- failsafe:3.5.6:verify (default) @ lab-01 ---
   # [INFO] BUILD SUCCESS
   # [INFO] Total time:  11.915 s
   ```

Las fases van en orden: primero Surefire con los 10 unitarios y después Failsafe con los 6 de integración. `BookRepositoryIT` tarda más de siete segundos en dos tests: arranca el contenedor.

### Paso 2: ejecutar cada fase por separado

La fase rápida, para mientras programas:

```bash
mvn verify -DskipITs
# [INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
# [INFO] --- failsafe:3.5.6:integration-test (default) @ lab-01 ---
# [INFO] Tests are skipped.
# [INFO] BUILD SUCCESS
# [INFO] Total time:  3.227 s
```

Una sola clase de integración, por ejemplo después de cambiar el SQL:

```bash
mvn verify -Dit.test=BookRepositoryIT
# [INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
# [INFO] --- failsafe:3.5.6:integration-test (default) @ lab-01 ---
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

Una sola clase unitaria:

```bash
mvn test -Dtest=LateFeesTest
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
# [INFO] Total time:  1.906 s
```

| Opción | Qué hace |
|---|---|
| `-DskipITs` | Failsafe no ejecuta nada; Surefire sí |
| `-Dit.test=Clase` | Failsafe ejecuta solo esa clase. Surefire sigue ejecutando todas las suyas |
| `-Dtest=Clase` | Surefire ejecuta solo esa clase |

`-DskipITs` tarda 3 segundos y la suite completa 12. Esa diferencia crece con cada `*IT`.

### Paso 3: por qué `RunCucumberTest` no es un IT

Abre `FeeSteps`: llama a `LateFees.lateFee` sin Spring, sin HTTP y sin base de datos. Por eso la suite de Cucumber se llama `RunCucumberTest` y va en Surefire, y tarda 0.2 segundos con cuatro escenarios.

En el proyecto, la suite de Cucumber arranca la API con PostgreSQL. Allí se llama `RunCucumberIT`. El criterio es lo que arranca el test, no la herramienta ([teoría 01](../../1-teoria/01-la-suite-java-completa.md#dos-fases-surefire-y-failsafe)).

### Paso 4: los informes

```bash
ls target/surefire-reports target/failsafe-reports
# target/failsafe-reports:
# TEST-com.bootcamp.BookRepositoryIT.xml
# TEST-com.bootcamp.LibraryControllerIT.xml
# com.bootcamp.BookRepositoryIT.txt
# com.bootcamp.LibraryControllerIT.txt
# failsafe-summary.xml
#
# target/surefire-reports:
# TEST-com.bootcamp.LateFeesTest.xml
# TEST-com.bootcamp.LoanServiceTest.xml
# TEST-com.bootcamp.RunCucumberTest.xml
# com.bootcamp.LateFeesTest.txt
# com.bootcamp.LoanServiceTest.txt
# com.bootcamp.RunCucumberTest.txt
```

Cada plugin escribe en su carpeta: un `.txt` con el resumen y un `.xml` con cada test, el formato que leen los servidores de integración continua. El lab 02 saca de ahí los tiempos del reporte.

## Experimentos

### Experimento 1: un nombre sin sufijo

Renombra `BookRepositoryIT.java` a `BookRepositoryCheck.java` y cambia el nombre de la clase dentro.

```bash
mvn verify
# [INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.428 s -- in com.bootcamp.LibraryControllerIT
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

Ningún plugin reconoce `*Check`: el build pasa con los tests del repositorio sin ejecutar. Es el mismo problema del Paso 0. Por eso conviene que la verificación compruebe cuántos tests hay en cada fase, no solo que el build pase. Deshaz el cambio.

### Experimento 2: un IT que falla

En `LibraryControllerIT.feeForThreeDaysLate`, cambia `isEqualTo(150)` por `isEqualTo(100)`.

```bash
mvn verify
# [INFO] --- failsafe:3.5.6:integration-test (default) @ lab-01 ---
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.332 s -- in com.bootcamp.BookRepositoryIT
# [ERROR] Tests run: 4, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 1.021 s <<< FAILURE! -- in com.bootcamp.LibraryControllerIT
# [ERROR] com.bootcamp.LibraryControllerIT.feeForThreeDaysLate -- Time elapsed: 0.152 s <<< FAILURE!
# [ERROR] Tests run: 6, Failures: 1, Errors: 0, Skipped: 0
# [INFO] --- failsafe:3.5.6:verify (default) @ lab-01 ---
# [INFO] BUILD FAILURE
# [ERROR] Failed to execute goal org.apache.maven.plugins:maven-failsafe-plugin:3.5.6:verify (default) on project lab-01: There are test failures.
```

Fíjate en el orden: el fallo aparece en `integration-test`, pero el build no se para ahí. Termina la fase, y es el *goal* `verify` de Failsafe el que lo hace fallar. Si el proyecto arrancara un servidor antes de los IT, `post-integration-test` lo pararía igualmente. Deshaz el cambio.

## Comprobación final

```bash
mvn verify
# [INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

## Preguntas de cierre

1. ¿Qué fase ejecutarías en cada guardado de archivo y cuál solo antes del commit? ¿Por qué?
2. Un compañero añade `@SpringBootTest` a `LateFeesTest` para inyectar un bean. ¿Qué le dirías sobre el nombre de la clase y sobre el tiempo de `mvn test`?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - JaCoCo, umbral y reporte](../lab-02-jacoco-umbral-y-reporte/README.md) → |

[↑ Semana 31](../../README.md)
