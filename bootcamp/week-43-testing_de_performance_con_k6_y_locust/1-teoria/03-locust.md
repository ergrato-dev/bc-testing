# 03 - Locust

> Herramienta: **Locust** 2.47.0, en Python 3.14 con uv

**Locust** genera carga con usuarios escritos en Python. Cada usuario es una clase con tareas; Locust crea tantas instancias como le pidas y las ejecuta a la vez con *gevent*, una biblioteca de corrutinas que permite tener miles de usuarios en un solo proceso. Lo que en k6 era una iteración de `default`, en Locust es una tarea elegida al azar entre las del usuario.

---

## 🎯 Objetivos

- Instalar Locust con uv y escribir un `locustfile.py` con usuarios, tareas, pesos y esperas.
- Validar respuestas con `catch_response` y agrupar URLs con `name`.
- Ejecutar Locust sin interfaz, con el CSV y el informe HTML como evidencia.
- Dar forma a la carga con `LoadTestShape` y convertir los criterios de aceptación en el código de salida.
- Elegir entre k6, Locust y JMeter.

---

## 1. Instalar

Locust es una dependencia de desarrollo del proyecto, como pytest en la [semana 16](../../week-16-fundamentos_con_pytest/README.md):

```bash
uv add --dev locust==2.47.0
uv run locust --version
# locust 2.47.0 from .../.venv/lib/python3.14/site-packages/locust (Python 3.14.10)
```

`uv add --dev` lo escribe en `[dependency-groups] dev` de `pyproject.toml` y fija la versión exacta en `uv.lock`. Locust trae Flask para su interfaz web y gevent para la concurrencia: 37 paquetes en total.

---

## 2. El `locustfile.py`

```python
import random

from locust import HttpUser, between, task


class Reader(HttpUser):
    """Un lector: consulta libros y busca por título."""

    host = "http://localhost:18043"
    wait_time = between(1, 2)
    weight = 4

    @task
    def book(self):
        isbn = f"978{random.randint(1, 100_000):010d}"
        self.client.get(f"/api/books/{isbn}", name="/api/books/{isbn}")

    @task(3)
    def search(self):
        self.client.get("/api/books", params={"q": "faro gris"}, name="/api/books?q=")
```

| Pieza | Qué / Para qué |
|---|---|
| `HttpUser` | Un usuario con `self.client`, una sesión HTTP de `requests` que mide cada petición |
| `host` | La URL base. `--host` en la línea de comandos la sustituye |
| `@task` y `@task(3)` | Las tareas. El número es el peso: `search` se elige tres veces más que `book` |
| `wait_time` | La espera después de cada tarea: `between(1, 2)` (al azar entre 1 y 2 s), `constant(1)`, `constant_pacing(2)` (una tarea cada 2 s, dure lo que dure) o `constant_throughput(0.5)` (0.5 tareas por segundo por usuario) |
| `weight` | Con varias clases de usuario, la proporción de cada una: con `Reader` a 4 y `Member` a 1, de 30 usuarios salen 24 lectores y 6 socios |
| `name=` | El nombre de la fila en las estadísticas. Sin él, cada ISBN sería una fila |
| `on_start` / `on_stop` | Se ejecutan una vez por usuario, al crearlo y al pararlo: iniciar sesión, elegir un socio |

Comparado con k6 ([teoría 02](02-k6.md#2-el-script)):

| k6 | Locust |
|---|---|
| Una función `default` que hace todo en orden | Varias tareas; cada vez se elige una al azar según su peso |
| `sleep(1)` dentro del script | `wait_time` después de cada tarea |
| `tags: { name: 'search' }` | `name="/api/books?q="` |
| VUs | Usuarios. Siempre modelo cerrado ([teoría 01](01-performance-testing.md#5-modelo-cerrado-y-modelo-abierto)) |

---

## 3. Validar respuestas

Locust cuenta como fallo cualquier respuesta 4xx o 5xx, o una conexión que falla. Para decidir tú qué es un fallo, abre la petición con `catch_response=True`:

```python
with self.client.post("/api/loans", json={"isbn": isbn, "member": self.member},
                      catch_response=True) as res:
    if res.status_code == 409:
        res.success()       # otro socio lo tiene prestado: es una respuesta correcta
    elif res.status_code == 201 and res.json()["isbn"] != isbn:
        res.failure("the loan is for another book")
```

Dentro del `with`, `res.success()` y `res.failure("motivo")` deciden. Si no llamas a ninguno, decide el código de estado. El motivo aparece en el informe de errores:

```text
Error report
# occurrences      Error
4                  POST /api/loans: HTTPError('409 Client Error: Conflict for url: /api/loans')
```

---

## 4. Ejecutar

| Modo | Comando | Para qué |
|---|---|---|
| Interfaz web | `uv run locust` y abre `http://localhost:8089` | Explorar: eliges usuarios y ritmo, y ves las gráficas en vivo |
| Sin interfaz | `uv run locust --headless -u 30 -r 10 -t 30s` | Una prueba repetible, en tu equipo o en integración continua |

| Opción | Qué / Para qué |
|---|---|
| `-f stress.py` | El archivo de usuarios. Por defecto, `locustfile.py` |
| `-u 30` | Usuarios en total |
| `-r 10` | Usuarios que arrancan por segundo: 30 usuarios a 10 por segundo tardan 3 s en estar todos |
| `-t 30s` | Duración. Sin ella, la prueba no termina |
| `--only-summary` | Solo las tablas finales; sin ella, Locust imprime las estadísticas cada 2 s |
| `--csv results/lab03` | Cuatro CSV: `_stats` (totales por petición), `_stats_history` (una fila por segundo), `_failures` y `_exceptions` |
| `--html results/lab03.html` | El informe HTML, con las tablas y las gráficas de peticiones por segundo, tiempos y usuarios |

El resumen final tiene dos tablas: la de peticiones (`# reqs`, `# fails`, `Avg`, `Med`, `req/s`) y la de percentiles, del 50 % al 100 %:

```text
Type     Name                  # reqs      # fails |    Avg     Min     Max    Med |   req/s  failures/s
GET      /api/books/{isbn}        141     0(0.00%) |      3       0      54      2 |    4.30        0.00
GET      /api/books?q=            439     0(0.00%) |     49      35     170     46 |   13.38        0.00
         Aggregated               580     0(0.00%) |     38       0     170     43 |   17.68        0.00

Type     Name                       50%    66%    75%    80%    90%    95%    98%    99%  99.9% 99.99%   100% # reqs
GET      /api/books/{isbn}            2      2      2      2      3      4     54     54     55     55     55    141
GET      /api/books?q=               46     49     52     54     60     76    120    130    170    170    170    439
```

Los tiempos van en milisegundos, y los percentiles son aproximados: Locust redondea cada tiempo antes de guardarlo (46, 120, 1100...) para no guardar todas las mediciones.

---

## 5. La forma de la carga: `LoadTestShape`

`-u` y `-r` describen una sola subida. Para escalones, picos o cualquier otra forma, define una clase que herede de `LoadTestShape`. Locust llama a su método `tick()` cada segundo:

```python
from locust import LoadTestShape


class StepLoad(LoadTestShape):
    """Cada 20 s, 50 usuarios más, hasta 300. Después, termina."""

    step_users = 50
    step_seconds = 20
    max_users = 300

    def tick(self):
        users = (int(self.get_run_time() // self.step_seconds) + 1) * self.step_users
        if users > self.max_users:
            return None
        return users, self.step_users
```

`tick()` devuelve una tupla `(usuarios, usuarios que arrancan por segundo)`, o `None` para terminar. Si el archivo tiene una forma, Locust ignora `-u`, `-r` y `-t`. Es el equivalente de `stages` en k6.

---

## 6. Criterios de aceptación y código de salida

Por defecto, Locust termina con código 1 si hubo algún fallo (`--exit-code-on-error` lo cambia) y con 0 si no. No tiene umbrales sobre la latencia: se escriben en Python, en un *listener* del evento `quitting`, que se ejecuta al terminar:

```python
from locust import events


@events.quitting.add_listener
def check_thresholds(environment, **kwargs):
    total = environment.stats.total
    p95 = total.get_response_time_percentile(0.95)
    if total.fail_ratio > 0.01:
        print(f"FALLO: {total.fail_ratio:.2%} de errores (máximo 1 %)")
        environment.process_exit_code = 1
    elif p95 > 300:
        print(f"FALLO: p95 de {p95} ms (máximo 300 ms)")
        environment.process_exit_code = 1
```

`environment.stats.total` es la fila `Aggregated`. Para exigir algo a una petición concreta, `environment.stats.get("/api/books?q=", "GET")` devuelve su fila. Es lo que en k6 hace `thresholds` con `{name:...}`, escrito a mano.

### `TaskSet`: el estilo antiguo

En tutoriales anteriores a Locust 1.0 verás las tareas dentro de una clase `TaskSet`, asignada al usuario con `tasks = [MyTaskSet]`. Sigue funcionando, y sirve para anidar comportamientos (un usuario que entra en «el catálogo» y hace varias cosas allí antes de salir con `self.interrupt()`). Para usuarios con tareas independientes, como los de esta semana, `@task` directamente en el `HttpUser` es más simple.

---

## 7. k6, Locust o JMeter

| | k6 | Locust | JMeter |
|---|---|---|---|
| Scripts | JavaScript | Python | Planes XML (`.jmx`) que se editan con su interfaz gráfica |
| Motor | Go | Python con gevent | Java, un hilo por usuario |
| Criterios de aceptación | `thresholds`, con código de salida 99 | Escritos en `events.quitting` | *Assertions* por petición; los criterios globales, con plugins o al procesar los resultados |
| Modelo abierto | Sí: *executors* `*-arrival-rate` | No: se aproxima con `constant_throughput` | Con el plugin *Throughput Shaping Timer* |
| Informe | Resumen en terminal, JSON y HTML | Terminal, CSV, HTML e interfaz web en vivo | Informe HTML (`-e -o`) a partir del `.jtl` |
| Protocolos | HTTP, WebSocket, gRPC, navegador | HTTP; otros escribiendo tu propio cliente | HTTP, JDBC, JMS, LDAP, FTP y más |
| Cuándo | Pruebas en el código del proyecto, con criterios en integración continua | Equipos que ya escriben Python, o usuarios con lógica compleja | Equipos con planes JMeter existentes, o protocolos que los otros no cubren |

El generador también tiene un límite. Contra la misma petición (`GET /api/books/9780000000001`, 100 usuarios sin espera, en el mismo equipo), cada herramienta midió:

| Generador | Peticiones por segundo | Mediana |
|---|---|---|
| Locust con `HttpUser` | 1831 | 29 ms |
| Locust con `FastHttpUser` | 6574 | 11 ms |
| k6 | 8396 | 11.4 ms |

La API era la misma: los 18 ms de diferencia en la mediana de `HttpUser` los pone el propio Locust, que pasa más tiempo preparando cada petición. `FastHttpUser` cambia `requests` por `geventhttpclient` y se acerca a k6. Si una prueba se acerca a esas cifras, sospecha del generador antes que del sistema ([teoría 04](04-linea-base-criterios-y-cuellos-de-botella.md#5-encontrar-el-cuello-de-botella)). `--processes 4` reparte los usuarios de Locust entre cuatro procesos.

---

## ✅ Resumen

- Un `locustfile.py` define clases de usuario (`HttpUser`) con tareas (`@task(peso)`), una espera (`wait_time`) y, si hay varias clases, un `weight`.
- `catch_response=True` con `success()` y `failure()` decide qué es un fallo; `name=` agrupa URLs.
- `--headless -u -r -t` ejecuta sin interfaz; `--csv` y `--html` guardan la evidencia.
- `LoadTestShape.tick()` da forma a la carga; `events.quitting` y `process_exit_code` convierten los criterios en el código de salida.
- k6 para criterios en integración continua y modelo abierto; Locust si el equipo escribe Python; JMeter para planes existentes y otros protocolos. El generador también se satura.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 02 - k6](02-k6.md) | [Teoría — 04 - Línea base, criterios y cuellos de botella](04-linea-base-criterios-y-cuellos-de-botella.md) → |

[↑ Semana 43](../README.md)
