# Proyecto Integrador - Suite Go completa del dominio

> **🎯 ÚNICO ENTREGABLE**: este proyecto es el único entregable obligatorio de la semana.

## Objetivo

Cerrar la etapa de Go con una sola suite en `go/` de tu repositorio:

- unitarios con tablas, dobles (mockgen) y `httptest`, e integración con PostgreSQL bajo la tag `integration`;
- el test concurrente, los fuzz tests y el benchmark de la semana 35;
- un solo comando, `verify.sh`, que ejecuta todo con `-race` y falla si la cobertura baja del 85 %;
- un reporte `testing-report-go.md` con datos reales.

## Dónde va

En tu **[proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)**, el repositorio `qa-<nombre>-<apellido>`, dentro de `go/`. Partes de tu entrega de la [semana 35](../../week-35-concurrencia_y_calidad_en_go/3-proyecto/README.md).

Los ejemplos usan el dominio del museo: paquetes `museum` y `pgstore`. Tus paquetes y tus cifras serán distintos.

## Tiempo

| Parte | Tiempo |
|---|---|
| Desarrollo: pasos 1 a 5 | 6.5 h |
| Presentación: paso 6 | 0.5 h |

## Paso a paso

### Paso 1: inventario y rama

1. Crea la rama de la semana:

   ```bash
   cd qa-<nombre>-<apellido>
   git switch main && git pull
   git switch -c week-36
   ```

2. Copia el starter sin sobrescribir nada:

   ```bash
   cp -rn <ruta-al-bootcamp>/bootcamp/week-36-proyecto_integrador_go/3-proyecto/starter/. .
   ```

   | Archivo | Qué es |
   |---|---|
   | `go/verify.sh` | El script del [lab 01](../2-practicas/lab-01-una-suite-un-comando/README.md) con el análisis y los unitarios, y un `TODO` por cada fase que falta |
   | `go/testing-report-go.md` | La plantilla del reporte, con `TODO` en cada sección |

   Si `cp` no conserva el permiso de ejecución, `chmod +x go/verify.sh`.

3. Haz el inventario de tus archivos de test. Para cada uno, anota qué pieza de la etapa contiene y en qué fase se ejecuta:

   | Si el archivo... | Fase |
   |---|---|
   | No tiene build tag: tablas, ejemplos, fakes, mocks, `httptest`, fuzz, benchmarks | Unitaria: `go test ./...` |
   | Empieza por `//go:build integration` y arranca un contenedor | Integración: `go test -tags integration ./...` |

   En el museo:

   | Archivo | Piezas | Fase |
   |---|---|---|
   | `museum/fees_test.go`, `piece_test.go`, `example_test.go` | Tablas, subtests, ejemplos, `BenchmarkValidatePiece` | Unitaria |
   | `museum/lending_test.go`, `fakes_test.go`, `mock_notifier_test.go` | Fake, stub, mock de mockgen, golden file | Unitaria |
   | `museum/api_test.go` | `httptest` y el test concurrente | Unitaria |
   | `museum/piece_fuzz_test.go`, `testdata/fuzz/` | Fuzz tests y el hallazgo de la semana 35 | Unitaria |
   | `pgstore/pgstore_test.go` | `TestMain` con testcontainers-go y PostgreSQL | Integración |

   Si te falta alguna pieza (por ejemplo, el mock o el hallazgo del fuzzer), vuelve a la semana donde se pedía: la verificación las busca.

### Paso 2: `verify.sh` completo

Referencia: [lab 01](../2-practicas/lab-01-una-suite-un-comando/README.md) y [teoría 01](../1-teoria/01-la-suite-go-completa.md#un-comando-verifysh).

1. Sustituye el `TODO` de staticcheck por las dos ejecuciones de la [semana 35](../../week-35-concurrencia_y_calidad_en_go/3-proyecto/README.md), con y sin la tag:

   ```bash
   go tool staticcheck ./...
   go tool staticcheck -tags integration ./...
   ```

2. Sustituye el `TODO` de la integración:

   ```bash
   echo "== unitarios + integración, con cobertura"
   go test -count=1 -race -shuffle=on -tags integration -covermode=atomic -coverpkg=./... -coverprofile=cover.out ./...
   ```

3. Sustituye el `TODO` de los benchmarks. `-run='^$'` no ejecuta ningún test y `-benchtime=1x` ejecuta cada benchmark una sola vez: no mide, comprueba que los benchmarks siguen funcionando.

   ```bash
   echo "== benchmarks (una iteración)"
   go test -run='^$' -bench=. -benchtime=1x ./...
   ```

4. Con Docker en marcha, ejecuta desde `go/`:

   ```bash
   cd go
   ./verify.sh
   # == go vet y staticcheck
   # == unitarios
   # ok  	example.com/qa-museo/go/museum	1.040s
   # ?   	example.com/qa-museo/go/pgstore	[no test files]
   # == unitarios + integración, con cobertura
   # ok  	example.com/qa-museo/go/museum	1.043s	coverage: 67.5% of statements in ./...
   # ok  	example.com/qa-museo/go/pgstore	4.340s	coverage: 39.8% of statements in ./...
   # == benchmarks (una iteración)
   # BenchmarkValidatePiece-8   	       1	      1993 ns/op
   # PASS
   # ok  	example.com/qa-museo/go/museum	0.008s
   # ?   	example.com/qa-museo/go/pgstore	[no test files]
   ```

   (Se omiten las líneas `goos`, `goarch`, `pkg` y `cpu`.) Las semillas de los fuzz tests y los archivos de `testdata/fuzz/` se ejecutan en las dos fases de tests, como tests normales: `verify.sh` no necesita `-fuzz`.

### Paso 3: el umbral

1. Sustituye el último `TODO` por el umbral del lab 01:

   ```bash
   echo "== umbral de cobertura"
   total=$(go tool cover -func=cover.out | awk '/^total:/ {print $3+0}')
   echo "total: $total % (mínimo: 85 %)"
   awk -v total="$total" 'BEGIN {if (total < 85) {print "FALLO: la cobertura no llega al 85 %"; exit 1}}'
   ```

2. Ejecuta y comprueba el código de salida:

   ```bash
   ./verify.sh; echo "exit=$?"
   # ...
   # == umbral de cobertura
   # total: 89.4 % (mínimo: 85 %)
   # exit=0
   ```

3. Comprueba cuánto cubre solo la fase unitaria. El perfil va a otro archivo para no pisar `cover.out`:

   ```bash
   go test -count=1 -race -covermode=atomic -coverpkg=./... -coverprofile=unit.out ./...
   go tool cover -func=unit.out | tail -1
   # total:						(statements)	67.5%
   rm unit.out
   ```

   Sin la integración, el museo no llegaría al umbral: `pgstore` solo lo cubren los tests con PostgreSQL.

4. `cover.out` no se sube al repositorio. Si tu `.gitignore` aún no lo tiene (lo pedía la semana 35), añádelo:

   ```bash
   grep -qx 'cover.out' ../.gitignore || echo 'cover.out' >> ../.gitignore
   ```

### Paso 4: al menos el 85 %

1. Si `verify.sh` termina con `FALLO`, busca lo que falta con las herramientas del [lab 02](../2-practicas/lab-02-datos-del-reporte/README.md#paso-4-la-cobertura-de-cada-paquete):

   ```bash
   go tool cover -func=cover.out | grep -v '^total:' | sort -k3 -n | head
   go tool cover -html=cover.out
   ```

2. Escribe el test que cubre cada línea roja en la fase que corresponda a lo que necesita: si solo crea valores de Go o usa un doble, sin tag; si necesita PostgreSQL, en el archivo con `//go:build integration`. No bajes el 85 de `verify.sh`: la verificación lo busca.

3. Un 100 % no significa que no haya errores: `go tool cover` mide las sentencias que existen, no las comprobaciones que faltan. Apunta lo que encuentres para el reporte.

### Paso 5: el reporte de testing

Rellena `go/testing-report-go.md` con los datos de tu suite, como en el [lab 02](../2-practicas/lab-02-datos-del-reporte/README.md#paso-5-escribir-el-reporte):

1. **Suite por capas:** una fila por fase, con sus paquetes, funciones y subtests (`go test -count=1 -race -v` y `grep -c`) y el tiempo de `time`.
2. **Cobertura:** la tabla por paquete del `awk` del lab 02, con el total, y qué fase cubre cada paquete.
3. **Tests más lentos:** los tres primeros de `grep -E -- '--- PASS' all.log | sort -t'(' -k2 -rn | head -3`, el tiempo de cada paquete y a dónde va: `TestMain`, la pausa de `-race`.
4. **Riesgos y huecos:** al menos tres cosas que la suite no prueba y que podrían fallar en producción. Busca operaciones de varios pasos sin transacción, errores de una dependencia que solo se prueban con un mock, y caminos que no pasan nunca por la API y la base de datos a la vez.
5. **Próximos pasos:** los tests que añadirías primero, en orden.

Puedes enlazar `calidad.md` de la semana 35 para no repetir el race detector, el fuzzer y benchstat. No dejes ningún `TODO`: la verificación los busca.

### Paso 6: verificar, entregar y presentar

1. Ejecuta la verificación desde la carpeta del bootcamp, con Docker en marcha:

   ```bash
   PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 36
   ```

2. Haz commit y push de la rama y abre un pull request `week-36` → `main`.
3. Presenta tu suite en 5 minutos: las fases de `verify.sh` y qué va en cada una, la cifra de cobertura y qué fase la sostiene, dónde se va el tiempo de la suite y el riesgo más importante del reporte.

## Ejemplos de adaptación por dominio

| Dominio | Unitaria (sin tag) | Integración (`//go:build integration`) | Riesgo típico |
|---|---|---|---|
| Museo | Seguro y recargos de un préstamo, API con fake | Piezas y préstamos en PostgreSQL | El aviso falla después de abrir el préstamo |
| Planetario | Precio de una entrada con descuentos | Funciones y entradas en PostgreSQL | Dos ventas de la última butaca a la vez |
| Acuario | Capacidad de un tanque | Tanques y ejemplares en PostgreSQL | Un traslado que mueve el ejemplar y no actualiza la capacidad |

## ✅ Requisitos verificables

`./scripts/verificar-semana.sh 36` comprueba lo siguiente ([checks.json](../checks.json)):

| Comprobación | Cómo se verifica |
|---|---|
| `verify.sh` está completo | Tiene `set -euo pipefail`, staticcheck, `-race`, `-tags integration`, `-coverpkg=./...`, `-coverprofile=cover.out`, `-bench`, el 85 y ningún `TODO` |
| `verify.sh` pasa | `bash go/verify.sh` termina con 0 |
| La cobertura llega al 85 % | La línea `total:` de `go tool cover -func=cover.out` es al menos 85 |
| La suite tiene todas las piezas | Hay un mock generado por mockgen con `EXPECT()`, `httptest.`, un archivo `//go:build integration`, `postgres.Run(` de testcontainers-go, un `FuzzXxx` con su hallazgo en `testdata/fuzz/` y un `b.Loop()` |
| `testing-report-go.md` tiene sus cinco secciones y ningún `TODO` | Las cinco cabeceras `##` del starter están y no queda ningún `TODO` |

> La `solution/` del proyecto no se publica en el repositorio del bootcamp.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Los datos del reporte](../2-practicas/lab-02-datos-del-reporte/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 36](../README.md)
