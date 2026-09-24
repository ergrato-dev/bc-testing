# Rúbrica de Evaluación - Semana 18

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Comprensión de mock/stub/spy, patch target y buenas prácticas |
| Desempeño | 40% | Implementación de ejercicios guiados con aislamiento correcto |
| Producto | 30% | Suite del proyecto con estrategia de mocking clara y ejecutable |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Diferencia con precisión mock, stub y spy.
- Identifica dónde aplicar `patch` según el import real y reconoce un target incorrecto.
- Explica riesgos de sobre-mockeo y tests frágiles.
- Interpreta `assert_called_once_with`, `assert_not_called`, `call_args_list` y el beneficio de `autospec`.

### Niveles

- **Alto (27-30):** distingue conceptos con ejemplos correctos y justificación.
- **Medio (21-26):** comprende conceptos, pero con una o dos confusiones menores.
- **Bajo (<21):** mezcla conceptos y no identifica target correcto.

---

## 2) Desempeño (40%)

### Criterios

- Completa los ejercicios descomentando cada PASO sin errores.
- Usa `return_value` y `side_effect` para modelar escenarios.
- Verifica interacciones relevantes (no cada detalle interno).
- Mantiene nombres descriptivos y patrón AAA.

### Niveles

- **Alto (36-40):** ejercicios completos, claros y robustos.
- **Medio (28-35):** funcional, con pequeños problemas de legibilidad o enfoque.
- **Bajo (<28):** fallos frecuentes en patching o aserciones de interacción.

---

## 3) Producto (30%)

### Criterios

- Suite del dominio con al menos 8 casos efectivos.
- Mínimo 3 casos de error con dobles adecuados.
- Estrategia explícita de qué se mockea, con qué doble y qué no.
- Ejecución reproducible con `uv run pytest`.

### Niveles

- **Alto (27-30):** suite mantenible, bien aislada y con buena cobertura de decisiones.
- **Medio (21-26):** cumple lo mínimo con oportunidades de mejora.
- **Bajo (<21):** suite incompleta o excesivamente acoplada a implementación.

---

## Condiciones de aprobación

1. Obtener mínimo 70% en cada tipo de evidencia.
2. Entregar proyecto ejecutable y coherente con el dominio asignado.
3. Mantener originalidad del trabajo y convenciones del bootcamp.
