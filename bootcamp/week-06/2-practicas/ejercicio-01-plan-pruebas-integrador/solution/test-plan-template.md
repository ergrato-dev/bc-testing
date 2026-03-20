# Test Plan Template - Solution Example

## 1. Alcance

- Modulo bajo prueba: ItemService (creacion y validacion de items)
- Incluye: reglas de validacion de nombre, cantidad y precio
- Excluye: persistencia real en base de datos y autenticacion

## 2. Riesgos

| Riesgo | Impacto | Probabilidad | Prioridad |
|---|---|---|---|
| Se permite precio negativo | Alto | Alta | Alta |
| Se permite nombre vacio | Alto | Alta | Alta |
| Error por valores limite no cubiertos | Medio | Media | Media |

## 3. Casos de Prueba

| Requirement ID | Test Case ID | Titulo | Prioridad | Resultado Esperado |
|---|---|---|---|---|
| REQ-001 | TC-001 | should create item when payload is valid | Alta | item creado |
| REQ-001 | TC-002 | should reject item when name is empty | Alta | ValidationError |
| REQ-002 | TC-003 | should reject item when quantity is negative | Alta | ValidationError |
| REQ-003 | TC-004 | should reject item when price is negative | Alta | ValidationError |
| REQ-004 | TC-005 | should allow quantity equal to zero | Media | item valido |
| REQ-005 | TC-006 | should trim item name before validation | Media | nombre normalizado |
| REQ-006 | TC-007 | should reject null payload | Alta | ValidationError |
| REQ-007 | TC-008 | should keep deterministic id format | Baja | id con prefijo ITEM- |

## 4. Criterios de Entrada

- Reglas de negocio aprobadas por el instructor.
- Datos de prueba ficticios definidos.
- Entorno local de ejecucion listo.

## 5. Criterios de Salida

- 100% de casos de prioridad alta ejecutados.
- Sin fallos bloqueantes abiertos.
- Evidencia documentada por lenguaje.

## 6. Evidencias

- Captura de ejecucion: consola con test pass/fail.
- Resumen de fallos: lista con causa raiz y estado.
- Acciones de mejora: backlog de 2-3 mejoras priorizadas.
