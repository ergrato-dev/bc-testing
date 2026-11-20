# Lab 03 - De casos de prueba a tests

## Objetivo

Automatizar los casos de partición y valores límite del formulario del planetario de la semana 2, encontrar los bugs que esconde su implementación y documentarlos antes de arreglarlos.

## Tiempo estimado

70 minutos.

## Requisitos

- El [lab 02 de la semana 2](../../../week-02-casos_de_prueba_y_tecnicas_de_diseno/2-practicas/lab-02-particion-y-valores-limite/README.md): las reglas del formulario y su análisis.
- El proyecto `~/labs-semana-05` con los labs 01 y 02.

## Punto de partida

```bash
cd ~/labs-semana-05
cp -r <ruta-del-bootcamp>/bootcamp/week-05-primeros_tests_automatizados_con_java/2-practicas/lab-03-de-casos-a-tests/starter/. .
mvn test -Dtest=VisitorValidatorTest | grep "Tests run" | tail -1
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

## Contexto

El equipo de desarrollo ha terminado el formulario de registro del planetario: `VisitorValidator.validate` en `src/main/java/com/bootcamp/VisitorValidator.java`. Recibe un `Visitor` y devuelve `"Visitante registrado"` o el mensaje del primer campo inválido. Las reglas son las de la semana 2:

| Campo | Regla | Mensaje si no se cumple |
|---|---|---|
| `name` | De 2 a 50 caracteres; solo letras (con tildes y ñ) y espacios | `El nombre debe tener de 2 a 50 caracteres` o `El nombre solo admite letras y espacios` |
| `age` | Número entero de 5 a 99 | `La edad debe ser un número entero de 5 a 99` |
| `schoolGroup` | `true` o `false`. Con `true`, la edad tiene que ser 17 o menos | `Los grupos escolares son para menores de 18 años` |

`age` es un `double`, como el `number` de JavaScript: el formulario puede enviar `10.5`.

**No abras `VisitorValidator.java` hasta el paso 5.** Pruebas el formulario como en la semana 2, desde los requisitos y sin mirar el código: así los tests comprueban lo que se pidió, no lo que se programó.

---

## Paso 1: los dos tests de ejemplo (10 min)

Abre `src/test/java/com/bootcamp/VisitorValidatorTest.java`. Arriba hay un visitante válido:

```java
private static final Visitor VALID_VISITOR = new Visitor("Ana", 30, false);
```

`static final` es la forma de Java de declarar una constante: un valor de la clase que no cambia. El nombre en mayúsculas es la convención. Cada test crea su visitante cambiando un solo campo:

```java
Visitor visitor = VALID_VISITOR.withName("A");
// Visitor[name=A, age=30.0, schoolGroup=false]
```

`withName`, `withAge` y `withSchoolGroup` están en `Visitor.java`: cada uno devuelve una copia con ese campo cambiado, el `{ ...VALID_VISITOR, name: "A" }` de JavaScript. Un `record` no se puede modificar, así que `VALID_VISITOR` no cambia. Así cada test tiene una sola clase inválida y el resto válido, la regla de la [semana 2](../../../week-02-casos_de_prueba_y_tecnicas_de_diseno/1-teoria/02-tecnicas-de-diseno.md#una-clase-inválida-por-caso). Para cambiar dos campos, encadena: `VALID_VISITOR.withAge(16).withSchoolGroup(true)`.

Los tests están agrupados en tres clases `@Nested`, `Name`, `Age` y `SchoolGroup`, como los tres `describe` de la semana 3. Cada test va dentro de las llaves de su clase, con cuatro espacios más de sangría.

## Paso 2: los casos de name (15 min)

Escribe un test por fila, en la clase `Name`, con el formato de los ejemplos (AAA, `@Test`, `@DisplayName`). Pon el ID en un comentario encima de `@Test`, para saber de qué caso viene:

| ID | Técnica | Cambia | Esperado |
|---|---|---|---|
| TC-PLAN-REG-001 | Límite (vecino de fuera) | `withName("A")` | Mensaje de 2 a 50 caracteres (ya escrito) |
| TC-PLAN-REG-002 | Límite (mínimo) | `withName("Al")` | `Visitante registrado` |
| TC-PLAN-REG-003 | Límite (vecino de dentro) | `withName("Ana")` | `Visitante registrado` |
| TC-PLAN-REG-004 | Límite (vecino de dentro) | `withName("a".repeat(49))` | `Visitante registrado` |
| TC-PLAN-REG-005 | Límite (máximo) | `withName("a".repeat(50))` | `Visitante registrado` |
| TC-PLAN-REG-006 | Límite (vecino de fuera) | `withName("a".repeat(51))` | Mensaje de 2 a 50 caracteres |
| TC-PLAN-REG-007 | Partición (clase inválida) | `withName("Ana3")` | Mensaje de solo letras y espacios |
| TC-PLAN-REG-008 | Partición (clase válida) | `withName("José Núñez")` | `Visitante registrado` |

`"a".repeat(49)` crea un texto con 49 letras `a`: así no las cuentas a mano.

Ejecuta `mvn test -Dtest=VisitorValidatorTest`. **Si un test falla, no lo cambies todavía**: primero comprueba que el test dice lo que dice la tabla. Si es así, déjalo en rojo y sigue.

## Paso 3: los casos de age (15 min)

En la clase `Age`:

| ID | Técnica | Cambia | Esperado |
|---|---|---|---|
| TC-PLAN-REG-009 | Partición (clase válida) | `withAge(30)` | `Visitante registrado` (ya escrito) |
| TC-PLAN-REG-010 | Límite (vecino de fuera) | `withAge(4)` | Mensaje de edad |
| TC-PLAN-REG-011 | Límite (mínimo) | `withAge(5)` | `Visitante registrado` |
| TC-PLAN-REG-012 | Límite (vecino de dentro) | `withAge(6)` | `Visitante registrado` |
| TC-PLAN-REG-013 | Límite (vecino de dentro) | `withAge(98)` | `Visitante registrado` |
| TC-PLAN-REG-014 | Límite (máximo) | `withAge(99)` | `Visitante registrado` |
| TC-PLAN-REG-015 | Límite (vecino de fuera) | `withAge(100)` | Mensaje de edad |
| TC-PLAN-REG-016 | Partición (no entero) | `withAge(10.5)` | Mensaje de edad |

El método del caso 016 se llama, por ejemplo, `shouldRejectTheVisitorWhenAgeIs10Point5`: un nombre de método no admite el punto.

## Paso 4: los casos de schoolGroup (10 min)

En la clase `SchoolGroup`. Aquí cambian dos campos, porque la regla relaciona los dos:

| ID | Técnica | Cambia | Esperado |
|---|---|---|---|
| TC-PLAN-REG-017 | Límite (vecino de dentro) | `withAge(16).withSchoolGroup(true)` | `Visitante registrado` |
| TC-PLAN-REG-018 | Límite (máximo) | `withAge(17).withSchoolGroup(true)` | `Visitante registrado` |
| TC-PLAN-REG-019 | Límite (vecino de fuera) | `withAge(18).withSchoolGroup(true)` | Mensaje de grupos escolares |

Ejecuta la suite completa del lab:

```bash
mvn test -Dtest=VisitorValidatorTest | grep "Tests run" | tail -1
# [ERROR] Tests run: 19, Failures: 4, Errors: 0, Skipped: 0
```

Si te salen otros números, compara tus tests con las tablas antes de seguir: un esperado mal copiado también da rojo.

## Paso 5: documentar los hallazgos (15 min)

Ahora sí, abre `VisitorValidator.java`. Por cada test en rojo, copia el bloque de `hallazgos.md` y rellénalo: el caso, el test, el esperado y el obtenido del informe, si falla el código o el test, y la línea de `VisitorValidator.java` que lo causa.

Con clases `@Nested`, Surefire anuncia cada grupo por su `@DisplayName` y el resumen final no lleva número de línea:

```text
[INFO] Running age
[ERROR] Tests run: 8, Failures: 2, Errors: 0, Skipped: 0, Time elapsed: 0.021 s <<< FAILURE! -- in age
...
[ERROR] Failures: 
[ERROR]   VisitorValidatorTest.shouldRegisterTheGroupWhenAgeIs17 expected: <Visitante registrado> but was: <Los grupos escolares son para menores de 18 años>
[ERROR]   VisitorValidatorTest.shouldRegisterTheVisitorWhenAgeIs99 expected: <Visitante registrado> but was: <La edad debe ser un número entero de 5 a 99>
[ERROR]   VisitorValidatorTest.shouldRegisterTheVisitorWhenNameHas2Characters expected: <Visitante registrado> but was: <El nombre debe tener de 2 a 50 caracteres>
[ERROR]   VisitorValidatorTest.shouldRejectTheVisitorWhenAgeIs10Point5 expected: <La edad debe ser un número entero de 5 a 99> but was: <Visitante registrado>
```

La línea del `assertEquals` está en el bloque de cada fallo, en su línea `at`; `$Age` es la clase `@Nested`:

```text
	at com.bootcamp.VisitorValidatorTest$Age.shouldRejectTheVisitorWhenAgeIs10Point5(VisitorValidatorTest.java:158)
```

El número de línea depende de cómo hayas escrito tus tests.

Fíjate en qué técnica encontró cada bug: los límites encuentran los `<` y `>=` mal puestos; la partición encuentra la regla que falta. Para el 10,5, busca qué línea comprueba que la edad no tenga decimales.

## Paso 6: arreglar y volver a verde (5 min)

Corrige `VisitorValidator.java` hasta que pasen los 19 tests. No toques ningún test: si el hallazgo dice que el fallo es del código, se arregla el código.

Para la regla que falta: `Math.floor(age)` quita los decimales de `age`, así que `age != Math.floor(age)` es verdadero cuando la edad tiene decimales. `||` es el «o» de Java.

```bash
git add .
git commit -m "lab 03: casos del planetario automatizados"
```

---

## Comprobación final

```bash
mvn test -Dtest=VisitorValidatorTest | grep "Tests run" | tail -1
# [INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0
grep -c '^## Hallazgo' hallazgos.md
# 4
grep -c TODO hallazgos.md src/test/java/com/bootcamp/VisitorValidatorTest.java
# hallazgos.md:0
# src/test/java/com/bootcamp/VisitorValidatorTest.java:0
```

Repasa:

- [ ] Cada test cambia solo los campos de su fila y lleva encima el ID del caso.
- [ ] Cada hallazgo nombra la línea del código que lo causa y la técnica que lo encontró.
- [ ] Ningún esperado se cambió para que el test pasara.

## Qué has aprendido

- Una tabla de partición y valores límite se convierte en tests casi línea a línea; el ID en un comentario mantiene la trazabilidad con el caso.
- Los valores límite encuentran comparaciones mal escritas; la partición, reglas que nadie programó.
- Escribiste 19 tests casi iguales. La [semana 25](../../../week-25-junit_6_avanzado_y_assertj/1-teoria/01-tests-parametrizados.md) los convierte en una tabla con `@ParameterizedTest`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - AAA y assertions](../lab-02-aaa-y-assertions/README.md) | [Lab 04 - Tests que mienten](../lab-04-tests-que-mienten/README.md) → |

[↑ Semana 05](../../README.md)
