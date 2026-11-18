# Rúbrica de Evaluación - Semana 30

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Cucumber con JUnit Platform, Cucumber Expressions, tablas y tipos de parámetro, estado entre steps con `cucumber-spring`, hooks, reportes, tags y documentación viva |
| Desempeño | 40% | Labs 01 a 04 completos, con cada experimento ejecutado y su salida explicada |
| Producto | 30% | Tres features en español con los flujos principales del dominio, ejecutados contra la API completa con PostgreSQL en un contenedor |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica cómo llega `mvn test` a ejecutar un `.feature`: Surefire, JUnit Platform, la clase `@Suite`, el motor de Cucumber y el glue.
- Lee un paso indefinido y uno ambiguo, y dice cómo resolver cada uno.
- Escribe una expresión con `{int}`, `{string}`, texto opcional y un `@ParameterType` propio.
- Diferencia recibir una tabla como `DataTable`, como `List<T>` con `@DataTableType` y como `Map<K, V>`.
- Explica cuánto viven el contexto de Spring, un singleton, un bean `@ScenarioScope` y una clase de steps, y por qué un bean `@ScenarioScope` se usa con métodos y no con campos.
- Explica por qué los escenarios con `@SpringBootTest` necesitan un `@Before` que limpie.
- Explica para qué sirve `cucumber.junit-platform.naming-strategy=long` y por qué lo filtrado por tags cuenta como saltado.
- Diferencia un paso declarativo de uno imperativo.

### Niveles

- **Alto (27-30):** explica cada concepto con una salida de los labs o del proyecto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** cree que la palabra clave (`Dado`, `Cuando`) decide qué step se ejecuta, o no distingue el estado de un escenario del de toda la ejecución.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: completa los steps a partir de los fragmentos de un paso indefinido y escribe un esquema del escenario.
- Lab 02: recibe tablas con `DataTable` y con `@DataTableType`, compara una lista entera y define `{pesos}`.
- Lab 03: comparte el pedido entre dos clases de steps, encuentra el dato que arrastra el singleton y lo limpia con `@Before`.
- Lab 04: ejecuta escenarios contra la API con `RestTestClient`, genera los reportes y filtra por tags.
- Ejecuta cada experimento, lee el error o la salida que produce y lo explica.

### Niveles

- **Alto (36-40):** los cuatro labs y todos los experimentos, con la explicación de cada salida.
- **Medio (28-35):** los cuatro labs con algún experimento sin hacer o sin explicar.
- **Bajo (<28):** labs incompletos o PASOS descomentados sin ejecutar.

---

## 3) Producto (30%)

### Criterios

- Al menos tres features en español, uno por flujo de negocio del dominio, con al menos 12 escenarios entre todos.
- Los pasos son declarativos: hablan del negocio, no de URLs, JSON ni nombres de campos.
- Los escenarios pasan por la API en un puerto aleatorio y por PostgreSQL en un contenedor, con la base de datos limpia antes de cada uno.
- Los steps están repartidos en varias clases que comparten la respuesta con un bean `@ScenarioScope`, usado con métodos.
- Hay tablas convertidas con `@DataTableType` y al menos un `Esquema del escenario`.
- Los pasos `Dado` comprueban que su preparación salió bien.
- Si un escenario encontró un hueco, se arregló el código con un ciclo de TDD registrado en `TDD-LOG.md`, no el feature.
- El reporte HTML y el JSON quedan en `target/cucumber-reports/`.
- `PROYECTO=... ./scripts/verificar-semana.sh 30` pasa.

### Niveles

- **Alto (27-30):** cumple todos los criterios y el pull request cuenta qué encontró un escenario o qué escenario costó más escribir de forma declarativa.
- **Medio (21-26):** cumple los criterios con algún paso imperativo o algún `Dado` sin comprobar.
- **Bajo (<21):** los features prueban códigos HTTP sueltos en lugar de flujos, o los escenarios dependen del orden en que se ejecutan.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Semanal - Los flujos del dominio en Cucumber](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 30](README.md)
