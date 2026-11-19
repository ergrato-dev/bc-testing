# Proyecto - CI de las cinco suites

## Objetivo

Añadir a tu repositorio `qa-<nombre>-<apellido>` un workflow de GitHub Actions por suite (Go, Python, Java, Rust y E2E) que ejecute la suite completa, suba el informe de cobertura y falle si baja del umbral; hooks de pre-commit para todo el repositorio, y `ci.md` con los runs que lo demuestran.

## Tiempo estimado

3 horas, más la espera de los runs.

## Punto de partida

Tu repositorio con las suites de las semanas anteriores, cada una con su comando de verificación y su umbral del 85 %:

| Suite | Comando | Umbral |
|---|---|---|
| Go | `go/verify.sh` ([semana 36](../../week-36-proyecto_integrador_go/README.md); en la [44](../../week-44-testing_de_seguridad_basico_owasp/README.md), `-coverpkg` sin `cmd/server`) | `awk` sobre `go tool cover -func` |
| Python | `coverage run -m pytest` + `coverage run --append -m behave` + `coverage report` ([semana 24](../../week-24-proyecto_integrador_python/README.md)) | `fail_under` de `pyproject.toml` |
| Java | `mvn verify` ([semana 31](../../week-31-proyecto_integrador_java/README.md)) | Regla `check` de JaCoCo |
| Rust | `rust/verify.sh` ([semana 41](../../week-41-proyecto_integrador_rust/README.md)) | `--fail-under-lines` de `cargo llvm-cov` |
| E2E | `pnpm exec playwright test` ([semana 42](../../week-42-testing_end_to_end_con_playwright/README.md)) | Sin cobertura |

Si tu repositorio tiene además `js/` con Jest, añade un sexto workflow con lo del [lab 01](../2-practicas/lab-01-primer-workflow/README.md) y el `coverageThreshold` de la semana 15; no lo comprueba el autograding.

| Archivo del starter | Qué es | Qué haces |
|---|---|---|
| `.github/workflows/go.yml` | El workflow de Go, completo: el modelo de los demás | Lo copias y lo lees (paso 2) |
| `.github/workflows/{python,java,rust,e2e}.yml` | Eventos, permisos, `concurrency` y el job con `checkout`; los steps propios del lenguaje son `TODO` | Los completas (pasos 3 a 6) |
| `.pre-commit-config.yaml` | Hooks publicados, `ruff` y `gofmt`; los hooks de `go vet` y de Rust son `TODO` | Lo completas (paso 8) |
| `ci.md` | La plantilla del informe, con seis secciones | La completas (paso 10) |

El ejemplo es el museo. Si tus carpetas o comandos se llaman distinto, cambia las rutas de `paths`, `working-directory` y los artifacts.

## Paso a paso

### Paso 1: la rama y los archivos

```bash
cd ~/qa-<nombre>-<apellido>
git switch -c week-45
cp -r <ruta-del-bootcamp>/bootcamp/week-45-ci_cd_y_testing_automatizado/3-proyecto/starter/.github .
cp <ruta-del-bootcamp>/bootcamp/week-45-ci_cd_y_testing_automatizado/3-proyecto/starter/.pre-commit-config.yaml .
cp <ruta-del-bootcamp>/bootcamp/week-45-ci_cd_y_testing_automatizado/3-proyecto/starter/ci.md .
```

Comprueba que tu repositorio está en GitHub (`gh repo view`). Si es privado, los runs gastan minutos de tu cuota gratuita: un *push* que lanza los cinco workflows del museo suma unos 9 minutos de jobs cuando todo va bien.

Y que las suites pasan en tu equipo antes de llevarlas a la CI: `bash go/verify.sh`, `bash rust/verify.sh`, `mvn -B verify` en `java/`, la suite de Python y la de Playwright. Lo que falla aquí fallará allí.

### Paso 2: el modelo, `go.yml`

Léelo entero. Lo común a los cinco workflows:

- `on`: `push` a `main`, `pull_request` y `workflow_dispatch`, con `paths` para su carpeta y su propio archivo.
- `permissions: contents: read` y `concurrency` por workflow y rama.
- `runs-on: ubuntu-24.04` y `timeout-minutes` con margen (15 en Go).
- `defaults.run.working-directory` con la carpeta de la suite.

Lo propio de Go:

- `setup-go` con `go-version-file: go/go.mod`. Si tu `go.mod` tiene línea `toolchain` (la corrección de la semana 44), instala esa versión:

  ```text
  Setup go version spec 1.26.11
  go version go1.26.11 linux/amd64
  ```

- Un solo step de trabajo: `bash verify.sh`. El umbral está dentro; el workflow no lo repite.
- Dos steps con `if: always()`: el HTML de cobertura y el artifact `coverage-go`.

Los tests de integración de `pgstore` arrancan PostgreSQL con testcontainers-go: el runner tiene Docker y el workflow no necesita `services`.

### Paso 3: `python.yml`

Completa los `TODO` con los comandos de tu suite. En el museo:

```yaml
      - uses: astral-sh/setup-uv@v10.3.3
        with:
          enable-cache: true
          cache-dependency-glob: python/uv.lock
      - run: uv sync --locked
      - run: uv run coverage run -m pytest -q
      - run: uv run coverage run --append -m behave
      - name: Coverage threshold (85 %)
        run: uv run coverage report
```

Sin `python-version`, `setup-uv` deja que uv use el `.python-version` del proyecto. Los tests de repositorio usan Testcontainers, como en Go.

Después del umbral, con `if: always()`: `coverage xml`, `coverage html` y la tabla en `$GITHUB_STEP_SUMMARY`, los tres con `--fail-under=0` ([lab 02](../2-practicas/lab-02-matrix-cache-y-artifacts/README.md#paso-3-el-informe-como-artifact-y-en-el-resumen)), y el artifact `coverage-python` con `coverage.xml` y `htmlcov/`. Usa `if-no-files-found: ignore`: si la suite falla antes de generar el informe, no hay nada que subir.

### Paso 4: `java.yml`

```yaml
      - uses: actions/setup-java@v6
        with:
          distribution: temurin
          java-version: "21"
          cache: maven
          cache-dependency-path: java/pom.xml
      - run: mvn -B -ntp verify
```

`-B` (*batch mode*) quita los colores y la interacción; `-ntp` (*no transfer progress*) quita del log miles de líneas de descargas. El artifact `coverage-java`: `target/site/jacoco/`, `target/surefire-reports/` y `target/failsafe-reports/`. En el log del museo, el umbral en verde:

```text
[INFO] All coverage checks have been met.
[INFO] BUILD SUCCESS
```

### Paso 5: `rust.yml`

```yaml
      - name: Toolchain from rust-toolchain.toml
        run: rustup toolchain install
      - uses: Swatinem/rust-cache@v2
        with:
          workspaces: rust
      - uses: taiki-e/install-action@v2
        with:
          tool: cargo-nextest@0.9.152,cargo-llvm-cov@0.9.1
      - run: bash verify.sh
```

- `rustup toolchain install` sin argumentos instala la versión y los componentes de `rust-toolchain.toml` (el runner trae otra).
- `rust-cache` guarda `~/.cargo` y `rust/target`: la compilación es lo que más tarda.
- `install-action` descarga los binarios de `cargo-nextest` y `cargo-llvm-cov` en lugar de compilarlos. Usa las versiones de tu semana 40.
- `verify.sh` arranca PostgreSQL con `docker compose -f ../compose.yaml`. Por eso `paths` incluye `compose.yaml`.

Después, con `if: always()`: `cargo llvm-cov report --lcov --output-path lcov.info` y `cargo llvm-cov report --html`, y el artifact `coverage-rust` con `rust/lcov.info` y `rust/target/llvm-cov/html/`.

### Paso 6: `e2e.yml`

Un job por navegador con una matriz:

```yaml
jobs:
  test:
    strategy:
      fail-fast: false
      matrix:
        project: [chromium, firefox, webkit]
```

Los steps: `pnpm/action-setup` y `setup-node` como en el lab 01 (con `e2e/` en lugar de `js/`), `pnpm install --frozen-lockfile`, y:

```yaml
      - run: pnpm exec playwright install --with-deps ${{ matrix.project }}
      - run: pnpm exec playwright test --project=${{ matrix.project }}
```

`--with-deps` instala también las bibliotecas del sistema que necesita cada navegador, con `apt`. El artifact, con `name: playwright-${{ matrix.project }}`: `e2e/playwright-report/` y `e2e/test-results/`.

`playwright.config.ts` de la semana 42 ya tiene en cuenta la CI: `retries: process.env.CI ? 1 : 0` y `reuseExistingServer: !process.env.CI`. GitHub Actions define `CI=true` en todos los jobs.

### Paso 7: actionlint

Antes de subir nada, comprueba la sintaxis y las expresiones de los cinco workflows con **actionlint**, desde la raíz del repositorio:

```bash
docker run --rm -v "$PWD:/repo" -w /repo rhysd/actionlint:1.7.12
echo $?
# 0
```

Sin salida y con código 0: no hay errores. actionlint entiende las expresiones de GitHub; un error de escritura que GitHub solo descubriría al ejecutar el job, lo ve antes. En el museo, con dos errores a propósito en `e2e.yml`:

```text
.github/workflows/e2e.yml:47:54: property "browser" is not defined in object type {project: string} [expression]
   |
47 |       - run: pnpm exec playwright test --project=${{ matrix.browser }}
   |                                                      ^~~~~~~~~~~~~~
.github/workflows/e2e.yml:49:13: undefined function "allways". available functions are "always", "cancelled", "case", "contains", "endswith", "failure", "format", "fromjson", "hashfiles", "join", "startswith", "success", "tojson" [expression]
```

### Paso 8: pre-commit

Completa los tres `TODO` de `.pre-commit-config.yaml` con hooks locales, como los de Go del [lab 04](../2-practicas/lab-04-hooks-y-quality-gate/README.md#paso-4-hooks-locales-de-go):

- `go vet`: `bash -c 'cd go && go vet ./... && go vet -tags integration ./...'`, `files: ^go/`, `pass_filenames: false`.
- `cargo fmt`: `bash -c 'cd rust && cargo fmt --check'`, solo con archivos `^rust/.*\.rs$`.
- `cargo clippy`: `bash -c 'cd rust && cargo clippy --all-targets -- -D warnings'`, con `files: ^rust/`.

`exclude: ^(perf|security)/results/` deja fuera las evidencias de las semanas 43 y 44: son salidas de herramientas y no se reformatean.

```bash
pre-commit install
pre-commit run --all-files
```

La primera pasada encuentra cosas. En el museo modificó seis archivos de `python/` (espacios al final de línea, fin de archivo y formato de `ruff format`), y `ruff check` dio 12 avisos: 7 de orden de imports en el código propio y 5 en la migración generada por Alembic. Se resolvió así:

1. `[tool.ruff]` en `python/pyproject.toml`, con `src = ["."]` (el motivo, en el lab 04) y `extend-exclude = ["migrations/versions"]` (el código generado no se toca).
2. `uvx ruff@0.16.7 check --fix .` en `python/` para los dos avisos restantes.
3. **La suite de Python otra vez**, antes del commit: `ruff` cambió código, y solo los tests dicen si el cambio es inocuo.

Repite `pre-commit run --all-files` hasta que todo esté en `Passed`.

### Paso 9: los runs

Haz commit y sube la rama:

```bash
git add .github .pre-commit-config.yaml python/pyproject.toml
git commit -m "Add CI workflows and pre-commit hooks"
git push -u origin week-45
gh pr create --title "Week 45: CI" --body "Un workflow por suite, artifacts de cobertura y pre-commit."
```

El PR añade los cinco workflows, y cada uno tiene su propio archivo en `paths`: se ejecutan los cinco.

```bash
gh pr checks --watch
```

Si alguno falla, lee el log (`gh run view <id> --log-failed`), arréglalo en la rama y vuelve a subir. Fallos habituales: una herramienta que en tu equipo estaba instalada y en el runner no, una ruta de artifact con la carpeta equivocada, una variable de entorno que solo existía en tu terminal.

Cuando los cinco estén en verde, descarga un artifact para comprobar que contiene el informe (`gh run download <id>`).

**El PR que el umbral para.** Desde `week-45`, crea una rama que borre los tests de integración de una suite y abre un PR contra `week-45` (o contra `main`, si ya mezclaste). En el museo, sin los tests de `pgstore`:

```text
?   	example.com/qa-museo/go/pgstore	[no test files]
== umbral de cobertura
total: 73.4 % (mínimo: 85 %)
FALLO: la cobertura no llega al 85 %
##[error]Process completed with exit code 1.
```

Solo se ejecutó el workflow `go`. Guarda el enlace del run y cierra ese PR sin mezclar (`gh pr close <número> --delete-branch`).

### Paso 10: `ci.md`

Completa las seis secciones con los runs de tu repositorio:

1. **Workflows**: una fila por workflow.
2. **Runs en verde**: el enlace de cada uno, la duración de cada job y la cobertura.
3. **Un PR que el umbral para**: el enlace, el log y por qué bajó.
4. **Hooks de pre-commit**: qué hay y qué encontró la primera pasada.
5. **Decisiones**: matriz o no, cómo arranca PostgreSQL cada suite, los `timeout-minutes` y lo que te sorprendió. En el museo, el job de Firefox tardó 10 min 42 s porque `apt` descargó a 89 kB/s; el mismo paso tardó 19 s en Chromium.
6. **Checks obligatorios recomendados**: cuáles harías obligatorios en `main` y cómo encaja con `paths` ([teoría 01](../1-teoria/01-ci-cd-y-anatomia-de-un-workflow.md#4-del-check-rojo-al-merge-bloqueado)).

Los enlaces van completos (`https://github.com/<usuario>/<repo>/actions/runs/<id>`): el autograding cuenta al menos seis distintos.

### Paso 11: comprobar y entregar

```bash
PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 45
```

Necesita Docker (actionlint) y uv (pre-commit). Haz commit de `ci.md` y súbelo al PR de `week-45`: como no está en ningún `paths`, no lanza ningún workflow.

## ✅ Requisitos verificables

Las comprobaciones de [checks.json](../checks.json):

| Comprobación | Qué verifica |
|---|---|
| `workflows` | Hay un workflow por suite en `.github/workflows/`: go, python, java, rust y e2e |
| `actionlint` | actionlint no encuentra errores en los workflows |
| `eventos` | Cada workflow se ejecuta en los pull requests, filtra por `paths`, solo lee el repositorio y tiene `timeout-minutes` |
| `artifacts` | Cada workflow sube sus informes como artifact también cuando falla (`if: always()`) |
| `umbrales` | La cobertura mínima del 85 % está en el proyecto de cada lenguaje y su workflow ejecuta ese comando |
| `pre-commit` | pre-commit pasa sobre todo el repositorio, con hooks de gofmt, go vet, cargo fmt y clippy |
| `informe` | `ci.md` tiene sus seis secciones, cinco runs en verde y el del PR en rojo |
| `sin-todo` | No queda ningún TODO en los workflows, en `.pre-commit-config.yaml` ni en `ci.md` |

El autograding no puede ver tus runs de GitHub: comprueba los archivos. Lo que demuestra que la CI funciona son los enlaces de `ci.md`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - Hooks y quality gate](../2-practicas/lab-04-hooks-y-quality-gate/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 45](../README.md)
