# Ejercicio 01 — Análisis de Bugs

**Tiempo estimado**: 45–60 minutos
**Tipo**: Análisis y corrección de reportes existentes
**Material**: `starter/bugs-para-analizar.md`

---

## Objetivo

Desarrollar el ojo crítico para identificar qué hace bueno o malo a un reporte de bug. Aprenderás a:

- Detectar información faltante o ambigua
- Corregir títulos de bug según el formato profesional
- Asignar severidad y prioridad de forma justificada

---

## Contexto

Eres el QA Lead de un equipo que recibe reportes de diferentes testers. Antes de que los desarrolladores trabajen en ellos, debes revisarlos y asegurarte de que cada uno tenga la información necesaria para ser reproducible y priorizable.

---

## Instrucciones

Abre el archivo `starter/bugs-para-analizar.md`.

Encontrarás **5 reportes de bug** con problemas distintos. Para cada uno:

### Paso 1 — Lee el reporte completo

Antes de hacer cambios, entiende qué está tratando de comunicar el tester.

### Paso 2 — Identifica los problemas

Responde para cada reporte:
- ¿Qué información falta?
- ¿El título describe claramente el problema y el componente?
- ¿La severidad y prioridad son correctas? ¿Por qué?
- ¿Los pasos son reproducibles tal como están escritos?

### Paso 3 — Corrige el reporte

Reescribe los campos problemáticos directamente en el archivo `starter/bugs-para-analizar.md`.

Usa el formato de corrección al pie de cada reporte:

```markdown
### Mi análisis

**Problemas encontrados:**
- ...

**Título corregido:** `[Componente] Descripción`

**Severidad corregida:** High — justificación: ...

**Prioridad corregida:** P2 — justificación: ...

**Pasos corregidos:**
1. ...
2. ...
```

---

## Escala de referencia

Repasa estos criterios antes de empezar:

| Severidad | Cuándo usarla |
| --- | --- |
| Critical | El sistema no arranca, pérdida de datos, crash total |
| High | Funcionalidad principal rota, sin forma de evitarlo |
| Medium | Funcionalidad secundaria afectada o hay un workaround |
| Low | Cosmético, typo, mínimo impacto |

| Prioridad | Cuándo usarla |
| --- | --- |
| P1 | Resolver hoy — bloquea el negocio o a usuarios activos |
| P2 | Resolver en el sprint actual |
| P3 | Resolver los próximos sprints |
| P4 | Backlog sin urgencia |

---

## ¿Terminaste?

Compara tu análisis con `solution/bugs-analizados.md`.

No existe una única respuesta correcta para severidad/prioridad — lo importante es que tu **justificación** sea coherente con el impacto real del bug.
