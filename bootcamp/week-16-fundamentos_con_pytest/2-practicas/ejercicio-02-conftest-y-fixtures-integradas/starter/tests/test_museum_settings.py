from museum_settings import DEFAULT_MUSEUM_NAME, get_museum_name, print_summary

# ============================================
# PASO 6: Variables de entorno con monkeypatch
# ============================================
# def test_get_museum_name_returns_default_when_env_var_is_missing():
#     # Arrange: la fixture autouse clean_museum_env ya borró MUSEUM_NAME
#
#     # Act
#     name = get_museum_name()
#
#     # Assert
#     assert name == DEFAULT_MUSEUM_NAME
#
#
# def test_get_museum_name_returns_env_value_when_env_var_is_set(monkeypatch):
#     # Arrange
#     monkeypatch.setenv("MUSEUM_NAME", "Museo de Ciencias")
#
#     # Act
#     name = get_museum_name()
#
#     # Assert
#     assert name == "Museo de Ciencias"


# ============================================
# PASO 7: Fixture de conftest.py en otro archivo + capsys
# ============================================
# def test_print_summary_lists_every_exhibit_when_catalog_has_items(
#     sample_exhibits, monkeypatch, capsys
# ):
#     # Arrange
#     monkeypatch.setenv("MUSEUM_NAME", "Museo de Ciencias")
#
#     # Act
#     print_summary(sample_exhibits)
#
#     # Assert
#     output = capsys.readouterr().out
#     assert output.splitlines() == [
#         "Museo de Ciencias: 3 piezas",
#         "- Astrolabio andalusí (Sala 1)",
#         "- Meteorito de Sena (Sala 2)",
#         "- Globo celeste (Sala 2)",
#     ]
