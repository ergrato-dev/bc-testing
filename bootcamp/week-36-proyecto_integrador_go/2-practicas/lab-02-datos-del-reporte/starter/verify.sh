#!/usr/bin/env bash
# La suite Go completa en un solo comando, como mvn verify en Java.
# Se para en el primer paso que falla. Necesita Docker para la integración.
set -euo pipefail
cd "$(dirname "$0")"

echo "== go vet"
go vet ./...
go vet -tags integration ./...

echo "== unitarios"
go test -count=1 -race -shuffle=on ./...

echo "== unitarios + integración, con cobertura"
go test -count=1 -race -shuffle=on -tags integration -covermode=atomic -coverpkg=./... -coverprofile=cover.out ./...

echo "== umbral de cobertura"
total=$(go tool cover -func=cover.out | awk '/^total:/ {print $3+0}')
echo "total: $total % (mínimo: 85 %)"
awk -v total="$total" 'BEGIN {if (total < 85) {print "FALLO: la cobertura no llega al 85 %"; exit 1}}'
