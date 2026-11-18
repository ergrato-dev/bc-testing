"""Resume el CSV de historia de Locust: una fila por escalón de usuarios.

Uso: uv run python resumen.py results/stress_stats_history.csv
"""

import csv
import statistics
import sys

steps: dict[int, list[dict]] = {}
for row in csv.DictReader(open(sys.argv[1])):
    if row["Name"] == "Aggregated" and row["User Count"] != "0":
        steps.setdefault(int(row["User Count"]), []).append(row)

print("usuarios  peticiones/s  errores/s  p50 ms  p95 ms")
for users, rows in sorted(steps.items()):
    rows = rows[3:]  # los primeros segundos de cada escalón son de arranque
    if not rows:
        continue  # un número de usuarios de paso, mientras arrancan los del escalón
    rps = statistics.median(float(r["Requests/s"]) for r in rows)
    fps = statistics.median(float(r["Failures/s"]) for r in rows)
    print(f"{users:8}  {rps:12.1f}  {fps:9.1f}  {rows[-1]['50%']:>6}  {rows[-1]['95%']:>6}")
