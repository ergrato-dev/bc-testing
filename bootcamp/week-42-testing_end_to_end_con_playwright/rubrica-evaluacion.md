# Rúbrica de Evaluación - Semana 42

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Qué recorre un test E2E y cuándo merece la pena, locators y modo estricto, auto-wait y aserciones web-first, POM y fixtures, aislamiento de datos, evidencias y navegadores |
| Desempeño | 40% | Labs 01 a 04 completos, con cada experimento provocado y explicado |
| Producto | 30% | La suite E2E del dominio en `e2e/`: al menos 10 tests con page objects y fixtures, evidencias en el informe HTML, Chromium y Firefox |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica qué encuentra un test E2E que no encuentra un test de API, qué cuesta y qué flujos merecen uno.
- Elige locators por rol y etiqueta, explica el modo estricto y lo resuelve encadenando.
- Distingue `await expect(locator)...` de `expect(await ...)` y explica por qué no hacen falta esperas fijas.
- Explica qué va en un page object y qué en el test, y qué hacen `test.extend` y `use`.
- Explica por qué el servidor compartido obliga a crear los datos de cada test y a no cambiar los iniciales.
- Explica qué guardan `screenshot`, `video` y `trace`, para qué sirve el informe HTML y qué significa un test flaky.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo correcto de los labs.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** usa esperas fijas o no distingue una aserción que reintenta de una que no.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: los dos tests, la aserción que reintenta 5 s y `reuseExistingServer` con y sin `CI`.
- Lab 02: los cuatro PASOS, y los experimentos de la lectura sin espera, el modo estricto y los datos compartidos.
- Lab 03: los tres page objects, los fixtures con `newBook`, y el cambio de interfaz arreglado en el page object.
- Lab 04: el fallo con captura, video, traza e informe; el test de evidencias; Firefox; WebKit (local o en Docker); y el experimento de dos navegadores con un servidor.

### Niveles

- **Alto (36-40):** los cuatro labs completos, con cada experimento ejecutado y explicado.
- **Medio (28-35):** los labs completos, con algún experimento sin explicar.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- `PROYECTO=... ./scripts/verificar-semana.sh 42` pasa las 8 comprobaciones.
- `app/domain.mjs` describe el dominio asignado con sus reglas: al menos una acción bloqueada por un estado o un campo.
- Los tests cubren el flujo principal, la validación visible en la interfaz y las acciones permitidas y bloqueadas.
- Los datos que un test cambia se crean por la API con clave única; los iniciales solo se leen.
- La suite pasa tres veces seguidas en los dos navegadores, sin tests inestables.
- El informe HTML muestra el flujo principal con sus pasos y capturas.

### Niveles

- **Alto (27-30):** todo lo anterior, con tests que se leen como el flujo del dominio.
- **Medio (21-26):** pasan las comprobaciones, con tests poco representativos del dominio o algún dato compartido.
- **Bajo (<21):** no pasan las comprobaciones.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto - Suite E2E del dominio con Playwright](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 42](README.md)
