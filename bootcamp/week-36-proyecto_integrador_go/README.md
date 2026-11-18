# Semana 36 - Proyecto Integrador: Etapa 4 (Go)

> **Etapa 4 - Testing con Go** | Semana 36 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Ordenar una suite Go en fases (análisis, unitarios e integración) según lo que arranca cada test.
2. Ejecutar la suite entera con un script, `verify.sh`, que se para en el primer fallo, también detrás de un `| tee`.
3. Hacer fallar ese script cuando la cobertura de todo el módulo baja del 85 %.
4. Escribir un reporte de testing con datos reales: fases, cobertura por paquete, a dónde va el tiempo, riesgos y próximos pasos.
5. Presentar tu suite y defender qué prueba cada fase y qué no prueba ninguna.

---

## Requisitos previos

- Semanas 32 a 35 entregadas en `go/` de tu repositorio `qa-<nombre>-<apellido>`.
- Go 1.26.5. Comprueba con `go version`.
- **Docker** en marcha. Comprueba con `docker version --format '{{.Server.Version}}'`.

Esta semana no tiene teoría de testing nueva: junta lo que ya sabes. Lo único nuevo es el script que lo ejecuta todo, el equivalente Go del `mvn clean verify` de la [semana 31](../week-31-proyecto_integrador_java/README.md).

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Repaso: la suite completa, `verify.sh`, el umbral y los datos del reporte | 0.5 h |
| Planificación | 2 labs: una suite en un comando, los datos del reporte | 2.5 h |
| Desarrollo | Proyecto integrador en el proyecto hilo conductor | 6.5 h |
| Presentación | 5 minutos por aprendiz con los datos del reporte | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [La suite Go completa](./1-teoria/01-la-suite-go-completa.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Una suite, un comando](./2-practicas/lab-01-una-suite-un-comando/README.md)
- [Lab 02 - Los datos del reporte](./2-practicas/lab-02-datos-del-reporte/README.md)

### Proyecto

- [Suite Go completa del dominio](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto integrador (único entregable obligatorio): rama `week-36` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo, con Docker en marcha:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 36
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-36-proyecto_integrador_go/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   `-- 01-verify-sh-por-fases.svg
|-- 1-teoria/
|   `-- 01-la-suite-go-completa.md
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-una-suite-un-comando/
|   `-- lab-02-datos-del-reporte/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/
|-- 4-recursos/
|   |-- ebooks-free/README.md
|   |-- videografia/README.md
|   `-- webgrafia/README.md
`-- 5-glosario/
    `-- README.md
```

---

## Versiones de la semana

Ninguna herramienta nueva: Go 1.26.5, testcontainers-go v0.45.0, pgx v5.13.0 y staticcheck v0.8.2, las de las semanas 32 a 35. Están registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

El reporte del museo encontró que, si falla el aviso a la institución, la API responde 500 pero el préstamo ya está abierto. `Lend` tiene el 100 % de cobertura y un test que prueba ese fallo: comprueba el error, no el estado que deja. La cobertura dice qué líneas se ejecutan; qué se comprueba lo decides tú en cada test y en la sección de riesgos.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 35 - Concurrencia y Calidad en Go](../week-35-concurrencia_y_calidad_en_go/README.md) | Semana 37 - Próximamente |
