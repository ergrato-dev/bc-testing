# Lab 02 - Table-driven tests y subtests

## Objetivo

Convertir cuatro tests copiados y pegados en un table-driven test, ejecutar una fila concreta con `-run`, ejecutar las filas en paralelo y ver qué pasa con la variable del bucle cuando el `go.mod` dice `go 1.21`.

## Tiempo estimado

70 minutos.

## Preparación

```bash
cd starter
go test -v ./...
# --- PASS: TestLateFeeBasicThreeDays (0.00s)
# --- PASS: TestLateFeeBasicCapped (0.00s)
# --- PASS: TestLateFeePremiumWithinGrace (0.00s)
# --- PASS: TestLateFeePremiumAfterGrace (0.00s)
# --- PASS: TestLateFeeRejectsNegativeDays (0.00s)
# ok  	bootcamp/lab02	0.003s
```

Las salidas de `-v` de este lab muestran solo las líneas `---` y la final; `=== RUN` aparece antes de cada test.

`fees.go` calcula la multa según el tipo de socio:

| Socio | Regla |
|---|---|
| `Basic` | 50 por día, hasta 1000 |
| `Premium` | 2 días de gracia, después 25 por día, hasta 500 |

Abre `fees_test.go`: los cuatro primeros tests son el mismo test con otros números. Para añadir un caso hay que copiar ocho líneas y cambiar tres sitios. Es lo que la tabla resuelve.

## Paso a paso

### Paso 1: la tabla (PASO 1)

1. Descomenta el bloque del PASO 1, `TestLateFee`. La línea `// t.Parallel()` queda comentada: es del PASO 3.
2. Ejecuta:

   ```bash
   go test -v ./...
   # --- PASS: TestLateFeeBasicThreeDays (0.00s)
   # --- PASS: TestLateFeeBasicCapped (0.00s)
   # --- PASS: TestLateFeePremiumWithinGrace (0.00s)
   # --- PASS: TestLateFeePremiumAfterGrace (0.00s)
   # --- PASS: TestLateFee (0.00s)
   #     --- PASS: TestLateFee/basic_on_time (0.00s)
   #     --- PASS: TestLateFee/basic_three_days (0.00s)
   #     --- PASS: TestLateFee/basic_just_below_cap (0.00s)
   #     --- PASS: TestLateFee/basic_capped (0.00s)
   #     --- PASS: TestLateFee/premium_within_grace (0.00s)
   #     --- PASS: TestLateFee/premium_after_grace (0.00s)
   #     --- PASS: TestLateFee/premium_capped (0.00s)
   # --- PASS: TestLateFeeRejectsNegativeDays (0.00s)
   # ok  	bootcamp/lab02	0.003s
   ```

   Cada fila es un subtest con el nombre de su campo `name`, con `_` en lugar de espacios. La tabla cubre los cuatro casos de los tests repetidos y tres más: el día cero, la frontera antes del tope y el tope de `Premium`.

3. Borra los cuatro tests repetidos, de `TestLateFeeBasicThreeDays` a `TestLateFeePremiumAfterGrace`. Deja el comentario del PASO 1 y `TestLateFee`.

`TestLateFeeRejectsNegativeDays` también recorre una lista, pero sin `t.Run`: los dos casos comparten test. Si uno falla, el mensaje dice cuál por el `%v` del socio, pero no puedes ejecutarlo solo. Para dos casos que no se van a ejecutar por separado basta.

### Paso 2: ejecutar una fila

`-run` recibe una expresión regular por nivel, separadas por `/`:

```bash
go test -run 'TestLateFee/premium' -v ./...
# --- PASS: TestLateFee (0.00s)
#     --- PASS: TestLateFee/premium_within_grace (0.00s)
#     --- PASS: TestLateFee/premium_after_grace (0.00s)
#     --- PASS: TestLateFee/premium_capped (0.00s)
# --- PASS: TestLateFeeRejectsNegativeDays (0.00s)
```

Las tres filas de `premium`, y también `TestLateFeeRejectsNegativeDays`: su nombre contiene `TestLateFee`, y como no tiene subtests no hay segundo nivel que filtrar. Ancla el primer nivel con `^` y `$`:

```bash
go test -run '^TestLateFee$/premium' -v ./...
# --- PASS: TestLateFee (0.00s)
#     --- PASS: TestLateFee/premium_within_grace (0.00s)
#     --- PASS: TestLateFee/premium_after_grace (0.00s)
#     --- PASS: TestLateFee/premium_capped (0.00s)
```

Una sola fila, con el nombre como sale en `-v`:

```bash
go test -run 'TestLateFee/^basic_capped$' -v ./...
# --- PASS: TestLateFee (0.00s)
#     --- PASS: TestLateFee/basic_capped (0.00s)
# --- PASS: TestLateFeeRejectsNegativeDays (0.00s)
```

También vale con espacios: `-run 'TestLateFee/premium after'` ejecuta `premium_after_grace`.

### Paso 3: en paralelo (PASO 3)

1. Descomenta `// t.Parallel()` dentro del `t.Run`.
2. Ejecuta y mira el principio de la salida:

   ```bash
   go test -count=1 -v ./...
   # === RUN   TestLateFee
   # === RUN   TestLateFee/basic_on_time
   # === PAUSE TestLateFee/basic_on_time
   # === RUN   TestLateFee/basic_three_days
   ```

   Cada subtest se pausa (`PAUSE`) y continúa (`CONT`) cuando el bucle termina de crearlos todos. Las líneas `--- PASS` salen en el orden en que terminan, que cambia de una ejecución a otra.

### Paso 4: orden aleatorio

```bash
go test -count=1 -shuffle=on -v ./...
# -test.shuffle 1794984235677549312
# --- PASS: TestLateFeeRejectsNegativeDays (0.00s)
# --- PASS: TestLateFee (0.00s)
```

Tu semilla será otra. `TestLateFeeRejectsNegativeDays` se ejecutó primero. Si un orden falla, `go test -shuffle=1794984235677549312 ./...` lo repite.

## Experimentos

### Experimento 1: una fila que falla

En la fila `premium after grace`, cambia `want: 75` por `want: 70`:

```bash
go test ./...
# --- FAIL: TestLateFee (0.00s)
#     --- FAIL: TestLateFee/premium_after_grace (0.00s)
#         fees_test.go:32: LateFee(5, 1) = 75, want 70
# FAIL
```

El fallo dice la fila por su nombre, y las demás filas se ejecutaron igual. El `1` es `Premium`: el `%v` de una constante `iota` imprime su número. No deshagas el cambio todavía.

### Experimento 2: go 1.21 y la variable del bucle

Con la fila mal del experimento anterior, cambia en `go.mod` la línea `go 1.26.5` por `go 1.21`:

```bash
go test ./...
# ok  	bootcamp/lab02	0.002s
```

Verde con una fila mal. Con `go 1.21`, `tt` es una sola variable para todo el bucle. Los subtests paralelos esperan a que el bucle termine, y cuando se ejecutan `tt` ya es la última fila, `premium capped`. Las siete filas comprueban la misma, que está bien ([teoría 02](../../1-teoria/02-table-driven-y-subtests.md#la-variable-del-bucle-go-122)).

`go vet` lo detecta:

```bash
go vet ./...
# fees_test.go:27:24: loop variable tt captured by func literal
# fees_test.go:27:33: loop variable tt captured by func literal
# fees_test.go:29:52: loop variable tt captured by func literal
# ...
```

Vuelve a `go 1.26.5` y a `want: 75`. Si mantienes código con `go 1.21` o anterior, el arreglo es `tt := tt` como primera línea del bucle.

### Experimento 3: dos filas con el mismo nombre

Cambia el `name` de la fila `premium capped` por `premium after grace`:

```bash
go test -run '^TestLateFee$' -v ./...
#     --- PASS: TestLateFee/premium_after_grace#01 (0.00s)
#     --- PASS: TestLateFee/premium_after_grace (0.00s)
```

No es un error: la segunda recibe el sufijo `#01`. Con `-run 'TestLateFee/premium_after_grace'` se ejecutan las dos. Deshaz el cambio.

## Comprobación final

```bash
go vet ./... && go test -count=1 -v ./... | grep -c -- '--- PASS'
# 9
```

Nueve: `TestLateFee`, sus siete filas y `TestLateFeeRejectsNegativeDays`.

## Preguntas de cierre

1. ¿Cuándo dejarías un bucle sin `t.Run`, como en `TestLateFeeRejectsNegativeDays`, y cuándo no?
2. Un compañero ve `tt := tt` en el bucle y lo borra. ¿Qué tiene que mirar en el `go.mod` antes?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Primer test con go test](../lab-01-primer-test-con-go-test/README.md) | [Lab 03 - Helpers, t.TempDir y t.Cleanup](../lab-03-helpers-tempdir-y-cleanup/README.md) → |

[↑ Semana 32](../../README.md)
