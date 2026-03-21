# Rubrica de Evaluacion - Semana 18

## Evidencias y ponderacion

| Tipo de evidencia | Ponderacion | Descripcion |
|---|---:|---|
| Conocimiento | 30% | Comprension de mock/stub/spy, patch target y buenas practicas |
| Desempeno | 40% | Implementacion de ejercicios guiados con aislamiento correcto |
| Producto | 30% | Suite del proyecto con estrategia de mocking clara y ejecutable |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Diferencia con precision mock, stub y spy.
- Identifica donde aplicar `patch` segun import real.
- Explica riesgos de sobre-mockeo y tests fragiles.
- Interpreta `assert_called_once_with` y variantes.

### Niveles

- **Alto (27-30):** distingue conceptos con ejemplos correctos y justificacion.
- **Medio (21-26):** comprende conceptos, pero con una o dos confusiones menores.
- **Bajo (<21):** mezcla conceptos y no identifica target correcto.

---

## 2) Desempeno (40%)

### Criterios

- Completa ejercicios con codigo comentado descomentado sin errores.
- Usa `return_value` y `side_effect` para modelar escenarios.
- Verifica interacciones relevantes (no cada detalle interno).
- Mantiene nombres descriptivos y patron AAA.

### Niveles

- **Alto (36-40):** ejercicios completos, claros y robustos.
- **Medio (28-35):** funcional, con pequenos problemas de legibilidad o enfoque.
- **Bajo (<28):** fallos frecuentes en patching o aserciones de interaccion.

---

## 3) Producto (30%)

### Criterios

- Suite del dominio con al menos 8 casos efectivos.
- Minimo 3 casos de error con dobles adecuados.
- Estrategia explicita de que se mockea y que no.
- Ejecucion reproducible con `pytest`.

### Niveles

- **Alto (27-30):** suite mantenible, bien aislada y con buena cobertura de decisiones.
- **Medio (21-26):** cumple lo minimo con oportunidades de mejora.
- **Bajo (<21):** suite incompleta o excesivamente acoplada a implementacion.

---

## Condiciones de aprobacion

1. Obtener minimo 70% en cada tipo de evidencia.
2. Entregar proyecto ejecutable y coherente con el dominio asignado.
3. Mantener originalidad del trabajo y convenciones del bootcamp.
