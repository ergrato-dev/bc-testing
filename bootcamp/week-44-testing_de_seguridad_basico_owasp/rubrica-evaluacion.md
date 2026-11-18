# Rúbrica de Evaluación - Semana 44

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | OWASP Top 10 2025 desde el tester, casos de abuso, inyección SQL y XSS, 401/403/404, IDOR y enumeración, cabeceras de seguridad, DAST pasivo y activo, auditoría de dependencias y alcanzabilidad |
| Desempeño | 40% | Labs 01 a 04 completos, con cada test visto en rojo antes de corregir y cada experimento explicado |
| Producto | 30% | Tests `TestSecurity...` en la API Go del dominio, `security/` con ZAP baseline, auditorías e informe |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Sitúa un fallo en su categoría del OWASP Top 10 2025 y dice si se prueba con un test, con un escáner o con un auditor.
- Explica por qué una consulta con parámetros no es inyectable, y qué no puede ser parámetro.
- Distingue XSS reflejado, almacenado y DOM, y explica el escape según el contexto.
- Elige entre 401, 403 y 404, y explica por qué un recurso ajeno responde igual que uno inexistente.
- Explica qué ve y qué no ve ZAP baseline, el escaneo activo y un auditor de dependencias.
- Explica la diferencia entre una vulnerabilidad alcanzable y una que solo está en el módulo (`govulncheck`).

### Niveles

- **Alto (27-30):** explica cada concepto con una salida de los labs.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** confunde autenticación con autorización, o cree que un escaneo sin avisos demuestra que la API es segura.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: las tres cargas en rojo y en verde, el error que deja de enseñar el SQL, el XSS escapado con `html/template` y el experimento con `text/template`.
- Lab 02: la matriz de acceso completa, el IDOR con la comparación de cuerpos, el borrado sin token y el experimento de la fila que falta.
- Lab 03: el *middleware* en `main` (sigue en rojo) y en `newHandler`, ZAP baseline antes (6 avisos) y después (0 con la regla ignorada), y el escaneo activo contra la API original.
- Lab 04: los cinco auditores en rojo y en verde, la traza alcanzable de `govulncheck` y la corrección de la biblioteca estándar con `toolchain`.

### Niveles

- **Alto (36-40):** los cuatro labs completos, con cada experimento ejecutado y explicado.
- **Medio (28-35):** los labs completos, con algún experimento sin explicar.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- `PROYECTO=... ./scripts/verificar-semana.sh 44` pasa las 7 comprobaciones.
- Cada test de seguridad se vio fallar antes de corregir, o se rompió el código a propósito para ver que detecta el fallo (la inyección en `pgstore`).
- La institución (o el dueño) sale del token, y los tests antiguos se adaptaron sin perder lo que probaban.
- Las reglas ignoradas de ZAP y los avisos de auditoría no corregidos tienen motivo.
- El informe explica cada hallazgo con la petición que lo demuestra, y nombra lo que las pruebas no cubren.

### Niveles

- **Alto (27-30):** todo lo anterior, con al menos un hallazgo propio del dominio corregido y vigilado por un test.
- **Medio (21-26):** pasan las comprobaciones, con un informe que lista resultados sin explicarlos.
- **Bajo (<21):** no pasan las comprobaciones.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto - Seguridad de la API del dominio](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 44](README.md)
