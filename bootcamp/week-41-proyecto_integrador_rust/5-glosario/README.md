# Glosario - Semana 41

| Término | Definición |
|---|---|
| Suite | Todos los tests de un proyecto, con las comprobaciones que los acompañan: formato, linter, cobertura. |
| Capa | Un grupo de tests según dónde están y qué necesitan: unitarios de `src/`, integración de `tests/`, doctests, benchmarks. |
| Test unitario (Rust) | Test dentro de `#[cfg(test)] mod tests` en `src/`. Ve también lo privado del módulo. |
| Test de integración (Rust) | Cada archivo de `tests/`. Usa el paquete desde fuera, solo su API pública. No implica base de datos. |
| Doctest | Bloque de código de un comentario `///` que `cargo test` compila y ejecuta. nextest no los ejecuta. |
| Binario de test | El ejecutable que compila cargo para los tests: uno para `src/` y uno por archivo de `tests/`. |
| Filtro de nextest (*filterset*) | Expresión de `-E` que elige qué tests ejecutar: `binary(store)`, `kind(lib)`, `not binary(/^pg/)`. |
| Fail-fast | Parar en el primer fallo. `cargo test` se para en el primer binario que falla; `--no-fail-fast` sigue hasta el final. |
| `verify.sh` | Script que ejecuta la suite entera en orden, de lo más barato a lo más caro, y se para en el primer fallo. |
| `set -euo pipefail` | Opciones de bash que paran el script en el primer comando que falla, también dentro de una tubería, y tratan como error una variable sin definir. |
| `docker compose up -d --wait` | Arranca los servicios de `compose.yaml` en segundo plano y espera a que su `healthcheck` diga que están listos. |
| `--no-report` | Opción de cargo-llvm-cov que ejecuta y guarda los datos de cobertura sin escribir el informe. Suma los datos a los que ya había. |
| `cargo llvm-cov clean --workspace` | Borra los datos de cobertura y los binarios del paquete, sin tocar las dependencias compiladas. |
| `--fail-under-lines` | Opción de `cargo llvm-cov` que termina con código `1` si la cobertura de líneas no llega al valor. No escribe ningún mensaje. |
| Umbral de cobertura | El mínimo por debajo del cual la suite falla: el 85 % de líneas en todo el bootcamp. |
| `target/llvm-cov-target` | Carpeta donde cargo-llvm-cov compila con instrumentación, aparte de `target/debug`. |
| Reporte de testing | Documento con los datos reales de la suite: capas, cobertura, tiempos, riesgos y próximos pasos. |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | — |

[↑ Semana 41](../README.md)
