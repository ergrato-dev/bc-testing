# Semana 03 — Primeros Tests Automatizados con JavaScript

> **Etapa 0 — Fundamentos de Testing** | Semana 3 de 6

![Bootcamp Testing](../../_assets/bootcamp-header.svg)

---

## 🎯 Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Configurar un entorno mínimo de testing con **Node.js + pnpm/yarn + Jest**
2. Explicar la anatomía de un test en Jest usando `describe`, `test`/`it` y `expect`
3. Aplicar el patrón **AAA (Arrange-Act-Assert)** en tests unitarios simples
4. Usar matchers básicos: `toBe`, `toEqual`, `toBeTruthy`, `toBeFalsy`, `toBeNull`, `toThrow`
5. Ejecutar y filtrar pruebas desde terminal (`pnpm test`, modo watch, patrón por nombre)
6. Interpretar resultados de ejecución: pass, fail, error de ejecución y assertion failure
7. Diseñar una suite inicial de tests unitarios para funciones puras del dominio asignado

---

## ⏱ Distribución del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| 📖 Teoría | Configuración de entorno, estructura de Jest, assertions y lectura de resultados | 2 h |
| 💻 Prácticas | 2 ejercicios guiados con código comentado para descomentar | 3.5 h |
| 🏗 Proyecto | Suite básica de tests unitarios del dominio asignado | 2 h |
| 📚 Recursos | Lecturas y videos de refuerzo | 0.5 h |

---

## 📂 Contenido de la Semana

### 📖 Teoría

1. [Setup del entorno con Node.js y Jest](./1-teoria/01-setup-jest.md)
2. [Estructura de un test en Jest (AAA)](./1-teoria/02-estructura-test-jest.md)
3. [Matchers básicos y ejecución de pruebas](./1-teoria/03-matchers-y-ejecucion.md)

### 💻 Prácticas

- [Ejercicio 01 — Configurar y ejecutar Jest por primera vez](./2-practicas/ejercicio-01-setup-jest/)
- [Ejercicio 02 — Escribir tests unitarios básicos con AAA](./2-practicas/ejercicio-02-primeros-tests/)

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
week-03/
├── README.md
├── rubrica-evaluacion.md
├── 0-assets/
│   ├── 01-ciclo-test-jest.svg
│   └── 02-patron-aaa.svg
├── 1-teoria/
│   ├── 01-setup-jest.md
│   ├── 02-estructura-test-jest.md
│   └── 03-matchers-y-ejecucion.md
├── 2-practicas/
│   ├── ejercicio-01-setup-jest/
│   │   ├── README.md
│   │   ├── starter/
│   │   └── solution/
│   └── ejercicio-02-primeros-tests/
│       ├── README.md
│       ├── starter/
│       └── solution/
├── 3-proyecto/
│   ├── README.md
│   └── starter/
│       └── item.service.test.js
├── 4-recursos/
│   ├── ebooks-free/README.md
│   ├── videografia/README.md
│   └── webgrafia/README.md
└── 5-glosario/
    └── README.md
```

---

## ⚠️ Nota importante

Esta es la **primera semana con tests automatizados en código real**. La prioridad no es la complejidad de los casos, sino construir correctamente los fundamentos:

- Setup correcto
- Nombres de test descriptivos
- AAA consistente
- Assertions claras

> "Un test simple, legible y confiable vale más que una suite grande y confusa."

---

## 🧭 Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 02 — Casos de Prueba y Técnicas de Diseño](../week-02/README.md) | [Semana 04 — Primeros Tests en Python](../week-04/README.md) |
