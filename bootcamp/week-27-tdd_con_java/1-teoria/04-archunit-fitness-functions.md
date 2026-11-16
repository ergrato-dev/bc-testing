# 04 - Fitness functions de arquitectura con ArchUnit

> Lenguaje: **Java**

---

## 🎯 Objetivos

- Explicar qué es una fitness function de arquitectura.
- Configurar ArchUnit 1.5.1 con JUnit 6 en Maven.
- Escribir reglas sobre dependencias entre paquetes, llamadas prohibidas, campos y capas.
- Leer el mensaje de una regla que falla.

---

## Qué es una fitness function

Las decisiones de diseño de las teorías anteriores ("el dominio no conoce la base de datos", "la fecha llega por un `Clock`", "las dependencias llegan por el constructor") se cumplen el día que se toman. Seis meses después, alguien con prisa escribe `LocalDate.now()` y nadie lo ve en la revisión.

Una **fitness function** de arquitectura es una comprobación automática de una de esas decisiones. Si se ejecuta con la suite, la decisión queda protegida igual que el comportamiento: un cambio que la rompe pone `mvn test` en rojo.

ArchUnit es una librería de Java que lee el bytecode de tus clases (los `.class` que genera el compilador) y comprueba reglas escritas como tests de JUnit.

---

## Configurar ArchUnit

Una dependencia en el `pom.xml`:

```xml
<dependency>
  <groupId>com.tngtech.archunit</groupId>
  <artifactId>archunit-junit5</artifactId>
  <version>1.5.1</version>
  <scope>test</scope>
</dependency>
```

- **Qué:** `archunit-junit5` trae ArchUnit y su motor para la JUnit Platform, que ejecuta los campos `@ArchTest`. El nombre dice `junit5`, pero funciona con JUnit 6: los dos usan la misma JUnit Platform (comprobado en todos los labs de la semana).
- **Para qué:** las reglas se ejecutan con `mvn test`, sin otro comando.
- **Impacto:** al arrancar escribe tres avisos `SLF4J(W): No SLF4J providers were found.` ArchUnit usa SLF4J para sus logs y no encuentra ninguna implementación; los avisos no afectan al resultado.

---

## La primera regla

```java
@AnalyzeClasses(packages = "com.bootcamp", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    @ArchTest
    static final ArchRule domainDependsOnNothingElse = noClasses()
        .that().resideInAPackage("..domain..")
        .should().dependOnClassesThat().resideInAnyPackage("..service..", "..infrastructure..");
}
```

| Pieza | Qué hace |
|---|---|
| `@AnalyzeClasses(packages = "com.bootcamp")` | Carga las clases de ese paquete y sus subpaquetes |
| `ImportOption.DoNotIncludeTests.class` | Deja fuera `src/test`: las reglas son sobre el código de producción |
| `@ArchTest` | Marca un campo `static final ArchRule` como test |
| `noClasses().that()...should()...` | La regla, en una frase: "ninguna clase de `domain` debe depender de clases de `service` o `infrastructure`" |
| `..domain..` | Cualquier paquete que contenga `domain` en algún nivel |

---

## Leer una violación

Si una clase del dominio crea un repositorio de la infraestructura ([lab 04](../2-practicas/lab-04-archunit/README.md), paso 1):

```text
Architecture Violation [Priority: MEDIUM] - Rule 'no classes that reside in a package '..domain..' should depend on classes that reside in any package ['..service..', '..infrastructure..']' was violated (2 times):
Method <com.bootcamp.domain.Loan.load(java.lang.String)> calls constructor <com.bootcamp.infrastructure.InMemoryLoanRepository.<init>()> in (Loan.java:9)
Method <com.bootcamp.domain.Loan.load(java.lang.String)> calls method <com.bootcamp.infrastructure.InMemoryLoanRepository.findById(java.lang.String)> in (Loan.java:9)
```

1. La primera línea repite la regla en inglés y dice cuántas veces se rompió.
2. Cada línea siguiente es una violación: quién (`Loan.load`), qué hace (`calls constructor`), a quién, y en qué línea del archivo.

`<init>` es el nombre que tiene un constructor dentro del bytecode.

ArchUnit mira lo que **hace** el código, no los `import`: un `import` que nadie usa desaparece al compilar y no cuenta como dependencia.

---

## Reglas de la semana

### Llamadas prohibidas

```java
@ArchTest
static final ArchRule todayComesFromAClock = noClasses()
    .should().callMethod(LocalDate.class, "now")
    .because("la fecha de hoy llega por un Clock para que los tests la controlen");
```

`callMethod(LocalDate.class, "now")` sin más argumentos se refiere a `now()` sin parámetros. `LocalDate.now(clock)` es otro método (recibe un `Clock`) y la regla lo permite. `because(...)` añade el motivo al mensaje de error, para quien rompa la regla dentro de un año.

### Campos

```java
@ArchTest
static final ArchRule serviceFieldsAreFinal = fields()
    .that().areDeclaredInClassesThat().resideInAPackage("..service..")
    .should().beFinal();
```

Un campo `final` solo se asigna en el constructor: las dependencias de un servicio no cambian después de crearlo.

### Capas

```java
@ArchTest
static final ArchRule layersAreRespected = layeredArchitecture()
    .consideringOnlyDependenciesInLayers()
    .layer("Domain").definedBy("..domain..")
    .layer("Service").definedBy("..service..")
    .layer("Infrastructure").definedBy("..infrastructure..")
    .whereLayer("Service").mayNotBeAccessedByAnyLayer()
    .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer();
```

| Capa | Quién puede usarla |
|---|---|
| `Domain` (`Loan`, la interfaz `LoanRepository`) | Todas: no tiene restricción |
| `Service` (`RenewalService`) | Ninguna otra capa |
| `Infrastructure` (`InMemoryLoanRepository`) | Ninguna otra capa |

ArchUnit obliga a decir qué dependencias mira la regla antes de definir las capas; sin esa línea, `.layer(...)` no compila.

| Modo | Qué dependencias mira |
|---|---|
| `consideringOnlyDependenciesInLayers()` | Solo las que van de una de las tres capas a otra |
| `consideringAllDependencies()` | También las que van a clases de fuera, como `java.time.Clock` o `java.lang.Object` |

Con estas dos restricciones (`mayNotBeAccessedByAnyLayer`) los dos modos dan el mismo resultado. La diferencia aparece con `mayOnlyAccessLayers("Domain")`: con `consideringAllDependencies()`, `RenewalService` la rompe solo por recibir un `Clock` (comprobado en el lab 04). Por eso los labs usan el primero.

El servicio usa la interfaz del dominio, y la infraestructura la implementa: las dos dependen del dominio, y el dominio de nadie. Ese es el mismo diseño que pedía la [teoría 03](03-outside-in-con-mockito.md).

---

## Cuántas reglas

Una regla por decisión que quieras proteger. Si una regla no corresponde a una decisión que el equipo tomó, nadie sabrá por qué existe cuando falle. En el proyecto de la semana hay tres:

- nadie pide la fecha al sistema (`LocalDate.now()`, `LocalDateTime.now()`, `Instant.now()`);
- los campos de las clases `*Service` son `final`;
- el código de producción no usa Mockito, JUnit ni AssertJ.

Cuando ArchUnit se añade a un proyecto con muchas violaciones antiguas, `FreezingArchRule` guarda las existentes y solo falla con las nuevas. La [guía de usuario](https://www.archunit.org/userguide/html/000_Index.html) lo explica; esta semana no hace falta.

---

## ✅ Resumen

- Una fitness function es una decisión de arquitectura convertida en test.
- `archunit-junit5` 1.5.1 funciona con JUnit 6; `@AnalyzeClasses` carga las clases y `@ArchTest` marca cada regla.
- Cada línea de una violación dice quién, qué hace, a quién y en qué línea.
- `because(...)` deja escrito el motivo de la regla.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 03 - Outside-in TDD con Mockito](03-outside-in-con-mockito.md) | [Índice de labs](../2-practicas/README.md) → |

[↑ Semana 27](../README.md)
