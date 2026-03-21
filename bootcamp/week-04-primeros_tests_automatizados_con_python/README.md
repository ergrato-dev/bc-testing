# Semana 04 — Primeros Tests Automatizados con Python

> **Etapa 0 — Fundamentos de Testing** | Semana 4 de 6

![Bootcamp Testing](../../_assets/bootcamp-header.svg)

---

## 🎯 Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Configurar un entorno mínimo de testing con **Python 3.12+ y pytest**
2. Entender la estructura básica de tests con `test_*.py` y `def test_*`
3. Escribir assertions claras usando `assert` nativo de Python
4. Ejecutar tests con `pytest`, `pytest -v` y filtrado por nombre con `-k`
5. Distinguir errores de ejecución vs fallos de assertion en reportes de pytest
6. Aplicar patrón AAA de forma consistente en tests unitarios simples
7. Construir una suite inicial de tests para funciones puras del dominio asignado

---

## ⏱ Distribución del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| 📖 Teoría | Setup de entorno Python + estructura y ejecución con pytest | 2 h |
| 💻 Prácticas | 2 ejercicios guiados de tests básicos con pytest | 3.5 h |
| 🏗 Proyecto | Suite inicial de tests en Python del dominio asignado | 2 h |
| 📚 Recursos | Lecturas y videos de refuerzo | 0.5 h |

---

## 📂 Contenido de la Semana

### 📖 Teoría

1. [Setup de entorno Python + pytest](./1-teoria/01-setup-pytest.md)
2. [Estructura de un test en pytest y patrón AAA](./1-teoria/02-estructura-test-pytest.md)
3. [Assertions, ejecución y lectura de reportes](./1-teoria/03-assertions-y-ejecucion.md)

### 💻 Prácticas

- [Ejercicio 01 — Configurar y ejecutar pytest por primera vez](./2-practicas/ejercicio-01-setup-pytest/)
- [Ejercicio 02 — Escribir tests unitarios básicos en Python](./2-practicas/ejercicio-02-primeros-tests-python/)

### 🏗 Proyecto

- [Suite Inicial de Tests del Dominio Asignado](./3-proyecto/README.md)

### 📚 Recursos

- [Ebooks Gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### 📝 Glosario

- [Términos Clave de la Semana](./5-glosario/README.md)

---

## 🗂 Estructura de Carpetas

```
week-04-primeros_tests_automatizados_con_python/
├── README.md
├── rubrica-evaluacion.md
├── 0-assets/
│   ├── 01-ciclo-pytest.svg
│   ├── 02-patron-aaa-python.svg
│   └── 03-cli-pytest.svg
├── 1-teoria/
│   ├── 01-setup-pytest.md
│   ├── 02-estructura-test-pytest.md
│   └── 03-assertions-y-ejecucion.md
├── 2-practicas/
│   ├── ejercicio-01-setup-pytest/
│   │   ├── README.md
│   │   ├── starter/
│   │   └── solution/
│   └── ejercicio-02-primeros-tests-python/
│       ├── README.md
│       ├── starter/
│       └── solution/
├── 3-proyecto/
│   ├── README.md
│   └── starter/
│       └── item_service_test.py
├── 4-recursos/
│   ├── ebooks-free/README.md
│   ├── videografia/README.md
│   └── webgrafia/README.md
└── 5-glosario/
    └── README.md
```

---

## ⚠️ Nota importante

Esta semana replica fundamentos de la semana 03 pero en Python. El foco es comparar estilo y sintaxis sin perder calidad de diseño.

> "Misma intención de prueba, diferente lenguaje."

---

## 🧭 Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 03 — Primeros Tests en JavaScript](../week-03-primeros_tests_automatizados_con_javascript/README.md) | [Semana 05 — Primeros Tests en Java](../week-05-primeros_tests_automatizados_con_java/README.md) |
