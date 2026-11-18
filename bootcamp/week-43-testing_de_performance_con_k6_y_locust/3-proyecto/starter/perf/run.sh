#!/usr/bin/env bash
# Ejecuta una prueba de rendimiento contra la API Go del dominio.
#
#   perf/run.sh smoke    k6 y Locust con 1-2 usuarios unos segundos: ¿funciona todo?
#   perf/run.sh load     prueba de carga con k6 -> perf/results/load-summary.json y load-report.html
#   perf/run.sh stress   stress test con Locust -> perf/results/stress_*.csv y stress.html
#
# Cada ejecución parte de cero: arranca PostgreSQL (compose.yaml de la raíz), crea de nuevo la
# base de datos de rendimiento, arranca la API, carga los datos de perf/seed.sql, ejecuta la
# prueba y para la API. Se ejecuta desde la raíz del repositorio.
#
# Variables (con su valor por defecto):
#   DB_USER=museum        usuario de PostgreSQL de compose.yaml
#   DB_PASSWORD=museum    su contraseña (por defecto, la misma que el usuario)
#   PERF_DB=museum_perf   base de datos de las pruebas; nunca la de tus otras suites
#   PORT=18080            puerto de la API
#   DB_MAX_CONNS          conexiones del pool de la API (por defecto, las de pgxpool)
set -euo pipefail

MODE="${1:?uso: perf/run.sh smoke|load|stress}"
DB_USER="${DB_USER:-museum}"
DB_PASSWORD="${DB_PASSWORD:-$DB_USER}"
PERF_DB="${PERF_DB:-museum_perf}"
export PORT="${PORT:-18080}"
export BASE_URL="http://localhost:$PORT"
export DATABASE_URL="postgres://$DB_USER:$DB_PASSWORD@localhost:55432/$PERF_DB"

cd "$(dirname "$0")/.."
mkdir -p perf/results perf/.bin

psql_db() { docker compose exec -T db psql -v ON_ERROR_STOP=1 -q -U "$DB_USER" "$@"; }

echo "== PostgreSQL y base de datos $PERF_DB"
docker compose up -d --wait
psql_db -d postgres -c "DROP DATABASE IF EXISTS $PERF_DB WITH (FORCE)" -c "CREATE DATABASE $PERF_DB" </dev/null

echo "== API en $BASE_URL"
(cd go && go build -o ../perf/.bin/server ./cmd/server)
perf/.bin/server > perf/results/server.log 2>&1 &
SERVER_PID=$!
trap 'kill $SERVER_PID 2>/dev/null || true' EXIT
for _ in $(seq 50); do
  curl -s -o /dev/null "$BASE_URL/" && break
  kill -0 $SERVER_PID 2>/dev/null || { cat perf/results/server.log; exit 1; }
  sleep 0.2
done

echo "== Datos de prueba"
psql_db -d "$PERF_DB" < perf/seed.sql

case "$MODE" in
  smoke)
    k6 run -q -e PROFILE=smoke perf/k6/load.js
    (cd perf/locust && STEP_USERS=2 MAX_USERS=2 STEP_SECONDS=5 uv run locust -f locustfile.py \
      --headless --only-summary --host "$BASE_URL")
    ;;
  load)
    K6_WEB_DASHBOARD=true K6_WEB_DASHBOARD_EXPORT=perf/results/load-report.html \
      k6 run --summary-export perf/results/load-summary.json perf/k6/load.js
    ;;
  stress)
    # Un stress test busca el punto de rotura: los errores no lo hacen fallar.
    (cd perf/locust && uv run locust -f locustfile.py --headless --only-summary --host "$BASE_URL" \
      --csv ../results/stress --html ../results/stress.html --exit-code-on-error 0)
    ;;
  *)
    echo "modo desconocido: $MODE (smoke, load o stress)" >&2
    exit 2
    ;;
esac
