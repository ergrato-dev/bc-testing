# Prácticas - Semana 41

Dos labs de planificación (2.5 h en total) que preparan el proyecto integrador. Los dos usan la misma biblioteca: el recargo por retraso, una API axum para prestar libros y los libros en PostgreSQL con sqlx. Los cambios de cada PASO están en el README del lab. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Una suite, un comando](lab-01-una-suite-un-comando/README.md) | Lo que `cargo test` no ejecuta sin PostgreSQL, `verify.sh` por fases con filtros de nextest, la cobertura limpia, el umbral con mensaje y clippy en los tests | 75 min |
| [Lab 02 - Los datos del reporte](lab-02-datos-del-reporte/README.md) | Tiempos por capa, tests por binario, los más lentos y el coste de `#[sqlx::test]`, la cobertura por archivo y el reporte | 75 min |

## Antes de empezar

Los dos labs necesitan **Docker** en marcha, y cargo-nextest y cargo-llvm-cov instalados ([semana 40](../../week-40-calidad_avanzada_en_rust/1-teoria/04-cobertura-nextest-mutantes-y-clippy.md#instalar)):

```bash
cargo --version              # cargo 1.100.0 ...
cargo nextest --version      # cargo-nextest 0.9.152 ...
cargo llvm-cov --version     # cargo-llvm-cov 0.9.1
docker version --format '{{.Server.Version}}'
```

Cada lab es un paquete Rust independiente con su `compose.yaml`:

```bash
cd lab-NN-.../starter
./verify.sh
```

El lab 01 construye `verify.sh` por PASOS: cada bloque está comentado con `# PASO N: ...` y se activa borrando el `# ` de sus líneas. El lab 02 parte de `verify.sh` terminado.

Los dos `compose.yaml` publican PostgreSQL en el puerto 55432, el mismo que el proyecto del museo. Antes de cambiar de lab o de volver al proyecto, para el anterior con `docker compose down`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 01 - La suite Rust completa](../1-teoria/01-la-suite-rust-completa.md) | [Lab 01 - Una suite, un comando](lab-01-una-suite-un-comando/README.md) → |

[↑ Semana 41](../README.md)
