# Cobertura, Tipos de Test y Herramientas de Gestión

> **Semana 02 — Teoría 03** | Etapa 0: Fundamentos de Testing | Transversal (JS / Python / Java)

---

## Cobertura de Pruebas: Qué es y qué NO es

### Definición

La **cobertura de pruebas** (test coverage) mide qué porcentaje del sistema ha sido ejercitado por los tests. Existen distintos tipos de cobertura según qué se mide.

### Tipos de cobertura (de menor a mayor rigor)

| Tipo | Qué mide | Nivel |
|---|---|---|
| **Cobertura de requisitos** | % de requisitos con al menos un caso de prueba | Conceptual |
| **Cobertura de código (statement)** | % de líneas de código ejecutadas | Técnico |
| **Cobertura de ramas (branch)** | % de ramas if/else tomadas | Técnico |
| **Cobertura de condiciones** | % de condiciones booleanas evaluadas como T y F | Técnico |
| **Cobertura de rutas** | % de caminos completos del flujo de control | Técnico |

### Lo que la cobertura NO garantiza

> **100% de cobertura de código ≠ ausencia de bugs**

```javascript
// Esta función tiene 100% de cobertura de sentencias
// con solo UN test... pero ese test no verifica
// que el resultado sea correcto

function multiply(a, b) {
  return a + b; // BUG: debería ser a * b
}

// Test con 100% de cobertura de sentencias:
// multiply(2, 3) → ejecuta la función
// ...pero si solo verificamos que "retorna un número",
// el bug nunca se detecta
```

La cobertura mide **cuánto código se ejecuta**, no **si el comportamiento es correcto**. Un test vacío da 100% de cobertura de sentencias.

### Cobertura como señal, no como objetivo

- **Cobertura baja** (< 60%) → hay funcionalidades sin ningún test → riesgo alto
- **Cobertura alta** (> 85%) → la mayoría del código se ejecuta → riesgo controlado
- **100% de cobertura** → puede ser una señal de tests débiles (sin assertions reales)

**Objetivo realista**: ≥ 85% de cobertura de ramas como umbral mínimo de calidad.

> El objetivo es **bugs encontrados**, no el porcentaje de cobertura.

---

## Tipos de Tests por Propósito

### Smoke Test (prueba de humo)

Un conjunto **mínimo** de tests que verifican que las funcionalidades críticas del sistema **no están completamente rotas** antes de invertir tiempo en testing detallado.

**Analogía**: Encender la TV antes de pagar la factura de luz — si no enciende, hay un problema mayor antes de preocuparse por el brillo.

**Cuándo se ejecuta**: Después de cada despliegue o compilación nueva.

**Características**:
- Rápido (minutos)
- Superficial (no profundiza en edge cases)
- Cubre las rutas más críticas: login, home, checkout, API principal
- Si falla → se detiene el proceso, no se continúa con más tests

```
Ejemplo de smoke test suite:
  ✓ La aplicación levanta sin errores
  ✓ La página principal responde HTTP 200
  ✓ El login con credenciales válidas funciona
  ✓ La API responde en /health
```

---

### Sanity Test (prueba de cordura)

Verifica que **un cambio específico** no haya introducido un error obvio en la funcionalidad modificada. Es más enfocado que el smoke test.

**Cuándo se ejecuta**: Luego de un fix o cambio pequeño, para validar rápidamente sin ejecutar toda la suite.

**Diferencia con smoke test**:
- Smoke: ¿el sistema vive? (general)
- Sanity: ¿este fix no rompió esto? (específico)

```
Escenario: Se corrigió el cálculo de descuentos.
Sanity test: ejecutar SOLO los tests del módulo
             de descuentos para verificar el fix.
```

---

### Regression Test (prueba de regresión)

Verifica que **funcionalidades que ya funcionaban** no se hayan roto por nuevos cambios. Es la suite completa que se ejecuta ante cualquier modificación.

**Por qué es crítica**: Los cambios de código en el área A pueden romper silenciosamente el área B. Sin regression testing, los bugs se descubren en producción.

**Características**:
- Más lenta que smoke y sanity (puede durar horas)
- Cubre la mayor parte de la funcionalidad del sistema
- Se ejecuta en CI/CD antes de merges o deploys importantes
- Crece con el tiempo: cada bug corregido → nuevo test de regresión

```
Regla: cada vez que corriges un bug, escribe un test
que lo hubiera detectado. Así nunca vuelve.
```

---

### Testing Exploratorio vs Testing Scripted

| Dimensión | Scripted | Exploratorio |
|---|---|---|
| **Definición** | Tests documentados con pasos fijos | Exploración libre guiada por intuición y experiencia |
| **Documentación** | Casos de prueba formales (semana 2) | Session notes, charters de sesión |
| **Cuándo usar** | Regresión, compliance, auditorías | Nuevas funcionalidades, explorar bordes desconocidos |
| **Ventaja** | Reproducible, colaborativo, trazable | Descubre bugs que los scripts no anticiparon |
| **Limitación** | No descubre lo desconocido | Difícil de reproducir y reportar |

**El tester moderno usa ambos**. Los scripted tests son la base automatizable. El testing exploratorio es el complemento humano que los scripts no pueden reemplazar.

#### Charter de sesión exploratoria (estructura básica)

```
CHARTER: Explorar el módulo de pagos
ÁREA: Flujo de checkout con tarjeta de crédito
DURACIÓN: 45 minutos
OBJETIVO: Encontrar comportamientos inesperados en los límites del monto
NOTAS:
  - ¿Qué pasa con montos de $0.01?
  - ¿Qué pasa con montos que tienen más de 2 decimales?
  - ¿Qué pasa si se cancela a mitad del flujo de 3D Secure?
```

---

## Herramientas de Gestión de Casos de Prueba

En el bootcamp no se requiere ninguna herramienta de gestión paga. Se trabaja con archivos Markdown. Sin embargo, es importante conocer el ecosistema profesional.

### Opciones en el mercado

| Herramienta | Tipo | Descripción |
|---|---|---|
| **TestRail** | SaaS (pago) | La más usada en empresas. Gestión completa de casos, runs y reportes |
| **Zephyr Scale** | Plugin de Jira | Integra casos de prueba directamente en el flujo de Jira |
| **Xray** | Plugin de Jira | Similar a Zephyr, con trazabilidad de requisitos |
| **qTest** | SaaS (pago) | Orientado a flujos ágiles y CI/CD |
| **Notion** | Gratuito | Flexible, útil para equipos pequeños sin herramienta dedicada |
| **Azure Test Plans** | SaaS (en Azure DevOps) | Integrado con el ecosistema Microsoft |
| **Archivo Markdown** | Gratuito | Lo que usamos en este bootcamp — portable y en control de versiones |

### Por qué usamos Markdown en el bootcamp

```
✓ Cero costo
✓ En el repositorio de código → versionado con Git
✓ Legible en GitHub sin herramienta adicional
✓ Portable a cualquier herramienta después
✓ Las convenciones son iguales en cualquier sistema
```

### Campos en herramientas reales vs Markdown

Las herramientas profesionales como TestRail tienen los mismos campos que aprendemos en este bootcamp: ID, título, precondiciones, pasos, resultado esperado, estado, severidad, prioridad. La diferencia es que ofrecen dashboards, trazabilidad y reportes automáticos.

> El conocimiento de diseño de casos es transferible. Las herramientas cambian; los principios no.

---

## Plan de Pruebas (Test Plan) — Introducción

Un **plan de pruebas** define el **alcance, enfoque y estrategia** de testing para un proyecto o release. Es el documento que responde:

- ¿Qué se va a probar?
- ¿Qué NO se va a probar (y por qué)?
- ¿Qué técnicas se van a usar?
- ¿Cuáles son los criterios de entrada y salida?
- ¿Cuántos recursos (tiempo, personas) se necesitan?

Este documento se profundizará en la **Semana 06** (Proyecto Integrador de Etapa 0). Por ahora, es suficiente saber que existe y que sus secciones principales son:

```
Test Plan
├── Alcance (scope): qué funcionalidades se prueban
├── Fuera de alcance: qué se excluye explícitamente
├── Enfoque: técnicas y tipos de tests a usar
├── Criterios de entrada: cuándo empezar a testear
├── Criterios de salida: cuándo el testing está "completo"
├── Riesgos y mitigaciones
└── Cronograma estimado
```

---

## Recapitulación de la Semana

Con las tres teorías de esta semana tienes el toolkit conceptual completo para diseñar pruebas manuales profesionales:

```
01 — Casos de prueba → estructura, campos, precondiciones, resultados esperados
02 — Técnicas de diseño → partición, BVA, tablas de decisión
03 — Cobertura y tipos → qué medir, smoke/sanity/regression, exploratorio
```

En las semanas 3–5 automatizarás estos mismos casos usando Jest, pytest y JUnit 5.

---

← [Técnicas de Diseño](./02-tecnicas-de-diseno.md) | [Volver al README de la semana](../README.md)
