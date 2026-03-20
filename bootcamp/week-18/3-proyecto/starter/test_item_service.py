# ============================================
# TEST SUITE: ItemService (Python)
# ============================================

# NOTA PARA EL APRENDIZ:
# Adapta esta suite a tu dominio asignado.
# Ejemplos genericos:
# - Museo: ExhibitService
# - Planetario: SessionService
# - Acuario: SpeciesService

# TODO: importar pytest y herramientas de mocking necesarias

# TODO: crear fixture del servicio con dependencia notifier mockeada

# TODO: test happy path de activacion
# - validar estado devuelto
# - validar que notifier.send se llama con argumentos correctos

# TODO: test de item bloqueado
# - simular condicion de bloqueo
# - validar PermissionError
# - validar que notifier.send NO se llama

# TODO: agregar al menos 2 casos extra con side_effect
# - ejemplo: fallo de infraestructura de notificacion
# - ejemplo: timeout transitorio

# TODO: revisar que el patch se aplique en el namespace correcto

# TODO: documentar brevemente la estrategia de mocking usada
