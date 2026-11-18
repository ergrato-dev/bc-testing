#!/usr/bin/env bash
# La suite Rust completa en un solo comando, como el verify.sh de Go.
# Se para en el primer paso que falla. Necesita Docker para PostgreSQL.
set -euo pipefail
cd "$(dirname "$0")"

echo "== formato y clippy"
cargo fmt --check
cargo clippy --all-targets -- -D warnings

# PASO 1: los tests que no necesitan PostgreSQL
echo "== sin base de datos"
cargo nextest run -E 'not binary(store)'

# PASO 2: PostgreSQL, todos los tests con cobertura y los doctests
echo "== PostgreSQL"
docker compose up -d --wait
echo "== todos los tests, con cobertura"
cargo llvm-cov clean --workspace
cargo llvm-cov nextest --no-report
echo "== doctests"
cargo test --doc

# PASO 3: el umbral
echo "== umbral de cobertura de líneas (mínimo: 85 %)"
cargo llvm-cov report --summary-only --fail-under-lines 85 || {
    echo "FALLO: la cobertura de líneas no llega al 85 %"
    exit 1
}
