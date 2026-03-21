# Ejercicio 02 - Marks y Seleccion de Suite

## Objetivo

Organizar pruebas con marks y ejecutar subconjuntos con criterios de riesgo.

## Tiempo estimado

90 minutos.

## Paso a paso

### Paso 1: Definir marks en pytest.ini

Abre `starter/pytest.ini` y descomenta PASO 1.

### Paso 2: Marcar tests criticos y de regresion

Descomenta PASO 2 y PASO 3 en `starter/test_order_health.py`.

### Paso 3: Filtrar por marks

Ejecuta:

```bash
pytest -m smoke
pytest -m "regression and not slow"
```

### Paso 4: Revisar solucion

Compara con archivos en `solution/`.
