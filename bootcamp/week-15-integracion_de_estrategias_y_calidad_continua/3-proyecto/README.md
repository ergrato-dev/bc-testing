# Proyecto Semanal - Cierre de Etapa JS con Quality Gate

## Objetivo

Consolidar una suite de testing JavaScript de nivel inicial profesional, integrando estrategia de pruebas y automatizacion minima de calidad en CI.

## Contexto

Este proyecto es el entregable obligatorio de la semana y cierre de la etapa JavaScript. Debes adaptar el starter a tu dominio asignado por el instructor.

## Requisitos

1. Implementar suite combinada con al menos 3 enfoques: unit (`src/item.service.test.js`), integracion HTTP con Supertest (`src/app.test.js`) y snapshot o property.
2. Completar la capa API en `src/app.js` (Express 5) reutilizando `createItemService`: `POST /items` responde 201, 400 en validaciones y 409 en duplicados.
3. Configurar `collectCoverageFrom` y `coverageThreshold` en `jest.config.js` (>=85% en branches y lines del modulo critico).
4. Completar el workflow `.github/workflows/js-quality.yml` (Node 22, pnpm, `pnpm test:coverage`, SonarQube scan).
5. Completar `sonar-project.properties` con `sonar.organization`, exclusion de tests en `sources` y `sonar.qualitygate.wait=true`.
6. Documentar una lista breve de riesgos aun no cubiertos.

## Estructura

```text
starter/
|-- .github/workflows/js-quality.yml   # TODO: pipeline de calidad
|-- jest.config.js                     # TODO: coverage obligatorio
|-- package.json                       # express, jest y supertest ya declarados
|-- sonar-project.properties           # TODO: analisis SonarQube
`-- src/
    |-- item.service.js                # servicio (reutilizado de semana 14)
    |-- item.service.test.js           # TODO: tests unitarios
    |-- app.js                         # TODO: capa API Express
    `-- app.test.js                    # TODO: tests de integracion con Supertest
```

`solution/` es la referencia local del instructor (no versionada).

## Criterios minimos

- Minimo 8 tests en total, de los cuales al menos 3 de integracion HTTP con Supertest.
- Minimo 3 pruebas de errores/validaciones.
- Minimo 1 evidencia de contrato estable (snapshot o propiedad equivalente).
- `pnpm test:coverage` en verde con `coverageThreshold` activo.
- Pipeline ejecutable con pasos de test, coverage y scan de calidad; evidencia (captura o enlace) de `pnpm test:coverage` corriendo en CI.

## Ejecucion sugerida

```bash
pnpm install
pnpm test:coverage
```
