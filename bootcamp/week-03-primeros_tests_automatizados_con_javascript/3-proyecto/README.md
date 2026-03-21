# Proyecto Semana 03 — Suite Inicial de Tests Unitarios (JavaScript)

> **Entregable obligatorio** | Etapa 0 · Semana 03

---

## 🎯 Objetivo

Construir una suite inicial de tests unitarios en Jest para funciones puras del dominio asignado, aplicando:

- Estructura de Jest (`describe`, `it`, `expect`)
- Patrón AAA
- Nomenclatura descriptiva de tests

---

## Contexto

Cada aprendiz trabaja su dominio asignado, pero en esta semana el alcance es acotado: funciones pequeñas y determinísticas sin dependencias externas.

**No usar**:

- Base de datos real
- APIs externas
- Lógica asíncrona compleja

**Sí usar**:

- Funciones puras
- Inputs controlados
- Assertions claras

---

## Reglas del proyecto

1. Crear al menos 3 funciones de negocio simples del dominio
2. Escribir mínimo 8 tests unitarios
3. Cubrir al menos:
   - 3 happy path
   - 3 casos inválidos o error
   - 2 edge cases
4. Nombrar tests con formato: `should [resultado] when [condicion]`
5. Usar `pnpm` para ejecución (`pnpm test`)

---

## Estructura sugerida

```text
3-proyecto/
├── README.md
└── starter/
    └── item.service.test.js
```

> `solution/` del proyecto se mantiene fuera del repositorio público.

---

## Guía de implementación (2 horas)

- **20 min**: definir funciones del dominio a testear
- **25 min**: escribir tests happy path
- **35 min**: escribir tests de validación y errores
- **25 min**: completar edge cases
- **15 min**: limpieza de nombres y ejecución final

---

## Ejemplo neutral (no usar en entrega)

Dominio ejemplo: **Museo Virtual**

Funciones:

- `calculateTicketPrice(age, isStudent)`
- `isVisitSlotAvailable(capacity, booked)`
- `formatVisitorName(name)`

La entrega real debe usar el dominio asignado a cada aprendiz.
