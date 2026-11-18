# Rúbrica de Evaluación - Semana 36

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Fases de la suite, `verify.sh`, el umbral y la lectura de los tiempos y la cobertura |
| Desempeño | 40% | Labs 01 y 02 completos y presentación de la suite |
| Producto | 30% | Suite Go completa del dominio con `verify.sh`, cobertura ≥85 % y reporte |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica qué decide si un test lleva la tag `integration` y por qué `go test ./...` en verde no basta.
- Explica qué hace cada opción de `set -euo pipefail` y qué pasa con `| tee` sin `pipefail`.
- Explica de dónde sale el total de cobertura, qué significa `coverage: X% of statements in ./...` en cada paquete y por qué el umbral se compara con `awk`.
- Explica por qué el tiempo de un paquete no es la suma de sus tests (`TestMain`, la pausa de `-race`) y por qué los datos del reporte se toman con `-count=1`.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo de su suite.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** no distingue las fases o no sabe de dónde sale la cifra de cobertura.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: construye `verify.sh` por PASOS, ve fallar el umbral y reproduce el fallo escondido por `tee`.
- Lab 02: cuenta los tests de cada fase, encuentra a dónde va el tiempo de cada paquete, calcula la cobertura por paquete y escribe el reporte.
- Presentación de 5 minutos: las fases de `verify.sh`, la cifra de cobertura y qué fase la sostiene, a dónde va el tiempo y el riesgo principal.

### Niveles

- **Alto (36-40):** los dos labs completos y una presentación clara con datos reales.
- **Medio (28-35):** labs completos y una presentación sin datos o sin el riesgo principal.
- **Bajo (<28):** labs incompletos o sin presentación.

---

## 3) Producto (30%)

### Criterios

- Los cinco requisitos de `./scripts/verificar-semana.sh 36` en verde.
- Cada test de integración lleva la tag y ningún test sin tag arranca un contenedor.
- El umbral de `verify.sh` es 85 y nadie lo ha bajado.
- El reporte nombra riesgos concretos del dominio, comprobados o razonados sobre el código, no generalidades.

### Niveles

- **Alto (27-30):** verificación en verde, fases limpias y reporte con riesgos concretos.
- **Medio (21-26):** verificación en verde con algún test en la fase equivocada o un reporte genérico.
- **Bajo (<21):** algún requisito verificable en rojo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Integrador - Suite Go completa del dominio](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 36](README.md)
