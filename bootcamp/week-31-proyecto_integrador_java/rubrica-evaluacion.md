# Rúbrica de Evaluación - Semana 31

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Fases de la suite, cobertura de JaCoCo y lectura del reporte |
| Desempeño | 40% | Labs 01 y 02 completos y presentación de la suite |
| Producto | 30% | Suite Java completa del dominio con cobertura ≥85 % y reporte |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica qué decide si un test es `*Test` o `*IT` y lo justifica con una clase de su suite.
- Explica por qué los `*IT` no se ejecutan sin Failsafe y por qué el build falla en `verify` y no en `integration-test`.
- Explica cómo llega el agente de JaCoCo a las JVM de Surefire y Failsafe, y qué hace `@{argLine}`.
- Explica por qué cada medición empieza con `clean` y por qué el 100 % de una clase no garantiza que sea correcta.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo de su suite.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** no distingue las fases o no sabe de dónde sale la cifra de cobertura.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: declara Failsafe, ejecuta cada fase por separado y explica la diferencia de tiempos.
- Lab 02: encuentra la clase al 0 % con sus tests en verde, la arregla con `@{argLine}`, provoca el fallo del umbral y escribe el reporte.
- Presentación de 5 minutos: las dos fases, la cifra de cobertura y qué fase la sostiene, la clase más lenta y el riesgo principal.

### Niveles

- **Alto (36-40):** los dos labs completos y una presentación clara con datos reales.
- **Medio (28-35):** labs completos y una presentación sin datos o sin el riesgo principal.
- **Bajo (<28):** labs incompletos o sin presentación.

---

## 3) Producto (30%)

### Criterios

- Los ocho requisitos de `./scripts/verificar-semana.sh 31` en verde.
- Cada clase de test tiene el sufijo que corresponde a lo que arranca, renombrada con `git mv`.
- El umbral está en el `pom.xml` y nadie lo ha bajado del 85 %.
- El reporte nombra riesgos concretos del dominio, no generalidades.

### Niveles

- **Alto (27-30):** verificación en verde, fases limpias y reporte con riesgos concretos.
- **Medio (21-26):** verificación en verde con algún test en la fase equivocada o un reporte genérico.
- **Bajo (<21):** algún requisito verificable en rojo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Integrador - Suite Java completa del dominio](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 31](README.md)
