# Glosario Semana 35 - Concurrencia y Calidad en Go

## B

- **`b.Loop()`**: condición del bucle de un benchmark desde Go 1.24. Repite el cuerpo hasta tener una medida estable, no cuenta la preparación y evita que el compilador quite las llamadas del cuerpo.
- **Benchmark**: función `BenchmarkXxx(b *testing.B)` que mide cuánto tarda y cuánta memoria pide un trozo de código. Se ejecuta con `go test -bench`.
- **`benchstat`**: herramienta de `golang.org/x/perf` que compara dos series de medidas de benchmarks y dice si la diferencia es significativa.
- **Bloqueo duradero**: en una burbuja de `synctest`, un bloqueo que solo otra goroutine de la misma burbuja puede deshacer (un canal de la burbuja, `time.Sleep`). Esperar un mutex o la red no lo es.
- **Burbuja**: grupo aislado de goroutines que crea `synctest.Test`, con su propio reloj virtual.

## C

- **Carrera de datos**: dos goroutines acceden a la vez a la misma variable, al menos una escribe, y nada sincroniza los accesos.
- **Corpus semilla**: entradas de ejemplo que se dan al fuzzer con `f.Add`. Se ejecutan en cada `go test`.
- **`covdata`**: `go tool covdata`, que lee los contadores que escribe un binario compilado con `-cover` y los suma o convierte.
- **`-covermode`**: cómo se cuenta la cobertura: `set` (se ejecutó o no), `count` (cuántas veces) o `atomic` (cuántas veces, con contadores atómicos; obligatorio con `-race`).
- **`-coverpkg`**: flag que instrumenta los paquetes indicados en todas las suites, para contar lo que un test ejecuta fuera de su paquete.
- **`-coverprofile`**: flag que guarda la cobertura en un archivo para leerla con `go tool cover`.

## F

- **Función objetivo**: la función que recibe `f.Fuzz`. Recibe un `*testing.T` y una entrada generada, y falla con `panic` o `t.Error`.
- **Fuzzing**: generar entradas de forma automática, guiándose por la cobertura, para encontrar las que hacen fallar el código.

## G

- **`GOCOVERDIR`**: variable de entorno con la carpeta donde un binario compilado con `go build -cover` escribe sus contadores de cobertura al terminar.
- **golangci-lint**: programa que ejecuta muchos linters en una sola pasada, con una configuración común.

## I

- **Intervalo de confianza**: en `benchstat`, el `± x%`: el margen en el que casi seguro está el valor real alrededor de la mediana.

## L

- **Linter**: programa que revisa el código sin ejecutarlo y avisa de errores probables o de estilo.

## M

- **Microbenchmark**: benchmark de una función aislada, no del sistema completo.
- **Minimizar**: lo que hace el fuzzer con una entrada que falla: recortarla mientras siga fallando, para que sea fácil de entender.
- **`sync.Mutex`**: cerrojo que deja pasar a una sola goroutine cada vez entre `Lock` y `Unlock`.

## P

- **p-valor**: en `benchstat`, la probabilidad de ver una diferencia así si las dos versiones fueran igual de rápidas. Por debajo de 0.05, la diferencia se considera real.
- **Propiedad**: algo que tiene que cumplirse para cualquier entrada, como que una ida y vuelta por JSON deje el valor igual.

## R

- **Race detector**: la instrumentación que activa `-race`. Registra los accesos a memoria y las sincronizaciones, e informa de las carreras que ocurren durante la ejecución.

## S

- **staticcheck**: linter de Go que encuentra errores probables, código simplificable, problemas de estilo y código sin usar que `go vet` no detecta.
- **Sub-benchmark**: benchmark creado con `b.Run` dentro de otro, por ejemplo uno por tamaño de entrada.
- **`synctest.Test`**: ejecuta una función dentro de una burbuja con reloj virtual (Go 1.25+).
- **`synctest.Wait`**: espera a que todas las demás goroutines de la burbuja estén bloqueadas de forma duradera.

## T

- **`testdata/fuzz/`**: carpeta donde el fuzzer guarda las entradas que hicieron fallar un fuzz test. Se sube al repositorio y funciona como test de regresión.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | — |

[↑ Semana 35](../README.md)
