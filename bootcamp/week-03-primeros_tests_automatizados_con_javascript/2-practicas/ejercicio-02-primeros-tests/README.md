# Ejercicio 02 — Primeros Tests Unitarios con AAA

> **Semana 03 · Prácticas · Ejercicio 02** | Duración estimada: 2 h

---

## Objetivo

Escribir una suite básica de tests unitarios para funciones puras usando Jest, aplicando:

- Nomenclatura descriptiva
- Patrón AAA
- Matchers correctos

---

## Contexto

Trabajarás con un módulo simple de utilidades de validación (`user-utils.js`) que modela reglas comunes de negocio:

- Validación de email
- Cálculo de descuento
- Validación de mayoría de edad

---

## Instrucciones

### Paso 1 — Instalar y ejecutar en modo watch

Desde `starter/`:

```bash
pnpm install
pnpm test:watch
```

Mientras no descomentes ningún bloque, Jest avisará `Your test suite must contain at least one test`. Es lo esperado.

### Paso 2 — PASO 1: tests de `isAdult`

Abre `starter/tests/user-utils.test.js` y descomenta el bloque `PASO 1`. Fíjate en los comentarios `// Arrange`, `// Act`, `// Assert` y en el borde `18` / `17`.

### Paso 3 — PASO 2: tests de `calculateDiscount`

Descomenta el bloque `PASO 2`. Observa que para `toThrow` se pasa una **función** a `expect` (`() => calculateDiscount(...)`); por eso Act y Assert van juntos.

### Paso 4 — PASO 3: tests de `isValidEmail`

Descomenta el bloque `PASO 3`. Compara el uso de `toBeTruthy`/`toBeFalsy` con `toBe(true)`/`toBe(false)` del PASO 1.

### Paso 5 — Revisar nombres

Comprueba que todos los tests siguen el formato:

```text
should [resultado esperado] when [condición]
```

---

## Resultado esperado

- Al menos 6 tests en verde
- Cobertura de happy path y casos inválidos
- AAA visible en cada test

---

## Criterios de evaluación

- Uso correcto de `toBe`, `toBeTruthy`/`toBeFalsy` y `toThrow`
- Tests legibles y reproducibles
- Nombres claros orientados a comportamiento
