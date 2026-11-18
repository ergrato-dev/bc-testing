# Lab 04 - Cobertura y linters

## Objetivo

Medir la cobertura de un módulo con dos paquetes, encontrar en el informe las ramas sin probar y cubrirlas, sumar la cobertura del binario en ejecución a la de los tests, y limpiar con `staticcheck` cuatro problemas que `go vet` deja pasar.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
go mod download
go test -count=1 -v ./...
# ?   	bootcamp/lab04/cmd/fees	[no test files]
# === RUN   TestLateFee
# --- PASS: TestLateFee (0.00s)
# PASS
# ok  	bootcamp/lab04/fees	0.003s
```

El módulo calcula los recargos de una biblioteca por devolver tarde un libro:

| Paquete | Qué tiene |
|---|---|
| `fees` | `LateFee(days, category)`: 50 por día de retraso, hasta 1500, y nada para la categoría `staff`. `Receipt(member, days, category)`: el texto del recibo |
| `cmd/fees` | Un programa de línea de comandos: `fees <socio> <días> [categoría]` imprime el recibo |

`go.mod` registra `staticcheck` como herramienta del módulo ([teoría 04](../../1-teoria/04-cobertura-y-linters.md#staticcheck)).

Los pasos crean archivos de resultados en `starter/`: `cover.out`, `cover.html`, `all.out` y las carpetas `bin/` y `covdata/`. En un proyecto real no se suben al repositorio.

## Paso a paso

### Paso 1: medir y leer la cobertura

1. Ejecuta los tests con cobertura:

   ```bash
   go test -cover ./...
   # 	bootcamp/lab04/cmd/fees		coverage: 0.0% of statements
   # ok  	bootcamp/lab04/fees	0.003s	coverage: 30.0% of statements
   ```

   `cmd/fees` no tiene tests y aparece con `0.0%`: desde Go 1.22, un paquete sin tests cuenta como no cubierto.

2. Guarda el perfil de cobertura y léelo por función:

   ```bash
   go test -coverprofile=cover.out ./...
   go tool cover -func=cover.out
   # bootcamp/lab04/cmd/fees/main.go:12:	main		0.0%
   # bootcamp/lab04/fees/fees.go:19:		LateFee		60.0%
   # bootcamp/lab04/fees/fees.go:30:		Receipt		0.0%
   # bootcamp/lab04/fees/fees.go:38:		roundUp		0.0%
   # total:					(statements)	13.6%
   ```

   El total (13.6 %) es más bajo que el 30 % de `fees` porque también cuenta las sentencias de `main.go`.

3. Genera el informe HTML y ábrelo en el navegador:

   ```bash
   go tool cover -html=cover.out -o cover.html
   ```

   Abre `cover.html` con tu navegador (en Linux, `xdg-open cover.html`). Sin `-o`, `go tool cover` intenta abrir el navegador él mismo.

En el informe, el código en verde se ejecutó y el rojo no. En `LateFee` están en rojo las dos ramas que la tabla de `TestLateFee` nunca toma: `days < 0` y `category == "staff"`. `Receipt` y `roundUp` están enteras en rojo.

### Paso 2: cubrir lo que falta (PASO 2)

1. En el import de `fees/fees_test.go`, descomenta `"errors"`.
2. Descomenta el bloque del PASO 2 y guarda.
3. Ejecuta:

   ```bash
   go test -count=1 -cover ./...
   # 	bootcamp/lab04/cmd/fees		coverage: 0.0% of statements
   # ok  	bootcamp/lab04/fees	0.003s	coverage: 90.0% of statements
   go test -coverprofile=cover.out ./... > /dev/null
   go tool cover -func=cover.out
   # bootcamp/lab04/cmd/fees/main.go:12:	main		0.0%
   # bootcamp/lab04/fees/fees.go:19:		LateFee		100.0%
   # bootcamp/lab04/fees/fees.go:30:		Receipt		100.0%
   # bootcamp/lab04/fees/fees.go:38:		roundUp		0.0%
   # total:					(statements)	40.9%
   ```

Los tres tests nuevos salen de leer el informe: `staff` no paga, los días negativos dan `ErrNegativeDays`, y `Receipt` forma el texto y propaga el error. Lo que queda en rojo es `roundUp`, que nadie llama (el paso 4 la borra), y `main`.

4. Prueba `-coverpkg`, que instrumenta todos los paquetes del módulo en cada suite de tests:

   ```bash
   go test -count=1 -coverpkg=./... -cover ./...
   # 	bootcamp/lab04/cmd/fees		coverage: 0.0% of statements
   # ok  	bootcamp/lab04/fees	0.003s	coverage: 40.9% of statements in ./...
   ```

   El porcentaje de `fees` pasa a ser la parte de **todo el módulo** (`in ./...`) que ejecutan sus tests ([teoría 04](../../1-teoria/04-cobertura-y-linters.md#3-varios-paquetes--coverpkg)). Para `main.go`, los tests unitarios no llegan.

### Paso 3: la cobertura del binario

`main` lee los argumentos, comprueba que haya suficientes y termina con `os.Exit`. Se prueba ejecutando el programa, como lo usaría alguien.

1. Compila el binario con instrumentación de cobertura:

   ```bash
   go build -cover -o bin/fees ./cmd/fees
   ```

2. Ejecútalo tres veces con `GOCOVERDIR`, la carpeta donde el binario escribe sus contadores al terminar. La carpeta tiene que existir:

   ```bash
   mkdir -p covdata/e2e
   GOCOVERDIR=covdata/e2e ./bin/fees "ana lucía" 3
   # Ana Lucía: 3 días, 150
   GOCOVERDIR=covdata/e2e ./bin/fees ana -1
   # Days cannot be negative
   GOCOVERDIR=covdata/e2e ./bin/fees ana
   # usage: fees <member> <days> [category]
   ls covdata/e2e
   # covcounters.f8f33937c20a776f00d75334ac8538d8.506343.1794989674851940309
   # covcounters.f8f33937c20a776f00d75334ac8538d8.506348.1794989674854691935
   # covcounters.f8f33937c20a776f00d75334ac8538d8.506353.1794989674858371611
   # covmeta.f8f33937c20a776f00d75334ac8538d8
   ```

   Un archivo `covcounters` por ejecución y un `covmeta` con la descripción del binario. Los nombres cambian en cada ejecución.

3. Calcula la cobertura de esas ejecuciones:

   ```bash
   go tool covdata percent -i=covdata/e2e
   # 	bootcamp/lab04/cmd/fees		coverage: 91.7% of statements
   # 	bootcamp/lab04/fees		coverage: 80.0% of statements
   ```

4. Haz que los tests unitarios escriban sus contadores en el mismo formato, con el flag `-test.gocoverdir` (todo lo que va detrás de `-args` se le pasa al binario de test):

   ```bash
   mkdir -p covdata/unit
   go test -count=1 -cover ./... -args -test.gocoverdir=$PWD/covdata/unit
   ```

   Usa una ruta absoluta (`$PWD/...`), porque los tests de cada paquete se ejecutan desde la carpeta del paquete.

5. Suma las dos fuentes y conviértelas al formato de `-coverprofile`:

   ```bash
   go tool covdata percent -i=covdata/e2e,covdata/unit
   # 	bootcamp/lab04/cmd/fees		coverage: 91.7% of statements
   # 	bootcamp/lab04/fees		coverage: 90.0% of statements
   go tool covdata textfmt -i=covdata/e2e,covdata/unit -o all.out
   go tool cover -func=all.out
   # bootcamp/lab04/cmd/fees/main.go:12:	main		91.7%
   # bootcamp/lab04/fees/fees.go:19:		LateFee		100.0%
   # bootcamp/lab04/fees/fees.go:30:		Receipt		100.0%
   # bootcamp/lab04/fees/fees.go:38:		roundUp		0.0%
   # total:					(statements)	90.9%
   ```

Los tests unitarios cubren `fees` y las ejecuciones del binario cubren `main`: juntos, el 90.9 % del módulo ([teoría 04](../../1-teoria/04-cobertura-y-linters.md#4-la-cobertura-de-un-binario)). A `main` le falta la línea que lee la categoría, porque ninguna ejecución pasó un tercer argumento. `go tool cover -html=all.out` lo muestra en rojo.

### Paso 4: staticcheck

1. Ejecuta los dos linters:

   ```bash
   go vet ./...
   go tool staticcheck ./...
   # cmd/fees/main.go:17:2: this value of err is never used (SA4006)
   # fees/fees.go:15:23: error strings should not be capitalized (ST1005)
   # fees/fees.go:35:41: strings.Title has been deprecated since Go 1.18 and an alternative has been available since Go 1.0: The rule Title uses for word boundaries does not handle Unicode punctuation properly. Use golang.org/x/text/cases instead.  (SA1019)
   # fees/fees.go:38:6: func roundUp is unused (U1000)
   ```

   `go vet` no imprime nada. La primera vez, `go tool staticcheck` tarda unos segundos en compilarse.

2. Arregla cada aviso. `go tool staticcheck -explain SA4006` explica cualquiera de ellos:

   | Aviso | Qué pasa | Arreglo |
   |---|---|---|
   | `SA4006` en `main.go:17` | El error de `strconv.Atoi` se guarda en `err` y se pisa en la línea siguiente sin mirarlo: `fees ana tres` calcularía un recargo de 0 días | Comprueba el error justo después de `Atoi` (código abajo) |
   | `ST1005` en `fees.go:15` | Los mensajes de error de Go van en minúscula, porque se suelen encadenar (`opening loan: days cannot...`) | `errors.New("days cannot be negative")` |
   | `SA1019` en `fees.go:35` | `strings.Title` está obsoleta | Usa `golang.org/x/text/cases`, como dice el aviso (código abajo) |
   | `U1000` en `fees.go:38` | `roundUp` no la llama nadie | Bórrala |

   En `cmd/fees/main.go`, después de `days, err := strconv.Atoi(os.Args[2])`:

   ```go
   if err != nil {
   	fmt.Fprintln(os.Stderr, "days must be a number:", os.Args[2])
   	os.Exit(2)
   }
   ```

   Para `strings.Title`, añade la dependencia:

   ```bash
   go get golang.org/x/text@v0.44.0
   # go: upgraded golang.org/x/mod v0.41.0 => v0.42.0
   # go: upgraded golang.org/x/sync v0.23.0 => v0.25.0
   # go: added golang.org/x/text v0.44.0
   # go: upgraded golang.org/x/tools v0.51.0 => v0.52.0
   ```

   En `fees/fees.go`, cambia `strings.Title(member)` por `cases.Title(language.Spanish).String(member)` y, en el import, `"strings"` por `"golang.org/x/text/cases"` y `"golang.org/x/text/language"`. `cases.Title` sabe en qué idioma está el texto: `language.Spanish` aplica las reglas del español.

3. Ordena `go.mod` y comprueba:

   ```bash
   go mod tidy
   go vet ./...
   go tool staticcheck ./...
   go test -count=1 -cover ./...
   # 	bootcamp/lab04/cmd/fees		coverage: 0.0% of statements
   # ok  	bootcamp/lab04/fees	0.003s	coverage: 100.0% of statements
   ```

   `staticcheck` sin salida significa que no queda nada. Con `roundUp` borrada, `fees` llega al 100 %.

## Experimentos

### Experimento 1: -race con el modo set

```bash
go test -race -covermode=set ./fees
# -covermode must be "atomic", not "set", when -race is enabled
```

Con `-race`, los contadores de cobertura se incrementan desde varias goroutines, y el race detector los vería como carreras. Por eso Go exige el modo `atomic` ([teoría 04](../../1-teoria/04-cobertura-y-linters.md#2-los-tres-modos)). Sin `-covermode`, `-race` ya lo elige: la primera línea de `cover.out` dice `mode: atomic`.

### Experimento 2: un binario con -cover sin GOCOVERDIR

Con el binario del paso 3, ejecútalo sin la variable:

```bash
./bin/fees ana 3
# warning: GOCOVERDIR not set, no coverage data emitted
# Ana: 3 días, 150
```

El programa funciona igual, pero avisa de que no guardó nada. En un test de extremo a extremo, ese aviso significa que la cobertura de esa ejecución se perdió.

### Experimento 3: un test que no comprueba nada

Con el paso 4 aplicado, cambia `TestReceipt` por una versión que llama a `Receipt` y no mira el resultado:

```go
func TestReceipt(t *testing.T) {
	Receipt("ana lucía", 3, "")
	Receipt("ana lucía", -1, "")
}
```

Ahora introduce un error en `Receipt`: en el `fmt.Sprintf`, intercambia `days, fee)` por `fee, days)`.

```bash
go test -count=1 -cover ./fees
# ok  	bootcamp/lab04/fees	0.003s	coverage: 100.0% of statements
```

100 % de cobertura y un recibo que dice `150 días, 3`. La cobertura cuenta líneas ejecutadas, no comprobadas. Restaura `TestReceipt` sin deshacer el error:

```bash
go test -count=1 -cover ./fees
# --- FAIL: TestReceipt (0.00s)
#     fees_test.go:44: Receipt() = "Ana Lucía: 150 días, 3", want "Ana Lucía: 3 días, 150"
# FAIL
# coverage: 100.0% of statements
# FAIL	bootcamp/lab04/fees	0.004s
```

La misma cobertura, y ahora el error se ve. Deshaz el intercambio.

### Experimento 4: golangci-lint

[golangci-lint](https://golangci-lint.run) junta muchos linters en una pasada. Se instala como binario, fuera del módulo:

```bash
go install github.com/golangci/golangci-lint/v2/cmd/golangci-lint@v2.15.0
golangci-lint version
# golangci-lint has version 2.15.0 built with go1.26.5 from (unknown, modified: ?, mod sum: "h1:w94zf8HAYI7hm3164kwKsvZRuqXvY+YxeFoTbu1OLg8=") on (unknown)
```

Su documentación recomienda descargar el binario publicado; `go install` da la misma versión y solo necesita Go. Este experimento se ve mejor **antes** del paso 4. Sobre el código del starter:

```bash
golangci-lint run ./...
# cmd/fees/main.go:17:8: ineffectual assignment to err (ineffassign)
# 	days, err := strconv.Atoi(os.Args[2])
# 	      ^
# fees/fees.go:15:23: ST1005: error strings should not be capitalized (staticcheck)
# ...
# fees/fees.go:38:6: func roundUp is unused (unused)
# ...
# 4 issues:
# * ineffassign: 1
# * staticcheck: 2
# * unused: 1
```

Son los cuatro avisos de `staticcheck`, repartidos entre los cinco linters que golangci-lint activa por defecto (`errcheck`, `govet`, `ineffassign`, `staticcheck` y `unused`). El de `err` lo da `ineffassign` en lugar de `SA4006`, porque golangci-lint quita los avisos repetidos. Con el paso 4 aplicado:

```bash
golangci-lint run ./...
# 0 issues.
```

## Preguntas de cierre

1. ¿Por qué el total de `go tool cover -func` es más bajo que el porcentaje de `fees`?
2. ¿Qué mide `-coverpkg=./...` que no mide `-cover` solo?
3. ¿Cómo se suma la cobertura de un binario en ejecución a la de los tests unitarios?
4. Un paquete tiene un 100 % de cobertura. ¿Qué no te dice ese número?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - Benchmarks con benchstat](../lab-03-benchmarks-con-benchstat/README.md) | [Proyecto — Proyecto Semanal - Concurrencia y calidad en la suite Go del dominio](../../3-proyecto/README.md) → |

[↑ Semana 35](../../README.md)
