# Lab 04 - Fitness functions con ArchUnit

## Objetivo

Convertir cuatro decisiones de diseño en tests con ArchUnit y romper cada una a propósito para leer el mensaje. Las decisiones son las de los labs anteriores: el dominio no conoce la infraestructura, la fecha llega por un `Clock`, las dependencias de un servicio no cambian y las capas solo se usan hacia dentro.

## Tiempo estimado

45 minutos.

## Preparación

```bash
cd starter
mvn test
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

El código está repartido en tres paquetes:

| Paquete | Clases |
|---|---|
| `com.bootcamp.domain` | `Loan` (record) y `LoanRepository` (interfaz) |
| `com.bootcamp.service` | `RenewalService`: renueva un préstamo 14 días, como mucho dos veces |
| `com.bootcamp.infrastructure` | `InMemoryLoanRepository`: implementa `LoanRepository` con un `HashMap` |

- `src/test/java/com/bootcamp/RenewalServiceTest.java` trae dos tests activos que usan `Clock.fixed`.
- `src/test/java/com/bootcamp/ArchitectureTest.java` trae cuatro reglas comentadas, PASOS 1 a 4.
- El `pom.xml` añade `archunit-junit5` 1.5.1 ([teoría 04](../../1-teoria/04-archunit-fitness-functions.md#configurar-archunit)).

Cada experimento cambia `src/main`. Deshaz el cambio al terminar cada uno (`Ctrl+Z` o `git restore` si usas Git).

## Paso a paso

### Paso 1: el dominio no depende de nadie (PASO 1)

Descomenta el import y la regla del PASO 1.

```bash
mvn test
# SLF4J(W): No SLF4J providers were found.
# ...
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

Los avisos de SLF4J los explica la teoría 04; no afectan al resultado.

Experimento: en `domain/Loan.java`, añade este método:

```java
public static Loan load(String id) {
    return new com.bootcamp.infrastructure.InMemoryLoanRepository().findById(id).orElseThrow();
}
```

```bash
mvn test
# [ERROR] Tests run: 3, Failures: 1, Errors: 0, Skipped: 0
```

```text
Architecture Violation [Priority: MEDIUM] - Rule 'no classes that reside in a package '..domain..' should depend on classes that reside in any package ['..service..', '..infrastructure..']' was violated (2 times):
Method <com.bootcamp.domain.Loan.load(java.lang.String)> calls constructor <com.bootcamp.infrastructure.InMemoryLoanRepository.<init>()> in (Loan.java:9)
Method <com.bootcamp.domain.Loan.load(java.lang.String)> calls method <com.bootcamp.infrastructure.InMemoryLoanRepository.findById(java.lang.String)> in (Loan.java:9)
```

Dos violaciones en la misma línea: una por el `new` y otra por la llamada a `findById`. Borra el método.

### Paso 2: la fecha llega por un Clock (PASO 2)

Descomenta el import y la regla del PASO 2.

```bash
mvn test
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
```

Experimento: en `service/RenewalService.java`, cambia `LocalDate.now(clock)` por `LocalDate.now()`.

```bash
mvn test
# [ERROR]   ArchitectureTest.todayComesFromAClock Architecture Violation [Priority: MEDIUM] - Rule 'no classes should call method LocalDate.now(), because la fecha de hoy llega por un Clock para que los tests la controlen' was violated (1 times):
# Method <com.bootcamp.service.RenewalService.renew(java.lang.String)> calls method <java.time.LocalDate.now()> in (RenewalService.java:26)
# [ERROR]   RenewalServiceTest.shouldExtendTheDueDateByTwoWeeks:NN » IllegalState loan is overdue: L-1
# [ERROR] Tests run: 4, Failures: 1, Errors: 1, Skipped: 0
```

Fallan dos cosas: la regla, con el motivo que escribiste en `because(...)`, y un test de comportamiento. Con la fecha real, el préstamo que vencía el 20 de marzo de 2026 ya venció. Ese segundo fallo depende del día en que ejecutes el test; la regla falla siempre. Deshaz el cambio.

### Paso 3: los campos de un servicio son final (PASO 3)

Descomenta el import y la regla del PASO 3.

```bash
mvn test
# [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

Experimento: en `RenewalService`, quita `final` de `private final Clock clock;`.

```text
Architecture Violation [Priority: MEDIUM] - Rule 'fields that are declared in classes that reside in a package '..service..' should be final' was violated (1 times):
Field <com.bootcamp.service.RenewalService.clock> does not have modifier FINAL in (RenewalService.java:0)
```

`RenewalService.java:0`: el bytecode no guarda la línea de un campo, así que ArchUnit pone `0`. Deshaz el cambio.

### Paso 4: las capas (PASO 4)

Descomenta el import y la regla del PASO 4.

```bash
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

Experimento: un constructor "cómodo" que crea su propio repositorio. En `RenewalService`, añade el import de `com.bootcamp.infrastructure.InMemoryLoanRepository` y este constructor:

```java
public RenewalService(Clock clock) {
    this(new InMemoryLoanRepository(), clock);
}
```

```text
Architecture Violation [Priority: MEDIUM] - Rule 'Layered architecture considering only dependencies in layers, consisting of
layer 'Domain' ('..domain..')
layer 'Service' ('..service..')
layer 'Infrastructure' ('..infrastructure..')
where layer 'Service' may not be accessed by any layer
where layer 'Infrastructure' may not be accessed by any layer' was violated (1 times):
Constructor <com.bootcamp.service.RenewalService.<init>(java.time.Clock)> calls constructor <com.bootcamp.infrastructure.InMemoryLoanRepository.<init>()> in (RenewalService.java:25)
```

La regla del PASO 1 no lo detecta: solo mira el paquete `domain`. La regla de capas protege todas las direcciones a la vez. Un servicio que crea su repositorio ya no se puede probar con otro, que es justo lo que hacen `RenewalServiceTest` y los mocks de la semana 26. Deshaz el cambio.

## Comprobación

```bash
cd ../solution
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

## Preguntas de cierre

1. ¿Qué regla de este lab habría evitado el `mockStatic` del lab 03 de la [semana 26](../../../week-26-mockito_mocking_en_java/2-practicas/lab-03-strict-stubs-spy-y-static/README.md)?
2. La regla del PASO 1 y la del PASO 4 se solapan. ¿Borrarías la primera? ¿Qué ganas al dejarla?
3. Piensa en una decisión de diseño de tu proyecto que te gustaría proteger. ¿Cómo sería su regla?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - Outside-in con Mockito](../lab-03-outside-in-con-mockito/README.md) | [Proyecto — Proyecto Semanal - Un servicio del dominio construido con TDD](../../3-proyecto/README.md) → |

[↑ Semana 27](../../README.md)
