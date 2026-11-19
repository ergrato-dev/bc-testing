# Lab 01 - Primer workflow

## Objetivo

Escribir el workflow de la suite de JavaScript de `ci-lab`, seguir sus runs desde la terminal con `gh`, leer el log y ver cómo un *pull request* con un fallo se queda con el check en rojo.

## Tiempo estimado

55 minutos.

## Requisitos

- La [teoría 01](../../1-teoria/01-ci-cd-y-anatomia-de-un-workflow.md).
- El repositorio `ci-lab` en tu GitHub ([preparar el repositorio](../README.md#preparar-el-repositorio-antes-del-lab-01)).

## Punto de partida

[`starter/js.yml`](starter/js.yml) es el workflow comentado por PASOS. Cópialo a la carpeta que lee GitHub:

```bash
cd ~/ci-lab
mkdir -p .github/workflows
cp <ruta-del-bootcamp>/bootcamp/week-45-ci_cd_y_testing_automatizado/2-practicas/lab-01-primer-workflow/starter/js.yml .github/workflows/js.yml
```

`mkdir -p` crea `.github` y `workflows` de una vez. El nombre del archivo no importa a GitHub; el del workflow sale de `name:`.

---

## Paso 1: lo que el workflow va a ejecutar

Antes de escribir el workflow, ejecuta en tu equipo lo mismo que ejecutará el runner, en el mismo orden:

```bash
cd js
pnpm install --frozen-lockfile
pnpm test
# Tests:       7 passed, 7 total
cd ..
```

Si algo falla aquí, fallará también en la CI, y es más rápido arreglarlo en tu equipo.

## Paso 2: el workflow mínimo

Descomenta el PASO 2: todas las líneas debajo de `# PASO 2` y de `# PASO 2: (sigue)`, salvo las del PASO 5. Lee cada clave con la tabla de la [teoría 01](../../1-teoria/01-ci-cd-y-anatomia-de-un-workflow.md#2-github-actions). Lo que hace, en orden:

1. `actions/checkout` descarga el repositorio en la máquina.
2. `pnpm/action-setup` instala la versión de pnpm que dice `packageManager` en `js/package.json`.
3. `actions/setup-node` instala Node 22 y restaura la caché de pnpm.
4. Los dos `run`, en `js/` por `working-directory`.

Haz commit y súbelo:

```bash
git add .github/workflows/js.yml
git commit -m "Add JS workflow"
git push
```

## Paso 3: seguir el run

El *push* dispara el workflow. Sigue el run hasta que termine:

```bash
gh run list -w js
# STATUS  TITLE            WORKFLOW  BRANCH  EVENT  ID           ELAPSED  AGE
# ✓       Add JS workflow  js        main    push   44425293075  18s      ...
```

Mientras está en marcha, el estado es `*` (en curso). `gh run watch` lo sigue en directo y termina cuando el run termina; con `--exit-status`, su código de salida es el del run:

```bash
gh run watch 44425293075 --exit-status
echo $?
# 0
```

Usa el id de tu run. Después, los jobs:

```bash
gh run view 44425293075
# ✓ main js · 44425293075
# Triggered via push less than a minute ago
#
# JOBS
# ✓ test in 15s (ID 135228455858)
```

Abre la URL que muestra al final (`View this run on GitHub`) para ver lo mismo en el navegador: la pestaña *Actions* del repositorio tiene un run por cada *push*.

## Paso 4: leer el log

```bash
gh run view 44425293075 --log | less
```

Cada línea empieza por el job y el step. Busca (`/` en `less`) estas cuatro partes:

```text
test  Run pnpm/action-setup@v6     Switching pnpm from v12.17.0 to v12.6.0...
test  Run actions/setup-node@v7    pnpm cache is not found
test  Run pnpm test                Tests:       7 passed, 7 total
test  Post Run actions/setup-node@v7  Cache saved with the key: node-cache-Linux-x64-pnpm-106d084e...
```

(Las líneas reales llevan además la hora; aquí están recortadas.)

- El runner traía pnpm 12.17.0; la action cambió a la versión del proyecto.
- El primer run no tiene caché; la guarda al final (`Post Run`) porque el job pasó.

Pulsa `q` para salir de `less`.

## Paso 5: un check rojo en un pull request

Descomenta las dos líneas del PASO 5 (`pull_request:` y `workflow_dispatch:`). `pull_request` ejecuta el workflow en cada PR, sobre el código del PR. Haz commit y súbelo a `main`:

```bash
git commit -am "Run the JS workflow on pull requests"
git push
```

Ahora rompe algo en una rama, como lo haría un cambio descuidado: quita el tope de la multa.

```bash
git switch -c remove-fee-cap
```

En `js/src/fees.js`, cambia la última línea de `lateFee`:

```js
  return daysLate * FEE_PER_DAY;
```

Haz commit, sube la rama y abre el PR:

```bash
git commit -am "Remove the late fee cap"
git push -u origin remove-fee-cap
gh pr create --title "Remove the late fee cap" --body "Prueba del lab 01: el workflow tiene que fallar."
# https://github.com/<tu-usuario>/ci-lab/pull/1
```

Espera a los checks del PR:

```bash
gh pr checks --watch
# test  fail  34s  https://github.com/ergrato-dev/ci-lab/actions/runs/44425501542/job/135229153580
echo $?
# 1
```

Y lee solo lo que falló:

```bash
gh run view 44425501542 --log-failed
```

```text
    ✓ 20 days late costs 10000
    ✕ 365 days late costs 10000 (1 ms)
  ● lateFee › 365 days late costs 10000
    Expected: 10000
    Received: 182500
Tests:       1 failed, 6 passed, 7 total
[ELIFECYCLE] Test failed. See above for more details.
##[error]Process completed with exit code 1.
```

Fíjate en el caso de 20 días: pasa, porque 20 × 500 es justo 10 000, el tope. Solo el caso de 365 detecta que el tope desapareció. Es el valor límite de la [semana 2](../../../week-02-casos_de_prueba_y_tecnicas_de_diseno/README.md): un caso en el borde no basta, hace falta uno al otro lado.

Abre el PR en el navegador (`gh pr view --web`). El check aparece en rojo, pero el botón de *merge* sigue activo: un check rojo no bloquea nada sin una regla en la rama ([teoría 01](../../1-teoria/01-ci-cd-y-anatomia-de-un-workflow.md#4-del-check-rojo-al-merge-bloqueado)).

## Paso 6: cerrar el PR

No vas a mezclar el cambio. Cierra el PR y borra la rama:

```bash
gh pr close 1 --delete-branch
# ✓ Deleted branch remove-fee-cap and switched to branch main
```

---

## Experimento: lanzarlo a mano

`workflow_dispatch` permite lanzar el workflow sin *push*, desde el botón *Run workflow* de la pestaña *Actions* o desde la terminal:

```bash
gh workflow run js
gh run list -w js --limit 1
# ✓  js  js  main  workflow_dispatch  44431618556  18s  ...
```

Sirve para repetir una suite inestable o para volver a ejecutarla después de un cambio fuera del repositorio (una imagen de Docker nueva, una dependencia publicada).

## Comprobación final

```bash
gh run list -w js --limit 5
```

El último run de `main` está en verde y el del PR en rojo. El archivo `.github/workflows/js.yml` descomentado entero es el que siguen usando los labs siguientes.

## Qué has aprendido

- Un workflow vive en `.github/workflows/` y se ejecuta con cada evento de `on`. Cada job empieza en una máquina vacía: `checkout`, instalar, ejecutar.
- `gh run list`, `watch --exit-status`, `view` y `view --log-failed` siguen un run sin salir de la terminal.
- `pull_request` pone el resultado como check en el PR. Rojo no es bloqueado.
- La caché se guarda al final de un job verde.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Matrix, caché y artifacts](../lab-02-matrix-cache-y-artifacts/README.md) → |

[↑ Semana 45](../../README.md)
