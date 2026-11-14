import pytest
from sqlalchemy import create_engine
from sqlalchemy.orm import Session
from testcontainers.community.postgres import PostgresContainer

from data.models import Base

POSTGRES_IMAGE = "postgres:18.7-alpine"


@pytest.fixture(scope="session")
def postgres_url():
    with PostgresContainer(POSTGRES_IMAGE, driver="psycopg") as postgres:
        yield postgres.get_connection_url()


# TODO (paso 3): engine con scope="session", que crea las tablas una vez.
# TODO (paso 3): session con transacción externa y
#                join_transaction_mode="create_savepoint".
@pytest.fixture
def session(postgres_url):
    engine = create_engine(postgres_url)
    Base.metadata.create_all(engine)
    with Session(engine) as session:
        yield session
    Base.metadata.drop_all(engine)
    engine.dispose()
