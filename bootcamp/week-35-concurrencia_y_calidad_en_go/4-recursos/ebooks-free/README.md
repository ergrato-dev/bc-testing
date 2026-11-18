# Ebooks gratuitos - Semana 35

| Recurso | URL | Por qué leerlo |
|---|---|---|
| Learn Go with Tests: Sync | https://quii.gitbook.io/learn-go-with-tests/go-fundamentals/sync | Un contador que falla con varias goroutines, el informe de `-race` y el arreglo con `sync.Mutex`: el paso 1 del lab 01 escrito con TDD. |
| Learn Go with Tests: Concurrency | https://quii.gitbook.io/learn-go-with-tests/go-fundamentals/concurrency | Un benchmark antes y después de hacer concurrente una función, y la carrera que aparece al escribir un mapa desde varias goroutines. |
| Tutorial: Getting started with fuzzing | https://go.dev/doc/tutorial/fuzz | El tutorial oficial: de un test de tabla a un fuzz test, con dos errores que el fuzzer encuentra en una función que invierte cadenas. El mismo recorrido que el lab 02. |

## Dinámica sugerida

- *Sync* antes del lab 01.
- *Getting started with fuzzing* antes del lab 02. Usa `go test -fuzz=Fuzz` con un solo fuzz test en el paquete; si tienes dos, ancla el nombre ([experimento 1 del lab 02](../../2-practicas/lab-02-fuzzing-nativo/README.md#experimento-1--fuzz-con-varios-fuzz-tests)).
- *Concurrency* después del lab 03, para ver otro benchmark de antes y después.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Rúbrica de evaluación](../../rubrica-evaluacion.md) | [Recursos — videos](../videografia/README.md) → |

[↑ Semana 35](../../README.md)
