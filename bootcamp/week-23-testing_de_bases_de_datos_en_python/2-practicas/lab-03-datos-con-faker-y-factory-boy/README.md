# Lab 03 - Datos de prueba con Faker y Factory Boy

## Objetivo

Sustituir los datos escritos a mano por factories: cada test indica solo los campos que le importan, los objetos relacionados se crean solos y todo se borra con el rollback del [lab 02](../lab-02-rollback-por-test/README.md).

## Tiempo estimado

50 minutos.

## Preparación

Docker debe estar en marcha, como en el lab 01.

```bash
cd starter
uv sync
uv run pytest -q
# no tests ran in 0.00s
```

Qué hay en `starter/`:

- **`pyproject.toml`:** añade `faker==40.54.0` y `factory-boy==3.3.3` al grupo `dev`.
- **`tests/conftest.py`:** trae `postgres_url`, `engine` y `session` del lab 02, ya completas. Las fixtures de factories están comentadas (PASOS 3 y 4).
- **`tests/factories.py`:** `BookFactory` y `LoanFactory`, comentadas (PASOS 1 y 2).
- **`tests/test_with_factories.py`:** cinco tests comentados (PASOS 5 a 9).
- **`tests/__init__.py`:** convierte `tests/` en paquete para que funcione `from tests.factories import ...`.

La [teoría 03](../../1-teoria/03-datos-con-faker-y-factory-boy.md) explica cada declaración.

## Paso a paso

### Paso 1: `BookFactory` (PASO 1)

Descomenta `BookFactory` en `tests/factories.py`. Fíjate en tres decisiones:

- **`isbn` con `factory.Sequence`:** el ISBN debe ser único, así que sale de un contador y no de Faker.
- **`title` con `factory.Faker("sentence", ...)`:** el título solo tiene que parecer real.
- **`sqlalchemy_session_persistence = "flush"`:** la fila se inserta, pero sin `commit()`, dentro de la transacción del test.

### Paso 2: `LoanFactory` (PASO 2)

Descomenta `LoanFactory`. `book = factory.SubFactory(BookFactory)` crea un libro nuevo para cada préstamo, salvo que le pases uno.

### Paso 3: conectar las factories a la sesión (PASO 3)

En `tests/conftest.py`, descomenta el import `from tests.factories import ...` y la fixture `bind_factories`.

`autouse=True` hace que la fixture se ejecute en cada test sin que el test la pida. Asigna a cada factory la sesión del test, que es la que tiene el rollback.

### Paso 4: semilla fija (PASO 4)

Descomenta `fixed_seed`. `factory.random.reseed_random(2026)` reinicia la semilla del Faker de las factories antes de cada test. Así cada test genera siempre los mismos títulos y correos, aunque cambie el orden de ejecución.

### Paso 5: Faker por separado (PASO 5)

En `tests/test_with_factories.py`, descomenta el import `from tests.factories import ...` y `test_faker_with_seed_is_repeatable`. Dos instancias con la misma semilla producen el mismo nombre.

Pruébalo en la terminal:

```bash
uv run python -c "from faker import Faker; f = Faker('es_ES'); f.seed_instance(42); print(f.name())"
# Albano Llopis Hierro
```

### Paso 6: solo lo que importa (PASO 6)

Descomenta `test_factory_overrides_only_what_the_test_cares_about`:

```python
BookFactory(stock=0)
available = BookFactory(stock=3)
```

Compáralo con el lab 01, donde cada libro necesitaba ISBN, título y stock escritos a mano.

### Paso 7: lotes (PASO 7)

Descomenta `test_create_batch_builds_many_rows`. `create_batch(5)` crea cinco libros con ISBN distintos.

### Paso 8: objetos relacionados (PASO 8)

Descomenta `test_sub_factory_creates_the_related_book`. `LoanFactory(book__title="Dune")` crea el préstamo y su libro, y cambia el título del libro con el doble guion bajo.

### Paso 9: un flujo completo (PASO 9)

Descomenta `test_lend_last_copy_then_out_of_stock`. La factory crea el libro y el repositorio hace el resto, con sus `commit()`, dentro del savepoint.

```bash
uv run pytest -q
# .....                                                                    [100%]
# 5 passed in 3.80s
```

## Comprobación

```bash
cd ../solution
uv sync
uv run pytest -q
# 5 passed in 3.80s
```

## Preguntas de cierre

1. ¿Qué pasaría si `isbn` usara `factory.Faker("isbn13")` en lugar de `Sequence`?
2. ¿Qué diferencia hay entre `sqlalchemy_session_persistence = "flush"` y `"commit"` cuando la sesión tiene rollback por test?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Rollback por test](../lab-02-rollback-por-test/README.md) | [Lab 04 - Migraciones con Alembic](../lab-04-migraciones-con-alembic/README.md) → |

[↑ Semana 23](../../README.md)
