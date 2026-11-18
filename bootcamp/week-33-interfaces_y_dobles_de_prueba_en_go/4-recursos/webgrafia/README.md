# Webgrafía - Semana 33

| Recurso | URL | Por qué leerlo |
|---|---|---|
| Go Code Review Comments: Interfaces | https://go.dev/wiki/CodeReviewComments#interfaces | La regla de la teoría 01, escrita por el equipo de Go: la interfaz pertenece al paquete que la usa. |
| uber-go/mock | https://github.com/uber-go/mock | El README de mockgen: los dos modos (paquete y archivo fuente), todas las opciones y los matchers de gomock. |
| Package gomock | https://pkg.go.dev/go.uber.org/mock/gomock | La referencia de `Controller`, `Call` (`Return`, `Times`, `DoAndReturn`) y `InOrder`. |
| Go 1.24 Release Notes: Tools | https://go.dev/doc/go1.24#tools | La directiva `tool` del `go.mod` y `go get -tool`. |
| Generating code | https://go.dev/blog/generate | Cómo funciona `go generate` y por qué no se ejecuta solo al compilar. |
| Package testify/assert | https://pkg.go.dev/github.com/stretchr/testify/assert | Todas las aserciones de testify. `require` tiene las mismas. |
| Package testify/mock | https://pkg.go.dev/github.com/stretchr/testify/mock | `On`, `Return`, `Once`, `AssertExpectations` y los matchers como `mock.Anything`. |
| Package cmp | https://pkg.go.dev/github.com/google/go-cmp/cmp | `Diff`, `Equal` y cómo se leen sus diferencias. |
| Package cmpopts | https://pkg.go.dev/github.com/google/go-cmp/cmp/cmpopts | Las opciones de la teoría 03 y otras, como `SortSlices` para comparar sin orden. |
| Package math/rand/v2 | https://pkg.go.dev/math/rand/v2 | `rand.New`, `NewPCG` y por qué la misma semilla da la misma secuencia. |

## Dinámica sugerida

- *Code Review Comments: Interfaces* con la teoría 01.
- El README de *uber-go/mock* durante el lab 02.
- *Package cmpopts* durante el lab 04, para ver qué más puedes ignorar o normalizar.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — videos](../videografia/README.md) | [Glosario](../../5-glosario/README.md) → |

[↑ Semana 33](../../README.md)
