# Rúbrica de Evaluación - Semana 14

## Evidencias y ponderación

| Evidencia | Peso | Descripción |
|---|---:|---|
| Conocimiento | 30% | Lectura crítica de cobertura y calidad de pruebas |
| Desempeño | 40% | Prácticas guiadas sobre mejoras de suite con Jest |
| Producto | 30% | Proyecto de hardening de suite con criterios de riesgo |

---

## 1) Conocimiento (30%)

### Criterios

1. Explica diferencias entre `lines`, `statements`, `functions` y `branches`, y por qué sin `collectCoverageFrom` un archivo nunca importado no cuenta.
2. Justifica por qué 100% de cobertura puede seguir ocultando defectos.
3. Identifica síntomas de tests frágiles y tests redundantes.
4. Propone mejoras priorizadas por impacto de negocio.

### Niveles

- **Alto (27-30 pts)**: interpreta métricas con criterio técnico y de producto.
- **Medio (21-26 pts)**: comprende bases con oportunidades de profundidad.
- **Bajo (0-20 pts)**: confunde métricas o interpreta cobertura de forma literal.

---

## 2) Desempeño (40%)

### Criterios

1. Completa ejercicios guiados descomentando por pasos.
2. Escribe o ajusta tests para cubrir ramas relevantes (errores, bordes, reglas).
3. Reduce fragilidad eliminando asserts ambiguos o snapshots de alto ruido.
4. Mantiene nomenclatura clara y patrón AAA.

### Niveles

- **Alto (36-40 pts)**: suite estable, legible y orientada a riesgo.
- **Medio (28-35 pts)**: funcionamiento correcto con mejoras pendientes.
- **Bajo (0-27 pts)**: cobertura superficial o alta fragilidad.

---

## 3) Producto (30%)

### Criterios

1. Adapta la plantilla del proyecto a su dominio asignado.
2. Define objetivo de cobertura por módulo crítico (no solo global) y lo hace cumplir con `collectCoverageFrom` + `coverageThreshold`.
3. Implementa tests para al menos 3 rutas de fallo relevantes.
4. Documenta decisiones de calidad tomadas (qué se agregó y por qué).

### Niveles

- **Alto (27-30 pts)**: evidencia mejora real de capacidad de detección.
- **Medio (21-26 pts)**: cumple mínimos sin justificar completamente.
- **Bajo (0-20 pts)**: cambios mínimos o sin relación con riesgos reales.

---

## Penalizaciones

- Perseguir cobertura global sin cubrir ramas críticas: hasta -10 pts.
- Tests que dependen de orden o tiempo real sin control: hasta -8 pts.
- Uso de `npm` en ruta JavaScript del bootcamp: hasta -5 pts.
- Nomenclatura técnica fuera de inglés: hasta -5 pts.

---

## Checklist de entrega

- [ ] Prácticas guiadas completadas.
- [ ] Proyecto implementado en `3-proyecto/starter/`.
- [ ] Evidencia de ejecución de cobertura (`pnpm test:coverage`).
- [ ] Mejora explícita de al menos 2 zonas de riesgo.
- [ ] Suite estable (sin flaky tests) en ejecuciones repetidas.
