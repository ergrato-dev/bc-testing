# ============================================
# PASO 2: marcar cada test según su carpeta
# ============================================
# LAYERS = ("unit", "api", "repository")


# def pytest_collection_modifyitems(items):
#     for item in items:
#         layer = item.path.parent.name
#         if layer in LAYERS:
#             item.add_marker(layer)
