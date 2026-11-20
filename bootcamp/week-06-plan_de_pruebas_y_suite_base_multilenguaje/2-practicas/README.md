# Prácticas - Semana 06

Dos labs de planificación (2.5 h en total) que preparan el proyecto integrador, sobre un mismo proyecto, `~/labs-semana-06`, que crea el lab 01. Es la validación del registro del planetario de la [semana 2](../../week-02-casos_de_prueba_y_tecnicas_de_diseno/2-practicas/lab-02-particion-y-valores-limite/README.md), escrita en JavaScript, Python y Java, con tres suites en verde que no prueban lo mismo. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Plan de pruebas por riesgo](lab-01-plan-de-pruebas-por-riesgo/README.md) | Alcance, riesgos con impacto y probabilidad, inventario de tres suites, matriz con ✅ y —, criterios de salida comprobables y huecos | 75 min |
| [Lab 02 - Un comando para tres suites](lab-02-un-comando-para-tres-suites/README.md) | `verify.sh` paso a paso: el código de salida que miente, `set -euo pipefail`, `awk`, `grep` y `comm` para comparar la matriz con los tests, y un caso con veredictos distintos | 75 min |

## Antes de empezar

```bash
node -v        # v22 o superior
pnpm -v        # 12.6.0 (lo fija packageManager)
uv --version
java -version  # openjdk version "21..." o superior
mvn -v         # Apache Maven 3.9 o superior
```

Son las herramientas de las semanas [3](../../week-03-primeros_tests_automatizados_con_javascript/1-teoria/01-setup-jest.md), [4](../../week-04-primeros_tests_automatizados_con_python/1-teoria/01-setup-pytest.md) y [5](../../week-05-primeros_tests_automatizados_con_java/1-teoria/01-setup-junit.md). Cada lab termina con una comprobación y una lista de repaso.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 01 - Plan de pruebas y trazabilidad en tres lenguajes](../1-teoria/01-plan-de-pruebas-y-trazabilidad.md) | [Lab 01 - Plan de pruebas por riesgo](lab-01-plan-de-pruebas-por-riesgo/README.md) → |

[↑ Semana 06](../README.md)
