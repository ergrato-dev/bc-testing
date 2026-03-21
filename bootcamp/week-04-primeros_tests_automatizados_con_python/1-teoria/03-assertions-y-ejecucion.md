# Assertions, Ejecución y Lectura de Reportes en pytest

> **Semana 04 — Teoría 03** | Lenguaje: Python

---

## Assertion nativo en Python

En pytest, la verificación principal es `assert`.

```python
assert result == expected
```

Ventaja: sintaxis simple y mensajes de error enriquecidos automáticamente por pytest.

---

## Assertions más comunes

```python
assert total == 10
assert user is not None
assert "error" in message
assert len(items) == 3
assert is_valid is True
assert is_valid is False
```

Evita asserts ambiguos como:

```python
assert value
```

si puedes expresar mejor la intención con comparación explícita.

---

## Ejecutar y filtrar tests

```bash
pytest
pytest -v
pytest -k "discount"
pytest tests/test_price.py
```

- `-v` ayuda a leer nombres completos
- `-k` permite foco en subset
- Ejecutar archivo puntual acelera depuración

---

## Failure vs Error

### Failure

El test corre, pero la assertion falla.

```text
E       assert 79 == 80
```

### Error

El test no alcanza assertion por excepción inesperada.

```text
E       TypeError: unsupported operand type(s)
```

Diagnóstico rápido:

- `assert ...` falló -> revisar expectativa o lógica
- excepción de runtime -> revisar setup, tipo de datos, imports

---

## Ciclo mínimo Red-Green-Refactor

1. **Red**: crear test que falla
2. **Green**: implementar lo mínimo para pasar
3. **Refactor**: mejorar diseño sin romper tests

```python
# Red: test espera True para edad 18
# Green:
def is_adult(age: int) -> bool:
    return age >= 18
```

---

## Checklist de calidad mínima

- [ ] Nombre del test comunica intención
- [ ] AAA visible
- [ ] Assertion específica
- [ ] Sin dependencias externas
- [ ] Resultado reproducible

---

## Cierre

Con esta base ya puedes traducir la disciplina de testing de JavaScript a Python, manteniendo la misma calidad de diseño y lectura.

← [Estructura de test en pytest](./02-estructura-test-pytest.md) | [Volver al README](../README.md)
