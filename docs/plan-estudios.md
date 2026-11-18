# 📅 Plan de Estudios — Bootcamp Testing Zero to Hero

**Duración total**: 46 semanas (~11,5 meses)
**Horas semanales**: 10 horas
**Horas totales**: ~460 horas
**Nivel de entrada**: Cero (sin experiencia previa en testing)
**Nivel de salida**: QA Engineer / SDET Junior
**Enfoque**: Testing de software con JavaScript, Python, Java, Go y Rust — desde fundamentos absolutos hasta automatización avanzada, TDD, BDD y pruebas de performance

---

## 🗂️ Orden de Creación de Cada Semana

El instructor debe seguir este orden al desarrollar el contenido de cada semana:

1. `README.md` — Descripción general, objetivos, navegación
2. `rubrica-evaluacion.md` — Criterios y rúbrica de evaluación
3. `1-teoria/` — Archivos de teoría numerados (`01-`, `02-`, …)
4. `0-assets/` — Diagramas SVG de apoyo visual vinculados a la teoría
   - Tema dark
   - Sin degradés (solo colores sólidos)
   - Fuentes sans-serif (Inter, Roboto, Open Sans o System UI)
   - Paleta base: `#3572A5` (azul Python), `#F89820` (naranja Java), `#F7DF1E` (amarillo JS), `#00ADD8` (cian Go), `#DEA584` (cobre Rust), fondos `#0f172a` / `#1e293b`
5. `2-practicas/` — Ejercicios guiados (starter + solution por ejercicio)
6. `3-proyecto/` — Proyecto integrador semanal (único entregable)
7. `4-recursos/` — Ebooks, videografía, webgrafía
8. `5-glosario/README.md` — Términos clave de la semana (A–Z)

---

## 🎯 Distribución por Etapas

### 🟥 Etapa 0: Fundamentos de Testing (Semanas 1–6)

60 horas totales — Mentalidad tester, terminología, tipos de prueba, primeros tests manuales y automatizados en JavaScript, Python y Java (Go y Rust arrancan desde cero en sus propias etapas).

### 🟦 Etapa 1: Testing con JavaScript (Semanas 7–15)

90 horas totales — Jest, testing unitario, mocks/stubs/spies, testing asíncrono, coverage, TDD y testing de APIs REST con Supertest.

### 🟩 Etapa 2: Testing con Python (Semanas 16–24)

90 horas totales — pytest, unittest, fixtures, parametrize, mocking con `unittest.mock`, testing de APIs con `httpx2` (y su equivalente en `requests`), BDD con Behave.

### 🟧 Etapa 3: Testing con Java (Semanas 25–31)

70 horas totales — JUnit 6, Mockito, AssertJ, testing de Spring Boot con MockMvc, Testcontainers y BDD con Cucumber.

### 🔷 Etapa 4: Testing con Go (Semanas 32–36)

50 horas totales — `go test` y el paquete `testing`, table-driven tests, dobles de prueba con interfaces, `httptest`, testcontainers-go, race detector, fuzzing nativo y benchmarks.

### 🟫 Etapa 5: Testing con Rust (Semanas 37–41)

50 horas totales — `cargo test`, tests unitarios/integración/doctests, traits y `mockall`, `rstest`, testing async con Tokio y axum, `proptest`, `cargo-fuzz`, `criterion` y coverage con `cargo-llvm-cov`.

### 🟪 Etapa 6: Testing Avanzado Transversal (Semanas 42–46)

50 horas totales — E2E con Playwright (JS), performance con k6/Locust, seguridad básica y auditoría de dependencias, estrategias CI/CD, Proyecto Final integrador multilenguaje.

---

## 📚 Contenido Semanal Detallado

> Desde S23 cada proyecto semanal es un incremento del [proyecto hilo conductor](proyecto-hilo-conductor.md) del aprendiz, con autograding por `checks.json` ([autograding.md](autograding.md)). Versiones en [stack-versions.md](stack-versions.md); justificación de duración y solapes en [decision-semanas.md](decision-semanas.md).

---

### 🟥 ETAPA 0: FUNDAMENTOS DE TESTING (Semanas 1–6)

> El estudiante no tiene experiencia en testing. Se construye la mentalidad de calidad antes de cualquier framework. Se trabaja en los tres lenguajes en paralelo a nivel básico para que el estudiante comprenda las diferencias desde el principio.

---

#### Week 01: ¿Qué es Testing? Calidad de Software

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- ¿Qué es la calidad del software? Definición y consecuencias de bugs
- El rol del tester vs desarrollador vs SDET
- Tipos de testing: manual vs automatizado
- Niveles de testing: unitario, integración, sistema, aceptación
- La pirámide de pruebas (Testing Pyramid)
- El ciclo de vida de un bug: reporte, reproducción, corrección, verificación
- Testing como actividad preventiva vs correctiva
- Introducción a los tres entornos: Node.js + Python + JDK

**Proyecto**: `reporte-bug.md` — Escribir un reporte de bug profesional (título, pasos para reproducir, resultado esperado vs obtenido, severidad, prioridad) sobre un software real elegido libremente.

---

#### Week 02: Casos de Prueba y Técnicas de Diseño

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- ¿Qué es un caso de prueba? Estructura: precondiciones, pasos, resultado esperado
- Partición de equivalencia
- Análisis de valores límite (Boundary Value Analysis)
- Tablas de decisión
- Testing exploratorio vs scripted
- Cobertura de pruebas: qué significa y qué NO significa
- Smoke tests vs regression tests vs sanity tests
- Herramientas de gestión: Notion, TestRail, Zephyr (conceptual)

**Proyecto**: `casos-de-prueba.md` — Diseñar una suite completa de casos de prueba manuales para una funcionalidad del dominio asignado (login, carrito, reserva, etc.) aplicando partición de equivalencia y valores límite.

---

#### Week 03: Primeros Tests Automatizados — JavaScript

**Horas**: 10h (2h teoría + 4.5h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Configuración del entorno: Node.js 22, pnpm, Jest
- Estructura de un test: `describe`, `test/it`, `expect`
- Matchers básicos: `toBe`, `toEqual`, `toBeTruthy`, `toBeFalsy`, `toBeNull`
- AAA Pattern: Arrange, Act, Assert
- ¿Qué es un assertion? Diferencia entre `==` y aserciones
- Ejecutar tests: `pnpm test`, watch mode
- Leer resultados: test pass, test fail, error vs failure
- Primer test que falla a propósito (red → green)

**Proyecto**: Suite de tests unitarios básicos en JavaScript para funciones puras del dominio asignado (suma, validación, formateo de datos).

---

#### Week 04: Primeros Tests Automatizados — Python

**Horas**: 10h (2h teoría + 4.5h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Configuración del entorno: Python 3.14 y uv (único gestor), pytest 9.1.1
- Estructura de un test en pytest: convenciones de nombres (`test_*.py`, `def test_*`)
- Assertions nativas con `assert`
- Comparación pytest vs unittest
- Ejecutar tests: `pytest`, `pytest -v`, `pytest -k`
- Matchers con pytest: `==`, `!=`, `in`, `not in`, `is None`
- Leer reportes de pytest
- Configuración básica con `pytest.ini` / `pyproject.toml`

**Proyecto**: Suite de tests en Python para las mismas funciones del dominio asignado ya testeadas en JS (semana 3) — comparar cómo se expresan las mismas intenciones en ambos lenguajes.

---

#### Week 05: Primeros Tests Automatizados — Java

**Horas**: 10h (2h teoría + 4.5h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Configuración del entorno: JDK 21 LTS, Maven/Gradle, JUnit 6
- Estructura de un test en JUnit 6: `@Test`, `@DisplayName`, `Assertions.*`
- `assertEquals`, `assertTrue`, `assertNotNull`, `assertThrows`
- Ciclo de vida: `@BeforeEach`, `@AfterEach`, `@BeforeAll`, `@AfterAll`
- Diferencias entre JUnit 4 y JUnit Jupiter (JUnit 5 y 6)
- Ejecutar tests con Maven: `mvn test`
- Leer reportes en consola y en Surefire
- Agregar AssertJ como librería de aserciones fluente

**Proyecto**: Suite de tests en Java para las mismas funciones del dominio asignado — comparar con JS (S03) y Python (S04). Análisis de similitudes y diferencias entre los tres lenguajes.

---

#### Week 06: Proyecto Integrador — Etapa 0

**Horas**: 10h (1h revisión + 2h planning + 6.5h desarrollo + 0.5h presentación)

**Temas**:

- Revisión de Semanas 1–5
- Estrategia de testing para un sistema pequeño
- Mapear qué tipos de pruebas aplican a cada capa
- Documentar plan de pruebas (test plan básico): alcance, enfoque, recursos
- Integración de las tres suites: JS, Python, Java sobre el mismo dominio

**Proyecto**: Test Plan + Suite de pruebas básica en los tres lenguajes para el dominio asignado. Documento `test-plan.md` con estrategia, scope y cobertura planificada.

---

### 🟦 ETAPA 1: TESTING CON JAVASCRIPT (Semanas 7–15)

> El estudiante ya conoce Jest básico. Se profundiza en el ecosistema de testing JavaScript moderno: mocks, stubs, spies, testing asíncrono, coverage, TDD y testing de APIs REST.

---

> ⚠️ **Nota de trazabilidad**: las Semanas 7-18 fueron desarrolladas reordenando algunos temas respecto al borrador original de este plan (p. ej. Mocks se movió de S08 a S10, Coverage de S10 a S14, y Python ganó una semana de fundamentos en S16 antes de Parametrización/Marks y Mocking). Los bloques de abajo reflejan el contenido **tal como fue publicado** — ver el `README.md` de cada semana como fuente definitiva. Las semanas aún no publicadas (ver Estado de Avance) su numeración/orden puede ajustarse igual durante el desarrollo.

#### Week 07: Jest Avanzado — Organización de Suites

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Organización de suites con `describe` anidados por comportamiento
- Hooks de ciclo de vida: `beforeAll`, `beforeEach`, `afterEach`, `afterAll`
- Introducción a `mock`, `stub` y `spy` en escenarios básicos de aislamiento
- Patrón AAA (Arrange-Act-Assert) y nombres de test profesionales
- Ejecución de tests por archivo y por patrón con `pnpm`
- Base de trabajo para mocking avanzado y asincronía (S09-S10)

**Proyecto**: *Suite Modular con Jest* — refactorización de la suite JS de Etapa 0 organizada con `describe` anidados y hooks de ciclo de vida.

---

#### Week 08: Matchers Avanzados y Parametrización con Jest

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Matchers avanzados de Jest según tipo de dato y comportamiento esperado
- Tests parametrizados con `test.each()` para reducir duplicación
- Legibilidad de assertions complejas en objetos, arrays y errores
- Patrón AAA aplicado a suites con múltiples casos
- Análisis de fallos de assertions para depurar más rápido
- Base robusta para testing asíncrono y mocking avanzado

**Proyecto**: *Suite Parametrizada con Jest* — casos parametrizados con `test.each()` sobre la suite del dominio asignado.

---

#### Week 09: Testing Asíncrono en Jest

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Tests para funciones asíncronas con `async/await` y Promises
- Validación de errores asíncronos con `.rejects` y `toThrow`
- Fake timers en escenarios de retry y debounce
- Falsos positivos por no esperar la finalización de tareas asíncronas
- Suites asíncronas legibles siguiendo patrón AAA
- Base para integrar mocks avanzados y APIs en semanas siguientes

**Proyecto**: *Suite Asíncrona de ItemService* — tests para módulos asíncronos del dominio asignado (fetching, timeouts, retries).

---

#### Week 10: Mocks Avanzados en Jest

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Test doubles: diferencia entre `mock`, `stub` y `spy` en escenarios reales
- `jest.fn()` y `jest.spyOn()` para controlar dependencias sin romper el objetivo del test
- `jest.mock()` para simular colaboraciones externas (módulos completos)
- Verificar interacciones: llamadas, argumentos, orden, cantidad
- Antipatrones de sobre-mocking que generan tests frágiles
- Suites aisladas y legibles bajo patrón AAA para servicios con dependencias

**Proyecto**: *Suite con Dependencias Aisladas* — mocks para aislar dependencias externas del dominio asignado (BD, APIs externas, notificaciones).

---

#### Week 11: TDD con JavaScript — Red, Green, Refactor

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- El ciclo Red → Green → Refactor de forma disciplinada con Jest
- Tests pequeños que expresan comportamiento antes de implementar
- Implementar solo el código mínimo para pasar cada test
- Refactor seguro usando la suite como red de protección
- Antipatrones de TDD: tests frágiles, sobreespecificación, saltarse Red
- Diseño emergente guiado por casos de prueba incrementales

**Proyecto**: *TDD Incremental en Servicio de Dominio* — módulo complejo del dominio asignado implementado con TDD estricto, commits por ciclo Red-Green-Refactor.

---

#### Week 12: Testing de APIs REST con Supertest

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Tests de integración para endpoints REST con Supertest
- Validación de códigos de estado HTTP y estructura de respuesta JSON
- Flujos GET, POST y validaciones de payload
- Casos de error esperados (400, 404, 409, 500)
- Aislamiento entre capas usando repositorios in-memory
- Suites legibles y repetibles bajo patrón AAA

**Proyecto**: *API REST del Dominio Asignado* — suite de integration tests con Supertest, CRUD y casos de error.

---

#### Week 13: Snapshot y Property-Based Testing

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Snapshot testing con criterio para detectar cambios no esperados
- Cómo evitar snapshots frágiles enfocándose en salidas relevantes
- Propiedades invariantes para funciones de negocio
- Pruebas property-based con `fast-check`
- Interpretar contraejemplos y simplificar casos fallidos
- Combinar tests de ejemplo + propiedades para mayor confianza

**Proyecto**: *Suite Combinada de Snapshots y Propiedades* — snapshots justificados + property-based tests con fast-check.

---

#### Week 14: Coverage y Calidad de Suites en Jest

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Ejecutar cobertura en Jest y leer métricas: `statements`, `branches`, `functions`, `lines`
- Cobertura alta vs. cobertura útil para el negocio
- Detectar tests frágiles, redundantes o con bajo poder de detección
- Priorizar mejoras de suite usando riesgo funcional, no solo porcentaje global
- Plan incremental para mantener cobertura saludable en CI

**Proyecto**: *Hardening de Suite con Coverage y Calidad* — suite JS del dominio asignado con cobertura auditada y mejorada.

---

#### Week 15: Proyecto Integrador — Etapa 1 (JavaScript)

**Horas**: 10h (0.5h teoría + 2.5h planning + 6.5h desarrollo + 0.5h presentación)

**Temas**:

- Integración en una sola suite de los enfoques de la etapa JS (unit, integration, snapshot, properties)
- Estrategia de calidad por capas y riesgo funcional
- Plantilla mínima de GitHub Actions para ejecutar tests y coverage
- Quality gate básico con SonarQube (Cloud free tier para públicos / Community Edition para privados)

**Proyecto**: *Cierre de Etapa JS con Quality Gate* — suite completa de tests JS para el dominio asignado con CI y quality gate.

---

### 🟩 ETAPA 2: TESTING CON PYTHON (Semanas 16–24)

> Se introduce pytest en profundidad, fixtures, parametrize, mocking, testing de APIs con `httpx2`, y BDD con Behave. El estudiante aplica los mismos conceptos del Etapa 1 al ecosistema Python.

---

#### Week 16: Fundamentos con pytest

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Repaso breve de la semana 04 (setup con `uv`, AAA, nombres, `pytest.raises`, filtros)
- Resultados de pytest: `passed`, `failed`, `error`, `skipped`, `xfailed`, `xpassed` (failed vs error de setup/teardown)
- Configuración en `[tool.pytest]` de `pyproject.toml` (`pythonpath`, `testpaths`, `addopts`) e inspección con `--setup-show` y `--fixtures`
- Fixtures con `yield` (setup y teardown), scopes `function`/`class`/`module`/`package`/`session` y composición de fixtures
- `conftest.py` compartido entre archivos y `autouse` con criterio
- Fixtures integradas: `tmp_path`, `monkeypatch` (variables de entorno y atributos) y `capsys`

**Proyecto**: *Suite Python con fixtures para el dominio asignado* — suite pytest del dominio con `conftest.py` compartido, fixtures `yield` con teardown, `tmp_path` y `monkeypatch`.

---

#### Week 17: Parametrización y Marks con pytest

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- `@pytest.mark.parametrize` para cubrir múltiples casos sin duplicación
- Tablas de casos: entradas válidas, valores límite y errores
- Marks (`smoke`, `regression`, `slow`) para organizar la suite
- Ejecutar subconjuntos de pruebas con `-m` y `-k`
- Trazabilidad de calidad con suites segmentadas por riesgo

**Proyecto**: *Suite Python Segmentada por Riesgo* — suite parametrizada y organizada con marks sobre el dominio asignado.

---

#### Week 18: Mocking con unittest.mock y pytest-mock

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Distinguir `mock`, `stub` y `spy` según el objetivo de la prueba
- `patch` en el target correcto para aislar dependencias
- Configurar `return_value`, `side_effect` y verificaciones de llamadas
- `pytest-mock` para mejorar la legibilidad de tests con dobles
- Evitar mocks frágiles que rompen por detalles de implementación

**Proyecto**: *Suite Python con Estrategia de Mocking* — mocks para aislar todas las dependencias externas del dominio asignado en Python (BD, servicios HTTP, sistema de archivos).

---

#### Week 19: Testing de APIs con Python (httpx2)

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Niveles para aislar HTTP: parchear la función, transporte simulado, servidor local (`pytest-httpserver`) y API real
- Testing de clientes `httpx2` con `MockTransport` (sin red); equivalencia con `requests` + `responses`
- Verificar la request enviada: ruta, query params, cuerpo JSON y autenticación (Bearer Token, Basic Auth)
- Traducción de errores HTTP (4xx, 5xx con cuerpo HTML) y timeouts a excepciones del dominio
- Validación de contratos con `pydantic` (`jsonschema` como alternativa)
- Fixtures para el cliente HTTP y las requests registradas
- Testing asíncrono: `httpx2.AsyncClient`, `pytest-asyncio` (modo `strict` vs `auto`), fixtures asíncronas y `AsyncMock`
- Contract testing con Pact (introducción conceptual)

> `respx` se descartó: no intercepta `httpx2`, el sucesor de `httpx` que usa `bc-testing-adso`. `MockTransport` viene incluido en `httpx2`.

**Proyecto**: Suite de tests del cliente HTTP del API del dominio asignado con `httpx2` + `MockTransport`: happy path con verificación de la request, 404, 422, 5xx, timeout y contrato validado con `pydantic`.

---

#### Week 20: TDD con Python

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- El ciclo Red → Green → Refactor en Python con pytest; qué es un Red válido
- TDD en Python: diferencias culturales con JS/Java
- Kata de TDD en Python: Roman Numerals (práctica) y Bowling Game (kata extra)
- Diseño emergente con TDD en Python: dataclasses, protocols y fakes
- Testing con tipos: `mypy --strict` en el ciclo TDD
- `hypothesis` — property-based testing en Python: estrategias, shrinking, `@example`
- Integración de TDD con fixtures de pytest (y su choque con `@given`)
- TDD en código legado: tests de caracterización y seams

**Proyecto**: Implementación TDD de un servicio del dominio asignado en Python. Evidencia de al menos 5 ciclos R-G-R, al menos 15 tests, 2 property-based tests con `hypothesis` y `mypy --strict` en verde.

---

#### Week 21: Coverage y Calidad en Python

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- `pytest-cov`: configuración en `pyproject.toml` y ejecución
- Reportes: terminal, HTML, XML (para CI)
- Branch coverage en Python: `branch = true` y lectura de `a->b`
- Excluir código del coverage: `# pragma: no cover` y `exclude_also`
- `fail_under` como umbral que falla la suite
- `coverage combine` (y `--cov-append`) para varias ejecuciones
- Mutation testing con `mutmut` (en Windows, dentro de WSL): mutantes matables y equivalentes
- SonarQube para Python: `coverage.xml` con `relative_files`, `sonar-project.properties` y workflow con `setup-uv` (SonarQube Cloud free tier en repos públicos)
- Alternativa para repos privados: SonarQube Community Edition autohospedado (opcional)
- Definir umbrales de calidad: coverage (`fail_under`), complejidad ciclomática (`ruff` C901) y duplicados (quality gate)

**Proyecto**: Llevar una suite heredada a 90%+ de branch coverage, ejecutar `mutmut` y clasificar los mutantes vivos, refactorizar hasta cumplir el umbral de complejidad. Documentar `quality-report-python.md`.

---

#### Week 22: BDD con Python — Behave

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- ¿Qué es BDD? Behavior Driven Development, tres amigos y ejemplos acordados
- Gherkin: `Feature`, `Scenario`, `Given`, `When`, `Then`, `And`, `But`, `Background` (en español con `# language: es`)
- Escenarios declarativos frente a imperativos
- Behave: instalación, estructura de proyecto (`features/`, `steps/`)
- Escribir step definitions en Python con parámetros tipados
- `context` object en Behave, tablas de datos y tags
- Scenario Outline con `Examples`
- Hooks: `before_scenario`, `after_scenario`, `before_feature` (y el alcance del estado)
- Integrar Behave con pytest (`pytest-bdd` como alternativa)

**Proyecto**: Suite BDD con Behave para el flujo de negocio principal del dominio asignado: al menos 3 features con múltiples scenarios y Scenario Outline.

---

#### Week 23: Testing de Bases de Datos en Python

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Estrategias para testear con BD: mock frente a PostgreSQL real, y por qué no SQLite en memoria
- Testcontainers en Python con PostgreSQL real desde el primer lab, con SQLAlchemy 2.0 (estilo `select()`)
- Fixtures de BD: crear esquema, poblar datos, limpiar (scope `session` vs `function`)
- Rollback por test: conexión con transacción externa + `join_transaction_mode="create_savepoint"`
- Faker para datos de prueba realistas
- Factory Boy para objetos de prueba complejos
- Testing de migraciones (Alembic)

**Proyecto**: Suite de tests para la capa de acceso a datos del dominio asignado: CRUD completo con SQLAlchemy, rollback por test y Factory Boy para datos de prueba; la suite corre contra PostgreSQL con Testcontainers.

---

#### Week 24: Proyecto Integrador — Etapa 2 (Python)

**Horas**: 10h (0.5h teoría + 2.5h planning + 6.5h desarrollo + 0.5h presentación)

**Temas**:

- Integración de Semanas 16–23
- Arquitectura: unitarios (pytest) + integración (httpx2) + BDD (Behave) + BD
- Coverage ≥85% en toda la suite Python
- Reporte de testing con análisis de calidad

**Proyecto**: Suite completa Python del dominio asignado: pytest + fixtures + mocks + async + BDD + BD. Coverage ≥85%, reporte `testing-report-python.md`.

---

### 🟧 ETAPA 3: TESTING CON JAVA (Semanas 25–31)

> Se profundiza en JUnit 6 y el ecosistema Java de testing: Mockito, AssertJ, testing de Spring Boot, Testcontainers y BDD con Cucumber.

---

#### Week 25: JUnit 6 Avanzado y AssertJ

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- JUnit 6 en profundidad: `@ParameterizedTest`, `@MethodSource`, `@CsvSource`, `@ValueSource`
- `@Nested` para organizar tests por contexto
- `@Tag` y filtrado de tests
- `@TempDir`, `@RegisterExtension`
- JUnit 6 Extensions: `@ExtendWith`
- AssertJ: aserciones fluentes y legibles
- `assertThat().isEqualTo()`, `containsExactly()`, `satisfies()`
- Soft assertions con `SoftAssertions`

**Proyecto**: Refactorización de la suite Java de Etapa 0 usando `@ParameterizedTest`, `@Nested` y AssertJ. Añadir soft assertions para escenarios con múltiples validaciones.

---

#### Week 26: Mockito — Mocking en Java

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Mockito: `@Mock`, `@InjectMocks`, `@Spy`, `@Captor`
- `when().thenReturn()`, `when().thenThrow()`, `doReturn()`
- `verify()`: verificar interacciones
- `ArgumentCaptor`: capturar argumentos de llamadas
- `@MockitoExtension` con JUnit 6
- Mockito strict stubbing: detectar stubs no utilizados
- Mockear métodos estáticos con `mockStatic()` dentro de try-with-resources (inline mock maker, por defecto en Mockito 5) y por qué suele indicar un problema de diseño
- Diferencia entre `@Mock` y `@Spy`
- BDDMockito: `given().willReturn()` y `then().should()`

**Proyecto**: Suite con Mockito para aislar todas las dependencias del dominio asignado en Java: repositorios, servicios externos, envío de notificaciones.

---

#### Week 27: TDD con Java

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- El ciclo Red → Green → Refactor en Java con JUnit 6
- TDD y diseño: cómo emerge el diseño OOP desde los tests
- Kata de TDD en Java: Prime Factors, Game of Life
- Outside-in TDD con Mockito
- TDD con interfaces: programar contra abstracciones
- Architectural fitness functions con ArchUnit
- Refactoring seguro con tests como red de seguridad
- Integración con IntelliJ IDEA: shortcuts para TDD

**Proyecto**: Implementación TDD de un servicio complejo del dominio asignado en Java. Commits por ciclo R-G-R, al menos 15 tests, interfaces y mocks con Mockito.

---

#### Week 28: Testing de Spring Boot

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Spring Boot Test: `@SpringBootTest`, `@WebMvcTest`, `@DataJpaTest`
- MockMvc y `MockMvcTester` (aserciones AssertJ): testear controladores REST sin levantar servidor
- `@MockitoBean` y `@MockitoSpyBean` (reemplazan a `@MockBean`/`@SpyBean`, eliminados en Spring Boot 4) y su diferencia con `@Mock`
- Starters de test modulares de Spring Boot 4 (un starter de test por tecnología)
- Testing del contexto de Spring: `@ActiveProfiles("test")`
- Test slices: `@JsonTest`, `@RestClientTest`
- Configuración de BD en tests: H2 in-memory
- `RestTestClient` / `TestRestTemplate` para tests de integración completos
- `@Transactional` en tests: rollback automático

**Proyecto**: Suite de tests para la API Spring Boot del dominio asignado: `@WebMvcTest` para controladores, `@DataJpaTest` para repositorios, `@SpringBootTest` para integración.

---

#### Week 29: Testcontainers en Java

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- ¿Qué es Testcontainers? Contenedores Docker en tests
- Testcontainers con JUnit 6: `@Testcontainers`, `@Container`
- PostgreSQL container: versión fijada igual que en producción
- Inicialización de schemas: `init.sql`, Flyway, Liquibase
- Kafka container para tests de mensajería
- `GenericContainer` para servicios arbitrarios
- `@ServiceConnection` (Spring Boot): conectar el contenedor sin `@DynamicPropertySource`
- Compartir containers entre clases: patrón singleton container; `withReuse(true)` solo en local
- Optimización de tiempos: `@Container` static vs instance

**Proyecto**: Migrar los tests de BD del dominio asignado de H2 a PostgreSQL real con Testcontainers. Demostrar paridad entre entorno de test y producción.

---

#### Week 30: BDD con Java — Cucumber

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Cucumber para Java: dependencias Maven/Gradle
- Gherkin en Java: `@Given`, `@When`, `@Then` con expresiones
- `DataTable` para datos tabulares en steps
- Estado compartido entre steps por inyección de dependencias (`cucumber-spring`, beans con scope de escenario)
- Cucumber con Spring Boot: `@CucumberContextConfiguration`
- Reportes HTML con el plugin nativo `html:` (y `json:` para herramientas externas)
- Ejecutar con JUnit Platform: `cucumber-junit-platform-engine` + `@Suite` + `@SelectClasspathResource`, en Surefire/Failsafe
- Living documentation: features como documentación ejecutable

**Proyecto**: Suite BDD con Cucumber para el flujo de negocio principal del dominio asignado en Java: al menos 3 features integradas con Spring Boot.

---

#### Week 31: Proyecto Integrador — Etapa 3 (Java)

**Horas**: 10h (0.5h teoría + 2.5h planning + 6.5h desarrollo + 0.5h presentación)

**Temas**:

- Integración de Semanas 25–30
- Arquitectura de testing Java: unit + integration (Spring) + BDD (Cucumber) + Testcontainers
- Coverage con JaCoCo ≥85%
- Reporte de testing con análisis de calidad

**Proyecto**: Suite completa Java del dominio asignado: JUnit 6 + AssertJ + Mockito + Spring Boot + Testcontainers + Cucumber. JaCoCo ≥85%, reporte `testing-report-java.md`.

---

### 🔷 ETAPA 4: TESTING CON GO (Semanas 32–36)

> El estudiante ya domina los conceptos de testing en tres lenguajes. Go no aparece en la Etapa 0, así que la S32 hace de "primeros tests" y compara cada idea con Jest, pytest y JUnit. Go trae el testing en la librería estándar: se prioriza `testing` + `go test` y solo se añaden librerías cuando aportan algo que la stdlib no da. BDD no se repite (ya visto con Behave y Cucumber); godog queda como lectura en recursos.

---

#### Week 32: Fundamentos de Testing en Go

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Módulos Go y layout: `go.mod`, archivos `_test.go`, `go test ./...`
- El paquete `testing`: `func TestXxx(t *testing.T)`, `t.Errorf` vs `t.Fatalf`
- Table-driven tests: el patrón idiomático de parametrización en Go
- Subtests con `t.Run` y filtrado con `go test -run 'TestX/caso'`
- `t.Helper`, `t.Cleanup`, `t.TempDir`, `t.Context`
- Tests en paralelo con `t.Parallel()` y la captura de variables de bucle (Go 1.22+)
- Caja blanca (`package x`) vs caja negra (`package x_test`)
- Example tests (`func ExampleXxx`) como documentación verificada
- `go vet` y `go test -v`, `-count=1`, `-shuffle=on`

**Proyecto**: Portar a Go las funciones del dominio asignado y cubrirlas con table-driven tests y subtests. Comparar en `comparativa.md` la misma suite con Jest, pytest y JUnit.

---

#### Week 33: Interfaces y Dobles de Prueba en Go

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Inyección de dependencias con interfaces pequeñas definidas del lado del consumidor
- Fakes y stubs escritos a mano: por qué en Go suelen bastar
- `go.uber.org/mock` (mockgen): mocks generados, `EXPECT()`, `gomock.Any()`, `Times()`
- testify: `assert` vs `require`, y `testify/mock` como alternativa
- Comparar estructuras con `github.com/google/go-cmp/cmp` (`cmp.Diff`)
- Golden files en `testdata/` con flag `-update`
- Inyección del reloj y de la aleatoriedad para tests deterministas

**Proyecto**: Aislar con interfaces las dependencias del servicio principal del dominio asignado (repositorio, notificador, reloj). Al menos un fake manual y un mock generado con mockgen, justificando cuándo usar cada uno.

---

#### Week 34: HTTP, BD e Integración en Go

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- `net/http/httptest`: `NewRecorder` para handlers y `NewServer` para clientes HTTP
- Testear handlers con el router estándar (`http.ServeMux` con patrones de método, Go 1.22+)
- Validar JSON de respuesta: `encoding/json` + `cmp.Diff`
- `TestMain` para preparar y liberar recursos compartidos
- Separar integración: build tags `//go:build integration` y `testing.Short()`
- testcontainers-go: módulo PostgreSQL, migraciones y limpieza con `t.Cleanup`
- Transacción con rollback por test vs base de datos por paquete

**Proyecto**: API REST del dominio asignado en Go: tests de handlers con `httptest` y tests de repositorio contra PostgreSQL real con testcontainers-go, ejecutables por separado con `-tags integration`.

---

#### Week 35: Concurrencia y Calidad en Go

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Race detector: `go test -race` y cómo leer su reporte
- `testing/synctest`: tiempo virtual para tests de código concurrente
- Fuzzing nativo: `func FuzzXxx(f *testing.F)`, corpus semilla y `testdata/fuzz/`
- Benchmarks: `func BenchmarkXxx(b *testing.B)` con `b.Loop()`, `-benchmem`, `benchstat`
- Coverage: `-coverprofile`, `-covermode=atomic`, `-coverpkg`, `go tool cover -html`
- Coverage de binarios e integración con `go build -cover` (`GOCOVERDIR`)
- Linters de calidad: `staticcheck` / `golangci-lint`

**Proyecto**: Añadir a la suite Go del dominio asignado un fuzz test que encuentre al menos un caso límite, un benchmark comparado con `benchstat` antes/después de una optimización y un test concurrente limpio bajo `-race`.

---

#### Week 36: Proyecto Integrador — Etapa 4 (Go)

**Horas**: 10h (0.5h teoría + 2.5h planning + 6.5h desarrollo + 0.5h presentación)

**Temas**:

- Integración de Semanas 32–35
- Arquitectura de testing en Go: unitarios table-driven + dobles + `httptest` + testcontainers-go
- Coverage ≥85% con `go test -coverprofile`
- Reporte de testing con análisis de calidad

**Proyecto**: Suite completa Go del dominio asignado: unitarios, mocks, HTTP, BD real, fuzz y benchmarks, limpia bajo `-race`. Coverage ≥85%, reporte `testing-report-go.md`.

---

### 🟫 ETAPA 5: TESTING CON RUST (Semanas 37–41)

> Rust integra el testing en `cargo` y en el propio lenguaje. El compilador ya elimina clases enteras de errores, así que la etapa se centra en lo que el tipo no garantiza: lógica de negocio, async, I/O y propiedades. Como en Go, la S37 arranca desde cero y compara con los lenguajes anteriores. BDD no se repite; el crate `cucumber` queda como lectura en recursos.

---

#### Week 37: Fundamentos de Testing en Rust

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- `cargo new`, `cargo test` y los tres tipos de test: unitarios, integración (`tests/`) y doctests
- `#[test]` y el módulo `#[cfg(test)] mod tests` junto al código
- `assert!`, `assert_eq!`, `assert_ne!` con mensajes personalizados
- `#[should_panic(expected = "...")]` y tests que devuelven `Result<(), E>`
- `#[ignore]`, filtros por nombre, `cargo test -- --nocapture`, `--test-threads`
- Testear funciones privadas: por qué es posible en Rust y cuándo evitarlo
- `pretty_assertions` para diffs legibles

**Proyecto**: Portar a Rust las funciones del dominio asignado con tests unitarios, al menos un test de integración en `tests/` y doctests en la API pública. Comparar con la suite Go de la Etapa 4.

---

#### Week 38: Traits, Dobles y Parametrización en Rust

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Inyección de dependencias con traits: genéricos (`impl Trait`) vs `dyn Trait`
- Fakes manuales implementando el trait
- `mockall`: `#[automock]`, `expect_*()`, `returning`, `times`, `with(predicate::eq(...))`
- `rstest`: fixtures y parametrización con `#[case]` y `#[values]`
- Snapshot testing con `insta` y revisión con `cargo insta review`
- Organización: módulos de soporte compartidos en `tests/common/`

**Proyecto**: Aislar con traits las dependencias del servicio principal del dominio asignado. Suite con fakes, al menos un mock con `mockall`, casos parametrizados con `rstest` y un snapshot con `insta`.

---

#### Week 39: Async, APIs y BD en Rust

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Tests async con `#[tokio::test]` (runtime y `flavor = "multi_thread"`)
- APIs con axum: testear el `Router` sin abrir puerto con `tower::ServiceExt::oneshot`
- Clientes HTTP (`reqwest`) contra un servidor simulado con `wiremock`
- `#[sqlx::test]`: base de datos aislada y migraciones por test
- testcontainers-rs con el módulo PostgreSQL
- Tiempo controlado en async: `tokio::time::pause` y `advance`

**Proyecto**: API REST del dominio asignado en Rust con axum: tests de rutas con `oneshot`, tests de repositorio con `#[sqlx::test]` sobre PostgreSQL y un cliente externo simulado con `wiremock`.

---

#### Week 40: Calidad Avanzada en Rust

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- Property-based testing con `proptest`: estrategias y shrinking
- Fuzzing con `cargo-fuzz` (requiere nightly; Linux o WSL)
- Benchmarks con `criterion`
- Coverage con `cargo-llvm-cov` (líneas y ramas, reporte HTML)
- `cargo-nextest`: runner más rápido y con mejor salida
- Mutation testing con `cargo-mutants`
- `cargo clippy` como red previa a los tests

**Proyecto**: Añadir a la suite Rust del dominio asignado propiedades con `proptest`, un benchmark con `criterion` y un reporte de `cargo-mutants` con los mutantes supervivientes analizados.

---

#### Week 41: Proyecto Integrador — Etapa 5 (Rust)

**Horas**: 10h (0.5h teoría + 2.5h planning + 6.5h desarrollo + 0.5h presentación)

**Temas**:

- Integración de Semanas 37–40
- Arquitectura de testing en Rust: unitarios + integración + doctests + async + BD
- Coverage ≥85% con `cargo-llvm-cov`
- Reporte de testing con análisis de calidad

**Proyecto**: Suite completa Rust del dominio asignado: unitarios, `mockall`, `rstest`, axum, `#[sqlx::test]`, `proptest` y benchmarks. Coverage ≥85%, reporte `testing-report-rust.md`.

---

### 🟪 ETAPA 6: TESTING AVANZADO TRANSVERSAL (Semanas 42–46)

> El estudiante domina testing unitario e integración en los cinco lenguajes. La etapa final cubre E2E, performance, seguridad, CI/CD y el proyecto final integrador multilenguaje.

---

#### Week 42: Testing End-to-End con Playwright

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- ¿Qué es E2E testing? Cuándo usar E2E vs unitario vs integración
- Playwright: instalación y configuración (`@playwright/test`)
- Locators: `getByRole`, `getByText`, `getByTestId`, `locator()`
- Actions: `click`, `fill`, `select`, `check`, `hover`
- Assertions: `expect(locator).toBeVisible()`, `toHaveText()`, `toHaveURL()`
- Page Object Model (POM) en Playwright
- Screenshots, videos y traces ante fallos
- Playwright en múltiples browsers (chromium, firefox, webkit)

**Proyecto**: Suite E2E con Playwright para el flujo principal del dominio asignado: al menos 10 tests en POM, con screenshots de evidencia y reporte HTML.

---

#### Week 43: Testing de Performance con k6 y Locust

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- ¿Qué es performance testing? Load, stress, spike, soak, volume
- Métricas clave: RPS, latencia (p50/p90/p99), error rate, throughput
- k6 (JavaScript): scripts, thresholds, stages, `check()`
- Interpretar resultados de k6: métricas y reporte
- Locust (Python): `HttpUser`, `@task`, `wait_time` (`TaskSet` solo como legado)
- Microbenchmarks (Go `b.Loop()`, Rust `criterion`) vs pruebas de carga: qué responde cada uno
- Comparativa k6 vs Locust vs JMeter
- Baseline, targets y criterios de aceptación de performance
- Identificar bottlenecks desde resultados de carga

**Proyecto**: Scripts de performance testing para la API del dominio asignado: load test con k6 (JS) y stress test con Locust (Python). Reporte con análisis de resultados y umbrales definidos.

---

#### Week 44: Testing de Seguridad Básico (OWASP)

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- OWASP Top 10: visión desde el tester
- SQL Injection: cómo detectarlo con tests
- XSS: validar en tests que el output está correctamente escapado
- Autenticación y autorización: tests para acceso no autorizado
- IDOR (Insecure Direct Object Reference): tests de acceso entre usuarios
- Cabeceras de seguridad HTTP: validarlas en tests de integración
- OWASP ZAP baseline scan real contra la API, ejecutado con Docker
- Auditoría de dependencias por lenguaje: `pnpm audit`, `pip-audit`, OWASP Dependency-Check, `govulncheck`, `cargo audit`
- Security testing en CI: integrar checks automáticos

**Proyecto**: Agregar security tests a la suite de integración del dominio asignado: al menos tests para IDOR, inyección de parámetros, acceso no autorizado y headers de seguridad, más el reporte de ZAP baseline y la auditoría de dependencias de cada lenguaje usado.

---

#### Week 45: CI/CD y Testing Automatizado

**Horas**: 10h (2.5h teoría + 4h práctica + 3h proyecto + 0.5h recursos)

**Temas**:

- ¿Qué es CI/CD? Integración continua y entrega continua
- GitHub Actions: anatomía de un workflow (`.github/workflows/`)
- Job de tests: checkout, setup Node/Python/Java/Go/Rust (`actions/setup-go`, toolchain de Rust), cachés, ejecutar tests
- Matrix strategy: correr tests en múltiples versiones de lenguaje
- Artifacts: guardar reportes de coverage y resultados
- Test parallelism en CI
- Fallar el pipeline si coverage < umbral
- Quality Gates básicos con SonarQube (free tier para públicos / Community Edition para privados)
- Pre-commit hooks con Husky (JS) y pre-commit (Python, con hooks para `gofmt`/`go vet` y `cargo fmt`/`clippy`)

**Proyecto**: Configurar un GitHub Actions workflow por lenguaje (cinco) para el repositorio del dominio asignado. Cada workflow ejecuta la suite completa, genera reporte de coverage y falla si baja del umbral.

---

#### Week 46: Proyecto Final — Bootcamp Testing Zero to Hero

**Horas**: 10h (0h teoría + 2.5h planning + 7h desarrollo + 0.5h presentación)

**Temas**:

- Consolidación de todo lo aprendido (Etapas 0–6)
- Estrategia de testing completa para un sistema real
- Integración de suites: JS + Python + Java + Go/Rust + E2E + Performance
- CI/CD completo con GitHub Actions
- Documentación de calidad: README profesional del proyecto

**Proyecto**: Sistema completo del dominio asignado con:

- Suite JS (Jest): tests unitarios + integración (Supertest) + coverage ≥85%
- Suite Python (pytest + Behave): unitarios + BD + BDD + coverage ≥85%
- Suite Java (JUnit 6 + Mockito + Cucumber): unitarios + Spring + Testcontainers + coverage ≥85%
- Suite Go o Rust (al menos una; las dos es opcional): unitarios + HTTP + BD + coverage ≥85%
- E2E con Playwright (≥10 tests en POM)
- Performance: k6 load test + criterios de aceptación
- Security: tests básicos OWASP
- CI/CD: un GitHub Actions por lenguaje incluido
- `testing-strategy.md`: documento de estrategia y decisiones tomadas

---

## 📊 Resumen por Competencias

### Fundamentos de Testing (Etapa 0)

- Mentalidad de calidad y terminología profesional (S01–S02)
- Primeros tests automatizados en JS, Python y Java (S03–S05)
- Plan de pruebas y estrategia básica (S06)

### Testing con JavaScript (Etapa 1)

- Jest avanzado: organización de suites, hooks de ciclo de vida (S07)
- Matchers avanzados y parametrización con `test.each()` (S08)
- Testing asíncrono y fake timers (S09)
- Mocks avanzados: `jest.fn()`, `jest.spyOn()`, `jest.mock()` (S10)
- TDD: Red-Green-Refactor (S11)
- Integration testing con Supertest (S12)
- Snapshot testing y property-based testing con fast-check (S13)
- Code coverage y calidad de suites (S14)

### Testing con Python (Etapa 2)

- Fundamentos con pytest: AAA, fixtures básicas (S16)
- Parametrización y marks con pytest (S17)
- Mocking con unittest.mock y pytest-mock (S18)
- Testing de APIs con httpx2 y MockTransport (S19)
- TDD en Python con hypothesis (S20)
- Coverage con pytest-cov y mutation testing (S21)
- BDD con Behave/pytest-bdd (S22)
- Testing de BD con SQLAlchemy y Testcontainers (S23)

### Testing con Java (Etapa 3)

- JUnit 6 avanzado y AssertJ (S25)
- Mockito: mocks, spies, captors (S26)
- TDD con Java y ArchUnit (S27)
- Testing de Spring Boot: MockMvc, slices (S28)
- Testcontainers: BD reales en tests (S29)
- BDD con Cucumber + Spring Boot (S30)

### Testing con Go (Etapa 4)

- Fundamentos: `go test`, table-driven tests, subtests (S32)
- Interfaces, fakes, mockgen y golden files (S33)
- `httptest`, build tags y testcontainers-go (S34)
- Race detector, `testing/synctest`, fuzzing y benchmarks (S35)

### Testing con Rust (Etapa 5)

- Fundamentos: unitarios, `tests/`, doctests (S37)
- Traits, `mockall`, `rstest` e `insta` (S38)
- Async con Tokio, axum, `#[sqlx::test]` y testcontainers (S39)
- `proptest`, `cargo-fuzz`, `criterion`, `cargo-llvm-cov`, `cargo-mutants` (S40)

### Testing Avanzado Transversal (Etapa 6)

- E2E con Playwright y Page Object Model (S42)
- Performance: k6 (JS) y Locust (Python) (S43)
- Security testing básico OWASP y auditoría de dependencias (S44)
- CI/CD con GitHub Actions para los cinco lenguajes (S45)

---

## 📋 Estado de Avance

| Semana | Título                                         | Estado       |
| ------ | ---------------------------------------------- | ------------ |
| S01    | ¿Qué es Testing? Calidad de Software           | ✅ Completo |
| S02    | Casos de Prueba y Técnicas de Diseño           | ✅ Completo |
| S03    | Primeros Tests Automatizados — JavaScript      | ✅ Completo |
| S04    | Primeros Tests Automatizados — Python          | ✅ Completo |
| S05    | Primeros Tests Automatizados — Java            | ✅ Completo |
| S06    | Proyecto Integrador — Etapa 0                  | ✅ Completo |
| S07    | Jest Avanzado — Organización de Suites         | ✅ Completo |
| S08    | Matchers Avanzados y Parametrización con Jest  | ✅ Completo |
| S09    | Testing Asíncrono en Jest                      | ✅ Completo |
| S10    | Mocks Avanzados en Jest                        | ✅ Completo |
| S11    | TDD con JavaScript — Red, Green, Refactor      | ✅ Completo |
| S12    | Testing de APIs REST con Supertest             | ✅ Completo |
| S13    | Snapshot y Property-Based Testing              | ✅ Completo |
| S14    | Coverage y Calidad de Suites en Jest           | ✅ Completo |
| S15    | Proyecto Integrador — Etapa 1 (JavaScript)     | ✅ Completo |
| S16    | Fundamentos con pytest                         | ✅ Completo |
| S17    | Parametrización y Marks con pytest             | ✅ Completo |
| S18    | Mocking con unittest.mock y pytest-mock        | ✅ Completo |
| S19    | Testing de APIs con Python (httpx2)            | ✅ Completo |
| S20    | TDD con Python                                 | ✅ Completo |
| S21    | Coverage y Calidad en Python                   | ✅ Completo |
| S22    | BDD con Python — Behave                        | ✅ Completo |
| S23    | Testing de Bases de Datos en Python            | ✅ Completo  |
| S24    | Proyecto Integrador — Etapa 2 (Python)         | ✅ Completo  |
| S25    | JUnit 6 Avanzado y AssertJ                     | ✅ Completo  |
| S26    | Mockito — Mocking en Java                      | ✅ Completo  |
| S27    | TDD con Java                                   | ✅ Completo  |
| S28    | Testing de Spring Boot                         | ✅ Completo  |
| S29    | Testcontainers en Java                         | ✅ Completo  |
| S30    | BDD con Java — Cucumber                        | ✅ Completo  |
| S31    | Proyecto Integrador — Etapa 3 (Java)           | ✅ Completo  |
| S32    | Fundamentos de Testing en Go                   | ⏳ Pendiente |
| S33    | Interfaces y Dobles de Prueba en Go            | ⏳ Pendiente |
| S34    | HTTP, BD e Integración en Go                   | ⏳ Pendiente |
| S35    | Concurrencia y Calidad en Go                   | ⏳ Pendiente |
| S36    | Proyecto Integrador — Etapa 4 (Go)             | ⏳ Pendiente |
| S37    | Fundamentos de Testing en Rust                 | ⏳ Pendiente |
| S38    | Traits, Dobles y Parametrización en Rust       | ⏳ Pendiente |
| S39    | Async, APIs y BD en Rust                       | ⏳ Pendiente |
| S40    | Calidad Avanzada en Rust                       | ⏳ Pendiente |
| S41    | Proyecto Integrador — Etapa 5 (Rust)           | ⏳ Pendiente |
| S42    | Testing End-to-End con Playwright              | ⏳ Pendiente |
| S43    | Testing de Performance con k6 y Locust         | ⏳ Pendiente |
| S44    | Testing de Seguridad Básico (OWASP)            | ⏳ Pendiente |
| S45    | CI/CD y Testing Automatizado                   | ⏳ Pendiente |
| S46    | Proyecto Final — Bootcamp Testing Zero to Hero | ⏳ Pendiente |

---

_Última actualización: Noviembre 2026 | Versión: 1.2 — Añadidas las etapas de Go (S32–S36) y Rust (S37–S41); la etapa transversal pasa a S42–S46; dedicación de 10 h por semana. Semanas pendientes actualizadas al stack fijado (Spring Boot 4, Mockito 5, Testcontainers 2)_

---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Inicio del bootcamp](../README.md) | [Versiones del stack](stack-versions.md) → |

← [Volver al inicio del bootcamp](../README.md)
