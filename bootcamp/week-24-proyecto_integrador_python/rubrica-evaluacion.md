# Rúbrica de Evaluación - Semana 24

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Organización por capas, cobertura combinada y lectura del reporte |
| Desempeño | 40% | Labs 01 y 02 completos y presentación de la suite |
| Producto | 30% | Suite Python completa del dominio con cobertura ≥85 % y reporte |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica qué capa prueba cada riesgo del dominio y por qué no lo prueba otra.
- Explica cómo marca el *hook* cada test y qué detecta `--strict-markers`.
- Explica por qué hace falta `--append` y qué cifra saldría sin él.
- Explica por qué `source` cambia la cifra de cobertura.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo de su suite.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** no distingue las capas o no sabe de dónde sale la cifra de cobertura.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: ejecuta cada capa por separado y explica la diferencia de tiempos.
- Lab 02: mide pytest y Behave juntos, provoca el fallo del umbral y escribe el reporte.
- Presentación de 5 minutos: estructura por capas, cifra de cobertura, test más lento y riesgo principal.

### Niveles

- **Alto (36-40):** los dos labs completos y una presentación clara con datos reales.
- **Medio (28-35):** labs completos y una presentación sin datos o sin el riesgo principal.
- **Bajo (<28):** labs incompletos o sin presentación.

---

## 3) Producto (30%)

### Criterios

- Los cinco requisitos de `./scripts/verificar-semana.sh 24` en verde.
- Código del dominio sin módulos duplicados de semanas distintas.
- Cada test está en la capa que corresponde a sus dependencias.
- El reporte nombra riesgos concretos del dominio, no generalidades.

### Niveles

- **Alto (27-30):** verificación en verde, capas limpias y reporte con riesgos concretos.
- **Medio (21-26):** verificación en verde con módulos duplicados o un reporte genérico.
- **Bajo (<21):** algún requisito verificable en rojo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Integrador - Suite Python completa del dominio](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 24](README.md)
