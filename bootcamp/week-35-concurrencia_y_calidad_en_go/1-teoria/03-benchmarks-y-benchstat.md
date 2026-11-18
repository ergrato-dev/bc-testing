# 03 - Benchmarks y benchstat

> Lenguaje: **Go**

"Esto es más rápido" es una afirmación que se puede medir. Un **benchmark** ejecuta un trozo de código muchas veces y mide cuánto tarda cada vez y cuánta memoria pide. Go los trae en `go test`, igual que los tests y los fuzz tests. Y como una medida suelta engaña (la misma función tarda distinto en cada ejecución), **benchstat** compara dos series de medidas y dice si la diferencia es real o es ruido.

Esto es un **microbenchmark**: mide una función, no el sistema. Cuánto aguanta la API entera con 500 usuarios es una prueba de carga, y llega en la [semana 43](../../../docs/plan-estudios.md).

---

## 🎯 Objetivos

- Escribir un benchmark con `func BenchmarkXxx(b *testing.B)` y `b.Loop()`.
- Ejecutarlo con `-bench` y `-benchmem`, y leer sus columnas.
- Medir cómo crece el coste con el tamaño de la entrada usando sub-benchmarks.
- Comparar el antes y el después de una optimización con `benchstat`, y leer si la diferencia es significativa.

---

## 1. Anatomía de un benchmark

```go
func BenchmarkInventory(b *testing.B) {
	books := catalog(1000) // preparación: no se mide
	for b.Loop() {
		Inventory(books) // lo que se mide
	}
}
```

| Pieza | Qué hace |
|---|---|
| `BenchmarkXxx(b *testing.B)` | El benchmark. Vive en un `_test.go`. `go test` sin `-bench` lo compila pero no lo ejecuta |
| `for b.Loop()` | Repite el cuerpo hasta tener una medida estable (por defecto, alrededor de 1 segundo) |
| Lo que va antes del bucle | Preparación. `b.Loop` pone el cronómetro a cero en su primera llamada, así que no cuenta |

`b.Loop()` apareció en Go 1.24. Antes se escribía `for i := 0; i < b.N; i++`, y lo encontrarás en código y en tutoriales. `b.Loop` lo sustituye por dos motivos que el [lab 03](../2-practicas/lab-03-benchmarks-con-benchstat/README.md#experimentos) comprueba:

| Con `b.N` | Con `b.Loop` |
|---|---|
| Go llama a la función varias veces con `b.N` creciente (1, 100, 6226...), y la preparación se repite en cada llamada | La función se llama una sola vez y la preparación se ejecuta una vez |
| La preparación cuenta en el tiempo, salvo que llames a `b.ResetTimer()` | El cronómetro arranca en la primera llamada a `b.Loop` |
| El compilador puede simplificar una llamada cuyo resultado nadie usa: mides menos de lo que el código real hace | `b.Loop` mantiene vivos los argumentos y el resultado de las llamadas del cuerpo |

---

## 2. Ejecutar y leer

```bash
go test -run=^$ -bench=Inventory -benchmem .
# BenchmarkInventory-8   	     211	   5801008 ns/op	29431607 B/op	    4045 allocs/op
```

| Flag | Qué hace |
|---|---|
| `-bench=Inventory` | Ejecuta los benchmarks cuyo nombre coincide con la expresión regular. `-bench=.` los ejecuta todos |
| `-run=^$` | No ejecuta ningún test (ningún nombre coincide con una cadena vacía): solo los benchmarks |
| `-benchmem` | Añade las columnas de memoria |
| `-count=10` | Repite cada benchmark 10 veces. Hace falta para `benchstat` |
| `-benchtime=2s` o `-benchtime=100x` | Cambia el tiempo de cada medida, o fija el número de vueltas |

| Columna | Qué significa |
|---|---|
| `BenchmarkInventory-8` | Nombre y `GOMAXPROCS`: los hilos que Go puede usar a la vez |
| `211` | Vueltas que dio el bucle para la medida |
| `5801008 ns/op` | Tiempo medio por vuelta: 5.8 ms |
| `29431607 B/op` | Bytes pedidos al recolector de basura por vuelta: 29 MB para generar un texto de 1000 líneas |
| `4045 allocs/op` | Reservas de memoria por vuelta |

Las dos últimas columnas suelen explicar la primera: cada reserva cuesta, y el recolector de basura tiene que liberarla después.

### Sub-benchmarks: cómo crece el coste

```go
func BenchmarkInventorySizes(b *testing.B) {
	for _, n := range []int{10, 100, 1000} {
		b.Run(fmt.Sprintf("books=%d", n), func(b *testing.B) {
			books := catalog(n)
			for b.Loop() {
				Inventory(books)
			}
		})
	}
}
```

```text
BenchmarkInventorySizes/books=10-8         	  486130	      2484 ns/op
BenchmarkInventorySizes/books=100-8        	   13126	     93717 ns/op
BenchmarkInventorySizes/books=1000-8       	     164	   7357085 ns/op
```

Diez veces más libros cuestan 38 veces más, y después 78 veces más. Si el coste fuera proporcional al tamaño, sería unas 10 veces cada salto. Crece más rápido: `Inventory` concatena con `+=`, y cada concatenación copia todo el texto anterior. Un solo tamaño no lo habría mostrado.

---

## 3. benchstat: antes y después

Una medida suelta no basta para decir que algo mejoró: el mismo código da números distintos en cada ejecución, según lo que esté haciendo la máquina. `benchstat` (del módulo `golang.org/x/perf`) toma varias medidas de cada versión y aplica un test estadístico.

```bash
go test -run=^$ -bench=Sizes -benchmem -count=10 . > old.txt
# ... optimizas ...
go test -run=^$ -bench=Sizes -benchmem -count=10 . > new.txt
go tool benchstat old.txt new.txt
```

```text
                            │   old.txt    │               new.txt               │
                            │    sec/op    │   sec/op     vs base                │
InventorySizes/books=10-8      2.486µ ± 4%   2.092µ ± 7%  -15.83% (p=0.000 n=10)
InventorySizes/books=100-8     95.15µ ± 5%   18.41µ ± 2%  -80.65% (p=0.000 n=10)
InventorySizes/books=1000-8   8308.4µ ± 5%   175.4µ ± 4%  -97.89% (p=0.000 n=10)
geomean                        125.3µ        18.91µ       -84.91%
```

| Dato | Qué significa |
|---|---|
| `2.486µ` | La mediana de las 10 medidas |
| `± 4%` | El intervalo de confianza del 95 %: el valor real está, casi seguro, a menos de un 4 % de la mediana |
| `-15.83%` | El cambio de `new` respecto a `old` |
| `p=0.000` | La probabilidad de ver una diferencia así si las dos versiones fueran igual de rápidas. Por debajo de 0.05, la diferencia se considera real |
| `~` en lugar de un porcentaje | No hay diferencia significativa: lo que ves puede ser ruido |
| `n=10` | Medidas usadas de cada lado |
| `geomean` | Media geométrica de todas las filas: un resumen del conjunto |

Dos lecturas que el [lab 03](../2-practicas/lab-03-benchmarks-con-benchstat/README.md#experimento-3-una-sola-medida) comprueba con salidas reales:

- **El mismo código dos veces** da `~` en todas las filas, aunque la media geométrica diga `-2.08%`. Esa es la escala del ruido de la máquina.
- **Con una sola medida** (`-count=1`), `benchstat` no puede calcular el intervalo (`± ∞`, `need >= 6 samples`) y marca `~` hasta una mejora del 98 %. Usa `-count=10`.

### Reglas para medir

| Regla | Por qué |
|---|---|
| `old.txt` y `new.txt` en la misma máquina, una detrás de otra | Otra máquina, otra carga u otra versión de Go cambian los números más que tu optimización |
| Cierra lo que consuma CPU mientras mides | El ruido ensancha el `±` y esconde las diferencias pequeñas |
| Comprueba con un test que la versión optimizada hace lo mismo | Un benchmark mide la velocidad, no la corrección. El proyecto lo hace con un fuzz test que compara las dos versiones |
| Guarda `old.txt` y `new.txt` junto al cambio | Son la prueba de la mejora en la revisión del pull request |

`benchstat` se instala como herramienta del módulo, igual que `mockgen` en la [semana 33](../../week-33-interfaces_y_dobles_de_prueba_en_go/README.md): `go get -tool golang.org/x/perf/cmd/benchstat@<versión>` lo registra en `go.mod`, y `go tool benchstat` lo ejecuta con esa versión.

---

## Resumen

| Comando o pieza | Para qué |
|---|---|
| `for b.Loop() { ... }` | El cuerpo que se mide; la preparación va antes |
| `b.Run(nombre, func(b *testing.B))` | Sub-benchmarks, por ejemplo uno por tamaño |
| `go test -run=^$ -bench=. -benchmem` | Ejecutar los benchmarks con las columnas de memoria |
| `-count=10 > old.txt` | Guardar 10 medidas para comparar |
| `go tool benchstat old.txt new.txt` | Decidir si la diferencia es real |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 02 - Fuzzing nativo](02-fuzzing-nativo.md) | [Teoría — 04 - Cobertura y linters](04-cobertura-y-linters.md) → |

[↑ Semana 35](../README.md)
