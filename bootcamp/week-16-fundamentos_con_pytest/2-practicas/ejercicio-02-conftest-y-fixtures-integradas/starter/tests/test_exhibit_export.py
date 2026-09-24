import json
from datetime import date

import pytest

import exhibit_export
from exhibit_export import export_exhibits, load_exhibits

# ============================================
# PASO 3: tmp_path + monkeypatch.setattr
# ============================================
# def test_export_exhibits_writes_dated_file_when_directory_exists(
#     sample_exhibits, tmp_path, monkeypatch
# ):
#     # Arrange
#     monkeypatch.setattr(exhibit_export, "today", lambda: date(2026, 9, 24))
#
#     # Act
#     path = export_exhibits(sample_exhibits, tmp_path)
#
#     # Assert
#     assert path == tmp_path / "exhibits-2026-09-24.json"
#     assert json.loads(path.read_text(encoding="utf-8")) == sample_exhibits


# ============================================
# PASO 4: Ida y vuelta con archivos temporales
# ============================================
# def test_load_exhibits_returns_same_exhibits_when_file_was_exported(
#     sample_exhibits, tmp_path
# ):
#     # Arrange
#     path = export_exhibits(sample_exhibits, tmp_path)
#
#     # Act
#     loaded = load_exhibits(path)
#
#     # Assert
#     assert loaded == sample_exhibits


# ============================================
# PASO 5: Ruta de error con un archivo preparado en tmp_path
# ============================================
# def test_load_exhibits_raises_error_when_file_is_not_a_list(tmp_path):
#     # Arrange
#     path = tmp_path / "broken.json"
#     path.write_text('{"title": "Astrolabio andalusí"}', encoding="utf-8")
#
#     # Act + Assert
#     with pytest.raises(ValueError, match="must contain a list"):
#         load_exhibits(path)
