# Lab 04 - @SpringBootTest y RestTestClient

## Objetivo

Arrancar la aplicación entera en un test, hablarle por HTTP real con `RestTestClient` y controlar la fecha sustituyendo el bean `Clock` con `@TestBean`. Después provocas el fallo típico de los tests que comparten contexto y cuentas cuántos contextos arranca Spring.

## Tiempo estimado

75 minutos.

## Preparación

```bash
cd starter
mvn test
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

La aplicación gestiona préstamos de la biblioteca:

| Clase | Qué hace |
|---|---|
| `LibraryApplication` | Arranca la aplicación y declara el bean `Clock` con `Clock.systemDefaultZone()` |
| `LoanService` | `open` crea un préstamo que vence 14 días después de hoy; `overdue` lista los vencidos. Hoy lo dice el `Clock` |
| `LoanRepository` | `@Repository` en memoria. Es un bean: vive lo mismo que el contexto |
| `LoanController` | `POST /api/loans`, `GET /api/loans/{id}` y `GET /api/loans/overdue` |
| `LibraryExceptionHandler` | `LoanNotFoundException` a 404 con `ProblemDetail` |

En `src/test/java/com/bootcamp/` hay tres archivos:

- `LoanApiTest.java`: `@SpringBootTest(webEnvironment = RANDOM_PORT)` y `@AutoConfigureRestTestClient`, con un test activo (el 404) y los PASOS 1 y 2 comentados.
- `LoanMockMvcTest.java`: el PASO 3, todo comentado.
- `MutableClock.java`: un `Clock` de test completo, con `set(Instant)` y `advance(Duration)`. Léelo: son 40 líneas.

Antes de descomentar nada, mira la salida del test activo:

```bash
mvn test | grep -E 'Tomcat started|Started LoanApiTest'
# ... Tomcat started on port 44943 (http) with context path '/'
# ... Started LoanApiTest in 1.173 seconds (process running for 2.016)
```

El puerto será otro en tu equipo y cambiará en cada ejecución: es `RANDOM_PORT` ([teoría 04](../../1-teoria/04-springboottest-de-extremo-a-extremo.md#dos-entornos-web)). El test activo envía un `GET` real a ese puerto y comprueba el 404.

## Paso a paso

### Paso 1: el vencimiento con un reloj del test (PASO 1)

Descomenta los cuatro bloques del PASO 1: los `import`, el reloj, el test y el método `openLoan`.

```java
private static final Instant MARCH_20 = Instant.parse("2026-03-20T10:00:00Z");
private static final MutableClock CLOCK = new MutableClock(MARCH_20, ZoneOffset.UTC);

@TestBean
private Clock clock;

static Clock clock() {
    return CLOCK;
}
```

`@TestBean` sustituye el bean `Clock` de `LibraryApplication` por lo que devuelve `clock()`. El servicio, el repositorio y el controlador son los reales. `CLOCK` es `static` para que los tests lo puedan adelantar en el PASO 2.

`openLoan` envía un `POST` y convierte la respuesta en un `Loan`. El test comprueba que vence el 3 de abril: el 20 de marzo más 14 días.

```bash
mvn test
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

Experimento 1: cambia el nombre del método `clock()` por `fixedClock()`.

```bash
mvn test
# [ERROR]   LoanApiTest » IllegalState No static method found named clock() in com.bootcamp.LoanApiTest with return type java.time.Clock
```

El método se busca por el nombre del campo. Deshaz el cambio.

Experimento 2: piensa en cómo escribirías este test **sin** `@TestBean`. Tendrías que comparar con `LocalDate.now().plusDays(14)`: el test calcula la fecha igual que el código (el antipatrón de la [semana 27](../../../week-27-tdd_con_java/1-teoria/02-diseno-emergente-y-refactoring.md#el-valor-esperado-no-sale-del-código)) y falla si se ejecuta justo a medianoche.

### Paso 2: el tiempo avanza (PASO 2)

Descomenta los tres bloques del PASO 2: los `import`, el `@BeforeEach` y el test.

El test abre un préstamo, adelanta el reloj 15 días y pide los vencidos. Comprueba con `contains`, no con `containsExactly`: el repositorio es un bean compartido y puede tener préstamos de otros tests.

```bash
mvn test
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

Experimento: comenta la línea `CLOCK.set(MARCH_20);` del `@BeforeEach`.

```bash
mvn test
# [ERROR]   LoanApiTest.shouldSetTheDueDateTwoWeeksFromToday:55
# expected: 2026-04-03 (java.time.LocalDate)
#  but was: 2026-04-18 (java.time.LocalDate)
```

JUnit ejecutó primero el test de los vencidos, que dejó el reloj en el 4 de abril, y el test del PASO 1 calculó desde ahí. Ninguno de los dos tests está mal por separado: el problema es el estado que comparten ([teoría 04](../../1-teoria/04-springboottest-de-extremo-a-extremo.md#tests-que-comparten-el-contexto)). Deshaz el cambio.

### Paso 3: la aplicación entera sin servidor (PASO 3)

Descomenta todo `LoanMockMvcTest.java`.

`@SpringBootTest` sin `webEnvironment` usa `MOCK`: todos los beans reales, sin Tomcat. `@AutoConfigureMockMvc` configura un `MockMvcTester` como el del [lab 02](../lab-02-validacion-con-mockmvctester/README.md). Esta clase no sustituye el reloj, así que comprueba solo campos que no dependen de la fecha.

```bash
mvn test
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 4: cuántos contextos arranca Spring

```bash
mvn test | grep -E 'Tomcat started|Started Loan'
# ... Tomcat started on port 36171 (http) with context path '/'
# ... Started LoanApiTest in 1.222 seconds (process running for 2.118)
# ... Started LoanMockMvcTest in 0.185 seconds (process running for 2.936)
```

Dos contextos: `LoanApiTest` y `LoanMockMvcTest` tienen configuraciones distintas (`RANDOM_PORT` frente a `MOCK`, y un `@TestBean` que la otra no tiene). Solo el primero arrancó Tomcat.

Experimento 1: copia `LoanApiTest.java` como `LoanApiCopyTest.java` y cambia el nombre de la clase dentro. Ejecuta el `grep` otra vez:

```bash
mvn test | grep -E 'Tomcat started|Started Loan'
# ... Tomcat started on port 44943 (http) with context path '/'
# ... Started LoanApiTest in 1.173 seconds (process running for 2.016)
# ... Started LoanMockMvcTest in 0.226 seconds (process running for 2.942)
# ... Tomcat started on port 38715 (http) with context path '/'
# ... Started LoanApiCopyTest in 0.159 seconds (process running for 3.146)
```

Tres contextos y dos Tomcat, aunque la copia es idéntica. El `@TestBean` de la copia apunta a **su** método `clock()`, que es de otra clase: para Spring es otra configuración ([teoría 01](../../1-teoria/01-contexto-de-spring-y-slices.md#el-contexto-se-reutiliza)).

Experimento 2: para que dos clases compartan contexto, la configuración tiene que estar en un solo sitio. Crea `ApiTestBase.java`:

```java
package com.bootcamp;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.convention.TestBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
abstract class ApiTestBase {

    static final MutableClock CLOCK = new MutableClock(Instant.parse("2026-03-20T10:00:00Z"), ZoneOffset.UTC);

    @TestBean
    private Clock clock;

    static Clock clock() {
        return CLOCK;
    }
}
```

Y dos clases que heredan de ella, `BaseATest.java` y `BaseBTest.java` (en la segunda cambia `A` por `B`):

```java
package com.bootcamp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.client.RestTestClient;

class BaseATest extends ApiTestBase {

    @Autowired
    RestTestClient client;

    @Test
    void notFound() {
        client.get().uri("/api/loans/X").exchange().expectStatus().isNotFound();
    }
}
```

Borra `LoanApiCopyTest.java` y ejecuta:

```bash
mvn test | grep 'Started '
# ... Started BaseATest in 1.284 seconds (process running for 2.147)
# ... Started LoanApiTest in 0.211 seconds (process running for 2.93)
# ... Started LoanMockMvcTest in 0.166 seconds (process running for 3.289)
```

No aparece `Started BaseBTest`: `BaseBTest` reutilizó el contexto de `BaseATest`, porque los dos heredan la misma configuración. En un proyecto con muchas clases de `@SpringBootTest`, una clase base como esta ahorra un arranque por clase. Borra los tres archivos al terminar.

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
| ← [Lab 03 - El contrato JSON con @JsonTest](../lab-03-contrato-json-con-jsontest/README.md) | [Proyecto — Proyecto Semanal - La API Spring Boot del dominio](../../3-proyecto/README.md) → |

[↑ Semana 28](../../README.md)
