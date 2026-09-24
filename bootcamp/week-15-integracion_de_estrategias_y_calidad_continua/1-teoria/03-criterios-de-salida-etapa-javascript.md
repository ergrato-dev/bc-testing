# 03 - Criterios de Salida de la Etapa JavaScript

## Objetivo

Definir que significa "cerrar bien" etapa JavaScript antes de pasar a Python.

---

## Lenguaje de esta semana

**Aplica a**: JavaScript (Jest) en cierre de etapa.

---

## Criterios mínimos sugeridos

1. Suite base estable y repetible en local y CI.
2. Coverage >=85% en módulos críticos, con ramas de error cubiertas.
3. Uso consciente de al menos 3 enfoques:
   - unit,
   - integration,
   - snapshot o properties.
4. Nomenclatura clara en tests (`should ... when ...`).
5. Documentación de deuda técnica de testing pendiente.

---

## Evidencias recomendadas

- Reporte de cobertura (`lcov` o HTML).
- Captura/log de pipeline exitoso.
- Lista breve de riesgos no cubiertos aún.
- Decisión justificada de quality gate.

---

## Checklist de salida por niveles

### Nivel mínimo aceptable

- [ ] Tests pasan en local.
- [ ] Tests pasan en CI.
- [ ] Coverage del módulo crítico >=85%.
- [ ] Al menos 3 pruebas de error relevantes.

### Nivel recomendado

- [ ] Cobertura de ramas clave (no solo líneas).
- [ ] Quality gate configurado para PR.
- [ ] Snapshot/property usados con criterio y sin ruido.
- [ ] Deuda técnica registrada con prioridad.

### Nivel sobresaliente

- [ ] Suite sin flaky tests en múltiples corridas.
- [ ] Diagnóstico de fallos rápido (nombres y asserts claros).
- [ ] Estrategia de testing documentada por módulo.

---

## Ejemplo de deuda técnica bien escrita

Correcto:

"Pendiente cubrir rama de timeout del adapter de pagos; impacto alto en checkout; prioridad P1 para semana 16".

Incorrecto:

"Faltan tests".

La deuda técnica debe tener:

- módulo,
- riesgo,
- prioridad,
- acción siguiente.

---

## Plantilla breve de retro de etapa

1. ¿Qué tipo de bug detectamos mejor ahora que al inicio?
2. ¿Qué parte de la suite genera más ruido y por qué?
3. ¿Qué guardrail de CI fue más útil para el equipo?
4. ¿Qué práctica migraremos tal cual a Python?

---

## Puente hacia week-16-fundamentos_con_pytest

Antes de iniciar Python, deja listo:

- mapa de módulos críticos JS (referencia comparativa),
- checklist de calidad reutilizable,
- lecciones aprendidas sobre fragilidad de tests.

Esto acelera la curva de aprendizaje en `pytest`, porque la estrategia permanece aunque cambie la sintaxis.

---

## Transición saludable a Python

Lo que se mantiene:

- Patrón AAA,
- foco en riesgo,
- calidad de assertions,
- estrategia por capas.

Lo que cambia:

- framework (`pytest`),
- ecosistema de mocking,
- estilo de fixtures y convenciones de nombres.

---

## Cierre

Una buena salida de etapa no es "ya vi todo Jest". Es poder justificar decisiones de calidad y sostener una suite confiable en automatización continua.
