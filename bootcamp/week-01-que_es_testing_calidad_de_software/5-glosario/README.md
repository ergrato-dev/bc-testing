# Glosario — Semana 01
## Calidad de Software y Fundamentos de Testing

Términos ordenados alfabéticamente. Todos los ejemplos son de dominio transversal.

---

## A

**Acceptance Testing** (Prueba de aceptación)
Nivel de testing que verifica si el sistema cumple los criterios de aceptación del negocio. Generalmente escrito en lenguaje natural (Given/When/Then). Lo ejecuta el cliente, el equipo de QA o el Product Owner antes de aprobar un lanzamiento.

**AAA (Arrange-Act-Assert)**
Patrón de estructura de un test unitario: preparar los datos (Arrange), ejecutar la acción (Act), verificar el resultado (Assert). Estándar de legibilidad en todos los lenguajes.

---

## B

**Bug**
Sinónimo de defecto. Problema en el código que puede producir un fallo (failure) cuando el sistema se ejecuta. Ver también: *defect*, *error*, *failure*.

**Bug Report**
Documento formal que describe un defecto encontrado en el software, incluyendo pasos para reproducirlo, resultado obtenido, resultado esperado, severidad y prioridad.

---

## C

**Caso de prueba** (Test case)
Conjunto de condiciones bajo las que un tester verificará si una funcionalidad cumple los requisitos. Incluye: precondiciones, pasos, datos de entrada, resultado esperado.

**Code Coverage** (Cobertura de código)
Métrica que mide qué porcentaje del código de producción es ejercitado por los tests. Se expresa en porcentaje. Una cobertura del 80% significa que el 20% del código no es ejecutado por ningún test.

**Critical** (Severidad crítica)
Nivel máximo de severidad. El sistema no puede funcionar — crash total, pérdida de datos, o flujo principal completamente bloqueado.

---

## D

**Defect** (Defecto)
El problema en el código fuente: una instrucción incorrecta, una condición mal escrita, una lógica invertida. Es la *causa* del fallo observable. Ver también: *bug*, *failure*.

**Defect Lifecycle** (Ciclo de vida del defecto)
Los estados por los que pasa un bug desde que se reporta hasta que se cierra: New → Open → In Progress → Fixed → Retest → Verified → Closed. Incluye flujos alternativos: Won't Fix, Duplicate, Reopened.

---

## E

**E2E Testing** (End-to-End Testing)
Nivel de testing que simula flujos completos del usuario real: desde la interfaz hasta la base de datos y de vuelta. Lento pero valioso para validar que todo el sistema funciona en conjunto.

**Error**
Acción humana (del desarrollador) que introduce un defecto en el código. El error es la equivocación; el defecto es su resultado en el código; el fallo es el comportamiento visible.

---

## F

**Failure** (Fallo)
El comportamiento incorrecto observable en el sistema en tiempo de ejecución. Es el *efecto* de un defecto. Ejemplo: el sistema cobra el doble al usuario (failure) porque la función de cálculo tiene un signo incorrecto (defect).

**FIRST** (principios)
Acrónimo para los cinco principios de un buen test unitario: **F**ast, **I**solated, **R**epeatable, **S**elf-validating, **T**imely.

---

## H

**Happy Path** (Camino feliz)
El flujo principal de una funcionalidad cuando todo va bien — inputs válidos, condiciones normales, sin errores. Es el primer caso de prueba que se escribe, pero no el único.

---

## I

**Integration Testing** (Prueba de integración)
Nivel de testing que verifica la interacción entre dos o más componentes del sistema (ej: servicio + base de datos, API + cliente HTTP). Más lento que el unitario, pero detecta problemas en los bordes entre componentes.

---

## M

**Manual Testing** (Testing manual)
Ejecución de casos de prueba por un ser humano que interactúa directamente con el sistema, sin scripts automatizados. Valioso para exploración y pruebas de usabilidad.

---

## P

**Pirámide de Testing**
Modelo visual que representa la proporción ideal de tests: muchos tests unitarios (~70%), moderados de integración (~20%), pocos E2E (~10%). Más unitarios = más rápido, más barato, más confiable.

**Priority** (Prioridad)
Urgencia con la que debe resolverse un bug, según el impacto en el negocio. Escala: P1 (hoy), P2 (este sprint), P3 (próximos sprints), P4 (backlog). Diferente de la severidad.

**P1 / P2 / P3 / P4**
Escala de prioridad de un bug. P1 = resolver hoy. P4 = backlog sin urgencia.

---

## Q

**QA Engineer** (Quality Assurance Engineer)
Profesional responsable de verificar que el software cumple los requisitos de calidad. Se enfoca en encontrar defectos antes de que lleguen al usuario.

---

## R

**Regression Testing** (Prueba de regresión)
Suite de tests que se ejecuta después de cada cambio en el código para asegurar que las funcionalidades existentes no se rompieron. Es el caso de uso principal para la automatización.

**Reopened**
Estado en el ciclo de vida de un bug: el tester verificó la corrección y el bug sigue presente — se reabre y vuelve al desarrollador.

**Reproducibility** (Reproducibilidad)
Capacidad de un reporte de bug para permitir a cualquier persona seguir los pasos y obtener el mismo fallo. Un bug no reproducible es muy difícil o imposible de corregir.

---

## S

**SDET** (Software Development Engineer in Test)
Ingeniero que combina habilidades de desarrollo con mentalidad de QA. Construye frameworks de testing, pipelines CI/CD y herramientas de automatización. Perfil objetivo de este bootcamp.

**Severity** (Severidad)
Impacto técnico de un bug en el sistema. Escala: Critical, High, Medium, Low. Diferente de la prioridad (que mide urgencia de negocio).

**Smoke Testing**
Suite mínima de tests que verifica que las funcionalidades más críticas del sistema funcionan tras un despliegue. Si falla el smoke test, no se continúa con el resto de la suite.

---

## T

**Test Case** → ver *Caso de prueba*

**Test Suite**
Colección de casos de prueba relacionados que se ejecutan juntos. Una suite puede cubrir un módulo, una funcionalidad o todo el sistema.

**Testing Automatizado** → ver *Automated Testing*

**Testing Manual** → ver *Manual Testing*

**Testing Preventivo**
Enfoque en el que los tests se escriben antes o durante el desarrollo, con el objetivo de prevenir defectos. TDD es el ejemplo más riguroso. Contrasta con el testing correctivo.

---

## U

**Unit Testing** (Prueba unitaria)
Nivel de testing que verifica la unidad mínima de código (función, método, clase) en aislamiento, sin dependencias externas. Es el nivel más rápido, más barato y el más abundante en la pirámide.

---

## V

**Verified**
Estado en el ciclo de vida de un bug: el tester confirmó que la corrección del desarrollador funciona correctamente. El bug queda listo para cerrar.

---

## W

**Won't Fix**
Estado alternativo en el ciclo de vida de un bug: el equipo decide no corregirlo. Motivos frecuentes: baja prioridad, costo de corrección desproporcionado, comportamiento fuera del alcance del producto, o duplicado de otro bug ya abierto.
