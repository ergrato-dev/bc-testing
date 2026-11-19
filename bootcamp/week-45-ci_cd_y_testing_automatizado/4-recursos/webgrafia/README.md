# Webgrafía - Semana 45

| Recurso | URL | Para qué |
|---|---|---|
| Workflow syntax for GitHub Actions | https://docs.github.com/en/actions/reference/workflows-and-actions/workflow-syntax | Cada clave de un workflow: `on`, `paths`, `permissions`, `concurrency`, `jobs`, `services`, `strategy`, `timeout-minutes`. |
| Running variations of jobs in a workflow | https://docs.github.com/en/actions/how-tos/write-workflows/choose-what-workflows-do/run-job-variations | La matriz: varias claves, `include`, `exclude`, `fail-fast` y `max-parallel`. |
| Creating PostgreSQL service containers | https://docs.github.com/en/actions/tutorials/use-containerized-services/create-postgresql-service-containers | El contenedor de servicio del lab 03, también para jobs que corren dentro de un contenedor. |
| Store and share data with workflow artifacts | https://docs.github.com/en/actions/tutorials/store-and-share-data | `upload-artifact` y `download-artifact`, retención y artifacts entre jobs. |
| Dependency caching | https://docs.github.com/en/actions/reference/workflows-and-actions/dependency-caching | Claves, `restore-keys`, límites de tamaño y cuándo se borra una caché. |
| About rulesets | https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-rulesets/about-rulesets | Las reglas de rama que hacen obligatorio un check. |
| GitHub Actions billing | https://docs.github.com/en/billing/concepts/product-billing/github-actions | Minutos gratuitos de repositorios públicos y privados. |
| `gh run` | https://cli.github.com/manual/gh_run | `list`, `view`, `watch`, `download`, `rerun` y `cancel`. |
| astral-sh/setup-uv | https://github.com/astral-sh/setup-uv | Entradas de la action de uv: `python-version`, `enable-cache`, `cache-dependency-glob`. |
| actions/setup-go | https://github.com/actions/setup-go | `go-version-file`, la línea `toolchain` y la caché. |
| Swatinem/rust-cache | https://github.com/Swatinem/rust-cache | Qué guarda la caché de Rust y cómo se calcula su clave. |
| taiki-e/install-action | https://github.com/taiki-e/install-action | Las herramientas que instala como binario, con versión fija. |
| actionlint | https://github.com/rhysd/actionlint | Las comprobaciones de actionlint y cómo ejecutarlo con Docker o en un hook. |
| Playwright: Continuous Integration | https://playwright.dev/docs/ci-intro | Playwright en GitHub Actions, *sharding* y la imagen oficial de Docker. |
| pre-commit | https://pre-commit.com/ | El formato de `.pre-commit-config.yaml`, los hooks locales y `pre-commit autoupdate`. |
| Husky | https://typicode.github.io/husky/ | Instalación con pnpm y repositorios con el `package.json` en una subcarpeta. |
| SonarQube Community Build | https://docs.sonarsource.com/sonarqube-community-build/ | Instalación, análisis por lenguaje e informes de cobertura. |
| SonarQube: quality gates | https://docs.sonarsource.com/sonarqube-community-build/quality-standards-administration/managing-quality-gates/introduction-to-quality-gates | Condiciones, Sonar way y el código nuevo. |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — videos](../videografia/README.md) | [Glosario](../../5-glosario/README.md) → |

[↑ Semana 45](../../README.md)
