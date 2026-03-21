# Ejercicio 02 - CI + SonarQube Minimo

## Objetivo

Configurar un pipeline minimo con GitHub Actions para ejecutar tests, generar coverage e integrar escaneo SonarQube.

## Tiempo estimado

90 minutos.

## Paso a paso

### Paso 1: Activar el workflow base

Abre `starter/.github/workflows/js-quality.yml` y descomenta PASO 1.

### Paso 2: Ejecutar tests con coverage

Descomenta PASO 2 para correr `yarn test --coverage`.

### Paso 3: Integrar SonarQube Scan

Descomenta PASO 3 y configura secretos `SONAR_TOKEN` + `SONAR_HOST_URL`.

### Paso 4: Configurar `sonar-project.properties`

Descomenta la configuracion base en `starter/sonar-project.properties`.

### Paso 5: Comparar con solucion

Revisa la carpeta `solution/`.

## Comando local sugerido

```bash
yarn test --coverage
```
