# Lab 01 - Repositorio con PostgreSQL y Testcontainers

## Objetivo

Probar un repositorio de SQLAlchemy contra un PostgreSQL real que Testcontainers arranca para la ejecución de pytest. Vas a comprobar consultas, el orden de los resultados y las restricciones `UNIQUE`, `CHECK` y `VARCHAR(120)`, que un mock no puede ver.

## Tiempo estimado

80 minutos.

## Preparación

Docker debe estar en marcha:

```bash
docker info --format '{{.ServerVersion}}'
# 29.9.3
```

Si el comando falla, arranca Docker Desktop (Windows y macOS) o el servicio `docker` (Linux, Ubuntu 24.04) y vuelve a probar.

```bash
cd starter
uv sync
uv run pytest -q
# no tests ran in 0.00s
```

`uv sync` crea el entorno virtual `.venv` e instala las versiones de `pyproject.toml`: `sqlalchemy`, el driver `psycopg` y, en el grupo `dev`, `pytest` y `testcontainers`. No tienes que activarlo: `uv run` lo usa por ti.

Qué hay en `starter/`:

- **`models.py`:** dos modelos.
  - `Book` (libro): `isbn` único, `title` de hasta 120 caracteres y `stock` con `CHECK stock >= 0`.
  - `Loan` (préstamo): enlazado al libro con `ForeignKey`.
- **`repository.py`:** `BookRepository`, con `add`, `get_by_isbn`, `list_available` y `lend`. `add` y `lend` hacen `commit()`. Si el ISBN está repetido, `add` lanza `DuplicateIsbnError`.
- **`tests/conftest.py`:** las fixtures `postgres_url` y `session`, comentadas (PASOS 1 y 2).
- **`tests/test_book_repository.py`:** ocho tests comentados (PASOS 3 a 10).

Lee `models.py` y `repository.py` antes de seguir. La [teoría 01](../../1-teoria/01-estrategias-y-sqlalchemy.md) explica cada pieza.

## Paso a paso

### Paso 1: el contenedor de PostgreSQL (PASO 1)

Descomenta `postgres_url` en `tests/conftest.py`:

```python
@pytest.fixture(scope="session")
def postgres_url():
    with PostgresContainer(POSTGRES_IMAGE, driver="psycopg") as postgres:
        yield postgres.get_connection_url()
```

1. **`PostgresContainer(POSTGRES_IMAGE, ...)`:** usa la imagen `postgres:18.7-alpine`, la misma versión en todos los equipos.
2. **`driver="psycopg"`:** la URL que devuelve usará el driver `psycopg` 3.
3. **`with`:** arranca el contenedor en un puerto libre y espera a que acepte conexiones. Al salir, lo detiene y lo borra.
4. **`scope="session"`:** el contenedor se arranca una vez para toda la ejecución de pytest, no una vez por test.

### Paso 2: la fixture `session` (PASO 2)

Descomenta `session`:

```python
@pytest.fixture
def session(postgres_url):
    engine = create_engine(postgres_url)
    Base.metadata.create_all(engine)
    with Session(engine) as session:
        yield session
    Base.metadata.drop_all(engine)
    engine.dispose()
```

1. **`create_engine(postgres_url)`:** conecta con el PostgreSQL del contenedor.
2. **`create_all`:** crea las tablas `books` y `loans`.
3. **`yield session`:** entrega la sesión al test.
4. **`drop_all`:** al terminar el test borra las tablas, y con ellas los datos.
5. **`engine.dispose()`:** cierra las conexiones.

Cada test recibe tablas nuevas y vacías.

### Paso 3: el primer test (PASO 3)

Descomenta `test_add_assigns_an_id` y ejecuta:

```bash
uv run pytest -q
# .                                                                        [100%]
# 1 passed in 3.30s
```

Casi todo ese tiempo es el arranque del contenedor. La primera vez también se descarga la imagen, así que tarda más.

El `id` lo asigna la base de datos en el `INSERT`. Que no sea `None` demuestra que la fila llegó a la tabla.

### Paso 4: buscar lo que no existe (PASO 4)

Descomenta `test_get_by_isbn_returns_none_when_missing`. `one_or_none()` devuelve `None` si la consulta no encuentra filas y lanza una excepción si encuentra más de una.

### Paso 5: filtro y orden (PASO 5)

Descomenta `test_list_available_skips_out_of_stock_and_sorts_by_title`. El test inserta los libros en un orden (`Dune`, `Ciropedia`, `Babel`) y espera otro (`Babel`, `Dune`):

- `Ciropedia` no aparece porque tiene `stock=0`.
- El orden lo pone el `ORDER BY title` de la consulta.

> Prueba a borrar `.order_by(Book.title)` en `repository.py` y ejecuta los tests. Sin `ORDER BY`, aquí PostgreSQL devuelve las filas en el orden en que se guardaron:
>
> ```text
> E       AssertionError: assert ['Dune', 'Babel'] == ['Babel', 'Dune']
> ```
>
> Después restaura la línea. Ninguna base de datos garantiza un orden sin `ORDER BY`: un test que lo da por hecho puede pasar hoy y fallar mañana.

### Paso 6: préstamo (PASO 6)

Descomenta `test_lend_decrements_stock_and_records_the_loan`. El test comprueba dos efectos de `lend`: el stock baja y aparece una fila en `loans`. La consulta `select(Loan)` lee esa tabla directamente, sin pasar por el repositorio.

### Paso 7: sin stock (PASO 7)

Descomenta `test_lend_without_stock_raises`. Es la regla de negocio: si no quedan copias, `lend` no presta y lanza `OutOfStockError`.

### Paso 8: ISBN duplicado (PASO 8)

Descomenta `test_duplicate_isbn_raises_domain_error`. El `UNIQUE` de `isbn` lo comprueba PostgreSQL. El repositorio traduce el `IntegrityError` de SQLAlchemy a `DuplicateIsbnError`, que es un error del dominio.

### Paso 9: la restricción `CHECK` (PASO 9)

Descomenta `test_negative_stock_violates_check_constraint`. Este test no usa el repositorio: va directo a la sesión para comprobar que la tabla rechaza un stock negativo.

### Paso 10: la longitud de `VARCHAR` (PASO 10)

Descomenta `test_title_longer_than_120_characters_is_rejected`. `String(120)` se crea como `VARCHAR(120)`, y PostgreSQL rechaza un título de 121 caracteres con un `DataError`. Si quitas el `pytest.raises`, ves el error del motor:

```text
E           psycopg.errors.StringDataRightTruncation: value too long for type character varying(120)
```

```bash
uv run pytest -q
# ........                                                                 [100%]
# 8 passed in 3.92s
```

## Comprobación

Tu `starter/` debe coincidir con `solution/`:

```bash
cd ../solution
uv sync
uv run pytest -q
# 8 passed in 3.92s
```

## Preguntas de cierre

1. ¿Qué test seguiría pasando si sustituyeras la sesión por un `Mock`? ¿Cuáles dejarían de tener sentido?
2. La fixture crea y borra las tablas en cada test. ¿Qué pasaría con 40 tablas y 200 tests? (La respuesta está en el lab 02.)

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Rollback por test](../lab-02-rollback-por-test/README.md) → |

[↑ Semana 23](../../README.md)
