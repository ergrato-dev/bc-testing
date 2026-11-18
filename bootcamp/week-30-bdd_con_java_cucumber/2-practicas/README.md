# Prácticas - Semana 30

Cuatro labs guiados (4 h en total). Cada uno trae un `starter/` con el código de producción completo, un feature con un escenario que ya pasa y los steps de los PASOS comentados. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

Cada lab es un proyecto Maven independiente que hereda de `spring-boot-starter-parent` para no escribir versiones e importa `cucumber-bom` 8.1.0. Los labs 01 y 02 no arrancan Spring; el 03 arranca un contexto sin web y el 04 la API en un puerto aleatorio. Ninguno necesita Docker.

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Primer feature con Cucumber](lab-01-primer-feature-con-cucumber/README.md) | La suite `@Suite`, Gherkin en español, pasos indefinidos y sus fragmentos, `{int}`, `{string}`, texto opcional, esquema del escenario, pasos ambiguos | 60 min |
| [Lab 02 - DataTable y tipos de parámetro](lab-02-datatable-y-tipos-de-parametro/README.md) | `Antecedentes`, `DataTable.asMaps()`, `@DataTableType`, comparar una lista entera, `@ParameterType` | 60 min |
| [Lab 03 - Estado compartido con cucumber-spring](lab-03-estado-compartido-con-cucumber-spring/README.md) | `@CucumberContextConfiguration`, steps con beans por constructor, `@ScenarioScope`, el singleton que arrastra datos, `@Before` y `@After` con `Scenario` | 60 min |
| [Lab 04 - Cucumber con Spring Boot y reportes](lab-04-cucumber-con-spring-boot-y-reportes/README.md) | `RANDOM_PORT` y `RestTestClient` en los steps, `junit-platform.properties`, reportes HTML y JSON, tags y `cucumber.filter.tags` | 60 min |

## Antes de empezar

```bash
java -version   # openjdk version "21..." o superior
mvn -v          # Apache Maven 3.9 o superior
```

Cada lab se ejecuta desde su `starter/`:

```bash
cd lab-NN-.../starter
mvn test
```

Cada PASO tiene dos partes:

1. **El Gherkin**, que está en el README del lab: cópialo al final del `.feature` (o donde diga el paso), con la misma indentación que el resto. Los editores no siempre saben comentar un `.feature`, por eso no va comentado en el archivo.
2. **El código Java**, comentado en el starter. Para descomentarlo, selecciona sus líneas y usa el atajo de comentar de tu editor (`Ctrl+/` en VS Code e IntelliJ): quita un nivel de `//`. Un PASO puede tener varios bloques en el mismo archivo (sus `import`, un campo, un step): descomenta todos los que lleven su número. Cuando un archivo entero pertenece a un PASO, descoméntalo todo menos la línea `package`.

Para leer los `.feature` con colores y saltar de un paso a su step, instala la extensión de Cucumber de tu editor: *Cucumber (Gherkin) Full Support* en VS Code o el plugin *Cucumber for Java* en IntelliJ. No hace falta para ejecutar nada.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 04 - Reportes, tags y documentación viva](../1-teoria/04-reportes-tags-y-documentacion-viva.md) | [Lab 01 - Primer feature con Cucumber](lab-01-primer-feature-con-cucumber/README.md) → |

[↑ Semana 30](../README.md)
