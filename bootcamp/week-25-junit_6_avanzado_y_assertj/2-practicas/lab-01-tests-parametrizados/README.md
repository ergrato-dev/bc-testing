# Lab 01 - Tests parametrizados

## Objetivo

Convertir cuatro tests repetidos en un test parametrizado y practicar las fuentes de argumentos de JUnit 6: `@CsvSource`, `@ValueSource`, `@EnumSource` y `@MethodSource`. Vas a provocar un fallo en una fila para ver cómo lo informa Maven.

## Tiempo estimado

70 minutos.

## Preparación

```bash
cd starter
mvn test
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

La primera ejecución descarga JUnit, AssertJ y Surefire, y tarda más.

- `src/main/java/com/bootcamp/LateFeeCalculator.java` calcula la multa por devolver un libro tarde: dos días de gracia, 500 por día después y un tope de 10 000. Las membresías `STUDENT` y `SENIOR` pagan la mitad.
- `src/test/java/com/bootcamp/LateFeeCalculatorTest.java` trae cuatro tests activos (PASO 0) y los PASOS 1 a 4 comentados.

La [teoría 01](../../1-teoria/01-tests-parametrizados.md) explica cada fuente de argumentos.

## Paso a paso

### Paso 1: leer el problema (PASO 0)

Abre `LateFeeCalculatorTest.java` y lee los cuatro tests del PASO 0. Solo cambian dos números: los días de retraso y la multa esperada. Añadir el caso de 10 días o el de 22 significaría copiar un quinto y un sexto método.

### Paso 2: un test, seis casos (PASO 1)

1. Borra los cuatro tests del PASO 0 y el import de `org.junit.jupiter.api.Test` que va marcado con `PASO 0`.
2. Descomenta los imports marcados con `PASO 1` y el test del PASO 1.
3. Ejecuta:

```bash
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

Un método, seis tests: uno por fila de `@CsvSource`.

### Paso 3: experimento, una fila que falla

Cambia la fila `"10, 4000"` por `"10, 4500"` y ejecuta:

```bash
mvn test
# [ERROR] com.bootcamp.LateFeeCalculatorTest.shouldChargeStandardMembersByDaysLate(int, int)[4] -- Time elapsed: 0.007 s <<< FAILURE!
# expected: 4500
#  but was: 4000
# ...
# [ERROR] Tests run: 6, Failures: 1, Errors: 0, Skipped: 0
```

`[4]` es la cuarta fila. Las otras cinco siguen pasando: cada fila es un test independiente.

Ahora mira el nombre que JUnit dio a cada ejecución. El `pom.xml` de este lab escribe esos nombres en el informe XML (la teoría 01 explica esa configuración):

```bash
grep -o 'testcase name="[^"]*"' target/surefire-reports/TEST-*.xml
# testcase name="should charge standard members by days late[1] 0 days late, fee 0"
# testcase name="should charge standard members by days late[2] 2 days late, fee 0"
# testcase name="should charge standard members by days late[3] 3 days late, fee 500"
# testcase name="should charge standard members by days late[4] 10 days late, fee 4500"
# testcase name="should charge standard members by days late[5] 22 days late, fee 10000"
# testcase name="should charge standard members by days late[6] 30 days late, fee 10000"
```

El texto después de `[4]` sale del atributo `name = "[{index}] {0} days late, fee {1}"`.

Vuelve a dejar la fila como `"10, 4000"`.

### Paso 4: un solo argumento (PASO 2)

Descomenta los imports marcados con `PASO 2` y el test del PASO 2. `@ValueSource` pasa un único valor por ejecución; aquí, dos números negativos que el cálculo debe rechazar.

```bash
mvn test
# [INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 5: todos los valores de un enum (PASO 3)

Descomenta el import marcado con `PASO 3` y el test del PASO 3. `@EnumSource(Membership.class)` ejecuta el test una vez por cada membresía.

```bash
mvn test
# [INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
```

Si añades un valor nuevo a `Membership`, este test lo cubre sin cambiarlo.

### Paso 6: casos con nombre (PASO 4)

Descomenta los imports marcados con `PASO 4`, el test del PASO 4 y el método `discountedCases()`.

```bash
mvn test
# [INFO] Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
```

`@MethodSource("discountedCases")` busca un método `static` con ese nombre en la misma clase. Cada `argumentSet` lleva un nombre y los argumentos del test, en orden:

```bash
grep -o 'testcase name="should charge half[^"]*"' target/surefire-reports/TEST-*.xml
# testcase name="should charge half to discounted memberships[1] student, first day after grace"
# testcase name="should charge half to discounted memberships[2] senior, ten days late"
# testcase name="should charge half to discounted memberships[3] student, capped fee"
```

## Comprobación

```bash
cd ../solution
mvn test
# [INFO] Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

## Preguntas de cierre

1. ¿Por qué las filas `"2, 0"` y `"3, 500"` son las más importantes de la tabla?
2. ¿Qué ventaja tiene `@EnumSource` frente a escribir las tres membresías en un `@CsvSource`?
3. ¿Cuándo elegirías `@MethodSource` en lugar de `@CsvSource`?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - @Nested y @Tag](../lab-02-nested-y-tags/README.md) → |

[↑ Semana 25](../../README.md)
