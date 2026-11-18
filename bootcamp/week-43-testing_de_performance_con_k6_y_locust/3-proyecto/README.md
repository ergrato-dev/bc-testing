# Proyecto - Rendimiento de la API del dominio con k6 y Locust

## Objetivo

Añadir a tu repositorio `qa-<nombre>-<apellido>` la carpeta `perf/`: una prueba de carga con k6 y un stress test con Locust contra la API Go de tu dominio ([semana 34](../../week-34-http_bd_e_integracion_en_go/3-proyecto/README.md)), con criterios de aceptación escritos antes de medir, los resultados guardados como evidencia y un informe que dice si la API los cumple, hasta dónde aguanta y qué la limita.

## Tiempo estimado

3 horas.

## Punto de partida

La API Go de la semana 34 tiene `NewHandler` y un almacén `pgstore`, pero ningún programa que la arranque: los tests la ejecutaban con `httptest`. `starter/` trae ese programa y todo lo que rodea a las pruebas.

| Archivo | Qué es | Qué haces |
|---|---|---|
| `go/cmd/server/main.go` | Arranca `NewHandler` con `pgstore` y un pool de `pgxpool`, aplica las migraciones y sustituye las dependencias externas (registro y notificador del museo) por versiones que no llaman a nada | Lo adaptas a tus paquetes y a tu servicio (paso 2) |
| `perf/run.sh` | Arranca PostgreSQL, crea de cero la base de datos `museum_perf`, arranca la API, carga los datos y ejecuta la prueba: `smoke`, `load` o `stress` | Nada, salvo las variables si tu `compose.yaml` usa otro usuario |
| `perf/seed.sql` | 20 000 piezas del museo | Lo cambias por tus datos (paso 3) |
| `perf/k6/load.js` | La estructura de la prueba de carga con un `default` de ejemplo | Escribes los escenarios y los umbrales (paso 5) |
| `perf/locust/` | Proyecto uv con Locust 2.47.0, `locustfile.py` con la forma escalonada y `resumen.py` (el del [lab 04](../2-practicas/lab-04-cuello-de-botella/README.md)) | Escribes el usuario (paso 6) |
| `perf/performance-report.md` | La plantilla del informe | La completas (pasos 4 y 7) |
| `perf/.gitignore` | Excluye el binario, el log de la API y el entorno de Python | Nada |

## Paso a paso

### Paso 1: la carpeta `perf/`

1. Crea la rama de la semana:

   ```bash
   git switch -c week-43
   ```

2. Copia `starter/go/cmd/` dentro de tu `go/` y `starter/perf/` en la raíz del repositorio, al lado de `compose.yaml`.
3. Comprueba las herramientas: k6 ([teoría 02](../1-teoria/02-k6.md#1-instalar-k6)), uv, Go y Docker.

   ```bash
   k6 version
   # k6 v2.3.0 (commit/e088784614, go1.26.8, linux/amd64)
   cd perf/locust && uv sync && cd ../..
   ```

4. Abre `compose.yaml`. `perf/run.sh` usa el usuario `museum` con contraseña `museum` y el puerto 55432, los de la [semana 23](../../week-23-testing_de_bases_de_datos_en_python/3-proyecto/README.md). Si cambiaste el usuario o la contraseña, ejecuta los comandos de esta semana con `DB_USER=... DB_PASSWORD=...` delante.

Las pruebas usan su propia base de datos, `museum_perf`, que `run.sh` borra y crea en cada ejecución. Así cada prueba parte de los mismos datos, y tus otras suites (las tablas de Alembic de Python, las de Go) no se tocan.

### Paso 2: el servidor

En `go/cmd/server/main.go`, sigue los `TODO`:

1. Los `import` de tu paquete de dominio y de `pgstore`, con la ruta de tu `go.mod`.
2. Las dependencias de tu servicio que llaman a otro sistema. En el museo, `Registry` (¿la institución está acreditada?) y `Notifier` (enviar las condiciones del préstamo): `acceptAll` acredita a todas y `discard` no envía nada. Una prueba de carga mide tu API y tu base de datos; si llamara al servicio real de otro equipo, también lo estarías cargando a él. Lo que queda fuera se dice en el informe.
3. El servicio con sus campos.

```bash
cd go
go vet ./cmd/server
go build -o /dev/null ./cmd/server
cd ..
```

El servidor lee `DATABASE_URL`, `PORT` (18080) y `DB_MAX_CONNS`; `run.sh` le da las dos primeras.

### Paso 3: los datos y la prueba de humo

1. En `perf/seed.sql`, inserta 20 000 elementos de tu dominio con `generate_series`, como `db/init.sql` de la [API de los labs](../2-practicas/biblioteca-api/db/init.sql). El código de cada uno tiene que cumplir tus reglas de validación y salir de su número `n`, porque k6 y Locust lo calculan igual: en el museo, el elemento 0 es `AAA-000` y el 19 999, `AAT-999`.
2. Cambia `code(n)` en `perf/k6/load.js` y en `perf/locust/locustfile.py` para que calculen lo mismo, y la ruta de `setup()` por la de tu API.
3. Ejecuta la prueba de humo desde la raíz del repositorio:

   ```bash
   perf/run.sh smoke
   # == PostgreSQL y base de datos museum_perf
   # ...
   # == API en http://localhost:18080
   # == Datos de prueba
   # ...
   #   █ TOTAL RESULTS
   # ...
   # ... INFO/locust.main: Shutting down (exit code 0)
   # ...
   ```

   Si `setup()` lanza `sin datos de prueba`, el código de k6 no coincide con el de `seed.sql`. Si la API no arranca, `perf/results/server.log` dice por qué.

### Paso 4: los criterios de aceptación

Antes de medir nada, completa las secciones 1 y 2 de `perf/performance-report.md` ([teoría 04](../1-teoria/04-linea-base-criterios-y-cuellos-de-botella.md#1-criterios-de-aceptación)). Un criterio por petición que vas a probar: qué petición, qué métrica, qué límite y con qué carga. Al menos:

- la tasa de errores;
- el p95 de la petición de lectura;
- el p95 de la petición que escribe.

Haz commit del informe con los criterios antes del paso 5: el historial demuestra que no los ajustaste a los resultados.

### Paso 5: la prueba de carga con k6

En `perf/k6/load.js`, sustituye el `default` de ejemplo por dos escenarios ([teoría 02](../1-teoria/02-k6.md#3-las-opciones-cuánta-carga-y-con-qué-forma)), cada uno con su función (`exec`):

| Escenario | *Executor* | Qué hace cada iteración |
|---|---|---|
| Lectura | `ramping-vus`: subida, meseta de al menos 60 s, bajada | Lee un elemento al azar, con `tags: { name: '...' }`, un `check` del código y del contenido, y `sleep` |
| Escritura | `constant-arrival-rate`: unas pocas operaciones por segundo durante toda la prueba | La operación que cambia el estado (en el museo, prestar), sobre un elemento distinto cada vez |

Para que cada escritura use un elemento distinto, `exec.scenario.iterationInTest` (del módulo `k6/execution`) numera las iteraciones del escenario desde 0. En el museo, `code(PIECES - 1 - exec.scenario.iterationInTest)`: la primera presta la última pieza, la segunda la penúltima. Si dos iteraciones escribieran sobre el mismo elemento, la segunda recibiría un 409 que no es un fallo de la API sino de la prueba.

Con `SMOKE`, los dos escenarios duran unos segundos con 1 usuario: es lo que ejecuta `run.sh smoke`.

Los umbrales son tus criterios del paso 4, uno por petición con su `name`, más `checks` y `dropped_iterations: ['count==0']`. Este último exige que el escenario de ritmo fijo pudo enviar todas sus peticiones: si la API se vuelve tan lenta que k6 se queda sin VUs, la prueba falla en lugar de enviar menos carga sin avisar ([teoría 01](../1-teoria/01-performance-testing.md#5-modelo-cerrado-y-modelo-abierto)).

```bash
k6 inspect perf/k6/load.js | jq -c '{scenarios: (.scenarios | keys), thresholds}'
# {"scenarios":["loans","visitors"],"thresholds":{"checks":["rate>0.99"],"dropped_iterations":["count==0"],"http_req_duration{name:loan}":["p(95)<200"],"http_req_duration{name:piece}":["p(95)<100"],"http_req_failed":["rate<0.01"]}}
perf/run.sh smoke
perf/run.sh load
```

`run.sh load` guarda `perf/results/load-summary.json` y `perf/results/load-report.html`. Comprueba en el resumen que ningún umbral por etiqueta vale `0s`: sería una etiqueta mal escrita ([lab 02, experimento 2](../2-practicas/lab-02-umbrales-etapas-y-etiquetas/README.md#experimento-2-sin-la-etiqueta-name)).

### Paso 6: el stress test con Locust

En `perf/locust/locustfile.py` ([teoría 03](../1-teoria/03-locust.md)):

1. El usuario lee un elemento al azar, con `name=` para agrupar las URLs y `catch_response=True` para fallar también si la respuesta trae otro elemento.
2. Los escalones por defecto de `StepLoad`: que la carga ofrecida pase de lo que esperas que aguante tu API. Con una espera media de 1 s, cada usuario ofrece casi una petición por segundo. En el museo, de 200 en 200 usuarios cada 30 s, hasta 1200.

No le pongas criterios de aceptación: un stress test busca dónde se rompe, y `run.sh stress` no lo da por fallido si hay errores.

```bash
perf/run.sh stress
cd perf/locust
uv run python resumen.py ../results/stress_stats_history.csv
cd ../..
```

Mientras dura, en otra terminal, mira quién usa la CPU: `top` muestra la API (`server`), Locust (`python`) y PostgreSQL. Si Locust se acerca al 100 % de un núcleo, el límite que mides es el suyo ([teoría 04](../1-teoria/04-linea-base-criterios-y-cuellos-de-botella.md#4-el-entorno-también-se-mide)). Anota los porcentajes: van al informe.

### Paso 7: el informe

Completa `perf/performance-report.md` con los números de `perf/results/`:

| Sección | Qué lleva |
|---|---|
| 3. Prueba de carga | El perfil, una tabla por petición (p50, p95, peticiones por segundo, errores) y cada umbral con ✓ o ✗ |
| 4. Stress test | La tabla de `resumen.py` y la lectura: dónde está la rodilla, o hasta qué carga llegaste sin encontrarla |
| 5. Cuello de botella | El recurso que limita, con los datos que lo demuestran. «No lo encontré porque el generador llegó antes a su límite» también es un resultado, si lo demuestras con la CPU |
| 6. Riesgos | Lo que no cubren estas pruebas: los servicios externos sustituidos, el mismo equipo para todo, el volumen de datos |

### Paso 8: verificar y entregar

```bash
cd ~/bc-testing
PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 43
```

Haz commit de `go/cmd/server/` y de `perf/` con `perf/results/` (sin `server.log`, que excluye `.gitignore`), sube la rama y abre el pull request `week-43` → `main`. En la descripción, pega la sección 5 del informe.

## ✅ Requisitos verificables

Los comprueba `scripts/verificar-semana.sh 43` ([checks.json](../checks.json)):

| Comprobación | Paso |
|---|---|
| `perf/run.sh smoke` arranca la API con sus datos y pasa k6 y Locust | 2 y 3 |
| La prueba de carga tiene al menos dos escenarios, uno de ellos de ritmo fijo (`constant-arrival-rate`) | 5 |
| La prueba de carga tiene umbrales de errores y de latencia por petición (al menos dos `http_req_duration{name:...}`) | 5 |
| El stress test tiene un usuario con `catch_response` y una forma de carga (`LoadTestShape`) | 6 |
| Los resultados de la prueba de carga están en `perf/results/`: el resumen JSON y el informe HTML | 5 |
| Los resultados del stress test están en `perf/results/`: el informe HTML y al menos 60 s de historia | 6 |
| El informe tiene sus seis secciones | 4 y 7 |
| No queda ningún `TODO` en `perf/` ni en `go/cmd/server/` | 2 a 7 |

La primera comprobación necesita Docker, Go, k6 y uv: ejecuta `run.sh smoke` de verdad.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - El cuello de botella](../2-practicas/lab-04-cuello-de-botella/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 43](../README.md)
