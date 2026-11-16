# Rúbrica de Evaluación - Semana 27

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Ciclo TDD en Java, diseño emergente, refactor seguro, outside-in, `Clock` y ArchUnit |
| Desempeño | 40% | Labs 01 a 04 completos, con cada Red comprobado antes del Green y cada experimento explicado |
| Producto | 30% | Servicio del dominio construido con TDD, con su TDD-LOG, un commit por ciclo y tres fitness functions |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Diferencia un Red de compilación de un Red de aserción, y `Failures` de `Errors`.
- Explica qué es un test de confirmación y cómo se comprueba.
- Explica qué test decidió cada tipo del Game of Life (`enum`, `record`, clase inmutable).
- Diferencia inside-out de outside-in y decide qué se sustituye con un mock.
- Explica por qué la fecha llega por un `Clock` y qué protege cada regla de ArchUnit.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo de los labs o del proyecto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** confunde refactor con añadir comportamiento, o mock con fake.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: los nueve pasos con su Red, el experimento del test de confirmación y la guarda final.
- Lab 02: las tres clases creadas desde los tests, el refactor del paso 12 y los dos errores del paso 13.
- Lab 03: las interfaces creadas desde el test, el error de strict stubbing del día equivocado y los textos sin mocks.
- Lab 04: las cuatro reglas y su violación provocada y leída.

### Niveles

- **Alto (36-40):** los cuatro labs completos y cada experimento explicado con sus palabras.
- **Medio (28-35):** labs completos con algún experimento sin explicar.
- **Bajo (<28):** labs incompletos, o Greens escritos sin haber visto antes el Red.

---

## 3) Producto (30%)

### Criterios

- Las siete comprobaciones de `verificar-semana.sh 27` pasan.
- El historial de la rama `week-27` tiene un commit por ciclo, y ninguno deja la suite en rojo.
- `TDD-LOG.md` coincide con el historial: cada fila tiene su Red real y su commit.
- Los cálculos se prueban sin mocks; el servicio, con mocks de sus dependencias y `Clock.fixed`.
- Los cinco cambios del paso 4 hacen fallar la suite.
- El `README.md` del repositorio explica qué partes son inside-out, cuáles outside-in y qué protege cada regla.

### Niveles

- **Alto (27-30):** cumple todos los criterios.
- **Medio (21-26):** pasa las comprobaciones, pero el historial agrupa varios ciclos en un commit o falta el párrafo.
- **Bajo (<21):** alguna comprobación falla, o el TDD-LOG no corresponde al historial.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Semanal - Un servicio del dominio construido con TDD](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 27](README.md)
