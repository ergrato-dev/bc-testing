# 01 - Calidad de software y tipos de testing

> Transversal: sin código. Los lenguajes llegan a partir de la [semana 3](../../week-03-primeros_tests_automatizados_con_javascript/README.md).

Antes de escribir un test hace falta saber qué protege. Este archivo responde tres preguntas: qué es la calidad de un software, quién se ocupa de ella en un equipo y de qué formas se prueba.

---

## 🎯 Objetivos

- Definir la calidad del software con sus dimensiones.
- Distinguir los roles de tester, desarrollador y SDET.
- Comparar el testing manual con el automatizado y saber cuándo conviene cada uno.
- Distinguir el testing preventivo del correctivo.

---

## 1. Qué es la calidad del software

La **calidad del software** es el grado en que un sistema cumple sus requisitos escritos **y** lo que el usuario espera aunque nadie lo haya escrito.

Que el software «funcione» no basta. La calidad tiene varias dimensiones:

| Dimensión | Pregunta | Ejemplo |
|---|---|---|
| Funcionalidad | ¿Hace lo que debe? | El carrito calcula bien el total |
| Fiabilidad | ¿Funciona sin fallos? | La app no se cierra durante el pago |
| Usabilidad | ¿Es fácil de usar? | El formulario dice qué campos son obligatorios |
| Rendimiento | ¿Responde a tiempo? | La página carga en menos de 2 segundos |
| Seguridad | ¿Protege los datos? | Las contraseñas se guardan con hash, no en texto plano |
| Mantenibilidad | ¿Es fácil de cambiar? | Cambiar el método de pago no rompe el módulo de envíos |

Este bootcamp las recorre casi todas: funcionalidad desde la semana 3, rendimiento en la [semana 43](../../week-43-testing_de_performance_con_k6_y_locust/README.md) y seguridad en la [semana 44](../../week-44-testing_de_seguridad_basico_owasp/README.md).

### Lo que cuesta un bug

Tres casos documentados:

- **2003, apagón del noreste de Estados Unidos y Canadá.** Un fallo de concurrencia en el software de alarmas de una eléctrica dejó a los operadores sin avisos mientras la red se degradaba. El apagón afectó a unos 50 millones de personas.
- **2012, Knight Capital.** Un despliegue dejó código antiguo activo en uno de sus servidores. En 45 minutos de operaciones automáticas, la empresa perdió unos 440 millones de dólares.
- **2020, registro de casos de COVID-19 en Inglaterra.** Los resultados de laboratorio pasaban por un archivo de Excel en formato antiguo (`.xls`), limitado a 65 536 filas. Se perdieron 15 841 casos positivos de los informes diarios, y sus contactos no se avisaron a tiempo.

En los tres, el problema no era difícil de detectar: nadie lo había probado en las condiciones en que falló.

---

## 2. Los roles en la calidad

| Rol | Qué hace | Con qué |
|---|---|---|
| **Tester (QA Engineer)** | Comprueba que el software cumple los requisitos y encuentra defectos antes que el usuario | Planes y casos de prueba, reportes de bug, exploración |
| **Desarrollador** | Construye el software y escribe los tests unitarios de su código | El framework de su lenguaje: Jest, pytest, JUnit |
| **SDET** (*Software Development Engineer in Test*) | Construye la infraestructura de pruebas: suites automatizadas, frameworks, pipelines | Playwright, k6, Testcontainers, GitHub Actions |

El SDET programa como un desarrollador y piensa como un tester. Es el perfil al que apunta este bootcamp: al terminar, tu repositorio `qa-<nombre>-<apellido>` tendrá suites en cinco lenguajes, E2E, rendimiento, seguridad y CI.

---

## 3. Testing manual y testing automatizado

**Manual:** una persona ejecuta los casos de prueba usando el sistema. **Automatizado:** un programa ejecuta los casos de prueba y compara el resultado con el esperado, sin intervención.

| Criterio | Manual | Automatizado |
|---|---|---|
| Velocidad | Lenta | Rápida |
| Repetición exacta | Difícil: la persona se cansa o se salta pasos | Siempre los mismos pasos |
| Coste inicial | Bajo | Medio o alto: hay que escribir el código |
| Coste de repetirlo | Igual cada vez | Casi cero |
| Detecta problemas de usabilidad | Sí | No |
| Corre en cada cambio (CI) | No | Sí |

Cuándo usar cada uno:

- **Manual:** la primera exploración de una funcionalidad, la usabilidad, los escenarios que todavía no sabes describir.
- **Automatizado:** todo lo que vas a ejecutar más de dos veces, en especial la **regresión**: comprobar que lo que funcionaba sigue funcionando después de un cambio.

No compiten: la exploración manual encuentra el bug, y un test automatizado impide que vuelva.

---

## 4. Testing preventivo y correctivo

| Enfoque | Qué es | Ejemplo |
|---|---|---|
| Preventivo | Probar antes o mientras se escribe el código | Escribir el test antes que el código (TDD, [semana 11](../../week-11-tdd_con_jest/README.md)) |
| Correctivo | Probar el software ya terminado para encontrar defectos | Una ronda de regresión al final de un sprint |

Cuanto antes se encuentra un defecto, menos cuesta arreglarlo: en el editor es un cambio de una línea; en producción es un incidente, un parche y, a veces, un titular. Por eso los equipos actuales prueban sobre todo mientras construyen.

---

## 5. Los entornos de la Etapa 0

De la semana 3 a la 5 escribirás tus primeros tests automatizados en tres lenguajes:

| Semana | Lenguaje | Framework | Gestor |
|---|---|---|---|
| 3 | JavaScript (Node.js 22) | Jest 30.5.2 | pnpm 12.6.0 |
| 4 | Python 3.14 | pytest 9.1.1 | uv |
| 5 | Java (JDK 21) | JUnit 6.0.3 | Maven |

Go y Rust llegan en las etapas 4 y 5. Esta semana no instalas nada: cada semana explica la instalación de lo que usa.

---

## ✅ Resumen

- La calidad tiene varias dimensiones: funcionalidad, fiabilidad, usabilidad, rendimiento, seguridad y mantenibilidad.
- Los bugs tienen coste real, y casi siempre fallan en condiciones que nadie probó.
- Tester, desarrollador y SDET comparten la calidad; el SDET construye la automatización.
- Manual y automatizado se complementan: el manual explora, el automatizado repite.
- Probar mientras se construye (preventivo) cuesta menos que probar al final (correctivo).

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Semana 01 - ¿Qué es Testing? Calidad de Software](../README.md) | [Teoría — 02 - La pirámide de testing y los niveles de prueba](02-piramide-y-niveles.md) → |

[↑ Semana 01](../README.md)
