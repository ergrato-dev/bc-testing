# 🤖 Instrucciones para GitHub Copilot

## 📋 Contexto del Bootcamp

Este es un **Bootcamp de Testing de Software Zero to Hero** que cubre testing automatizado en tres lenguajes: JavaScript, Python y Java. Está estructurado para llevar a estudiantes sin experiencia en testing hasta un nivel de QA Engineer / SDET Junior.

### 📊 Datos del Bootcamp

- **Duración**: 36 semanas (~9 meses)
- **Dedicación semanal**: 8 horas
- **Total de horas**: ~288 horas
- **Nivel de entrada**: Cero (sin experiencia previa en testing)
- **Nivel de salida**: QA Engineer / SDET Junior
- **Lenguajes**: JavaScript (Jest), Python (pytest), Java (JUnit 5)
- **Enfoque**: Testing desde fundamentos hasta automatización avanzada, TDD, BDD y CI/CD

---

## 🎯 Objetivos de Aprendizaje

Al finalizar el bootcamp, los estudiantes serán capaces de:

- ✅ Aplicar la mentalidad de calidad y diseñar casos de prueba profesionales
- ✅ Escribir tests unitarios en JavaScript (Jest), Python (pytest) y Java (JUnit 5)
- ✅ Usar mocks, stubs y spies para aislar dependencias en los tres lenguajes
- ✅ Aplicar TDD (Red-Green-Refactor) en JavaScript, Python y Java
- ✅ Implementar BDD con Behave (Python) y Cucumber (Java)
- ✅ Escribir tests de integración para APIs REST en los tres lenguajes
- ✅ Automatizar pruebas E2E con Playwright
- ✅ Ejecutar tests de performance con k6 y Locust
- ✅ Integrar testing en pipelines CI/CD con GitHub Actions
- ✅ Medir y mantener coverage de código ≥85% en los tres lenguajes

---

## 📚 Estructura del Bootcamp

### Distribución por Etapas

#### 🟥 Etapa 0: Fundamentos de Testing (Semanas 1–6) — 48 horas

- Mentalidad de calidad, terminología, tipos de testing
- Pirámide de testing
- Primeros tests automatizados en los tres lenguajes en paralelo
- Plan de pruebas y casos de prueba

#### 🟦 Etapa 1: Testing con JavaScript (Semanas 7–15) — 72 horas

- Jest avanzado: matchers, organización, ciclo de vida
- Mocks, stubs y spies con `jest.fn()`, `jest.spyOn()`, `jest.mock()`
- Testing asíncrono: async/await, timers falsos
- Code coverage con Istanbul/c8
- TDD: ciclo Red-Green-Refactor
- Testing de APIs REST con Supertest
- Snapshot testing, property-based testing con fast-check
- `test.each()` para tests parametrizados

#### 🟩 Etapa 2: Testing con Python (Semanas 16–24) — 72 horas

- pytest avanzado: fixtures, `@pytest.mark.parametrize`, `conftest.py`
- Mocking con `unittest.mock`, `MagicMock`, `@patch`, `pytest-mock`
- Testing asíncrono con `pytest-asyncio` y `AsyncMock`
- Testing de APIs con `httpx` + `respx`
- TDD en Python con `hypothesis` (property-based)
- Coverage con `pytest-cov`, mutation testing con `mutmut`
- BDD con Behave (Gherkin + step definitions)
- Testing de BD con SQLAlchemy + Factory Boy + Testcontainers

#### 🟧 Etapa 3: Testing con Java (Semanas 25–31) — 56 horas

- JUnit 5 avanzado: `@ParameterizedTest`, `@Nested`, `@Tag`
- AssertJ: aserciones fluentes y soft assertions
- Mockito: `@Mock`, `@InjectMocks`, `@Spy`, `@Captor`, `ArgumentCaptor`
- TDD con Java: diseño emergente OOP
- Testing de Spring Boot: `@WebMvcTest`, `@DataJpaTest`, `MockMvc`, `@MockBean`
- Testcontainers: PostgreSQL/MySQL reales en tests
- BDD con Cucumber + Spring Boot
- JaCoCo para coverage

#### 🟪 Etapa 4: Testing Avanzado Transversal (Semanas 32–36) — 40 horas

- E2E con Playwright: locators, POM, assertions visuales
- Performance testing con k6 (JS) y Locust (Python)
- Security testing básico OWASP
- CI/CD con GitHub Actions: workflows por lenguaje
- Proyecto final integrador multilenguaje

---

## 🗂️ Estructura de Carpetas

Cada semana sigue esta estructura estándar:

```
bootcamp/week-XX/
├── README.md                 # Descripción y objetivos de la semana
├── rubrica-evaluacion.md     # Criterios de evaluación detallados
├── 0-assets/                 # Diagramas SVG y recursos visuales
├── 1-teoria/                 # Material teórico (archivos .md)
├── 2-practicas/              # Ejercicios guiados paso a paso
│   └── ejercicio-XX-nombre/
│       ├── README.md
│       ├── starter/          # Código comentado para descomentar
│       └── solution/         # Solución completa
├── 3-proyecto/               # Proyecto semanal (único entregable)
│   ├── README.md
│   ├── starter/              # Con TODOs para implementar
│   └── solution/             # Excluida con .gitignore
├── 4-recursos/
│   ├── ebooks-free/
│   ├── videografia/
│   └── webgrafia/
└── 5-glosario/
    └── README.md
```

### Carpetas Raíz

- `_assets/`: Recursos visuales globales (logos, headers SVG, banners)
- `_docs/`: Documentación general del bootcamp
- `_scripts/`: Scripts de automatización y utilidades
- `bootcamp/`: Contenido semanal

### Orden de Creación de Cada Semana

Seguir **siempre** este orden:

1. `README.md` — Visión general, objetivos, distribución del tiempo, navegación
2. `rubrica-evaluacion.md` — Criterios de evaluación con 3 tipos de evidencia
3. `1-teoria/` — Archivos de teoría numerados (`01-tema.md`, `02-tema.md`, …)
4. `0-assets/` — Diagramas SVG vinculados a la teoría
5. `2-practicas/` — Ejercicios guiados con `starter/` comentado + `solution/`
6. `3-proyecto/` — Proyecto con TODOs genéricos adaptables al dominio
7. `4-recursos/` — Ebooks, videografía, webgrafía
8. `5-glosario/README.md` — Términos clave A–Z

---

## 🎓 Componentes de Cada Semana

### 1. Teoría (1-teoria/)

- Archivos markdown con explicaciones conceptuales
- Ejemplos de código con comentarios educativos en español
- Indicar claramente a qué lenguaje aplica cada sección (JS / Python / Java / Transversal)
- Cuando un concepto aplica a los tres lenguajes, mostrar el equivalente en cada uno
- Diagramas SVG referenciados con `![Descripción](../0-assets/01-nombre.svg)`

### 2. Prácticas (2-practicas/)

Los ejercicios son **tutoriales guiados**, NO tareas con TODOs. El estudiante aprende descomentando código.

**✅ CORRECTO — código comentado para descomentar**:

`README.md`:

```markdown
### Paso 1: Describir el test

Explicación del concepto con ejemplo...

**Abre `starter/index.test.js`** y descomenta la sección correspondiente.
```

`starter/index.test.js` (JavaScript):

```javascript
// ============================================
// PASO 1: Mi primer test con Jest
// ============================================
describe("Calculator", () => {
  // Descomenta las siguientes líneas:
  // test('should add two numbers', () => {
  //   const result = add(2, 3);
  //   expect(result).toBe(5);
  // });
});
```

`starter/test_starter.py` (Python):

```python
# ============================================
# PASO 1: Mi primer test con pytest
# ============================================

# Descomenta las siguientes líneas:
# def test_add_two_numbers():
#     result = add(2, 3)
#     assert result == 5
```

`starter/CalculatorTest.java` (Java):

```java
// ============================================
// PASO 1: Mi primer test con JUnit 5
// ============================================
class CalculatorTest {
    // Descomenta las siguientes líneas:
    // @Test
    // @DisplayName("should add two numbers")
    // void shouldAddTwoNumbers() {
    //     int result = Calculator.add(2, 3);
    //     assertEquals(5, result);
    // }
}
```

**❌ INCORRECTO — NO usar TODOs en ejercicios**:

```javascript
// ❌ Este formato es SOLO para PROYECTOS, no ejercicios
test("should add", () => {
  // TODO: Implementar el test
});
```

### 3. Proyecto (3-proyecto/)

El proyecto **SÍ usa TODOs** para implementación desde cero. Es el **único entregable obligatorio** de cada semana.

#### 🏛️ Política de Dominios Únicos (Anticopia)

Cada aprendiz recibe un dominio único asignado por el instructor:

- 📖 Biblioteca | 💊 Farmacia | 🏋️ Gimnasio | 🏫 Escuela
- 🏬 Tienda de mascotas | 🏪 Restaurante | 🏦 Banco | 🚕 Agencia de taxis
- 🏥 Hospital | 🎥 Cine | 🏨 Hotel | ✈️ Agencia de viajes
- 🚗 Concesionario | 👗 Tienda de ropa | 🛠️ Taller mecánico

> ⚠️ **IMPORTANTE**: Los ejemplos en los proyectos **NO** deben usar dominios de la lista. Usar ejemplos genéricos (Museo, Planetario, Acuario). Esto evita "regalar" soluciones.

**Formato de starter en proyecto** (ejemplo JavaScript):

```javascript
// ============================================
// TEST SUITE: ItemService
// Servicio para gestión de elementos del dominio
// ============================================

// NOTA PARA EL APRENDIZ:
// Adapta esta suite a tu dominio asignado.
// Ejemplos:
// - Biblioteca: BookService (libro)
// - Farmacia: MedicineService (medicamento)
// - Gimnasio: MemberService (miembro)

describe("ItemService", () => {
  // TODO: Configurar el servicio bajo prueba
  // let service;

  beforeEach(() => {
    // TODO: Inicializar el servicio antes de cada test
  });

  describe("create", () => {
    // TODO: Implementar tests para la creación de elementos
    // 1. Test caso válido (happy path)
    // 2. Test campo requerido faltante
    // 3. Test tipo de dato incorrecto
  });
});
```

---

## 📝 Convenciones de Código

### ⚠️ REGLA CRÍTICA: Inglés Técnico + Español Educativo

**NOMENCLATURA TÉCNICA — SIEMPRE EN INGLÉS**:

- ✅ Variables, constantes, funciones, métodos
- ✅ Clases, interfaces, tipos
- ✅ Nombres de archivos de test
- ✅ Nombres de test (`describe`, `it`, `test`, `@DisplayName`)
- ✅ Propiedades de objetos y fixtures

**COMENTARIOS Y DOCUMENTACIÓN — SIEMPRE EN ESPAÑOL**:

- ✅ Comentarios de código explicativos
- ✅ READMEs y documentación
- ✅ Mensajes educativos en los TODO
- ✅ Explicaciones en glosarios

**Ejemplos correctos**:

```javascript
// ✅ CORRECTO
describe("UserRepository", () => {
  // Verificar que el repositorio lanza error cuando el email ya existe
  it("should throw DuplicateEmailError when email already exists", () => {
    const existingUser = { email: "test@example.com" };
    expect(() => repo.create(existingUser)).toThrow(DuplicateEmailError);
  });
});
```

```python
# ✅ CORRECTO
class TestUserRepository:
    def test_raises_duplicate_email_error_when_email_already_exists(self):
        # Verificar que el repositorio lanza error al duplicar email
        existing_user = {"email": "test@example.com"}
        with pytest.raises(DuplicateEmailError):
            repo.create(existing_user)
```

### Nomenclatura de Tests por Lenguaje

**JavaScript (Jest) — patrón recomendado**:

```javascript
describe('ClassName', () => {
  describe('methodName', () => {
    it('should [resultado esperado] when [condición]', () => { ... });
    it('should throw [Error] when [condición]', () => { ... });
  });
});
```

**Python (pytest) — patrón recomendado**:

```python
class TestClassName:
    def test_method_name_returns_value_when_condition(self): ...
    def test_method_name_raises_error_when_condition(self): ...

# O funciones independientes:
def test_function_name_description(): ...
```

**Java (JUnit 5) — patrón recomendado**:

```java
@Nested
@DisplayName("methodName")
class MethodName {
    @Test
    @DisplayName("should [resultado esperado] when [condición]")
    void shouldResultWhenCondition() { ... }
}
```

### Convenciones por Lenguaje

**JavaScript**:

- `const`/`let` (nunca `var`)
- Arrow functions para callbacks de test
- `async/await` para tests asíncronos
- Gestores: `pnpm` o `yarn` (❌ NUNCA `npm`)

**Python**:

- Python 3.12+, type hints donde sea útil
- `uv` (recomendado) o `pip` + `venv`
- Nombres de test en snake_case: `test_should_validate_email`
- Fixtures en `conftest.py` cuando se comparten entre módulos

**Java**:

- JDK 21 LTS
- Maven como build tool por defecto
- `@DisplayName` siempre en methods de test para legibilidad
- Assertions con AssertJ (no solo JUnit assertions)

---

## 🧪 Herramientas por Lenguaje

### JavaScript

| Herramienta | Versión | Propósito                      |
| ----------- | ------- | ------------------------------ |
| Jest        | 29+     | Testing unitario e integración |
| Supertest   | 6+      | Testing de APIs Express        |
| fast-check  | 3+      | Property-based testing         |
| Playwright  | 1.40+   | E2E testing                    |
| k6          | 0.50+   | Performance testing            |

### Python

| Herramienta    | Versión | Propósito                    |
| -------------- | ------- | ---------------------------- |
| pytest         | 8+      | Testing framework principal  |
| pytest-cov     | 4+      | Code coverage                |
| pytest-asyncio | 0.23+   | Testing asíncrono            |
| pytest-mock    | 3+      | Wrapper de unittest.mock     |
| httpx + respx  | latest  | Testing de APIs async        |
| Behave         | 1.2+    | BDD (Gherkin)                |
| hypothesis     | 6+      | Property-based testing       |
| Factory Boy    | 3+      | Factories de datos de prueba |
| Locust         | 2+      | Performance testing          |

### Java

| Herramienta      | Versión | Propósito                   |
| ---------------- | ------- | --------------------------- |
| JUnit 5          | 5.10+   | Testing framework principal |
| Mockito          | 5+      | Mocking                     |
| AssertJ          | 3+      | Aserciones fluentes         |
| Spring Boot Test | 3+      | Testing de Spring Boot      |
| Testcontainers   | 1.19+   | BD reales en tests          |
| Cucumber         | 7+      | BDD (Gherkin)               |
| JaCoCo           | 0.8+    | Code coverage               |

---

## 🎨 Recursos Visuales y Estándares de Diseño

### Formato de Assets

- ✅ SVG para todos los diagramas y gráficos
- ❌ NO usar ASCII art
- ✅ Tema dark sin degradés (colores sólidos)
- ✅ Tipografía sans-serif (Inter, Roboto, Open Sans, System UI)

### Paleta de Colores por Lenguaje

| Lenguaje / Concepto   | Color            | Hex       |
| --------------------- | ---------------- | --------- |
| JavaScript            | Amarillo JS      | `#F7DF1E` |
| Python                | Azul Python      | `#3572A5` |
| Python (acento)       | Amarillo Python  | `#FFD43B` |
| Java                  | Naranja Java     | `#F89820` |
| Testing / Transversal | Índigo           | `#6366f1` |
| Test passing (green)  | Verde            | `#22c55e` |
| Test failing (red)    | Rojo             | `#ef4444` |
| Fondo principal       | Dark slate       | `#0f172a` |
| Fondo secundario      | Dark slate light | `#1e293b` |
| Texto                 | Slate light      | `#f1f5f9` |

### Diagramas Frecuentes en Testing

- Pirámide de testing (triángulo: unitario → integración → E2E)
- Ciclo Red-Green-Refactor (TDD)
- Comparativa de test doubles (mock, stub, spy, fake, dummy)
- Flujo de ejecución de un test (setup → act → assert → teardown)
- Flujo CI/CD con gates de testing
- Comparativa de herramientas entre lenguajes

---

## 🔐 Mejores Prácticas de Testing

### El Patrón AAA (Arrange-Act-Assert)

```javascript
test("should calculate discount", () => {
  // Arrange — preparar datos y dependencias
  const price = 100;
  const percentage = 20;

  // Act — ejecutar la acción a testear
  const result = calculateDiscount(price, percentage);

  // Assert — verificar el resultado
  expect(result).toBe(80);
});
```

### Principios FIRST para Tests

- **F**ast — Los tests deben ser rápidos
- **I**solated — Cada test es independiente, sin orden de ejecución
- **R**epeatable — Mismo resultado en cualquier entorno
- **S**elf-validating — Pass o fail sin interpretación manual
- **T**imely — Escritos junto con (o antes de) el código de producción

### Tests que NO se deben escribir

- ❌ Tests que nunca fallan (siempre pasan sin importar el código)
- ❌ Tests que dependen del orden de ejecución
- ❌ Tests que acceden a recursos externos reales en unit tests
- ❌ Tests con múltiples asserts no relacionados (un test = una razón de fallo)
- ❌ Tests con nombres genéricos: `test1`, `testMethod`, `shouldWork`

### Seguridad en Tests

- ✅ Usar datos de prueba ficticios, nunca datos reales de producción
- ✅ Variables de entorno para credenciales de prueba
- ✅ Limpiar datos entre tests para evitar contaminación
- ❌ NUNCA hardcodear credenciales ni tokens en archivos de test

---

## 📊 Evaluación

Cada semana incluye tres tipos de evidencias:

1. **Conocimiento 🧠 (30%)**: Cuestionarios teóricos sobre conceptos de testing
2. **Desempeño 💪 (40%)**: Ejercicios prácticos con el framework de la semana
3. **Producto 📦 (30%)**: Suite de tests del proyecto semanal del dominio asignado

### Criterios de Aprobación

- Mínimo 70% en cada tipo de evidencia
- Suite de tests funcional y ejecutable
- Tests con nombres descriptivos que documentan el comportamiento
- Implementación coherente con el dominio asignado
- Originalidad: sin copia de suites de otros aprendices

---

## 🚀 Metodología de Aprendizaje

### Estrategias Didácticas

- **Aprendizaje Basado en Proyectos (ABP)**: Suite de tests semanal por dominio
- **Dominios Únicos**: Cada aprendiz testea su dominio asignado
- **Comparación entre lenguajes**: Mismo concepto en JS, Python y Java
- **TDD como disciplina**: Semanas dedicadas a practicar el ciclo R-G-R
- **Code Review**: Revisión de suites entre estudiantes
- **Live Testing**: Sesiones en vivo de testing exploratorio

### Distribución del Tiempo (8h/semana)

| Actividad         | Tiempo      |
| ----------------- | ----------- |
| Teoría            | 2–2.5 horas |
| Prácticas guiadas | 3–3.5 horas |
| Proyecto semanal  | 2–2.5 horas |

---

## 🤖 Instrucciones para Copilot

Cuando trabajes en este proyecto:

### Límites de Respuesta

1. **Divide respuestas largas**
   - ❌ NUNCA generar respuestas que superen los límites de tokens
   - ✅ SIEMPRE dividir contenido extenso en múltiples entregas
   - ✅ Crear contenido por secciones, esperar confirmación del usuario
   - Para semanas completas: dividir por carpetas (`teoria` → `practicas` → `proyecto`)

### Generación de Tests

1. **Usa siempre las herramientas correctas por lenguaje**
   - JavaScript: Jest 29+ con `pnpm`/`yarn` (❌ NUNCA `npm`)
   - Python: pytest 8+ con `uv` o `pip`+`venv`
   - Java: JUnit 5 + AssertJ + Mockito con Maven

2. **Sigue el patrón AAA en todos los tests**

3. **Nomenclatura descriptiva siempre**
   - JS: `it('should [expected] when [condition]')`
   - Python: `def test_[context]_[expected]_when_[condition]()`
   - Java: `@DisplayName("should [expected] when [condition]")`

4. **Cuando generes mocks, especifica el tipo**
   - Mock: reemplaza completamente la dependencia
   - Stub: retorna valores predefinidos
   - Spy: observa llamadas sin reemplazar la implementación real

5. **Bases de datos en tests**
   - ✅ JavaScript: SQLite con `better-sqlite3` o in-memory
   - ✅ Python: SQLite + SQLAlchemy o Testcontainers
   - ✅ Java: H2 in-memory para slices, Testcontainers para integración
   - ❌ NUNCA usar la BD de producción en tests

### Creación de Contenido

1. **Progresión incremental**
   - De lo simple a lo complejo dentro de cada etapa
   - Siempre construir sobre conocimientos previos
   - En semanas multilenguaje (Etapa 0 y 4), mostrar equivalencias

2. **Ejemplos que realmente fallan y pasan**
   - El código en `solution/` debe ejecutarse sin errores
   - El `starter/` comentado debe funcionar al descomentarse
   - Los TODOs del proyecto deben ser implementables con los conceptos de la semana

3. **No mezclar lenguajes en los ejemplos**
   - Semanas 7–15: solo JavaScript
   - Semanas 16–24: solo Python
   - Semanas 25–31: solo Java
   - Semanas 1–6 y 32–36: indicar lenguaje en cada bloque de código

### Respuestas y Ayuda

1. **Cuando el usuario pida generar una semana completa**: dividir en partes y esperar confirmación
2. **Cuando el usuario pida ayuda con un test que falla**: revisar primero si el test expresa correctamente la intención
3. **Referencias recomendadas**:
   - Jest: [jestjs.io](https://jestjs.io)
   - pytest: [docs.pytest.org](https://docs.pytest.org)
   - JUnit 5: [junit.org/junit5](https://junit.org/junit5)
   - Playwright: [playwright.dev](https://playwright.dev)
   - Testcontainers: [testcontainers.com](https://testcontainers.com)

---

## 📚 Referencias Oficiales

- **Jest**: https://jestjs.io/docs/getting-started
- **pytest**: https://docs.pytest.org/en/stable/
- **JUnit 5**: https://junit.org/junit5/docs/current/user-guide/
- **Mockito**: https://site.mockito.org/
- **AssertJ**: https://assertj.github.io/doc/
- **Playwright**: https://playwright.dev/docs/intro
- **Behave**: https://behave.readthedocs.io/
- **Cucumber (Java)**: https://cucumber.io/docs/cucumber/api/?lang=java
- **Testcontainers**: https://testcontainers.com/guides/
- **k6**: https://k6.io/docs/
- **Locust**: https://docs.locust.io/
- **hypothesis**: https://hypothesis.readthedocs.io/
- **OWASP Testing Guide**: https://owasp.org/www-project-web-security-testing-guide/

---

## ✅ Checklist para Nuevas Semanas

Antes de considerar una semana completa:

- [ ] `README.md` con objetivos, distribución del tiempo y navegación
- [ ] Rúbrica de evaluación con 3 tipos de evidencia (Conocimiento/Desempeño/Producto)
- [ ] Archivos de teoría con indicación clara del lenguaje por sección
- [ ] Assets SVG con paleta por lenguaje
- [ ] Ejercicios con formato tutorial (código comentado, NO TODOs)
- [ ] Proyecto con TODOs genéricos (NO usar dominios de la lista anticopia)
- [ ] Recursos: ebooks, videografía, webgrafía
- [ ] Glosario con términos técnicos A–Z con ejemplos por lenguaje
- [ ] Soluciones verificadas y funcionales
- [ ] Navegación anterior/siguiente semana actualizada

---

## 💡 Notas Finales

- **Prioridad**: Tests que documentan el comportamiento > tests que solo ejecutan código
- **Enfoque**: Testing como habilidad de diseño, no solo de verificación
- **Objetivo**: Profesionales que usan testing como herramienta de confianza en producción
- **Filosofía**: Un test que falla por la razón correcta es más valioso que diez tests que siempre pasan

_Última actualización: Marzo 2026 | Versión: 1.0_
