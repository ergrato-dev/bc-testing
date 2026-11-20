# Proyecto - Primera suite de tests del dominio

> Entregable obligatorio de la semana 05.

## Objetivo

Escribir en Java las funciones de tu dominio de las semanas [3](../../week-03-primeros_tests_automatizados_con_javascript/3-proyecto/README.md) y [4](../../week-04-primeros_tests_automatizados_con_python/3-proyecto/README.md) y una suite de JUnit 6 que automatice los mismos casos: camino feliz, entradas inválidas y valores límite. Al terminar tendrás la misma funcionalidad probada en tres lenguajes.

## Tiempo estimado

3 horas.

## Requisitos

- Tu proyecto de la semana 4: funciones, tests y `mutaciones.md`.
- Tu `casos-de-prueba.md` de la [semana 2](../../week-02-casos_de_prueba_y_tecnicas_de_diseno/3-proyecto/README.md), para los ID de los casos.
- Los [labs](../2-practicas/README.md) de esta semana.
- El dominio que te asignó el instructor.

## Alcance

Solo **funciones puras**, como en las semanas 3 y 4: reciben datos y devuelven un resultado o lanzan un error. No leen la fecha del sistema, no usan bases de datos, ficheros ni red. Si una regla depende de «hoy», el método recibe la fecha de hoy como parámetro (un `LocalDate`).

---

## Paso 1: el proyecto (15 min)

```bash
cp -r <ruta-del-bootcamp>/bootcamp/week-05-primeros_tests_automatizados_con_java/3-proyecto/starter ~/proyecto-semana-05
cd ~/proyecto-semana-05
mvn test
git init
echo "target/" > .gitignore
git add .
git commit -m "proyecto semana 05: punto de partida"
```

El `mvn test` del starter termina así:

```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 1
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
```

El único test está marcado con `@Disabled`: JUnit no lo ejecuta y Surefire lo cuenta como `Skipped`, el `test.todo` de Jest. Un `Skipped` distinto de 0 es trabajo pendiente.

El starter trae:

| Archivo | Qué es |
|---|---|
| `pom.xml` | Java 21, JUnit 6.0.3, AssertJ 3.27.7 y Surefire 3.5.6 fijados; no cambies esas versiones |
| `src/main/java/com/bootcamp/Domain.java` | Clase vacía con `TODO` |
| `src/test/java/com/bootcamp/DomainTest.java` | Un test `@Disabled` y los `TODO` de la suite |
| `mutaciones.md` | Tabla de mutaciones con `TODO` |

## Paso 2: las funciones (45 min)

Renombra `Domain.java` con el nombre de tu funcionalidad en `PascalCase` (por ejemplo, `Reservation.java`), y la clase igual. Traduce tus funciones a métodos `public static`. Al menos tres:

- Uno que **valide** una entrada y lance `new IllegalArgumentException("mensaje")` si es inválida. El mensaje es el de tu regla.
- Uno que **calcule** algo: un importe, una cantidad, si una fecha está en rango.
- Uno que **formatee** o transforme un dato: un código, un nombre, un texto para mostrar.

Nombres de métodos y variables en inglés y en `camelCase` (`validate_group_size` vuelve a ser `validateGroupSize`); mensajes en el idioma de tu dominio, idénticos a los de las semanas 3 y 4.

Traducir no es copiar línea a línea: revisa cada regla.

| Python | Java |
|---|---|
| `isinstance(people, int)` | Sobra si el parámetro es `int`: Java no deja pasar un `2.5`. El caso del decimal no se automatiza; anótalo en un comentario |
| `date(2026, 11, 18)` | `LocalDate.of(2026, 11, 18)` (con `import java.time.LocalDate;`) |
| `(day - today).days` | `ChronoUnit.DAYS.between(today, day)` (con `import java.time.temporal.ChronoUnit;`) |
| `{"Individual": 12}` | `Map.of("Individual", 12)` (con `import java.util.Map;`) |
| `text.strip().upper()` | `text.strip().toUpperCase()` |
| `a == b` con textos | `a.equals(b)`: nunca `==` ([teoría 03](../1-teoria/03-assertions-y-ejecucion.md#textos-nunca-)) |

Borra los `TODO` de `src/main/` cuando termines.

## Paso 3: los tests (60 min)

Renombra `DomainTest.java` igual que tu clase, más `Test` (`ReservationTest.java`): si no termina en `Test`, Surefire no la ejecuta. Borra el test `@Disabled` y traduce tus tests de la semana 4. Al menos **8 tests**:

| Tipo | Mínimo | De dónde salen |
|---|---|---|
| Camino feliz | 3 | Las clases válidas de tu partición |
| Entradas inválidas | 3, uno o más con `assertThrows` o `assertThatThrownBy` | Las clases inválidas, una por test |
| Valores límite | 2 | Tu tabla de límites: el límite y su vecino de fuera |

Reglas:

- Cada test con `@Test`, `@DisplayName("should ... when ...")` y un método `should<Resultado>When<Condición>`; agrupa los de cada método en una clase `@Nested` si quieres.
- AAA en el orden de siempre, y el ID del caso de la semana 2 en un comentario encima de `@Test`.
- Una comprobación como mínimo en cada test, con el esperado escrito a mano y **primero** en `assertEquals`.
- La comprobación de la [teoría 03](../1-teoria/03-assertions-y-ejecucion.md#resumen-de-comparaciones): delta `0.0001` para decimales y `assertEquals` para textos.
- Al menos una comprobación con AssertJ: `assertThat(...).isEqualTo(...)` o `assertThatThrownBy(...)`.
- Ningún `@Disabled` ni `System.out.println`.

## Paso 4: verde (15 min)

```bash
mvn test
```

Si no compila, arregla el primer error de la lista. Si un test falla, decide si el error está en el método o en el test, como en el [lab 03](../2-practicas/lab-03-de-casos-a-tests/README.md). Si un test que pasaba en pytest falla en JUnit, mira primero las diferencias de la teoría 03 (orden de `assertEquals`, delta, `==` con textos). Si encuentras un bug en tu método, apúntalo: es un hallazgo real.

Comprueba en la última línea `Tests run:` que se ejecutan todos tus tests.

## Paso 5: mutaciones (30 min)

Comprueba que tu suite protege el código, como en el [lab 04](../2-practicas/lab-04-tests-que-mienten/README.md). Repite en Java al menos dos de las mutaciones de la semana 4 (cambia un `>=` por `>`, un número, un mensaje), ejecuta `mvn test` con cada una y deshazla con `git restore src/main/`.

Anota cada una en `mutaciones.md`, con el nombre exacto del método de test que la detecta:

```markdown
| M1 | `people > 8` | `people > 9` | shouldThrowAnErrorWhenTheGroupHas9People |
```

Si ninguna mutación pone un test en rojo, te falta un test: escríbelo y vuelve a probar. Si la línea tiene un `|` (como `||`), escríbela sin él: rompe la tabla.

## Paso 6: comprobar y entregar (15 min)

Desde la carpeta del bootcamp:

```bash
PROYECTO=~/proyecto-semana-05 ./scripts/verificar-semana.sh 05
```

Cuando pasen las 10 comprobaciones, haz un commit y entrega por el canal que indique el instructor.

---

## Ejemplo

Un dominio de ejemplo, el **acuario virtual** de la semana 2. Reglas: el grupo tiene de 1 a 8 personas; el tour Familiar exige 3 o más; la fecha va de mañana a 60 días después de hoy.

```java
// src/main/java/com/bootcamp/TourBooking.java
public static boolean validateGroupSize(int people, String tourType) {
    if (people < 1 || people > 8) {
        throw new IllegalArgumentException("Entre 1 y 8 personas");
    }
    if (tourType.equals("Familiar") && people < 3) {
        throw new IllegalArgumentException("El tour familiar es para 3 personas o más");
    }
    return true;
}
```

```java
// src/test/java/com/bootcamp/TourBookingTest.java
// TC-ACUA-TOUR-006
@Test
@DisplayName("should throw an error when the group has 9 people")
void shouldThrowAnErrorWhenTheGroupHas9People() {
    // Arrange
    int people = 9;

    // Act + Assert
    assertThatThrownBy(() -> TourBooking.validateGroupSize(people, "Individual"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Entre 1 y 8 personas");
}
```

Tu entrega usa tu dominio, tus reglas y tus casos.

---

## ✅ Requisitos verificables

Copia legible de [checks.json](../checks.json):

| Comprobación | Qué mira |
|---|---|
| compila | `mvn -q test-compile` descarga las dependencias y compila código y tests |
| versiones | El `pom.xml` fija `maven.compiler.release` 21, `junit-jupiter` 6.0.3, `assertj-core` 3.27.7 y `maven-surefire-plugin` 3.5.6 |
| funciones | `src/main/java` define al menos tres métodos `public static` |
| pasa | La última línea `Tests run:` de `mvn test` dice 8 o más, con `Failures: 0, Errors: 0, Skipped: 0` |
| nombres | Tantos métodos `should...When...()` y `@DisplayName("should ... when ...")` como `@Test` |
| errores | Al menos un `assertThrows(` o `assertThatThrownBy(` en `src/test/java` |
| assertj | Al menos un `assertThat(` o `assertThatThrownBy(` |
| sin-trampas | Ningún `Disabled` ni `System.out` en los tests, y tantas líneas con `assert...(` como `@Test` |
| mutaciones | `mutaciones.md` sin `TODO` y con dos filas o más que nombran un test `should...When...` |
| sin-todo | Ningún `TODO` en `src/` |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - Tests que mienten](../2-practicas/lab-04-tests-que-mienten/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 05](../README.md)
