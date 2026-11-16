# Proyecto Semanal - Un servicio del dominio construido con TDD

> **🎯 ÚNICO ENTREGABLE**: este proyecto es el único entregable obligatorio de la semana.

## Objetivo

Hasta ahora escribiste los tests de un servicio que ya existía. Esta semana construyes un servicio **nuevo** de tu dominio con TDD, ciclo a ciclo, y lo dejas protegido con tres reglas de arquitectura:

- al menos **15 ciclos** Red → Green → Refactor, cada uno con su commit y su fila en `TDD-LOG.md`;
- reglas de negocio puras (cálculos, sin dependencias) hechas inside-out, sin mocks;
- el servicio hecho outside-in, con sus dependencias como interfaces y la fecha por un `Clock`;
- tres fitness functions con ArchUnit que se ejecutan con `mvn test`.

## Dónde va

En la carpeta `java/` de tu repositorio `qa-<nombre>-<apellido>` ([proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)), sobre la entrega de la [semana 26](../../week-26-mockito_mocking_en_java/3-proyecto/README.md).

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

El servicio nuevo es la operación que **cierra** la operación principal de la semana 26: si allí prestabas, aquí se devuelve; si reservabas, aquí se cancela o se cumple; si vendías, aquí se devuelve el producto. Tiene que tener:

| Pieza | En el ejemplo (museo) |
|---|---|
| Una regla que depende de la fecha | Días de retraso desde la fecha de vencimiento del préstamo |
| Al menos dos cálculos | Recargo por retraso (50 por día, máximo 1.500) y cargo por daños (10 % del valor declarado) |
| Un cambio de estado que depende de la entrada | Una pieza en buen estado vuelve a `AVAILABLE`; una dañada pasa a `IN_RESTORATION` |
| Al menos tres dependencias | `PieceRepository`, `LoanRepository`, `Notifier` y el `Clock` |
| Un aviso con el resultado | `ReturnNotice` con los días de retraso y los dos cargos |

En el ejemplo, `ReturnService.receive(code, condition)` recibe una pieza prestada: busca el préstamo abierto, calcula los cargos, guarda la pieza con su nuevo estado, cierra el préstamo y avisa a la institución.

## Paso a paso

### Paso 1: rama, pom.xml y fitness functions

1. Crea la rama de la semana:

   ```bash
   cd qa-<nombre>-<apellido>
   git switch main && git pull
   git switch -c week-27
   ```

2. Copia el starter:

   ```bash
   cp -r <ruta-al-bootcamp>/bootcamp/week-27-tdd_con_java/3-proyecto/starter/. .
   ```

   - `java/pom.xml` **sustituye** al tuyo: es el de la semana 26 con `archunit-junit5` 1.5.1. Si tenías otras dependencias, vuelve a añadirlas.
   - `java/src/test/java/com/bootcamp/ArchitectureTest.java` es nuevo: tres reglas ([teoría 04](../1-teoria/04-archunit-fitness-functions.md#cuántas-reglas)).
   - `java/TDD-LOG.md` es nuevo: la tabla donde anotas cada ciclo.

3. Ejecuta:

   ```bash
   cd java
   mvn test
   # [INFO] Tests run: 34, Failures: 0, Errors: 0, Skipped: 0
   ```

   Tus tests de la semana 26 siguen pasando (31 en el ejemplo) y las tres reglas también. Si una regla falla, tu código de la semana 26 ya la rompía: lee el mensaje y arréglalo antes de seguir. El caso más común es `LocalDate.now()`: cámbialo por un `Clock` como en el paso 2.

4. Primer commit:

   ```bash
   git add -A
   git commit -m "chore: add ArchUnit fitness functions"
   ```

### Paso 2: la lista de tests

Antes de escribir código, apunta en un papel (o al final de `TDD-LOG.md`, fuera de la tabla) los comportamientos que esperas. Es la lista de tareas de Kent Beck: no tiene que estar completa, y la irás cambiando.

Lista del ejemplo, en el orden en que se hicieron:

1. Días de retraso: cero el día del vencimiento, cero si llega antes, los días que pasan si llega tarde.
2. Recargo: 50 por día, nunca más de 1.500.
3. Cargo por daños: cero en buen estado, 10 % si está dañada, redondeado hacia abajo.
4. El servicio: estado de la pieza, cierre del préstamo, aviso con los cargos, orden de las operaciones, rechazos.

Empieza por los cálculos (inside-out, [lab 01](../2-practicas/lab-01-prime-factors/README.md)) y sigue por el servicio (outside-in, [lab 03](../2-practicas/lab-03-outside-in-con-mockito/README.md)). La fecha la recibe el servicio por el constructor:

```java
public ReturnService(PieceRepository pieces, LoanRepository loans, Notifier notifier, Clock clock) {
```

En los tests, `Clock.fixed(...)`; nunca `LocalDate.now()` (la regla `todayComesFromAClock` lo impide).

### Paso 3: los ciclos

Para cada comportamiento de la lista:

1. **Red.** Escribe un test, ejecuta `mvn test -Dtest=<TuClaseDeTest>` y copia la línea que explica el fallo.
2. **Green.** El código mínimo que lo hace pasar. Ejecuta.
3. **Refactor.** Si hay algo que mejorar, mejóralo con la suite en verde.
4. **Suite completa** (`mvn test`) y **commit**, con el tipo de la [teoría 01](../1-teoria/01-ciclo-tdd-en-java.md#un-commit-por-ciclo).
5. **Una fila en `TDD-LOG.md`.**

| Columna | Qué escribes | Ejemplo |
|---|---|---|
| `#` | Número del ciclo | `2` |
| Test | Nombre del método de test | `` `shouldCountTheDaysAfterTheDueDate` `` |
| Red: por qué falló | La línea del fallo, o "pasó en verde" si fue de confirmación | `` `expected: 1L but was: 0L` `` |
| Green: qué cambiaste | El cambio, en pocas palabras | `` `ChronoUnit.DAYS.between(dueDate, returnedOn)` `` |
| Refactor | Lo que mejoraste, o `—` | `Constante LATE_FEE_PER_DAY` |
| Commit | El mensaje del commit | `` `feat: count late days` `` |

El ejemplo del museo tiene 17 ciclos en dos clases de test: `ReturnFeesTest` (8 métodos, sin mocks, con `@Nested` y `@ParameterizedTest` de la [semana 25](../../week-25-junit_6_avanzado_y_assertj/README.md)) y `PieceReturnTest` (9 métodos, con `@Mock`, `@Captor` e `InOrder` de la semana 26).

```bash
mvn test
# [INFO] Tests run: 57, Failures: 0, Errors: 0, Skipped: 0
```

El total depende de tu dominio; debe ser 35 o más, sin `Skipped`.

Al terminar, el historial de la rama cuenta la historia del servicio:

```bash
git log --oneline main..HEAD | wc -l
# 18
```

Diecisiete ciclos más el commit del paso 1.

### Paso 4: comprobar que la suite detecta errores

Haz estos cambios en el código de producción, uno cada vez. Con cada uno, `mvn test` debe fallar; si alguno pasa en verde, falta un test. Deshaz cada cambio antes del siguiente.

1. Quita el `Math.max(0, ...)` (o tu equivalente) de los días de retraso.
2. Cambia el máximo del recargo (en el ejemplo, `1500` por `1550`).
3. Borra la línea que cierra la operación (`loans.close(loan)`).
4. Cambia `LocalDate.now(clock)` por la fecha de vencimiento.
5. Cambia `LocalDate.now(clock)` por `LocalDate.now()`.

En el ejemplo, el cambio 2 pasó en verde la primera vez: el test comparaba con la constante del código. La [teoría 02](../1-teoria/02-diseno-emergente-y-refactoring.md#el-valor-esperado-no-sale-del-código) explica el arreglo.

### Paso 5: verificar y entregar

Desde la raíz del bootcamp:

```bash
PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 27
```

Añade al `README.md` de tu repositorio un párrafo: qué partes del servicio hiciste inside-out y cuáles outside-in, y qué decisión de diseño protege cada regla de `ArchitectureTest`.

Sube la rama y abre un pull request a `main`. Quien revise puede seguir los ciclos commit a commit.

## ✅ Requisitos verificables

Copia de [checks.json](../checks.json):

| Comprobación | Pista |
|---|---|
| La suite Java pasa con `mvn test` | Pasos 3 y 4 |
| El `pom.xml` incluye ArchUnit | Paso 1 |
| La suite tiene al menos 3 reglas `@ArchTest` | Paso 1 |
| El código de producción recibe la fecha por un `Clock` | Paso 2 |
| No queda ningún `@Disabled` en `src/test` | Paso 3 |
| Al menos 35 ejecuciones de test, sin fallos ni saltados | Paso 3 |
| `TDD-LOG.md` registra al menos 15 ciclos | Paso 3 |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - Fitness functions con ArchUnit](../2-practicas/lab-04-archunit/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 27](../README.md)
