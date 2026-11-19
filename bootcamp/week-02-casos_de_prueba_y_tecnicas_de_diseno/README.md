# Semana 02 - Casos de Prueba y Técnicas de Diseño

> **Etapa 0 - Fundamentos de Testing** | Semana 2 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Escribir casos de prueba que otra persona ejecute sin preguntarte: precondiciones comprobables, pasos de una acción y resultado esperado observable.
2. Dividir las entradas de un campo en clases de equivalencia y elegir sus valores límite.
3. Construir una tabla de decisión, encontrar las combinaciones que los requisitos no deciden y simplificarla.
4. Explicar qué mide la cobertura y qué no.
5. Elegir qué casos van en smoke, sanity y regresión.
6. Hacer una sesión exploratoria con charter y notas.

---

## Requisitos previos

- La [semana 1](../week-01-que_es_testing_calidad_de_software/README.md): niveles de prueba, reporte de bug, severidad y prioridad.
- Una terminal (en Windows, WSL o Git Bash) y un editor de texto.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | El caso de prueba, técnicas de diseño, cobertura y tipos de suite | 2.5 h |
| Prácticas | 4 labs: análisis de casos, partición y límites, tabla de decisión, exploratorio | 4 h |
| Proyecto | Suite de casos de prueba de una funcionalidad del dominio | 3 h |
| Recursos | Lecturas y vídeos | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [El caso de prueba](./1-teoria/01-casos-de-prueba.md)
2. [Técnicas de diseño: partición, valores límite y tablas de decisión](./1-teoria/02-tecnicas-de-diseno.md)
3. [Cobertura, suites y testing exploratorio](./1-teoria/03-cobertura-y-tipos.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Análisis de casos](./2-practicas/lab-01-analisis-de-casos/README.md)
- [Lab 02 - Partición y valores límite](./2-practicas/lab-02-particion-y-valores-limite/README.md)
- [Lab 03 - Tabla de decisión](./2-practicas/lab-03-tabla-de-decision/README.md)
- [Lab 04 - Exploratorio y regresión](./2-practicas/lab-04-exploratorio-y-regresion/README.md)

### Proyecto

- [Suite de casos de prueba](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto (único entregable obligatorio): `casos-de-prueba.md` de una funcionalidad de tu dominio, por el canal que indique el instructor. Antes de entregarlo:

  ```bash
  PROYECTO=~/proyecto-semana-02 ./scripts/verificar-semana.sh 02
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-02-casos_de_prueba_y_tecnicas_de_diseno/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-particion-equivalencia.svg
|   `-- 02-valores-limite.svg
|-- 1-teoria/
|   |-- 01-casos-de-prueba.md
|   |-- 02-tecnicas-de-diseno.md
|   `-- 03-cobertura-y-tipos.md
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-analisis-de-casos/
|   |-- lab-02-particion-y-valores-limite/
|   |-- lab-03-tabla-de-decision/
|   `-- lab-04-exploratorio-y-regresion/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/
|       `-- casos-de-prueba.md
|-- 4-recursos/
|   |-- ebooks-free/README.md
|   |-- videografia/README.md
|   `-- webgrafia/README.md
`-- 5-glosario/
    `-- README.md
```

---

## Versiones de la semana

Ninguna herramienta que instalar. El lab 04 usa el `date` de GNU que trae la terminal (GNU coreutils 8.32 en las salidas de este material). Las versiones de todo el curso están en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Los casos que diseñas esta semana son los que automatizarás a partir de la [semana 3](../week-03-primeros_tests_automatizados_con_javascript/README.md): cada fila de valores límite será un test, y cada resultado esperado, una aserción. Un caso mal diseñado en Markdown da un test automatizado igual de malo.

---

## Navegación

| ← Semana anterior | Siguiente → |
|---|---|
| [Semana 01 - ¿Qué es Testing? Calidad de Software](../week-01-que_es_testing_calidad_de_software/README.md) | [Semana 03 - Primeros Tests Automatizados con JavaScript](../week-03-primeros_tests_automatizados_con_javascript/README.md) |
