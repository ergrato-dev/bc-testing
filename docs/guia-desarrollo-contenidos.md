# 📋 Guía de Desarrollo de Contenidos Semanales

## 🎯 Objetivo

Este documento establece el orden y metodología para desarrollar los contenidos de cada semana del bootcamp de Testing, asegurando consistencia y calidad en todos los materiales educativos para los tres lenguajes (JavaScript, Python y Java).

---

## 📝 Orden de Desarrollo

El desarrollo de contenidos para cada semana **DEBE** seguir este orden estricto:

### 1️⃣ README de la Semana

**Archivo**: `bootcamp/week-XX/README.md`

**Contenido requerido**:

- Título y descripción general
- 🎯 Objetivos de aprendizaje
- 📚 Requisitos previos
- 🗂️ Estructura de la semana
- 📝 Contenidos (con enlaces a teoría/prácticas)
- ⏱️ Distribución del tiempo (8 horas)
- 📌 Entregables
- 🔗 Navegación (semana anterior / siguiente)
- 🏷️ Etiqueta de lenguaje(s) cubiertos en la semana (JS / Python / Java / Transversal)

**Propósito**: Establecer la visión general y alcance de la semana antes de desarrollar contenidos específicos.

---

### 2️⃣ Rúbrica de Evaluación

**Archivo**: `bootcamp/week-XX/rubrica-evaluacion.md`

**Contenido requerido**:

- Criterios de evaluación por tipo de evidencia:
  - 🧠 Conocimiento (30%)
  - 💪 Desempeño (40%)
  - 📦 Producto (30%)
- Niveles de logro: Excelente, Bueno, Suficiente, Insuficiente
- Criterios específicos y medibles
- Mínimo 70% en cada tipo de evidencia para aprobar

**Propósito**: Definir qué se espera del estudiante antes de crear el contenido, asegurando alineación entre objetivos y evaluación.

---

### 3️⃣ Teoría

**Carpeta**: `bootcamp/week-XX/1-teoria/`

**Archivos**: `01-tema.md`, `02-tema.md`, etc.

**Estructura de cada archivo**:

```markdown
# Título del Tema

## 🎯 Objetivos

## 📋 Contenido

## 📚 Recursos Adicionales

## ✅ Checklist de Verificación
```

**Consideraciones**:

- Nomenclatura técnica en inglés (nombres de métodos, clases, librerías, comandos)
- Documentación explicativa en español
- Longitud objetivo: los archivos de teoría deben tener en promedio ~100 líneas por semana (rango sugerido: 80-120 por archivo, según complejidad)
- Ejemplos de código progresivos (simple → complejo)
- Conceptos construidos sobre conocimientos previos
- Indicar claramente a qué lenguaje aplica cada sección (JS, Python, Java o todos)
- Cuando un tema aplica a los tres lenguajes (ej: TDD, BDD, cobertura), mostrar el equivalente en cada uno

**Propósito**: Desarrollar el contenido conceptual antes de crear ejercicios prácticos.

---

### 4️⃣ Assets (y vincularlos a teoría)

**Carpeta**: `bootcamp/week-XX/0-assets/`

**Archivos**:

- Diagramas SVG (`01-nombre.svg`, `02-nombre.svg`, etc.)
- `README.md` describiendo cada asset

**Estándares visuales**:

- Formato: SVG
- Tema: Dark (`#0f172a` como fondo principal)
- Sin degradés (solo colores sólidos)
- Tipografía sans-serif (Inter, Roboto, Open Sans o System UI)
- Nomenclatura técnica en inglés dentro de diagramas
- Etiquetas descriptivas en español

**Paleta de colores por lenguaje**:

- JavaScript: `#F7DF1E` (amarillo JS)
- Python: `#3572A5` (azul Python) + `#FFD43B` (amarillo Python)
- Java: `#F89820` (naranja Java)
- Conceptos transversales: `#6366f1` (índigo)
- Fondos: `#0f172a` / `#1e293b`
- Texto: `#f1f5f9`
- Acentos positivos: `#22c55e` (verde)
- Acentos de error: `#ef4444` (rojo)

**Tipos de diagramas frecuentes en testing**:

- Pirámide de testing
- Flujo Red-Green-Refactor
- Comparativas de herramientas (tabla visual)
- Flujo de ejecución de un test
- Diagrama de dobles de prueba (mock, stub, spy, fake)
- Flujo CI/CD con testing

**Proceso**:

1. Crear assets siguiendo los estándares anteriores
2. Vincular en archivos de teoría con sintaxis: `![Descripción](../0-assets/01-nombre.svg)`
3. Actualizar `0-assets/README.md` con descripción de cada asset

**Propósito**: Enriquecer el contenido teórico con visualizaciones que faciliten el aprendizaje.

---

### 5️⃣ Práctica Guiada

**Carpeta**: `bootcamp/week-XX/2-practicas/`

**Estructura por ejercicio**:

```
ejercicio-XX-nombre/
├── README.md          # Tutorial paso a paso
├── starter/
│   └── index.js       # JS: código comentado para descomentar
│   └── test_starter.py # Python: código comentado para descomentar
│   └── StarterTest.java # Java: código comentado para descomentar
└── solution/
    └── index.js        # Solución completa JS
    └── test_solution.py # Solución completa Python
    └── SolutionTest.java # Solución completa Java
```

> ⚠️ **Nota**: No todos los ejercicios requieren los tres lenguajes. Las semanas 7–15 son solo JS, 16–24 solo Python, 25–31 solo Java. Las semanas 1–6 y 32–36 pueden ser multilenguaje o con el lenguaje indicado.

**Formato de ejercicios — CORRECTO (Tutorial guiado)**:

`README.md`:

```markdown
### Paso 1: Concepto

Explicación del concepto con ejemplo...

**Abre `starter/index.js`** y descomenta la sección correspondiente.
```

`starter/index.js`:

```javascript
// ============================================
// PASO 1: Nombre del Concepto
// ============================================
console.log("--- Paso 1: Nombre del Concepto ---");

// Explicación breve del concepto
// Descomenta las siguientes líneas:
// const result = functionToTest(input);
// console.log('Resultado:', result);

console.log("");
```

`starter/test_starter.py`:

```python
# ============================================
# PASO 1: Nombre del Concepto
# ============================================

# Descomenta las siguientes líneas:
# def test_concepto_basico():
#     result = function_to_test(input)
#     assert result == expected
```

**Formato INCORRECTO — No usar TODOs en ejercicios**:

```javascript
// ❌ Este formato es SOLO para PROYECTOS
const result = null; // TODO: Implementar
```

**Propósito**: Crear ejercicios prácticos que refuercen la teoría mediante descubrimiento guiado.

---

### 6️⃣ Proyecto (ÚNICO ENTREGABLE OBLIGATORIO)

**Carpeta**: `bootcamp/week-XX/3-proyecto/`

> 🎯 **ÚNICO ENTREGABLE**: El proyecto es el único entregable obligatorio de cada semana. Las prácticas y teoría son materiales de estudio que preparan para este proyecto.

**Estructura**:

```
3-proyecto/
├── README.md          # Instrucciones genéricas del proyecto
├── starter/
│   └── (archivos con TODOs según el lenguaje de la semana)
└── solution/
    └── (solución completa — excluida de GitHub con .gitignore)
```

#### 🏛️ Política de Dominios Únicos (Anticopia)

Cada aprendiz recibe un dominio único asignado por el instructor al inicio del trimestre:

- 📖 Biblioteca
- 💊 Farmacia
- 🏋️ Gimnasio
- 🏫 Escuela
- 🏬 Tienda de mascotas
- 🏪 Restaurante
- 🏦 Banco
- 🚕 Agencia de taxis
- 🏥 Hospital
- 🎥 Cine
- 🏨 Hotel
- ✈️ Agencia de viajes
- 🚗 Concesionario de autos
- 👗 Tienda de ropa
- 🛠️ Taller mecánico
- Y otros dominios únicos según cantidad de aprendices

**Objetivo**:

- Prevenir copia entre estudiantes
- Fomentar implementaciones originales
- Aplicar conceptos generales a contextos específicos
- Desarrollar capacidad de abstracción y adaptación

**El instructor debe**:

1. Asignar un dominio único a cada aprendiz al inicio del bootcamp
2. Mantener un registro de dominios asignados
3. No repetir dominios en el mismo grupo
4. Validar que las suites de tests sean coherentes con el dominio

> ⚠️ **IMPORTANTE para desarrollo de contenidos**: Los ejemplos en los proyectos **NO** deben usar dominios de la lista anterior. Usar ejemplos genéricos o dominios diferentes (ej: Museo, Planetario, Acuario). Esto evita "regalar" soluciones a aprendices con esos dominios asignados.

#### 📋 Formato de Proyecto (con TODOs)

A diferencia de los ejercicios, el proyecto **SÍ usa TODOs** para que el estudiante implemente desde cero.

Las instrucciones deben ser genéricas y adaptables a cualquier dominio.

**Ejemplo — `starter/` en JS**:

```javascript
// ============================================
// MÓDULO: ItemService
// Servicio para gestión de elementos del dominio
// ============================================

// NOTA PARA EL APRENDIZ:
// Adapta este servicio a tu dominio asignado.
// Ejemplos:
// - Biblioteca: BookService (libro)
// - Farmacia: MedicineService (medicamento)
// - Gimnasio: MemberService (miembro)

/**
 * @param {Object} item - Elemento a validar
 * @returns {boolean}
 */
const validateItem = (item) => {
  // TODO: Implementar validación específica de tu dominio
  // 1. Verificar que los campos requeridos existen
  // 2. Verificar tipos de datos correctos
  // 3. Verificar reglas de negocio del dominio
};

// ============================================
// TESTS (en el mismo archivo para simplicidad)
// ============================================

// TODO: Escribir tests para validateItem
// Usar describe/test/expect de Jest
// Cubrir: caso válido, campo faltante, tipo incorrecto
```

**El README.md del proyecto debe incluir**:

```markdown
## 🏛️ Proyecto Semanal: [Título Genérico]

> **🎯 ÚNICO ENTREGABLE**: Este proyecto es el **único entregable obligatorio** para aprobar la semana.

### 🎯 Objetivo

Implementar [concepto aprendido] aplicado a tu dominio asignado.

### 📋 Tu Dominio Asignado

**Dominio**: [El instructor te asignará tu dominio al inicio del trimestre]

### ✅ Requisitos Funcionales (Adaptables a tu dominio)

1. ...
2. ...

### 💡 Ejemplos de Adaptación por Dominio

- **Biblioteca**: Testear gestión de libros, préstamos, disponibilidad
- **Farmacia**: Testear gestión de medicamentos, stock, ventas
- **Gimnasio**: Testear gestión de miembros, rutinas, pagos

### 🛠️ Entregables

1. Suite de tests funcional adaptada al dominio
2. README con descripción del dominio y decisiones tomadas
3. Reporte de coverage (si aplica a la semana)
```

**Propósito**: Integrar todos los conocimientos de la semana en una suite de tests funcional adaptada al dominio personal.

---

### 7️⃣ Recursos

**Carpeta**: `bootcamp/week-XX/4-recursos/`

**Subcarpetas**:

- `ebooks-free/` — Libros gratuitos relevantes
- `videografia/` — Videos tutoriales (con enlaces)
- `webgrafia/` — Documentación y artículos

**Contenido de `webgrafia/README.md`**:

```markdown
# 🔗 Webgrafía

## Documentación Oficial

- [Jest Docs](url) - Descripción
- [pytest Docs](url) - Descripción
- [JUnit 5 Docs](url) - Descripción

## Artículos Recomendados

- [Título del artículo](url) - Autor - Descripción

## Tutoriales Interactivos

- [Plataforma - Curso](url) - Descripción
```

**Recursos recurrentes por tema**:

- JavaScript/Jest: [jestjs.io](https://jestjs.io), [testing-library.com](https://testing-library.com)
- Python/pytest: [docs.pytest.org](https://docs.pytest.org), [realpython.com](https://realpython.com)
- Java/JUnit 5: [junit.org/junit5](https://junit.org/junit5), [baeldung.com](https://www.baeldung.com)
- Testing en general: [martinfowler.com](https://martinfowler.com), [testingjavascript.com](https://testingjavascript.com)

**Propósito**: Proporcionar recursos complementarios de calidad para profundizar en los temas.

---

### 8️⃣ Glosario

**Carpeta**: `bootcamp/week-XX/5-glosario/`

**Archivo**: `README.md`

**Formato**:

```markdown
# 📖 Glosario - Semana XX

Términos técnicos clave de esta semana, ordenados alfabéticamente.

---

## A

### Assertion

**Definición**: Verificación que comprueba que un valor cumple una condición esperada en un test.

**Ejemplo** (JavaScript):
\`\`\`javascript
expect(result).toBe(42);
\`\`\`

**Ejemplo** (Python):
\`\`\`python
assert result == 42
\`\`\`

**Ejemplo** (Java):
\`\`\`java
assertEquals(42, result);
\`\`\`

**Ver también**: Matcher, Expect

---

## M

### Mock

**Definición**: Objeto de prueba que simula el comportamiento de una dependencia real, permitiendo verificar interacciones.
...
```

**Propósito**: Consolidar la terminología clave de la semana en un formato de referencia rápida, con ejemplos en los lenguajes que apliquen.

---

## 🔍 Identificación de Patrones

⚠️ **SIEMPRE** revisar semanas anteriores para identificar:

### Patrones de Estructura

- Layout de archivos README
- Formato de títulos y secciones
- Estructura de proyectos de test

### Patrones de Contenido

- Progresión de dificultad dentro de cada etapa
- Estilo de ejemplos de código (siempre funcionales, nunca pseudocódigo en secciones de código)
- Nivel de detalle en explicaciones
- Correspondencia entre lenguajes: cuando un concepto se enseña en los tres, mostrar el equivalente

### Patrones Visuales

- Paleta de colores en assets
- Colores por lenguaje (JS amarillo, Python azul, Java naranja)
- Estilos de diagramas SVG
- Tipografía y diseño

### Semanas de Referencia Clave

- S01-S02: Estructura base para semanas conceptuales (sin código)
- S03-S05: Estructura para semanas de inicio con un lenguaje
- S06, S15, S24, S31, S36: Semanas integradoras (sin teoría nueva, todo práctica)
- S11, S20, S27: Semanas de TDD (misma estructura pero en lenguajes distintos)
- S22, S30: Semanas de BDD (Behave vs Cucumber)

---

## ✅ Checklist de Verificación Final

Antes de considerar una semana completa:

- [ ] README.md con estructura completa y navegación
- [ ] Rúbrica de evaluación con 3 tipos de evidencia
- [ ] Todos los archivos de teoría creados y revisados
- [ ] Assets SVG creados y vinculados en teoría
- [ ] README.md de assets con descripción completa
- [ ] Ejercicios prácticos con formato de tutorial guiado (starter comentado)
- [ ] Proyecto con TODOs genéricos adaptables al dominio
- [ ] Recursos completos (ebooks, videos, webgrafía)
- [ ] Glosario con términos clave ordenados A-Z
- [ ] Nomenclatura técnica en inglés, documentación en español
- [ ] Código funcional y probado (soluciones verificadas)
- [ ] Consistencia con semanas anteriores
- [ ] Ejemplos de proyecto NO usan dominios de la lista anticopia
- [ ] Indicación clara del lenguaje (JS/Python/Java) en cada sección si aplica

---

## 🎨 Estándares de Calidad

### Código

- ✅ Versiones modernas: Jest 29+, pytest 8+, JUnit 5.10+
- ✅ Nombres descriptivos en tests: `should_return_null_when_input_is_empty`
- ✅ Comentarios educativos en español para explicar el "por qué"
- ✅ Ejemplos progresivos (simple → complejo)
- ✅ Tests que fallan por la razón correcta (primero verificar que el test falla)

### Documentación

- ✅ Markdown bien formateado
- ✅ Bloques de código con lenguaje especificado (` ```javascript `, ` ```python `, ` ```java `)
- ✅ Emojis para mejorar legibilidad (con moderación)
- ✅ Español claro y directo
- ✅ Siempre indicar a qué lenguaje aplica cada fragmento de código

### Assets Visuales

- ✅ SVG (no ASCII art)
- ✅ Tema dark sin degradés
- ✅ Tipografía sans-serif
- ✅ Paleta de colores consistente por lenguaje
- ✅ Optimizados antes de incluir (sin metadatos innecesarios)

---

## 📌 Notas Importantes

### Diferencia entre Ejercicios y Proyectos

|            | Ejercicio (2-practicas)              | Proyecto (3-proyecto)              |
| ---------- | ------------------------------------ | ---------------------------------- |
| Formato    | Código comentado para descomentar    | TODOs para implementar             |
| Propósito  | Aprendizaje guiado paso a paso       | Aplicación integral                |
| Dificultad | Incremental por paso                 | Integración completa               |
| Estructura | Tutorial con README detallado        | Instrucciones generales adaptables |
| Lenguaje   | Indicado en la carpeta del ejercicio | Indicado por la etapa del bootcamp |

### Gestores de Paquetes

- ❌ **NUNCA** usar `npm` en proyectos JavaScript
- ✅ **SOLO** usar `pnpm` o `yarn`
- ✅ Python: usar `uv` (recomendado moderno) o `pip` con `venv`
- ✅ Java: usar `Maven` (por defecto) o `Gradle`

### Versiones de Lenguajes y Frameworks

| Tecnología | Versión mínima recomendada |
| ---------- | -------------------------- |
| Node.js    | 22 LTS                     |
| Python     | 3.12+                      |
| Java (JDK) | 21 LTS                     |
| Jest       | 29+                        |
| pytest     | 8+                         |
| JUnit 5    | 5.10+                      |
| Mockito    | 5+                         |
| Playwright | 1.40+                      |
| k6         | 0.50+                      |

### Bases de Datos en Tests

- ✅ **JavaScript/Node.js**: SQLite con `better-sqlite3` para tests de integración
- ✅ **Python**: SQLite con SQLAlchemy o Testcontainers para BD reales
- ✅ **Java**: H2 in-memory para unit/slice tests; Testcontainers (PostgreSQL) para integración real
- ❌ No usar la BD de producción en ningún test automatizado

### Nomenclatura de Tests

**JavaScript (Jest)**:

```javascript
describe('UserService', () => {
  describe('createUser', () => {
    it('should return created user when input is valid', () => { ... });
    it('should throw ValidationError when email is missing', () => { ... });
  });
});
```

**Python (pytest)**:

```python
class TestUserService:
    def test_create_user_returns_user_when_input_is_valid(self): ...
    def test_create_user_raises_validation_error_when_email_is_missing(self): ...
```

**Java (JUnit 5)**:

```java
@Nested
@DisplayName("createUser")
class CreateUser {
    @Test
    @DisplayName("should return created user when input is valid")
    void shouldReturnCreatedUserWhenInputIsValid() { ... }
}
```

---

## 🔗 Referencias

- [Plan de Estudios](./plan-estudios.md)
- [Repo de referencia — BC JavaScript ES2023](https://github.com/ergrato-dev/bc-javascript-es2023-cf)
- [Jest Documentation](https://jestjs.io/docs/getting-started)
- [pytest Documentation](https://docs.pytest.org/en/stable/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Playwright Documentation](https://playwright.dev/docs/intro)
- [k6 Documentation](https://k6.io/docs/)
- [Behave Documentation](https://behave.readthedocs.io/)
- [Cucumber for Java](https://cucumber.io/docs/cucumber/api/?lang=java)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Testcontainers](https://testcontainers.com/guides/)

---

_Última actualización: Marzo 2026 | Versión: 1.0_
