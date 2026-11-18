# Prácticas - Semana 36

Dos labs de planificación (2.5 h en total) que preparan el proyecto integrador. Los dos usan la misma biblioteca: un paquete `catalog` con las reglas y la API, y un paquete `pgstore` con PostgreSQL. Los cambios de cada PASO están en el README del lab. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Una suite, un comando](lab-01-una-suite-un-comando/README.md) | Los tests que `go test ./...` no ejecuta, `verify.sh` por fases, `set -euo pipefail`, el umbral con `awk` y la trampa de `tee` | 75 min |
| [Lab 02 - Los datos del reporte](lab-02-datos-del-reporte/README.md) | Tiempos por fase, tests por capa con `-v`, los más lentos, `TestMain` y la pausa de `-race`, cobertura por paquete y el reporte | 75 min |

Los dos labs necesitan **Docker** en marcha: los tests de `pgstore` arrancan PostgreSQL con testcontainers-go.

## Antes de empezar

```bash
go version      # go version go1.26.5 ...
docker version --format '{{.Server.Version}}'
```

Cada lab es un módulo Go independiente:

```bash
cd lab-NN-.../starter
./verify.sh
```

El lab 01 construye `verify.sh` por PASOS: cada bloque está comentado con `# PASO N: ...` y se activa borrando el `# ` de sus líneas. El lab 02 parte de `verify.sh` terminado.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 01 - La suite Go completa](../1-teoria/01-la-suite-go-completa.md) | [Lab 01 - Una suite, un comando](lab-01-una-suite-un-comando/README.md) → |

[↑ Semana 36](../README.md)
