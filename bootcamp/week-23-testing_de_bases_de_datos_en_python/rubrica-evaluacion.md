# Rúbrica de Evaluación - Semana 23

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Estrategias de testing de BD, rollback por test, factories, Testcontainers y migraciones |
| Desempeño | 40% | Labs 01 a 04 completos, con cada fallo provocado y explicado |
| Producto | 30% | Capa de datos del dominio con su suite en SQLite y PostgreSQL |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica qué detecta un test de repositorio que un mock de la sesión no ve.
- Reparte engine, esquema y transacción entre scopes `session` y `function`, y justifica el reparto.
- Explica qué hace `join_transaction_mode="create_savepoint"` y qué pasa sin él cuando el código hace `rollback()`.
- Distingue cuándo usar SQLite y cuándo PostgreSQL, con un ejemplo de diferencia real.
- Explica qué riesgos cubren `upgrade head`, `check` y `downgrade base`.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo correcto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** trata SQLite y PostgreSQL como equivalentes o no explica el aislamiento.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: los siete tests pasan y explica el fallo al quitar `ORDER BY`.
- Lab 02: provoca el fallo sin aislamiento, aplica el savepoint, explica el ajuste de `sqlite3` y el experimento sin `create_savepoint`.
- Lab 03: factories con `Sequence`, `SubFactory` y semilla fija.
- Lab 04: la suite en los dos motores, el `VARCHAR` saltado con motivo, la migración generada y el experimento de `check`.

### Niveles

- **Alto (36-40):** los cuatro labs completos y cada fallo explicado.
- **Medio (28-35):** labs completos sin explicar alguno de los fallos.
- **Bajo (<28):** labs incompletos o sin Docker funcionando.

---

## 3) Producto (30%)

### Criterios

- Los seis requisitos de `./scripts/verificar-semana.sh 23` en verde.
- Modelos del dominio con código único, estado con `CheckConstraint` y una segunda entidad relacionada.
- Tests que indican solo los datos que importan (factories) y prueban fronteras: duplicados, estados no válidos, longitud.
- Párrafo en el README: el error que solo vio PostgreSQL y la restricción que protege la base de datos.

### Niveles

- **Alto (27-30):** verificación en verde, tests legibles y párrafo con un caso concreto del dominio.
- **Medio (21-26):** verificación en verde con tests que preparan datos a mano o sin el párrafo.
- **Bajo (<21):** algún requisito verificable en rojo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Semanal - Capa de datos del dominio con tests de repositorio](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 23](README.md)
