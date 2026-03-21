# Rubrica de Evaluacion - Semana 11

## Evidencias y ponderacion

| Evidencia | Peso | Descripcion |
|---|---:|---|
| Conocimiento | 30% | Comprension del ciclo TDD y criterios de refactor seguro |
| Desempeno | 40% | Ejecucion de practicas guiadas con micro-ciclos Red-Green-Refactor |
| Producto | 30% | Proyecto incremental TDD funcional y mantenible |

---

## 1) Conocimiento (30%)

### Criterios

1. Explica con claridad cada fase: **Red**, **Green**, **Refactor**.
2. Justifica por que se implementa el minimo codigo para pasar el test.
3. Diferencia refactor de cambio de comportamiento.
4. Identifica anti patrones de TDD en ejemplos reales.

### Niveles

- **Alto (27-30 pts)**: domina principios y toma decisiones tecnicas coherentes.
- **Medio (21-26 pts)**: comprende el ciclo, con vacios menores en aplicacion.
- **Bajo (0-20 pts)**: confunde etapas o no evidencia uso real de TDD.

---

## 2) Desempeno (40%)

### Criterios

1. Completa ejercicios en formato guiado (descomentado progresivo).
2. Mantiene tests pequenos, descriptivos y ejecutables.
3. Realiza refactor sin romper casos ya cubiertos.
4. Aplica patron AAA en cada test.

### Niveles

- **Alto (36-40 pts)**: flujo TDD consistente y sin saltos conceptuales.
- **Medio (28-35 pts)**: resuelve ejercicios con detalles por mejorar.
- **Bajo (0-27 pts)**: ejecucion incompleta o sin evidencia de micro-ciclos.

---

## 3) Producto (30%)

### Criterios

1. Proyecto parte de tests primero y evoluciona incrementalmente.
2. Incluye casos felices, validaciones y errores de dominio.
3. Evidencia al menos una mejora por refactor sin cambio funcional.
4. Minimo sugerido: 8 tests descriptivos y estables.

### Niveles

- **Alto (27-30 pts)**: suite clara, incremental y de alta mantenibilidad.
- **Medio (21-26 pts)**: cumple objetivos con oportunidades de limpieza.
- **Bajo (0-20 pts)**: suite fragil, incompleta o sin secuencia TDD visible.

---

## Penalizaciones

- Saltar fase Red (tests nacen ya en verde): hasta -10 pts.
- Tests genericos sin intencion de comportamiento: hasta -5 pts.
- Mezcla de nomenclatura tecnica en espanol: hasta -5 pts.
- Uso de gestor no recomendado en ruta JavaScript (`npm`): hasta -5 pts.

---

## Checklist de entrega

- [ ] Ejercicios guiados completados.
- [ ] Proyecto desarrollado con enfoque TDD.
- [ ] Tests ejecutan en local sin errores.
- [ ] Evidencia de refactor sin cambio funcional.
- [ ] Nombres de tests descriptivos con patron recomendado.
