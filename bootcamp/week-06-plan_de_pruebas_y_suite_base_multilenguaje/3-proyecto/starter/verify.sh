#!/usr/bin/env bash
# verify.sh: ejecuta las tres suites y comprueba la trazabilidad con plan-de-pruebas.md.
set -euo pipefail
cd "$(dirname "$0")"

echo "== JavaScript"
(cd js && pnpm test)

echo "== Python"
(cd python && uv run pytest -q)

echo "== Java"
(cd java && mvn -B test | grep -E "Tests run:|FAIL")

echo "== Trazabilidad"
ID='TC-[A-Z0-9]+(-[A-Z0-9]+)*'
huecos=0

# trazar LENGUAJE COLUMNA CARPETA ARCHIVOS PATRON_DE_TEST
trazar() {
  local esperados encontrados faltan sobran tests con_id
  esperados=$(awk -F'|' -v c="$2" '/^[|] TC-/ && $c ~ /✅/ { gsub(/ /, "", $2); print $2 }' plan-de-pruebas.md | sort -u)
  encontrados=$(grep -rhIE --include="$4" "^ *(//|#) *$ID *$" "$3" | grep -oE "$ID" | sort -u || true)
  faltan=$(comm -23 <(echo "$esperados") <(echo "$encontrados"))
  sobran=$(comm -13 <(echo "$esperados") <(echo "$encontrados"))
  tests=$(grep -rhIE --include="$4" "$5" "$3" | wc -l)
  con_id=$(grep -rhIE --include="$4" "^ *(//|#) *$ID *$" "$3" | wc -l)

  echo "$1: $tests tests, $con_id con ID"
  if [ -n "$faltan" ]; then echo "  faltan en los tests:" $faltan; huecos=1; fi
  if [ -n "$sobran" ]; then echo "  no están en la matriz:" $sobran; huecos=1; fi
  if [ "$con_id" -lt "$tests" ]; then echo "  hay tests sin ID"; huecos=1; fi
}

trazar JavaScript 6 js/tests '*.js' '^ *(it|test)[(]'
trazar Python 7 python/tests '*.py' '^ *def test_'
trazar Java 8 java/src/test '*.java' '^ *@Test *$'

if [ "$huecos" -ne 0 ]; then
  echo "FALLO: la matriz y los tests no coinciden"
  exit 1
fi
echo "OK: tres suites en verde y trazabilidad completa"
