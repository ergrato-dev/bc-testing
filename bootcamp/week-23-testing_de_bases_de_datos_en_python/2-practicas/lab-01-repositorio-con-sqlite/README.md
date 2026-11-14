# Lab 01 - Repositorio con SQLite en memoria

## Objetivo

Probar un repositorio de SQLAlchemy contra una base de datos real (SQLite en memoria). Vas a comprobar consultas, el orden de los resultados y las restricciones `UNIQUE` y `CHECK`, que un mock no puede ver.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
uv sync
uv run pytest -q
# no tests ran in 0.01s
```

`uv sync` crea el entorno virtual `.venv` e instala `sqlalchemy` y `pytest` con las versiones de `pyproject.toml`. No tienes que activarlo: `uv run` lo usa por ti.

Qué hay en `starter/`:

- **`models.py`:** dos modelos.
  - `Book` (libro): `isbn` único, `title` de hasta 120 caracteres y `stock` con `CHECK stock >= 0`.
  - `Loan` (préstamo): enlazado al libro con `ForeignKey`.
- **`repository.py`:** `BookRepository`, con `add`, `get_by_isbn`, `list_available` y `lend`. `add` y `lend` hacen `commit()`. Si el ISBN está repetido, `add` lanza `DuplicateIsbnError`.
- **`tests/conftest.py`:** la fixture `session`, comentada (PASO 1).
- **`tests/test_book_repository.py`:** siete tests comentados (PASOS 2 a 8).

Lee `models.py` y `repository.py` antes de seguir. La [teoría 01](../../1-teoria/01-estrategias-y-sqlalchemy.md) explica cada pieza.

## Paso a paso

### Paso 1: la fixture `session` (PASO 1)

Descomenta la fixture en `tests/conftest.py`:

```python
@pytest.fixture
def session():
    engine = create_engine("sqlite+pysqlite:///:memory:")
    Base.metadata.create_all(engine)
    with Session(engine) as session:
        yield session
    engine.dispose()
```

1. **`create_engine("sqlite+pysqlite:///:memory:")`:** crea una base de datos SQLite en RAM.
2. **`create_all`:** crea las tablas `books` y `loans`.
3. **`yield session`:** entrega la sesión al test.
4. **`engine.dispose()`:** al terminar el test cierra la conexión y la base de datos desaparece.

Cada test recibe una base de datos nueva y vacía.

### Paso 2: el primer test (PASO 2)

Descomenta `test_add_assigns_an_id` y ejecuta:

```bash
uv run pytest -q
# .                                                                        [100%]
# 1 passed in 0.05s
```

El `id` lo asigna la base de datos en el `INSERT`. Que no sea `None` demuestra que la fila llegó a la tabla.

### Paso 3: buscar lo que no existe (PASO 3)

Descomenta `test_get_by_isbn_returns_none_when_missing`. `one_or_none()` devuelve `None` si la consulta no encuentra filas y lanza una excepción si encuentra más de una.

### Paso 4: filtro y orden (PASO 4)

Descomenta `test_list_available_skips_out_of_stock_and_sorts_by_title`. El test inserta los libros en un orden (`Dune`, `Ciropedia`, `Babel`) y espera otro (`Babel`, `Dune`):

- `Ciropedia` no aparece porque tiene `stock=0`.
- El orden lo pone el `ORDER BY title` de la consulta.

> Prueba a borrar `.order_by(Book.title)` en `repository.py` y ejecuta los tests. Sin `ORDER BY`, SQLite devuelve las filas en el orden en que se guardaron:
>
> ```text
> E       AssertionError: assert ['Dune', 'Babel'] == ['Babel', 'Dune']
> ```
>
> Después restaura la línea. Ninguna base de datos garantiza un orden sin `ORDER BY`: un test que lo da por hecho puede pasar hoy y fallar mañana.

### Paso 5: préstamo (PASO 5)

Descomenta `test_lend_decrements_stock_and_records_the_loan`. El test comprueba dos efectos de `lend`: el stock baja y aparece una fila en `loans`. La consulta `select(Loan)` lee esa tabla directamente, sin pasar por el repositorio.

### Paso 6: sin stock (PASO 6)

Descomenta `test_lend_without_stock_raises`. Es la regla de negocio: si no quedan copias, `lend` no presta y lanza `OutOfStockError`.

### Paso 7: ISBN duplicado (PASO 7)

Descomenta `test_duplicate_isbn_raises_domain_error`. El `UNIQUE` de `isbn` lo comprueba SQLite. El repositorio traduce el `IntegrityError` de SQLAlchemy a `DuplicateIsbnError`, que es un error del dominio.

### Paso 8: la restricción `CHECK` (PASO 8)

Descomenta `test_negative_stock_violates_check_constraint`. Este test no usa el repositorio: va directo a la sesión para comprobar que la tabla rechaza un stock negativo.

```bash
uv run pytest -q
# .......                                                                  [100%]
# 7 passed in 0.06s
```

## Comprobación

Tu `starter/` debe coincidir con `solution/`:

```bash
cd ../solution
uv sync
uv run pytest -q
# 7 passed in 0.06s
```

## Preguntas de cierre

1. ¿Qué test seguiría pasando si sustituyeras la sesión por un `Mock`? ¿Cuáles dejarían de tener sentido?
2. La fixture crea las tablas en cada test. ¿Qué pasaría con 40 tablas y 200 tests? (La respuesta está en el lab 02.)

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Rollback por test](../lab-02-rollback-por-test/README.md) → |

[↑ Semana 23](../../README.md)
