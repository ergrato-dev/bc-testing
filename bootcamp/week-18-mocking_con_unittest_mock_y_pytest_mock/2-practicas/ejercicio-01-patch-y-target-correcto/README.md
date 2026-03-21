# Ejercicio 01 - Patch y Target Correcto

## Objetivo

Aprender a parchear donde el simbolo es usado para aislar dependencias sin falsos positivos.

## Tiempo estimado

90 minutos.

## Paso a paso

### Paso 1: Revisar modulo bajo prueba

Abre `starter/order_service.py` y observa como se importa `PaymentGateway`.

### Paso 2: Aplicar patch correcto

Abre `starter/test_order_service.py` y descomenta PASO 1.

### Paso 3: Probar escenario de error

Descomenta PASO 2 para simular timeout con `side_effect`.

### Paso 4: Revisar solucion

Compara con `solution/test_order_service.py`.

## Comando sugerido

```bash
pytest -q
```
