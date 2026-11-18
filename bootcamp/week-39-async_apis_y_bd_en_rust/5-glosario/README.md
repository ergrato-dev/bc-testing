# Glosario - Semana 39

| Término | Definición |
|---|---|
| Futuro (`Future`) | El valor que devuelve una función `async`. No hace nada hasta que un runtime lo ejecuta con `.await` o `block_on`. |
| Runtime | El código que ejecuta los futuros y reparte las tareas entre hilos. En Rust no viene con el lenguaje: el más usado es tokio. |
| `#[tokio::test]` | Atributo que convierte un `async fn` en un test: crea un runtime de tokio, de un hilo por defecto, y ejecuta el test dentro. |
| `flavor = "multi_thread"` | Opción de `#[tokio::test]` para usar el runtime de varios hilos. Hace falta para `block_in_place`. |
| `start_paused` | Opción de `#[tokio::test]` que arranca el reloj de tokio parado: las esperas no ocurren, pero el tiempo medido es exacto. Solo con un hilo. |
| `tokio::time::advance` | Mueve el reloj parado de tokio una cantidad exacta. Entra en pánico si el reloj no está parado. |
| `Send` | Marca de un tipo que se puede mover a otro hilo. axum exige que el futuro de cada handler sea `Send`. |
| `impl Future<Output = T> + Send` | Forma de declarar un método async en un trait garantizando que su futuro es `Send`. Las implementaciones pueden escribirse con `async fn`. |
| `Arc<Mutex<T>>` | Un valor compartido entre hilos (`Arc`) y modificable desde `&self` (`Mutex`). La versión para varios hilos de `RefCell`. |
| `Router` | El conjunto de rutas de axum. Es un servicio de tower: recibe peticiones y devuelve respuestas. |
| `oneshot` | Método de `tower::ServiceExt` que envía una sola petición a un servicio, sin abrir un puerto. Consume el servicio. |
| Extractor | Tipo que axum construye a partir de la petición antes de llamar al handler: `Path`, `Json`, `State`. Si falla, axum responde por su cuenta. |
| `IntoResponse` | Trait de axum para convertir un valor en una respuesta HTTP. Implementado para un error, permite devolver `Err` desde los handlers. |
| `json!` | Macro de serde_json que escribe un `Value` con sintaxis JSON. Se usa para comparar el cuerpo de una respuesta. |
| `MockServer` | Servidor HTTP de wiremock en un puerto local, con las respuestas que el test le programa. |
| `expect(n)` | Cuántas peticiones debe recibir un mock de wiremock. Se comprueba al destruirse el servidor. |
| `set_delay` | Retraso de una respuesta de wiremock. Sirve para probar el plazo del cliente. |
| `PgPool` | Pool de conexiones de sqlx a PostgreSQL. Se clona barato: los clones comparten las conexiones. |
| `.bind` | Valor de un parámetro (`$1`, `$2`) de una consulta de sqlx. Va aparte del SQL, nunca pegado al texto. |
| `#[sqlx::test]` | Atributo que crea una base de datos nueva por test en el PostgreSQL de `DATABASE_URL`, aplica `migrations/` y la borra si el test pasa. |
| Fixture de sqlx | Archivo SQL que `#[sqlx::test(fixtures(...))]` ejecuta tras las migraciones. Su ruta es relativa al archivo del test. |
| `_sqlx_test.databases` | Tabla donde sqlx anota las bases de los tests que fallaron, con el nombre del test. |
| `.cargo/config.toml` | Configuración de Cargo del proyecto. Su sección `[env]` pone variables de entorno, como `DATABASE_URL`, al compilar y ejecutar los tests. |
| testcontainers-rs | Crate que arranca contenedores de Docker desde un test y los borra al terminar. Sus módulos, como `postgres`, vienen en testcontainers-modules. |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | [Semana 40 - Calidad Avanzada en Rust](../../week-40-calidad_avanzada_en_rust/README.md) → |

[↑ Semana 39](../README.md)
