# ============================================
# TEST SUITE: ItemService (Python)
# ============================================

# NOTA PARA EL APRENDIZ:
# Adapta esta suite a tu dominio asignado.
# Ejemplos genéricos:
# - Museo: ExhibitService
# - Planetario: SessionService
# - Acuario: SpeciesService
#
# Dependencias de ItemService (ver gateways.py):
# - fetch_item_status: API HTTP importada a nivel de módulo en item_service.py.
#   No se inyecta: hay que parchearla con patch/mocker.patch. Piensa qué target
#   usar (¿dónde busca Python ese nombre cuando se ejecuta activate?).
# - repository (BD) y notifier: se inyectan en el constructor. Basta con dobles
#   simples, preferiblemente con spec/autospec de las clases reales.
# Si un test ejecuta una dependencia real verás un ConnectionError: señal de que
# el doble no está donde crees.

# TODO: importar pytest y las herramientas de mocking que necesites

# TODO: fixture(s) para los colaboradores inyectados y el servicio

# TODO: happy path de activación
# - estado devuelto
# - interacción con el repositorio y el notifier

# TODO: item bloqueado según la API HTTP
# - excepción esperada
# - qué colaboradores NO deben haberse llamado

# TODO: al menos 3 casos de error con side_effect
# - fallo o timeout de la API HTTP
# - fallo del repositorio (¿se notifica igualmente?)
# - fallo del notifier (lee activate: ¿qué debería devolver?)

# TODO: completar hasta al menos 8 casos efectivos

# TODO: documentar brevemente la estrategia de mocking usada
# (qué dependencias se reemplazan, con qué tipo de doble y por qué)
