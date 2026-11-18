# Prácticas - Semana 33

Cuatro labs guiados (4 h en total). Cada uno trae un `starter/` con un módulo de Go y los tests comentados por PASOS; lo que no es descomentar, como borrar un test o cambiar una línea, está en el README del lab. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

Los cuatro labs prueban el mismo servicio de recordatorios de una biblioteca, cada vez con otra herramienta. Así puedes comparar el mismo test escrito de cuatro formas.

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Interfaces y fakes a mano](lab-01-interfaces-y-fakes-a-mano/README.md) | Interfaces del lado del consumidor, fake, spy y stubs escritos a mano, el reloj como función | 60 min |
| [Lab 02 - Mocks con mockgen](lab-02-mocks-con-mockgen/README.md) | `go get -tool`, `//go:generate`, `EXPECT()`, `gomock.Any()`, `Times()`, y los fallos de un mock | 70 min |
| [Lab 03 - assert, require y testify/mock](lab-03-assert-require-y-testify-mock/README.md) | `assert` frente a `require`, el `panic` de un puntero nulo y el mock escrito con testify | 50 min |
| [Lab 04 - go-cmp y golden files](lab-04-go-cmp-y-golden-files/README.md) | `cmp.Diff`, `cmpopts`, la semilla fija y un golden file con `-update` | 60 min |

## Antes de empezar

```bash
go version
# go version go1.26.5 linux/amd64
```

Necesitas Go 1.24 o superior: la línea `tool` del `go.mod` apareció en Go 1.24. Cada lab es un módulo independiente. El lab 01 no tiene dependencias; los demás las descargan con:

```bash
cd lab-NN-.../starter
go mod download
```

`go.sum` fija el contenido exacto de cada versión, así que la descarga es la misma en todas las máquinas.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 03 - go-cmp y golden files](../1-teoria/03-go-cmp-y-golden-files.md) | [Lab 01 - Interfaces y fakes a mano](lab-01-interfaces-y-fakes-a-mano/README.md) → |

[↑ Semana 33](../README.md)
