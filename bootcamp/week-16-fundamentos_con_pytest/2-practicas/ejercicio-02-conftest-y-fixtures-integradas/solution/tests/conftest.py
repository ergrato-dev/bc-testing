import pytest

# ============================================
# PASO 1: Fixture compartida en conftest.py
# ============================================
@pytest.fixture
def sample_exhibits():
    """Tres piezas del Museo listas para exportar o imprimir."""
    return [
        {"title": "Astrolabio andalusí", "room": "Sala 1"},
        {"title": "Meteorito de Sena", "room": "Sala 2"},
        {"title": "Globo celeste", "room": "Sala 2"},
    ]


# ============================================
# PASO 2: Fixture autouse con criterio
# ============================================
@pytest.fixture(autouse=True)
def clean_museum_env(monkeypatch):
    """Elimina MUSEUM_NAME para que ningún test dependa del entorno de la máquina."""
    monkeypatch.delenv("MUSEUM_NAME", raising=False)
