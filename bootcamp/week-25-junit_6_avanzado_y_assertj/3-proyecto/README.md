# Proyecto Semanal - Suite Java del dominio con JUnit 6 y AssertJ

> **🎯 ÚNICO ENTREGABLE**: este proyecto es el único entregable obligatorio de la semana.

## Objetivo

Llevar al proyecto hilo conductor la suite Java de la [semana 6](../../week-06-plan_de_pruebas_y_suite_base_multilenguaje/3-proyecto/README.md) y refactorizarla con lo que viste esta semana:

- contextos `@Nested`, uno por operación del servicio;
- tests parametrizados para las validaciones y los cálculos;
- solo aserciones de AssertJ, con soft assertions donde un resultado tiene varios campos.

## Dónde va

En tu repositorio `qa-<nombre>-<apellido>` ([proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)) empieza esta semana la carpeta `java/`, que crecerá hasta la semana 31.

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

## Paso a paso

### Paso 1: traer la suite de la semana 6

1. Crea la rama de la semana:

   ```bash
   cd qa-<nombre>-<apellido>
   git switch main && git pull
   git switch -c week-25
   ```

2. Copia tu entrega Java de la semana 6 en `java/`: `pom.xml`, `src/main/java/com/bootcamp/ItemService.java` (o el nombre que le diste) y `src/test/java/com/bootcamp/ItemServiceTest.java`.
3. Comprueba que pasa y guárdala en un commit. Ese commit es tu punto de partida:

   ```bash
   cd java
   mvn test
   # [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
   cd ..
   git add java
   git commit -m "chore(java): import week 6 suite"
   ```

   El número de tests es el de tu entrega de la semana 6.

### Paso 2: copiar el starter

```bash
cp -r <ruta-al-bootcamp>/bootcamp/week-25-junit_6_avanzado_y_assertj/3-proyecto/starter/. .
```

Sin `-n`: el starter **sustituye** dos archivos.

- `java/pom.xml`: JUnit 6.0.3, AssertJ 3.27.7 y Surefire 3.5.6 con versión exacta. Si tu `pom.xml` tenía otras dependencias, vuelve a añadirlas.
- `java/src/test/java/com/bootcamp/ItemServiceTest.java`: la plantilla de la suite refactorizada, con contextos `@Nested` y tests `@Disabled("TODO: ...")`.

Tu versión anterior del test sigue en el commit del paso 1. Ábrela al lado para pasar cada caso:

```bash
git show HEAD:java/src/test/java/com/bootcamp/ItemServiceTest.java
```

Ejecuta la plantilla:

```bash
cd java
mvn test
# [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 5
```

Los cinco tests aparecen como saltados (`Skipped`) hasta que quites sus `@Disabled`.

### Paso 3: contextos @Nested (al menos dos)

1. Renombra `ItemService` e `ItemServiceTest` a la entidad de tu dominio, por ejemplo `PieceService` y `PieceServiceTest`.
2. Crea el servicio en el `@BeforeEach` de la clase de fuera.
3. El primer `@Nested` agrupa la operación de creación (los TC-001 a TC-003 de tu `test-plan.md`).
4. El segundo agrupa otra operación del dominio: un préstamo, una reserva, una venta. Su `@BeforeEach` crea los datos que comparten sus tests.
5. Quita cada `@Disabled` cuando el test tenga cuerpo. Al terminar no debe quedar ninguno.

Referencia: [lab 02](../2-practicas/lab-02-nested-y-tags/README.md).

### Paso 4: tests parametrizados (al menos tres)

1. **TC-002**: un solo test con `@NullAndEmptySource` y `@ValueSource(strings = {"   "})` para el nombre.
2. **TC-003**: convierte el test del monto en `@ParameterizedTest` con `0`, `-1` y la frontera de tu dominio.
3. **Un cálculo del dominio** (precio, recargo, seguro...) con `@CsvSource` y un `name` que diga qué caso es. Ponlo en un tercer `@Nested`.
4. Añade los casos frontera que falten: longitud máxima del nombre, formato del código, duplicados.

La suite debe sumar **al menos 15 ejecuciones**: cada fila de un test parametrizado cuenta como una.

Referencia: [lab 01](../2-practicas/lab-01-tests-parametrizados/README.md).

### Paso 5: AssertJ y soft assertions

1. Sustituye cualquier `assertEquals`, `assertTrue` o `assertThrows` por su equivalente de AssertJ. El archivo no debe importar `org.junit.jupiter.api.Assertions`.
2. En el TC-001, comprueba todos los campos del objeto creado con `assertSoftly`.
3. Si una operación devuelve una lista, compruébala con `extracting(...)` y `containsExactly(...)`.

Referencia: [lab 04](../2-practicas/lab-04-assertj-y-soft-assertions/README.md).

### Paso 6: verificar y entregar

```bash
cd java
mvn test
# [INFO] Tests run: 27, Failures: 0, Errors: 0, Skipped: 0
```

El total depende de tu dominio; debe ser 15 o más, sin `Skipped`. Desde la raíz del bootcamp:

```bash
PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 25
```

Añade al `README.md` de tu repositorio un párrafo: qué test de la semana 6 se convirtió en un test parametrizado y qué casos nuevos aparecieron al hacerlo.

Haz commit, sube la rama y abre un pull request a `main`.

## ✅ Requisitos verificables

Copia de [checks.json](../checks.json):

| Comprobación | Pista |
|---|---|
| La suite Java pasa con `mvn test` | Pasos 1 y 2 |
| No queda ningún `@Disabled` en `src/test` | Paso 3 |
| Al menos 15 ejecuciones de test, sin fallos ni saltados | Pasos 3 y 4 |
| Al menos 3 tests parametrizados | Paso 4 |
| Al menos 2 contextos `@Nested` | Paso 3 |
| Usa soft assertions de AssertJ | Paso 5 |
| Las aserciones son de AssertJ, no de `org.junit.jupiter.api.Assertions` | Paso 5 |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - AssertJ y soft assertions](../2-practicas/lab-04-assertj-y-soft-assertions/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 25](../README.md)
