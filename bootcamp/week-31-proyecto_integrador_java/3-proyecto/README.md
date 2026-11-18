# Proyecto Integrador - Suite Java completa del dominio

> **🎯 ÚNICO ENTREGABLE**: este proyecto es el único entregable obligatorio de la semana.

## Objetivo

Cerrar la etapa de Java con una sola suite en `java/` de tu repositorio:

- tests unitarios (`*Test`) en Surefire y tests de integración (`*IT`) en Failsafe;
- escenarios de Cucumber contra la API, en la fase de integración;
- cobertura de JaCoCo de las dos fases de al menos el 85 % de líneas, con el umbral en el `pom.xml`;
- un reporte `testing-report-java.md` con datos reales.

## Dónde va

En tu **[proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)**, el repositorio `qa-<nombre>-<apellido>`, dentro de `java/`. Partes de tu entrega de la [semana 30](../../week-30-bdd_con_java_cucumber/3-proyecto/README.md): la aplicación Spring Boot con los tests de las semanas 25 a 30.

Los ejemplos usan el dominio del museo. Tus clases y tus cifras serán distintas.

## Tiempo

| Parte | Tiempo |
|---|---|
| Desarrollo: pasos 1 a 5 | 6.5 h |
| Presentación: paso 6 | 0.5 h |

## Paso a paso

### Paso 1: inventario y rama

1. Crea la rama de la semana:

   ```bash
   cd qa-<nombre>-<apellido>
   git switch main && git pull
   git switch -c week-31
   ```

2. Copia el starter sin sobrescribir nada:

   ```bash
   cp -rn <ruta-al-bootcamp>/bootcamp/week-31-proyecto_integrador_java/3-proyecto/starter/. .
   ```

   Trae `java/testing-report-java.md`, con `TODO` en cada sección.

3. Haz el inventario de `java/src/test/java`. Para cada clase de test, anota qué arranca:

   | Si la clase... | Fase | Nombre |
   |---|---|---|
   | Solo crea objetos, con o sin mocks: JUnit, AssertJ, Mockito, ArchUnit, repositorios en memoria | Unitaria | `*Test` |
   | Lleva `@WebMvcTest`, `@JsonTest`, `@JdbcTest` o `@SpringBootTest`, o es la suite de Cucumber con `cucumber-spring` | Integración | `*IT` |

   En el museo:

   | Clase | Arranca | Fase |
   |---|---|---|
   | `ReturnFeesTest`, `PieceServiceTest`, `PieceLendingTest`, `PieceReturnTest` | Nada: dominio con Mockito | Unitaria |
   | `InMemoryRepositoriesTest`, `ArchitectureTest` | Nada: contrato en memoria y ArchUnit | Unitaria |
   | `PieceControllerTest`, `JsonContractTest` | Contexto de Spring MVC o de Jackson | Integración |
   | `JdbcPieceRepositoryTest`, `JdbcLoanRepositoryTest` | `@JdbcTest` y PostgreSQL | Integración |
   | `MuseumApiTest`, `RunCucumberTest` | La aplicación completa y PostgreSQL | Integración |

   Las clases abstractas (`PieceRepositoryContract`) y las de apoyo (`TestcontainersConfiguration`, los steps) no se ejecutan solas: no cambian de nombre.

### Paso 2: dos fases con Surefire y Failsafe

Referencia: [lab 01](../2-practicas/lab-01-test-e-it-con-surefire-y-failsafe/README.md).

1. Renombra cada clase de integración con `git mv`, para que git conserve su historia, y cambia el nombre de la clase dentro del archivo:

   ```bash
   cd java/src/test/java/com/bootcamp
   git mv MuseumApiTest.java MuseumApiIT.java
   git mv web/PieceControllerTest.java web/PieceControllerIT.java
   git mv web/JsonContractTest.java web/JsonContractIT.java
   git mv infrastructure/JdbcPieceRepositoryTest.java infrastructure/JdbcPieceRepositoryIT.java
   git mv infrastructure/JdbcLoanRepositoryTest.java infrastructure/JdbcLoanRepositoryIT.java
   git mv bdd/RunCucumberTest.java bdd/RunCucumberIT.java
   ```

   Busca también el nombre viejo en comentarios y en otras clases:

   ```bash
   grep -rn "MuseumApiTest\|PieceControllerTest\|JsonContractTest\|JdbcPieceRepositoryTest\|JdbcLoanRepositoryTest\|RunCucumberTest" ../../../../
   ```

2. Vuelve a `java/` y comprueba qué queda en Surefire:

   ```bash
   cd ../../../../..
   mvn test
   # [INFO] Tests run: 67, Failures: 0, Errors: 0, Skipped: 0
   # [INFO] BUILD SUCCESS
   ```

   En la semana 30 eran 110. Los 43 que faltan son los `*IT`, que ahora no ejecuta nadie.

3. Declara Failsafe en el `pom.xml`, debajo de Surefire:

   ```xml
         <!-- Failsafe: tests de integración (*IT) en mvn verify. Spring Boot fija la versión y sus goals -->
         <plugin>
           <groupId>org.apache.maven.plugins</groupId>
           <artifactId>maven-failsafe-plugin</artifactId>
           <configuration>
             <argLine>@{argLine} -javaagent:${org.mockito:mockito-core:jar} -Xshare:off</argLine>
           </configuration>
         </plugin>
   ```

   El `<argLine>` carga Mockito como agente también en los `*IT`. `@{argLine}` deja sitio al agente de JaCoCo del Paso 3. Hasta entonces la propiedad no existe, la JVM recibe el texto tal cual y Failsafe falla con ``Error: could not open `{argLine}'``. Por eso los pasos 2 y 3 se comprueban juntos al final del Paso 3.

### Paso 3: JaCoCo con umbral

Referencia: [lab 02](../2-practicas/lab-02-jacoco-umbral-y-reporte/README.md).

1. En el `<argLine>` de Surefire de la semana 26, añade `@{argLine} ` al principio:

   ```xml
             <argLine>@{argLine} -javaagent:${org.mockito:mockito-core:jar} -Xshare:off</argLine>
   ```

   Sin esto, los tests unitarios se ejecutan sin JaCoCo y el dominio sale al 0 % ([teoría 01](../1-teoria/01-la-suite-java-completa.md#las-dos-trampas)).

2. Añade JaCoCo debajo de Failsafe, con el informe y el umbral en `verify`:

   ```xml
         <!-- JaCoCo: mide Surefire y Failsafe en target/jacoco.exec, genera el informe y exige el 85 % de líneas -->
         <plugin>
           <groupId>org.jacoco</groupId>
           <artifactId>jacoco-maven-plugin</artifactId>
           <version>0.8.16</version>
           <executions>
             <execution>
               <id>agent</id>
               <goals>
                 <goal>prepare-agent</goal>
               </goals>
             </execution>
             <execution>
               <id>report-and-check</id>
               <phase>verify</phase>
               <goals>
                 <goal>report</goal>
                 <goal>check</goal>
               </goals>
               <configuration>
                 <rules>
                   <rule>
                     <element>BUNDLE</element>
                     <limits>
                       <limit>
                         <counter>LINE</counter>
                         <value>COVEREDRATIO</value>
                         <minimum>0.85</minimum>
                       </limit>
                     </limits>
                   </rule>
                 </rules>
               </configuration>
             </execution>
           </executions>
         </plugin>
   ```

3. Con Docker en marcha, ejecuta la suite completa:

   ```bash
   mvn clean verify
   # [INFO] --- surefire:3.5.6:test (default-test) @ qa-java ---
   # [INFO] Tests run: 67, Failures: 0, Errors: 0, Skipped: 0
   # [INFO] --- failsafe:3.5.6:integration-test (default) @ qa-java ---
   # [INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.319 s -- in com.bootcamp.bdd.RunCucumberIT
   # ...
   # [INFO] Tests run: 43, Failures: 0, Errors: 0, Skipped: 0
   # [INFO] --- jacoco:0.8.16:report (report-and-check) @ qa-java ---
   # [INFO] Analyzed bundle 'qa-java' with 23 classes
   # [INFO] --- jacoco:0.8.16:check (report-and-check) @ qa-java ---
   # [INFO] BUILD SUCCESS
   # [INFO] Total time:  31.297 s
   ```

   67 + 43 = 110: los mismos tests que en la semana 30, ahora en dos fases.

4. Comprueba cuánto cubre cada fase. Sin los `*IT`:

   ```bash
   mvn clean verify -DskipITs
   # [WARNING] Rule violated for bundle qa-java: lines covered ratio is 0.48, but expected minimum is 0.85
   # [INFO] BUILD FAILURE
   ```

   Los unitarios cubren el dominio; `web`, `infrastructure` y `config` solo los cubren los `*IT`. Vuelve a ejecutar `mvn clean verify` antes de seguir.

### Paso 4: al menos el 85 %

1. Convierte `target/site/jacoco/jacoco.csv` en una tabla con el `awk` del [lab 02](../2-practicas/lab-02-jacoco-umbral-y-reporte/README.md#paso-1-añadir-jacoco-paso-1):

   ```bash
   awk -F, 'NR==1 {print "| Clase | Líneas cubiertas | Líneas | Cobertura |\n|---|---:|---:|---:|"}
   NR>1 {m+=$8; c+=$9; printf "| %s | %d | %d | %.0f %% |\n", $3, $9, $8+$9, 100*$9/($8+$9)}
   END {printf "| **Total** | **%d** | **%d** | **%.1f %%** |\n", c, m+c, 100*c/(m+c)}' target/site/jacoco/jacoco.csv
   # | Clase | Líneas cubiertas | Líneas | Cobertura |
   # |---|---:|---:|---:|
   # | ReturnService | 19 | 19 | 100 % |
   # ...
   # | QaApplication | 1 | 3 | 33 % |
   # ...
   # | **Total** | **152** | **154** | **98.7 %** |
   ```

2. Si no llegas al 85 %, abre `target/site/jacoco/index.html`: en cada clase, las líneas rojas no las ejecuta ningún test y las amarillas tienen ramas sin probar. Escribe el test que las cubre en la fase que corresponda a lo que arranca. No bajes el `<minimum>`: la verificación calcula la cifra desde el CSV.

3. Un 100 % no significa que no haya errores: JaCoCo mide las líneas que existen, no las comprobaciones que faltan. Apunta lo que encuentres para el reporte.

### Paso 5: el reporte de testing

Rellena `java/testing-report-java.md` con los datos de tu suite, como en el [lab 02](../2-practicas/lab-02-jacoco-umbral-y-reporte/README.md#paso-5-escribir-el-reporte):

1. **Suite por capas:** una fila por fase, con sus clases, sus tests y el tiempo de `mvn clean verify -DskipITs` y de `mvn clean verify`.
2. **Cobertura:** la tabla del Paso 4 y qué fase cubre qué paquete.
3. **Tests más lentos:** `grep -h "Time elapsed" target/*-reports/*.txt | sort -t: -k6 -rn | head -3`, explicado. Una clase con `@Nested` aparece con `Tests run: 0`: su tiempo es el del contexto, y sus tests están en las clases anidadas.
4. **Riesgos y huecos:** al menos tres cosas que la suite no prueba y que podrían fallar en producción. Busca reglas del dominio que el código no comprueba, errores de la base de datos que la API no traduce y dependencias que solo se prueban con mocks.
5. **Próximos pasos:** los tests que añadirías primero, en orden.

No dejes ningún `TODO`: la verificación los busca.

### Paso 6: verificar, entregar y presentar

1. Ejecuta la verificación desde la carpeta del bootcamp, con Docker en marcha:

   ```bash
   PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 31
   ```

2. Haz commit y push de la rama y abre un pull request `week-31` → `main`.
3. Presenta tu suite en 5 minutos: las dos fases y qué va en cada una, la cifra de cobertura y qué fase la sostiene, la clase más lenta y el riesgo más importante del reporte.

## Ejemplos de adaptación por dominio

| Dominio | Unitaria (`*Test`) | Integración (`*IT`) | BDD (`RunCucumberIT`) |
|---|---|---|---|
| Museo | Seguro y recargos de un préstamo | Piezas y préstamos en PostgreSQL, API | Registro, préstamo y devolución |
| Planetario | Precio de una entrada con descuentos | Funciones y entradas en PostgreSQL, API | Venta y anulación de entradas |
| Acuario | Capacidad de un tanque | Tanques y ejemplares en PostgreSQL, API | Alta y traslado de un ejemplar |

## ✅ Requisitos verificables

`./scripts/verificar-semana.sh 31` comprueba lo siguiente ([checks.json](../checks.json)):

| Comprobación | Cómo se verifica |
|---|---|
| La suite completa pasa | `mvn -B -q clean verify` termina con 0 |
| Surefire ejecuta tests unitarios | Hay al menos un `TEST-*.xml` en `target/surefire-reports/` |
| Failsafe ejecuta tests de integración | Hay al menos un `TEST-*.xml` en `target/failsafe-reports/` |
| Ningún `*Test` arranca Spring | Ningún `*Test.java` lleva `@SpringBootTest`, `@WebMvcTest`, `@JsonTest` ni `@JdbcTest` |
| Cucumber va en la fase de integración | Failsafe tiene un informe `TEST-*Cucumber*IT.xml` |
| El umbral de JaCoCo está en el `pom.xml` | `jacoco-maven-plugin` con el goal `check` y `<minimum>0.85</minimum>` |
| La cobertura de líneas llega al 85 % | La suma de `LINE_COVERED` de `jacoco.csv` es al menos el 85 % de las líneas |
| `testing-report-java.md` tiene sus cinco secciones y ningún `TODO` | Las cinco cabeceras `##` del starter están y no queda ningún `TODO` |

> La `solution/` del proyecto no se publica en el repositorio del bootcamp.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - JaCoCo, umbral y reporte](../2-practicas/lab-02-jacoco-umbral-y-reporte/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 31](../README.md)
