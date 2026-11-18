# Prácticas - Semana 35

Cuatro labs guiados (4 h en total). Cada uno trae un `starter/` con un módulo de Go y los tests comentados por PASOS. Lo que no es descomentar, como arreglar el código que un test acaba de romper, está en el README del lab. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

Los cuatro labs siguen con la biblioteca de las semanas anteriores: las visitas y las reservas del catálogo, el lector de ISBN, el inventario y los recargos por retraso. En cada lab, una herramienta encuentra algo que los tests normales dejaban pasar: una carrera de datos, una entrada que hace `panic`, un coste que crece con el cuadrado del tamaño, y cuatro avisos que `go vet` no da.

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Race detector y synctest](lab-01-race-detector-y-synctest/README.md) | `go test -race` y su informe, `sync.Mutex`, `synctest.Test` y `synctest.Wait` con tiempo virtual | 60 min |
| [Lab 02 - Fuzzing nativo](lab-02-fuzzing-nativo/README.md) | `FuzzXxx`, corpus semilla, `-fuzz` y `-fuzztime`, `testdata/fuzz/` como regresión, propiedades | 60 min |
| [Lab 03 - Benchmarks con benchstat](lab-03-benchmarks-con-benchstat/README.md) | `b.Loop()`, `-benchmem`, sub-benchmarks por tamaño, `benchstat` antes y después | 60 min |
| [Lab 04 - Cobertura y linters](lab-04-cobertura-y-linters/README.md) | `-coverprofile`, `go tool cover`, `-coverpkg`, `go build -cover` y `GOCOVERDIR`, `staticcheck` | 60 min |

## Antes de empezar

```bash
go version
# go version go1.26.5 linux/amd64
```

Los cuatro labs necesitan Go 1.25 o superior: `testing/synctest` y `sync.WaitGroup.Go` son estables desde Go 1.25, y `b.Loop()` existe desde Go 1.24. Ninguno necesita Docker. El lab 01 y el proyecto usan `-race`, que necesita un compilador de C (`gcc`): el [lab 01](lab-01-race-detector-y-synctest/README.md#preparación) explica cómo comprobarlo. Cada lab es un módulo independiente. Los labs 01 y 02 no tienen dependencias; los labs 03 y 04 traen una herramienta (`benchstat` y `staticcheck`) y la descargan con:

```bash
cd lab-NN-.../starter
go mod download
```

El fuzzing y los benchmarks usan todos los núcleos de la máquina mientras se ejecutan. Es normal que el ventilador se note.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 04 - Cobertura y linters](../1-teoria/04-cobertura-y-linters.md) | [Lab 01 - Race detector y synctest](lab-01-race-detector-y-synctest/README.md) → |

[↑ Semana 35](../README.md)
