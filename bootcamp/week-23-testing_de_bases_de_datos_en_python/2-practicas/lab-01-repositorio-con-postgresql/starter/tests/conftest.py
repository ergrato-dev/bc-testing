import pytest
from sqlalchemy import create_engine
from sqlalchemy.orm import Session
from testcontainers.community.postgres import PostgresContainer

from models import Base

POSTGRES_IMAGE = "postgres:18.7-alpine"


# ============================================
# PASO 1: postgres_url
# ============================================
# Descomenta las siguientes líneas:
# @pytest.fixture(scope="session")
# def postgres_url():
#     with PostgresContainer(POSTGRES_IMAGE, driver="psycopg") as postgres:
#         yield postgres.get_connection_url()


# ============================================
# PASO 2: session
# ============================================
# Descomenta las siguientes líneas:
# @pytest.fixture
# def session(postgres_url):
#     engine = create_engine(postgres_url)
#     Base.metadata.create_all(engine)
#     with Session(engine) as session:
#         yield session
#     Base.metadata.drop_all(engine)
#     engine.dispose()
