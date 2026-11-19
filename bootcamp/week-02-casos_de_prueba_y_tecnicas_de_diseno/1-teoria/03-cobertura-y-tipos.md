# 03 - Cobertura, suites y testing exploratorio

> Transversal: sin código.

Ya sabes escribir un caso y elegir sus datos. Esta teoría responde a tres preguntas que salen en cuanto tienes cien casos: ¿cuánto del sistema cubren?, ¿cuáles ejecuto hoy? y ¿qué pasa con lo que no está en ningún caso?

---

## 🎯 Objetivos

- Distinguir cobertura de requisitos y cobertura de código, y explicar lo que ninguna garantiza.
- Distinguir smoke, sanity y regresión por cuándo se ejecutan y qué incluyen.
- Comparar testing guionizado y exploratorio, y escribir el charter de una sesión exploratoria.
- Conocer las herramientas de gestión de casos y las secciones de un plan de pruebas.

---

## 1. Cobertura

La **cobertura** mide qué parte de algo ejercitan los tests. Hay dos familias, según qué sea ese «algo».

### Cobertura de requisitos

Qué porcentaje de los requisitos tiene al menos un caso de prueba. Se lleva con una **matriz de trazabilidad**: una tabla requisito por caso.

| Requisito | Casos | Cubierto |
|---|---|---|
| R1 La edad va de 18 a 65 | TC-REG-001 a TC-REG-007 | Sí |
| R2 El nombre tiene de 2 a 50 letras | TC-REG-008 a TC-REG-013 | Sí |
| R3 El email no se puede repetir | (ninguno) | No |

Cobertura de requisitos: 2 de 3, un 67 %. La fila vacía dice qué caso escribir a continuación. Es la cobertura de esta semana: no hace falta código.

### Cobertura de código

Qué porcentaje del código ejecutan los tests automatizados. Las dos medidas más usadas:

| Medida | Qué cuenta |
|---|---|
| **Sentencias** (statements) | Las líneas de código que se ejecutaron al menos una vez |
| **Ramas** (branches) | Los caminos de cada `if`: cuántos se tomaron, del total |

```javascript
function precio(base, esPremium) {
  let total = base;
  if (esPremium) {
    total = base * 0.9;
  }
  return total;
}
```

Un test con `precio(100, true)` ejecuta todas las líneas: 100 % de sentencias. Pero el `if` tiene dos caminos, entrar y no entrar, y solo se tomó uno: 50 % de ramas. Hace falta otro test con `esPremium` falso. Por eso la cobertura de ramas es más exigente.

La medirás con herramientas en la [semana 14](../../week-14-coverage_y_calidad_de_suites_en_jest/README.md) (Jest) y la [21](../../week-21-coverage_y_calidad_en_python/README.md) (Python).

### Lo que la cobertura no dice

La cobertura cuenta **qué se ejecutó**, no **qué se comprobó**.

```javascript
function multiplicar(a, b) {
  return a + b; // bug: debería ser a * b
}
```

Un test que comprueba que `multiplicar(2, 2)` devuelve `4` pasa: 2 + 2 y 2 × 2 dan lo mismo. La función tiene el 100 % de sentencias y de ramas, y el bug sigue ahí. Con `multiplicar(2, 3)` el test esperaría `6`, recibiría `5` y fallaría. El dato elegido importa más que el porcentaje: es lo que hacen las técnicas de la [teoría 02](02-tecnicas-de-diseno.md).

Lo mismo con los requisitos: R1 tiene casos, pero si son solo el 35 y el 80, el error del 18 pasa.

Cómo leer la cobertura:

- **Baja:** hay partes que ningún test toca. Es un dato útil: ahí seguro que no se encontrará nada.
- **Alta:** todo se ejecuta, pero no dice si las comprobaciones son buenas.
- **Como objetivo:** si el equipo persigue un número, aparecen tests que ejecutan sin comprobar. En el bootcamp las suites piden un mínimo del 85 % como red, no como meta.

---

## 2. Smoke, sanity y regresión

Tres maneras de elegir qué casos ejecutar, según el momento.

| Tipo | Pregunta | Cuándo | Qué incluye | Duración |
|---|---|---|---|---|
| **Smoke** | ¿Arranca y se puede usar? | Tras cada despliegue o build nueva | Las funciones críticas, solo el happy path | Minutos |
| **Sanity** | ¿El cambio hace lo que debía? | Tras un arreglo o cambio pequeño | Los casos de la zona cambiada | Minutos |
| **Regresión** | ¿Se ha roto algo que funcionaba? | Antes de cada release o merge importante | Todo, o casi todo | De minutos a horas |

### Smoke

El nombre viene de la electrónica: enciendes la placa y miras si sale humo. Si falla, no se sigue probando: la build no sirve y vuelve a desarrollo.

```text
Smoke de una tienda online:
- La portada responde
- El login con un usuario válido funciona
- Se puede añadir un producto al carrito
- El checkout llega a la pantalla de pago
```

### Sanity

Tras arreglar el cálculo de descuentos, se ejecutan los casos de descuentos y poco más. Es estrecha y rápida: comprueba que el arreglo es razonable antes de gastar horas en la regresión.

### Regresión

Una **regresión** es algo que funcionaba y deja de funcionar por un cambio en otra parte. La suite de regresión existe para pillarlas, y crece con el tiempo: cada bug corregido añade un caso que lo habría detectado, para que no vuelva.

Es la suite que más compensa automatizar: se ejecuta muchas veces con los mismos pasos (lo viste en la [teoría 01 de la semana 1](../../week-01-que_es_testing_calidad_de_software/1-teoria/01-calidad-y-tipos-de-testing.md#3-testing-manual-y-testing-automatizado)).

Las tres no son suites separadas. Son selecciones de la misma colección de casos: la smoke son los happy paths de prioridad alta; la sanity, los del módulo tocado.

---

## 3. Testing guionizado y testing exploratorio

| | Guionizado (scripted) | Exploratorio |
|---|---|---|
| **Qué es** | Ejecutar casos escritos antes, paso a paso | Diseñar y ejecutar a la vez, decidiendo el siguiente paso por lo que ves |
| **Documento** | Casos de prueba | Un charter antes y notas durante la sesión |
| **Sirve para** | Regresión, auditorías, lo que se repite | Funciones nuevas, zonas sin casos, buscar lo que nadie pensó |
| **Fuerte en** | Repetible y medible | Encuentra bugs que ningún caso había previsto |
| **Débil en** | Solo encuentra lo que alguien previó | Depende de quien prueba; sin notas, no se puede repetir |

Exploratorio no es «probar a lo loco». Se hace en **sesiones** de tiempo fijo, con un objetivo escrito antes: el **charter**.

```text
Charter:   Explorar el importe del checkout
Con:       importes con céntimos, cero, negativos y muy grandes
Para:      descubrir importes que el sistema acepte o redondee mal
Duración:  45 minutos

Notas (durante la sesión):
- 10:05  0,01 € se acepta y el resumen muestra 0,01 €. Ok.
- 10:12  0,015 € se redondea a 0,02 € en el resumen y a 0,01 € en el pago. Bug.
- 10:20  -5 € deja pulsar "Pagar"; el error sale después. Pregunta para producto.
```

Las notas convierten la sesión en algo útil: el redondeo es un reporte de bug, la pregunta va a quien define el producto y los importes probados se pueden volver casos guionizados para la regresión.

Las dos formas se complementan. Los casos protegen lo que ya sabes que importa; la exploración encuentra lo que todavía no sabías.

---

## 4. Herramientas de gestión de casos

En un equipo, los casos viven en una herramienta que guarda su historia de ejecuciones y los enlaza con requisitos y bugs:

| Herramienta | Tipo |
|---|---|
| TestRail | Aplicación web de pago, independiente |
| Zephyr y Xray | Extensiones de pago para Jira |
| Azure Test Plans | Parte de Azure DevOps |
| Una hoja de cálculo o Notion | Lo que usan muchos equipos pequeños |

En el bootcamp los casos se escriben en **Markdown**: no cuesta nada, va en el repositorio junto al código, Git guarda su historia y GitHub lo muestra sin instalar nada. Los campos son los mismos que en cualquier herramienta: quien sabe escribir un buen caso en Markdown lo sabe escribir en TestRail.

---

## 5. El plan de pruebas

Cuando hay que probar una release entera, los casos se organizan en un **plan de pruebas**: qué se prueba, qué no, cómo, y cuándo se da por terminado.

```text
Plan de pruebas
|-- Alcance: qué funciones se prueban
|-- Fuera de alcance: qué no, y por qué
|-- Enfoque: técnicas y tipos de prueba
|-- Criterios de entrada: cuándo se puede empezar
|-- Criterios de salida: cuándo se da por terminado
`-- Riesgos: qué puede salir mal y qué se hará
```

Lo escribirás completo en la [semana 6](../../week-06-plan_de_pruebas_y_suite_base_multilenguaje/README.md).

---

## ✅ Resumen

- La cobertura de requisitos se lleva con una matriz de trazabilidad; la de código, con herramientas (sentencias y ramas). Ninguna dice si las comprobaciones son buenas.
- Smoke: ¿arranca? Sanity: ¿el cambio funciona? Regresión: ¿se ha roto algo? Son selecciones de la misma colección de casos.
- El exploratorio se hace en sesiones con charter y notas; encuentra lo que los casos no previeron.
- Los campos de un caso son los mismos en Markdown y en TestRail. El plan de pruebas los organiza para una release.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 02 - Técnicas de diseño: partición, valores límite y tablas de decisión](02-tecnicas-de-diseno.md) | [Índice de labs](../2-practicas/README.md) → |

[↑ Semana 02](../README.md)
