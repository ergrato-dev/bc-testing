# Semana 45 - CI/CD y Testing Automatizado

> **Etapa 6 - Testing Avanzado Transversal** | Semana 45 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Explicar CI y CD, y escribir y leer un workflow de GitHub Actions: eventos, jobs, steps, runners y permisos.
2. Preparar cada lenguaje del curso en la CI con su versión y su caché, y arrancar PostgreSQL como servicio, con `docker compose` o con Testcontainers.
3. Usar matrices, artifacts, el resumen del job, `paths`, `concurrency` y `timeout-minutes`, y razonar sobre el paralelismo.
4. Hacer que la cobertura por debajo del umbral ponga el pipeline en rojo, con el umbral en el proyecto.
5. Evaluar el código con una quality gate de SonarQube y parar los errores antes del commit con Husky y pre-commit.

---

## Requisitos previos

- Las semanas integradoras 24, 31, 36 y 41, y la 42: cada suite del repositorio con su comando de verificación y su umbral.
- Una cuenta de GitHub y la CLI `gh` con sesión iniciada (`gh auth login`).
- pnpm, uv, Go, Rust, Java con Maven y Docker. Las imágenes de SonarQube ocupan unos 3 GB: descárgalas antes del lab 04.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | CI/CD y anatomía de un workflow; cada lenguaje, cachés y servicios; matriz, artifacts, paralelismo y umbrales; quality gates y hooks | 2.5 h |
| Prácticas | 3 labs con un workflow por lenguaje en un repositorio de prueba y 1 de hooks y SonarQube | 4 h |
| Proyecto | Cinco workflows en el repositorio del dominio, pre-commit y `ci.md` | 3 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [CI/CD y anatomía de un workflow](./1-teoria/01-ci-cd-y-anatomia-de-un-workflow.md)
2. [Preparar cada lenguaje, cachés y servicios](./1-teoria/02-preparar-cada-lenguaje-caches-y-servicios.md)
3. [Matrix, artifacts, paralelismo y umbrales](./1-teoria/03-matrix-artifacts-paralelismo-y-umbrales.md)
4. [Quality gates y hooks de pre-commit](./1-teoria/04-quality-gates-y-hooks-de-pre-commit.md)

### Prácticas

- [Índice de labs y repositorio ci-lab](./2-practicas/README.md)
- [Lab 01 - Primer workflow](./2-practicas/lab-01-primer-workflow/README.md)
- [Lab 02 - Matrix, caché y artifacts](./2-practicas/lab-02-matrix-cache-y-artifacts/README.md)
- [Lab 03 - PostgreSQL como servicio](./2-practicas/lab-03-postgresql-como-servicio/README.md)
- [Lab 04 - Hooks y quality gate](./2-practicas/lab-04-hooks-y-quality-gate/README.md)

### Proyecto

- [CI de las cinco suites](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): rama `week-45` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`, con los cinco checks en verde. Antes de abrirlo:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 45
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md). Necesitan Docker (actionlint) y uv (pre-commit). No pueden ver tus runs de GitHub: los enlaces de `ci.md` son la evidencia.

---

## Estructura de Carpetas

```text
week-45-ci_cd_y_testing_automatizado/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-anatomia-de-un-workflow.svg
|   `-- 02-del-commit-al-merge.svg
|-- 1-teoria/
|-- 2-practicas/
|   |-- README.md
|   |-- ci-lab/
|   |-- lab-01-primer-workflow/
|   |-- lab-02-matrix-cache-y-artifacts/
|   |-- lab-03-postgresql-como-servicio/
|   `-- lab-04-hooks-y-quality-gate/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/
|       |-- .github/workflows/
|       |-- .pre-commit-config.yaml
|       `-- ci.md
|-- 4-recursos/
|   |-- ebooks-free/README.md
|   |-- videografia/README.md
|   `-- webgrafia/README.md
`-- 5-glosario/
    `-- README.md
```

---

## Versiones de la semana

Runner `ubuntu-24.04`. Actions: `actions/checkout@v7`, `actions/setup-node@v7`, `pnpm/action-setup@v6`, `astral-sh/setup-uv@v10.3.3`, `actions/setup-java@v6`, `actions/setup-go@v7`, `Swatinem/rust-cache@v2`, `taiki-e/install-action@v2` y `actions/upload-artifact@v7`. actionlint 1.7.12 (imagen `rhysd/actionlint:1.7.12`), pre-commit 4.7.0 (con `pre-commit-hooks` v6.0.0 y `ruff-pre-commit` v0.16.7), Husky 9.1.7, SonarQube Community Build 26.11 (imagen `sonarqube:26.11.0.134175-community`) y SonarScanner CLI 8.4 (imagen `sonarsource/sonar-scanner-cli:12.4.0.4321_8.4.0`). Están registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Los labs crean un repositorio público en tu cuenta de GitHub: no subas nada privado a `ci-lab`. Los minutos de GitHub Actions no tienen límite en repositorios públicos con los runners estándar; en uno privado gastan la cuota de tu cuenta. Y un pipeline en verde dice que las suites pasaron en una máquina limpia, no que el código esté bien: vale lo que valen los tests que ejecuta.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 44 - Testing de Seguridad Básico (OWASP)](../week-44-testing_de_seguridad_basico_owasp/README.md) | Semana 46 - Próximamente |
