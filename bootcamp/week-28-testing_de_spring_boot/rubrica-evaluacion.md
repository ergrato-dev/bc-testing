# Rúbrica de Evaluación - Semana 28

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Contexto de Spring, slices, `@MockitoBean` y `@TestBean`, contrato de errores, contrato JSON, caché de contextos |
| Desempeño | 40% | Labs 01 a 04 completos, con cada experimento ejecutado y su salida explicada |
| Producto | 30% | La API Spring Boot del dominio, con tests de controlador, de JSON y de extremo a extremo, y el dominio libre de Spring |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica qué carga `@WebMvcTest`, `@JsonTest` y `@SpringBootTest`, y elige uno para un caso dado.
- Diferencia `@MockitoBean` de `@Mock`, y `@MockitoBean` de `@TestBean`.
- Diferencia un 400 de forma de un 422 de regla del dominio, y explica por qué un test de 400 lleva `verifyNoInteractions`.
- Explica por qué el JSON es un contrato y qué cambios lo rompen sin que falle el compilador.
- Explica cuándo Spring reutiliza un contexto y qué problemas trae compartir sus beans.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo de los labs o del proyecto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** usa `@SpringBootTest` para todo, o confunde un slice con un test unitario.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: los cuatro PASOS, el `NullPointerException` del año cambiado, el modo `STRICT` y el `No qualifying bean` explicados.
- Lab 02: los seis PASOS, el cuerpo sin `errors` y los mensajes en dos idiomas explicados.
- Lab 03: los cinco PASOS, la propiedad `non_null` y el campo renombrado detectados por los tests.
- Lab 04: los tres PASOS, el reloj sin reiniciar y el recuento de contextos con la copia y con la clase base.

### Niveles

- **Alto (36-40):** los cuatro labs completos y cada experimento explicado con sus palabras.
- **Medio (28-35):** labs completos con algún experimento sin explicar.
- **Bajo (<28):** labs incompletos o experimentos sin ejecutar.

---

## 3) Producto (30%)

### Criterios

- Las nueve comprobaciones de `verificar-semana.sh 28` pasan.
- El dominio no tiene anotaciones de Spring y la regla `domainDoesNotKnowSpring` lo protege.
- Cada ruta tiene un test de `@WebMvcTest` para la respuesta correcta y otro para cada error, y los de 400 comprueban que el servicio no se llamó.
- Los tests de `@SpringBootTest` sustituyen solo el `Clock`, usan datos propios en cada test y no dependen del orden.
- Los cuatro cambios del paso 7 hacen fallar la suite.
- El `README.md` del repositorio tiene la tabla de rutas y el párrafo sobre qué prueba cada clase de test.

### Niveles

- **Alto (27-30):** cumple todos los criterios.
- **Medio (21-26):** pasa las comprobaciones, pero falta algún test de error o el párrafo del README.
- **Bajo (<21):** alguna comprobación falla, o el dominio depende de Spring.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Semanal - La API Spring Boot del dominio](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 28](README.md)
