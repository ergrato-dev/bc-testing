# Lab 03 - Benchmarks con benchstat

## Objetivo

Medir con benchmarks una función que genera el inventario del catálogo, descubrir con sub-benchmarks que su coste crece más rápido que el catálogo, optimizarla y demostrar con `benchstat` que la mejora es real y no ruido.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
go mod download
go test -count=1 -v ./...
# === RUN   TestInventory
# --- PASS: TestInventory (0.00s)
# PASS
# ok  	bootcamp/lab03	0.002s
```

`report.go` tiene `Inventory`, que devuelve una línea de texto por libro del catálogo, con su ISBN, su título y sus copias. `TestInventory` comprueba el formato exacto con dos libros. En `report_test.go` también está `catalog(n)`, que crea `n` libros de prueba para los benchmarks.

`go.mod` registra `benchstat` como herramienta del módulo, con la línea `tool golang.org/x/perf/cmd/benchstat` ([teoría 03](../../1-teoria/03-benchmarks-y-benchstat.md#3-benchstat-antes-y-después)). `go mod download` descarga su código, y `go tool benchstat` la compila la primera vez que la ejecutas.

Mientras mides, cierra lo que consuma CPU (el navegador con vídeo, otras compilaciones): todo lo que haga la máquina entra en los números.

## Paso a paso

### Paso 1: el primer benchmark (PASO 1)

1. Descomenta el bloque del PASO 1 y guarda.
2. Ejecuta solo los benchmarks, con las columnas de memoria:

   ```bash
   go test -run=^$ -bench=Inventory -benchmem .
   # goos: linux
   # goarch: amd64
   # pkg: bootcamp/lab03
   # cpu: AMD Ryzen 9 270 w/ Radeon 780M Graphics
   # BenchmarkInventory-8   	     177	   6678457 ns/op	29431713 B/op	    4046 allocs/op
   # PASS
   # ok  	bootcamp/lab03	1.188s
   ```

   Tus números serán otros: dependen del procesador y de lo que esté haciendo la máquina.

`BenchmarkInventory` prepara 1000 libros antes del bucle y mide `Inventory` dentro de `for b.Loop()`. Go repitió el cuerpo 177 veces hasta tener una medida estable de alrededor de un segundo. Cada inventario tardó 6.7 ms, pidió 29 MB de memoria e hizo 4046 reservas ([teoría 03](../../1-teoria/03-benchmarks-y-benchstat.md#2-ejecutar-y-leer)).

29 MB para un texto de 1000 líneas de 54 caracteres (54 KB) es sospechoso. Un solo tamaño no dice de dónde sale.

### Paso 2: el coste según el tamaño (PASO 2)

1. Descomenta el bloque del PASO 2 y guarda.
2. Ejecuta todos los benchmarks:

   ```bash
   go test -run=^$ -bench=. -benchmem .
   # BenchmarkInventory-8        	     188	   6409379 ns/op	29431758 B/op	    4046 allocs/op
   # BenchmarkInventorySizes/books=10-8         	  455737	      2613 ns/op	    4033 B/op	      39 allocs/op
   # BenchmarkInventorySizes/books=100-8        	   12775	     93218 ns/op	  301203 B/op	     399 allocs/op
   # BenchmarkInventorySizes/books=1000-8       	     154	   7570430 ns/op	29432041 B/op	    4047 allocs/op
   # PASS
   # ok  	bootcamp/lab03	4.761s
   ```

   (Se omiten las cuatro líneas de cabecera.)

`b.Run` crea un sub-benchmark por tamaño, como `t.Run` crea subtests. Compara las filas:

| Libros | Tiempo | Memoria |
|---|---|---|
| 10 | 2.6 µs | 4 KB |
| 100 (×10) | 93 µs (×36) | 301 KB (×75) |
| 1000 (×10) | 7.6 ms (×81) | 29 MB (×98) |

Si el coste fuera proporcional al número de libros, cada fila costaría unas 10 veces la anterior. La memoria crece casi 100 veces por cada salto: crece con el **cuadrado** del tamaño. La causa está en `Inventory`: `s += ...` crea un texto nuevo en cada vuelta y copia en él todo lo anterior. La línea 1000 copia las 999 anteriores.

### Paso 3: optimizar y comparar con benchstat

1. Guarda 10 medidas de la versión actual. Tarda unos 40 segundos:

   ```bash
   go test -run=^$ -bench=Sizes -benchmem -count=10 . > old.txt
   ```

   `-count=10` repite cada benchmark 10 veces, y `>` guarda la salida en `old.txt` en lugar de mostrarla.

2. Cambia `Inventory` en `report.go` para que escriba en un `strings.Builder`, que va ampliando un único búfer en lugar de copiar el texto entero en cada línea. Añade `"strings"` al import:

   ```go
   func Inventory(books []Book) string {
   	var sb strings.Builder
   	for _, b := range books {
   		fmt.Fprintf(&sb, "%s  %-30s %3d\n", b.ISBN, b.Title, b.Copies)
   	}
   	return sb.String()
   }
   ```

3. Comprueba que el resultado no cambió. Un benchmark mide la velocidad, no la corrección:

   ```bash
   go test -count=1 ./...
   # ok  	bootcamp/lab03	0.003s
   ```

4. Guarda 10 medidas de la versión nueva y compara:

   ```bash
   go test -run=^$ -bench=Sizes -benchmem -count=10 . > new.txt
   go tool benchstat old.txt new.txt
   # goos: linux
   # goarch: amd64
   # pkg: bootcamp/lab03
   # cpu: AMD Ryzen 9 270 w/ Radeon 780M Graphics
   #                             │   old.txt    │               new.txt               │
   #                             │    sec/op    │   sec/op     vs base                │
   # InventorySizes/books=10-8      2.486µ ± 4%   2.092µ ± 7%  -15.83% (p=0.000 n=10)
   # InventorySizes/books=100-8     95.15µ ± 5%   18.41µ ± 2%  -80.65% (p=0.000 n=10)
   # InventorySizes/books=1000-8   8308.4µ ± 5%   175.4µ ± 4%  -97.89% (p=0.000 n=10)
   # geomean                        125.3µ        18.91µ       -84.91%
   #
   #                             │    old.txt     │               new.txt                │
   #                             │      B/op      │     B/op      vs base                │
   # InventorySizes/books=10-8       3.938Ki ± 0%   2.156Ki ± 0%  -45.25% (p=0.000 n=10)
   # InventorySizes/books=100-8     294.14Ki ± 0%   27.35Ki ± 0%  -90.70% (p=0.000 n=10)
   # InventorySizes/books=1000-8   28743.0Ki ± 0%   238.1Ki ± 0%  -99.17% (p=0.000 n=10)
   # geomean                         321.7Ki        24.13Ki       -92.50%
   #
   #                             │   old.txt   │               new.txt               │
   #                             │  allocs/op  │  allocs/op   vs base                │
   # InventorySizes/books=10-8      39.00 ± 0%    26.00 ± 0%  -33.33% (p=0.000 n=10)
   # InventorySizes/books=100-8     399.0 ± 0%    212.0 ± 0%  -46.87% (p=0.000 n=10)
   # InventorySizes/books=1000-8   4.053k ± 0%   2.019k ± 0%  -50.19% (p=0.000 n=10)
   # geomean                        398.1         223.3       -43.91%
   ```

Cómo se lee la primera fila ([teoría 03](../../1-teoria/03-benchmarks-y-benchstat.md#3-benchstat-antes-y-después)): la mediana de las 10 medidas pasó de 2.486 µs a 2.092 µs, con un margen del 4 % y del 7 %. La diferencia es de un 15.83 %, y `p=0.000` dice que es casi imposible ver una diferencia así si las dos versiones fueran igual de rápidas.

La mejora crece con el tamaño: un 16 % con 10 libros y un 98 % con 1000. Con 1000 libros, la memoria baja de 28 MB a 238 KB. Por eso hacía falta el paso 2: con el benchmark de 10 libros, la optimización parecería pequeña.

Guarda `old.txt` y `new.txt`: en un pull request son la prueba de la mejora.

## Experimentos

### Experimento 1: b.N repite la preparación

Con los tres pasos aplicados, añade al final de `report_test.go` el mismo benchmark escrito de las dos formas, con una línea de registro en la preparación:

```go
func BenchmarkSetupN(b *testing.B) {
	b.Logf("setup with b.N = %d", b.N)
	books := catalog(1000)
	for i := 0; i < b.N; i++ {
		Inventory(books)
	}
}

func BenchmarkSetupLoop(b *testing.B) {
	b.Logf("setup with b.N = %d", b.N)
	books := catalog(1000)
	for b.Loop() {
		Inventory(books)
	}
}
```

```bash
go test -run=^$ -bench=Setup .
# BenchmarkSetupN-8      	    5997	    173583 ns/op
# --- BENCH: BenchmarkSetupN-8
#     report_test.go:51: setup with b.N = 1
#     report_test.go:51: setup with b.N = 100
#     report_test.go:51: setup with b.N = 5997
# BenchmarkSetupLoop-8   	    5919	    176433 ns/op
# --- BENCH: BenchmarkSetupLoop-8
#     report_test.go:59: setup with b.N = 1
```

Con `b.N`, Go llama a la función tres veces: con 1 vuelta, con 100 y con las 5997 que calcula para llenar un segundo. La preparación se repite en cada llamada y su tiempo entra en la medida, salvo que añadas `b.ResetTimer()` después de prepararla. Con `b.Loop`, la función se llama una vez y el cronómetro arranca en la primera llamada a `b.Loop`. Aquí `catalog(1000)` es rápido y los dos números se parecen. Con una preparación cara, como leer un archivo, el de `b.N` saldría inflado.

### Experimento 2: el compilador quita lo que nadie usa

Añade al final de `report_test.go` la comprobación del dígito de control del [lab 02](../lab-02-fuzzing-nativo/README.md) y dos benchmarks de ella:

```go
// valid comprueba el dígito de control de un ISBN-13 (lab 02).
func valid(isbn string) bool {
	sum := 0
	for i := range len(isbn) {
		d := int(isbn[i] - '0')
		if i%2 == 1 {
			d *= 3
		}
		sum += d
	}
	return sum%10 == 0
}

func BenchmarkValidN(b *testing.B) {
	for i := 0; i < b.N; i++ {
		valid("9788437604947")
	}
}

func BenchmarkValidLoop(b *testing.B) {
	for b.Loop() {
		valid("9788437604947")
	}
}
```

```bash
go test -run=^$ -bench=Valid -count=3 .
# BenchmarkValidN-8      	359932294	         3.335 ns/op
# BenchmarkValidN-8      	342275455	         3.202 ns/op
# BenchmarkValidN-8      	360425791	         3.353 ns/op
# BenchmarkValidLoop-8   	133952026	         8.939 ns/op
# BenchmarkValidLoop-8   	132898148	         9.080 ns/op
# BenchmarkValidLoop-8   	133438346	         9.048 ns/op
```

La misma función, con el mismo argumento, da 3.3 ns o 9 ns. En `BenchmarkValidN`, el compilador integra `valid` dentro del bucle y, como nadie usa el resultado, se ahorra parte del trabajo: mide menos de lo que cuesta una llamada real. La forma antigua de evitarlo era guardar el resultado en una variable global (`sink = valid(...)`). Con esa línea, la versión con `b.N` da 8.75 ns. `b.Loop` mantiene vivos los argumentos y el resultado sin ese truco. Borra los dos experimentos.

### Experimento 3: una sola medida

Guarda una sola medida de la versión nueva y compárala con `old.txt`:

```bash
go test -run=^$ -bench=Sizes -benchmem -count=1 . > one.txt
go tool benchstat old.txt one.txt
#                             │   old.txt    │                one.txt                 │
#                             │    sec/op    │    sec/op     vs base                  │
# InventorySizes/books=10-8      2.486µ ± 4%   2.099µ ± ∞ ¹        ~ (p=0.182 n=10+1)
# InventorySizes/books=100-8     95.15µ ± 5%   18.07µ ± ∞ ¹        ~ (p=0.182 n=10+1)
# InventorySizes/books=1000-8   8308.4µ ± 5%   170.0µ ± ∞ ¹        ~ (p=0.182 n=10+1)
# geomean                        125.3µ        18.61µ        -85.14%
# ¹ need >= 6 samples for confidence interval at level 0.95
```

(Se omiten la cabecera y las tablas de memoria.) Con una medida no hay margen que calcular (`± ∞`), y el test estadístico no puede declarar significativa ni una mejora de 50 veces: `~` y `p=0.182`. Por eso se usa `-count=10`. Borra `one.txt`.

### Experimento 4: el mismo código dos veces

Sin cambiar nada, guarda otras 10 medidas y compáralas con `new.txt`:

```bash
go test -run=^$ -bench=Sizes -count=10 . > again.txt
go tool benchstat new.txt again.txt
#                             │   new.txt   │             again.txt              │
#                             │   sec/op    │   sec/op     vs base               │
# InventorySizes/books=10-8     2.092µ ± 7%   2.003µ ± 6%       ~ (p=0.353 n=10)
# InventorySizes/books=100-8    18.41µ ± 2%   18.10µ ± 4%       ~ (p=0.481 n=10)
# InventorySizes/books=1000-8   175.4µ ± 4%   175.0µ ± 6%       ~ (p=0.912 n=10)
# geomean                       18.91µ        18.51µ       -2.08%
```

El mismo código "mejora" un 2 % en la media geométrica, y `benchstat` dice `~` en todas las filas: es ruido. Si tu optimización da una cifra de ese tamaño, no has demostrado nada. Borra `again.txt`.

## Preguntas de cierre

1. ¿Qué significan las columnas `ns/op`, `B/op` y `allocs/op`?
2. ¿Qué te enseñó el benchmark por tamaños que el de 1000 libros solo no enseñaba?
3. En la salida de `benchstat`, ¿qué significan `± 4%`, `p=0.000` y `~`?
4. ¿Por qué `b.Loop` da medidas más fiables que el bucle con `b.N`?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Fuzzing nativo](../lab-02-fuzzing-nativo/README.md) | [Lab 04 - Cobertura y linters](../lab-04-cobertura-y-linters/README.md) → |

[↑ Semana 35](../../README.md)
