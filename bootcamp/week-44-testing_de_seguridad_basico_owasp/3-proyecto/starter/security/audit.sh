#!/usr/bin/env bash
# Auditoría de dependencias de cada lenguaje del repositorio -> security/results/audit-*.txt
#
# Cada herramienta compara las versiones exactas de los lockfiles con una base de datos
# de vulnerabilidades publicadas. Se ejecutan todas aunque alguna encuentre algo, y al
# final se imprime el código de salida de cada una (0: nada encontrado).
# Se ejecuta desde la raíz del repositorio.
#
# Sale con 1 si alguna auditoría encontró vulnerabilidades o falló.
set -uo pipefail

OSV_IMAGE=ghcr.io/google/osv-scanner:v2.7.1

cd "$(dirname "$0")/.."
mkdir -p security/results

declare -A codes

# audit NOMBRE CARPETA COMANDO...: ejecuta el comando en la carpeta y guarda su salida.
audit() {
  local name=$1 dir=$2
  shift 2
  echo "== $name ($dir): $*"
  (cd "$dir" && "$@") > "security/results/audit-$name.txt" 2>&1
  codes[$name]=$?
}

audit go      go           go tool govulncheck ./...
audit python  python       uv audit --preview-features audit-command --locked
audit locust  perf/locust  uv audit --preview-features audit-command --locked
audit java    java         docker run --rm -v "$PWD/java:/src" "$OSV_IMAGE" scan source -L /src/pom.xml
audit rust    rust         cargo audit
audit e2e     e2e          pnpm audit

echo
echo "| Auditoría | Código de salida |"
echo "|---|---:|"
status=0
for name in go python locust java rust e2e; do
  echo "| $name | ${codes[$name]} |"
  [ "${codes[$name]}" -eq 0 ] || status=1
done
exit $status
