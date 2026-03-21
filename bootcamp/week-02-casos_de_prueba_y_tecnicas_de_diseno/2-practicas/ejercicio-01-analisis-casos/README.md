# Ejercicio 01 — Analizar y Completar Casos de Prueba

> **Semana 02 · Prácticas · Ejercicio 01** | Duración estimada: 1.5 h

---

## Objetivo

Entrenar el ojo crítico para detectar defectos en casos de prueba mal escritos. Un caso con campo faltante, resultado esperado vago o pasos ambiguos no puede ser ejecutado de forma reproducible.

---

## Contexto

Eres el nuevo QA engineer de un equipo que tiene una suite de casos de prueba heredada. Al revisar los casos, encuentras varios problemas: títulos que no describen la acción, precondiciones ausentes, pasos que saltan etapas y resultados esperados que no son verificables.

Tu misión es **corregirlos**.

---

## Instrucciones

### Paso 1 — Leer los casos problemáticos

Abre `starter/casos-problematicos.md`. Encontrarás 4 casos de prueba con defectos de distinto tipo.

### Paso 2 — Identificar los problemas

Para cada caso, responde en tu hoja de trabajo:
- ¿Qué campo falta o está incompleto?
- ¿El resultado esperado es observable y concreto?
- ¿Los pasos son reproducibles sin conocimiento implícito?
- ¿La severidad y prioridad son coherentes?

### Paso 3 — Reescribir los casos corregidos

En `starter/casos-corregidos.md` (está vacío), reescribe cada caso con todos los campos correctos.

---

## Referencia rápida — Campos obligatorios

| Campo | Qué debe incluir |
|---|---|
| **ID** | Formato `TC-[MÓDULO]-[NNN]` |
| **Título** | Acción + condición + resultado esperado resumido |
| **Precondiciones** | Estado exacto del sistema antes de ejecutar |
| **Datos de prueba** | Valores específicos a usar (no genéricos) |
| **Pasos** | Numerados, atómicos, sin ambigüedades |
| **Resultado esperado** | Observable, concreto, sin "funciona bien" |
| **Severidad / Prioridad** | Coherentes entre sí |

---

## Criterios de Evaluación

- Identificar correctamente el defecto en ≥ 3 de 4 casos → **6 pts**
- Precondiciones claras y completas → **4 pts**
- Pasos redactados sin ambigüedades → **4 pts**
- Resultados esperados observables → **4 pts**
- Severidad/prioridad coherentes → **2 pts**
