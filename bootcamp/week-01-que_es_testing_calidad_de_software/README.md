# Semana 01 - ¿Qué es Testing? Calidad de Software

> **Etapa 0 - Fundamentos de Testing** | Semana 1 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Explicar qué es la calidad del software, sus dimensiones y lo que cuesta un bug.
2. Distinguir los roles de tester, desarrollador y SDET, y el testing manual del automatizado.
3. Describir los niveles de prueba y la pirámide de testing, y decidir el nivel de un test por lo que usa.
4. Distinguir error, defecto y fallo, y severidad de prioridad.
5. Seguir un bug por su ciclo de vida y hacer el triage de reportes nuevos.
6. Escribir un reporte de bug que otra persona pueda reproducir sin preguntarte.

---

## Requisitos previos

- Ninguno de testing. Una terminal (en Windows, WSL o Git Bash) y un editor de texto.

Esta semana no hay código: se instala lo necesario a partir de la [semana 3](../week-03-primeros_tests_automatizados_con_javascript/README.md).

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Calidad y tipos de testing, pirámide y niveles, ciclo de vida del bug | 2.5 h |
| Prácticas | 4 labs: clasificación, análisis de reportes, del mensaje al reporte, triage | 4 h |
| Proyecto | Reporte de bug de un software real | 3 h |
| Recursos | Lecturas y vídeos | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Calidad de software y tipos de testing](./1-teoria/01-calidad-y-tipos-de-testing.md)
2. [La pirámide de testing y los niveles de prueba](./1-teoria/02-piramide-y-niveles.md)
3. [El ciclo de vida del bug](./1-teoria/03-ciclo-de-vida-del-bug.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Calidad y niveles](./2-practicas/lab-01-calidad-y-niveles/README.md)
- [Lab 02 - Análisis de reportes](./2-practicas/lab-02-analisis-de-reportes/README.md)
- [Lab 03 - Del mensaje al reporte](./2-practicas/lab-03-del-mensaje-al-reporte/README.md)
- [Lab 04 - Triage y ciclo de vida](./2-practicas/lab-04-triage-y-ciclo-de-vida/README.md)

### Proyecto

- [Reporte de bug profesional](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto (único entregable obligatorio): `reporte-bug.md` de un bug real, por el canal que indique el instructor. Antes de entregarlo:

  ```bash
  PROYECTO=~/proyecto-semana-01 ./scripts/verificar-semana.sh 01
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-01-que_es_testing_calidad_de_software/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-piramide-testing.svg
|   `-- 02-ciclo-bug.svg
|-- 1-teoria/
|   |-- 01-calidad-y-tipos-de-testing.md
|   |-- 02-piramide-y-niveles.md
|   `-- 03-ciclo-de-vida-del-bug.md
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-calidad-y-niveles/
|   |-- lab-02-analisis-de-reportes/
|   |-- lab-03-del-mensaje-al-reporte/
|   `-- lab-04-triage-y-ciclo-de-vida/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/
|       `-- reporte-bug.md
|-- 4-recursos/
|   |-- ebooks-free/README.md
|   |-- videografia/README.md
|   `-- webgrafia/README.md
`-- 5-glosario/
    `-- README.md
```

---

## Versiones de la semana

Ninguna herramienta. Las versiones de todo el curso están en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Reportar bien es la primera habilidad de quien prueba y la que más se nota en un equipo. Cada test automatizado que escribirás a partir de la semana 3 es un reporte de bug escrito antes de que el bug exista: un punto de partida, unos pasos y un resultado esperado.

---

## Navegación

| ← Semana anterior | Siguiente → |
|---|---|
| [Índice del bootcamp](../README.md) | [Semana 02 - Casos de Prueba y Técnicas de Diseño](../week-02-casos_de_prueba_y_tecnicas_de_diseno/README.md) |
