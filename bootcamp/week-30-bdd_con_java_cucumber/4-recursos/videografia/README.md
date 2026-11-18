# Videografía - Semana 30

Los videos están en inglés y usan Cucumber 7 con JUnit 5 y Spring Boot 3. Lo que cambia con las versiones del curso: el BOM de Cucumber es el 8.1.0 y JUnit es la 6, pero `@Suite`, `@IncludeEngines("cucumber")`, `cucumber-spring` y `@CucumberContextConfiguration` funcionan igual. Si un video usa `@SelectClasspathResource("features")`, en Cucumber 8 es `@SelectPackages("features")` ([teoría 01](../../1-teoria/01-cucumber-en-java.md#la-suite)).

| Recurso | URL | Por qué verlo |
|---|---|---|
| BDD Explained (Behaviour Driven Development) - Modern Software Engineering | https://www.youtube.com/watch?v=zYj70EsD7uI | Dave Farley explica el origen de BDD y por qué un escenario describe comportamiento, no implementación. Si lo viste en la semana 22, vuelve a ver la parte de los escenarios declarativos. |
| The new way to do BDD testing with JUnit 5 and Cucumber - Cătălin Tudose | https://www.youtube.com/watch?v=pHnHyBhK4SI | Cucumber ejecutado por JUnit Platform con una clase `@Suite`, como en el lab 01. |
| How to Add Cucumber to Spring Boot Java Projects | https://www.youtube.com/watch?v=OqhTQSJTeOs | Features, steps y glue en un proyecto Spring Boot existente, como el paso 1 del proyecto. |
| Spring Boot 3 + Cucumber + JUnit 5 Testing | https://www.youtube.com/watch?v=fiEJ7h2OgfI | Escenarios contra una API REST de Spring Boot, como el lab 04. |

## Dinámica sugerida

- Ver el video 2 antes del lab 01.
- Ver los videos 3 y 4 antes del lab 04.
- Ver el video 1 antes del paso 3 del proyecto.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — ebooks](../ebooks-free/README.md) | [Recursos — web](../webgrafia/README.md) → |

[↑ Semana 30](../../README.md)
