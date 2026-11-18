# Proyecto Semanal - Concurrencia y calidad en la suite Go del dominio

## Objetivo

Pasar la suite Go de las semanas 32 a 34 por las herramientas de calidad de esta semana, y dejar en el repositorio la prueba de cada resultado:

- un test concurrente contra la API que pasa limpio con `-race`;
- un fuzz test que encuentra al menos un caso límite en tu dominio, con el fallo guardado en `testdata/fuzz/` y arreglado;
- un benchmark comparado con `benchstat` antes y después de una optimización;
- `staticcheck` sin avisos, y la cobertura medida con `-race`;
- un informe, `calidad.md`, con lo que encontró cada herramienta.

## Dónde va

En la carpeta `go/` de tu repositorio `qa-<nombre>-<apellido>` ([proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)). Los tests van en el paquete de tu servicio. Los ejemplos usan el museo: paquete `museum`, servicio `LendingService` y API `NewHandler`.

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

## Paso a paso

### Paso 1: la rama y el starter

```bash
cd qa-<nombre>-<apellido>
git switch main && git pull
git switch -c week-35
cp -rn <ruta-al-bootcamp>/bootcamp/week-35-concurrencia_y_calidad_en_go/3-proyecto/starter/. .
```

El starter trae:

| Archivo | Qué es |
|---|---|
| `go/domain/quality_test.go` | Tres esqueletos con TODO: un test concurrente, un fuzz test y un benchmark |
| `go/calidad.md` | La plantilla del informe, con una sección por herramienta |

Mueve el archivo de test al paquete de tu servicio y cambia su línea `package`. Para el museo:

```bash
mv go/domain/quality_test.go go/museum/
rmdir go/domain
sed -i 's/^package domain$/package museum/' go/museum/quality_test.go
cd go
go test -count=1 -run 'Concurrent|FuzzDomain' -bench=HotPath -v ./...
# === RUN   TestConcurrentRequests
#     quality_test.go:6: TODO: varios clientes a la vez contra httptest.NewServer, limpio con -race (paso 3)
# --- SKIP: TestConcurrentRequests (0.00s)
# === RUN   FuzzDomain
# --- PASS: FuzzDomain (0.00s)
# BenchmarkHotPath
#     quality_test.go:17: TODO: la función que vas a optimizar, dentro de for b.Loop() (paso 5)
# --- SKIP: BenchmarkHotPath
# PASS
# ok  	example.com/qa-museo/go/museum	0.007s
# ?   	example.com/qa-museo/go/pgstore	[no test files]
```

(Se omiten las líneas `goos`, `goarch`, `pkg` y `cpu`.) Los esqueletos te dicen qué falta. Puedes escribir cada test en el archivo que le corresponda (el concurrente junto a los de la API, el fuzz junto a la validación) y borrar `quality_test.go` al final.

### Paso 2: las herramientas en go.mod

Registra `staticcheck` y `benchstat` como herramientas del módulo, con las versiones del curso:

```bash
go get -tool honnef.co/go/tools/cmd/staticcheck@v0.8.2
go get -tool golang.org/x/perf/cmd/benchstat@v0.0.0-20261117185222-4febdb082b2c
```

`go get` actualiza también algunas dependencias compartidas (`golang.org/x/tools`, `x/sync`...). Es normal: las dos herramientas piden versiones más nuevas que las que ya había en `go.mod`.

Pasa `staticcheck` por el código que ya tienes, con y sin la tag de integración:

```bash
go tool staticcheck ./... && go tool staticcheck -tags integration ./...
```

Si da avisos, arréglalos como en el [lab 04](../2-practicas/lab-04-cobertura-y-linters/README.md#paso-4-staticcheck) y anótalos en la sección 4 de `calidad.md`. En el museo no dio ninguno.

### Paso 3: un test concurrente limpio con -race

Escribe un test que haga lo que hace un servidor en producción: atender varias peticiones a la vez. Para el museo, `TestLendConcurrently` en `museum/api_test.go`:

| Pieza | Qué hace |
|---|---|
| `httptest.NewServer(NewHandler(s))` | La API en un puerto local: cada petición se atiende en su propia goroutine, como en producción ([semana 34](../../week-34-http_bd_e_integracion_en_go/1-teoria/01-httptest-handlers-y-clientes.md#2-clientes-con-newserver)) |
| 20 goroutines con `wg.Go` | Cada una pide el préstamo de la **misma** pieza y envía el código de estado a un canal con capacidad para 20 |
| `notifier.EXPECT().Send(...).Times(1)` | Solo se puede enviar un aviso |
| `cmp.Diff` sobre un `map[int]int` | Exactamente un `200` y diecinueve `409` |

Ejecútalo sin y con `-race`:

```bash
go test -count=1 -run LendConcurrently ./museum
# ok  	example.com/qa-museo/go/museum	0.008s
go test -count=1 -race -run LendConcurrently ./museum
# WARNING: DATA RACE
# Read at 0x00c0000b52c0 by goroutine 52:
#   ...
#       .../museum/fakes_test.go:32 +0xa4
# Previous write at 0x00c0000b52c0 by goroutine 59:
#   ...
#       .../museum/fakes_test.go:40 +0x68
# ...
```

Sin `-race` pasa en verde. Con `-race`, el informe señala el `fakeStore` de la semana 33: un mapa sin protección, que ahora recibe llamadas desde las goroutines del servidor ([lab 01](../2-practicas/lab-01-race-detector-y-synctest/README.md#paso-1-una-carrera-que-el-test-no-ve-paso-1)). Arréglalo con un `sync.Mutex` en cada método del fake.

Ejecuta después el test muchas veces: `go test -count=50 -race -run LendConcurrently ./museum`. En el museo, con solo el mutex, falló 3 veces de 50: dos peticiones leyeron la pieza como disponible antes de que ninguna la guardara, y las dos la prestaron (el mock recibió un segundo `Send`). El mutex protege cada llamada al almacén, no la secuencia leer, decidir y guardar. En PostgreSQL lo impide la clave primaria de `loans` (`pgstore` devuelve `ErrNotAvailable`); el fake tiene que cumplir la misma regla. Con `Open` rechazando un segundo préstamo abierto de la misma pieza, 50 rondas sin fallos.

Anota en la sección 1 de `calidad.md` qué encontró `-race` y qué encontró la repetición.

### Paso 4: un fuzz test que encuentre un caso límite

Elige una propiedad que tu dominio tiene que cumplir para **cualquier** entrada válida ([teoría 02](../1-teoria/02-fuzzing-nativo.md#4-qué-comprobar-en-la-función-objetivo)). Algunas ideas:

| Propiedad | Ejemplo en el museo |
|---|---|
| Ida y vuelta | Una pieza que `ValidatePiece` acepta sale igual de `json.Marshal` + `json.Unmarshal` |
| Sin `panic` | Validar o calcular con cualquier texto o número |
| Un invariante del resultado | Un recargo nunca es negativo ni supera el máximo |

Para el museo, `FuzzPieceJSON` en `museum/piece_fuzz_test.go`: recibe código, nombre y valor; descarta con `t.Skip()` lo que `ValidatePiece` rechaza; construye la pieza, la pasa por JSON y la compara con `%#v`, que muestra los textos entre comillas y con sus bytes escapados. La semilla es una pieza válida (`f.Add("MAL-001", "Vasija Moche", 250_000)`).

```bash
go test -run='^$' -fuzz='^FuzzPieceJSON$' -fuzztime=60s ./museum
# ...
#         piece_fuzz_test.go:24: JSON round trip changed the piece:
#              got museum.Piece{Code:"AAA-000", Name:"�", DeclaredValue:249958, Status:1}
#             want museum.Piece{Code:"AAA-000", Name:"\xff", DeclaredValue:249958, Status:1}
#     Failing input written to testdata/fuzz/FuzzPieceJSON/2346bfc02c4cf8bf
#     To re-run:
#     go test -run=FuzzPieceJSON/2346bfc02c4cf8bf
# FAIL
# exit status 1
# FAIL	example.com/qa-museo/go/museum	0.245s
```

En 0.25 s: un nombre con un byte que no es UTF-8 (`\xff`). `ValidatePiece` lo acepta, y `encoding/json` lo cambia por `U+FFFD` (`�`) al serializar. Contra PostgreSQL, guardar esa pieza falla con `invalid byte sequence for encoding "UTF8"` y la API respondería `500`. El byte exacto cambia en cada ejecución.

Arréglalo donde está la regla: `ValidatePiece` rechaza los nombres que no son UTF-8 válido (`utf8.ValidString`), con un error nuevo y una fila nueva en la tabla de `TestValidatePiece`. Después:

```bash
go test -count=1 ./museum
go test -run='^$' -fuzz='^FuzzPieceJSON$' -fuzztime=30s ./museum
```

Los dos tienen que pasar. Haz commit de `testdata/fuzz/`: es el test de regresión del fallo. Si en 2 o 3 minutos de fuzzing tu propiedad no encuentra nada, prueba otra; con enteros muy grandes el fuzzer tarda mucho en llegar a los límites, y con texto los construye enseguida. Documenta en la sección 2 de `calidad.md` la propiedad, el fallo y el arreglo.

### Paso 5: un benchmark antes y después de optimizar

Elige una función que se ejecute a menudo (en una petición, en una validación) y escribe su benchmark con `for b.Loop()` ([teoría 03](../1-teoria/03-benchmarks-y-benchstat.md#1-anatomía-de-un-benchmark)). Para el museo, `BenchmarkValidatePiece`, que valida una pieza correcta.

```bash
mkdir -p bench
go test -run='^$' -bench=ValidatePiece -benchmem -count=10 ./museum > bench/old.txt
```

Optimiza la función. En el museo, `ValidatePiece` comprobaba el código con la expresión regular `^[A-Z]{3}-\d{3}$`. La optimización es `isCode`, que comprueba la longitud, el guion y cada byte a mano. Después:

```bash
go test -count=1 ./museum
go test -run='^$' -bench=ValidatePiece -benchmem -count=10 ./museum > bench/new.txt
go tool benchstat bench/old.txt bench/new.txt
#                 │ bench/old.txt │            bench/new.txt            │
#                 │    sec/op     │   sec/op     vs base                │
# ValidatePiece-8     70.73n ± 4%   16.50n ± 3%  -76.67% (p=0.000 n=10)
# ...
```

Un 77 % menos, significativo. Una optimización también puede romper la función. Protégela con un fuzz test que compare la versión nueva con la antigua: en el museo, `FuzzIsCode` comprueba que `isCode` y la expresión regular responden lo mismo para cualquier cadena. Pega la salida completa de `benchstat` en la sección 3 de `calidad.md`, y haz commit de `bench/old.txt` y `bench/new.txt`.

### Paso 6: la cobertura con -race

```bash
go test -count=1 -race -short -covermode=atomic -coverprofile=cover.out ./...
# ok  	example.com/qa-museo/go/museum	1.043s	coverage: 88.3% of statements
# 	example.com/qa-museo/go/pgstore		coverage: 0.0% of statements
go test -count=1 -race -tags integration -coverpkg=./... -coverprofile=cover-all.out ./...
# ok  	example.com/qa-museo/go/museum	1.037s	coverage: 67.5% of statements in ./...
# ok  	example.com/qa-museo/go/pgstore	3.927s	coverage: 39.8% of statements in ./...
go tool cover -func=cover-all.out | tail -1
# total:						(statements)	89.4%
```

La segunda línea necesita Docker. Con `-coverpkg=./...` y la integración, el total cuenta todo el módulo ([teoría 04](../1-teoria/04-cobertura-y-linters.md#3-varios-paquetes--coverpkg)). Copia las líneas `coverage:` en la sección 5 de `calidad.md`. No subas `cover.out` ni `cover-all.out`: añádelos a `.gitignore`. La semana 36 pedirá un mínimo de cobertura; esta semana solo se mide.

### Paso 7: verificar y entregar

```bash
go vet ./... && go vet -tags integration ./...
go tool staticcheck ./... && go tool staticcheck -tags integration ./...
go test -count=1 -race -shuffle=on -short ./...
go test -count=1 -race -shuffle=on -tags integration ./...
```

Desde la raíz del bootcamp, con Docker en marcha:

```bash
PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 35
```

Haz commit (incluidos `testdata/fuzz/`, `bench/` y `calidad.md`), sube la rama y abre un pull request a `main`.

## ✅ Requisitos verificables

Copia de [checks.json](../checks.json):

| Comprobación | Pista |
|---|---|
| `go vet` no encuentra problemas, con y sin la tag `integration` | Paso 7 |
| `staticcheck` está en `go.mod` como herramienta y no da avisos, con y sin la tag `integration` | Paso 2 |
| La suite unitaria pasa con `-race`, `-short` y en orden aleatorio | Paso 3 |
| La suite de integración pasa con `-race` contra PostgreSQL (necesita Docker) | Paso 7 |
| No queda ningún TODO en el código Go | Pasos 3 a 5 |
| Hay un test que lanza goroutines | Paso 3 |
| Hay un fuzz test con corpus semilla (`f.Add`) | Paso 4 |
| El fallo que encontró el fuzzer está en `testdata/fuzz/` | Paso 4 |
| Hay un benchmark con `b.Loop()` y los benchmarks se ejecutan | Paso 5 |
| `benchstat` está en `go.mod` y compara `bench/old.txt` con `bench/new.txt`, con al menos 6 medidas en cada uno | Paso 5 |
| `calidad.md` documenta el race detector, el fallo del fuzzer, la salida de `benchstat` y la cobertura | Pasos 3 a 6 |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - Cobertura y linters](../2-practicas/lab-04-cobertura-y-linters/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 35](../README.md)
