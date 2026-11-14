import pytest
from sqlalchemy import create_engine
from sqlalchemy.orm import Session
from testcontainers.community.postgres import PostgresContainer

from models import Base

POSTGRES_IMAGE = "postgres:18.7-alpine"


# ============================================
# PASO 1: contenedor de PostgreSQL
# ============================================
# Descomenta las siguientes líneas:
# @pytest.fixture(scope="session")
# def postgres_url():
#     with PostgresContainer(POSTGRES_IMAGE, driver="psycopg") as postgres:
#         yield postgres.get_connection_url()
#
#
# @pytest.fixture(scope="session", params=["sqlite", "postgresql"])
# def engine(request):
#     if request.param == "sqlite":
#         url = "sqlite+pysqlite:///:memory:"
#         connect_args = {"autocommit": False}
#     else:
#         url = request.getfixturevalue("postgres_url")
#         connect_args = {}
#     engine = create_engine(url, connect_args=connect_args)
#     Base.metadata.create_all(engine)
#     yield engine
#     engine.dispose()


# ============================================
# PASO 2: engine parametrizado
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
