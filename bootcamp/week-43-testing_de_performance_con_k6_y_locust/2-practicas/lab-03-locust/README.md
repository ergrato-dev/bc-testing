# Lab 03 - Locust

## Objetivo

Escribir en Python los usuarios de la biblioteca: lectores que consultan y buscan, y socios que prestan y devuelven. Ejecutarlos con Locust sin interfaz, guardar el CSV y el informe HTML, y convertir los criterios de aceptación en el código de salida. Después, ver cuándo un 409 es un fallo y cuándo no.

## Tiempo estimado

60 minutos.

## Preparación

La API de la biblioteca arrancada ([índice de labs](../README.md#la-api-de-la-biblioteca)).

Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `pyproject.toml` | El proyecto `lab03`, con `locust==2.47.0` en `[dependency-groups] dev` ([teoría 03](../../1-teoria/03-locust.md#1-instalar)) |
| `.python-version` y `uv.lock` | Python 3.14 y las versiones exactas de las 37 dependencias |
| `locustfile.py` | La clase `Reader` sin tareas, y los PASOS comentados |

1. Instala las dependencias:

   ```bash
   cd starter
   uv sync
   uv run locust --version
   # locust 2.47.0 from .../starter/.venv/lib/python3.14/site-packages/locust (Python 3.14.10)
   ```

## Paso a paso

### Paso 1: un usuario sin tareas

```bash
uv run locust --headless -u 5 -r 5 -t 5s; echo "exit=$?"
# ... WARNING/root: Reader had no tasks left after filtering, instantiating it will fail!
# ... INFO/locust.runners: All users spawned: {"Reader": 5} (5 total users)
# ... ERROR/locust.user.task: No tasks defined on Reader. Use the @task decorator or set the 'tasks' attribute of the User (or mark it as abstract = True if you only intend to subclass it)
# Traceback (most recent call last):
# ...
# exit=1
```

| Opción | Qué hace |
|---|---|
| `--headless` | Sin interfaz web: arranca la prueba al momento |
| `-u 5` | 5 usuarios |
| `-r 5` | Arrancan 5 por segundo |
| `-t 5s` | La prueba dura 5 s |

Locust busca `locustfile.py` en la carpeta actual. Encuentra `Reader`, crea los 5 usuarios y cada uno falla al buscar su primera tarea.

### Paso 2: consultar y buscar (PASOS 1 y 2)

1. En `locustfile.py`, descomenta los bloques `# PASO 1` y `# PASO 2`, dentro de `Reader`.
2. Ejecuta 30 lectores durante 30 s, solo con el resumen final:

   ```bash
   uv run locust --headless -u 30 -r 10 -t 30s --only-summary; echo "exit=$?"
   # ... INFO/locust.runners: All users spawned: {"Reader": 30} (30 total users)
   # ... INFO/locust.main: --run-time limit reached, shutting down
   # ... INFO/locust.main: Shutting down (exit code 0)
   # Type     Name                  # reqs      # fails |    Avg     Min     Max    Med |   req/s  failures/s
   # --------|---------------------|-------|-------------|-------|-------|-------|-------|--------|-----------
   # GET      /api/books/{isbn}        141     0(0.00%) |      3       0      54      2 |    4.30        0.00
   # GET      /api/books?q=            439     0(0.00%) |     49      35     170     46 |   13.38        0.00
   # --------|---------------------|-------|-------------|-------|-------|-------|-------|--------|-----------
   #          Aggregated               580     0(0.00%) |     38       0     170     43 |   17.68        0.00
   #
   # Response time percentiles (approximated)
   # Type     Name                       50%    66%    75%    80%    90%    95%    98%    99%  99.9% 99.99%   100% # reqs
   # ...
   # GET      /api/books/{isbn}            2      2      2      2      3      4     54     54     55     55     55    141
   # GET      /api/books?q=               46     49     52     54     60     76    120    130    170    170    170    439
   # ...
   # exit=0
   ```

   | Pieza | Qué hace |
   |---|---|
   | `@task` y `@task(3)` | Por cada consulta, tres búsquedas: 141 frente a 439 |
   | `wait_time = between(1, 2)` | Entre 1 y 2 s de espera después de cada tarea: 30 usuarios dan unas 20 peticiones por segundo, no miles |
   | `name="/api/books/{isbn}"` | Una sola fila para las 141 consultas, cada una con un ISBN distinto |
   | `catch_response=True` | La búsqueda falla también si no trae 20 libros, aunque responda 200 ([teoría 03](../../1-teoria/03-locust.md#3-validar-respuestas)) |

   Números parecidos a los de k6 en el lab 02: consulta de 1–2 ms de mediana, búsqueda de 46–49 ms. Dos generadores distintos miden la misma API.

### Paso 3: dos tipos de usuario (PASO 3)

1. Descomenta el bloque `# PASO 3`: la clase `Member`.
2. Ejecuta:

   ```bash
   uv run locust --headless -u 30 -r 10 -t 30s --only-summary; echo "exit=$?"
   # ... INFO/locust.runners: All users spawned: {"Member": 6, "Reader": 24} (30 total users)
   # ...
   # GET      /api/books/{isbn}        117     0(0.00%) |      4       0      59      2 |    3.45        0.00
   # GET      /api/books?q=            344     0(0.00%) |     49      35     102     47 |   10.13        0.00
   # POST     /api/loans               119     0(0.00%) |      4       1      61      3 |    3.51        0.00
   # DELETE   /api/loans/{isbn}        119     0(0.00%) |      3       1      48      2 |    3.51        0.00
   # ...
   #          Aggregated               699     0(0.00%) |     26       0     102     37 |   20.59        0.00
   # ...
   # exit=0
   ```

   `weight = 4` en `Reader` y `weight = 1` en `Member`: de 30 usuarios, 24 lectores y 6 socios. Cada socio elige su nombre una vez, en `on_start`, y en cada tarea presta un libro al azar y lo devuelve. Si el préstamo responde 409 (otro socio lo tiene), lo marca como correcto con `res.success()` y no lo devuelve: no es suyo.

### Paso 4: los criterios de aceptación (PASO 4)

Sin criterios, Locust termina con código 0 si no hubo fallos y con 1 si hubo alguno ([teoría 03](../../1-teoria/03-locust.md#6-criterios-de-aceptación-y-código-de-salida)). La latencia no cuenta.

1. Descomenta el bloque `# PASO 4`: un *listener* de `events.quitting` con los mismos criterios del lab 02 (menos del 1 % de errores y p95 por debajo de 300 ms).
2. Ejecuta con 30 usuarios: termina con `exit=0`.
3. Ejecuta con 300:

   ```bash
   uv run locust --headless -u 300 -r 50 -t 30s --only-summary; echo "exit=$?"
   # ... INFO/locust.main: Shutting down (exit code 1)
   # ...
   #          Aggregated              4091     0(0.00%) |    729       1    1110    790 |  119.54        0.00
   # ...
   #          Aggregated                     790    840    880    900    940    990   1000   1100   1100   1100   1100   4091
   # FALLO: p95 de 990 ms (máximo 300 ms)
   # exit=1
   ```

   Ningún error, y la prueba falla: p95 de 990 ms. Con 300 usuarios, la API atiende unas 120 peticiones por segundo y el resto espera.

### Paso 5: la evidencia

```bash
uv run locust --headless -u 30 -r 10 -t 20s --only-summary --csv results/lab03 --html results/lab03.html
ls results
# lab03.html
# lab03_exceptions.csv
# lab03_failures.csv
# lab03_stats.csv
# lab03_stats_history.csv
```

| Archivo | Qué tiene |
|---|---|
| `lab03_stats.csv` | Una fila por petición y la fila `Aggregated`: las dos tablas del resumen |
| `lab03_stats_history.csv` | Una fila por segundo: usuarios, peticiones por segundo y percentiles. Con él se dibuja la curva del lab 04 |
| `lab03_failures.csv` y `lab03_exceptions.csv` | Los fallos y las excepciones de tus tareas |
| `lab03.html` | El informe: tablas y gráficas de peticiones por segundo, tiempos de respuesta y usuarios |

Abre `lab03.html` en el navegador (en WSL, `explorer.exe results/lab03.html`).

### Paso 6: la interfaz web

```bash
uv run locust
```

Abre `http://localhost:8089`, pon 50 usuarios y 10 por segundo, y pulsa *Start*. Las pestañas *Statistics* y *Charts* muestran lo mismo que el informe, en vivo. Sirve para explorar; la prueba que se repite y se entrega es la de `--headless`. Para Locust con `Ctrl+C`.

## Experimentos

### Experimento 1: un 409 que no es un fallo

1. En `Member.lend_and_return`, cambia `isbn = random_isbn()` por `isbn = "9780000000001"`: todos los socios piden el mismo libro.
2. Ejecuta `uv run locust --headless -u 30 -r 10 -t 15s --only-summary` y mira la fila `POST`:

   ```bash
   # POST     /api/loans                56     0(0.00%) |     10       1      74      3 |    3.76        0.00
   ```

   Algunos préstamos chocan (409), y `res.success()` los cuenta como correctos.

3. Cambia `res.success()` por `pass` y repite:

   ```bash
   # Error report
   # # occurrences      Error
   # 4                  POST /api/loans: HTTPError('409 Client Error: Conflict for url: /api/loans')
   # FALLO: 1.19% de errores (máximo 1 %)
   # exit=1
   ```

   Sin `success()` ni `failure()`, decide el código de estado, y un 409 es un fallo. Cuatro choques bastan para pasar del 1 %. Decidir qué es un error es parte del diseño de la prueba: un 409 de «ya prestado» es el comportamiento correcto de la API, no un fallo.

4. Deshaz los dos cambios.

### Experimento 2: el generador también mide

1. Cambia `wait_time = between(1, 2)` por `wait_time = constant(0)` en `Reader` (impórtalo de `locust`), y quita los PASOS 2 y 3 para que solo consulte libros.
2. Ejecuta 100 usuarios con `HttpUser` y después con `FastHttpUser` (cambia la clase base e impórtala). Compara la mediana y las peticiones por segundo con las de la [teoría 03](../../1-teoria/03-locust.md#7-k6-locust-o-jmeter).

   La API es la misma. Si la mediana cambia al cambiar de cliente, parte de lo que mides es Locust.

3. Deshaz los cambios.

## Comprobación final

Con los cuatro PASOS descomentados:

```bash
uv run locust --headless -u 30 -r 10 -t 30s --only-summary; echo "exit=$?"
# ...
# exit=0
```

## Preguntas de cierre

1. ¿Qué diferencia hay entre `@task(3)` y `weight = 4`?
2. En el Paso 4, con 300 usuarios, no hubo ningún fallo y la prueba falló. ¿Qué habría pasado sin el PASO 4?
3. ¿Cuándo es correcto que una prueba de carga cuente un 4xx como éxito?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Umbrales, etapas y etiquetas](../lab-02-umbrales-etapas-y-etiquetas/README.md) | [Lab 04 - El cuello de botella](../lab-04-cuello-de-botella/README.md) → |

[↑ Semana 43](../../README.md)
