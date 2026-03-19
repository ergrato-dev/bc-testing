# Ejercicio 02 — Aplicar BVA y Partición de Equivalencia

> **Semana 02 · Prácticas · Ejercicio 02** | Duración estimada: 1.5 h

---

## Objetivo

Diseñar una suite de casos de prueba manuales usando técnicas formales de diseño: partición de equivalencia y análisis de valores límite (BVA). El objetivo es cubrir todos los escenarios con el mínimo de casos necesarios.

---

## Contexto

Eres el QA engineer de un sistema de reservas de un planetario virtual. Debes diseñar los casos de prueba para la función de registro de visitantes. La funcionalidad tiene las siguientes reglas de negocio:

### Función: `registrarVisitante(nombre, edad, grupoEscolar)`

**Reglas**:
- **`nombre`**: campo de texto, entre 2 y 50 caracteres, solo letras y espacios (sin números ni especiales)
- **`edad`**: número entero, entre 5 y 99 años
- **`grupoEscolar`**: booleano (`true`/`false`)
  - Si `grupoEscolar = true` → solo se acepta si `3 ≤ edad ≤ 17`
  - Si `grupoEscolar = false` → no hay restricción adicional sobre la edad (dentro del rango válido)

---

## Instrucciones

### Paso 1 — Partición de equivalencia para cada campo

Abre `starter/analisis-particion.md` y completa la tabla de clases de equivalencia para los tres campos.

### Paso 2 — Aplicar BVA

Abre `starter/analisis-bva.md` y completa la tabla de valores límite para los campos `edad` y `nombre` (longitud).

### Paso 3 — Diseñar la suite mínima

Abre `starter/suite-minima.md` y escribe los casos de prueba combinando los resultados de los pasos anteriores. No dupliques casos que cubran la misma clase.

---

## Referencia rápida

**Partición de equivalencia**: un valor por clase (válida e inválida)
**BVA**: en cada límite → `límite - 1`, `límite`, `límite + 1`

---

## Criterios de Evaluación

- Clases de equivalencia correctas (válidas e inválidas) → **6 pts**
- BVA aplicado en todos los límites correctamente → **6 pts**
- Sin casos redundantes en la suite final → **4 pts**
- Nombres de casos claros → **4 pts**
