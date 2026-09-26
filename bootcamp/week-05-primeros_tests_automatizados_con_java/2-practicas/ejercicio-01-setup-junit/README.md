# Ejercicio 01 — Setup y Primera Ejecución con JUnit 6

> **Semana 05 · Prácticas · Ejercicio 01** | Duración estimada: 1.5 h

---

## Objetivo

Configurar un proyecto Maven con JUnit 6 y completar el ciclo inicial:

1. Suite vacía que compila
2. Test en rojo que revela un bug
3. Corrección mínima
4. Suite en verde

---

## Instrucciones

### Paso 1 — Revisar estructura

```bash
cd starter
```

Revisa `pom.xml` (dependencia `junit-jupiter` con `scope` `test` y `maven-surefire-plugin`) y la estructura `src/main/java` / `src/test/java`.

### Paso 2 — Primera ejecución

```bash
mvn test
```

Todos los tests están comentados, así que Maven compila y termina en verde sin ejecutar nada:

```text
Tests run: 0, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Una suite en verde con 0 tests no prueba nada: por eso siempre hay que leer el número de tests ejecutados, no solo `BUILD SUCCESS`.

### Paso 3 — Descomentar el PASO 1 y ver el rojo

Abre `starter/src/test/java/com/bootcamp/CalculatorTest.java`, descomenta el bloque del `PASO 1` y ejecuta `mvn test` de nuevo:

```text
[ERROR] Failures:
[ERROR]   CalculatorTest.shouldReturnFiveWhenAddingTwoAndThree:25 expected: <5> but was: <-1>
[ERROR] Tests run: 1, Failures: 1, Errors: 0, Skipped: 0
BUILD FAILURE
```

Lee la línea completa: clase, método, número de línea del test y valor esperado frente al real.

### Paso 4 — Corregir la implementación

Abre `starter/src/main/java/com/bootcamp/Calculator.java` y corrige el bug intencional de `add`. Ejecuta `mvn test` hasta ver `Tests run: 1, Failures: 0`.

### Paso 5 — Descomentar los PASO 2 y 3

Descomenta los bloques restantes y vuelve a ejecutar. Debes terminar con:

```text
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## Resultado esperado

- Estructura Maven + JUnit funcional
- Lectura del resumen de Surefire (tests ejecutados, failures, errors)
- Corrección mínima y reproducible
