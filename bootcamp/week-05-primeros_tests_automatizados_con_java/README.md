# Semana 05 — Primeros Tests Automatizados con Java

> **Etapa 0 — Fundamentos de Testing** | Semana 5 de 6

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## 🎯 Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Configurar un entorno de testing con **JDK 21 + Maven + JUnit 6**
2. Entender la estructura base de un test con `@Test`, `@DisplayName` y el ciclo de vida (`@BeforeEach`, `@AfterEach`, `@BeforeAll`, `@AfterAll`)
3. Aplicar patrón AAA en tests unitarios en Java
4. Usar assertions básicas: `assertEquals` (con delta para `double`), `assertTrue`, `assertFalse`, `assertNotNull`, `assertThrows`
5. Ejecutar tests con `mvn test` y leer la consola y los reportes de `target/surefire-reports/`
6. Distinguir Failure vs Error en el reporte de Surefire
7. Reconocer las diferencias de anotaciones entre JUnit 4 y JUnit Jupiter (JUnit 5 y 6)
8. Escribir las primeras assertions fluentes con AssertJ
9. Construir una suite inicial de tests unitarios para funciones del dominio asignado y compararla con JS y Python

---

## ⏱ Distribución del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| 📖 Teoría | Setup Java, estructura y ciclo de vida de JUnit 6, assertions, Surefire y AssertJ | 2 h |
| 💻 Prácticas | 2 ejercicios guiados de tests unitarios básicos | 3.5 h |
| 🏗 Proyecto | Suite inicial de tests Java del dominio asignado | 2 h |
| 📚 Recursos | Lecturas y videos de refuerzo | 0.5 h |

---

## 📂 Contenido de la Semana

### 📖 Teoría

1. [Setup de entorno Java + Maven + JUnit 6](./1-teoria/01-setup-junit.md)
2. [Estructura de un test en JUnit 6](./1-teoria/02-estructura-test-junit.md)
3. [Assertions y ejecución con Maven](./1-teoria/03-assertions-y-ejecucion.md)

### 💻 Prácticas

- [Ejercicio 01 — Configurar y ejecutar JUnit por primera vez](./2-practicas/ejercicio-01-setup-junit/)
- [Ejercicio 02 — Escribir tests unitarios básicos en Java](./2-practicas/ejercicio-02-primeros-tests-java/)

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
week-05-primeros_tests_automatizados_con_java/
├── README.md
├── rubrica-evaluacion.md
├── 0-assets/
│   ├── 01-ciclo-junit.svg
│   ├── 02-patron-aaa-java.svg
│   └── 03-cli-maven-test.svg
├── 1-teoria/
│   ├── 01-setup-junit.md
│   ├── 02-estructura-test-junit.md
│   └── 03-assertions-y-ejecucion.md
├── 2-practicas/
│   ├── ejercicio-01-setup-junit/
│   │   ├── README.md
│   │   ├── starter/
│   │   └── solution/
│   └── ejercicio-02-primeros-tests-java/
│       ├── README.md
│       ├── starter/
│       └── solution/
├── 3-proyecto/
│   ├── README.md
│   └── starter/
│       ├── pom.xml
│       └── src/test/java/com/bootcamp/ItemServiceTest.java
├── 4-recursos/
│   ├── ebooks-free/README.md
│   ├── videografia/README.md
│   └── webgrafia/README.md
└── 5-glosario/
    └── README.md
```

---

## ⚠️ Nota importante

Esta semana cierra el bloque de fundamentos multilenguaje (JS, Python, Java). La prioridad es comparar la intención de test entre lenguajes, manteniendo calidad de diseño.

---

## 🧭 Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 04 — Primeros Tests en Python](../week-04-primeros_tests_automatizados_con_python/README.md) | [Semana 06 — Proyecto Integrador Etapa 0](../week-06-plan_de_pruebas_y_suite_base_multilenguaje/README.md) |
