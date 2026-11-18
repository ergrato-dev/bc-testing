# Prácticas - Semana 34

Cuatro labs guiados (4 h en total). Cada uno trae un `starter/` con un módulo de Go y los tests comentados por PASOS. Lo que no es descomentar, como borrar una función o añadir una línea, está en el README del lab. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

Los cuatro labs giran alrededor del catálogo de una biblioteca: su API, el cliente de un catálogo remoto, y el repositorio en PostgreSQL. Van de dentro hacia fuera: del handler en memoria a la red, y de la red a una base de datos real.

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Handlers con httptest](lab-01-handlers-con-httptest/README.md) | `NewRequest` y `NewRecorder`, rutas con método del `ServeMux`, `404` y `405`, el contrato JSON | 60 min |
| [Lab 02 - Clientes HTTP con httptest.NewServer](lab-02-clientes-con-httptest-newserver/README.md) | Servidor de pruebas como spy y como stub, estados de error, plazo con `context`, HTTPS | 60 min |
| [Lab 03 - TestMain, build tags y -short](lab-03-testmain-build-tags-y-short/README.md) | Un recurso compartido con `TestMain`, `testing.Short()`, `//go:build integration` | 50 min |
| [Lab 04 - PostgreSQL con testcontainers-go](lab-04-postgresql-con-testcontainers-go/README.md) | Contenedor por test y por paquete, migraciones con `embed`, transacción por test | 70 min |

## Antes de empezar

```bash
go version
# go version go1.26.5 linux/amd64
docker version --format '{{.Server.Version}}'
# 29.10.0
```

Los labs 01 a 03 solo necesitan Go 1.25 o superior: el lab 03 usa `sync.WaitGroup.Go`, que apareció en Go 1.25. El lab 04 necesita además Docker en marcha. Cada lab es un módulo independiente. El lab 03 no tiene dependencias; los demás las descargan con:

```bash
cd lab-NN-.../starter
go mod download
```

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 03 - testcontainers-go y PostgreSQL](../1-teoria/03-testcontainers-go-y-postgresql.md) | [Lab 01 - Handlers con httptest](lab-01-handlers-con-httptest/README.md) → |

[↑ Semana 34](../README.md)
