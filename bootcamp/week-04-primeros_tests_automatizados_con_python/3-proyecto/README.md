# Proyecto Semana 04 — Suite Inicial de Tests Unitarios en Python

> **Entregable obligatorio** | Etapa 0 · Semana 04

---

## 🎯 Objetivo

Construir una suite inicial de tests unitarios con `pytest` para las mismas funciones puras de tu dominio que testeaste en JavaScript en la semana 03, y comparar cómo se expresa cada intención en ambos lenguajes.

Debes aplicar:

- Convenciones de pytest (`test_*.py`, `def test_*`)
- Patrón AAA
- Assertions claras y manejo de excepciones

---

## Reglas del proyecto

1. Definir al menos 3 funciones de negocio del dominio
2. Escribir mínimo 8 tests unitarios
3. Cubrir al menos:
   - 3 happy path
   - 3 casos inválidos o error
   - 2 edge cases
4. Nombrar tests con el patrón `test_[contexto]_[resultado]_when_[condicion]`
5. Ejecutar con `uv run pytest -v` desde `starter/` (antes, `uv sync`)

---

## Alcance recomendado

Usar funciones puras, por ejemplo:

- validaciones
- cálculos
- transformaciones de datos

No usar:

- API externas
- base de datos real
- IO de archivos compleja

---

## Guía de trabajo (2 horas)

- **20 min**: definir funciones y reglas
- **25 min**: tests happy path
- **35 min**: tests de validación y errores
- **25 min**: edge cases
- **15 min**: limpieza, ejecución final y tabla comparativa

---

## Entregable

1. `starter/item_service.py` con las funciones de tu dominio.
2. `starter/test_item_service.py` completo, con tu suite adaptada al dominio asignado.
3. Una tabla comparativa breve (en tu README o al final de la entrega) con 3 tests equivalentes JS ↔ Python:

| Intención | Jest (semana 03) | pytest (semana 04) |
|---|---|---|
| Igualdad | `expect(result).toBe(80)` | `assert result == 80` |
| Excepción | `expect(() => fn()).toThrow("...")` | `with pytest.raises(ValueError, match="..."):` |
| ... | ... | ... |

> `solution/` del proyecto no se publica en el repositorio.
