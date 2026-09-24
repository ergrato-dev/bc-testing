# Ejercicio 02 - CI + SonarQube Minimo

## Objetivo

Configurar un pipeline minimo con GitHub Actions (Node 22 + pnpm) para ejecutar tests, generar coverage e integrar el escaneo de SonarQube con un quality gate que bloquee el pipeline.

## Tiempo estimado

90 minutos.

## Requisito previo

Los tests de `starter/src/sum.test.js` ya estan escritos: este ejercicio se centra en la automatizacion. Comprueba que pasan en local:

```bash
cd starter
pnpm install
pnpm test:coverage
```

Para que el workflow se ejecute de verdad, copia el contenido de `starter/` a la raiz de un repositorio tuyo en GitHub (la carpeta `.github/workflows/` debe quedar en la raiz).

## Paso a paso

### Paso 1: Activar checkout, pnpm y Node 22

Abre `starter/.github/workflows/js-quality.yml` y descomenta PASO 1. `pnpm/action-setup` lee la version de pnpm del campo `packageManager` de `package.json`, y `actions/setup-node` instala Node 22.

### Paso 2: Ejecutar tests con coverage

Descomenta PASO 2 para correr `pnpm test:coverage`, que genera `coverage/lcov.info`.

### Paso 3: Integrar SonarQube Scan

Descomenta PASO 3 y crea el secreto `SONAR_TOKEN` en tu repositorio (Settings > Secrets and variables > Actions). Si usas SonarQube autohospedado (Community Edition), descomenta tambien `SONAR_HOST_URL` y crea ese secreto.

### Paso 4: Configurar `sonar-project.properties`

Descomenta la configuracion en `starter/sonar-project.properties` y reemplaza `sonar.organization` y `sonar.projectKey` por los de tu proyecto en SonarQube Cloud. Fíjate en `sonar.exclusions=**/*.test.js`: sin esa linea los tests quedan dentro de `sonar.sources` y de `sonar.tests` a la vez, y el analisis falla con `File can't be indexed twice`.

## Cierre

Compara con la carpeta `solution/` y verifica en la pestana Actions que el job falla si el quality gate no se cumple (`sonar.qualitygate.wait=true`).
