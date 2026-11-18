# Lab 02 - Fuzzing nativo

## Objetivo

Dejar que el fuzzer de Go encuentre dos errores en un lector de ISBN que pasa todos los tests de su tabla: primero un `panic`, después una entrada inválida que se acepta. Cada error encontrado queda en `testdata/fuzz/` como test de regresión.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
go test -count=1 -v ./...
# === RUN   TestParse
# --- PASS: TestParse (0.00s)
# PASS
# ok  	bootcamp/lab02	0.003s
```

El módulo no tiene dependencias. `isbn.go` tiene `Parse`, que lee un ISBN-13 tal como lo teclea un usuario del catálogo:

| Entrada | Resultado |
|---|---|
| `978-84-376-0494-7`, `9788437604947` o `ISBN 978 0 14 118776 1` | Los 13 dígitos sin separadores |
| Una longitud distinta de 13 tras quitar guiones y espacios | `ErrLength` |
| Un dígito de control que no cuadra | `ErrChecksum` |

El dígito de control de un ISBN-13 se comprueba así: los 13 dígitos, multiplicados alternativamente por 1 y por 3, suman un múltiplo de 10.

`isbn_test.go` tiene `parseTests`, una tabla con seis casos, y `TestParse`, que los recorre. La tabla cubre los casos que se le ocurrirían a cualquiera, y `Parse` los pasa todos.

## Paso a paso

### Paso 1: el primer fuzz test (PASO 1)

1. Descomenta el bloque del PASO 1 y guarda.
2. Ejecuta los tests como siempre:

   ```bash
   go test -count=1 -v ./...
   # === RUN   TestParse
   # --- PASS: TestParse (0.00s)
   # === RUN   FuzzParse
   # === RUN   FuzzParse/seed#0
   # ...
   # === RUN   FuzzParse/seed#5
   # --- PASS: FuzzParse (0.00s)
   #     --- PASS: FuzzParse/seed#0 (0.00s)
   #     ...
   #     --- PASS: FuzzParse/seed#5 (0.00s)
   # PASS
   # ok  	bootcamp/lab02	0.003s
   ```

`FuzzParse` es un fuzz test ([teoría 02](../../1-teoria/02-fuzzing-nativo.md#1-anatomía-de-un-fuzz-test)). Con `f.Add`, cada entrada de la tabla pasa a ser una **semilla**. La función de `f.Fuzz` solo llama a `Parse`: la única comprobación es que no haga `panic`. Sin `-fuzz`, `go test` ejecuta la función una vez por semilla (`seed#0` a `seed#5`), como subtests.

3. Ahora arranca el fuzzer durante 30 segundos como máximo:

   ```bash
   go test -run=^$ -fuzz=FuzzParse -fuzztime=30s .
   # fuzz: elapsed: 0s, gathering baseline coverage: 0/6 completed
   # fuzz: elapsed: 0s, gathering baseline coverage: 6/6 completed, now fuzzing with 8 workers
   # fuzz: elapsed: 3s, execs: 1277518 (425778/sec), new interesting: 20 (total: 26)
   # fuzz: elapsed: 6s, execs: 2518044 (413479/sec), new interesting: 20 (total: 26)
   # fuzz: minimizing 31-byte failing input file
   # fuzz: elapsed: 7s, minimizing
   # --- FAIL: FuzzParse (6.80s)
   #     --- FAIL: FuzzParse (0.00s)
   #         testing.go:1927: panic: runtime error: slice bounds out of range [5:4]
   #             goroutine 222463 [running]:
   #             ...
   #
   #     Failing input written to testdata/fuzz/FuzzParse/ea624b8b77c021c0
   #     To re-run:
   #     go test -run=FuzzParse/ea624b8b77c021c0
   # FAIL
   # exit status 1
   # FAIL	bootcamp/lab02	6.807s
   ```

   El tiempo, el número de ejecuciones y el de núcleos (`8 workers`) dependen de tu máquina. Si en 30 segundos no aparece el fallo, vuelve a ejecutarlo: el fuzzer parte de lo que ya probó.

`-run=^$` evita que se ejecuten antes los tests normales, y `-fuzztime` pone un límite: sin él, el fuzzer no para hasta encontrar un fallo. En unos segundos y un par de millones de entradas, encontró una que hace `panic`, la **minimizó** (de 31 bytes a lo mínimo que sigue fallando) y la guardó:

```bash
cat testdata/fuzz/FuzzParse/ea624b8b77c021c0
# go test fuzz v1
# string("ISBN")
```

`Parse` quita el prefijo con `s[5:]`, contando con que detrás de `ISBN` viene un espacio. Con la cadena `ISBN` sola, el índice 5 está fuera de rango. Ningún caso de la tabla tenía el prefijo sin nada detrás.

4. El archivo de `testdata/` ya forma parte de la suite. Sin fuzzing:

   ```bash
   go test -count=1 ./...
   # --- FAIL: FuzzParse (0.00s)
   #     --- FAIL: FuzzParse/ea624b8b77c021c0 (0.00s)
   # panic: runtime error: slice bounds out of range [5:4] [recovered, repanicked]
   # ...
   ```

5. Arregla `Parse`: cambia las tres líneas del `if strings.HasPrefix(...)` por una sola:

   ```go
   s = strings.TrimPrefix(s, "ISBN")
   ```

   `TrimPrefix` quita el prefijo solo si está, y el espacio que venga después lo quita el `Replacer` de la línea siguiente.

6. Comprueba el arreglo y vuelve a lanzar el fuzzer:

   ```bash
   go test -count=1 -v ./...
   # === RUN   TestParse
   # --- PASS: TestParse (0.00s)
   # === RUN   FuzzParse
   # (las 6 semillas)
   # === RUN   FuzzParse/ea624b8b77c021c0
   # --- PASS: FuzzParse (0.00s)
   # ...
   #     --- PASS: FuzzParse/ea624b8b77c021c0 (0.00s)
   # PASS
   # ok  	bootcamp/lab02	0.003s

   go test -run=^$ -fuzz=FuzzParse -fuzztime=10s .
   # fuzz: elapsed: 10s, execs: 4136901 (347269/sec), new interesting: 0 (total: 27)
   # PASS
   # ok  	bootcamp/lab02	10.125s
   ```

Deja `testdata/fuzz/FuzzParse/` en el proyecto: en un repositorio, se sube con el arreglo. Si alguien deshace el arreglo, `go test` lo detecta sin fuzzing (experimento 2).

### Paso 2: una propiedad del resultado (PASO 2)

1. Descomenta el bloque del PASO 2 y guarda.
2. Lanza el fuzzer sobre el nuevo fuzz test. `-fuzz` es una expresión regular, y el `^` y el `$` la anclan al nombre completo (el experimento 1 enseña por qué hace falta):

   ```bash
   go test -run=^$ -fuzz='^FuzzParseDigits$' -fuzztime=30s .
   # --- FAIL: FuzzParseDigits (0.06s)
   #     --- FAIL: FuzzParseDigits (0.00s)
   #         isbn_test.go:55: Parse("9788z2700z9z8") = "9788z2700z9z8": 'z' is not a digit
   #         isbn_test.go:55: Parse("9788z2700z9z8") = "9788z2700z9z8": 'z' is not a digit
   #         isbn_test.go:55: Parse("9788z2700z9z8") = "9788z2700z9z8": 'z' is not a digit
   #
   #     Failing input written to testdata/fuzz/FuzzParseDigits/735e67ff4a6645ca
   #     To re-run:
   #     go test -run=FuzzParseDigits/735e67ff4a6645ca
   # FAIL
   # exit status 1
   # FAIL	bootcamp/lab02	0.064s
   ```

   La entrada exacta y el nombre del archivo cambian en cada ejecución: el fuzzer muta al azar.

`FuzzParseDigits` no espera ningún valor concreto, porque no conoce la entrada. Comprueba una **propiedad**: si `Parse` acepta algo, el resultado tiene 13 caracteres y todos son dígitos ASCII ([teoría 02](../../1-teoria/02-fuzzing-nativo.md#4-qué-comprobar-en-la-función-objetivo)). En 0.06 s, el fuzzer encontró un "ISBN" con letras que `Parse` dio por bueno. El bucle del dígito de control calcula `s[i] - '0'` sin comprobar que `s[i]` sea un dígito: para la `z` vale 74, y con tres `z` en las posiciones adecuadas la suma sigue siendo múltiplo de 10. El mensaje sale tres veces porque `t.Errorf` no para el test, y hay tres letras.

3. Arregla `Parse`: dentro del `for`, antes de calcular `d`, rechaza lo que no sea un dígito:

   ```go
   if s[i] < '0' || s[i] > '9' {
   	return "", ErrLength
   }
   ```

4. Comprueba:

   ```bash
   go test -count=1 ./...
   # ok  	bootcamp/lab02	0.004s
   go test -run=^$ -fuzz='^FuzzParseDigits$' -fuzztime=30s .
   # fuzz: elapsed: 30s, execs: 12314692 (0/sec), new interesting: 18 (total: 29)
   # PASS
   # ok  	bootcamp/lab02	30.112s
   ```

`ErrLength` ya decía "must have 13 digits", así que sirve también para este caso.

## Experimentos

### Experimento 1: -fuzz con varios fuzz tests

Con el PASO 2 aplicado, ejecuta el fuzzer sin anclar el nombre:

```bash
go test -run=^$ -fuzz=FuzzParse .
# testing: will not fuzz, -fuzz matches more than one fuzz test: [FuzzParse FuzzParseDigits]
# FAIL
# exit status 1
```

El fuzzer trabaja con un solo fuzz test cada vez, y `FuzzParse` coincide con los dos nombres. Con `'^FuzzParse$'` coincide solo con el primero. Pasa lo mismo con los paquetes: `-fuzz` con `./...` y varios paquetes da `cannot use -fuzz flag with multiple packages`.

### Experimento 2: el corpus como test de regresión

Con los dos arreglos aplicados, quita de `Parse` la comprobación de dígitos del paso 2 y ejecuta los tests normales, sin fuzzing:

```bash
go test -count=1 ./...
# --- FAIL: FuzzParseDigits (0.00s)
#     --- FAIL: FuzzParseDigits/735e67ff4a6645ca (0.00s)
#         isbn_test.go:55: Parse("9788z2700z9z8") = "9788z2700z9z8": 'z' is not a digit
#         ...
# FAIL
# FAIL	bootcamp/lab02	0.003s
```

`go test` ejecuta la entrada guardada en `testdata/fuzz/FuzzParseDigits/` en milisegundos. El error que el fuzzer tardó en encontrar ya no puede volver sin que se note, en cualquier máquina y en CI. Restaura la comprobación.

### Experimento 3: una semilla de otro tipo

En `FuzzParse`, cambia `f.Add(tt.in)` por `f.Add([]byte(tt.in))`:

```bash
go test -count=1 ./...
# # bootcamp/lab02
# # [bootcamp/lab02]
# ./isbn_test.go:33:9: mismatched type in call to (*testing.F).Add: []byte, fuzz target expects string
# FAIL	bootcamp/lab02 [build failed]
```

Cada `f.Add` tiene que pasar los mismos tipos, en el mismo orden, que los parámetros de la función de `f.Fuzz`. Lo comprueba el análisis `tests` de `go vet`, que `go test` ejecuta antes de compilar los tests. Restaura la línea.

### Experimento 4: la caché del fuzzer

Las entradas interesantes que el fuzzer va generando no van a `testdata/`, sino a la caché de Go:

```bash
ls $(go env GOCACHE)/fuzz/bootcamp/lab02/FuzzParseDigits | wc -l
# 22
go test -run=^$ -fuzz='^FuzzParseDigits$' -fuzztime=2s . | head -2
# fuzz: elapsed: 0s, gathering baseline coverage: 0/29 completed
# fuzz: elapsed: 0s, gathering baseline coverage: 29/29 completed, now fuzzing with 8 workers
```

29 entradas de partida: 6 semillas, 1 archivo de `testdata/` y 22 de la caché. Cada sesión continúa donde dejó la anterior. Ahora borra la caché:

```bash
go clean -fuzzcache
go test -run=^$ -fuzz='^FuzzParseDigits$' -fuzztime=2s . | head -2
# fuzz: elapsed: 0s, gathering baseline coverage: 0/7 completed
# fuzz: elapsed: 0s, gathering baseline coverage: 7/7 completed, now fuzzing with 8 workers
```

Quedan las semillas y `testdata/`, que son lo que se sube al repositorio. La caché es local: cada máquina, y cada ejecución de CI que empiece de cero, construye la suya. `go clean -fuzzcache` borra la caché de fuzzing de todos tus módulos, no solo de este.

## Preguntas de cierre

1. ¿Qué diferencia hay entre `go test` y `go test -fuzz` para un fuzz test?
2. ¿Por qué la tabla de `TestParse` no encontró ninguno de los dos errores, y el fuzzer sí?
3. ¿Qué se sube al repositorio, `testdata/fuzz/` o la caché del fuzzer? ¿Por qué?
4. `FuzzParseDigits` no conoce el resultado esperado. ¿Qué comprueba entonces?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Race detector y synctest](../lab-01-race-detector-y-synctest/README.md) | [Lab 03 - Benchmarks con benchstat](../lab-03-benchmarks-con-benchstat/README.md) → |

[↑ Semana 35](../../README.md)
