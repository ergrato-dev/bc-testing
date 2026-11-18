# Lab 01 - Una suite, un comando

## Objetivo

Construir `verify.sh`, el script que ejecuta la suite Go completa: análisis, unitarios, integración con cobertura y un umbral del 85 %. Comprobar que se para en el primer fallo, también cuando la salida pasa por `tee`.

## Tiempo estimado

75 minutos.

## Preparación

Docker debe estar en marcha: `pgstore` arranca PostgreSQL con testcontainers-go, como en la [semana 34](../../../week-34-http_bd_e_integracion_en_go/README.md).

```bash
cd starter
go test ./...
# ok  	bootcamp/lab01/catalog	0.005s
# ?   	bootcamp/lab01/pgstore	[no test files]
```

Qué hay en `starter/`, una biblioteca con su API y su almacén:

| Archivo | Qué es |
|---|---|
| `catalog/catalog.go` | `LateFee(daysLate)`: 50 por día, hasta 1000; error con días negativos. `NewHandler`: `GET /fees?days=N` y `POST /books/{isbn}/lend` |
| `catalog/catalog_test.go` | Unitarios: una tabla para `LateFee` y la API con `httptest` y un stub del almacén |
| `pgstore/store.go` | Libros en PostgreSQL con pgx y la migración de `migrations/` embebida |
| `pgstore/store_test.go` | Integración (`//go:build integration`): `TestMain` arranca un contenedor; cada test trabaja en una transacción que se deshace |
| `verify.sh` | El script del lab. Solo trae `go vet`; los PASOS añaden el resto |

`verify.sh` ya tiene permiso de ejecución. Si al copiarlo lo pierdes (`permission denied`), devuélveselo con `chmod +x verify.sh`.

## Paso a paso

### Paso 0: lo que no se ejecuta

1. Lista los tests que `go test` encuentra. `-list .` imprime el nombre de cada función de test que cumple la expresión regular `.` (todas) sin ejecutarla:

   ```bash
   go test -list . ./...
   # TestLateFee
   # TestFeesEndpoint
   # TestLendEndpoint
   # ok  	bootcamp/lab01/catalog	0.003s
   # ?   	bootcamp/lab01/pgstore	[no test files]
   ```

2. Repite con la tag:

   ```bash
   go test -tags integration -list . ./...
   # TestLateFee
   # TestFeesEndpoint
   # TestLendEndpoint
   # ok  	bootcamp/lab01/catalog	0.004s
   # ... (líneas de testcontainers: Ryuk y el contenedor postgres:18.7-alpine)
   # TestLendUntilNoCopies
   # TestLendUnknownBook
   # TestGet
   # ... (parada del contenedor)
   # ok  	bootcamp/lab01/pgstore	4.264s
   ```

   `-list` ejecuta `TestMain`, así que arranca el contenedor aunque no ejecute ningún test ([semana 34](../../../week-34-http_bd_e_integracion_en_go/1-teoria/02-testmain-build-tags-y-short.md)).

Sin la tag, `go test ./...` termina en verde y tres tests no se han ejecutado: `pgstore` sale como `[no test files]`. Es el mismo problema que los `*IT` sin Failsafe de la [semana 31](../../../week-31-proyecto_integrador_java/2-practicas/lab-01-test-e-it-con-surefire-y-failsafe/README.md#paso-0-lo-que-no-se-ejecuta). Por eso la suite completa necesita un comando que nadie olvide.

### Paso 1: el análisis y los unitarios (PASO 1)

1. Ejecuta el script tal como viene:

   ```bash
   ./verify.sh
   # == go vet
   ```

   `go vet` no imprime nada si no encuentra problemas. Va dos veces: sin `-tags integration`, `go vet` no compila `store_test.go` y no lo revisa.

2. En `verify.sh`, descomenta las líneas del bloque `# PASO 1` (borra el `# ` del principio de cada una; en VS Code, selecciónalas y pulsa `Ctrl+/`). Deja la línea `# PASO 1: ...` comentada.

3. Ejecuta:

   ```bash
   ./verify.sh
   # == go vet
   # == unitarios
   # ok  	bootcamp/lab01/catalog	1.014s
   # ?   	bootcamp/lab01/pgstore	[no test files]
   ```

   `-count=1` desactiva la caché, `-race` activa el detector de carreras y `-shuffle=on` cambia el orden de los tests en cada ejecución ([teoría 01](../../1-teoria/01-la-suite-go-completa.md#un-comando-verifysh)). El segundo que tarda `catalog` lo pone `-race`, no los tests: lo verás en el [lab 02](../lab-02-datos-del-reporte/README.md).

### Paso 2: la integración con cobertura (PASO 2)

1. Descomenta el bloque `# PASO 2`.

2. Ejecuta:

   ```bash
   ./verify.sh
   # == go vet
   # == unitarios
   # ok  	bootcamp/lab01/catalog	1.014s
   # ?   	bootcamp/lab01/pgstore	[no test files]
   # == unitarios + integración, con cobertura
   # ok  	bootcamp/lab01/catalog	1.019s	coverage: 51.0% of statements in ./...
   # ok  	bootcamp/lab01/pgstore	3.461s	coverage: 41.2% of statements in ./...
   ```

   Con la tag, `go test` ejecuta los unitarios otra vez y además los de integración. `-coverpkg=./...` instrumenta los dos paquetes en cada binario de test, y `-coverprofile=cover.out` junta los contadores en un archivo ([semana 35](../../../week-35-concurrencia_y_calidad_en_go/1-teoria/04-cobertura-y-linters.md)). El `51.0%` de `catalog` no es la cobertura de `catalog`: es la parte de **todo el módulo** que ejecutan los tests de `catalog`. El lab 02 calcula la de cada paquete.

3. Lee el total:

   ```bash
   go tool cover -func=cover.out
   # bootcamp/lab01/catalog/catalog.go:20:	LateFee		100.0%
   # bootcamp/lab01/catalog/catalog.go:36:	NewHandler	100.0%
   # bootcamp/lab01/catalog/catalog.go:67:	writeJSON	100.0%
   # bootcamp/lab01/catalog/catalog.go:73:	writeError	100.0%
   # bootcamp/lab01/pgstore/store.go:33:	Migrate		70.0%
   # bootcamp/lab01/pgstore/store.go:56:	Add		100.0%
   # bootcamp/lab01/pgstore/store.go:62:	Get		100.0%
   # bootcamp/lab01/pgstore/store.go:73:	Lend		87.5%
   # total:					(statements)	92.2%
   ```

### Paso 3: el umbral (PASO 3)

1. Comprueba por qué la comparación no puede hacerla bash:

   ```bash
   bash -c 'total=92.2; [ "$total" -ge 85 ]'
   # bash: line 1: [: 92.2: integer expression expected
   ```

   `-ge` solo compara enteros. `awk` compara decimales.

2. Descomenta el bloque `# PASO 3`. La primera línea guarda en `total` el tercer campo de la línea `total:`; `$3+0` lo convierte en número y quita el `%`. La última línea termina con `exit 1` si el total no llega a 85.

3. Ejecuta y mira el código de salida. `$?` es el código con el que terminó el último comando: 0 si fue bien.

   ```bash
   ./verify.sh; echo "exit=$?"
   # ...
   # == umbral de cobertura
   # total: 92.2 % (mínimo: 85 %)
   # exit=0
   ```

### Paso 4: ver fallar el umbral

Un umbral que nunca has visto fallar puede no estar comprobando nada.

1. En `catalog/catalog_test.go`, añade una línea al principio de `TestLendEndpoint`:

   ```go
   func TestLendEndpoint(t *testing.T) {
   	t.Skip("experimento: sin este test")
   ```

2. Ejecuta:

   ```bash
   ./verify.sh; echo "exit=$?"
   # ...
   # ok  	bootcamp/lab01/catalog	1.018s	coverage: 39.2% of statements in ./...
   # ok  	bootcamp/lab01/pgstore	4.915s	coverage: 41.2% of statements in ./...
   # == umbral de cobertura
   # total: 80.4 % (mínimo: 85 %)
   # FALLO: la cobertura no llega al 85 %
   # exit=1
   ```

   La ruta `POST /books/{isbn}/lend` ya no la ejecuta nadie: el total baja al 80.4 % y el script termina con 1.

3. Borra la línea `t.Skip(...)`.

## Experimentos

### Experimento 1: `tee` sin `pipefail`

Quieres guardar la salida de los tests para el reporte, y añades `| tee` a las dos líneas de `go test`:

1. En `verify.sh`, cambia `set -euo pipefail` por `set -eu` y añade al final de cada línea `go test`:

   ```bash
   go test -count=1 -race -shuffle=on ./... | tee unit.log
   go test ... -coverprofile=cover.out ./... | tee integration.log
   ```

2. Rompe un test: en `TestLateFee`, cambia la fila `{"three days", 3, 150, nil}` por `{"three days", 3, 151, nil}`.

3. Ejecuta:

   ```bash
   ./verify.sh; echo "exit=$?"
   # == unitarios
   # --- FAIL: TestLateFee (0.00s)
   #     --- FAIL: TestLateFee/three_days (0.00s)
   #         catalog_test.go:29: LateFee(3) = 150, <nil>; want 151, <nil>
   # FAIL
   # ...
   # == umbral de cobertura
   # total: 92.2 % (mínimo: 85 %)
   # exit=0
   ```

   Un test falla y el script termina con 0. El código de una tubería es el del último comando, `tee`, que ha guardado el log sin problemas. `-e` no ve ningún fallo, y el umbral pasa porque `go test` escribe `cover.out` aunque un test falle.

4. Vuelve a poner `set -euo pipefail` y ejecuta otra vez: el script se para en la fase de unitarios con `exit=1`.

5. Deshaz los cambios: quita los `| tee` y deja la fila con 150.

## Comprobación final

```bash
./verify.sh; echo "exit=$?"
# == go vet
# == unitarios
# ok  	bootcamp/lab01/catalog	1.014s
# ?   	bootcamp/lab01/pgstore	[no test files]
# == unitarios + integración, con cobertura
# ok  	bootcamp/lab01/catalog	1.019s	coverage: 51.0% of statements in ./...
# ok  	bootcamp/lab01/pgstore	3.461s	coverage: 41.2% of statements in ./...
# == umbral de cobertura
# total: 92.2 % (mínimo: 85 %)
# exit=0
```

## Preguntas de cierre

1. ¿Por qué `go test ./...` en verde no basta para decir que la suite pasa?
2. ¿Por qué `verify.sh` ejecuta los unitarios dos veces? ¿Qué perderías quitando la fase de unitarios?
3. ¿Qué tres razones distintas pueden hacer que `verify.sh` termine con 1?
4. Sin `pipefail`, ¿qué otro comando delante de una tubería podría esconder un fallo en un script de CI?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Los datos del reporte](../lab-02-datos-del-reporte/README.md) → |

[↑ Semana 36](../../README.md)
