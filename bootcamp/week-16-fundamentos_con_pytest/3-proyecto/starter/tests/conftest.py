# ============================================
# conftest.py: fixtures compartidas del proyecto
# ============================================
# Adapta los nombres a tu dominio (Museo: exhibit, Planetario: session, Acuario: species).
#
# TODO 1: fixture con datos base válidos (payload) reutilizable desde los dos archivos de test.
#
# TODO 2: fixture con yield que cree un ItemStore en un archivo dentro de tmp_path,
#         lo abra antes del test y lo cierre después (teardown).
#
# TODO 3 (opcional): fixture autouse SOLO si la justificas; por ejemplo, aislar la
#         variable ITEM_LIMIT del entorno de la máquina. Explica el motivo en su docstring.
