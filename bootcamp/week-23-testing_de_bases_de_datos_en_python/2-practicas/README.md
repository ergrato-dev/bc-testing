# Prácticas - Semana 23

Cuatro labs guiados (4 h en total). Cada uno trae un `starter/` con el código comentado por PASOS y una `solution/` con el resultado. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Repositorio con PostgreSQL y Testcontainers](lab-01-repositorio-con-postgresql/README.md) | PostgreSQL desechable, modelos, `select()`, `UNIQUE`, `CHECK` y `VARCHAR` probados contra el motor real | 80 min |
| [Lab 02 - Rollback por test](lab-02-rollback-por-test/README.md) | Esquema una vez por sesión, transacción externa y `create_savepoint` | 50 min |
| [Lab 03 - Datos de prueba con Faker y Factory Boy](lab-03-datos-con-faker-y-factory-boy/README.md) | `SQLAlchemyModelFactory`, `Sequence`, `SubFactory` y semilla fija | 50 min |
| [Lab 04 - Migraciones con Alembic](lab-04-migraciones-con-alembic/README.md) | `autogenerate` y `upgrade`/`check`/`downgrade` probados | 60 min |

Todos los labs necesitan **Docker** en marcha: los tests arrancan PostgreSQL con Testcontainers.

## Antes de empezar

```bash
uv --version     # uv 0.13.7 o superior
docker info --format '{{.ServerVersion}}'
```

Cada lab es un proyecto `uv` independiente:

```bash
cd lab-NN-.../starter
uv sync
uv run pytest -q
```

`.python-version` fija Python 3.14. Si no lo tienes, `uv sync` lo descarga.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 04 - Migraciones con Alembic](../1-teoria/04-migraciones-con-alembic.md) | [Lab 01 - Repositorio con PostgreSQL y Testcontainers](lab-01-repositorio-con-postgresql/README.md) → |

[↑ Semana 23](../README.md)
