# Proyecto Semanal - Cierre de Etapa JS con Quality Gate

## Objetivo

Consolidar una suite de testing JavaScript de nivel inicial profesional, integrando estrategia de pruebas y automatizacion minima de calidad en CI.

## Contexto

Este proyecto es el entregable obligatorio de la semana y cierre de la etapa JavaScript. Debes adaptar el starter a tu dominio asignado por el instructor.

## Requisitos

1. Implementar suite combinada con al menos 3 enfoques (unit + integration + snapshot/property).
2. Alcanzar cobertura >=85% en el modulo critico definido.
3. Configurar workflow minimo de GitHub Actions para tests y coverage.
4. Integrar configuracion minima de SonarQube.
5. Documentar una lista breve de riesgos aun no cubiertos.

## Estructura

- `starter/`: plantilla con TODOs para implementar.
- `solution/`: referencia local del instructor (no versionada).

## Criterios minimos

- Minimo 8 tests en total.
- Minimo 3 pruebas de errores/validaciones.
- Minimo 1 evidencia de contrato estable (snapshot o propiedad equivalente).
- Pipeline ejecutable con pasos de test, coverage y scan de calidad.

## Ejecucion sugerida

```bash
yarn test --coverage
```
