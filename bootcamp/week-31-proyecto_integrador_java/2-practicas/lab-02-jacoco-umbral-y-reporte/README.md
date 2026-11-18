# Lab 02 - JaCoCo, umbral y reporte

## Objetivo

Medir con JaCoCo la cobertura de Surefire y Failsafe en una sola cifra, hacer que el build falle por debajo del 85 % y escribir el reporte de testing con los datos de esa ejecución.

## Tiempo estimado

75 minutos.

## Preparación

El código es el del [lab 01](../lab-01-test-e-it-con-surefire-y-failsafe/README.md) con Failsafe ya declarado. Docker debe estar en marcha.

```bash
cd starter
mvn verify
# [INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
# Mockito is currently self-attaching to enable the inline-mock-maker. ...
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

Qué cambia respecto al lab 01:

- **`pom.xml`:** Surefire carga Mockito como agente con un `<argLine>`, igual que en la [semana 26](../../../week-26-mockito_mocking_en_java/README.md). Por eso el aviso de Mockito ya solo sale una vez: en Failsafe, que no tiene `<argLine>`.
- **`testing-report-java.md`:** la plantilla del reporte, con `TODO` en cada sección.

## Paso a paso

### Paso 1: añadir JaCoCo (PASO 1)

1. En `pom.xml`, sustituye el comentario `<!-- PASO 1: ... -->` por:

   ```xml
         <!-- JaCoCo: mide Surefire y Failsafe en target/jacoco.exec y genera el informe en mvn verify -->
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
               </goals>
             </execution>
           </executions>
         </plugin>
   ```

   - **`prepare-agent`:** define la propiedad `argLine` con el agente de JaCoCo. Surefire y Failsafe pasan esa propiedad a la JVM de los tests.
   - **`report`:** en `verify`, después de Failsafe, lee `target/jacoco.exec` y escribe el informe en `target/site/jacoco/`.

2. Mide:

   ```bash
   mvn clean verify
   # [INFO] --- jacoco:0.8.16:prepare-agent (agent) @ lab-02 ---
   # [INFO] argLine set to -javaagent:~/.m2/repository/org/jacoco/org.jacoco.agent/0.8.16/org.jacoco.agent-0.8.16-runtime.jar=destfile=.../target/jacoco.exec
   # [INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
   # Mockito is currently self-attaching to enable the inline-mock-maker. ...
   # [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
   # [INFO] --- jacoco:0.8.16:report (report-and-check) @ lab-02 ---
   # [INFO] Loading execution data file .../target/jacoco.exec
   # [INFO] Analyzed bundle 'lab-02' with 6 classes
   # [INFO] BUILD SUCCESS
   ```

   `clean` borra `target/`, también el `jacoco.exec` de la ejecución anterior. El Paso 3 explica por qué importa.

3. El informe HTML está en `target/site/jacoco/index.html`: ábrelo en el navegador. Para leer la cobertura en la terminal, JaCoCo también escribe `target/site/jacoco/jacoco.csv`, con una fila por clase. Este `awk` la convierte en una tabla de Markdown:

   ```bash
   awk -F, 'NR==1 {print "| Clase | Líneas cubiertas | Líneas | Cobertura |\n|---|---:|---:|---:|"}
   NR>1 {m+=$8; c+=$9; printf "| %s | %d | %d | %.0f %% |\n", $3, $9, $8+$9, 100*$9/($8+$9)}
   END {printf "| **Total** | **%d** | **%d** | **%.1f %%** |\n", c, m+c, 100*c/(m+c)}' target/site/jacoco/jacoco.csv
   # | Clase | Líneas cubiertas | Líneas | Cobertura |
   # |---|---:|---:|---:|
   # | LabApplication | 1 | 3 | 33 % |
   # | LoanService | 0 | 10 | 0 % |
   # | LateFees | 2 | 3 | 67 % |
   # | LibraryController | 9 | 9 | 100 % |
   # | BookRepository | 14 | 14 | 100 % |
   # | Book | 1 | 1 | 100 % |
   # | **Total** | **27** | **40** | **67.5 %** |
   ```

   | En el `awk` | Qué es |
   |---|---|
   | `-F,` | Separa las columnas por comas |
   | `$3`, `$8`, `$9` | Columnas `CLASS`, `LINE_MISSED` y `LINE_COVERED` del CSV |
   | `NR==1`, `NR>1`, `END` | La cabecera, cada fila de datos y el final, donde se imprime el total |

`LoanService` al 0 %, y `LoanServiceTest` pasa sus dos tests. `LateFees` al 67 %, y `LateFeesTest` prueba los días negativos. Lo que sí se ha medido es lo que ejecutan los `*IT`: el controlador y el repositorio.

### Paso 2: `@{argLine}` (PASO 2)

El `<argLine>` de Surefire es el culpable: al escribirlo en la configuración, sustituye a la propiedad `argLine` que definió `prepare-agent`. La JVM de los tests unitarios arranca con Mockito y sin JaCoCo, y no hay ningún aviso ([teoría 01](../../1-teoria/01-la-suite-java-completa.md#las-dos-trampas)).

1. En Surefire, añade `@{argLine} ` al principio del `<argLine>`:

   ```xml
             <argLine>@{argLine} -javaagent:${org.mockito:mockito-core:jar} -Xshare:off</argLine>
   ```

2. Dale a Failsafe la misma configuración, para que los `*IT` también carguen Mockito:

   ```xml
         <plugin>
           <groupId>org.apache.maven.plugins</groupId>
           <artifactId>maven-failsafe-plugin</artifactId>
           <configuration>
             <argLine>@{argLine} -javaagent:${org.mockito:mockito-core:jar} -Xshare:off</argLine>
           </configuration>
         </plugin>
   ```

3. Cambia el comentario de encima de Surefire por `<!-- Surefire (*Test) y Failsafe (*IT) cargan Mockito como agente -->`.

4. Mide otra vez:

   ```bash
   mvn clean verify
   # [INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
   # [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
   # [INFO] Analyzed bundle 'lab-02' with 6 classes
   # [INFO] BUILD SUCCESS
   ```

   Con el `awk` del Paso 1:

   ```text
   | Clase | Líneas cubiertas | Líneas | Cobertura |
   |---|---:|---:|---:|
   | LabApplication | 1 | 3 | 33 % |
   | LoanService | 10 | 10 | 100 % |
   | LateFees | 3 | 3 | 100 % |
   | LibraryController | 9 | 9 | 100 % |
   | BookRepository | 14 | 14 | 100 % |
   | Book | 1 | 1 | 100 % |
   | **Total** | **38** | **40** | **95.0 %** |
   ```

`@{argLine}` se resuelve justo antes de lanzar la JVM, cuando `prepare-agent` ya ha definido la propiedad. El aviso de Mockito desaparece de los dos plugins. Las dos líneas que faltan son el `main` de `LabApplication`: ningún test arranca la aplicación con `main`.

### Paso 3: el umbral (PASO 3)

1. En la ejecución `report-and-check` de JaCoCo, añade el *goal* `check` debajo de `report`, y la configuración con la regla debajo de `</goals>`:

   ```xml
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
   ```

   | Elemento | Qué dice |
   |---|---|
   | `BUNDLE` | La regla se aplica al proyecto entero, no a cada clase |
   | `LINE` y `COVEREDRATIO` | Mide la proporción de líneas cubiertas |
   | `0.85` | El mínimo: el 85 % |

2. Con la suite completa, el 95 % pasa:

   ```bash
   mvn clean verify
   # [INFO] BUILD SUCCESS
   ```

3. Mide sin los `*IT`, como si no tuvieras Docker, pero sin `clean`:

   ```bash
   mvn verify -DskipITs
   # [INFO] BUILD SUCCESS
   ```

   Pasa porque `jacoco.exec` aún tiene lo que cubrieron los `*IT` de la ejecución anterior: el agente añade datos al archivo, no lo vacía.

4. Ahora con `clean`:

   ```bash
   mvn clean verify -DskipITs
   # [WARNING] Rule violated for bundle lab-02: lines covered ratio is 0.35, but expected minimum is 0.85
   # [INFO] BUILD FAILURE
   # [ERROR] Failed to execute goal org.jacoco:jacoco-maven-plugin:0.8.16:check (report-and-check) on project lab-02: Coverage checks have not been met. See log for details. -> [Help 1]
   echo $?
   # 1
   ```

Sin los `*IT`, `LibraryController` y `BookRepository` quedan al 0 % y la cobertura baja al 35 %. El umbral pide la suite completa, y cada medición debe empezar con `clean`.

### Paso 4: los datos del reporte

Vuelve a medir todo:

```bash
mvn clean verify
```

1. **Cobertura:** el `awk` del Paso 1. Pega su salida en el reporte tal cual: ya es una tabla de Markdown.

2. **Tests más lentos:** Surefire y Failsafe escriben en cada `.txt` de `target/*-reports/` el tiempo de la clase. Ordénalos por ese tiempo:

   ```bash
   grep -h "Time elapsed" target/*-reports/*.txt | sort -t: -k6 -rn | head -3
   # Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 7.507 s -- in com.bootcamp.BookRepositoryIT
   # Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.199 s -- in com.bootcamp.LibraryControllerIT
   # Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.835 s -- in com.bootcamp.LoanServiceTest
   ```

   `sort -t: -k6 -rn` parte cada línea por los dos puntos y ordena por el sexto campo, el tiempo, de mayor a menor. El tiempo de una clase incluye su preparación: en `BookRepositoryIT`, arrancar el contenedor.

3. **Suite por capas:** los `Tests run:` de cada plugin en `mvn clean verify`, y el tiempo de la fase rápida en la línea `Total time` de `mvn clean verify -DskipITs`. Maven la imprime aunque el umbral haga fallar el build.

### Paso 5: escribir el reporte

Rellena `testing-report-java.md` sustituyendo cada `TODO`:

1. **Suite por capas:** una fila por fase, con sus clases, sus tests y su tiempo.
2. **Cobertura:** la tabla del Paso 4 y una frase sobre qué fase cubre qué clase.
3. **Tests más lentos:** las tres clases y por qué tardan.
4. **Riesgos y huecos:** lo que la suite no prueba. Por ejemplo: ¿qué responde `GET /fees?daysLate=-1`? ¿Qué pasa si dos personas se llevan a la vez el último ejemplar?
5. **Próximos pasos:** los tests que añadirías primero, en orden.

## Experimentos

### Experimento 1: el umbral por clase

Cambia `<element>BUNDLE</element>` por `<element>CLASS</element>`. Con `CLASS`, la regla se aplica a cada clase por separado.

```bash
mvn clean verify
# [WARNING] Rule violated for class com.bootcamp.LabApplication: lines covered ratio is 0.33, but expected minimum is 0.85
# [INFO] BUILD FAILURE
```

El total sigue en el 95 %, pero `LabApplication` no llega. ¿Tiene sentido exigir el 85 % a una clase que solo tiene `main`? JaCoCo puede excluir clases del informe con `<excludes>`, pero cada exclusión es código que la cifra deja de mirar: con `BUNDLE` no hace falta ninguna. Deshaz el cambio.

## Comprobación final

```bash
mvn clean verify
# [INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
grep -c TODO testing-report-java.md
# 0
```

## Preguntas de cierre

1. ¿Qué cifra darías en el reporte si solo midieras `mvn test`? ¿Qué riesgo tiene esa cifra?
2. `LoanServiceTest` pasaba en el Paso 1 y su clase salía al 0 %. ¿Cómo lo detectarías en un proyecto que ya tiene JaCoCo configurado?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Test e IT con Surefire y Failsafe](../lab-01-test-e-it-con-surefire-y-failsafe/README.md) | [Proyecto — Proyecto Integrador - Suite Java completa del dominio](../../3-proyecto/README.md) → |

[↑ Semana 31](../../README.md)
