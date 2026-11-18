# Rúbrica de Evaluación - Semana 39

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Runtimes de tokio y reloj en pausa, `oneshot`, traits async con `Send`, wiremock, `#[sqlx::test]` y testcontainers-rs |
| Desempeño | 40% | Labs 01 a 04 completos, con cada experimento provocado y explicado |
| Producto | 30% | La API del dominio en Rust con axum, su cliente externo con reqwest y sus almacenes en PostgreSQL, cada capa con su test |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica qué hace `#[tokio::test]` y cuándo hace falta `flavor = "multi_thread"`.
- Explica qué reloj para `start_paused` y en qué se diferencia de `tokio::time::advance` y de `synctest` de Go.
- Explica qué parte de la aplicación ejecuta `oneshot` y qué códigos da axum antes de llegar al handler.
- Explica por qué los métodos async de un trait que usa axum se declaran como `impl Future<Output = ...> + Send`, y por qué los fakes pasan de `RefCell` a `Mutex`.
- Explica qué responde wiremock a una petición que ningún mock acepta y para qué sirve `expect`.
- Compara `#[sqlx::test]` con la transacción por test de Go y con testcontainers-rs.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo correcto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** no distingue un test con el reloj en pausa de uno con el reloj real, o no sabe qué crea `#[sqlx::test]` para cada test.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: los tests con `start_paused`, `timeout`, `join!` y `advance`, el runtime de varios hilos, y los cuatro experimentos (`#[test]` async, reloj real, `block_in_place` en un hilo, `advance` sin pausa).
- Lab 02: los tests con `oneshot`, el estado compartido, los `400`, `415` y `422` de axum, y los experimentos del trait sin importar, el campo renombrado y el router movido.
- Lab 03: los tests con wiremock, la cabecera olvidada que solo detecta `expect`, el `404` tratado como caída y el plazo del cliente.
- Lab 04: los tests con `#[sqlx::test]` y fixtures, el test con testcontainers, la base de un test que falla inspeccionada con `psql`, la fixture que no existe y la suite sin servidor.

### Niveles

- **Alto (36-40):** los cuatro labs completos y cada experimento explicado.
- **Medio (28-35):** labs completos sin explicar alguno de los experimentos.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- Los once requisitos de `./scripts/verificar-semana.sh 39` en verde.
- Traits async con `+ Send`, el servicio async y los tests de la semana 38 convertidos a `#[tokio::test]` sin perder ninguno.
- La API con las rutas y los códigos de la API Go, probada con `oneshot`: el JSON con `json!`, un caso por rechazo, el 500 que no enseña el error, el 400 y el 405.
- El cliente externo con plazo, y sus tests con wiremock con `expect` en la ruta exacta.
- Los almacenes en PostgreSQL con la migración de Go sin cambios, probados con `#[sqlx::test]`, incluido lo que el esquema rechaza y el servicio completo; y `dobles.md` con lo que cada capa detecta.

### Niveles

- **Alto (27-30):** verificación en verde y un `dobles.md` que explica qué detecta cada capa que las otras no.
- **Medio (21-26):** verificación en verde con algún test que no comprueba lo que su nombre dice, o un `dobles.md` genérico.
- **Bajo (<21):** algún requisito verificable en rojo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Semanal - La API del dominio en Rust, sobre PostgreSQL](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 39](README.md)
