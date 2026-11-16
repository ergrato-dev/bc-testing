# Rúbrica de Evaluación - Semana 26

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Mocks, stubbing, `verify`, captores, strict stubbing, spies, `mockStatic` y BDDMockito |
| Desempeño | 40% | Labs 01 a 04 completos, con cada experimento provocado y explicado |
| Producto | 30% | Servicio del dominio con tres dependencias inyectadas y su suite con Mockito |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica qué configura cada pieza del `pom.xml` (las dos dependencias, `maven-dependency-plugin` y el agente).
- Diferencia una entrada indirecta (stub) de una salida indirecta (`verify`) y decide qué verificar.
- Explica los dos errores de strict stubbing y la regla de los matchers.
- Diferencia `@Mock` de `@Spy` y explica por qué en un spy se usa `doReturn`.
- Explica por qué `mockStatic` va en un `try` con recursos y qué cambio de diseño lo evita.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo correcto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** confunde mock con spy, o stub con verificación.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: sustituye los stubs a mano, explica el error de la respuesta por defecto y el de la extensión olvidada.
- Lab 02: el test del PASO 0 que no detecta nada, los mensajes de `times`, de argumentos distintos, de matchers mezclados y de orden.
- Lab 03: los dos errores de strict stubbing, el `mockStatic` sin `try` y la diferencia de tarifa entre mock y spy.
- Lab 04: los cinco tests en forma BDD y el mensaje de `then().should()`.

### Niveles

- **Alto (36-40):** los cuatro labs completos y cada experimento explicado con sus palabras.
- **Medio (28-35):** labs completos con algún experimento sin explicar.
- **Bajo (<28):** labs incompletos o experimentos sin hacer.

---

## 3) Producto (30%)

### Criterios

- Las ocho comprobaciones de `verificar-semana.sh 26` pasan.
- El servicio recibe sus tres dependencias por el constructor y ya no guarda datos en un `Map` propio.
- Los tests de la operación principal cubren el caso feliz, el rechazo de cada dependencia y el fallo del servicio externo.
- Los cuatro cambios del paso 5 hacen fallar la suite.
- El `README.md` del repositorio explica qué dependencias son mocks, cuál es un fake y por qué.

### Niveles

- **Alto (27-30):** cumple todos los criterios.
- **Medio (21-26):** pasa las comprobaciones, pero algún cambio del paso 5 sigue en verde o falta el párrafo.
- **Bajo (<21):** alguna comprobación falla.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Semanal - Aislar las dependencias del dominio con Mockito](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 26](README.md)
