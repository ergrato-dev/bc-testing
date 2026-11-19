# Lab 03 - PostgreSQL como servicio

## Objetivo

Escribir el workflow de Go de `ci-lab` con dos jobs en paralelo (`lint` y `test`), ver cómo fallan los tests de base de datos en una máquina sin PostgreSQL, arrancarlo como contenedor de servicio y poner un umbral de cobertura con `awk`.

## Tiempo estimado

50 minutos.

## Requisitos

- La [teoría 02](../../1-teoria/02-preparar-cada-lenguaje-caches-y-servicios.md), sección 3.
- Los labs 01 y 02 terminados. Docker en tu equipo.

## Punto de partida

```bash
cd ~/ci-lab
cp <ruta-del-bootcamp>/bootcamp/week-45-ci_cd_y_testing_automatizado/2-practicas/lab-03-postgresql-como-servicio/starter/go.yml .github/workflows/go.yml
```

`go/store.go` guarda libros en la tabla `books` de PostgreSQL con [pgx](../../../week-34-http_bd_e_integracion_en_go/README.md), y `go/store_test.go` tiene tres tests que se conectan a la URL de la variable `DATABASE_URL`.

---

## Paso 1: en tu equipo

Sin base de datos, los tests no tienen dónde conectarse:

```bash
cd go
go test -count=1 ./...
# --- FAIL: TestLendMarksBookAsUnavailable (0.00s)
#     store_test.go:34: DATABASE_URL no está definida
# ...
# FAIL	example.com/cilab	0.004s
```

Con el PostgreSQL de `go/compose.yaml` (puerto 55445 de tu equipo):

```bash
docker compose up -d --wait
#  Container ci-lab-db-1 Healthy
DATABASE_URL=postgres://library:library@127.0.0.1:55445/library go test -count=1 -race -coverprofile=cover.out ./...
# ok  	example.com/cilab	1.075s	coverage: 87.5% of statements
docker compose down -v
cd ..
```

`VARIABLE=valor comando` define la variable solo para ese comando. En la CI, la variable irá en el workflow.

## Paso 2: dos jobs, sin base de datos

Descomenta el PASO 2 y su `(sigue)`, sin el PASO 4. El workflow tiene dos jobs que no dependen uno del otro:

- `lint`: `gofmt -l .` lista los archivos mal formateados; `test -z` falla si la lista no está vacía, y entonces la imprime. Después, `go vet`.
- `test`: los tests con `-race` y `-coverprofile`, y un step que lee el total con `go tool cover -func` y falla por debajo del 85 %.

`go-version-file: go/go.mod` instala la versión de la directiva `go` (1.26.5). `DATABASE_URL` está definida en el `env` del job, pero nadie arranca PostgreSQL.

```bash
git add .github/workflows/go.yml
git commit -m "Add Go workflow"
git push
```

## Paso 3: leer el fallo

```bash
gh run view <id>
# X test in 36s (ID 135233167481)
#   ✓ Set up job
#   ✓ Run actions/checkout@v7
#   ✓ Run actions/setup-go@v7
#   X Run go test -count=1 -race -coverprofile=cover.out ./...
#   - Coverage threshold (85 %)
# ✓ lint in 27s (ID 135233167922)
```

- `lint` pasa y `test` falla: los jobs son independientes. Uno no espera al otro ni se cancela si el otro falla.
- `-` delante de `Coverage threshold` es un step que no se ejecutó: el anterior falló.

```bash
gh run view <id> --log-failed
```

```text
  DATABASE_URL: ***localhost:5432/library
--- FAIL: TestLendMarksBookAsUnavailable (0.00s)
    store_test.go:34: failed to connect to `user=library database=library`:
        	127.0.0.1:5432 (localhost): dial error: dial tcp 127.0.0.1:5432: connect: connection refused
        	[::1]:5432 (localhost): dial error: dial tcp [::1]:5432: connect: connection refused
...
coverage: 18.8% of statements
FAIL
##[error]Process completed with exit code 1.
```

`connection refused`: en el puerto 5432 del runner no escucha nadie. Fíjate también en la primera línea: GitHub tapó el usuario y la contraseña de la URL con `***`.

## Paso 4: el contenedor de servicio

Descomenta el PASO 4: el bloque `services` del job `test`. Cada clave está explicada en la [teoría 02](../../1-teoria/02-preparar-cada-lenguaje-caches-y-servicios.md#el-contenedor-de-servicio).

```bash
git commit -am "Run PostgreSQL as a service container for the Go tests"
git push
```

```bash
gh run view <id>
# ✓ test in 51s (ID 135233992484)
# ✓ lint in 31s (ID 135233992663)
```

En el log del job `test`, antes del primer step:

```text
##[group]Starting postgres service container
##[command]/usr/bin/docker pull postgres:18.7-alpine
...
##[command]/usr/bin/docker create --name 87029d0c..._postgres187alpine_c097a2 ... -p 5432:5432 --health-cmd "pg_isready -U library -d library" --health-interval 1s --health-timeout 3s --health-retries 30 -e "POSTGRES_USER=library" ...
##[group]Waiting for all services to be ready
postgres service is starting, waiting 2 seconds before checking again.
postgres service is healthy.
```

`services` se traduce en un `docker create` con tus opciones; GitHub espera al *health check* antes de ejecutar los steps. Y al final:

```text
ok  	example.com/cilab	1.059s	coverage: 87.5% of statements
total: 87.5 %
```

`lint` (31 s) terminó antes que `test` (51 s): el run duró lo que el job más lento, no la suma.

---

## Experimento: el umbral en tu equipo

El step del umbral es bash, y funciona igual en tu equipo. Con PostgreSQL arrancado (`docker compose up -d --wait` en `go/`) y el umbral al 90 en lugar de 85:

```bash
cd go
DATABASE_URL=postgres://library:library@127.0.0.1:55445/library go test -count=1 -coverprofile=cover.out ./...
total=$(go tool cover -func=cover.out | awk '/^total:/ {print $3+0}')
echo "total: $total %"
# total: 87.5 %
awk -v total="$total" 'BEGIN { if (total < 90) { print "la cobertura no llega al 90 %"; exit 1 } }'
# la cobertura no llega al 90 %
echo $?
# 1
```

`$3+0` convierte `87.5%` en el número 87.5. `go tool cover -func=cover.out` sin el `awk` dice qué falta:

```text
example.com/cilab/store.go:20:	NewStore	75.0%
example.com/cilab/store.go:32:	Add		100.0%
example.com/cilab/store.go:42:	Lend		75.0%
example.com/cilab/store.go:51:	CountAvailable	100.0%
total:				(statements)	87.5%
```

Lo que falta son las ramas de error de la base de datos. Un umbral se elige con lo que el proyecto puede cubrir de forma razonable, no con un número redondo. Apaga PostgreSQL con `docker compose down -v` y vuelve a la carpeta raíz.

## Comprobación final

```bash
gh run list --limit 3
```

El último run de `go` está en verde con sus dos jobs. Para cada uno de los tres workflows del repositorio sabes qué lo dispara, cuántos jobs tiene y qué hace que falle.

## Qué has aprendido

- Los jobs de un workflow corren en paralelo y en máquinas distintas; uno que falla no para a los demás.
- Un test que necesita PostgreSQL falla con `connection refused` en un runner vacío. `services` arranca el contenedor y, con `--health-cmd`, GitHub espera a que acepte conexiones.
- La configuración del test (`DATABASE_URL`) va en el `env` del job.
- El umbral de cobertura de Go es un `awk` sobre `go tool cover -func`: el mismo comando en tu equipo y en la CI.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Matrix, caché y artifacts](../lab-02-matrix-cache-y-artifacts/README.md) | [Lab 04 - Hooks y quality gate](../lab-04-hooks-y-quality-gate/README.md) → |

[↑ Semana 45](../../README.md)
