# Prácticas - Semana 26

Cuatro labs guiados (4 h en total). Cada uno trae un `starter/` con el código comentado por PASOS y una `solution/` con el resultado. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

Todos usan el mismo dominio, una biblioteca: reservas, avisos de préstamos vencidos, renovaciones y préstamos.

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Del stub a mano al primer mock](lab-01-primer-mock/README.md) | `mock()`, `when`/`thenReturn`, `thenThrow`, respuestas por defecto, `@Mock` e `@InjectMocks`, el agente de Mockito | 70 min |
| [Lab 02 - verify y ArgumentCaptor](lab-02-verify-y-captores/README.md) | `verify` con `times` y `never`, matchers, `@Captor`, `InOrder` | 60 min |
| [Lab 03 - Strict stubs, spies y mockStatic](lab-03-strict-stubs-spy-y-static/README.md) | `UnnecessaryStubbingException`, `PotentialStubbingProblem`, `mockStatic`, `@Spy` y `doReturn` | 55 min |
| [Lab 04 - BDDMockito](lab-04-bddmockito/README.md) | `given`/`willReturn`, `willThrow`, `then().should()`, `shouldHaveNoInteractions` | 55 min |

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

Los bloques comentados dentro de otro bloque llevan doble comentario (`// //`). Usa el atajo de comentar de tu editor (`Ctrl+/` en VS Code e IntelliJ) sobre el bloque entero: quita un solo nivel.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 04 - BDDMockito](../1-teoria/04-bddmockito.md) | [Lab 01 - Del stub a mano al primer mock](lab-01-primer-mock/README.md) → |

[↑ Semana 26](../README.md)
