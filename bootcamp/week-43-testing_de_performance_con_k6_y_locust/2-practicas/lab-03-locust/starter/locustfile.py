"""Lab 03 - Locust: los usuarios de la biblioteca, en Python.

Descomenta cada bloque PASO cuando el README lo pida.
"""

import random

from locust import HttpUser, between, events, task

# Cinco títulos que existen, para que no todas las búsquedas sean la misma.
TERMS = ["faro gris", "isla azul", "tren verde", "carta breve", "río final"]


def random_isbn() -> str:
    return f"978{random.randint(1, 100_000):010d}"


class Reader(HttpUser):
    """Un lector: consulta libros y busca por título."""

    host = "http://localhost:18043"
    wait_time = between(1, 2)
    weight = 4

    # PASO 1: consultar un libro
    # @task
    # def book(self):
    #     self.client.get(f"/api/books/{random_isbn()}", name="/api/books/{isbn}")

    # PASO 2: buscar, tres veces más a menudo, y validar la respuesta
    # @task(3)
    # def search(self):
    #     with self.client.get(
    #         "/api/books",
    #         params={"q": random.choice(TERMS)},
    #         name="/api/books?q=",
    #         catch_response=True,
    #     ) as res:
    #         if res.status_code == 200 and len(res.json()) != 20:
    #             res.failure(f"expected 20 books, got {len(res.json())}")


# PASO 3: un socio que presta y devuelve
# class Member(HttpUser):
#     """Un socio: presta un libro y lo devuelve."""
#
#     host = "http://localhost:18043"
#     wait_time = between(1, 2)
#     weight = 1
#
#     def on_start(self):
#         self.member = f"member-{random.randint(1, 1_000_000)}"
#
#     @task
#     def lend_and_return(self):
#         isbn = random_isbn()
#         with self.client.post(
#             "/api/loans",
#             json={"isbn": isbn, "member": self.member},
#             catch_response=True,
#         ) as res:
#             if res.status_code == 409:
#                 res.success()  # otro socio lo tiene prestado: es una respuesta correcta
#                 return
#         if res.status_code == 201:
#             self.client.delete(f"/api/loans/{isbn}", name="/api/loans/{isbn}")


# PASO 4: los criterios de aceptación deciden el código de salida
# @events.quitting.add_listener
# def check_thresholds(environment, **kwargs):
#     total = environment.stats.total
#     p95 = total.get_response_time_percentile(0.95)
#     if total.fail_ratio > 0.01:
#         print(f"FALLO: {total.fail_ratio:.2%} de errores (máximo 1 %)")
#         environment.process_exit_code = 1
#     elif p95 > 300:
#         print(f"FALLO: p95 de {p95} ms (máximo 300 ms)")
#         environment.process_exit_code = 1
