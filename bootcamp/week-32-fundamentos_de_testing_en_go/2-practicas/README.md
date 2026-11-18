# Prácticas - Semana 32

Cuatro labs guiados (4 h en total). Cada uno trae un `starter/` con un módulo de Go y los tests comentados por PASOS; lo que no es descomentar, como borrar un test o cambiar una línea, está en el README del lab. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Primer test con go test](lab-01-primer-test-con-go-test/README.md) | `TestXxx`, `t.Errorf` frente a `t.Fatalf`, `-v`, `-run`, la caché y `-count=1`, `go vet` y los nombres que no son tests | 55 min |
| [Lab 02 - Table-driven tests y subtests](lab-02-table-driven-y-subtests/README.md) | Tabla de casos, `t.Run`, `-run 'TestX/fila'`, `t.Parallel()`, la variable del bucle con `go 1.21` y `-shuffle=on` | 70 min |
| [Lab 03 - Helpers, t.TempDir y t.Cleanup](lab-03-helpers-tempdir-y-cleanup/README.md) | `t.Helper`, `t.TempDir`, `t.Cleanup` frente a `defer` y un contexto cancelado con `t.Context` | 60 min |
| [Lab 04 - Caja negra y examples](lab-04-caja-negra-y-examples/README.md) | `package x` frente a `package x_test`, `ExampleXxx` con `// Output:` y los fallos de un example | 55 min |

## Antes de empezar

```bash
go version
# go version go1.26.5 linux/amd64
```

Go 1.24 o superior basta para todo lo de esta semana: `t.Context` apareció en Go 1.24. Cada lab es un módulo independiente:

```bash
cd lab-NN-.../starter
go test ./...
```

Los labs no tienen dependencias: solo la librería estándar, así que no hay `go.sum` ni nada que descargar.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 03 - Helpers, caja negra y examples](../1-teoria/03-helpers-y-caja-negra.md) | [Lab 01 - Primer test con go test](lab-01-primer-test-con-go-test/README.md) → |

[↑ Semana 32](../README.md)
