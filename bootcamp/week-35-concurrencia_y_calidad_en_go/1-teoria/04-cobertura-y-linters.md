# 04 - Cobertura y linters

> Lenguaje: **Go**

La cobertura ya la mediste con Jest ([semana 14](../../week-14-coverage_y_calidad_de_suites_en_jest/README.md)), con pytest-cov ([semana 21](../../week-21-coverage_y_calidad_en_python/1-teoria/01-pytest-cov-branch-coverage-y-reportes.md)) y con JaCoCo ([semana 31](../../week-31-proyecto_integrador_java/README.md)). En Go viene en `go test`, sin plugins. Dos diferencias con lo que ya conoces: Go mide **sentencias**, no ramas, y además puede medir la cobertura de un **binario** en ejecución, no solo la de los tests. Al final, los linters: `go vet` ya lo usas, y `staticcheck` encuentra lo que `vet` deja pasar.

---

## 🎯 Objetivos

- Medir la cobertura con `-cover` y `-coverprofile`, y leerla con `go tool cover`.
- Elegir el modo de cobertura y saber por qué `-race` obliga a usar `atomic`.
- Contar la cobertura de varios paquetes con `-coverpkg`.
- Medir la cobertura de un binario con `go build -cover` y `GOCOVERDIR`, y sumarla a la de los tests.
- Ejecutar `staticcheck` como herramienta del módulo y `golangci-lint` como agregador de linters.

---

## 1. Medir y leer

```bash
go test -cover ./...
#	bootcamp/lab04/cmd/fees		coverage: 0.0% of statements
# ok  	bootcamp/lab04/fees	0.003s	coverage: 30.0% of statements
```

Un paquete sin tests, como `cmd/fees`, aparece con `0.0%`: desde Go 1.22 cuenta como no cubierto, en lugar de salir como `[no test files]`.

Para ver **qué** falta, guarda el perfil y léelo:

```bash
go test -coverprofile=cover.out ./...
go tool cover -func=cover.out      # porcentaje por función
go tool cover -html=cover.out      # el código en el navegador: verde cubierto, rojo no
```

```text
bootcamp/lab04/fees/fees.go:19:		LateFee		60.0%
bootcamp/lab04/fees/fees.go:30:		Receipt		0.0%
total:					(statements)	13.6%
```

`-coverprofile` ya implica `-cover`. Con `-html=cover.out -o cover.html`, el informe se guarda en un archivo en lugar de abrir el navegador.

Go cuenta **sentencias** ejecutadas. Un `if` sin `else` cuya condición nunca fue falsa sale cubierto al 100 %, porque todas sus sentencias se ejecutaron alguna vez. pytest-cov con `--branch` y JaCoCo sí distinguen las ramas. En Go, las ramas que nunca se tomaron se buscan leyendo el informe HTML y pensando en los casos límite.

---

## 2. Los tres modos

| `-covermode` | Qué guarda cada sentencia | Cuándo |
|---|---|---|
| `set` | Si se ejecutó o no | Por defecto sin `-race` |
| `count` | Cuántas veces se ejecutó | Para ver en el HTML qué código está caliente (más intenso cuantas más veces) |
| `atomic` | Lo mismo que `count`, con contadores atómicos | Por defecto con `-race`, y obligatorio: `-covermode must be "atomic", not "set", when -race is enabled` |

Con `-race`, el detector vería los contadores de cobertura como carreras de datos si varias goroutines los incrementaran sin sincronizar. Por eso `atomic` es más lento, pero es el único modo correcto en código concurrente. En CI, la suite se ejecuta con `-race -covermode=atomic`.

---

## 3. Varios paquetes: -coverpkg

Por defecto, cada paquete solo cuenta la cobertura de su propio código. Un test del paquete `api` que ejecuta código del paquete `fees` no suma nada a `fees`. Con `-coverpkg`, los paquetes indicados se instrumentan en todos los tests:

```bash
go test -coverpkg=./... -cover ./...
# ok  	bootcamp/lab04/fees	0.003s	coverage: 40.9% of statements in ./...
```

El porcentaje cambia de significado: ahora es la parte de **todo el módulo** que esa suite ejecuta. Sirve para medir cuánto cubren los tests de integración, que atraviesan varios paquetes (en el proyecto, los de `pgstore` también ejecutan `museum.Lend`).

---

## 4. La cobertura de un binario

`main` casi nunca tiene tests unitarios: lee argumentos, llama al resto y termina con `os.Exit`. Desde Go 1.20 se puede compilar el binario con instrumentación de cobertura y ejecutarlo como lo haría un usuario o un test de extremo a extremo:

```bash
go build -cover -o bin/fees ./cmd/fees
GOCOVERDIR=covdata/e2e ./bin/fees "ana lucía" 3
go tool covdata percent -i=covdata/e2e
#	bootcamp/lab04/cmd/fees		coverage: 91.7% of statements
#	bootcamp/lab04/fees		coverage: 80.0% of statements
```

| Pieza | Qué hace |
|---|---|
| `go build -cover` | Compila el binario con contadores de cobertura |
| `GOCOVERDIR=dir` | Carpeta donde el binario escribe sus contadores al terminar. Sin la variable, no escribe nada |
| `go tool covdata percent -i=dir` | Porcentaje por paquete a partir de esos contadores |
| `go test -cover ./... -args -test.gocoverdir=dir` | Hace que los tests escriban sus contadores en el mismo formato |
| `go tool covdata textfmt -i=dir1,dir2 -o all.out` | Suma varias carpetas y las convierte al formato de `-coverprofile`, para usar `go tool cover -func` o `-html` |

Cada ejecución del binario añade sus archivos `covcounters.*` a la carpeta, y `covdata` las suma todas. Así, la cobertura de los tests unitarios y la de las ejecuciones del binario se combinan en un solo informe ([lab 04, paso 3](../2-practicas/lab-04-cobertura-y-linters/README.md#paso-3-la-cobertura-del-binario)).

### Lo que la cobertura no dice

La cobertura cuenta sentencias **ejecutadas**, no comprobadas. Un test que llama a `Receipt` y no mira el resultado cubre las mismas líneas que uno que lo compara ([experimento 3 del lab 04](../2-practicas/lab-04-cobertura-y-linters/README.md#experimento-3-un-test-que-no-comprueba-nada)). Un porcentaje alto con tests débiles es lo que el mutation testing de la [semana 21](../../week-21-coverage_y_calidad_en_python/1-teoria/02-mutation-testing-con-mutmut.md) pone en evidencia. La cobertura sirve para encontrar código que **ningún** test ejecuta, no para demostrar que el resto está bien probado.

---

## 5. Linters

`go vet` revisa errores seguros (un `Printf` con argumentos que no cuadran, un `f.Add` del tipo equivocado) y `go test` ejecuta una parte de sus comprobaciones. Deja pasar mucho de lo que un revisor señalaría.

### staticcheck

[staticcheck](https://staticcheck.dev) es el linter más usado de Go. Se instala como herramienta del módulo, con la versión fija en `go.mod`:

```bash
go get -tool honnef.co/go/tools/cmd/staticcheck@v0.8.2
go tool staticcheck ./...
# cmd/fees/main.go:17:2: this value of err is never used (SA4006)
# fees/fees.go:15:23: error strings should not be capitalized (ST1005)
# fees/fees.go:35:41: strings.Title has been deprecated since Go 1.18 ... (SA1019)
# fees/fees.go:38:6: func roundUp is unused (U1000)
```

`go vet ./...` no informa de ninguno de los cuatro. El código entre paréntesis dice qué grupo de comprobaciones lo encontró:

| Prefijo | Qué busca | Ejemplo |
|---|---|---|
| `SA` | Errores probables | Un error que se asigna y nunca se mira; una función obsoleta |
| `S` | Código que se puede simplificar | Un `if x { return true }; return false` |
| `ST` | Estilo de Go | Mensajes de error con mayúscula inicial |
| `U1000` | Código sin usar | Una función privada que nadie llama |

`go tool staticcheck -explain SA4006` explica cada comprobación. La versión `v0.8.2` del módulo es la que staticcheck publica como `2026.2.2`.

### golangci-lint

[golangci-lint](https://golangci-lint.run) ejecuta muchos linters en una pasada, con una configuración común (`.golangci.yml`) y un solo informe. Es lo que suelen ejecutar los pipelines de CI. Sin configuración, activa cinco: `errcheck`, `govet`, `ineffassign`, `staticcheck` y `unused`.

No se instala como herramienta del módulo: sus dependencias se mezclarían con las del proyecto. Se instala como binario aparte:

```bash
go install github.com/golangci/golangci-lint/v2/cmd/golangci-lint@v2.15.0
golangci-lint run ./...
```

Sobre el mismo código encuentra los mismos cuatro problemas. El de `err` lo informa `ineffassign` en lugar de `SA4006`, porque golangci-lint quita los avisos repetidos ([experimento 4 del lab 04](../2-practicas/lab-04-cobertura-y-linters/README.md#experimento-4-golangci-lint)). Su documentación recomienda instalar el binario publicado en lugar de compilarlo con `go install`. Las dos formas dan la misma versión.

| Herramienta | Cuándo |
|---|---|
| `go vet ./...` | Siempre; ya viene con Go |
| `go tool staticcheck ./...` | En cada proyecto: una dependencia de herramienta, con versión fija |
| `golangci-lint run` | Cuando el equipo quiere varios linters con una configuración común, normalmente en CI |

---

## Resumen

| Comando | Para qué |
|---|---|
| `go test -coverprofile=cover.out ./...` | Guardar la cobertura |
| `go tool cover -func=cover.out` / `-html=cover.out` | Leerla por función o sobre el código |
| `go test -race -covermode=atomic ./...` | Cobertura correcta en código concurrente |
| `go test -coverpkg=./... ./...` | Contar lo que cada suite ejecuta de todo el módulo |
| `go build -cover` + `GOCOVERDIR` | Cobertura de un binario en ejecución |
| `go tool covdata textfmt -i=a,b -o all.out` | Sumar coberturas de varias fuentes |
| `go tool staticcheck ./...` | Errores y estilo que `go vet` no ve |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 03 - Benchmarks y benchstat](03-benchmarks-y-benchstat.md) | [Índice de labs](../2-practicas/README.md) → |

[↑ Semana 35](../README.md)
