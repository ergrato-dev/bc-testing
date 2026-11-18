# Lab 04 - go-cmp y golden files

## Objetivo

Comparar un resumen entero con `cmp.Diff`, ajustar la comparación con `cmpopts`, fijar la aleatoriedad con una semilla, y probar el texto del resumen contra un golden file que se regenera con `-update`.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
go mod download
go test -count=1 -v ./...
# --- PASS: TestBuildTotal (0.00s)
# ok  	bootcamp/lab04	0.002s
```

`report.go` tiene dos funciones:

| Función | Qué hace |
|---|---|
| `Build(loans, now, catalog, r)` | Calcula el `Summary` a la hora `now`: una `Line` por préstamo vencido (50 por día de retraso), ordenadas del más antiguo al más reciente, el total, y un libro destacado elegido al azar del catálogo con `r` |
| `Summary.Render()` | Devuelve el resumen como texto, listo para enviarlo por correo |

`Line` tiene un campo no exportado, `due`: la fecha de vencimiento, que `Build` usa para ordenar y que no se muestra.

En `report_test.go`, `seeded()` devuelve un generador con semilla fija, `rand.New(rand.NewPCG(1, 2))`: con la misma semilla da siempre la misma secuencia ([teoría 01](../../1-teoria/01-interfaces-y-dobles-a-mano.md#el-reloj-y-la-aleatoriedad-también-son-dependencias)). `TestBuildTotal` solo comprueba dos campos.

## Paso a paso

### Paso 1: el resumen entero con cmp.Diff (PASO 1)

1. En el import, descomenta `cmp` y `cmpopts`.
2. Descomenta el bloque del PASO 1 y guarda.
3. Ejecuta:

   ```bash
   go test -count=1 -v ./...
   # --- PASS: TestBuildTotal (0.00s)
   # --- PASS: TestBuild (0.00s)
   # ok  	bootcamp/lab04	0.004s
   ```

`TestBuild` construye el `Summary` esperado entero y lo compara con `cmp.Diff(want, got, cmpopts.IgnoreUnexported(Line{}))`. La opción ignora `due`: el test comprueba lo que se ve del resumen, no cómo se ordena por dentro. `Featured: "Dune"` es lo que elige la semilla `(1, 2)` entre los cuatro libros del catálogo.

### Paso 2: una lista vacía no es una lista nil (PASO 2)

Descomenta el bloque del PASO 2:

```bash
go test -count=1 -run Without -v ./...
# --- PASS: TestBuildWithoutOverdueLoans (0.00s)
# ok  	bootcamp/lab04	0.003s
```

Con solo el préstamo de Emma, que no está vencido, `Build` no añade ninguna fila y `Lines` se queda en `nil`. El test espera `[]Line{}`, que es lo que escribirías al leer el caso. `cmpopts.EquateEmpty()` trata los dos como iguales.

### Paso 3: el texto contra un golden file (PASO 3)

1. En el import, descomenta `"flag"`, `"os"` y `"path/filepath"`.
2. Descomenta el bloque del PASO 3 y guarda.
3. Ejecuta. La primera vez falla, porque el golden file todavía no existe:

   ```bash
   go test -count=1 ./...
   # --- FAIL: TestRender (0.00s)
   #     report_test.go:87: reading golden file: open testdata/TestRender.golden: no such file or directory (run go test -update)
   ```

4. Créalo con `-update`. El paquete va antes del flag ([teoría 03](../../1-teoria/03-go-cmp-y-golden-files.md#dónde-va--update)):

   ```bash
   go test -count=1 . -update
   # ok  	bootcamp/lab04	0.004s
   ```

5. **Lee el archivo** antes de darlo por bueno:

   ```bash
   cat testdata/TestRender.golden
   # Préstamos vencidos a 2026-03-10
   #
   # Ulises     eva     12 días    600
   # Dune       ana      3 días    150
   #
   # Total: 750
   # Libro destacado: Dune
   ```

   Comprueba cada número: 12 días × 50 = 600, 3 × 50 = 150, total 750. Desde ahora, este archivo es lo esperado.

6. Ejecuta sin `-update`:

   ```bash
   go test -count=1 -v ./...
   # --- PASS: TestBuildTotal (0.00s)
   # --- PASS: TestBuild (0.00s)
   # --- PASS: TestBuildWithoutOverdueLoans (0.00s)
   # --- PASS: TestRender (0.00s)
   # ok  	bootcamp/lab04	0.005s
   ```

## Experimentos

### Experimento 1: reflect.DeepEqual

En `TestBuild`, añade `"reflect"` al import y cambia la comparación por la de la librería estándar:

```go
if !reflect.DeepEqual(want, got) {
	t.Errorf("Build() = %+v, want %+v", got, want)
}
```

```bash
go test -count=1 -run '^TestBuild$' ./...
# --- FAIL: TestBuild (0.00s)
#     report_test.go:49: Build() = {Date:2026-03-10 Lines:[{Title:Ulises Member:eva DaysLate:12 Fee:600 due:{wall:0 ext:63907693200 loc:<nil>}} {Title:Dune Member:ana DaysLate:3 Fee:150 due:{wall:0 ext:63908470800 loc:<nil>}}] Total:750 Featured:Dune}, want {Date:2026-03-10 Lines:[{Title:Ulises Member:eva DaysLate:12 Fee:600 due:{wall:0 ext:0 loc:<nil>}} {Title:Dune Member:ana DaysLate:3 Fee:150 due:{wall:0 ext:0 loc:<nil>}}] Total:750 Featured:Dune}
```

Falla por `due`, un campo interno que el test no quería comprobar, y el mensaje te deja buscar la diferencia a ojo en dos líneas enormes. Deshaz el cambio.

### Experimento 2: sin IgnoreUnexported

Quita `cmpopts.IgnoreUnexported(Line{})` de `TestBuild`, dejando `cmp.Diff(want, got)`:

```bash
go test -count=1 -run '^TestBuild$' ./...
# --- FAIL: TestBuild (0.00s)
# panic: cannot handle unexported field at {report.Summary}.Lines[1].due:
# 		"bootcamp/lab04".Line
# 	consider using cmpopts.EquateComparable to compare comparable Go types [recovered, repanicked]
```

`cmp` no compara un campo no exportado por su cuenta: te obliga a decidir si cuenta. Deshaz el cambio.

### Experimento 3: sin EquateEmpty

Quita `cmpopts.EquateEmpty()` de `TestBuildWithoutOverdueLoans`:

```bash
go test -count=1 -run Without ./...
# --- FAIL: TestBuildWithoutOverdueLoans (0.00s)
#     report_test.go:57: Build() mismatch (-want +got):
#           report.Summary{
#           	Date:     "2026-03-10",
#         - 	Lines:    []report.Line{},
#         + 	Lines:    nil,
#           	Total:    0,
#           	Featured: "",
#           }
```

Así se lee un diff de `cmp`: el valor entero, con `-` en lo esperado y `+` en lo obtenido solo en el campo que cambia. Deshaz el cambio.

### Experimento 4: sin semilla fija

En `seeded()`, cambia la semilla fija por una que depende de la hora:

```go
func seeded() *rand.Rand { return rand.New(rand.NewPCG(uint64(time.Now().UnixNano()), 2)) }
```

Ejecuta `TestBuild` 20 veces y cuenta los fallos:

```bash
go test -count=20 -run '^TestBuild$' ./... | grep -c -- '--- FAIL'
# 15
```

A ti te saldrá otro número, cerca de 15: con cuatro libros en el catálogo, la elección acierta `Dune` una de cada cuatro veces. Es un test inestable (*flaky*). Deshaz el cambio.

### Experimento 5: un cambio en la salida

En `Render`, cambia `"\nTotal: %d\n"` por `"\nTotal a pagar: %d\n"`:

```bash
go test -count=1 ./...
# --- FAIL: TestRender (0.00s)
#     report_test.go:87: testdata/TestRender.golden mismatch (-want +got):
#           strings.Join({
#           	"Préstamos vencidos a 2026-03-10\n\nUlises     eva     12 días   ",
#           	" 600\nDune       ana      3 días    150\n\nTotal",
#         + 	" a pagar",
#           	": 750\nLibro destacado: Dune\n",
#           }, "")
```

El diff señala lo que cambió dentro del texto. Si el cambio es correcto, regenera el golden file con `go test -count=1 . -update`, léelo y haz commit de los dos archivos juntos. Aquí no lo es: deshaz el cambio en `Render` y ejecuta `go test -count=1 ./...` para comprobar que pasa con el golden file de antes.

## Comprobación final

```bash
go vet ./... && go test -count=1 ./...
# ok  	bootcamp/lab04	0.004s
```

## Preguntas de cierre

1. ¿Qué ventaja tiene `cmp.Diff` frente a varios `if got.X != want.X` cuando el struct tiene diez campos?
2. ¿Por qué el golden file se lee antes del commit, y qué pasa si alguien ejecuta `-update` sin leerlo?
3. `Featured` depende del azar. Además de fijar la semilla, ¿qué otras dos formas tendrías de probar `Build` sin que el test sea inestable?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - assert, require y testify/mock](../lab-03-assert-require-y-testify-mock/README.md) | [Proyecto — Proyecto Semanal - El servicio del dominio con dobles de prueba](../../3-proyecto/README.md) → |

[↑ Semana 33](../../README.md)
