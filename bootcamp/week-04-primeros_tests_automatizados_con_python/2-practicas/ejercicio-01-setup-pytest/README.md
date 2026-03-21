# Ejercicio 01 — Setup y Primera Ejecucion con pytest

> **Semana 04 · Practicas · Ejercicio 01** | Duracion estimada: 1.5 h

---

## Objetivo

Configurar pytest por primera vez y ejecutar el ciclo minimo:

1. Test en rojo
2. Correccion minima
3. Test en verde

---

## Instrucciones

### Paso 1 — Revisar estructura

Abre carpeta `starter/`:

- `src/math_utils.py`
- `tests/test_math_utils.py`
- `requirements.txt`

### Paso 2 — Instalar dependencias

```bash
python -m venv .venv
source .venv/bin/activate
python -m pip install -r requirements.txt
```

### Paso 3 — Ejecutar tests

```bash
pytest -v
```

Uno de los tests esta preparado para fallar inicialmente.

### Paso 4 — Descomentar secciones guiadas

Abre `starter/tests/test_math_utils.py` y descomenta los pasos 1, 2 y 3.

### Paso 5 — Corregir implementacion

Abre `starter/src/math_utils.py` y corrige el bug intencional.

### Paso 6 — Verificar en verde

```bash
pytest -v
```

---

## Resultado esperado

- Entorno pytest funcional
- Comprension de error de assertion
- Correccion minima para pasar tests
