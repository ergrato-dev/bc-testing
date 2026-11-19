# 03 - Matrix, artifacts, paralelismo y umbrales

> Herramientas: `strategy.matrix`, `actions/upload-artifact`, `concurrency`, filtros de `paths` y el umbral de cobertura de cada lenguaje

Un workflow que ejecuta la suite ya es CI. Esta teoría lo convierte en uno que se usa a diario: que pruebe varias versiones a la vez, que guarde los informes para leerlos después, que no gaste minutos en lo que no cambió y que se ponga en rojo cuando la cobertura baja.

---

## 🎯 Objetivos

- Multiplicar un job con una matriz y decidir qué hace cuando una combinación falla.
- Guardar informes con artifacts y descargarlos con `gh`.
- Evitar runs inútiles con `paths` y `concurrency`.
- Distinguir los niveles de paralelismo: workflows, jobs, matriz y tests.
- Hacer que la cobertura por debajo del umbral ponga el job en rojo, en cada lenguaje.

---

## 1. La matriz

```yaml
jobs:
  test:
    strategy:
      fail-fast: false
      matrix:
        python-version: ["3.13", "3.14"]
    runs-on: ubuntu-24.04
    steps:
      - uses: actions/checkout@v7
      - uses: astral-sh/setup-uv@v10.3.3
        with:
          python-version: ${{ matrix.python-version }}
```

`matrix` crea un job por cada valor: `test (3.13)` y `test (3.14)`, cada uno en su máquina y a la vez. `${{ matrix.python-version }}` es una **expresión**: GitHub la sustituye por el valor del job antes de ejecutarlo. Con dos claves (`os` y `python-version`, por ejemplo), la matriz crea todas las combinaciones.

| Opción | Qué hace | Cuándo |
|---|---|---|
| `fail-fast: true` (por defecto) | Si un job de la matriz falla, GitHub cancela los demás | Ahorrar minutos cuando cualquier fallo invalida el cambio |
| `fail-fast: false` | Todos terminan | Ver si falla en una versión o en todas. En el lab 02 las dos fallan igual, y eso dice que el fallo no depende de la versión |
| `max-parallel` | Limita cuántos jobs de la matriz corren a la vez | Un servicio externo que no aguanta tantas conexiones |

**Para qué una matriz en un proyecto que fija su versión.** El museo usa una versión de cada lenguaje y no necesita matriz de versiones. Una biblioteca que otros instalan sí: la del lab 02 declara `requires-python = ">=3.13"`, así que promete funcionar en 3.13 y en 3.14. La matriz comprueba la promesa. En el museo la matriz sirve para otra cosa: un job por navegador de Playwright.

---

## 2. Artifacts

Un job termina y su máquina se borra. Lo que no se guarde se pierde, incluido el informe de cobertura. Un **artifact** es una carpeta que el job sube a GitHub:

```yaml
      - uses: actions/upload-artifact@v7
        if: always()
        with:
          name: coverage-python-${{ matrix.python-version }}
          path: |
            python/coverage.xml
            python/htmlcov/
          retention-days: 7
```

| Clave | Para qué |
|---|---|
| `if: always()` | Ejecutar el step aunque uno anterior haya fallado. Sin él, el informe solo se sube cuando todo pasa, que es cuando menos falta hace |
| `name` | Único en el run. En una matriz, incluye la variable o los jobs chocan |
| `path` | Una o varias rutas, desde la raíz del repositorio (no le afecta `working-directory`) |
| `retention-days` | Días que GitHub lo guarda. Por defecto, 90 |
| `if-no-files-found: ignore` | No avisar si no hay archivos: en el museo, un job que falla antes de generar el informe |

Se descargan desde la página del run o con `gh`:

```bash
gh run download 44426167606
find . -maxdepth 2
# ./coverage-python-3.13
# ./coverage-python-3.13/coverage.xml
# ./coverage-python-3.13/htmlcov
# ./coverage-python-3.14
# ./coverage-python-3.14/coverage.xml
# ./coverage-python-3.14/htmlcov
```

`htmlcov/index.html` se abre en el navegador, igual que en tu equipo. Para un fallo de Playwright, el artifact es la traza de la [semana 42](../../week-42-testing_end_to_end_con_playwright/README.md): `pnpm exec playwright show-trace` sobre el `trace.zip` descargado.

### El resumen del job

Lo que un step escribe en el archivo `$GITHUB_STEP_SUMMARY` aparece, en Markdown, en la página del run. Es el sitio para la tabla de cobertura:

```yaml
      - name: Coverage summary
        if: always()
        run: uv run coverage report --format=markdown --fail-under=0 >> "$GITHUB_STEP_SUMMARY"
```

`--fail-under=0` tiene su motivo. `coverage report` comprueba el `fail_under` de `pyproject.toml` y termina con código 2 si no se cumple; sin la opción, el step del resumen falla también y el log muestra dos errores por la misma causa (lab 02).

---

## 3. Gastar solo los minutos necesarios

### `paths`: solo si cambió lo suyo

```yaml
on:
  push:
    branches: [main]
    paths:
      - "python/**"
      - ".github/workflows/python.yml"
```

El workflow solo se ejecuta si el *push* toca alguna de esas rutas. En el museo, un commit que solo cambia `python/` lanza solo el workflow `python`, no los cinco. El propio workflow va en la lista: si lo cambias, quieres verlo funcionar.

Sin `paths`, cualquier cambio lo lanza. El workflow `js` del lab 01 no tiene filtro y se ejecutó en los 11 *pushes* a `main` del lab, incluidos los que solo tocaban Python y Go. En un repositorio pequeño da igual; con cinco suites que tardan minutos, no.

El riesgo está en la [teoría 01](01-ci-cd-y-anatomia-de-un-workflow.md#4-del-check-rojo-al-merge-bloqueado): un check obligatorio cuyo workflow no se ejecuta deja el PR esperando.

### `concurrency`: un run por rama

```yaml
concurrency:
  group: ${{ github.workflow }}-${{ github.ref }}
  cancel-in-progress: true
```

Dos runs del mismo grupo no corren a la vez. Con `cancel-in-progress`, el nuevo cancela al viejo: si subes dos commits seguidos, solo importa el resultado del último. En el lab 02:

```text
X Canceling since a higher priority waiting request for python-refs/heads/main exists
X The operation was canceled.
```

`github.workflow` y `github.ref` son el nombre del workflow y la rama. Cada workflow y cada rama tienen su grupo: un *push* a `main` no cancela un PR.

### `timeout-minutes`: un techo

Un job sin límite puede quedarse colgado hasta 6 horas, el máximo de GitHub, gastando minutos. Los workflows del museo ponen `timeout-minutes` de 15 a 25. No es teórico: en el run del museo, el job de Firefox tardó 10 min 42 s porque `playwright install --with-deps` descargó los paquetes del sistema de un *mirror* lento:

```text
Fetched 48.8 MB in 9min 7s (89.3 kB/s)
```

El mismo paso tardó 19 s en el job de Chromium. Con `timeout-minutes: 15` el job pasó; con 10 habría fallado sin que el código tuviera la culpa. El techo se elige con margen sobre lo normal, y un job que lo toca se reintenta (*Re-run failed jobs*) antes de sospechar del código.

---

## 4. Paralelismo

| Nivel | Cómo | En el museo |
|---|---|---|
| **Workflows** | Cada archivo de `.github/workflows/` es independiente | Los cinco empezaron a la vez, en el mismo segundo |
| **Jobs** | Los jobs de un workflow corren a la vez, salvo que uno tenga `needs: [otro]` | `lint` y `test` del lab 03 |
| **Matriz** | Un job por combinación | Un job de E2E por navegador |
| **Dentro del job** | El *runner* de tests reparte entre los núcleos de la máquina | `go test` por paquetes, `nextest`, Playwright con `workers` |

El tiempo que esperas es el del job más lento, no la suma. En el primer run del museo, todo en paralelo:

| Workflow | Job | Duración |
|---|---|---|
| python | test | 33 s |
| java | verify | 1 min 15 s |
| go | verify | 1 min 36 s |
| rust | verify | 2 min 46 s |
| e2e | test (chromium) / (webkit) / (firefox) | 41 s / 54 s / 10 min 42 s |

En serie, los siete jobs habrían sumado más de 18 minutos.

**Dentro del job**, el runner `ubuntu-24.04` de un repositorio público tiene 4 núcleos. Playwright usa por defecto la mitad:

```text
Running 13 tests using 2 workers
```

Para una suite de E2E grande, Playwright reparte los tests entre jobs con `--shard=1/3`, `--shard=2/3` y `--shard=3/3` en una matriz, y `playwright merge-reports` junta los informes. Con 13 tests no compensa: arrancar un job cuesta más que los tests.

**`needs`** ordena jobs cuando uno depende de otro: `needs: [lint]` hace que `test` espere a que `lint` pase, y no se ejecute si falla. Ahorra minutos si `lint` falla a menudo, pero alarga el camino cuando todo va bien. Los labs y el museo no lo usan.

---

## 5. Fallar por cobertura

El workflow no sabe qué es la cobertura: el job falla si un comando termina con un código distinto de 0. Cada lenguaje del curso ya tiene ese comando desde su semana integradora; el workflow solo tiene que ejecutarlo.

| Lenguaje | Dónde está el umbral | Qué ves en el log si no llega |
|---|---|---|
| JavaScript | `coverageThreshold` de Jest ([semana 15](../../week-15-integracion_de_estrategias_y_calidad_continua/README.md)) | `Jest: Coverage for lines (66.66%) does not meet "global" threshold (85%)` |
| Python | `fail_under` de `pyproject.toml` | `FAIL Required test coverage of 80.0% not reached. Total coverage: 76.00%` (lab 02) |
| Java | Regla de JaCoCo `check` en el `pom.xml` | En verde: `All coverage checks have been met.` |
| Go | El `awk` de `verify.sh` sobre `go tool cover -func` | `total: 73.4 % (mínimo: 85 %)` y `FALLO: la cobertura no llega al 85 %` |
| Rust | `cargo llvm-cov report --fail-under-lines 85` en `verify.sh` | El mensaje de `verify.sh` y código 1 |

El mensaje de Go es de un PR real del museo que borraba los tests de `pgstore`: los unitarios seguían en verde, pero sin la integración la cobertura bajó de 92.3 % a 73.4 % y el check `go` se puso en rojo.

Una regla para que el umbral sirva:

- **Un solo sitio.** El umbral vive en el proyecto (`pyproject.toml`, `pom.xml`, `verify.sh`) y el workflow ejecuta ese comando. Si el workflow tuviera su propio 85, tu equipo y la CI podrían discrepar sin que nadie lo notara.

---

## ✅ Resumen

- La matriz crea un job por combinación. `fail-fast: false` deja ver si falla en una versión o en todas.
- Los artifacts guardan informes después de que la máquina desaparezca; con `if: always()` se suben también cuando algo falla. `$GITHUB_STEP_SUMMARY` pone una tabla en la página del run.
- `paths` evita runs que no tocan nada; `concurrency` cancela los runs viejos de la misma rama; `timeout-minutes` pone un techo con margen.
- Paralelismo en cuatro niveles; esperas lo que tarda el job más lento.
- El umbral de cobertura vive en el proyecto y el workflow lo ejecuta: el job falla porque el comando termina con 1.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 02 - Preparar cada lenguaje, cachés y servicios](02-preparar-cada-lenguaje-caches-y-servicios.md) | [Teoría — 04 - Quality gates y hooks de pre-commit](04-quality-gates-y-hooks-de-pre-commit.md) → |

[↑ Semana 45](../README.md)
