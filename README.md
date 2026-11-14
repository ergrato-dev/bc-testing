<p align="center">
  <img src="assets/bootcamp-header.svg" alt="Bootcamp Testing de Software Zero to Hero" width="800">
</p>

<p align="center">
  <a href="https://github.com/ergrato-dev/bc-testing/blob/main/LICENSE"><img src="https://img.shields.io/badge/license-CC_BY--NC--SA_4.0-green.svg" alt="License CC BY-NC-SA 4.0"></a>
  <a href="#"><img src="https://img.shields.io/badge/semanas-46-6366f1.svg" alt="46 Semanas"></a>
  <a href="#"><img src="https://img.shields.io/badge/horas-460-6366f1.svg" alt="460 Horas"></a>
  <a href="#"><img src="https://img.shields.io/badge/JavaScript-Jest-F7DF1E?logo=javascript&logoColor=black" alt="JavaScript Jest"></a>
  <a href="#"><img src="https://img.shields.io/badge/Python-pytest-3572A5?logo=python&logoColor=white" alt="Python pytest"></a>
  <a href="#"><img src="https://img.shields.io/badge/Java-JUnit6-F89820?logo=openjdk&logoColor=white" alt="Java JUnit 6"></a>
  <a href="#"><img src="https://img.shields.io/badge/Go-go_test-00ADD8?logo=go&logoColor=white" alt="Go go test"></a>
  <a href="#"><img src="https://img.shields.io/badge/Rust-cargo_test-DEA584?logo=rust&logoColor=black" alt="Rust cargo test"></a>
</p>

<p align="center">
  <a href="README_EN.md"><img src="https://img.shields.io/badge/🇺🇸_English-0969DA?style=for-the-badge&logoColor=white" alt="English Version"></a>
</p>

---

## 📋 Descripción

Bootcamp intensivo de **46 semanas (~11,5 meses)** enfocado en el dominio del **testing de software automatizado** en cinco lenguajes: JavaScript, Python, Java, Go y Rust. Diseñado para llevar a estudiantes de cero a **QA Engineer / SDET Junior**, con énfasis en buenas prácticas, TDD, BDD y testing en pipelines CI/CD.

> 🏛️ **Política de Dominios Únicos (Anticopia)**: Cada aprendiz trabaja sobre un dominio de negocio único asignado por el instructor (Biblioteca, Farmacia, Gimnasio, etc.). Esto garantiza suites de tests originales y previene la copia entre compañeros.

### 🎯 Objetivos

Al finalizar el bootcamp, los estudiantes serán capaces de:

- ✅ Aplicar la mentalidad de calidad y diseñar casos de prueba profesionales
- ✅ Escribir tests unitarios en JavaScript (Jest), Python (pytest), Java (JUnit 6), Go (`go test`) y Rust (`cargo test`)
- ✅ Usar mocks, stubs y spies para aislar dependencias en los cinco lenguajes
- ✅ Aplicar TDD (Red-Green-Refactor) en JavaScript, Python y Java
- ✅ Implementar BDD con Behave (Python) y Cucumber (Java)
- ✅ Escribir tests de integración para APIs REST en los cinco lenguajes
- ✅ Aplicar fuzzing, property-based testing y benchmarks (Go y Rust)
- ✅ Automatizar pruebas E2E con Playwright
- ✅ Ejecutar tests de performance con k6 y Locust
- ✅ Integrar testing en pipelines CI/CD con GitHub Actions
- ✅ Medir y mantener coverage de código ≥85% en los cinco lenguajes

---

## 🗓️ Estructura del Bootcamp

| Etapa | Semanas | Horas | Temas Principales |
| :---: | :-----: | :---: | --- |
| **Etapa 0** — Fundamentos de Testing | 1–6 | 60h | Mentalidad de calidad, terminología, pirámide de testing, primeros tests en los 3 lenguajes en paralelo |
| **Etapa 1** — Testing con JavaScript | 7–15 | 90h | Jest avanzado, mocks/stubs/spies, async testing, TDD, Supertest, snapshot, fast-check |
| **Etapa 2** — Testing con Python | 16–24 | 90h | pytest, fixtures, unittest.mock, TDD, Behave (BDD), httpx2, Testcontainers |
| **Etapa 3** — Testing con Java | 25–31 | 70h | JUnit 6, AssertJ, Mockito, TDD, Spring Boot Test, Testcontainers, Cucumber |
| **Etapa 4** — Testing con Go | 32–36 | 50h | `go test`, table-driven tests, interfaces y mockgen, httptest, testcontainers-go, `-race`, fuzzing, benchmarks |
| **Etapa 5** — Testing con Rust | 37–41 | 50h | `cargo test`, doctests, mockall, rstest, Tokio/axum, `#[sqlx::test]`, proptest, cargo-llvm-cov |
| **Etapa 6** — Testing Avanzado Transversal | 42–46 | 50h | Playwright E2E, k6/Locust performance, OWASP y auditoría de dependencias, CI/CD con GitHub Actions |

**Total: 46 semanas** | **~460 horas** de formación intensiva

---

## 📚 Contenido por Semana

Cada semana incluye:

```
bootcamp/week-XX/
├── README.md                 # Descripción y objetivos de la semana
├── rubrica-evaluacion.md     # Criterios de evaluación
├── 0-assets/                 # Diagramas SVG
├── 1-teoria/                 # Material teórico
├── 2-practicas/              # Ejercicios guiados (código comentado)
├── 3-proyecto/               # Proyecto semanal (entregable)
├── 4-recursos/               # Recursos adicionales
│   ├── ebooks-free/
│   ├── videografia/
│   └── webgrafia/
└── 5-glosario/               # Términos clave
```

### 🔑 Componentes Clave

- 📖 **Teoría**: Conceptos de testing con comparativas entre lenguajes
- 💻 **Prácticas**: Ejercicios guiados — el estudiante descomenta código paso a paso
- 📝 **Proyecto**: Suite de tests del dominio asignado con TODOs para implementar
- 📊 **Rúbrica**: Criterios claros con tres tipos de evidencia (Conocimiento · Desempeño · Producto)

---

## 🛠️ Herramientas por Lenguaje

| Lenguaje | Framework principal | Gestor / build |
| --- | --- | --- |
| JavaScript | Jest | `pnpm` (❌ NO usar `npm` ni `yarn`) |
| Python | pytest | `uv` |
| Java | JUnit 6 | Maven |
| Go | `go test` (stdlib) | `go` |
| Rust | `cargo test` | `cargo` (`rustup`) |

Versiones exactas, dónde se fijan y por qué: [`docs/stack-versions.md`](docs/stack-versions.md).

---

## 🚀 Inicio Rápido

### Prerrequisitos

- **Node.js** 22 LTS + `pnpm` (para semanas de JavaScript)
- **Python** 3.14 + `uv` (para semanas de Python; nunca `pip`)
- **JDK 21 LTS** + **Maven** (para semanas de Java)
- **Go** (para semanas de Go, versión en la tabla de herramientas)
- **Rust** vía `rustup`, toolchain stable (+ nightly solo para `cargo-fuzz`)
- **Git** 2.40+
- **VS Code** con extensiones recomendadas (aparecen automáticamente al abrir el repo)
- **Docker** (para Testcontainers desde S23 y OWASP ZAP en S44)

### 1. Clonar el Repositorio

```bash
git clone https://github.com/ergrato-dev/bc-testing.git
cd bc-testing
```

### 2. Instalar Extensiones de VS Code

```bash
# Abrir en VS Code
code .

# Las extensiones recomendadas aparecerán automáticamente
# O ejecutar: Ctrl+Shift+P → "Extensions: Show Recommended Extensions"
```

### 3. Navegar a la Semana Actual

```bash
cd bootcamp/week-01-que_es_testing_calidad_de_software
```

### 4. Seguir las Instrucciones

Cada semana contiene un `README.md` con instrucciones detalladas y la distribución de las 10 horas de esa semana.

---

## 📊 Metodología de Aprendizaje

### Estrategias Didácticas

- 🎯 **Aprendizaje Basado en Proyectos (ABP)** — Suite de tests semanal por dominio
- 🔄 **Dominios Únicos** — Cada aprendiz testea su propio dominio de negocio
- 🔁 **TDD como disciplina** — Semanas dedicadas al ciclo Red-Green-Refactor
- 🌐 **Comparación entre lenguajes** — Mismo concepto en JS, Python y Java lado a lado
- 👥 **Code Review entre pares** — Revisión de suites entre estudiantes
- 🎮 **Live Testing** — Sesiones en vivo de testing exploratorio

### Distribución del Tiempo (10h/semana)

- **Teoría**: 2–2.5 horas
- **Prácticas guiadas**: 4–4.5 horas
- **Proyecto semanal**: 3 horas
- **Recursos**: 0.5 horas

### Evaluación

Cada semana incluye tres tipos de evidencias:

1. **Conocimiento 🧠** (30%): Cuestionarios teóricos sobre conceptos de testing
2. **Desempeño 💪** (40%): Ejercicios prácticos con el framework de la semana
3. **Producto 📦** (30%): Suite de tests del proyecto del dominio asignado

**Criterio de aprobación**: Mínimo 70% en cada tipo de evidencia

---

## 🤝 Contribuir

¡Las contribuciones son bienvenidas! Este es un proyecto educativo de código abierto.

### Cómo Contribuir

1. Fork del repositorio
2. Crea tu rama (`git checkout -b feature/nueva-funcionalidad`)
3. Commit con [Conventional Commits](https://www.conventionalcommits.org/) (`git commit -m 'feat: add new exercise'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

### 📋 Áreas de Contribución

- ✨ Ejercicios adicionales (en cualquiera de los 5 lenguajes)
- 📚 Mejoras en documentación
- 🐛 Corrección de errores en código de prácticas
- 🎨 Recursos visuales (diagramas SVG)
- 🌐 Traducciones
- 📹 Videos tutoriales

---

## 📞 Soporte

- 💬 Discussions: [GitHub Discussions](https://github.com/ergrato-dev/bc-testing/discussions)
- 🐛 Issues: [GitHub Issues](https://github.com/ergrato-dev/bc-testing/issues)

---

## 📄 Licencia

Este proyecto está bajo la licencia **CC BY-NC-SA 4.0** (Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International).

- ✅ Puedes compartir y adaptar el material con atribución
- ✅ Se permiten forks educativos
- ❌ No se permite uso comercial
- ❌ Los trabajos derivados deben mantener la misma licencia

Ver el archivo [LICENSE](LICENSE) para más detalles.

---

## 🏆 Agradecimientos

- [Jest](https://jestjs.io/) · [pytest](https://docs.pytest.org/) · [JUnit 6](https://docs.junit.org/6.0.3/overview.html) — Por los frameworks de testing
- [Playwright](https://playwright.dev/) — Por el mejor framework de E2E
- [Testcontainers](https://testcontainers.com/) — Por hacer los tests de integración realistas
- [OWASP](https://owasp.org/) — Por las guías de seguridad
- Comunidad de Testing — Por los recursos, ejemplos y buenas prácticas
- Todos los contribuidores

---

## 📚 Documentación Adicional

- [📋 Plan de Estudios](docs/plan-estudios.md)
- [📖 Guía de Desarrollo de Contenidos](docs/guia-desarrollo-contenidos.md)
- [🧱 Versiones del Stack](docs/stack-versions.md)
- [📐 Decisión de Duración y Solapes](docs/decision-semanas.md)
- [🧵 Proyecto Hilo Conductor](docs/proyecto-hilo-conductor.md)
- [✅ Autograding](docs/autograding.md)
- [🗂️ Índice de Semanas](bootcamp/README.md)
- [🤖 Instrucciones de Copilot](.github/copilot-instructions.md)
- [🐛 Reportar Issue](https://github.com/ergrato-dev/bc-testing/issues)

---

## ⚠️ Exención de Responsabilidad

Este repositorio es un recurso educativo de código abierto provisto **"tal cual"** (_as is_), sin garantías de ningún tipo, ya sean expresas o implícitas.

- El contenido es de carácter **exclusivamente educativo** y no constituye asesoramiento profesional de ningún tipo.
- Los autores y colaboradores **no se responsabilizan** por daños directos, indirectos, incidentales o consecuentes derivados del uso de este material.
- Los ejemplos de código tienen fines didácticos. Su uso en entornos de producción es **responsabilidad exclusiva del usuario**.
- Las herramientas, frameworks y bibliotecas de terceros mencionados están sujetos a sus propias licencias y condiciones de uso.
- Este proyecto no está afiliado ni respaldado oficialmente por ninguna de las herramientas o frameworks referenciados (Jest, pytest, JUnit 6, Playwright, etc.).

Ver el archivo [LICENSE](LICENSE) para los términos de la licencia CC BY-NC-SA 4.0 aplicables a este proyecto.

---

<p align="center">
  <strong>🎓 Bootcamp Testing de Software Zero to Hero</strong><br>
  <em>De cero a QA Engineer / SDET Junior en ~9 meses</em>
</p>

<p align="center">
  <a href="bootcamp/week-01-que_es_testing_calidad_de_software">Comenzar Semana 1</a> •
  <a href="docs/plan-estudios.md">Ver Plan de Estudios</a> •
  <a href="https://github.com/ergrato-dev/bc-testing/issues">Reportar Issue</a>
</p>

<p align="center">
  Hecho con ❤️ para la comunidad de testers y desarrolladores
</p>
