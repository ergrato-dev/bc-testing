# Rúbrica de Evaluación - Semana 46

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Estrategia frente a plan de pruebas, inventario, riesgos del sistema, dónde corre cada prueba, criterios de salida, decisiones y README; defendidos en la presentación |
| Desempeño | 40% | Labs 01 y 02 sobre `ci-lab`, con el inventario medido y el PR de la estrategia mezclado |
| Producto | 30% | `testing-strategy.md` y `README.md` del repositorio del dominio, con todas las suites en verde |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Distingue la estrategia de testing del repositorio del plan de pruebas de una entrega.
- Explica por qué los totales de tests no se comparan entre lenguajes y qué se compara en su lugar.
- Junta los riesgos repetidos de varios reportes en un riesgo del sistema y lo lleva a capa, comando, sitio y criterio.
- Justifica por qué cada comprobación corre donde corre: hook, local, CI o a mano antes de una versión.
- Distingue un criterio de salida (número y comando) de una intención.
- En la presentación, defiende una decisión con sus alternativas y sus consecuencias.

### Niveles

- **Alto (27-30):** cada respuesta se apoya en un número o un archivo de su repositorio.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** confunde la estrategia con una lista de herramientas, o los criterios con intenciones.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: el inventario de `ci-lab` con los números de cada herramienta (listado, cobertura, tiempo), dónde corre cada suite y los cuatro huecos encontrados.
- Lab 02: `testing-strategy.md` y `README.md` de `ci-lab` sin `TODO`; D1 aplicada (`coverageThreshold` y `fail_under = 85`) con el experimento `-t lateFee` en rojo; D2 y D3 escritas; el PR mezclado y los *badges* en verde.

### Niveles

- **Alto (36-40):** los dos labs completos, con cada hueco del inventario resuelto por una decisión o escrito como riesgo abierto.
- **Medio (28-35):** los labs completos, con alguna sección copiada del ejemplo sin adaptar.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- `PROYECTO=... ./scripts/verificar-semana.sh 46` pasa las 8 comprobaciones.
- Todas las suites pasan con su umbral y el inventario de `testing-strategy.md` tiene los números de una ejecución real, con fecha.
- Los riesgos repetidos entre reportes aparecen una sola vez, con todas las suites afectadas.
- Al menos tres decisiones con alternativas reales y consecuencias que incluyen el coste.
- El README permite ejecutar cada suite desde la raíz y sus *badges* están en verde.

### Niveles

- **Alto (27-30):** todo lo anterior, con riesgos abiertos numerados y priorizados, cada uno con su próximo paso.
- **Medio (21-26):** pasan las comprobaciones, con una estrategia que repite los reportes en lugar de enlazarlos.
- **Bajo (<21):** no pasan las comprobaciones.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto final - La estrategia de testing de tu repositorio](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 46](README.md)
