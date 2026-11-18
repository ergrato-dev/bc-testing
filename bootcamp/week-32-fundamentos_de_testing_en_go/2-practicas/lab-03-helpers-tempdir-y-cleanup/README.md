# Lab 03 - Helpers, t.TempDir y t.Cleanup

## Objetivo

Sustituir un test que deja basura en `/tmp` por uno con `t.TempDir`, sacar la lectura y la comparación del informe a helpers con `t.Helper`, cerrar el archivo con `t.Cleanup` y probar un contexto cancelado con `t.Context`.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
go test -count=1 ./...
# ok  	bootcamp/lab03	0.003s
```

`report.go` tiene `WriteOverdue(ctx, dir, loans)`: escribe `dir/overdue.csv` con una cabecera y los préstamos con retraso, y devuelve la ruta. Si el contexto ya está cancelado, no escribe nada y devuelve el error del contexto.

## Paso a paso

### Paso 0: el test que deja basura

`TestWriteOverdueManualDir` crea un directorio con `os.MkdirTemp`, escribe el informe y comprueba que contiene `Ulises`. Pasa, pero nadie borra el directorio. Ejecuta la suite otra vez y cuenta:

```bash
go test -count=1 ./... > /dev/null
ls -d /tmp/lab03-*
# /tmp/lab03-3457332234
# /tmp/lab03-4075294661
```

Un directorio por ejecución. Bórralos con `rm -r /tmp/lab03-*`. El test tiene otro problema: `strings.Contains` comprueba una línea, no el informe entero.

### Paso 1: helpers y t.TempDir (PASO 1)

1. En el import, descomenta `"io"` y `"slices"`.
2. Descomenta el bloque del PASO 1: `openReport`, `readLines`, `assertLines` y `TestWriteOverdue`.
3. Ejecuta:

   ```bash
   go test -count=1 -v ./...
   # --- PASS: TestWriteOverdueManualDir (0.00s)
   # --- PASS: TestWriteOverdue (0.00s)
   # ok  	bootcamp/lab03	0.004s
   ```

Lee `TestWriteOverdue`:

| Línea | Qué hace |
|---|---|
| `t.Context()` | Un contexto que se cancela al terminar el test ([teoría 03](../../1-teoria/03-helpers-y-caja-negra.md#tcontext)) |
| `t.TempDir()` | Un directorio nuevo que `go test` borra al terminar el test |
| `readLines(t, path)` | Abre el informe con `openReport`, lo lee entero y lo parte en líneas |
| `assertLines(t, got, want)` | Compara las dos listas con `slices.Equal` y muestra las dos si no coinciden |

Los tres helpers empiezan con `t.Helper()` y reciben `t` como primer argumento.

### Paso 2: el mismo helper en otro test (PASO 2)

Descomenta `TestWriteOverdueWithoutLateLoans`. Pasa a `WriteOverdue` solo el préstamo sin retraso, `loans[1:2]`, y espera solo la cabecera. Reutiliza los helpers sin repetir la lectura del archivo.

```bash
go test -count=1 -run WithoutLate -v ./...
# --- PASS: TestWriteOverdueWithoutLateLoans (0.00s)
```

### Paso 3: dónde está el directorio

Añade `t.Log` para ver el directorio de `TestWriteOverdue`. Al principio del test escribe:

```go
dir := t.TempDir()
t.Log(dir)
```

y pasa `dir` a `WriteOverdue` en lugar de `t.TempDir()`:

```bash
go test -count=1 -v -run '^TestWriteOverdue$' ./...
# === RUN   TestWriteOverdue
#     report_test.go:71: /tmp/TestWriteOverdue1454083705/001
# --- PASS: TestWriteOverdue (0.00s)
ls /tmp/TestWriteOverdue1454083705/001
# ls: cannot access '/tmp/TestWriteOverdue1454083705/001': No such file or directory
```

El nombre lleva el del test, y el directorio ya no existe cuando el test termina. Usa la ruta que te salga a ti. Deshaz el cambio.

### Paso 4: un contexto cancelado (PASO 4)

1. En el import, descomenta `"errors"`.
2. Descomenta `TestWriteOverdueStopsWhenContextIsCanceled`. Deriva un contexto de `t.Context()`, lo cancela antes de llamar y espera `context.Canceled`.
3. Borra `TestWriteOverdueManualDir` con su comentario del PASO 0: `TestWriteOverdue` lo sustituye.
4. Ejecuta:

   ```bash
   go test -count=1 -v ./...
   # --- PASS: TestWriteOverdue (0.00s)
   # --- PASS: TestWriteOverdueWithoutLateLoans (0.00s)
   # --- PASS: TestWriteOverdueStopsWhenContextIsCanceled (0.00s)
   # ok  	bootcamp/lab03	0.005s
   ```

## Experimentos

### Experimento 1: sin t.Helper

1. En `TestWriteOverdue`, cambia la línea esperada `"Ulises,eva,12"` por `"Ulises,eva,11"`:

   ```bash
   go test ./...
   # --- FAIL: TestWriteOverdue (0.00s)
   #     report_test.go:55: lines = ["title,member,days_late" "Dune,ana,3" "Ulises,eva,12"], want ["title,member,days_late" "Dune,ana,3" "Ulises,eva,11"]
   ```

   La línea 55 es la llamada a `assertLines` dentro de `TestWriteOverdue`.

2. Borra la línea `t.Helper()` de `assertLines` y ejecuta otra vez:

   ```bash
   go test ./...
   # --- FAIL: TestWriteOverdue (0.00s)
   #     report_test.go:45: lines = ["title,member,days_late" "Dune,ana,3" "Ulises,eva,12"], want ["title,member,days_late" "Dune,ana,3" "Ulises,eva,11"]
   ```

   Ahora la línea 45 es el `t.Errorf` dentro de `assertLines`. Si fallaran los dos tests que la usan, los dos mensajes dirían 45. Deshaz los dos cambios.

### Experimento 2: defer en lugar de t.Cleanup

En `openReport`, cambia `t.Cleanup(func() { f.Close() })` por `defer f.Close()`:

```bash
go test ./...
# --- FAIL: TestWriteOverdue (0.00s)
#     report_test.go:55: reading report: read /tmp/TestWriteOverdue1215630636/001/overdue.csv: file already closed
# --- FAIL: TestWriteOverdueWithoutLateLoans (0.00s)
#     report_test.go:68: reading report: read /tmp/TestWriteOverdueWithoutLateLoans1462025452/001/overdue.csv: file already closed
```

`defer` cerró el archivo al salir de `openReport`, antes de que `readLines` lo leyera. `t.Cleanup` lo cierra al terminar el test. Fíjate en las líneas: 55 y 68 son las llamadas de cada test, porque `readLines` y `openReport` tienen `t.Helper()`. Deshaz el cambio.

## Comprobación final

```bash
go vet ./... && go test -count=1 ./...
# ok  	bootcamp/lab03	0.005s
ls -d /tmp/lab03-* 2>/dev/null | wc -l
# 0
```

## Preguntas de cierre

1. ¿Por qué `openReport` registra el cierre con `t.Cleanup` en lugar de devolver una función para que el test la llame?
2. `t.Context()` se cancela antes de las funciones de `t.Cleanup`. ¿Qué pasaría si una de ellas necesitara ese contexto para borrar un registro de una base de datos?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Table-driven tests y subtests](../lab-02-table-driven-y-subtests/README.md) | [Lab 04 - Caja negra y examples](../lab-04-caja-negra-y-examples/README.md) → |

[↑ Semana 32](../../README.md)
