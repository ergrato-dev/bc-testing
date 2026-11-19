# Proyecto final - La estrategia de testing de tu repositorio

## Objetivo

Cerrar tu repositorio `qa-<nombre>-<apellido>`: comprobar que todas las suites pasan con su umbral, escribir `testing-strategy.md` con los números de una ejecución real, los riesgos del sistema y las decisiones que tomaste, y un `README.md` en la raíz con el que cualquiera puede ejecutarlo todo. Al final, lo presentas.

## Tiempo estimado

6.5 horas de desarrollo y 0.5 horas de presentación.

## Punto de partida

Tu repositorio tal como quedó en la [semana 45](../../week-45-ci_cd_y_testing_automatizado/README.md):

| Carpeta | Semanas | Qué tiene |
|---|---|---|
| `python/` | [16–24](../../week-24-proyecto_integrador_python/README.md) | pytest por capas, Behave, Testcontainers, `fail_under` y `testing-report-python.md` |
| `java/` | [25–31](../../week-31-proyecto_integrador_java/README.md) | JUnit, Mockito, Spring, Testcontainers, Cucumber, JaCoCo y `testing-report-java.md` |
| `go/` | [32–36](../../week-36-proyecto_integrador_go/README.md) | `go/verify.sh` y `testing-report-go.md`; tests de seguridad de la [semana 44](../../week-44-testing_de_seguridad_basico_owasp/README.md) |
| `rust/` | [37–41](../../week-41-proyecto_integrador_rust/README.md) | `rust/verify.sh` y `testing-report-rust.md` |
| `e2e/` | [42](../../week-42-testing_end_to_end_con_playwright/README.md) | Playwright con page objects |
| `perf/` | [43](../../week-43-testing_de_performance_con_k6_y_locust/README.md) | k6, Locust y `performance-report.md` |
| `security/` | [44](../../week-44-testing_de_seguridad_basico_owasp/README.md) | ZAP, `audit.sh` y `security-report.md` |
| `.github/workflows/`, `.pre-commit-config.yaml`, `ci.md` | [45](../../week-45-ci_cd_y_testing_automatizado/README.md) | Un workflow por suite y los hooks |
| `js/` (si lo tienes) | [7–15](../../week-15-integracion_de_estrategias_y_calidad_continua/README.md) | Jest y Supertest |

El [starter](starter/) trae las plantillas de `testing-strategy.md` y `README.md`, las mismas del lab 02.

---

## Paso a paso

### Paso 1: la rama y el inventario

```bash
cd ~/qa-<nombre>-<apellido>
git switch main && git pull
git switch -c week-46
cp <ruta-del-bootcamp>/bootcamp/week-46-proyecto_final_bootcamp_testing_zero_to_hero/3-proyecto/starter/testing-strategy.md .
```

Si tu repositorio ya tiene un `README.md` en la raíz, no lo sobrescribas: compara sus secciones con las de [`starter/README.md`](starter/README.md) en el paso 5. Si no tiene, cópialo también.

Ejecuta cada suite con el comando de su workflow, una detrás de otra, y apunta tests, cobertura y tiempo en la sección 2 de `testing-strategy.md`, como en el [lab 01](../2-practicas/lab-01-inventario-del-repositorio/README.md):

```bash
(cd python && uv run coverage run -m pytest -q && uv run coverage run --append -m behave && uv run coverage report)
(cd java && mvn -B -ntp clean verify)
go/verify.sh
rust/verify.sh
(cd e2e && pnpm exec playwright test --project=chromium)
```

Los paréntesis ejecutan cada línea en una subshell: el `cd` de dentro no cambia la carpeta de las líneas siguientes. Lo que da el museo, en un equipo con todo descargado:

| Suite | Última línea útil | Tiempo |
|---|---|---|
| `python/` | `20 passed`, `3 scenarios passed`, `TOTAL ... 100%` | 9 s |
| `java/` | `Tests run: 67` (Surefire), `Tests run: 43` (Failsafe), `All coverage checks have been met.` | 36 s |
| `go/` | `total: 92.3 % (mínimo: 85 %)` | 8 s |
| `rust/` | `72 tests run: 72 passed`, `TOTAL ... 97.96%` | 91 s |
| `e2e/` | `13 passed (41.5s)` | 41.5 s |

Para el número de tests de Go, `go test -list` cuenta funciones, no subtests. Con `-v`, cada test y cada subtest imprime una línea `=== RUN`; las de los subtests llevan `/` en el nombre:

```bash
cd go
go test -count=1 -tags integration -v ./... > /tmp/go-v.txt 2>&1
grep '^=== RUN' /tmp/go-v.txt | grep -vc /
# 36
grep '^=== RUN' /tmp/go-v.txt | grep -c /
# 80
cd ..
```

La fila de `perf/` y `security/` sale de sus reportes de las semanas 43 y 44: no hace falta repetir la prueba de carga para el inventario.

Si una suite falla o no llega al umbral, arréglala antes de seguir. La estrategia describe un repositorio en verde.

### Paso 2: los umbrales

Comprueba que el 85 % está en el proyecto de cada suite, no en el workflow:

```bash
grep -nE '^fail_under' python/pyproject.toml
# 35:fail_under = 85
grep -n '<minimum>' java/pom.xml
# 186:                      <minimum>0.85</minimum>
grep -n 'total < ' go/verify.sh
# 25:awk -v total="$total" 'BEGIN {if (total < 85) {print "FALLO: la cobertura no llega al 85 %"; exit 1}}'
grep -n -- '--fail-under-lines' rust/verify.sh
# 29:cargo llvm-cov report --summary-only --fail-under-lines 85 || {
```

`--` le dice a `grep` que lo siguiente no es una opción suya, aunque empiece por `-`. Los números de línea de tu repositorio serán otros.

Si tienes `js/`, haz lo del [lab 02](../2-practicas/lab-02-estrategia-y-readme/README.md#paso-5-criterios-de-salida-y-la-primera-decisión): `coverageThreshold` en `package.json` y `jest --coverage` en el script `test`.

### Paso 3: los workflows

Los cinco de la semana 45 siguen ahí. Si tienes `js/`, añade `js.yml` como en el [lab 01 de la semana 45](../../week-45-ci_cd_y_testing_automatizado/2-practicas/lab-01-primer-workflow/README.md), con `pnpm test`, que ya aplica el umbral. Después:

```bash
docker run --rm -v "$PWD:/repo" -w /repo rhysd/actionlint:1.7.12
echo $?
# 0
```

### Paso 4: `testing-strategy.md`

Las secciones siguen la [teoría](../1-teoria/01-la-estrategia-de-testing-del-repositorio.md), con el mismo número:

1. **Sistema y alcance.** Tu dominio en dos o tres frases, qué implementa cada carpeta y qué queda fuera.
2. **Inventario de suites.** La tabla del paso 1, con la fecha y el equipo.
3. **Riesgos.** Lee la sección «Riesgos y huecos» de tus cuatro `testing-report-*.md` y los riesgos de `performance-report.md` y `security-report.md`. Junta los que se repiten: en el museo, «el préstamo no es una transacción» aparecía en tres reportes. Cada riesgo con test va a una fila con capa, suite y comando, dónde corre y criterio.
4. **Dónde se ejecuta cada prueba.** Hook, local, CI y a mano. Sale de `.pre-commit-config.yaml`, de los workflows y de `ci.md`.
5. **Criterios de salida.** Los cuatro umbrales del paso 2, los workflows en verde, los navegadores del E2E, los `thresholds` de k6 y el código de salida de `audit.sh`. Cada uno con dónde vive y quién lo comprueba.
6. **Decisiones.** Al menos tres, con contexto, decisión, alternativas y consecuencias. Las tuyas ya están tomadas; solo falta escribirlas. Candidatas: cómo arranca PostgreSQL cada suite, dónde vive el umbral, qué no corre en la CI y por qué, un workflow por suite o uno solo, por qué Testcontainers en una suite y `docker compose` en otra.
7. **Riesgos abiertos y próximos pasos.** Los riesgos sin test, lo que la sección 4 deja fuera de la CI y lo que los reportes dejaron pendiente. Cada uno con su próximo paso, numerado.

Enlaza los reportes en lugar de copiarlos. Las rutas son relativas a la raíz, como `go/testing-report-go.md`.

### Paso 5: `README.md`

Las cinco secciones del [starter](starter/README.md):

- **Estado:** un *badge* por cada archivo de `.github/workflows/`, con tu usuario y tu repositorio, y la tabla de tests, cobertura y umbral de cada suite.
- **Estructura:** una línea por carpeta.
- **Requisitos:** herramientas con la versión fijada en tu repositorio (`.python-version`, `go.mod`, `rust-toolchain.toml`, `packageManager`...) y Docker.
- **Cómo ejecutar las suites:** los comandos del paso 1, más los de `perf/` y `security/`.
- **Documentación:** `testing-strategy.md`, `ci.md` y cada reporte.

Si tu README ya existía, conserva lo que tenga y añade las secciones que falten con estos mismos títulos.

### Paso 6: los enlaces

```bash
for l in $(cat README.md testing-strategy.md | grep -oE '[]][(][^)]+[)]' | sed -E 's/^..//; s/.$//; s/#.*//' | grep -v '^http' | sort -u); do
  test -e "$l" || echo "no existe: $l"
done
```

- `grep -oE '[]][(][^)]+[)]'` saca de cada enlace Markdown el corchete de cierre, el paréntesis y el destino.
- `sed` quita los dos primeros caracteres y el último, y lo que va detrás de `#` (el ancla).
- `grep -v '^http'` deja fuera los enlaces externos, como los *badges*.
- `test -e` comprueba que el archivo existe.

Sin salida, todos los enlaces llevan a un archivo.

### Paso 7: comprobar y entregar

```bash
cd <ruta-del-bootcamp>
PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 46
# 8/8 comprobaciones superadas.
```

```bash
cd ~/qa-<nombre>-<apellido>
git add testing-strategy.md README.md
git commit -m "Add the testing strategy and the repository README"
git push -u origin week-46
gh pr create --fill
```

Añade al `git add` lo que hayas cambiado en los pasos 2 y 3. Si el PR solo toca documentos de la raíz, por los `paths` de la semana 45 no se ejecuta ningún workflow: los checks que valen son los del último run de cada suite en `main`. Mezcla el PR y comprueba en la página del repositorio que todos los *badges* salen en verde.

### Paso 8: la versión 1.0.0 (opcional)

La sección 4 de tu estrategia dice qué se ejecuta a mano antes de cada versión. Hazlo, con tu repositorio en `main`:

```bash
perf/run.sh load
security/audit.sh
```

Si los dos terminan con código 0, etiqueta la versión:

```bash
git tag -a v1.0.0 -m "First complete version: all suites, CI and testing strategy"
git push origin v1.0.0
```

`git tag -a` crea una etiqueta con mensaje que señala el commit actual; `git push origin v1.0.0` la sube. En GitHub aparece en *Tags*.

---

## Presentación (0.5 h)

Diez minutos por aprendiz, con el repositorio abierto en GitHub:

1. **El sistema** (1 min): el dominio y las carpetas, desde el README.
2. **El estado** (1 min): los *badges* y la tabla de cobertura.
3. **Un riesgo de punta a punta** (3 min): uno de la sección 3. Enseña el test que lo detecta, el workflow donde corre y un run en verde.
4. **Una decisión** (2 min): contexto, alternativas y qué te costó.
5. **Lo que no cubre ninguna suite** (2 min): los riesgos abiertos y el orden en que los atacarías.
6. **Preguntas** (1 min).

---

## ✅ Requisitos verificables

Las comprobaciones de [checks.json](../checks.json):

| Comprobación | Qué verifica |
|---|---|
| `suites` | El repositorio tiene `python/`, `java/`, `go/`, `rust/`, `e2e/`, `perf/`, `security/` y `.github/workflows/` |
| `umbrales` | Cada suite con cobertura tiene un umbral de al menos el 85 % en su proyecto (y `coverageThreshold` si existe `js/`) |
| `e2e` | Al menos 10 tests E2E y page objects en `e2e/pages/` |
| `perf-security` | Una prueba de k6 con `thresholds`, `performance-report.md`, `security/audit.sh` y `security-report.md` |
| `workflows` | Un workflow por suite (y `js.yml` si existe `js/`), sin errores de actionlint |
| `estrategia` | `testing-strategy.md` tiene sus siete secciones, al menos tres decisiones completas y ningún `TODO` |
| `readme` | `README.md` tiene sus cinco secciones, un *badge* por workflow, el enlace a la estrategia y ningún `TODO` |
| `enlaces` | Los enlaces relativos de los dos documentos llevan a archivos que existen |

El autograding comprueba los archivos, no ejecuta las suites: eso ya lo hace la CI en cada PR, y los *badges* lo enseñan.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Estrategia y README](../2-practicas/lab-02-estrategia-y-readme/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 46](../README.md)
