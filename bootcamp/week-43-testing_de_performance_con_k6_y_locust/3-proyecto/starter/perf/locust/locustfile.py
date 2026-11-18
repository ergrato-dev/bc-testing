"""Stress test de la API del dominio (semana 43).

TODO: completa el paso 5 de 3-proyecto/README.md.

perf/run.sh stress lo ejecuta con --host, --csv y --html. Los escalones se cambian sin tocar el
código: STEP_USERS, STEP_SECONDS y MAX_USERS (perf/run.sh smoke usa 2 usuarios).
"""

import os

from locust import HttpUser, LoadTestShape, between, task

PIECES = 20_000  # TODO: los elementos que crea perf/seed.sql


def code(n: int) -> str:
    """TODO: el código del elemento n, igual que en perf/seed.sql."""
    return f"AA{chr(65 + n // 1000)}-{n % 1000:03d}"


class Visitor(HttpUser):
    """TODO: el usuario de tu dominio que lee: un elemento al azar, con name y catch_response."""

    wait_time = between(0.5, 1.5)

    @task
    def piece(self):
        self.client.get(f"/api/pieces/{code(0)}")


class StepLoad(LoadTestShape):
    """Cada STEP_SECONDS, STEP_USERS usuarios más, hasta MAX_USERS. Después, termina."""

    # TODO: los escalones por defecto de tu stress test.
    step_users = int(os.getenv("STEP_USERS", "50"))
    step_seconds = int(os.getenv("STEP_SECONDS", "20"))
    max_users = int(os.getenv("MAX_USERS", "100"))

    def tick(self):
        users = (int(self.get_run_time() // self.step_seconds) + 1) * self.step_users
        if users > self.max_users:
            return None
        # Arrancan como mucho 100 por segundo: Locust avisa de que más puede dar problemas.
        return users, min(self.step_users, 100)
