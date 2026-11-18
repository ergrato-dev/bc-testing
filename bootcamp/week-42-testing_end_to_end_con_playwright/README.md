# Semana 42 - Testing End-to-End con Playwright

> **Etapa 6 - Testing Avanzado Transversal** | Semana 42 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Explicar qué encuentra un test E2E que no encuentran los de API ni los unitarios, y decidir qué flujos merecen uno.
2. Configurar Playwright con pnpm para que arranque la aplicación y ejecute la suite en Chromium, Firefox y WebKit.
3. Encontrar elementos con locators por rol, etiqueta y test id, actuar sobre ellos y comprobar lo que se ve con aserciones que reintentan.
4. Organizar la suite con page objects y fixtures que preparan los datos de cada test por la API.
5. Guardar capturas, videos y trazas de los fallos, y documentar el flujo principal en un informe HTML.

---

## Requisitos previos

- Semana 1: la pirámide de testing y el nivel E2E.
- Semanas 12, 34 y 39: tests de API en JavaScript, Go y Rust, que esta semana se comparan con los E2E.
- Semana 16: los fixtures de pytest, el mismo modelo que los de Playwright.
- Node 22 o superior y pnpm. Docker, para ejecutar WebKit en Linux sin sus bibliotecas del sistema.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | E2E y Playwright; locators, acciones y aserciones; POM y fixtures; evidencias, reportes y navegadores | 2.5 h |
| Prácticas | 4 labs: primer test, locators y aserciones, POM y fixtures, evidencias y navegadores | 4 h |
| Proyecto | Suite E2E del dominio con Playwright | 3 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [E2E y Playwright](./1-teoria/01-e2e-y-playwright.md)
2. [Locators, acciones y aserciones](./1-teoria/02-locators-acciones-y-aserciones.md)
3. [Page Object Model y fixtures](./1-teoria/03-page-object-model-y-fixtures.md)
4. [Evidencias, reportes y navegadores](./1-teoria/04-evidencias-reportes-y-navegadores.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Primer test E2E](./2-practicas/lab-01-primer-test-e2e/README.md)
- [Lab 02 - Locators, acciones y aserciones](./2-practicas/lab-02-locators-acciones-y-aserciones/README.md)
- [Lab 03 - Page Object Model y fixtures](./2-practicas/lab-03-page-object-model-y-fixtures/README.md)
- [Lab 04 - Evidencias y navegadores](./2-practicas/lab-04-evidencias-y-navegadores/README.md)

### Proyecto

- [Suite E2E del dominio con Playwright](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): rama `week-42` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 42
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md). La primera ejecuta la suite en Chromium y Firefox: necesitas los dos navegadores instalados con `pnpm exec playwright install`.

---

## Estructura de Carpetas

```text
week-42-testing_end_to_end_con_playwright/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-lo-que-recorre-un-test-e2e.svg
|   |-- 02-auto-wait.svg
|   `-- 03-page-object-model.svg
|-- 1-teoria/
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-primer-test-e2e/
|   |-- lab-02-locators-acciones-y-aserciones/
|   |-- lab-03-page-object-model-y-fixtures/
|   `-- lab-04-evidencias-y-navegadores/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/e2e/
|-- 4-recursos/
|   |-- ebooks-free/README.md
|   |-- videografia/README.md
|   `-- webgrafia/README.md
`-- 5-glosario/
    `-- README.md
```

---

## Versiones de la semana

Node 22 o superior y pnpm 12.6.0; `@playwright/test` 1.63.0 en `devDependencies`, con sus navegadores descargados por `pnpm exec playwright install`; la imagen Docker `mcr.microsoft.com/playwright:v1.63.0-noble` para WebKit. Están registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Un test E2E es el único que ve lo que ve el usuario, y también el más caro y el más frágil. En los labs, todos los fallos que aparecen sin provocarlos vienen del mismo sitio: el servidor es compartido. Un libro prestado en una ejecución sigue prestado en la siguiente, y el libro que presta Chromium ya está prestado cuando llega Firefox. Antes de sospechar de Playwright, sospecha de los datos.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 41 - Proyecto Integrador: Etapa 5 (Rust)](../week-41-proyecto_integrador_rust/README.md) | [Semana 43 - Testing de Performance con k6 y Locust](../week-43-testing_de_performance_con_k6_y_locust/README.md) |
