# Proyecto Semanal - Hardening de Suite con Coverage y Calidad

## Objetivo

Mejorar una suite de pruebas para que detecte regresiones reales en tu dominio asignado, usando cobertura como guia y criterio de riesgo.

## Contexto

Este proyecto es el entregable obligatorio de la semana. Debes adaptar el starter a tu dominio asignado por el instructor.

## Requisitos

1. Definir un objetivo de coverage por modulo critico.
2. Agregar tests para al menos 3 rutas de fallo relevantes.
3. Reducir al menos 2 fuentes de fragilidad de la suite.
4. Mantener patron AAA y nombres descriptivos.
5. Justificar que cambios subieron la confianza de calidad.

## Estructura

- `starter/`: plantilla con TODOs para implementar.
- `solution/`: referencia local del instructor (no versionada).

## Criterios minimos

- Minimo 8 tests en total.
- Minimo 3 tests de errores/validaciones.
- Minimo 1 test de borde de negocio.
- Cobertura del modulo principal >=85% con ramas relevantes cubiertas.

## Ejecucion sugerida

```bash
yarn test --coverage
```
