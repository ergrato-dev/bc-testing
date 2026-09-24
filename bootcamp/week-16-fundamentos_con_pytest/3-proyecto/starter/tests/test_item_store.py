# ============================================
# Tests de ItemStore (estado, archivos y entorno)
# ============================================
# Usa la fixture con yield de conftest.py: ningún test debe abrir ni cerrar el
# almacén a mano salvo que ese sea justamente el comportamiento que pruebas.
#
# Requisitos que deben quedar cubiertos:
# TODO: agregar un item lo deja disponible en el almacén.
# TODO: lo agregado persiste en el archivo tras cerrar el almacén y se recupera al abrir
#       uno nuevo sobre el mismo archivo (tmp_path).
# TODO: operar con el almacén cerrado produce el error esperado.
# TODO: el límite de items se respeta cuando ITEM_LIMIT está definido (monkeypatch).
