# Rubrica de Evaluacion - Semana 15

## Evidencias y ponderacion

| Evidencia | Peso | Descripcion |
|---|---:|---|
| Conocimiento | 30% | Estrategia integrada y entendimiento de quality gates |
| Desempeno | 40% | Practicas guiadas de suite integrada y CI minimo |
| Producto | 30% | Cierre de etapa JS con pipeline y criterios de calidad |

---

## 1) Conocimiento (30%)

### Criterios

1. Explica como combinar tests unitarios, integracion, snapshot y properties sin redundancia.
2. Interpreta el rol de un quality gate en CI.
3. Diferencia uso de SonarQube en repos publicos vs privados.
4. Define criterios de salida realistas para etapa JS.

### Niveles

- **Alto (27-30 pts)**: integra conceptos con criterio tecnico y de negocio.
- **Medio (21-26 pts)**: comprension correcta con vacios menores.
- **Bajo (0-20 pts)**: mezcla enfoques sin justificacion o confusion conceptual.

---

## 2) Desempeno (40%)

### Criterios

1. Completa ejercicios guiados descomentando por pasos.
2. Mantiene patron AAA y nombres descriptivos.
3. Configura workflow minimo de GitHub Actions para test + coverage.
4. Integra analisis SonarQube basico con configuracion coherente.

### Niveles

- **Alto (36-40 pts)**: ejecucion estable y decisiones bien justificadas.
- **Medio (28-35 pts)**: implementacion funcional con mejoras pendientes.
- **Bajo (0-27 pts)**: configuracion incompleta o fragil.

---

## 3) Producto (30%)

### Criterios

1. Adapta plantilla al dominio asignado.
2. Implementa al menos 8 tests con mezcla de enfoques.
3. Incluye pipeline basico de calidad automatizada.
4. Documenta criterios de salida y deuda tecnica pendiente.

### Niveles

- **Alto (27-30 pts)**: cierre solido de etapa JS con guardrails utiles.
- **Medio (21-26 pts)**: cumple minimos pero sin profundidad.
- **Bajo (0-20 pts)**: no evidencia integracion real de estrategias.

---

## Penalizaciones

- Pipeline sin evidencia de ejecucion de tests: hasta -10 pts.
- Uso de SonarQube sin aclarar publico/privado: hasta -6 pts.
- Tests redundantes o sin senal clara: hasta -8 pts.
- Uso de `npm` en ruta JavaScript del bootcamp: hasta -5 pts.

---

## Checklist de entrega

- [ ] Practicas guiadas completadas.
- [ ] Proyecto implementado en `3-proyecto/starter/`.
- [ ] Evidencia de `yarn test --coverage` en CI.
- [ ] Configuracion SonarQube minima documentada.
- [ ] Criterios de salida de etapa JS explicitados.
