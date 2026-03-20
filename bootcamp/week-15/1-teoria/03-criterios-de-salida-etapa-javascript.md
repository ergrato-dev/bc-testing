# 03 - Criterios de Salida de la Etapa JavaScript

## Objetivo

Definir que significa "cerrar bien" etapa JavaScript antes de pasar a Python.

---

## Lenguaje de esta semana

**Aplica a**: JavaScript (Jest) en cierre de etapa.

---

## Criterios minimos sugeridos

1. Suite base estable y repetible en local y CI.
2. Coverage >=85% en modulos criticos, con ramas de error cubiertas.
3. Uso consciente de al menos 3 enfoques:
   - unit,
   - integration,
   - snapshot o properties.
4. Nomenclatura clara en tests (`should ... when ...`).
5. Documentacion de deuda tecnica de testing pendiente.

---

## Evidencias recomendadas

- Reporte de cobertura (`lcov` o HTML).
- Captura/log de pipeline exitoso.
- Lista breve de riesgos no cubiertos aun.
- Decision justificada de quality gate.

---

## Checklist de salida por niveles

### Nivel minimo aceptable

- [ ] Tests pasan en local.
- [ ] Tests pasan en CI.
- [ ] Coverage del modulo critico >=85%.
- [ ] Al menos 3 pruebas de error relevantes.

### Nivel recomendado

- [ ] Cobertura de ramas clave (no solo lineas).
- [ ] Quality gate configurado para PR.
- [ ] Snapshot/property usados con criterio y sin ruido.
- [ ] Deuda tecnica registrada con prioridad.

### Nivel sobresaliente

- [ ] Suite sin flaky tests en multiples corridas.
- [ ] Diagnostico de fallos rapido (nombres y asserts claros).
- [ ] Estrategia de testing documentada por modulo.

---

## Ejemplo de deuda tecnica bien escrita

Correcto:

"Pendiente cubrir rama de timeout del adapter de pagos; impacto alto en checkout; prioridad P1 para semana 16".

Incorrecto:

"Faltan tests".

La deuda tecnica debe tener:

- modulo,
- riesgo,
- prioridad,
- accion siguiente.

---

## Plantilla breve de retro de etapa

1. Que tipo de bug detectamos mejor ahora que al inicio?
2. Que parte de la suite genera mas ruido y por que?
3. Que guardrail de CI fue mas util para el equipo?
4. Que practica migraremos tal cual a Python?

---

## Puente hacia week-16

Antes de iniciar Python, deja listo:

- mapa de modulos criticos JS (referencia comparativa),
- checklist de calidad reutilizable,
- lecciones aprendidas sobre fragilidad de tests.

Esto acelera la curva de aprendizaje en `pytest`, porque la estrategia permanece aunque cambie la sintaxis.

---

## Transicion saludable a Python

Lo que se mantiene:

- Patron AAA,
- foco en riesgo,
- calidad de assertions,
- estrategia por capas.

Lo que cambia:

- framework (`pytest`),
- ecosistema de mocking,
- estilo de fixtures y convenciones de nombres.

---

## Cierre

Una buena salida de etapa no es "ya vi todo Jest". Es poder justificar decisiones de calidad y sostener una suite confiable en automatizacion continua.
