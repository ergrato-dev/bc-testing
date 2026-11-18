# Glosario - Semana 40

| Término | Definición |
|---|---|
| Propiedad | Algo que se cumple para cualquier entrada: «el recargo nunca pasa de 1500», «lo que se escribe se vuelve a leer igual». |
| `proptest!` | Macro de proptest que convierte cada función de dentro en un test que se ejecuta 256 veces con entradas generadas. |
| Estrategia | La descripción de qué entradas genera proptest: un rango, `any::<T>()`, una expresión regular, `prop_oneof!` o una propia con `prop_compose!`. |
| `prop_assert!` | La aserción de proptest: devuelve el fallo a proptest en lugar de entrar en pánico. |
| `prop_assume!` | Descarta una entrada generada que no sirve para la propiedad. Con demasiados descartes, proptest se rinde (`Too many global rejects`). |
| Reducción (*shrinking*) | Lo que hace proptest con una entrada que falla: probar otras más simples mientras sigan fallando. Puede quedarse en un mínimo local. |
| `proptest-regressions/` | Carpeta con las semillas de los fallos encontrados. proptest las prueba primero en cada ejecución; se sube al repositorio. |
| Fuzzing guiado por cobertura | Generar entradas mutando las que recorrieron código nuevo. Lo hace libFuzzer. |
| cargo-fuzz | Subcomando de cargo que ejecuta libFuzzer sobre un fuzz target. Necesita nightly. |
| Fuzz target | La función que recibe cada entrada del fuzzer, escrita con `fuzz_target!` en `fuzz/fuzz_targets/`. |
| Nightly | La versión diaria de Rust, con opciones del compilador que aún no son estables. Se elige por comando: `cargo +nightly ...`. |
| Corpus | Las entradas que el fuzzer guardó porque recorrían código nuevo, en `fuzz/corpus/<target>/`. La siguiente ejecución parte de ellas. |
| `cargo fuzz tmin` | Busca una entrada más corta que falle igual. |
| criterion | Biblioteca de benchmarks para Rust estable: mide, calcula el intervalo de confianza y compara con la medición anterior. |
| `harness = false` | Opción de `[[bench]]` en `Cargo.toml` que deja el `main` a criterion. Sin ella, `cargo bench` no mide nada y no avisa. |
| `black_box` | Función de `std::hint` que impide que el compilador optimice sabiendo un valor. |
| Línea base (*baseline*) | Una medición guardada con nombre (`--save-baseline`) para comparar después (`--baseline`). |
| Umbral de ruido | Diferencia (±1 % en criterion) por debajo de la cual un cambio se considera ruido de la máquina. |
| cargo-llvm-cov | Subcomando que mide la cobertura con la instrumentación de LLVM. Necesita el componente `llvm-tools-preview`. |
| Región | Trozo de código que se ejecuta junto. Una línea con un `if` tiene varias; `^0` señala la que no se ejecutó. |
| cargo-nextest | Ejecutor de tests con un proceso por test y un resumen de fallos. No ejecuta los doctests. |
| Mutante | Una copia del código con un cambio pequeño: un operador distinto o una función que devuelve un valor fijo. |
| `MISSED` | Mutante con el que todos los tests pasan: nadie comprueba ese comportamiento. |
| `unviable` | Mutante que no compila. No cuenta. |
| Mutante equivalente | Mutante que hace lo mismo que el original con cualquier entrada. Ningún test puede matarlo. |
| clippy | El linter oficial de Rust. Con `-D warnings`, cualquier aviso hace fallar el comando. |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | [Semana 41 - Proyecto Integrador: Etapa 5 (Rust)](../../week-41-proyecto_integrador_rust/README.md) → |

[↑ Semana 40](../README.md)
