# Lab 02 - Rollback por test

## Objetivo

Crear el esquema una sola vez y aislar cada test con una transacción que termina en rollback. Vas a provocar el fallo que aparece sin aislamiento, aplicar el patrón con savepoint y comprobar qué pasa sin `create_savepoint`.

## Tiempo estimado

50 minutos.

## Preparación

Docker debe estar en marcha, como en el lab 01.

```bash
cd starter
uv sync
```

- `models.py` y `repository.py` son los del [lab 01](../lab-01-repositorio-con-postgresql/README.md).
- `tests/test_isolation.py` trae tres tests activos que **dependen del aislamiento**:
  1. `test_lend_commits_inside_the_test`: presta un libro; `lend` hace `commit()`.
  2. `test_next_test_starts_with_an_empty_database`: espera que no haya libros.
  3. `test_rollback_inside_the_code_keeps_previous_commits`: inserta un libro y luego un duplicado; el repositorio hace `rollback()` y el primer libro debe seguir ahí.
- `tests/conftest.py` trae `postgres_url` del lab 01, una fixture `engine` con scope `session` que crea las tablas una vez y una fixture `session` **sin aislamiento** (PASO 1).

La [teoría 02](../../1-teoria/02-fixtures-y-rollback-por-test.md) explica el patrón paso a paso.

## Paso a paso

### Paso 1: ver el fallo sin aislamiento (PASO 1)

```bash
uv run pytest -q
# FAILED tests/test_isolation.py::test_next_test_starts_with_an_empty_database
# FAILED tests/test_isolation.py::test_rollback_inside_the_code_keeps_previous_commits
# 2 failed, 1 passed in 4.08s
```

Lee los dos errores:

- **Test 2:** `assert [<models.Book...>] == []`. Encuentra el libro que el test 1 confirmó con `commit()`.
- **Test 3:** `duplicate key value violates unique constraint "books_isbn_key"` al insertar el **primer** libro, porque el test 1 ya usó ese ISBN.

Ejecuta solo el test 2:

```bash
uv run pytest -q tests/test_isolation.py::test_next_test_starts_with_an_empty_database
# 1 passed in 3.40s
```

Solo pasa si se ejecuta solo: depende del orden de ejecución.

### Paso 2: transacción externa con savepoint (PASO 2)

1. Borra la fixture `session` del PASO 1, con su comentario.
2. Descomenta la del PASO 2:

```python
@pytest.fixture
def session(engine):
    with engine.connect() as connection:
        transaction = connection.begin()
        session = Session(bind=connection, join_transaction_mode="create_savepoint")
        yield session
        session.close()
        transaction.rollback()
```

```bash
uv run pytest -q
# ...                                                                      [100%]
# 3 passed in 3.53s
```

Ejecuta los tests en orden inverso para confirmar que ya no dependen del orden:

```bash
uv run pytest -q tests/test_isolation.py::test_rollback_inside_the_code_keeps_previous_commits tests/test_isolation.py::test_next_test_starts_with_an_empty_database tests/test_isolation.py::test_lend_commits_inside_the_test
# 3 passed in 3.78s
```

### Paso 3: experimento sin `create_savepoint`

Borra `, join_transaction_mode="create_savepoint"` de la fixture y ejecuta:

```bash
uv run pytest -q
# E       AttributeError: 'NoneType' object has no attribute 'title'
# FAILED tests/test_isolation.py::test_rollback_inside_the_code_keeps_previous_commits
# 1 failed, 2 passed, 1 warning in 3.35s
```

Sin ese argumento, el `rollback()` del repositorio deshace la transacción externa entera y se lleva por delante el primer libro. El aviso `SAWarning: transaction already deassociated from connection` lo confirma: cuando la fixture intenta hacer su rollback, la transacción ya no existe.

Vuelve a añadir el argumento.

## Comprobación

```bash
cd ../solution
uv sync
uv run pytest -q
# 3 passed in 3.34s
```

## Preguntas de cierre

1. ¿Por qué el esquema puede crearse una sola vez y los datos no?
2. El código bajo prueba hace `commit()`. ¿Por qué ese commit no llega a la base de datos al terminar el test?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Repositorio con PostgreSQL y Testcontainers](../lab-01-repositorio-con-postgresql/README.md) | [Lab 03 - Datos de prueba con Faker y Factory Boy](../lab-03-datos-con-faker-y-factory-boy/README.md) → |

[↑ Semana 23](../../README.md)
