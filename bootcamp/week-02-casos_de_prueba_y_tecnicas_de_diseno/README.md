# Semana 02 — Casos de Prueba y Técnicas de Diseño

> **Etapa 0 — Fundamentos de Testing** | Semana 2 de 6

![Bootcamp Testing](../../_assets/bootcamp-header.svg)

---

## 🎯 Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Definir la estructura de un caso de prueba profesional con todos sus campos obligatorios
2. Aplicar la técnica de **partición de equivalencia** para reducir el número de casos necesarios
3. Aplicar el **análisis de valores límite** (BVA) para detectar errores en bordes
4. Construir **tablas de decisión** para reglas de negocio con múltiples condiciones
5. Distinguir entre testing exploratorio y scripted, y saber cuándo usar cada uno
6. Diferenciar smoke tests, regression tests y sanity tests por propósito y alcance
7. Diseñar una suite completa de casos de prueba manuales para una funcionalidad real

---

## ⏱ Distribución del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|-----------|-----------|--------|
| 📖 Teoría | Casos de prueba, técnicas de diseño, cobertura y tipos | 2.5 h |
| 💻 Prácticas | 2 ejercicios guiados de diseño de casos | 3 h |
| 🏗 Proyecto | Suite de casos de prueba del dominio asignado | 2 h |
| 📚 Recursos | Lecturas, videos y referencias | 0.5 h |

---

## 📂 Contenido de la Semana

### 📖 Teoría

1. [Casos de Prueba — Estructura y Campos](./1-teoria/01-casos-de-prueba.md)
2. [Técnicas de Diseño — Partición, BVA y Tablas de Decisión](./1-teoria/02-tecnicas-de-diseno.md)
3. [Cobertura, Tipos de Test y Herramientas de Gestión](./1-teoria/03-cobertura-y-tipos.md)

### 💻 Prácticas

- [Ejercicio 01 — Analizar y Completar Casos de Prueba](./2-practicas/ejercicio-01-analisis-casos/)
- [Ejercicio 02 — Aplicar BVA y Partición de Equivalencia](./2-practicas/ejercicio-02-tecnicas-diseno/)

### 🏗 Proyecto

- [Suite de Casos de Prueba del Dominio Asignado](./3-proyecto/README.md)

### 📚 Recursos

- [Ebooks Gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### 📝 Glosario

- [Términos Clave de la Semana](./5-glosario/README.md)

---

## 🗂 Estructura de Carpetas

```
week-02-casos_de_prueba_y_tecnicas_de_diseno/
├── README.md
├── rubrica-evaluacion.md
├── 0-assets/
│   ├── 01-particion-equivalencia.svg
│   └── 02-valores-limite.svg
├── 1-teoria/
│   ├── 01-casos-de-prueba.md
│   ├── 02-tecnicas-de-diseno.md
│   └── 03-cobertura-y-tipos.md
├── 2-practicas/
│   ├── ejercicio-01-analisis-casos/
│   │   ├── README.md
│   │   ├── starter/
│   │   └── solution/
│   └── ejercicio-02-tecnicas-diseno/
│       ├── README.md
│       ├── starter/
│       └── solution/
├── 3-proyecto/
│   ├── README.md
│   └── starter/
│       └── casos-de-prueba.md
├── 4-recursos/
│   ├── ebooks-free/README.md
│   ├── videografia/README.md
│   └── webgrafia/README.md
└── 5-glosario/
    └── README.md
```

---

## ⚠️ Nota importante

Esta semana es **100% conceptual** — no hay código. El objetivo es dominar el diseño de pruebas antes de automatizarlas. Un test mal diseñado sigue siendo un test inútil aunque esté automatizado.

> "Escribir tests es fácil. Saber *qué* testear es la habilidad real." — Michael Bolton

---

## 🧭 Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 01 — ¿Qué es Testing?](../week-01-que_es_testing_calidad_de_software/README.md) | [Semana 03 — Primeros Tests en JavaScript](../week-03-primeros_tests_automatizados_con_javascript/README.md) |
