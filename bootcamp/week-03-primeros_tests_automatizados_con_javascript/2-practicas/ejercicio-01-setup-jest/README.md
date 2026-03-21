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

### Paso 3 — Ejecutar tests

```bash
pnpm test
```

Verás un test fallando por diseño. Lee el mensaje de error.

### Paso 4 — Abrir test y descomentar por secciones

Abre `starter/tests/math.test.js` y sigue los bloques `PASO 1`, `PASO 2`, `PASO 3`.

### Paso 5 — Corregir función

Abre `starter/src/math.js`. Hay una implementación intencionalmente incorrecta para practicar ciclo red-green.

### Paso 6 — Ejecutar de nuevo hasta ver todo en verde

```bash
pnpm test
```

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
