# Rubrica de Evaluacion - Semana 14

## Evidencias y ponderacion

| Evidencia | Peso | Descripcion |
|---|---:|---|
| Conocimiento | 30% | Lectura critica de cobertura y calidad de pruebas |
| Desempeno | 40% | Practicas guiadas sobre mejoras de suite con Jest |
| Producto | 30% | Proyecto de hardening de suite con criterios de riesgo |

---

## 1) Conocimiento (30%)

### Criterios

1. Explica diferencias entre `lines`, `statements`, `functions` y `branches`.
2. Justifica por que 100% de cobertura puede seguir ocultando defectos.
3. Identifica sintomas de tests fragiles y tests redundantes.
4. Propone mejoras priorizadas por impacto de negocio.

### Niveles

- **Alto (27-30 pts)**: interpreta metricas con criterio tecnico y de producto.
- **Medio (21-26 pts)**: comprende bases con oportunidades de profundidad.
- **Bajo (0-20 pts)**: confunde metricas o interpreta cobertura de forma literal.

---

## 2) Desempeno (40%)

### Criterios

1. Completa ejercicios guiados descomentando por pasos.
2. Escribe o ajusta tests para cubrir ramas relevantes (errores, bordes, reglas).
3. Reduce fragilidad eliminando asserts ambiguos o snapshots de alto ruido.
4. Mantiene nomenclatura clara y patron AAA.

### Niveles

- **Alto (36-40 pts)**: suite estable, legible y orientada a riesgo.
- **Medio (28-35 pts)**: funcionamiento correcto con mejoras pendientes.
- **Bajo (0-27 pts)**: cobertura superficial o alta fragilidad.

---

## 3) Producto (30%)

### Criterios

1. Adapta la plantilla del proyecto a su dominio asignado.
2. Define objetivo de cobertura por modulo critico (no solo global).
3. Implementa tests para al menos 3 rutas de fallo relevantes.
4. Documenta decisiones de calidad tomadas (que se agrego y por que).

### Niveles

- **Alto (27-30 pts)**: evidencia mejora real de capacidad de deteccion.
- **Medio (21-26 pts)**: cumple minimos sin justificar completamente.
- **Bajo (0-20 pts)**: cambios minimos o sin relacion con riesgos reales.

---

## Penalizaciones

- Perseguir cobertura global sin cubrir ramas criticas: hasta -10 pts.
- Tests que dependen de orden o tiempo real sin control: hasta -8 pts.
- Uso de `npm` en ruta JavaScript del bootcamp: hasta -5 pts.
- Nomenclatura tecnica fuera de ingles: hasta -5 pts.

---

## Checklist de entrega

- [ ] Practicas guiadas completadas.
- [ ] Proyecto implementado en `3-proyecto/starter/`.
- [ ] Evidencia de ejecucion de cobertura (`yarn test --coverage`).
- [ ] Mejora explicita de al menos 2 zonas de riesgo.
- [ ] Suite estable (sin flaky tests) en ejecuciones repetidas.
