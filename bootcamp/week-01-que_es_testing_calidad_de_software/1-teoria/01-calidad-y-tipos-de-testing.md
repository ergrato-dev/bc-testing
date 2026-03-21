# 01 — Calidad de Software y Tipos de Testing

> **Semana 01 · Etapa 0: Fundamentos** | Transversal (sin código)

---

## ¿Qué es la calidad del software?

La **calidad del software** es el grado en que un sistema satisface los requisitos establecidos **y** las necesidades implícitas del usuario.

No basta con que el software "funcione". Un sistema de calidad es:

| Dimensión | Pregunta clave | Ejemplo |
| --- | --- | --- |
| **Funcionalidad** | ¿Hace lo que debe hacer? | El carrito de compras calcula el total correctamente |
| **Fiabilidad** | ¿Funciona sin fallos? | La app no se cae durante el pago |
| **Usabilidad** | ¿Es fácil de usar? | Un formulario indica claramente qué campos son obligatorios |
| **Rendimiento** | ¿Responde en tiempo razonable? | La página carga en menos de 2 segundos |
| **Seguridad** | ¿Protege los datos? | Las contraseñas están hasheadas, no en texto plano |
| **Mantenibilidad** | ¿Es fácil de modificar? | Cambiar el método de pago no rompe el módulo de envíos |

### Las consecuencias de los bugs

Los errores de software tienen consecuencias reales:

- **2003 — Blackout del noreste de EE.UU.**: un bug en el software de monitoreo eléctrico provocó un apagón que afectó a 55 millones de personas.
- **2012 — Knight Capital Group**: un error de despliegue generó pérdidas de 440 millones de dólares en 45 minutos.
- **2020 — NHS Test and Trace (UK)**: un error de Excel (límite de filas) perdió casi 16.000 registros de infectados de COVID-19.

> **Conclusión**: el testing no es un lujo ni un paso final — es una inversión en confianza y en usuarios.

---

## Los roles en calidad del software

Comprender quién hace qué es fundamental para trabajar en equipos de desarrollo.

### Tester (QA Engineer)

- **Responsabilidad**: verificar que el software cumple los requisitos
- **Enfoque**: encontrar defectos antes de que lleguen al usuario
- **Herramientas**: planes de prueba, casos de prueba, reportes de bugs
- **Perfil**: pensamiento crítico, ojo para el detalle, conocimiento del negocio

### Desarrollador (Developer)

- **Responsabilidad**: construir el software
- **Rol en calidad**: escribir tests unitarios de su propio código
- **Enfoque**: que el código funcione según la lógica implementada
- **Herramienta**: frameworks de testing del lenguaje (Jest, pytest, JUnit)

### SDET (Software Development Engineer in Test)

- **Responsabilidad**: construir la _infraestructura_ de testing
- **Enfoque**: automatización, frameworks de prueba, pipelines de CI
- **Combina**: habilidades de desarrollo + mentalidad de QA
- **Herramientas**: Playwright, k6, Locust, GitHub Actions, Testcontainers

> Este bootcamp te llevará de cero hasta el nivel de **SDET Junior** capaz de automatizar los tres tipos de prueba: unitarias, integración y E2E.

---

## Tipos de testing: manual vs automatizado

### Testing Manual

Un tester ejecuta los casos de prueba **interactuando directamente** con el sistema.

**Ventajas:**
- Detecta problemas de usabilidad y experiencia de usuario
- Flexible para escenarios no previstos
- No requiere conocimiento de programación

**Desventajas:**
- Lento y costoso de escalar
- Propenso a errores humanos por fatiga
- No repetible de forma exacta

**Cuándo usarlo:** exploración inicial, pruebas de usabilidad, escenarios difíciles de automatizar.

---

### Testing Automatizado

Scripts de código ejecutan los casos de prueba **sin intervención humana**.

**Ventajas:**
- Rápido y repetible (mismos pasos, mismo resultado)
- Ejecutable en cada cambio de código (CI/CD)
- Escala sin costo proporcional

**Desventajas:**
- Requiere tiempo inicial de implementación
- El mantenimiento tiene costo cuando el código cambia
- No detecta problemas subjetivos de experiencia de usuario

**Cuándo usarlo:** tests de regresión, suites grandes, cualquier test que se ejecute más de dos veces.

---

### Comparativa rápida

| Criterio | Manual | Automatizado |
| --- | :---: | :---: |
| Velocidad de ejecución | Lenta | Rápida |
| Reproducibilidad | Baja | Alta |
| Costo inicial | Bajo | Medio-alto |
| Costo a largo plazo | Alto | Bajo |
| Detección de UX | Alta | Baja |
| Apto para CI/CD | No | Sí |

---

## Testing preventivo vs correctivo

| Tipo | Definición | Ejemplo |
| --- | --- | --- |
| **Preventivo** | Probar antes o durante el desarrollo para evitar defectos | Escribir el test antes del código (TDD) |
| **Correctivo** | Probar después del desarrollo para encontrar defectos existentes | Suite de regresión al terminar un sprint |

El enfoque moderno es **mayormente preventivo**: escribir tests mientras se construye, no al final.

---

## Introducción a los tres entornos del bootcamp

En las semanas siguientes configuraremos y usaremos tres entornos de testing:

| Entorno | Lenguaje | Framework principal | Gestor |
| --- | --- | --- | --- |
| Node.js 20 LTS | JavaScript | Jest 29 | pnpm |
| Python 3.12 | Python | pytest 8 | uv |
| JDK 21 LTS | Java | JUnit 5.10 | Maven |

> No es necesario instalar nada esta semana. La semana 03 cubre la configuración de entornos.

---

## Resumen

- La **calidad del software** tiene múltiples dimensiones: funcional, fiable, usable, segura.
- Los **bugs tienen consecuencias reales** — el testing protege usuarios y negocios.
- Los roles principales son **Tester**, **Desarrollador** y **SDET** — este bootcamp apunta al perfil SDET.
- **Testing manual** vs **automatizado**: complementarios, con casos de uso distintos.
- **Preventivo** > **Correctivo** en la práctica profesional moderna.

---

**Siguiente**: [02 — Pirámide de Testing y Niveles](./02-piramide-y-niveles.md)
