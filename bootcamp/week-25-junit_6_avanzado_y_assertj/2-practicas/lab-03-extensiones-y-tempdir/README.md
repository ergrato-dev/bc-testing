# Lab 03 - @TempDir y extensiones

## Objetivo

Probar un informe que escribe un CSV sin dejar archivos en el proyecto, con `@TempDir`, y escribir una extensión que inyecta un reloj fijo en los tests. La registrarás con `@ExtendWith` y con `@RegisterExtension`, y provocarás el error de dos extensiones que compiten.

## Tiempo estimado

55 minutos.

## Preparación

```bash
cd starter
mvn test
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

- `src/main/java/com/bootcamp/OverdueReport.java` escribe en un CSV los préstamos vencidos a fecha de hoy. "Hoy" sale del `Clock` que recibe en el constructor.
- `src/main/java/com/bootcamp/ReturnReminder.java` devuelve el texto del recordatorio: `due today`, `due tomorrow`, `due in N days` u `overdue by N day(s)`.
- `src/test/java/com/bootcamp/OverdueReportTest.java` trae un test activo (PASO 0) y los PASOS 1 y 2 comentados.
- `FixedClockExtension.java`, `ReturnReminderTest.java` y `ReturnReminderAtYearEndTest.java`, en la misma carpeta de tests, vienen comentados enteros (PASOS 3 y 4).

La [teoría 03](../../1-teoria/03-tempdir-y-extensiones.md) explica `@TempDir` y las extensiones.

## Paso a paso

### Paso 1: el archivo que se queda (PASO 0)

El test del PASO 0 escribe en `Path.of("overdue.csv")`, una ruta relativa a la carpeta desde la que ejecutas Maven. Después del `mvn test` de la preparación:

```bash
ls overdue.csv
# overdue.csv
cat overdue.csv
# 9780000000001,2026-03-10
# 9780000000003,2026-03-15
```

El archivo sigue ahí. Otro test que leyera esa ruta vería datos de una ejecución anterior. Bórralo:

```bash
rm overdue.csv
```

### Paso 2: @TempDir como parámetro (PASO 1)

1. Borra el test del PASO 0.
2. Descomenta el import marcado con `PASO 1` y el test del PASO 1.

```bash
mvn test
# [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
ls overdue.csv
# ls: cannot access 'overdue.csv': No such file or directory
```

JUnit creó un directorio vacío, se lo pasó al test en `dir` y lo borró al terminar.

### Paso 3: @TempDir como campo (PASO 2)

Descomenta el campo `reportsDir` y el test del PASO 2. El test escribe en `2026/03/overdue.csv` dentro del directorio temporal, y comprueba que `writeTo` crea las carpetas que faltan.

```bash
mvn test
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 4: la extensión y @ExtendWith (PASO 3)

1. Descomenta `FixedClockExtension.java` entero. Implementa `ParameterResolver`: cuando un test pide un parámetro de tipo `Clock`, le da un reloj fijo en el 16 de marzo de 2026.
2. Descomenta `ReturnReminderTest.java` entero. La clase lleva `@ExtendWith(FixedClockExtension.class)` y sus tests reciben `Clock clock` como parámetro.

```bash
mvn test
# [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

Experimento: comenta la línea `@ExtendWith(FixedClockExtension.class)` y ejecuta.

```bash
mvn test
# org.junit.jupiter.api.extension.ParameterResolutionException: No ParameterResolver registered for parameter [java.time.Clock arg0] in method [void com.bootcamp.ReturnReminderTest.shouldSayDueTomorrowTheDayBefore(java.time.Clock)].
# ...
# [ERROR] Tests run: 5, Failures: 0, Errors: 3, Skipped: 0
```

Son **errores**, no fallos: el test no llegó a ejecutarse. Vuelve a descomentar la línea.

### Paso 5: una extensión configurada con @RegisterExtension (PASO 4)

Descomenta `ReturnReminderAtYearEndTest.java` entero. Aquí la extensión se crea en un campo `static` con otra fecha, el 31 de diciembre, para probar un préstamo que vence en el año siguiente.

```bash
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

Experimento: añade `@ExtendWith(FixedClockExtension.class)` encima de `class ReturnReminderAtYearEndTest` (y su import, `org.junit.jupiter.api.extension.ExtendWith`).

```bash
mvn test
# org.junit.jupiter.api.extension.ParameterResolutionException: Discovered multiple competing ParameterResolvers for parameter [java.time.Clock arg0] in method [void com.bootcamp.ReturnReminderAtYearEndTest.shouldCountTheDaysAcrossTheNewYear(java.time.Clock)]: com.bootcamp.FixedClockExtension@732d0d24, com.bootcamp.FixedClockExtension@3370f42
```

Dos extensiones saben crear un `Clock` y JUnit no elige. Los números tras `@` cambian en cada ejecución. Quita la anotación y el import.

## Comprobación

```bash
cd ../solution
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

## Preguntas de cierre

1. ¿Qué problema tendría `OverdueReport` si llamara a `LocalDate.now()` sin recibir un `Clock`?
2. ¿Cuándo usarías `@RegisterExtension` en lugar de `@ExtendWith`?
3. ¿Por qué el campo de `@RegisterExtension` es `static`?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - @Nested y @Tag](../lab-02-nested-y-tags/README.md) | [Lab 04 - AssertJ y soft assertions](../lab-04-assertj-y-soft-assertions/README.md) → |

[↑ Semana 25](../../README.md)
