# Rúbrica de Evaluación - Semana 37

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | `cargo test`, `#[test]`, las macros `assert!`, pánicos y `Result`, control de la ejecución y los tres tipos de test |
| Desempeño | 40% | Labs 01 a 04 completos, con cada experimento provocado y explicado |
| Producto | 30% | Reglas del dominio en Rust con unitarios, integración, doctests y la comparativa con Go |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica qué hace `#[cfg(test)]` y por qué `mod tests` puede llamar a una función privada.
- Elige entre `assert!` y `assert_eq!` según lo que imprime cada uno al fallar.
- Explica cuándo una función debe entrar en pánico y cuándo devolver `Err`, y cómo se prueba cada caso.
- Explica qué deja pasar un `#[should_panic]` sin `expected` y qué se pierde con `?` frente a `unwrap` en un test.
- Diferencia lo que va antes y después de `--` en `cargo test`, y explica para qué sirven `--test-threads=1` e `#[ignore]`.
- Distingue unitarios, integración y doctests por dónde viven y qué ven.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo correcto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** no distingue un pánico de un `Err`, o no sabe qué ve un test de `tests/`.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: los cuatro tests, los filtros con `--exact` y `--list`, el desbordamiento de `u32` en debug y en `--release`, y la función sin `#[test]`.
- Lab 02: el pánico esperado, los `Err` con `assert_eq!` y `matches!`, el test con `?`, el diff de `pretty_assertions` y el `should_panic` sin `expected` que sigue en verde.
- Lab 03: la salida con `--nocapture` y `--show-output`, el test inestable diagnosticado con `--test-threads=1` y arreglado, y el test ignorado ejecutado con `--include-ignored`.
- Lab 04: el unitario de la función privada, los tests de `tests/`, el `E0603`, los doctests con la línea oculta y el doctest que encuentra la documentación desfasada.

### Niveles

- **Alto (36-40):** los cuatro labs completos y cada experimento explicado.
- **Medio (28-35):** labs completos sin explicar alguno de los experimentos.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- Los nueve requisitos de `./scripts/verificar-semana.sh 37` en verde.
- Errores como variantes de un `enum` comparadas con `assert_eq!`, y fronteras reales del dominio, incluido el valor máximo de cada entrada numérica.
- Un caso con caracteres no ASCII en la longitud máxima del nombre.
- Una `comparativa.md` con código de Go y de Rust, los tests que el tipo hace innecesarios y al menos un hallazgo propio del port.

### Niveles

- **Alto (27-30):** verificación en verde y un port que encuentra al menos un caso que la suite Go no tenía.
- **Medio (21-26):** verificación en verde con una comparativa genérica o sin fronteras de desbordamiento.
- **Bajo (<21):** algún requisito verificable en rojo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Semanal - Las reglas del dominio en Rust](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 37](README.md)
