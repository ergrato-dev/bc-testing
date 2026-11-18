#!/usr/bin/env bash
# ZAP baseline contra la API Go del dominio -> security/results/zap-baseline.{html,json,txt}
#
# Arranca PostgreSQL (compose.yaml de la raíz), crea de nuevo la base de datos de seguridad,
# arranca la API con un token, carga los datos de perf/seed.sql (semana 43) y ejecuta el
# escaneo pasivo de ZAP con Docker sobre una petición de cada tipo. Las reglas que se
# ignoran, con su motivo, están en security/zap.conf. Se ejecuta desde la raíz del repositorio.
#
# Sale con el código de ZAP: 0 sin avisos, 1 con algún FAIL, 2 con algún WARN.
#
# Variables (con su valor por defecto):
#   DB_USER=museum        usuario de PostgreSQL de compose.yaml
#   DB_PASSWORD=museum    su contraseña
#   SEC_DB=museum_sec     base de datos del escaneo; nunca la de tus otras suites
#   PORT=18080            puerto de la API
#   TARGET=/api/pieces/AAA-000   la ruta desde la que empieza ZAP
set -euo pipefail

DB_USER="${DB_USER:-museum}"
DB_PASSWORD="${DB_PASSWORD:-$DB_USER}"
SEC_DB="${SEC_DB:-museum_sec}"
export PORT="${PORT:-18080}"
export DATABASE_URL="postgres://$DB_USER:$DB_PASSWORD@localhost:55432/$SEC_DB"
export API_TOKENS="zap-token:MALI"
BASE_URL="http://localhost:$PORT"
TARGET="${TARGET:-/api/pieces/AAA-000}"
ZAP_IMAGE=zaproxy/zap-stable:2.17.0

cd "$(dirname "$0")/.."
mkdir -p security/results security/.bin

psql_db() { docker compose exec -T db psql -v ON_ERROR_STOP=1 -q -U "$DB_USER" "$@"; }

echo "== PostgreSQL y base de datos $SEC_DB"
docker compose up -d --wait
psql_db -d postgres -c "DROP DATABASE IF EXISTS $SEC_DB WITH (FORCE)" -c "CREATE DATABASE $SEC_DB" </dev/null

echo "== API en $BASE_URL"
(cd go && go build -o ../security/.bin/server ./cmd/server)
security/.bin/server > security/results/server.log 2>&1 &
SERVER_PID=$!
trap 'kill $SERVER_PID 2>/dev/null || true' EXIT
for _ in $(seq 50); do
  curl -s -o /dev/null "$BASE_URL/" && break
  kill -0 $SERVER_PID 2>/dev/null || { cat security/results/server.log; exit 1; }
  sleep 0.2
done

echo "== Datos de prueba"
psql_db -d "$SEC_DB" < perf/seed.sql

echo "== ZAP baseline desde $BASE_URL$TARGET"
# --network host: ZAP ve la API en localhost. El volumen es la carpeta de resultados:
# ZAP lee zap.conf de ella y escribe allí los informes.
cp security/zap.conf security/results/zap.conf
status=0
docker run --rm --network host -v "$PWD/security/results:/zap/wrk:rw" "$ZAP_IMAGE" \
  zap-baseline.py -t "$BASE_URL$TARGET" -c zap.conf \
  -r zap-baseline.html -J zap-baseline.json | tee security/results/zap-baseline.txt || status=$?
rm security/results/zap.conf
exit $status
