# Semana 43 - Testing de Performance con k6 y Locust

> **Etapa 6 - Testing Avanzado Transversal** | Semana 43 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Elegir el tipo de prueba de rendimiento según la pregunta, y distinguirla de un microbenchmark.
2. Leer una prueba de carga: peticiones por segundo, percentiles de latencia y tasa de errores, y comprobarlos con la ley de Little.
3. Escribir pruebas de carga con k6, con escenarios, umbrales por petición y resultados guardados.
4. Escribir usuarios de Locust con pesos, validación de respuestas, una forma de carga escalonada y criterios en el código de salida.
5. Medir una línea base, encontrar la rodilla de un stress test y demostrar con datos qué recurso limita.

---

## Requisitos previos

- Semanas 35 y 40: los microbenchmarks de Go (`b.Loop()`) y Rust (criterion), que esta semana se comparan con las pruebas de carga.
- Semanas 34 y 36: la API Go del dominio, el objetivo del proyecto.
- Semana 16 y siguientes: Python con uv, para Locust.
- Go 1.26.5, uv, Docker y k6 2.3.0 (se instala en el lab 01).

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Qué es performance testing; k6; Locust; línea base, criterios y cuellos de botella | 2.5 h |
| Prácticas | 4 labs contra la API de una biblioteca: primer test con k6, umbrales y etapas, Locust, el cuello de botella | 4 h |
| Proyecto | Prueba de carga y stress test de la API Go del dominio, con informe | 3 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Qué es performance testing](./1-teoria/01-performance-testing.md)
2. [k6](./1-teoria/02-k6.md)
3. [Locust](./1-teoria/03-locust.md)
4. [Línea base, criterios y cuellos de botella](./1-teoria/04-linea-base-criterios-y-cuellos-de-botella.md)

### Prácticas

- [Índice de labs y API de la biblioteca](./2-practicas/README.md)
- [Lab 01 - Primer test de carga con k6](./2-practicas/lab-01-primer-test-de-carga-con-k6/README.md)
- [Lab 02 - Umbrales, etapas y etiquetas](./2-practicas/lab-02-umbrales-etapas-y-etiquetas/README.md)
- [Lab 03 - Locust](./2-practicas/lab-03-locust/README.md)
- [Lab 04 - El cuello de botella](./2-practicas/lab-04-cuello-de-botella/README.md)

### Proyecto

- [Rendimiento de la API del dominio con k6 y Locust](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): rama `week-43` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 43
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md). La primera ejecuta `perf/run.sh smoke` de verdad: necesita Docker, Go, k6 y uv.

---

## Estructura de Carpetas

```text
week-43-testing_de_performance_con_k6_y_locust/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-tipos-de-prueba.svg
|   |-- 02-modelo-cerrado-y-abierto.svg
|   `-- 03-la-rodilla.svg
|-- 1-teoria/
|-- 2-practicas/
|   |-- README.md
|   |-- biblioteca-api/
|   |-- lab-01-primer-test-de-carga-con-k6/
|   |-- lab-02-umbrales-etapas-y-etiquetas/
|   |-- lab-03-locust/
|   `-- lab-04-cuello-de-botella/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/
|       |-- go/cmd/server/
|       `-- perf/
|-- 4-recursos/
|   |-- ebooks-free/README.md
|   |-- videografia/README.md
|   `-- webgrafia/README.md
`-- 5-glosario/
    `-- README.md
```

---

## Versiones de la semana

k6 2.3.0 (binario o imagen `grafana/k6:2.3.0`); Locust 2.47.0 en `[dependency-groups] dev`, con Python 3.14 y uv; Go 1.26.5 y `pgx` 5.13.0 para la API de los labs; PostgreSQL con la imagen `postgres:18.7-alpine`. Están registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Una prueba de carga mide todo lo que hay entre el generador y la base de datos, el generador incluido. En los labs, la API, PostgreSQL, k6 y Locust comparten tu equipo, y hasta el reloj del sistema puede poner un máximo de 3 s que nunca ocurrió. Por eso los criterios se escriben sobre percentiles, las comparaciones se hacen con la misma carga y en el mismo equipo, y cada conclusión se apoya en un dato que la demuestra.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 42 - Testing End-to-End con Playwright](../week-42-testing_end_to_end_con_playwright/README.md) | Semana 44 - Próximamente |
