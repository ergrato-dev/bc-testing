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

### Paso 1 — Abre `starter/tests/user-utils.test.js`

Encontrarás bloques comentados por pasos. Ve descomentando sección por sección.

### Paso 2 — Ejecuta tests continuamente

```bash
pnpm install
pnpm test
```

### Paso 3 — Completa assertions faltantes

Algunas secciones requieren completar matcher o valor esperado.

### Paso 4 — Refactor de nombres

Asegura formato:

```text
should [resultado esperado] when [condicion]
```

---

## Resultado esperado

- Al menos 6 tests en verde
- Cobertura de happy path y casos inválidos
- AAA visible en cada test

---

## Criterios de evaluación

- Uso correcto de `toBe`, `toEqual`, `toThrow`
- Tests legibles y reproducibles
- Nombres claros orientados a comportamiento
