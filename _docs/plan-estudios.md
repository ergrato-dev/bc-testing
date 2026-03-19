# 📅 Plan de Estudios — Bootcamp Testing Zero to Hero

**Duración total**: 36 semanas (~9 meses)
**Horas semanales**: 8 horas
**Horas totales**: ~288 horas
**Nivel de entrada**: Cero (sin experiencia previa en testing)
**Nivel de salida**: QA Engineer / SDET Junior
**Enfoque**: Testing de software con JavaScript, Python y Java — desde fundamentos absolutos hasta automatización avanzada, TDD, BDD y pruebas de performance

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
   - Paleta base: `#3572A5` (azul Python), `#F89820` (naranja Java), `#F7DF1E` (amarillo JS), fondos `#0f172a` / `#1e293b`
5. `2-practicas/` — Ejercicios guiados (starter + solution por ejercicio)
6. `3-proyecto/` — Proyecto integrador semanal (único entregable)
7. `4-recursos/` — Ebooks, videografía, webgrafía
8. `5-glosario/README.md` — Términos clave de la semana (A–Z)

---

## 🎯 Distribución por Etapas

### 🟥 Etapa 0: Fundamentos de Testing (Semanas 1–6)

48 horas totales — Mentalidad tester, terminología, tipos de prueba, primeros tests manuales y automatizados en los tres lenguajes.

### 🟦 Etapa 1: Testing con JavaScript (Semanas 7–15)

72 horas totales — Jest, testing unitario, mocks/stubs/spies, testing asíncrono, coverage, TDD y testing de APIs REST con Supertest.

### 🟩 Etapa 2: Testing con Python (Semanas 16–24)

72 horas totales — pytest, unittest, fixtures, parametrize, mocking con `unittest.mock`, testing de APIs con `requests` y `httpx`, BDD con Behave.

### 🟧 Etapa 3: Testing con Java (Semanas 25–31)

56 horas totales — JUnit 5, Mockito, AssertJ, testing de Spring Boot con MockMvc, Testcontainers y BDD con Cucumber.

### 🟪 Etapa 4: Testing Avanzado Transversal (Semanas 32–36)

40 horas totales — E2E con Playwright (JS), performance con k6/Locust, seguridad básica, estrategias CI/CD, Proyecto Final integrador multilenguaje.

---

## 📚 Contenido Semanal Detallado

---

### 🟥 ETAPA 0: FUNDAMENTOS DE TESTING (Semanas 1–6)

> El estudiante no tiene experiencia en testing. Se construye la mentalidad de calidad antes de cualquier framework. Se trabaja en los tres lenguajes en paralelo a nivel básico para que el estudiante comprenda las diferencias desde el principio.

---

#### Week 01: ¿Qué es Testing? Calidad de Software

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

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

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

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

**Horas**: 8h (2h teoría + 3.5h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- Configuración del entorno: Node.js, pnpm/yarn, Jest
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

**Horas**: 8h (2h teoría + 3.5h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- Configuración del entorno: Python 3.12+, pip/uv, pytest
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

**Horas**: 8h (2h teoría + 3.5h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- Configuración del entorno: JDK 21 LTS, Maven/Gradle, JUnit 5
- Estructura de un test en JUnit 5: `@Test`, `@DisplayName`, `Assertions.*`
- `assertEquals`, `assertTrue`, `assertNotNull`, `assertThrows`
- Ciclo de vida: `@BeforeEach`, `@AfterEach`, `@BeforeAll`, `@AfterAll`
- Diferentes entre JUnit 4 y JUnit 5 (por qué JUnit 5)
- Ejecutar tests con Maven: `mvn test`
- Leer reportes en consola y en Surefire
- Agregar AssertJ como librería de aserciones fluente

**Proyecto**: Suite de tests en Java para las mismas funciones del dominio asignado — comparar con JS (S03) y Python (S04). Análisis de similitudes y diferencias entre los tres lenguajes.

---

#### Week 06: Proyecto Integrador — Etapa 0

**Horas**: 8h (1h revisión + 1.5h planning + 5h desarrollo + 0.5h presentación)

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

#### Week 07: Jest Avanzado — Matchers y Organización

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- Matchers avanzados: `toContain`, `toHaveLength`, `toMatchObject`, `toStrictEqual`
- Custom matchers con `expect.extend()`
- `expect.anything()`, `expect.any(Constructor)`
- Organización de tests: `describe` anidados, `beforeEach`/`afterEach`
- `test.only`, `test.skip`, `test.todo`
- Naming conventions para tests: "should...", "given...when...then..."
- Setup de Jest en `package.json` vs `jest.config.js`
- Diferencia entre `toBe` y `toEqual` en profundidad

**Proyecto**: Refactorización de la suite JS de Etapa 0 usando matchers avanzados y organización por bloques `describe`. Añadir al menos 3 custom matchers relevantes al dominio.

---

#### Week 08: Mocks, Stubs y Spies en Jest

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- Test doubles: diferencia entre mock, stub, spy, fake, dummy
- `jest.fn()` — crear funciones mock
- `.mockReturnValue()`, `.mockResolvedValue()`, `.mockImplementation()`
- `jest.spyOn()` — observar funciones reales sin reemplazarlas
- Verificar llamadas: `.toHaveBeenCalled()`, `.toHaveBeenCalledWith()`
- `jest.mock()` — mockear módulos completos
- Cuándo mockear: principio de aislamiento
- Restaurar mocks: `mockRestore()`, `clearMocks`

**Proyecto**: Suite con mocks para aislar dependencias externas del dominio asignado (servicios de base de datos, APIs externas, envío de emails). Al menos 5 tests con doubles distintos.

---

#### Week 09: Testing Asíncrono en Jest

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- El problema del código asíncrono y los tests
- Callbacks en tests: el parámetro `done`
- Testing con Promises: `return promise`
- Testing con `async/await` (patrón recomendado)
- `.resolves` y `.rejects` matchers
- `mockResolvedValue` vs `mockRejectedValue`
- Timers falsos: `jest.useFakeTimers()`, `jest.runAllTimers()`
- Antipatrones: tests que siempre pasan (falsos positivos)

**Proyecto**: Suite de tests para módulos asíncronos del dominio asignado: fetching de datos, operaciones con base de datos simuladas, timeouts y retries.

---

#### Week 10: Code Coverage en JavaScript

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- ¿Qué es code coverage? Tipos: statement, branch, function, line
- Configurar coverage en Jest: `--coverage`, `collectCoverageFrom`
- Leer reportes HTML de Istanbul/c8
- Coverage ≠ calidad: el problema de los tests superficiales
- Definir umbrales de cobertura: `coverageThreshold`
- Qué código cubrir y qué excluir (`/* istanbul ignore */`)
- Coverage en CI: fallar build si coverage baja
- Integración con Codecov/SonarQube (conceptual)

**Proyecto**: Alcanzar 90%+ de coverage en la suite JS del dominio asignado. Documentar qué ramas quedaron sin cubrir y por qué.

---

#### Week 11: TDD con JavaScript — Red, Green, Refactor

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- ¿Qué es TDD? El ciclo Red → Green → Refactor
- Diferencia entre TDD, BDD y ATDD
- Cómo escribir el test primero sin conocer la implementación
- Diseño emergente: cómo TDD guía la arquitectura
- Kata de TDD: FizzBuzz, String Calculator
- TDD en funciones puras vs con efectos secundarios
- Antipatrones de TDD: escribir muchos tests de golpe, tests frágiles
- Outside-in TDD vs inside-out TDD

**Proyecto**: Implementar de cero (con TDD estricto) un módulo complejo del dominio asignado: commits separados por ciclo Red-Green-Refactor, al menos 15 tests.

---

#### Week 12: Testing de APIs REST con Supertest

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- Testing de integración vs testing unitario
- Supertest: instalación y configuración con Express
- `request(app).get('/ruta').expect(200)`
- Verificar body, headers, status codes
- Testing de endpoints POST/PUT/PATCH/DELETE
- Autenticación en tests: JWT, API keys
- Bases de datos en tests: in-memory (SQLite), setup/teardown
- Separar entorno de test con variables de entorno

**Proyecto**: Suite de integration tests con Supertest para la API REST del dominio asignado: CRUD completo (mínimo 20 tests), autenticación incluida.

---

#### Week 13: Snapshot Testing y Testing de Componentes

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- ¿Qué es snapshot testing? Cuándo usarlo y cuándo NO
- `toMatchSnapshot()` y `toMatchInlineSnapshot()`
- Actualizar snapshots: `--updateSnapshot`
- Testing de funciones que generan HTML/JSON
- Introducción a Testing Library (conceptual)
- Testing de DOM con jsdom en Jest
- Problemas comunes: snapshots frágiles, snapshots grandes
- Snapshot testing para APIs: contract testing básico

**Proyecto**: Suite de snapshot tests para los renderizadores/generadores de HTML/JSON del dominio asignado. Al menos 8 snapshots con justificación de cada uno.

---

#### Week 14: Patrones Avanzados de Testing en JavaScript

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- Property-based testing con fast-check
- Parametric tests: `test.each()` con tabla de datos
- Test factories y builders para datos de prueba
- Page Object Model básico (para futura automatización E2E)
- Testing de errores y excepciones con detalle
- Mutation testing con Stryker (introducción)
- Organización de tests en proyectos grandes: colocation vs carpeta `__tests__`
- Testability: escribir código pensado para ser testeable

**Proyecto**: Refactorización de la suite JS usando `test.each()` para reducir duplicación, implementar 2 property-based tests con fast-check, y aplicar el patrón builder para datos de prueba.

---

#### Week 15: Proyecto Integrador — Etapa 1 (JavaScript)

**Horas**: 8h (0.5h teoría + 2h planning + 5h desarrollo + 0.5h presentación)

**Temas**:

- Integración de Semanas 7–14
- Arquitectura de la suite de tests: unitarios + integración + snapshots
- Coverage mínimo 85% en todo el proyecto
- Reporte de testing con resultados y análisis

**Proyecto**: Suite completa de tests JS para el dominio asignado: unitarios (Jest + mocks), integración (Supertest), snapshot, `test.each()`, coverage ≥85%, reporte `testing-report-js.md`.

---

### 🟩 ETAPA 2: TESTING CON PYTHON (Semanas 16–24)

> Se introduce pytest en profundidad, fixtures, parametrize, mocking, testing de APIs con `requests`/`httpx`, y BDD con Behave. El estudiante aplica los mismos conceptos del Etapa 1 al ecosistema Python.

---

#### Week 16: pytest Avanzado — Fixtures y Parametrize

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- Fixtures de pytest: `@pytest.fixture`, scope (`function`, `class`, `module`, `session`)
- Fixtures con `yield` para setup/teardown
- Fixtures anidados y dependencias entre fixtures
- Fixture factory pattern
- `@pytest.mark.parametrize` — pruebas parametrizadas simples y combinadas
- `pytest.param` con ids y marks
- `conftest.py` — fixtures compartidos entre módulos
- Markers: `@pytest.mark.skip`, `@pytest.mark.xfail`, `@pytest.mark.slow`

**Proyecto**: Refactorización de la suite Python de Etapa 0 usando fixtures con diferentes scopes y `@pytest.mark.parametrize` para cubrir múltiples casos del dominio asignado.

---

#### Week 17: Mocking en Python con unittest.mock

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- `unittest.mock`: `Mock`, `MagicMock`, `patch`
- `@patch` como decorador y como context manager
- `patch.object` para mockear métodos de instancia
- `side_effect`: simular excepciones, retornos dinámicos
- `call_args`, `call_count`, `assert_called_once_with()`
- `pytest-mock`: `mocker` fixture como wrapper de unittest.mock
- Mockear módulos externos: `datetime`, `os`, `requests`
- Cuándo usar `MagicMock` vs `Mock` vs `AsyncMock`

**Proyecto**: Suite con mocks para aislar todas las dependencias externas del dominio asignado en Python: base de datos, servicios HTTP, sistema de archivos.

---

#### Week 18: Testing Asíncrono en Python

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- Código asíncrono en Python: `asyncio`, `async def`, `await`
- `pytest-asyncio`: `@pytest.mark.asyncio`
- `AsyncMock` para mockear corutinas
- Testing de código con `asyncio.gather()`
- Testing con `httpx.AsyncClient`
- Timeouts en tests asíncronos
- `anyio` como alternativa (mención)
- Antipatrones: tests síncronos que ignoran corutinas

**Proyecto**: Suite de tests para el módulo asíncrono del dominio asignado (cliente HTTP async, tareas concurrentes). Al menos 10 tests async con mocks adecuados.

---

#### Week 19: Testing de APIs con Python (requests + httpx)

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- Testing de APIs REST con `requests` y `responses` (mock de HTTP)
- `pytest-httpserver` para simular servidor HTTP real
- Testing con `httpx` y `respx` para mockear responses
- Validación de esquemas JSON con `jsonschema` y `pydantic`
- Testing de autenticación: Basic Auth, Bearer Token
- Testing de errores HTTP (4xx, 5xx) y timeouts
- Fixtures para cliente HTTP y datos de prueba
- Contract testing básico con Pact (introducción)

**Proyecto**: Suite de integration tests en Python para el API del dominio asignado usando `httpx` + `respx` para mocks HTTP. Incluir validación de esquemas JSON con pydantic.

---

#### Week 20: TDD con Python

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- El ciclo Red → Green → Refactor en Python con pytest
- TDD en Python: diferencias culturales con JS/Java
- Kata de TDD en Python: Roman Numerals, Bowling Game
- Diseño emergente con TDD en Python: dataclasses, protocols
- Testing con tipos: `mypy` en el ciclo TDD
- `hypothesis` — property-based testing en Python
- Integración de TDD con fixtures de pytest
- TDD en código legado: el problema del código no testeable

**Proyecto**: Implementación TDD de un módulo complejo del dominio asignado en Python. Commits por ciclo R-G-R, al menos 15 tests, integrar 2 property-based tests con `hypothesis`.

---

#### Week 21: Coverage y Calidad en Python

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- `pytest-cov`: configuración y ejecución
- Reportes: terminal, HTML, XML (para CI)
- Branch coverage en Python: `.coveragerc` / `pyproject.toml`
- Excluir código del coverage: `# pragma: no cover`
- `coverage combine` para proyectos multi-módulo
- Mutation testing con `mutmut`
- SonarQube para Python: métricas de calidad
- Definir umbrales de calidad: coverage, duplicados, complejidad ciclomática

**Proyecto**: Alcanzar 90%+ de coverage en la suite Python. Ejecutar `mutmut` y analizar los mutantes vivos. Documentar `quality-report-python.md`.

---

#### Week 22: BDD con Python — Behave

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- ¿Qué es BDD? Behavior Driven Development
- Gherkin: `Feature`, `Scenario`, `Given`, `When`, `Then`, `And`, `But`
- Behave: instalación, estructura de proyecto (`features/`, `steps/`)
- Escribir step definitions en Python
- `context` object en Behave
- Scenario Outline con `Examples`
- Hooks: `before_scenario`, `after_scenario`, `before_feature`
- Integrar Behave con pytest (`pytest-bdd` como alternativa)

**Proyecto**: Suite BDD con Behave para el flujo de negocio principal del dominio asignado: al menos 3 features con múltiples scenarios y Scenario Outline.

---

#### Week 23: Testing de Bases de Datos en Python

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- Estrategias para testear con BD: in-memory, fixtures, transacciones
- SQLite en tests: `pytest` + `sqlalchemy` con BD en memoria
- Fixtures de BD: crear esquema, poblar datos, limpiar
- Testing con SQLAlchemy: rollback por test
- Faker para datos de prueba realistas
- Factory Boy para objetos de prueba complejos
- Testing de migraciones (Alembic)
- Testcontainers en Python para BD reales (PostgreSQL, MySQL)

**Proyecto**: Suite de tests para la capa de acceso a datos del dominio asignado: CRUD completo con SQLAlchemy en SQLite, usando Factory Boy para datos de prueba.

---

#### Week 24: Proyecto Integrador — Etapa 2 (Python)

**Horas**: 8h (0.5h teoría + 2h planning + 5h desarrollo + 0.5h presentación)

**Temas**:

- Integración de Semanas 16–23
- Arquitectura: unitarios (pytest) + integración (httpx) + BDD (Behave) + BD
- Coverage ≥85% en toda la suite Python
- Reporte de testing con análisis de calidad

**Proyecto**: Suite completa Python del dominio asignado: pytest + fixtures + mocks + async + BDD + BD. Coverage ≥85%, reporte `testing-report-python.md`.

---

### 🟧 ETAPA 3: TESTING CON JAVA (Semanas 25–31)

> Se profundiza en JUnit 5 y el ecosistema Java de testing: Mockito, AssertJ, testing de Spring Boot, Testcontainers y BDD con Cucumber.

---

#### Week 25: JUnit 5 Avanzado y AssertJ

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- JUnit 5 en profundidad: `@ParameterizedTest`, `@MethodSource`, `@CsvSource`, `@ValueSource`
- `@Nested` para organizar tests por contexto
- `@Tag` y filtrado de tests
- `@TempDir`, `@RegisterExtension`
- JUnit 5 Extensions: `@ExtendWith`
- AssertJ: aserciones fluentes y legibles
- `assertThat().isEqualTo()`, `containsExactly()`, `satisfies()`
- Soft assertions con `SoftAssertions`

**Proyecto**: Refactorización de la suite Java de Etapa 0 usando `@ParameterizedTest`, `@Nested` y AssertJ. Añadir soft assertions para escenarios con múltiples validaciones.

---

#### Week 26: Mockito — Mocking en Java

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- Mockito: `@Mock`, `@InjectMocks`, `@Spy`, `@Captor`
- `when().thenReturn()`, `when().thenThrow()`, `doReturn()`
- `verify()`: verificar interacciones
- `ArgumentCaptor`: capturar argumentos de llamadas
- `@MockitoExtension` con JUnit 5
- Mockito strict stubbing: detectar stubs no utilizados
- Mockear métodos estáticos con `mockStatic()` (Mockito 3.4+)
- Diferencia entre `@Mock` y `@Spy`

**Proyecto**: Suite con Mockito para aislar todas las dependencias del dominio asignado en Java: repositorios, servicios externos, envío de notificaciones.

---

#### Week 27: TDD con Java

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- El ciclo Red → Green → Refactor en Java con JUnit 5
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

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- Spring Boot Test: `@SpringBootTest`, `@WebMvcTest`, `@DataJpaTest`
- MockMvc: testear controladores REST sin levantar servidor
- `@MockBean` y `@SpyBean` (diferencia con `@Mock` de Mockito)
- Testing del contexto de Spring: `@ActiveProfiles("test")`
- Test slices: `@JsonTest`, `@RestClientTest`
- Configuración de BD en tests: H2 in-memory
- `TestRestTemplate` para tests de integración completos
- `@Transactional` en tests: rollback automático

**Proyecto**: Suite de tests para la API Spring Boot del dominio asignado: `@WebMvcTest` para controladores, `@DataJpaTest` para repositorios, `@SpringBootTest` para integración.

---

#### Week 29: Testcontainers en Java

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- ¿Qué es Testcontainers? Contenedores Docker en tests
- Testcontainers con JUnit 5: `@Testcontainers`, `@Container`
- PostgreSQL container, MySQL container
- Inicialización de schemas: `init.sql`, Flyway, Liquibase
- Kafka container para tests de mensajería
- `GenericContainer` para servicios arbitrarios
- Compartir containers entre tests: `reuse = true`
- Optimización de tiempos: `@Container` static vs instance

**Proyecto**: Migrar los tests de BD del dominio asignado de H2 a PostgreSQL real con Testcontainers. Demonstrar paridad entre entorno de test y producción.

---

#### Week 30: BDD con Java — Cucumber

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- Cucumber para Java: dependencias Maven/Gradle
- Gherkin en Java: `@Given`, `@When`, `@Then` con expresiones
- `DataTable` para datos tabulares en steps
- `World` / State sharing entre steps
- Cucumber con Spring Boot: `@CucumberContextConfiguration`
- Generar reportes HTML con `cucumber-reporting`
- Integrar Cucumber en Maven Surefire/Failsafe
- Living documentation: features como documentación ejecutable

**Proyecto**: Suite BDD con Cucumber para el flujo de negocio principal del dominio asignado en Java: al menos 3 features integradas con Spring Boot.

---

#### Week 31: Proyecto Integrador — Etapa 3 (Java)

**Horas**: 8h (0.5h teoría + 2h planning + 5h desarrollo + 0.5h presentación)

**Temas**:

- Integración de Semanas 25–30
- Arquitectura de testing Java: unit + integration (Spring) + BDD (Cucumber) + Testcontainers
- Coverage con JaCoCo ≥85%
- Reporte de testing con análisis de calidad

**Proyecto**: Suite completa Java del dominio asignado: JUnit 5 + AssertJ + Mockito + Spring Boot + Testcontainers + Cucumber. JaCoCo ≥85%, reporte `testing-report-java.md`.

---

### 🟪 ETAPA 4: TESTING AVANZADO TRANSVERSAL (Semanas 32–36)

> El estudiante domina testing unitario e integración en los tres lenguajes. La etapa final cubre E2E, performance, seguridad, CI/CD y el proyecto final integrador multilenguaje.

---

#### Week 32: Testing End-to-End con Playwright

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

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

#### Week 33: Testing de Performance con k6 y Locust

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- ¿Qué es performance testing? Load, stress, spike, soak, volume
- Métricas clave: RPS, latencia (p50/p90/p99), error rate, throughput
- k6 (JavaScript): scripts, thresholds, stages, `check()`
- Interpretar resultados de k6: métricas y reporte
- Locust (Python): `HttpUser`, `TaskSet`, `@task`, `wait_time`
- Comparativa k6 vs Locust vs JMeter
- Baseline, targets y criterios de aceptación de performance
- Identificar bottlenecks desde resultados de carga

**Proyecto**: Scripts de performance testing para la API del dominio asignado: load test con k6 (JS) y stress test con Locust (Python). Reporte con análisis de resultados y umbrales definidos.

---

#### Week 34: Testing de Seguridad Básico (OWASP)

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- OWASP Top 10: visión desde el tester
- SQL Injection: cómo detectarlo con tests
- XSS: validar en tests que el output está correctamente escapado
- Autenticación y autorización: tests para acceso no autorizado
- IDOR (Insecure Direct Object Reference): tests de acceso entre usuarios
- Cabeceras de seguridad HTTP: validarlas en tests de integración
- OWASP ZAP básico: scan pasivo (conceptual)
- Security testing en CI: integrar checks automáticos

**Proyecto**: Agregar security tests a la suite de integración del dominio asignado: al menos tests para IDOR, inyección de parámetros, acceso no autorizado y headers de seguridad.

---

#### Week 35: CI/CD y Testing Automatizado

**Horas**: 8h (2.5h teoría + 3h práctica + 2h proyecto + 0.5h recursos)

**Temas**:

- ¿Qué es CI/CD? Integración continua y entrega continua
- GitHub Actions: anatomía de un workflow (`.github/workflows/`)
- Job de tests: checkout, setup Node/Python/Java, ejecutar tests
- Matrix strategy: correr tests en múltiples versiones de lenguaje
- Artifacts: guardar reportes de coverage y resultados
- Test parallelism en CI
- Fallar el pipeline si coverage < umbral
- Pre-commit hooks con Husky (JS), pre-commit (Python)

**Proyecto**: Configurar 3 GitHub Actions workflows (uno por lenguaje) para el repositorio del dominio asignado. Cada workflow ejecuta la suite completa, genera reporte de coverage y falla si baja del umbral.

---

#### Week 36: Proyecto Final — Bootcamp Testing Zero to Hero

**Horas**: 8h (0h teoría + 2h planning + 5.5h desarrollo + 0.5h presentación)

**Temas**:

- Consolidación de todo lo aprendido (Etapas 0–4)
- Estrategia de testing completa para un sistema real
- Integración de suites: JS + Python + Java + E2E + Performance
- CI/CD completo con GitHub Actions
- Documentación de calidad: README profesional del proyecto

**Proyecto**: Sistema completo del dominio asignado con:

- Suite JS (Jest): tests unitarios + integración (Supertest) + coverage ≥85%
- Suite Python (pytest + Behave): unitarios + BD + BDD + coverage ≥85%
- Suite Java (JUnit 5 + Mockito + Cucumber): unitarios + Spring + Testcontainers + coverage ≥85%
- E2E con Playwright (≥10 tests en POM)
- Performance: k6 load test + criterios de aceptación
- Security: tests básicos OWASP
- CI/CD: 3 GitHub Actions configurados
- `testing-strategy.md`: documento de estrategia y decisiones tomadas

---

## 📊 Resumen por Competencias

### Fundamentos de Testing (Etapa 0)

- Mentalidad de calidad y terminología profesional (S01–S02)
- Primeros tests automatizados en JS, Python y Java (S03–S05)
- Plan de pruebas y estrategia básica (S06)

### Testing con JavaScript (Etapa 1)

- Jest avanzado: matchers, organización, ciclo de vida (S07)
- Mocks, stubs y spies con Jest (S08)
- Testing asíncrono y timers falsos (S09)
- Code coverage con Istanbul/c8 (S10)
- TDD: Red-Green-Refactor (S11)
- Integration testing con Supertest (S12)
- Snapshot testing y contract testing básico (S13)
- Property-based testing y test.each() (S14)

### Testing con Python (Etapa 2)

- pytest avanzado: fixtures, parametrize, conftest (S16)
- Mocking con unittest.mock y pytest-mock (S17)
- Testing asíncrono con pytest-asyncio (S18)
- Testing de APIs con httpx/respx (S19)
- TDD en Python con hypothesis (S20)
- Coverage con pytest-cov y mutation testing (S21)
- BDD con Behave/pytest-bdd (S22)
- Testing de BD con SQLAlchemy y Testcontainers (S23)

### Testing con Java (Etapa 3)

- JUnit 5 avanzado y AssertJ (S25)
- Mockito: mocks, spies, captors (S26)
- TDD con Java y ArchUnit (S27)
- Testing de Spring Boot: MockMvc, slices (S28)
- Testcontainers: BD reales en tests (S29)
- BDD con Cucumber + Spring Boot (S30)

### Testing Avanzado Transversal (Etapa 4)

- E2E con Playwright y Page Object Model (S32)
- Performance: k6 (JS) y Locust (Python) (S33)
- Security testing básico OWASP (S34)
- CI/CD con GitHub Actions (S35)

---

## 📋 Estado de Avance

| Semana | Título                                         | Estado       |
| ------ | ---------------------------------------------- | ------------ |
| S01    | ¿Qué es Testing? Calidad de Software           | ⏳ Pendiente |
| S02    | Casos de Prueba y Técnicas de Diseño           | ⏳ Pendiente |
| S03    | Primeros Tests Automatizados — JavaScript      | ⏳ Pendiente |
| S04    | Primeros Tests Automatizados — Python          | ⏳ Pendiente |
| S05    | Primeros Tests Automatizados — Java            | ⏳ Pendiente |
| S06    | Proyecto Integrador — Etapa 0                  | ⏳ Pendiente |
| S07    | Jest Avanzado — Matchers y Organización        | ⏳ Pendiente |
| S08    | Mocks, Stubs y Spies en Jest                   | ⏳ Pendiente |
| S09    | Testing Asíncrono en Jest                      | ⏳ Pendiente |
| S10    | Code Coverage en JavaScript                    | ⏳ Pendiente |
| S11    | TDD con JavaScript — Red, Green, Refactor      | ⏳ Pendiente |
| S12    | Testing de APIs REST con Supertest             | ⏳ Pendiente |
| S13    | Snapshot Testing y Testing de Componentes      | ⏳ Pendiente |
| S14    | Patrones Avanzados de Testing en JavaScript    | ⏳ Pendiente |
| S15    | Proyecto Integrador — Etapa 1 (JavaScript)     | ⏳ Pendiente |
| S16    | pytest Avanzado — Fixtures y Parametrize       | ⏳ Pendiente |
| S17    | Mocking en Python con unittest.mock            | ⏳ Pendiente |
| S18    | Testing Asíncrono en Python                    | ⏳ Pendiente |
| S19    | Testing de APIs con Python (requests + httpx)  | ⏳ Pendiente |
| S20    | TDD con Python                                 | ⏳ Pendiente |
| S21    | Coverage y Calidad en Python                   | ⏳ Pendiente |
| S22    | BDD con Python — Behave                        | ⏳ Pendiente |
| S23    | Testing de Bases de Datos en Python            | ⏳ Pendiente |
| S24    | Proyecto Integrador — Etapa 2 (Python)         | ⏳ Pendiente |
| S25    | JUnit 5 Avanzado y AssertJ                     | ⏳ Pendiente |
| S26    | Mockito — Mocking en Java                      | ⏳ Pendiente |
| S27    | TDD con Java                                   | ⏳ Pendiente |
| S28    | Testing de Spring Boot                         | ⏳ Pendiente |
| S29    | Testcontainers en Java                         | ⏳ Pendiente |
| S30    | BDD con Java — Cucumber                        | ⏳ Pendiente |
| S31    | Proyecto Integrador — Etapa 3 (Java)           | ⏳ Pendiente |
| S32    | Testing End-to-End con Playwright              | ⏳ Pendiente |
| S33    | Testing de Performance con k6 y Locust         | ⏳ Pendiente |
| S34    | Testing de Seguridad Básico (OWASP)            | ⏳ Pendiente |
| S35    | CI/CD y Testing Automatizado                   | ⏳ Pendiente |
| S36    | Proyecto Final — Bootcamp Testing Zero to Hero | ⏳ Pendiente |

---

_Última actualización: Marzo 2026 | Versión: 1.0 — Creación inicial zero-to-hero_
