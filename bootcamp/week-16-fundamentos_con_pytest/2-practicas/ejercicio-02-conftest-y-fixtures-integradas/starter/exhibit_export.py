"""Exportación del catálogo de piezas del Museo a archivos JSON."""

import json
from datetime import date
from pathlib import Path


def today() -> date:
    return date.today()


def export_exhibits(exhibits: list[dict], directory: Path) -> Path:
    path = directory / f"exhibits-{today().isoformat()}.json"
    path.write_text(json.dumps(exhibits, ensure_ascii=False), encoding="utf-8")
    return path


def load_exhibits(path: Path) -> list[dict]:
    data = json.loads(path.read_text(encoding="utf-8"))
    if not isinstance(data, list):
        raise ValueError("exhibit file must contain a list")
    return data
