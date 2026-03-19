# Glosario — Semana 02

> Casos de Prueba y Técnicas de Diseño

Términos clave ordenados alfabéticamente. Los términos en inglés se mantienen porque así aparecen en la industria y en el ISTQB.

---

## A

**Acceptance Testing (Prueba de aceptación)**
Nivel de prueba que verifica si el sistema cumple con los criterios de aceptación del cliente o usuario final. Suele ser el último nivel antes de la entrega.

**AAA (Arrange-Act-Assert)**
Patrón estructural de organización interna de un test: preparar los datos (Arrange), ejecutar la acción (Act) y verificar el resultado (Assert).

---

## B

**BDD (Behavior-Driven Development)**
Práctica que describe el comportamiento del sistema en lenguaje natural (Gherkin: Given-When-Then) para que tanto técnicos como no técnicos puedan colaborar en la definición de los tests.

**Boundary Value Analysis (BVA) — Análisis de Valores Límite**
Técnica de diseño de pruebas que se enfoca en los valores en los bordes de las clases de equivalencia. Los errores suelen concentrarse en los límites (off-by-one errors).

*Ejemplo*: Si un campo acepta edades 18–65, se testean los valores 17, 18, 19, 64, 65 y 66.

---

## C

**Caso de prueba (Test case)**
Documento que especifica precondiciones, pasos, datos de prueba y resultado esperado para verificar un comportamiento específico del software.

**Clase de equivalencia (Equivalence class)**
Subconjunto de valores de entrada donde se asume que el sistema se comporta de la misma manera para todos los valores del subconjunto.

**Cobertura de pruebas (Test coverage)**
Métrica que indica qué porcentaje del sistema ha sido ejercitado por los tests. No garantiza ausencia de bugs por sí sola.

**Condición (Condition)**
Expresión booleana que puede evaluarse como verdadera o falsa. En tablas de decisión, las condiciones son las filas de entrada.

---

## D

**Decision Table (Tabla de decisión)**
Técnica de diseño que lista todas las combinaciones posibles de condiciones booleanas y los resultados esperados para cada combinación. Garantiza cobertura exhaustiva de reglas de negocio.

**Defect (Defecto)**
Imperfección en un componente de software que puede causar que el componente no realice su función requerida. Ver también: Bug, Failure, Error.

---

## E

**Edge case (Caso extremo)**
Escenario que ocurre en los límites extremos del dominio de entrada. Los edge cases frecuentemente revelan bugs que los valores típicos no detectan.

**Equivalence Partitioning (Partición de equivalencia)**
Técnica de diseño de pruebas que divide el dominio de entrada en clases donde todos los valores producen el mismo comportamiento del sistema. Permite reducir el número de casos al probar un representativo por clase.

**Exploratory Testing (Testing exploratorio)**
Estilo de testing donde el tester diseña y ejecuta tests simultáneamente, guiado por su conocimiento del sistema y su intuición. No sigue scripts fijos. Complementario al testing scripted.

---

## H

**Happy path**
El flujo principal y exitoso de una funcionalidad, sin errores ni condiciones especiales. Todo funciona exactamente como se espera.

---

## I

**ISTQB (International Software Testing Qualifications Board)**
Organización que define el estándar internacional de certificaciones y terminología en testing de software. Su glosario es la referencia oficial de la industria.

---

## O

**Off-by-one error**
Bug clásico donde un límite se trata incorrectamente: se usa `<` en lugar de `<=`, o viceversa. Es la razón principal por la que el BVA testea valores en los bordes exactos.

**On point**
En BVA, el valor exactamente en el límite de una clase de equivalencia.

**Off point**
En BVA, los valores justo por encima o por debajo del límite. Se testean tanto el off point interior (sigue siendo válido) como el exterior (ya no es válido).

---

## P

**Partición de equivalencia** — ver Equivalence Partitioning

**Precondición (Precondition)**
Estado del sistema que debe existir antes de ejecutar un caso de prueba. Sin precondiciones correctas, el test puede fallar por razones ajenas a lo que se prueba.

**Prioridad (Priority)**
Indicator de la urgencia con la que un defecto debe ser corregido. Se define en términos del negocio. Escala: P1 (máxima urgencia) a P4 (puede esperar).

---

## R

**Regression Testing (Prueba de regresión)**
Conjunto de tests que verifica que funcionalidades que ya funcionaban no se hayan roto por cambios recientes en el código. Se ejecuta ante cualquier modificación del sistema.

**Reproducibility (Reproducibilidad)**
Propiedad de un caso de prueba o bug report que indica que puede ser ejecutado múltiples veces con el mismo resultado por cualquier persona con los mismos pasos y datos.

**Resultado esperado (Expected result)**
Lo que el sistema debe hacer exactamente al finalizar la ejecución del caso de prueba. Debe ser observable, concreto y verificable.

---

## S

**Sad path**
El flujo alternativo de una funcionalidad cuando la entrada es incorrecta o una condición falla. El sistema debe manejar el error de forma controlada.

**Sanity Testing (Prueba de cordura)**
Verificación rápida y enfocada de que un cambio específico no ha introducido errores obvios en la funcionalidad modificada. Menos amplio que el smoke test.

**Scripted Testing (Testing scripted)**
Estilo de testing donde los casos están documentados de antemano con pasos fijos. Permite reproducibilidad, colaboración y trazabilidad. Complementario al testing exploratorio.

**Severity (Severidad)**
Grado de impacto de un defecto sobre el sistema o el usuario. Escala: Critical, High, Medium, Low. No debe confundirse con Prioridad.

**Smoke Testing (Prueba de humo)**
Suite mínima de tests que verifica que las funcionalidades críticas del sistema no están completamente rotas. Se ejecuta inmediatamente después de un deploy o compilación.

**Suite de pruebas (Test suite)**
Colección de casos de prueba relacionados que se agrupan por funcionalidad, módulo o nivel de prueba. Organización básica de un plan de pruebas.

---

## T

**Test Case** — ver Caso de prueba

**Test Plan (Plan de pruebas)**
Documento que define el alcance, enfoque, recursos, cronograma y criterios de salida de una actividad de testing. Responde: ¿qué, cómo, cuándo y quién testea?

**Test Suite** — ver Suite de pruebas

---

## V

**Valor límite** — ver Boundary Value Analysis

**Verificación (Verification)**
Actividad que comprueba que un producto de trabajo cumple con las especificaciones establecidas. *¿Construimos el producto correctamente?*

**Validación (Validation)**
Actividad que comprueba que el producto satisface la necesidad real del usuario. *¿Construimos el producto correcto?*
