# Ejercicio 02 — Primeros Tests Unitarios en Python

> **Semana 04 · Practicas · Ejercicio 02** | Duracion estimada: 2 h

---

## Objetivo

Escribir una suite inicial con pytest para funciones puras aplicando:

- Nombres descriptivos
- Patron AAA
- Assertions claras
- Manejo de excepciones con `pytest.raises`

---

## Instrucciones

### Paso 1 — Abre `starter/tests/test_user_utils.py`

Encontraras bloques comentados por pasos para descomentar.

### Paso 2 — Ejecuta tests

```bash
python -m venv .venv
source .venv/bin/activate
python -m pip install -r requirements.txt
pytest -v
```

### Paso 3 — Completa casos

Completa assertions faltantes y valida excepciones.

### Paso 4 — Ajusta nombres

Patron sugerido:

```text
test_[contexto]_[resultado]_when_[condicion]
```

---

## Resultado esperado

- Al menos 6 tests en verde
- Happy path + casos invalidos
- AAA visible en cada test
