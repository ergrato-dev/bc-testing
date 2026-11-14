import pytest
from sqlalchemy import create_engine
from sqlalchemy.orm import Session
from testcontainers.community.postgres import PostgresContainer

from models import Base

POSTGRES_IMAGE = "postgres:18.7-alpine"


@pytest.fixture(scope="session")
def postgres_url():
    with PostgresContainer(POSTGRES_IMAGE, driver="psycopg") as postgres:
        yield postgres.get_connection_url()


@pytest.fixture(scope="session")
def engine(postgres_url):
    engine = create_engine(postgres_url)
    Base.metadata.create_all(engine)
    yield engine
    engine.dispose()


# PASO 1: fixture sin aislamiento. Bórrala en el PASO 2.
@pytest.fixture
def session(engine):
    with Session(engine) as session:
        yield session


# ============================================
# PASO 2: session con transacción externa y savepoint
# ============================================
# Descomenta las siguientes líneas:
# @pytest.fixture
# def session(engine):
#     with engine.connect() as connection:
#         transaction = connection.begin()
#         session = Session(bind=connection, join_transaction_mode="create_savepoint")
#         yield session
#         session.close()
#         transaction.rollback()
