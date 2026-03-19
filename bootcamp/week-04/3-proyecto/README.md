# Proyecto Semana 04 — Suite Inicial de Tests Unitarios en Python

> **Entregable obligatorio** | Etapa 0 · Semana 04

---

## 🎯 Objetivo

Construir una suite inicial de tests unitarios con `pytest` para funciones puras de tu dominio asignado.

Debes aplicar:

- Convenciones de pytest (`test_*.py`, `def test_*`)
- Patron AAA
- Assertions claras y manejo de excepciones

---

## Reglas del proyecto

1. Definir al menos 3 funciones de negocio del dominio
2. Escribir minimo 8 tests unitarios
3. Cubrir al menos:
   - 3 happy path
   - 3 casos invalidos o error
   - 2 edge cases
4. Mantener nombres descriptivos en snake_case
5. Ejecutar con `pytest -v`

---

## Alcance recomendado

Usar funciones puras, por ejemplo:

- validaciones
- calculos
- transformaciones de datos

No usar:

- API externas
- base de datos real
- IO de archivos compleja

---

## Guia de trabajo (2 horas)

- **20 min**: definir funciones y reglas
- **25 min**: tests happy path
- **35 min**: tests de validacion y errores
- **25 min**: edge cases
- **15 min**: limpieza y ejecucion final

---

## Entregable

Completar `starter/item_service_test.py` con tu suite adaptada al dominio asignado.

> `solution/` del proyecto no se publica en el repositorio.
