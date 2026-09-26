# Rúbrica de Evaluación - Semana 19

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Niveles de aislamiento HTTP, traducción de errores, contratos y testing asíncrono |
| Desempeño | 40% | Ejercicios guiados completados, con los bugs del starter corregidos |
| Producto | 30% | Suite del cliente HTTP del dominio, sin red y ejecutable |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Compara parchear una función, simular el transporte, usar un servidor local y llamar al API real.
- Explica por qué un 5xx con cuerpo HTML rompe un cliente que llama a `response.json()` sin revisar el código de estado.
- Explica qué detecta un modelo de `pydantic` y qué añade Pact.
- Distingue los modos `strict` y `auto` de `pytest-asyncio` y `call_count` frente a `await_count`.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo correcto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** confunde niveles de aislamiento o no distingue una corutina creada de una esperada.

---

## 2) Desempeño (40%)

### Criterios

- Completa los dos ejercicios descomentando cada PASO.
- Corrige los tres bugs que encuentran los tests: 503 con HTML, timeout sin capturar y `await` olvidado.
- Aplica las mutaciones propuestas en cada README y comprueba qué test las detecta.
- Mantiene nombres descriptivos y patrón AAA.

### Niveles

- **Alto (36-40):** ejercicios completos, bugs corregidos y mutaciones explicadas.
- **Medio (28-35):** ejercicios completos con correcciones parciales o sin analizar las mutaciones.
- **Bajo (<28):** ejercicios incompletos o tests que salen a la red.

---

## 3) Producto (30%)

### Criterios

- Al menos 10 tests efectivos del cliente del dominio.
- Cubre 2xx, 404, 422, 5xx con cuerpo no JSON, timeout y contrato roto.
- Verifica la request enviada: ruta, query params o cuerpo JSON, y header `Authorization`.
- Ningún test sale a la red; ejecución reproducible con `uv run pytest` y sin tests en `skipped`.

### Niveles

- **Alto (27-30):** suite completa, legible y sin acoplarse a detalles internos del cliente.
- **Medio (21-26):** cumple lo mínimo con ramas o verificaciones de request pendientes.
- **Bajo (<21):** suite incompleta o dependiente de la red.

---

## Condiciones de aprobación

1. Obtener mínimo 70% en cada tipo de evidencia.
2. Entregar proyecto ejecutable y coherente con el dominio asignado.
3. Mantener originalidad del trabajo y convenciones del bootcamp.
