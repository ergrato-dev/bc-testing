# Rúbrica de Evaluación - Semana 11

## Evidencias y ponderación

| Evidencia | Peso | Descripción |
|---|---:|---|
| Conocimiento | 30% | Comprensión del ciclo TDD y criterios de refactor seguro |
| Desempeño | 40% | Ejecución de prácticas guiadas con micro-ciclos Red-Green-Refactor |
| Producto | 30% | Proyecto incremental TDD funcional y mantenible |

---

## 1) Conocimiento (30%)

### Criterios

1. Explica con claridad cada fase: **Red**, **Green**, **Refactor**.
2. Justifica por qué se implementa el mínimo código para pasar el test.
3. Diferencia refactor de cambio de comportamiento.
4. Identifica anti patrones de TDD en ejemplos reales.

### Niveles

- **Alto (27-30 pts)**: domina principios y toma decisiones técnicas coherentes.
- **Medio (21-26 pts)**: comprende el ciclo, con vacíos menores en aplicación.
- **Bajo (0-20 pts)**: confunde etapas o no evidencia uso real de TDD.

---

## 2) Desempeño (40%)

### Criterios

1. Completa ejercicios en formato guiado (descomentado progresivo).
2. Mantiene tests pequeños, descriptivos y ejecutables.
3. Realiza refactor sin romper casos ya cubiertos.
4. Aplica patrón AAA en cada test.

### Niveles

- **Alto (36-40 pts)**: flujo TDD consistente y sin saltos conceptuales.
- **Medio (28-35 pts)**: resuelve ejercicios con detalles por mejorar.
- **Bajo (0-27 pts)**: ejecución incompleta o sin evidencia de micro-ciclos.

---

## 3) Producto (30%)

### Criterios

1. Proyecto parte de tests primero y evoluciona incrementalmente.
2. Incluye casos felices, validaciones y errores de dominio.
3. Evidencia al menos una mejora por refactor sin cambio funcional.
4. Mínimo sugerido: 8 tests descriptivos y estables.

### Niveles

- **Alto (27-30 pts)**: suite clara, incremental y de alta mantenibilidad.
- **Medio (21-26 pts)**: cumple objetivos con oportunidades de limpieza.
- **Bajo (0-20 pts)**: suite frágil, incompleta o sin secuencia TDD visible.

---

## Penalizaciones

- Saltar fase Red (tests nacen ya en verde): hasta -10 pts.
- Tests genéricos sin intención de comportamiento: hasta -5 pts.
- Mezcla de nomenclatura técnica en español: hasta -5 pts.
- Uso de gestor no recomendado en ruta JavaScript (`npm`): hasta -5 pts.

---

## Checklist de entrega

- [ ] Ejercicios guiados completados.
- [ ] Proyecto desarrollado con enfoque TDD.
- [ ] Tests ejecutan en local sin errores.
- [ ] Evidencia de refactor sin cambio funcional.
- [ ] Nombres de tests descriptivos con patrón recomendado.
