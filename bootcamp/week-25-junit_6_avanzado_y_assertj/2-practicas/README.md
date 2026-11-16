# Prácticas - Semana 25

Cuatro labs guiados (4 h en total). Cada uno trae un `starter/` con el código comentado por PASOS y una `solution/` con el resultado. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Tests parametrizados](lab-01-tests-parametrizados/README.md) | `@CsvSource`, `@ValueSource`, `@EnumSource`, `@MethodSource` con `argumentSet` y el nombre de cada ejecución | 70 min |
| [Lab 02 - @Nested y @Tag](lab-02-nested-y-tags/README.md) | Contextos anidados con su `@BeforeEach`, el aviso al olvidar `@Nested` y `-Dgroups` | 60 min |
| [Lab 03 - @TempDir y extensiones](lab-03-extensiones-y-tempdir/README.md) | Directorio temporal, una extensión `ParameterResolver`, `@ExtendWith` y `@RegisterExtension` | 55 min |
| [Lab 04 - AssertJ y soft assertions](lab-04-assertj-y-soft-assertions/README.md) | `extracting`, `containsExactly`, `Optional`, `assertSoftly` y `SoftAssertionsExtension` | 55 min |

## Antes de empezar

```bash
java -version    # openjdk version "21..." o superior
mvn -v           # Apache Maven 3.9 o superior
```

Cada lab es un proyecto Maven independiente:

```bash
cd lab-NN-.../starter
mvn test
```

Las versiones (JUnit 6.0.3, AssertJ 3.27.7 y Surefire 3.5.6) están fijadas en el `pom.xml` de cada carpeta.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 04 - AssertJ a fondo y soft assertions](../1-teoria/04-assertj-y-soft-assertions.md) | [Lab 01 - Tests parametrizados](lab-01-tests-parametrizados/README.md) → |

[↑ Semana 25](../README.md)
