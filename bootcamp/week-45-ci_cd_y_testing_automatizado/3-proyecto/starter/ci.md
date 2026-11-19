# CI de <tu dominio>

## 1. Workflows

TODO: una fila por workflow: archivo, qué rutas lo disparan, qué ejecuta, dónde vive su umbral y qué artifacts sube.

| Workflow | Archivo | Se ejecuta si cambia | Qué ejecuta | Umbral (dónde vive) | Artifacts |
|---|---|---|---|---|---|

## 2. Runs en verde

TODO: el enlace al run en verde de cada uno de los cinco workflows, con la duración de cada job y la cobertura.

## 3. Un PR que el umbral para

TODO: el enlace al run en rojo del PR que baja la cobertura, las líneas del log que lo explican y por qué bajó.

## 4. Hooks de pre-commit

TODO: qué hooks hay, qué encontró la primera pasada de `pre-commit run --all-files` y qué cambiaste.

## 5. Decisiones

TODO: por qué un workflow por lenguaje, matriz o no, cómo arranca PostgreSQL cada suite, los `timeout-minutes` y cualquier sorpresa de los runs.

## 6. Checks obligatorios recomendados

TODO: qué checks harías obligatorios en `main` y cómo encaja con los filtros de `paths`.
