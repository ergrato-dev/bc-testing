#!/usr/bin/env bash
# La suite Rust completa en un solo comando, como el verify.sh de Go.
# Se para en el primer paso que falla. Necesita Docker para PostgreSQL.
set -euo pipefail
cd "$(dirname "$0")"

echo "== formato y clippy"
cargo fmt --check
cargo clippy --all-targets -- -D warnings

# Los archivos de tests/ que necesitan PostgreSQL empiezan por pg.
# Si los tuyos se llaman de otra forma, cambia la expresión regular (paso 2).
echo "== sin base de datos"
cargo nextest run -E 'not binary(/^pg/)'

# TODO: arrancar el PostgreSQL de ../compose.yaml (paso 2)

# TODO: todos los tests con cobertura, empezando por llvm-cov clean (paso 2)

# TODO: los doctests y los benchmarks en modo test (paso 2)

# TODO: el umbral del 85 % de líneas, con un mensaje si falla (paso 3)
