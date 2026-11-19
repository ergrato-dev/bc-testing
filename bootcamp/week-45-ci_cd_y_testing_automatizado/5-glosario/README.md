# Glosario - Semana 45

| Término | Definición |
|---|---|
| Integración continua (CI) | Práctica de compilar y probar automáticamente cada cambio que se sube, en una máquina limpia. |
| Entrega continua (*continuous delivery*) | Además de la CI, cada cambio que pasa queda empaquetado y listo para desplegar con un botón. |
| Despliegue continuo (*continuous deployment*) | Cada cambio que pasa se despliega solo, sin intervención. |
| Pipeline | La secuencia de pasos automáticos que recorre un cambio: compilar, probar, analizar, desplegar. |
| GitHub Actions | El sistema de CI/CD de GitHub. Ejecuta workflows en máquinas de GitHub o propias. |
| Workflow | Archivo YAML en `.github/workflows/` con eventos, jobs y steps. |
| Evento | Lo que dispara un workflow: `push`, `pull_request`, `workflow_dispatch`, un horario... |
| Run | Una ejecución de un workflow, con su id, sus jobs y sus logs. |
| Job | Grupo de steps que corre en una máquina propia. Los jobs de un workflow corren en paralelo salvo con `needs`. |
| Step | Un paso de un job: una action (`uses`) o un comando (`run`). El primero que falla para el job. |
| Runner | La máquina que ejecuta un job, como `ubuntu-24.04`. Empieza vacía en cada job. |
| Action | Código reutilizable que hace una tarea en un step, publicado en un repositorio (`actions/checkout@v7`). |
| Expresión | `${{ ... }}`: un valor que GitHub calcula antes de ejecutar el step, como `${{ matrix.python-version }}`. |
| Matriz | `strategy.matrix`: crea un job por cada combinación de valores. |
| Caché | Carpetas que un job guarda con una clave para restaurarlas en otro job. Se guarda al final de un job verde. |
| Artifact | Archivos que un job sube a GitHub para descargarlos después, como un informe de cobertura. |
| Contenedor de servicio | `services` de un job: un contenedor (PostgreSQL) que GitHub arranca antes de los steps. |
| Health check | Comando que dice si un contenedor está listo (`pg_isready`). GitHub espera a que pase. |
| `concurrency` | Grupo de runs que no corren a la vez; con `cancel-in-progress`, el nuevo cancela al viejo. |
| `paths` | Filtro de un evento: el workflow solo corre si el cambio toca esas rutas. |
| Check | El resultado de un job en un pull request. Solo bloquea el merge si una regla de la rama lo exige. |
| Regla de rama (*ruleset*) | Configuración de una rama que exige, por ejemplo, que ciertos checks pasen antes del merge. |
| Secreto | Valor guardado en GitHub (`secrets.NOMBRE`) que el log muestra como `***`. |
| Quality gate | Lista de condiciones (cobertura, bugs, duplicación) que el código tiene que cumplir. |
| SonarQube | Herramienta que analiza el código, guarda sus medidas y evalúa una quality gate. |
| Código nuevo (*new code*) | En SonarQube, lo que cambió desde una referencia. Sonar way solo pone condiciones sobre él. |
| Hook de git | Programa que git ejecuta en un momento concreto. El de `pre-commit` puede impedir un commit. |
| Husky | Herramienta de JavaScript que instala hooks de git desde `package.json`. |
| pre-commit | Herramienta que instala y ejecuta hooks de cualquier lenguaje desde `.pre-commit-config.yaml`. |
| actionlint | Analizador de workflows de GitHub Actions: sintaxis, expresiones y nombres. |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | — |

[↑ Semana 45](../README.md)
