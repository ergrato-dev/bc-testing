# Videografía - Semana 35

Los videos están en inglés. Si un video escribe los benchmarks con `for i := 0; i < b.N; i++`, desde Go 1.24 se escribe `for b.Loop()`, que no repite la preparación ni deja que el compilador quite la llamada ([teoría 03](../../1-teoria/03-benchmarks-y-benchstat.md#1-anatomía-de-un-benchmark)).

| Recurso | URL | Por qué verlo |
|---|---|---|
| Using synctest for testing concurrent code - new in Golang 1.25 | https://www.youtube.com/watch?v=KAImJOqagXY | `synctest.Test` y `synctest.Wait` sobre código con temporizadores, como los pasos 2 y 3 del lab 01. |
| GopherCon 2022: Fuzz Testing Made Easy - Katie Hockman | https://www.youtube.com/watch?v=7KWPiRq3ZYI | De quien diseñó el fuzzing de Go: cómo funciona el fuzzer guiado por cobertura, el corpus, y el fuzzing que compara dos implementaciones, como `FuzzIsCode` en el proyecto. |
| Go Benchmarking - Prove your code is harder better faster stronger | https://www.youtube.com/watch?v=ctC6agjbbH0 | Benchmarks para demostrar que una optimización mejora algo, que es lo que el lab 03 hace con `benchstat`. |
| Go Test Coverage: A Beginner's Guide to Measuring Test Quality | https://www.youtube.com/watch?v=3ilxk24hzyA | `-cover`, `-coverprofile` y el informe HTML, el paso 1 del lab 04. |

## Dinámica sugerida

- Video 1 antes del paso 2 del lab 01.
- Video 2 antes del lab 02.
- Videos 3 y 4 antes de los labs 03 y 04.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — ebooks](../ebooks-free/README.md) | [Recursos — web](../webgrafia/README.md) → |

[↑ Semana 35](../../README.md)
