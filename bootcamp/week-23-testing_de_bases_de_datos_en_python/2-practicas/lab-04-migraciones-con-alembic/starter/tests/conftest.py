import pytest
from testcontainers.community.postgres import PostgresContainer

POSTGRES_IMAGE = "postgres:18.7-alpine"


@pytest.fixture(scope="session")
def postgres_url():
    with PostgresContainer(POSTGRES_IMAGE, driver="psycopg") as postgres:
        yield postgres.get_connection_url()
