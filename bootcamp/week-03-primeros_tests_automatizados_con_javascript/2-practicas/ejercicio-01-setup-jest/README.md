# Ejercicio 01 — Setup y Primera Ejecución con Jest

> **Semana 03 · Prácticas · Ejercicio 01** | Duración estimada: 1.5 h

---

## Objetivo

Levantar por primera vez un entorno de tests unitarios con Jest y validar el ciclo básico:

1. Test en rojo (falla)
2. Corrección mínima
3. Test en verde (pasa)

---

## Instrucciones

### Paso 1 — Revisar estructura

Abre la carpeta `starter/`. Encontrarás:

- `src/math.js`
- `tests/math.test.js`
- `package.json`

### Paso 2 — Instalar dependencias

Desde terminal en esa carpeta:

```bash
pnpm install
```

### Paso 3 — Ejecutar tests (rojo)

```bash
pnpm test
```

Verás un test fallando por diseño: `should return 5 when adding 2 and 3` está activo en `tests/math.test.js`, pero `add` tiene un bug. Lee el mensaje de error (`Expected: 5`, `Received: -1`).

### Paso 4 — PASO 1: corregir la función (verde)

Abre `starter/src/math.js` y sigue el bloque `PASO 1`: sustituye la resta por la suma. Ejecuta `pnpm test` y comprueba que el test pasa a verde.

### Paso 5 — PASO 2 y PASO 3: añadir tests de `isEven`

Abre `starter/tests/math.test.js` y descomenta los bloques `PASO 2` (número par) y `PASO 3` (número impar). Observa la estructura AAA de cada test.

### Paso 6 — Ejecutar de nuevo hasta ver todo en verde

```bash
pnpm test
```

Deben pasar los 3 tests.

---

## Resultado esperado

- Comprendes cómo correr Jest localmente
- Sabes interpretar un fallo básico de assertion
- Corriges una función mínima para pasar una prueba

---

## Criterios de evaluación

- Entorno ejecuta tests correctamente
- Identifica causa raíz del fallo inicial
- Corrige implementación sin romper legibilidad
- Mantiene nomenclatura de tests descriptiva
