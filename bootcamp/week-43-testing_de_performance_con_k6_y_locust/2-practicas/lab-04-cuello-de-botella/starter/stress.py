"""Lab 04 - Stress test: escalones de usuarios hasta romper la API.

Descomenta el bloque PASO cuando el README lo pida.
"""

import random

from locust import HttpUser, LoadTestShape, between, task

TERMS = ["faro gris", "isla azul", "tren verde", "carta breve", "río final"]


class Reader(HttpUser):
    """El lector del lab 03: consulta libros y busca por título."""

    host = "http://localhost:18043"
    wait_time = between(0.5, 1.5)

    @task
    def book(self):
        isbn = f"978{random.randint(1, 100_000):010d}"
        self.client.get(f"/api/books/{isbn}", name="/api/books/{isbn}")

    @task(3)
    def search(self):
        self.client.get("/api/books", params={"q": random.choice(TERMS)}, name="/api/books?q=")


# PASO 1: la forma de la carga
# class StepLoad(LoadTestShape):
#     """Cada 20 s, 50 usuarios más, hasta 300. Después, termina."""
#
#     step_users = 50
#     step_seconds = 20
#     max_users = 300
#
#     def tick(self):
#         users = (int(self.get_run_time() // self.step_seconds) + 1) * self.step_users
#         if users > self.max_users:
#             return None
#         return users, self.step_users
