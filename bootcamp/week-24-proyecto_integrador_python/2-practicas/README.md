# Prácticas - Semana 24

Dos labs de planificación (2.5 h en total) que preparan el proyecto integrador. Cada uno trae un `starter/` con el código comentado por PASOS y una `solution/` con el resultado. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Una suite por capas](lab-01-suite-por-capas/README.md) | Marcas por carpeta con un *hook*, `--strict-markers` y ejecución de cada capa | 75 min |
| [Lab 02 - Coverage combinado y reporte](lab-02-coverage-combinado-y-reporte/README.md) | `coverage run --append` con pytest y Behave, `fail_under` y el reporte de testing | 75 min |

Los dos labs necesitan **Docker** en marcha: la capa de repositorio arranca PostgreSQL con Testcontainers.

## Antes de empezar

```bash
uv --version     # uv 0.13.7 o superior
docker info --format '{{.ServerVersion}}'
```

Cada lab es un proyecto `uv` independiente:

```bash
cd lab-NN-.../starter
uv sync
uv run pytest -q
```

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 01 - La suite Python completa](../1-teoria/01-la-suite-python-completa.md) | [Lab 01 - Una suite por capas](lab-01-suite-por-capas/README.md) → |

[↑ Semana 24](../README.md)
