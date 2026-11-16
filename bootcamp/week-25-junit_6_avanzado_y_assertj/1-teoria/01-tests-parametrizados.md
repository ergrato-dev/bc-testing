# 01 - Tests parametrizados con JUnit 6

> Lenguaje: **Java**

---

## 🎯 Objetivos

- Escribir un solo método de test que se ejecute una vez por cada caso.
- Elegir la fuente de argumentos adecuada: `@ValueSource`, `@NullAndEmptySource`, `@CsvSource`, `@EnumSource` o `@MethodSource`.
- Dar a cada ejecución un nombre que diga qué caso falló.

---

## La idea, comparada con lo que ya conoces

Ya parametrizaste en JavaScript con `test.each` ([semana 8](../../week-08-matchers_avanzados_y_parametrizacion/1-teoria/02-tests-parametrizados-jest.md)) y en Python con `@pytest.mark.parametrize` ([semana 17](../../week-17-parametrizacion_y_marks_con_pytest/1-teoria/01-parametrizacion-efectiva-pytest.md)). La idea es la misma: el cuerpo del test se escribe una vez y cada fila de datos es una ejecución independiente.

La diferencia en JUnit es que la tabla de casos va en anotaciones encima del método, y los valores llegan como parámetros del método:

| | Jest | pytest | JUnit 6 |
|---|---|---|---|
| Marca el test | `test.each(tabla)` | `@pytest.mark.parametrize` | `@ParameterizedTest` |
| Dónde van los datos | Array o tabla | Lista de tuplas | Una anotación *source* |
| Nombre de cada caso | `%s`, `$variable` | `ids=` o `pytest.param(..., id=)` | `name = "..."` o `argumentSet` |

---

## Dependencia

`@ParameterizedTest` vive en el módulo `junit-jupiter-params`. No hace falta añadirlo: el artefacto `junit-jupiter` del `pom.xml` que usas desde la [semana 5](../../week-05-primeros_tests_automatizados_con_java/1-teoria/01-setup-junit.md) ya lo incluye.

```xml
<dependency>
  <groupId>org.junit.jupiter</groupId>
  <artifactId>junit-jupiter</artifactId>
  <version>6.0.3</version>
  <scope>test</scope>
</dependency>
```

---

## Paso 1: del test repetido al test parametrizado

Estos dos tests solo cambian en los datos:

```java
@Test
@DisplayName("should not charge a standard member on the last grace day")
void shouldNotChargeAStandardMemberOnTheLastGraceDay() {
    assertThat(calculator.feeFor(2, Membership.STANDARD)).isEqualTo(0);
}

@Test
@DisplayName("should charge a standard member the first day after grace")
void shouldChargeAStandardMemberTheFirstDayAfterGrace() {
    assertThat(calculator.feeFor(3, Membership.STANDARD)).isEqualTo(500);
}
```

Con `@CsvSource`, cada fila es un caso y cada columna, un parámetro del método:

```java
@ParameterizedTest
@CsvSource({
    "0,  0",
    "2,  0",
    "3,  500",
    "10, 4000",
    "22, 10000",
    "30, 10000"
})
@DisplayName("should charge standard members by days late")
void shouldChargeStandardMembersByDaysLate(int daysLate, int expectedFee) {
    assertThat(calculator.feeFor(daysLate, Membership.STANDARD)).isEqualTo(expectedFee);
}
```

1. `@ParameterizedTest` sustituye a `@Test`: no pongas las dos anotaciones en el mismo método.
2. Cada fila del CSV es un texto. JUnit convierte `"3"` en el `int` del parámetro: es la **conversión implícita**, que también funciona con `long`, `boolean`, `enum`, `LocalDate` y otros tipos comunes.
3. Los espacios alrededor de cada valor se ignoran, así que puedes alinear las columnas.

Las seis filas cuentan como seis tests:

```bash
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

---

## Paso 2: las fuentes de argumentos

| Fuente | Úsala cuando | Ejemplo |
|---|---|---|
| `@ValueSource` | Un solo parámetro de tipo simple | `@ValueSource(ints = {-1, -30})` |
| `@NullAndEmptySource` | El método debe rechazar `null` y `""` | Junto a `@ValueSource(strings = {"   "})` |
| `@CsvSource` | Varios parámetros por caso | `"3, 500"` |
| `@EnumSource` | Un caso por cada valor de un `enum` | `@EnumSource(Membership.class)` |
| `@MethodSource` | Objetos que no caben en un texto, o casos con nombre | Un método `static` que devuelve `Stream<Arguments>` |

`@ValueSource` y `@NullAndEmptySource` se combinan; las ejecuciones se suman:

```java
@ParameterizedTest
@NullAndEmptySource
@ValueSource(strings = {"   ", "\t"})
@DisplayName("should reject a missing name (TC-002)")
void shouldRejectAMissingName(String name) {
    assertThatThrownBy(() -> service.register("MUS-001", name, 250_000))
        .isInstanceOf(ValidationException.class)
        .hasMessage("name is required");
}
```

Esto son cuatro ejecuciones: `null`, `""`, `"   "` y `"\t"`.

`@EnumSource` recorre el `enum` entero. Si mañana alguien añade un valor, el test lo cubre sin tocarlo:

```java
@ParameterizedTest
@EnumSource(Membership.class)
@DisplayName("should not charge any membership during the grace period")
void shouldNotChargeAnyMembershipDuringTheGracePeriod(Membership membership) {
    assertThat(calculator.feeFor(LateFeeCalculator.GRACE_DAYS, membership)).isZero();
}
```

`@MethodSource` lee los casos de un método `static` de la misma clase. Con `argumentSet` cada caso lleva un nombre:

```java
@ParameterizedTest
@MethodSource("discountedCases")
@DisplayName("should charge half to discounted memberships")
void shouldChargeHalfToDiscountedMemberships(int daysLate, Membership membership, int expectedFee) {
    assertThat(calculator.feeFor(daysLate, membership)).isEqualTo(expectedFee);
}

static Stream<Arguments> discountedCases() {
    return Stream.of(
        argumentSet("student, first day after grace", 3, Membership.STUDENT, 250),
        argumentSet("senior, ten days late", 10, Membership.SENIOR, 2000),
        argumentSet("student, capped fee", 40, Membership.STUDENT, 5000)
    );
}
```

---

## Paso 3: el nombre de cada ejecución

Cuando falla una fila, Surefire indica su posición entre corchetes. Si cambias la fila `"10, 4000"` por `"10, 4500"`:

```text
[ERROR] com.bootcamp.LateFeeCalculatorTest.shouldChargeStandardMembersByDaysLate(int, int)[4] -- Time elapsed: 0.007 s <<< FAILURE!
expected: 4500
 but was: 4000
```

`[4]` es la cuarta fila. Para saber qué caso es sin contar filas, dale un nombre con el atributo `name`:

```java
@ParameterizedTest(name = "[{index}] {0} days late, fee {1}", quoteTextArguments = false)
```

| Marcador | Qué pone |
|---|---|
| `{index}` | Número de la ejecución, desde 1 |
| `{0}`, `{1}`... | El argumento en esa posición |
| `{arguments}` | Todos los argumentos separados por comas |

`quoteTextArguments = false` evita un cambio de JUnit 6: por defecto rodea de comillas los argumentos de texto. Como las filas de `@CsvSource` son texto antes de convertirse en `int`, sin esa opción el nombre sale como `"10" days late, fee "4000"`.

La consola de Maven no muestra el nombre de cada ejecución, pero el informe XML de Surefire sí. El `pom.xml` del [lab 01](../2-practicas/lab-01-tests-parametrizados/README.md) lo activa:

```xml
<configuration>
  <statelessTestsetReporter implementation="org.apache.maven.plugin.surefire.extensions.junit5.JUnit5Xml30StatelessReporter">
    <usePhrasedTestCaseMethodName>true</usePhrasedTestCaseMethodName>
  </statelessTestsetReporter>
</configuration>
```

- **Qué:** escribe en `target/surefire-reports/TEST-*.xml` el `@DisplayName` y el nombre de cada ejecución, en lugar del nombre del método.
- **Para qué:** ver qué casos se ejecutaron y cómo se llaman.
- **Impacto:** solo cambia el XML; la salida de `mvn test` es la misma.

```bash
grep -o 'testcase name="[^"]*"' target/surefire-reports/TEST-*.xml
# testcase name="should charge standard members by days late[1] 0 days late, fee 0"
# testcase name="should charge standard members by days late[2] 2 days late, fee 0"
# ...
# testcase name="should charge half to discounted memberships[1] student, first day after grace"
```

---

## Cuándo no parametrizar

Parametriza cuando **el comportamiento esperado es el mismo** y solo cambian los datos. Si necesitas un `if` dentro del test para decidir qué comprobar, son dos comportamientos y van en dos tests:

```java
@Test
@DisplayName("should accept a name of exactly 120 characters")
void shouldAcceptANameOfExactly120Characters() { ... }

@Test
@DisplayName("should reject a name of 121 characters")
void shouldRejectANameOf121Characters() { ... }
```

---

## ✅ Resumen

- `@ParameterizedTest` con una fuente de argumentos ejecuta el método una vez por caso, y cada caso cuenta como un test.
- `@CsvSource` para varios parámetros simples, `@ValueSource` para uno, `@EnumSource` para recorrer un `enum` y `@MethodSource` para objetos o casos con nombre.
- Un buen `name` (o `argumentSet`) dice qué caso falló sin contar filas.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Semana 25 - Java Testing I: JUnit 6 Avanzado y AssertJ](../README.md) | [Teoría — 02 - Organizar la suite con @Nested y @Tag](02-nested-y-tags.md) → |

[↑ Semana 25](../README.md)
