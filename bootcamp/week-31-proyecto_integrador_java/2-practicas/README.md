# Prácticas - Semana 31

Dos labs de planificación (2.5 h en total) que preparan el proyecto integrador. Cada uno trae un `starter/` con una API de biblioteca en Spring Boot y sus tests; los cambios de cada PASO están en el README del lab. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Test e IT con Surefire y Failsafe](lab-01-test-e-it-con-surefire-y-failsafe/README.md) | Los `*IT` que nadie ejecuta, Failsafe, `-DskipITs`, `-Dit.test`, `-Dtest`, los informes de cada plugin y un IT que falla | 75 min |
| [Lab 02 - JaCoCo, umbral y reporte](lab-02-jacoco-umbral-y-reporte/README.md) | `prepare-agent` y `report`, el `argLine` que pisa a JaCoCo, `@{argLine}`, `check` con el 85 %, `clean` y el reporte de testing | 75 min |

Los dos labs necesitan **Docker** en marcha: `BookRepositoryIT` arranca PostgreSQL con Testcontainers.

## Antes de empezar

```bash
java -version   # openjdk version "21..." o superior
mvn -v          # Apache Maven 3.9 o superior
docker version --format '{{.Server.Version}}'
```

Cada lab es un proyecto Maven independiente que hereda de `spring-boot-starter-parent` 4.1.1:

```bash
cd lab-NN-.../starter
mvn verify
```

En estos labs, los PASOS cambian el `pom.xml`: el README de cada lab trae el XML que pegas o la línea que cambias. El starter marca con un comentario `<!-- PASO 1: ... -->` dónde va el primer bloque.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 01 - La suite Java completa](../1-teoria/01-la-suite-java-completa.md) | [Lab 01 - Test e IT con Surefire y Failsafe](lab-01-test-e-it-con-surefire-y-failsafe/README.md) → |

[↑ Semana 31](../README.md)
