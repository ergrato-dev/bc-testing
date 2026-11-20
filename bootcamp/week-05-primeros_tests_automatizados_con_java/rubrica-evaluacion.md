# Rúbrica de Evaluación - Semana 05

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Proyecto Maven con JUnit 6 y AssertJ, tests con `@Test` y `@Nested`, `assertEquals` con delta, `assertThrows`, `Failure` frente a `Error` y a un error de compilación, ejecución parcial |
| Desempeño | 40% | Labs 01 a 04: primer proyecto, AAA y assertions, casos del planetario, tests que mienten |
| Producto | 30% | La suite de la semana 4 traducida a JUnit, con mutaciones anotadas |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica qué hace cada parte del `pom.xml` (coordenadas, `maven.compiler.release`, dependencias con `scope` `test`, Surefire) y su pareja en los proyectos de las semanas 3 y 4.
- Explica por qué un error de compilación impide ejecutar todos los tests y qué sale sin la dependencia de JUnit.
- Escribe un test con `@Test`, `@DisplayName` y nombre `should<Resultado>When<Condición>`, con AAA, y agrupa tests en una clase `@Nested`.
- Explica por qué `assertEquals` recibe primero el esperado y qué dice el informe si se invierte el orden.
- Elige entre `assertEquals`, `assertNull`, `assertTrue` y `assertThrows` según el valor, y explica por qué `==` no sirve para comparar textos y por qué una clase sin `equals` no es igual a otra con el mismo contenido.
- Explica qué delta usar con decimales y por qué `0.01` daría por bueno 16,9915 como 16,99.
- Distingue `Failure`, `Error` y `COMPILATION ERROR`, y explica por qué `BUILD SUCCESS` no garantiza que se ejecutara algún test.
- Ejecuta una clase, un test con `Clase#método` y un filtro con `*`.
- Escribe la misma comprobación con JUnit y con AssertJ.

### Niveles

- **Alto (27-30):** responde con ejemplos propios y compara cada pieza con su equivalente de Jest y de pytest.
- **Medio (21-26):** comprende los conceptos con alguna confusión menor (por ejemplo, el orden de `assertEquals`).
- **Bajo (<21):** no distingue un `Failure` de un error de compilación, o cree que `BUILD SUCCESS` significa que los tests comprobaron algo.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: `~/labs-semana-05` con un `pom.xml` escrito a mano, 3 tests de `Calculator` en verde, `target/` en `.gitignore` y un commit.
- Lab 02: 10 tests de `UserUtils` en verde, sin `TODO`; `createUser` con `assertEquals` del `User` completo, la búsqueda fallida con `assertNull`, el descuento de 29,90 con delta `0.0001` y dos tests con AssertJ.
- Lab 03: 19 tests del planetario en tres clases `@Nested`, con el ID de su caso, 4 hallazgos con línea y técnica, y el código corregido sin tocar ningún esperado.
- Lab 04: los cinco tests reescritos, el envío probado con 29,99 y 30, y cada una de las cuatro mutaciones detectada por un test.

### Niveles

- **Alto (36-40):** los cuatro labs completos, con cada decisión (comprobación, delta, código o test) justificada.
- **Medio (28-35):** los cuatro labs completos, con algún esperado calculado en lugar de escrito o algún nombre genérico.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- `PROYECTO=... ./scripts/verificar-semana.sh 05` pasa las 10 comprobaciones.
- Los métodos son las funciones de la semana 4, con los mismos mensajes, y siguen siendo puros.
- Los tests cubren los mismos casos que la suite de pytest, con el ID del caso encima; los que el tipado hace imposibles están anotados en un comentario.
- Cada comprobación corresponde a su valor; todos los esperados están escritos a mano y en primer lugar.
- Las mutaciones de `mutaciones.md` son reales: al repetirlas, el test anotado falla.

### Niveles

- **Alto (27-30):** todo lo anterior, con alguna diferencia entre lenguajes encontrada y anotada (un caso que Java no deja escribir, o un test que pasaba en pytest y falla en JUnit).
- **Medio (21-26):** pasan las comprobaciones, pero la suite de Java cubre menos casos que la de pytest o algún límite no se prueba.
- **Bajo (<21):** no pasan las comprobaciones.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto - Primera suite de tests del dominio](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 05](README.md)
