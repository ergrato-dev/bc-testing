#!/usr/bin/env bash
# La suite Go completa en un solo comando, como mvn verify en Java.
# Se para en el primer paso que falla. Necesita Docker para la integración.
set -euo pipefail
cd "$(dirname "$0")"

echo "== go vet y staticcheck"
go vet ./...
go vet -tags integration ./...
# TODO: staticcheck con y sin la tag integration (paso 2)

echo "== unitarios"
go test -count=1 -race -shuffle=on ./...

# TODO: unitarios + integración con cobertura en cover.out (paso 2)

# TODO: los benchmarks, una iteración (paso 2)

# TODO: el umbral del 85 % sobre cover.out (paso 3)
