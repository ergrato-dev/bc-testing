# Lab 01 - Primer proyecto con Jest

## Objetivo

Montar desde cero el proyecto donde harás los cuatro labs de la semana, ver un test en rojo por un bug real, arreglarlo y distinguir un fallo de aserción de un error de ejecución.

## Tiempo estimado

60 minutos.

## Requisitos

- La [teoría 01](../../1-teoria/01-setup-jest.md) y la [02](../../1-teoria/02-estructura-test-jest.md).
- Node.js 22 instalado (`node -v` muestra `v22.algo`) y git.

## Punto de partida

La carpeta `~/labs-semana-03` es el proyecto de los cuatro labs. Créala y copia el código de este lab:

```bash
mkdir ~/labs-semana-03
cd ~/labs-semana-03
cp -r <ruta-del-bootcamp>/bootcamp/week-03-primeros_tests_automatizados_con_javascript/2-practicas/lab-01-primer-proyecto-jest/starter/. .
ls -R src tests
# src:
# math.js
#
# tests:
# math.test.js
```

`cp -r origen/. .` copia el contenido de `starter/` (no la carpeta) en la carpeta actual.

---

## Paso 1: pnpm (5 min)

Si no lo has hecho nunca en este equipo, activa pnpm:

```bash
corepack enable pnpm
```

Crea `package.json` en `~/labs-semana-03` con tu editor:

```json
{
  "name": "labs-semana-03",
  "private": true,
  "packageManager": "pnpm@12.6.0",
  "scripts": {
    "test": "jest"
  }
}
```

Comprueba que corepack usa la versión del archivo (la primera vez pregunta si descarga pnpm; responde `Y`):

```bash
pnpm -v
# 12.6.0
```

## Paso 2: instalar Jest (10 min)

```bash
pnpm add -D jest@30.5.2
```

Termina con `ERR_PNPM_IGNORED_BUILDS` y crea `pnpm-workspace.yaml`. Es lo esperado: la [teoría 01](../../1-teoria/01-setup-jest.md#instalar-jest) explica por qué. Abre `pnpm-workspace.yaml`, cambia `set this to true or false` por `false` y vuelve a instalar:

```bash
cat pnpm-workspace.yaml
# allowBuilds:
#   '@parcel/watcher': false
pnpm install
# Lockfile is up to date, resolution step is skipped
# Already up to date
# Done in 58ms using pnpm v12.6.0
```

`cat` muestra el contenido de un archivo.

## Paso 3: rojo (10 min)

Abre `tests/math.test.js`. Tiene un test activo y dos comentados. Ejecuta:

```bash
pnpm test
```

```text
$ jest
FAIL tests/math.test.js
  math
    ✕ should return 5 when adding 2 and 3 (4 ms)

  ● math › should return 5 when adding 2 and 3

    expect(received).toBe(expected) // Object.is equality
```

Lee el resto del informe como en la [teoría 02](../../1-teoria/02-estructura-test-jest.md#4-leer-un-test-que-falla) y contesta en voz alta o en una nota:

- ¿Qué valores dan `Expected` y `Received`?
- ¿En qué línea del test está el `expect` que falla?
- ¿El fallo está en el código o en el test? ¿Por qué?

## Paso 4: verde (5 min)

Abre `src/math.js` y sigue el comentario `PASO 1`: borra `return a - b;` y descomenta `return a + b;`. Ejecuta `pnpm test`: el test pasa.

## Paso 5: dos tests más (10 min)

En `tests/math.test.js`, descomenta los bloques `PASO 2` y `PASO 3`: quita el `// ` del principio de cada línea del test, incluidas `it(...` y `});`. En VS Code, selecciona las líneas y pulsa `Ctrl+/` (`Cmd+/` en macOS).

Antes de ejecutar, escribe qué esperas que salga. Después:

```bash
pnpm test
```

```text
$ jest
PASS tests/math.test.js
  math
    ✓ should return 5 when adding 2 and 3 (2 ms)
    ✓ should return true when number is even
    ✓ should return false when number is odd

Test Suites: 1 passed, 1 total
Tests:       3 passed, 3 total
Snapshots:   0 total
Time:        0.295 s, estimated 1 s
Ran all test suites.
```

Los tests de `isEven` usan `toBeTruthy` y `toBeFalsy`. La [teoría 03](../../1-teoria/03-matchers-y-ejecucion.md#tobetruthy-tobefalsy) explica por qué `toBe(true)` comprueba más; cámbialos y vuelve a ejecutar.

## Paso 6: un error de ejecución (10 min)

En la primera línea de `tests/math.test.js`, cambia `"../src/math"` por `"../src/maths"` y ejecuta `pnpm test`:

```text
FAIL tests/math.test.js
  ● Test suite failed to run

    Cannot find module '../src/maths' from 'tests/math.test.js'

    > 1 | const { add, isEven } = require("../src/maths");
        |                         ^

Test Suites: 1 failed, 1 total
Tests:       0 total
```

Compáralo con el paso 3: no hay `Expected` ni `Received` y `Tests: 0 total`, porque el archivo no llegó a ejecutar ningún test. Deshaz el cambio.

## Paso 7: git y modo watch (10 min)

Prueba el modo watch:

```bash
pnpm test --watch
# $ jest --watch
#
# --watch is not supported without git/hg, please use --watchAll
```

Convierte la carpeta en un repositorio git. `.gitignore` dice qué no se guarda en git: `node_modules` se regenera con `pnpm install`.

```bash
git init
echo "node_modules/" > .gitignore
git add .
git commit -m "lab 01: primer proyecto con Jest"
```

Si `git commit` responde `Please tell me who you are`, git no sabe tu nombre todavía. Díselo una vez por equipo y repite el commit:

```bash
git config --global user.name "Tu Nombre"
git config --global user.email "tu-correo@example.com"
```

Vuelve a lanzar `pnpm test --watch`. Con la terminal y el editor a la vista, cambia `toBe(5)` por `toBe(6)` en el test y guarda: Jest vuelve a ejecutar solo y el test sale en rojo. Deshazlo, guarda, y sal con `q`.

El lab 04 usará git para deshacer cambios en el código.

---

## Comprobación final

```bash
pnpm test 2>&1 | grep '^Tests:'
# Tests:       3 passed, 3 total
git log --oneline | wc -l
# 1
```

`2>&1` junta los mensajes de error con la salida normal, para que `grep` los vea todos.

Repasa:

- [ ] `package.json` tiene `packageManager` con `pnpm@12.6.0` y `jest` en `devDependencies` con `30.5.2`.
- [ ] `pnpm-workspace.yaml` tiene `'@parcel/watcher': false`.
- [ ] Sabes decir, viendo un informe, si es un fallo de aserción o un error de ejecución.

## Qué has aprendido

- Un proyecto de tests en JavaScript son tres archivos de configuración (`package.json`, `pnpm-workspace.yaml` y el lockfile) más `src/` y `tests/`.
- Un test en rojo te dice qué valor esperaba, cuál recibió y en qué línea.
- El modo watch acorta el ciclo de cambiar y comprobar, y necesita git.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - AAA y matchers](../lab-02-aaa-y-matchers/README.md) → |

[↑ Semana 03](../../README.md)
