# Calidad de la suite Rust de <tu dominio>

## Propiedades (proptest)

TODO (pasos 3 y 4): una tabla con cada propiedad y lo que fija. El fallo que encontró la propiedad contra PostgreSQL: la salida con `minimal failing input`, la causa y el arreglo.

## Benchmark (criterion)

TODO (paso 5): qué mides y por qué. La salida con la línea base `before` y la comparación (`change:`).

## Cobertura (cargo-llvm-cov)

TODO (paso 6): la cobertura de líneas por archivo y qué hay en las líneas sin ejecutar.

## Mutantes (cargo-mutants)

TODO (paso 7): la salida con `mutants tested`, una fila por superviviente con por qué sobrevivía y qué lo mata (o por qué es equivalente), y la ejecución final.

## clippy y nextest

TODO (paso 8): el resultado de `cargo clippy --all-targets -- -D warnings` y de `cargo nextest run`.
