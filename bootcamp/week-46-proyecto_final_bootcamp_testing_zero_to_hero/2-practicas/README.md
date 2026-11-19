# Prácticas - Semana 46

Dos labs de planificación (2.5 h en total) que preparan el proyecto final. Los dos trabajan sobre `ci-lab`, el repositorio que subiste a GitHub en la [semana 45](../../week-45-ci_cd_y_testing_automatizado/2-practicas/README.md): tres suites pequeñas que se pueden ejecutar enteras en un minuto. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Inventario del repositorio](lab-01-inventario-del-repositorio/README.md) | Listar, ejecutar y medir cada suite; encontrar dónde corre y qué umbral la para | 75 min |
| [Lab 02 - Estrategia y README](lab-02-estrategia-y-readme/README.md) | `testing-strategy.md` con riesgos, criterios y decisiones; un umbral común; el README con *badges* | 75 min |

Cada lab trae en `starter/` las plantillas que llenas. Las de `testing-strategy.md` y `README.md` son las mismas del proyecto.

## Antes de empezar

```bash
cd ~/ci-lab
git status --short          # sin cambios pendientes
gh run list --limit 3       # los últimos runs de js, python y go
docker info --format '{{.ServerVersion}}'
```

Si `ci-lab` no está completo (faltan los workflows, el `.pre-commit-config.yaml` o los tests de `describeFee`), termina antes los labs de la semana 45: el inventario tiene que medir el repositorio tal como quedó.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 01 - La estrategia de testing del repositorio](../1-teoria/01-la-estrategia-de-testing-del-repositorio.md) | [Lab 01 - Inventario del repositorio](lab-01-inventario-del-repositorio/README.md) → |

[↑ Semana 46](../README.md)
