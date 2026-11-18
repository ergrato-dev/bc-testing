# Semana 44 - Testing de Seguridad Básico (OWASP)

> **Etapa 6 - Testing Avanzado Transversal** | Semana 44 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Situar un fallo en el OWASP Top 10 2025 y escribir los casos de abuso que lo prueban.
2. Escribir tests de inyección SQL y XSS que distinguen "sin resultados" de "consulta rota" y de "devolvió de más", y corregir con parámetros y escape según el contexto.
3. Probar la autenticación y la matriz de autorización, y encontrar un IDOR con dos usuarios.
4. Comprobar las cabeceras de seguridad en un test y desde fuera con OWASP ZAP baseline, y distinguirlo de un escaneo activo.
5. Auditar las dependencias de los cinco lenguajes del curso y corregir lo que encuentran.

---

## Requisitos previos

- Semanas 34 y 43: la API Go del dominio, sus tests con `httptest` y `pgstore`, y su servidor `go/cmd/server`.
- Semanas 24, 31, 41 y 42: los proyectos de Python, Java, Rust y Playwright, que se auditan en el proyecto.
- Go 1.26.5, pnpm, uv, Rust y Docker. La imagen de ZAP ocupa unos 2 GB: descárgala antes (lab 03).

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | OWASP Top 10 desde el tester; inyección y XSS; autenticación, autorización e IDOR; cabeceras, ZAP y dependencias | 2.5 h |
| Prácticas | 3 labs que vuelven segura la API de una biblioteca y 1 de auditoría de dependencias | 4 h |
| Proyecto | Tests de seguridad de la API Go del dominio, ZAP baseline, auditorías e informe | 3 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [OWASP Top 10 desde el tester](./1-teoria/01-owasp-top-10-para-testers.md)
2. [Inyección SQL y XSS](./1-teoria/02-inyeccion-y-xss.md)
3. [Autenticación, autorización e IDOR](./1-teoria/03-autenticacion-autorizacion-e-idor.md)
4. [Cabeceras, ZAP y auditoría de dependencias](./1-teoria/04-cabeceras-zap-y-dependencias.md)

### Prácticas

- [Índice de labs y API de la biblioteca insegura](./2-practicas/README.md)
- [Lab 01 - Inyección SQL y XSS](./2-practicas/lab-01-inyeccion-sql-y-xss/README.md)
- [Lab 02 - Autenticación, autorización e IDOR](./2-practicas/lab-02-autenticacion-autorizacion-e-idor/README.md)
- [Lab 03 - Cabeceras y ZAP baseline](./2-practicas/lab-03-cabeceras-y-zap-baseline/README.md)
- [Lab 04 - Auditoría de dependencias](./2-practicas/lab-04-auditoria-de-dependencias/README.md)

### Proyecto

- [Seguridad de la API del dominio](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): rama `week-44` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 44
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md). Necesitan Docker (los tests de integración) y los resultados de `security/zap.sh` y `security/audit.sh` ya guardados.

---

## Estructura de Carpetas

```text
week-44-testing_de_seguridad_basico_owasp/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-donde-mira-cada-tecnica.svg
|   `-- 02-idor.svg
|-- 1-teoria/
|-- 2-practicas/
|   |-- README.md
|   |-- biblioteca-insegura/
|   |-- lab-01-inyeccion-sql-y-xss/
|   |-- lab-02-autenticacion-autorizacion-e-idor/
|   |-- lab-03-cabeceras-y-zap-baseline/
|   `-- lab-04-auditoria-de-dependencias/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/
|       |-- go/museum/
|       `-- security/
|-- 4-recursos/
|   |-- ebooks-free/README.md
|   |-- videografia/README.md
|   `-- webgrafia/README.md
`-- 5-glosario/
    `-- README.md
```

---

## Versiones de la semana

OWASP ZAP 2.17.0 (imagen `zaproxy/zap-stable:2.17.0`), OSV-Scanner 2.7.1 (imagen `ghcr.io/google/osv-scanner:v2.7.1`), `govulncheck` 1.10.0 (`golang.org/x/vuln`), `cargo-audit` 0.23.0, `uv audit` de uv 0.13 y `pnpm audit` de pnpm 11. Go 1.26.5 y `pgx` 5.13.0 para la API de los labs; PostgreSQL con la imagen `postgres:18.7-alpine`. Están registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Todo lo de esta semana se ejecuta contra APIs que arrancas en tu equipo. Escanear o atacar un sistema sin permiso de su dueño es ilegal, aunque sea para probar. Y ningún resultado de esta semana demuestra que un sistema sea seguro: un test demuestra que un ataque concreto falla, y un escaneo sin avisos dice lo que el escáner sabe mirar.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 43 - Testing de Performance con k6 y Locust](../week-43-testing_de_performance_con_k6_y_locust/README.md) | Semana 45 - Próximamente |
