# Rúbrica de Evaluación - Semana 15

## Evidencias y ponderación

| Evidencia | Peso | Descripción |
|---|---:|---|
| Conocimiento | 30% | Estrategia integrada y entendimiento de quality gates |
| Desempeño | 40% | Prácticas guiadas de suite integrada y CI mínimo |
| Producto | 30% | Proyecto integrador (5 h de la semana): API + Supertest, coverage con umbral y pipeline |

---

## 1) Conocimiento (30%)

### Criterios

1. Explica como combinar tests unitarios, integración, snapshot y properties sin redundancia.
2. Interpreta el rol de un quality gate en CI.
3. Diferencia uso de SonarQube en repos públicos vs privados.
4. Define criterios de salida realistas para etapa JS.

### Niveles

- **Alto (27-30 pts)**: integra conceptos con criterio técnico y de negocio.
- **Medio (21-26 pts)**: comprensión correcta con vacíos menores.
- **Bajo (0-20 pts)**: mezcla enfoques sin justificación o confusión conceptual.

---

## 2) Desempeño (40%)

### Criterios

1. Completa ejercicios guiados descomentando por pasos.
2. Mantiene patrón AAA y nombres descriptivos.
3. Configura workflow mínimo de GitHub Actions para test + coverage.
4. Integra análisis SonarQube básico con configuración coherente.

### Niveles

- **Alto (36-40 pts)**: ejecución estable y decisiones bien justificadas.
- **Medio (28-35 pts)**: implementación funcional con mejoras pendientes.
- **Bajo (0-27 pts)**: configuración incompleta o frágil.

---

## 3) Producto (30%)

### Criterios

1. Adapta plantilla al dominio asignado.
2. Implementa al menos 8 tests con mezcla de enfoques, incluida integración HTTP con Supertest sobre la capa API.
3. Configura `coverageThreshold` y `collectCoverageFrom` y la suite cumple el umbral.
4. Incluye pipeline de calidad automatizada con evidencia de `pnpm test:coverage` ejecutado en CI y quality gate bloqueante.
5. Documenta criterios de salida y deuda técnica pendiente.

### Niveles

- **Alto (27-30 pts)**: cierre sólido de etapa JS con guardrails útiles.
- **Medio (21-26 pts)**: cumple mínimos pero sin profundidad.
- **Bajo (0-20 pts)**: no evidencia integración real de estrategias.

---

## Penalizaciones

- Pipeline sin evidencia de ejecución de tests: hasta -10 pts.
- Uso de SonarQube sin aclarar público/privado: hasta -6 pts.
- Tests redundantes o sin señal clara: hasta -8 pts.
- Uso de `npm` en ruta JavaScript del bootcamp: hasta -5 pts.

---

## Checklist de entrega

- [ ] Prácticas guiadas completadas.
- [ ] Proyecto implementado en `3-proyecto/starter/`.
- [ ] Evidencia de `pnpm test:coverage` en CI.
- [ ] Configuración SonarQube mínima documentada.
- [ ] Criterios de salida de etapa JS explicitados.
