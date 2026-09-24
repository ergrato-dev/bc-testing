# Proyecto Semanal - Hardening de Suite con Coverage y Calidad

## Objetivo

Mejorar una suite de pruebas para que detecte regresiones reales en tu dominio asignado, usando cobertura como guía y criterio de riesgo.

## Contexto

Este proyecto es el entregable obligatorio de la semana. Debes adaptar el starter a tu dominio asignado por el instructor.

## Requisitos

1. Definir un objetivo de coverage por módulo crítico y hacerlo obligatorio en `starter/jest.config.js` con `collectCoverageFrom` y `coverageThreshold`.
2. Agregar tests para al menos 3 rutas de fallo relevantes.
3. Reducir al menos 2 fuentes de fragilidad de la suite.
4. Mantener patrón AAA y nombres descriptivos.
5. Justificar que cambios subieron la confianza de calidad.

## Estructura

- `starter/`: plantilla con TODOs para implementar.
- `solution/`: referencia local del instructor (no versionada).

## Criterios mínimos

- Mínimo 8 tests en total.
- Mínimo 3 tests de errores/validaciones.
- Mínimo 1 test de borde de negocio.
- Cobertura del módulo principal >=85% con ramas relevantes cubiertas, exigida por `coverageThreshold` (`pnpm test:coverage` debe fallar si baja).

## Ejecución sugerida

```bash
pnpm install
pnpm test:coverage
```
