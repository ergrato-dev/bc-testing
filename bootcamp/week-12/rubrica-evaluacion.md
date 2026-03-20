# Rubrica de Evaluacion - Semana 12

## Evidencias y ponderacion

| Evidencia | Peso | Descripcion |
|---|---:|---|
| Conocimiento | 30% | Comprension de HTTP, contratos JSON y estrategia de pruebas con Supertest |
| Desempeno | 40% | Ejecucion correcta de practicas guiadas de endpoints y errores |
| Producto | 30% | API del dominio con suite REST funcional y mantenible |

---

## 1) Conocimiento (30%)

### Criterios

1. Explica diferencia entre test unitario y test de integracion de API.
2. Define criterios de contrato: status, body, headers y estructura.
3. Selecciona codigos HTTP correctos por escenario.
4. Identifica riesgos de flakiness en pruebas de API.

### Niveles

- **Alto (27-30 pts)**: dominio conceptual claro y aplicacion consistente.
- **Medio (21-26 pts)**: buena base con detalles por afinar.
- **Bajo (0-20 pts)**: conceptos incompletos o aplicacion incorrecta.

---

## 2) Desempeno (40%)

### Criterios

1. Completa ejercicios guiados en formato descomentado.
2. Valida status y payload con aserciones claras.
3. Cubre casos felices y errores esperados.
4. Mantiene tests repetibles sin dependencias externas reales.

### Niveles

- **Alto (36-40 pts)**: ejecucion precisa y estable.
- **Medio (28-35 pts)**: cumple objetivos con pequenos ajustes necesarios.
- **Bajo (0-27 pts)**: pruebas incompletas o poco confiables.

---

## 3) Producto (30%)

### Criterios

1. API adaptada al dominio asignado desde plantilla starter.
2. Suite cubre CRUD basico y validaciones clave.
3. Manejo de errores consistente con contrato definido.
4. Minimo sugerido: 8 tests de endpoint ejecutables.

### Niveles

- **Alto (27-30 pts)**: API bien probada y contrato consistente.
- **Medio (21-26 pts)**: funcional con oportunidades de mejora.
- **Bajo (0-20 pts)**: cobertura insuficiente o inconsistencias de contrato.

---

## Penalizaciones

- Solo validar `status` sin validar cuerpo/contrato: hasta -8 pts.
- Uso de gestor no recomendado en JavaScript (`npm`): hasta -5 pts.
- Tests con datos acoplados o poco descriptivos: hasta -5 pts.
- Nomenclatura tecnica fuera de ingles: hasta -5 pts.

---

## Checklist de entrega

- [ ] Practicas guiadas completadas.
- [ ] Proyecto en `3-proyecto/starter/` implementado.
- [ ] Suite ejecuta sin errores en local.
- [ ] Contratos HTTP validados en casos exitosos y de error.
- [ ] Nombres de tests describen comportamiento esperado.
