# Proyecto - Suite modular con Jest

> Entregable obligatorio de la semana 07. Primer incremento de la Etapa 1 sobre el proyecto de la semana 6.

## Objetivo

Reorganizar la suite de JS de tu dominio con `describe` por comportamiento, añadir un servicio que guarda estado y avisa al cliente, y probarlo con `beforeEach`, un stub del reloj, un mock del notificador y un spy, sin romper la trazabilidad de la semana 6.

## Tiempo estimado

3 horas.

## Requisitos

- Tu proyecto de la [semana 6](../../week-06-plan_de_pruebas_y_suite_base_multilenguaje/3-proyecto/README.md) en `~/proyecto-semana-06`, con `./verify.sh` en verde.
- Los [labs](../2-practicas/README.md) de esta semana: el proyecto repite sus pasos con tu dominio.

## Alcance

Solo cambia `js/`, más el plan. Python y Java no se tocan: el servicio de esta semana es solo de JS, y en el plan sus casos llevan — en esas dos columnas, con su motivo.

---

## Paso 1: punto de partida (15 min)

Copia el proyecto de la semana 6 con su historial y añade las piezas de esta semana a `js/`:

```bash
cp -r ~/proyecto-semana-06 ~/proyecto-semana-07
cd ~/proyecto-semana-07
cp -r <ruta-del-bootcamp>/bootcamp/week-07-jest_avanzado_y_organizacion_de_suites/3-proyecto/starter/js/. js/
git status --short
# ?? js/src/clock.js
# ?? js/src/notifier.js
# ?? js/src/service.js
# ?? js/tests/notifier.test.js
# ?? js/tests/service.test.js
```

| Archivo | Qué es |
|---|---|
| `js/src/clock.js` | `systemClock`, el reloj real, igual que en los labs |
| `js/src/notifier.js` | `emailNotifier`, el notificador real, igual que en los labs |
| `js/src/service.js` | El servicio de tu dominio, con `TODO` |
| `js/tests/service.test.js` | Su suite, con los `describe`, el `beforeEach` y los tests como `it.todo` |
| `js/tests/notifier.test.js` | La suite del notificador, con `TODO` |

`service.js` importa tus funciones de `./domain`, el archivo del starter de la semana 3. Si las tienes en otro archivo, cambia el `require` ahora. Después:

```bash
cd js
pnpm test
# Tests:       7 todo, 16 passed, 23 total
cd ..
./verify.sh
# ...
# OK: tres suites en verde y trazabilidad completa
```

Tu número de tests será otro; lo importante es que no haya ninguno `failed` ni un `FAIL` en la salida. `verify.sh` sigue en verde porque solo cuenta los `it(`, y los `it.todo` no lo son. Guarda el punto de partida:

```bash
git add .
git commit -m "proyecto semana 07: punto de partida"
```

## Paso 2: ordenar la suite del dominio (40 min)

Reorganiza el archivo de tests de tus funciones de la semana 3 como en el [lab 01](../2-practicas/lab-01-describe-por-comportamiento/README.md): el módulo, la función y los contextos `when ...`, con cada `it` empezando por `should`. Una función con un solo test no necesita contexto.

El comentario con el ID se mueve con su test: tiene que seguir en la línea de encima del `it`.

```bash
cd js
pnpm test --verbose
grep -rn "\.only(\|\.skip(" tests/
cd ..
./verify.sh
# OK: tres suites en verde y trazabilidad completa
```

Mira el informe de `--verbose`: tiene que leerse como las reglas de tu plan. Si un contexto no corresponde a ninguna clase o límite de tus casos de la semana 2, revisa el nombre.

`verify.sh` no te avisa de un `.only` olvidado: Jest termina con código 0 y `set -e` no tiene nada que parar. Por eso el `grep` va antes de cada commit.

## Paso 3: el servicio (35 min)

Renombra `service.js` con el nombre de tu dominio (por ejemplo, `booking-service.js` y `createBookingService`) y completa sus `TODO`:

- `create` valida con tus funciones de la semana 3, comprueba la fecha con `clock.today()`, guarda y, **solo después**, avisa con `notifier.send(destinatario, mensaje)`.
- `cancel` lanza un error si no existe; si existe, la quita y avisa.

```bash
git mv js/src/service.js js/src/booking-service.js
```

`git mv` renombra el archivo y se lo dice a git. Cambia también el `require` de `js/tests/service.test.js` (y renombra ese archivo igual, con `git mv`).

En el plan, añade a la sección 2 las reglas nuevas (qué se guarda, a quién se avisa y con qué mensaje, qué pasa al cancelar) y a la sección 3 sus riesgos, con impacto y probabilidad. Piensa en el peor: avisar de algo que no se guardó.

## Paso 4: tests del servicio (45 min)

Completa la suite del servicio. El `beforeEach` del starter ya crea, para cada test, tres cosas nuevas:

- `clock`: un stub con `jest.fn().mockReturnValue(...)`. Elige una fecha y calcula tus casos a partir de ella.
- `notifier`: un mock con `send: jest.fn()`.
- `service`: el servicio, con los dos anteriores.

Como mínimo:

| Contexto | Qué comprueba el test |
|---|---|
| Datos válidos | Se guarda (`list()`) |
| Datos válidos | Se avisa una vez, con `toHaveBeenCalledWith(destinatario, mensaje)` exactos |
| Una regla de tu dominio no se cumple | Lanza el error y `expect(notifier.send).not.toHaveBeenCalled()` |
| La fecha es hoy (según el stub) | Lanza el error y no avisa |
| Cancelar algo que existe | Se quita y se avisa (`toHaveBeenLastCalledWith`) |
| Cancelar algo que no existe | Lanza el error |

Los tests de «cancelar algo que existe» necesitan algo creado: ponlo en un `beforeEach` dentro de ese contexto, como en el [lab 02](../2-practicas/lab-02-hooks-y-estado-compartido/README.md#paso-4-un-hook-dentro-de-un-contexto-15-min).

Cada test lleva encima su ID, en una línea sola. Usa números nuevos, a continuación del último de tu matriz.

## Paso 5: spy del notificador (15 min)

Completa `js/tests/notifier.test.js` como en el [lab 03](../2-practicas/lab-03-stubs-mocks-y-spies/README.md#paso-4-spy-del-notificador-real-20-min): un `jest.spyOn(console, "log").mockImplementation(() => {})`, el `toHaveBeenCalledWith` con el texto exacto, y `jest.restoreAllMocks()` en un `afterEach`.

## Paso 6: aislamiento (10 min)

```bash
cd js
pnpm test --randomize --seed=1
pnpm test --randomize --seed=2
pnpm test --randomize --seed=3
```

Las tres tienen que pasar. Después, comprueba que tus tests detectan el bug de los labs: en tu servicio, mueve el `notifier.send` a la primera línea de `create` y ejecuta `pnpm test`. Tienen que fallar, como mínimo, el test de «una regla no se cumple» y el de «la fecha es hoy». Si alguno pasa, no está comprobando el aviso. Devuelve la línea a su sitio.

## Paso 7: el plan (15 min)

- **Sección 6**: un caso por test nuevo, con su prioridad (la del riesgo que cubre), ✅ en JS y — en Python y Java.
- **Sección 7**: el motivo de esos —, con el ID de cada caso escrito entero. El servicio solo existe en JS; Python y Java lo tendrán en sus etapas.
- **Sección 5**: el criterio «todos los casos de prioridad Alta en los tres lenguajes» ya no es cierto. Cámbialo por uno que se pueda cumplir y comprobar: los Alta, en todos los lenguajes donde existe su código.
- **Sección 4**: una frase sobre los dobles: qué es stub, qué es mock y qué prueba el spy.
- **Sección 8**: lo que encontraste al escribir el servicio. Guardar estado suele destapar reglas que faltaban.

## Paso 8: verify.sh y entrega (5 min)

```bash
cd ~/proyecto-semana-07
./verify.sh
# OK: tres suites en verde y trazabilidad completa
```

Copia el resumen en la sección 9 con la fecha, como en la semana 6, y comprueba la entrega:

```bash
PROYECTO=~/proyecto-semana-07 <ruta-del-bootcamp>/scripts/verificar-semana.sh 07
git add .
git commit -m "proyecto semana 07: suite modular y servicio con dobles"
```

---

## Ejemplo

El **acuario virtual** de las semanas anteriores. Su servicio de reservas guarda cada reserva con el código de la semana 3 (`ACUA-AAAAMMDD-APELLIDO`) y avisa al titular. Su suite, con el stub del reloj fijado en el 18 de noviembre de 2026:

```bash
pnpm test --verbose tests/booking-service.test.js
# PASS tests/booking-service.test.js
#   createBookingService
#     book
#       when the booking is valid
#         ✓ should store it with its code and price (3 ms)
#         ✓ should send one confirmation to the holder (1 ms)
#       when the group breaks a rule
#         ✓ should throw and neither store nor notify (4 ms)
#       when the date is today
#         ✓ should throw and neither store nor notify (3 ms)
#       when the code already exists
#         ✓ should throw and notify only the first booking (1 ms)
#     cancel
#       when the booking exists
#         ✓ should remove it and tell the holder (1 ms)
#       when the booking does not exist
#         ✓ should throw (1 ms)
#
# Tests:       7 passed, 7 total
```

El contexto `when the code already exists` salió al escribir el servicio. Dos titulares con el mismo apellido, el mismo día:

```bash
node -e 'const { formatBookingCode } = require("./src/tour"); console.log(formatBookingCode("Lucía Ruiz", "2026-11-19")); console.log(formatBookingCode("Pablo Ruiz", "2026-11-19"))'
# ACUA-20261119-RUIZ
# ACUA-20261119-RUIZ
```

Con funciones sueltas daba igual. Con reservas guardadas, cancelar `ACUA-20261119-RUIZ` no sabría cuál anular. El plan lo registra como hueco, añade una regla (la segunda reserva se rechaza, sin aviso) y su caso:

```markdown
| TC-ACUA-TOUR-029 | R8 | Segunda reserva con el mismo código: se rechaza sin aviso | Media | ✅ | — | — |
```

Tu entrega usa tu dominio, tus reglas y tus casos.

---

## ✅ Requisitos verificables

Copia legible de [checks.json](../checks.json). Todos se ejecutan sobre `js/`, salvo los dos del plan y `verify`:

| Comprobación | Qué mira |
|---|---|
| js-pasa | `pnpm install --frozen-lockfile` y `pnpm test` terminan con código 0, sin tests `todo`, saltados ni fallidos |
| sin-focos | Ningún archivo de `tests/` tiene `.only(`, `.skip(`, `fit(`, `xit(`, `fdescribe(` ni `xdescribe(` |
| contextos | 6 `describe("when ...` o más, con 4 espacios de sangría o más (dentro de otro `describe`) |
| nombres | Todos los `it(` y `test(` empiezan por `should ` |
| before-each | Hay al menos un `beforeEach(` |
| aislamiento | `pnpm test --randomize` pasa con `--seed=1`, `2` y `3` |
| stub | Algún test usa `mockReturnValue` |
| mock-llamada | Algún test usa `toHaveBeenCalledWith(` |
| mock-sin-llamada | Algún test usa `not.toHaveBeenCalled(` |
| spy | Hay un `jest.spyOn` y se restaura (`restoreAllMocks`, `mockRestore` o `"restoreMocks": true` en `package.json`) |
| servicio-en-matriz | 5 filas `TC-` o más terminan en `✅ \| — \| —` |
| motivos | El ID de cada fila con — aparece en la sección 7 |
| verify | `./verify.sh` termina con código 0 |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - Stubs, mocks y spies](../2-practicas/lab-03-stubs-mocks-y-spies/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 07](../README.md)
