# Proyecto Semanal - Aislar las dependencias del dominio con Mockito

> **🎯 ÚNICO ENTREGABLE**: este proyecto es el único entregable obligatorio de la semana.

## Objetivo

Hasta la semana 25, el servicio Java de tu dominio guardaba los datos en un `Map` interno y no hablaba con nadie. Esta semana le das tres dependencias, como tendría en producción, y pruebas su operación principal sin ninguna de ellas:

- un **repositorio** donde se guardan los datos (una base de datos en producción);
- un **servicio externo** que autoriza la operación (una API de otra organización);
- un **notificador** que avisa del resultado (correo, SMS).

## Dónde va

En la carpeta `java/` de tu repositorio `qa-<nombre>-<apellido>` ([proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)), sobre la entrega de la [semana 25](../../week-25-junit_6_avanzado_y_assertj/3-proyecto/README.md).

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

El ejemplo de este README es un museo que presta piezas a otras instituciones: `PieceService.lend(code, institutionId, days)` comprueba en un registro externo que la institución está acreditada, guarda la pieza como prestada y envía a la institución el seguro que debe pagar.

## Paso a paso

### Paso 1: rama y pom.xml

1. Crea la rama de la semana:

   ```bash
   cd qa-<nombre>-<apellido>
   git switch main && git pull
   git switch -c week-26
   ```

2. Copia el starter:

   ```bash
   cp -r <ruta-al-bootcamp>/bootcamp/week-26-mockito_mocking_en_java/3-proyecto/starter/. .
   ```

   - `java/pom.xml` **sustituye** al tuyo: añade `mockito-core` y `mockito-junit-jupiter` 5.23.0, `maven-dependency-plugin` y el agente de Mockito en Surefire ([teoría 01](../1-teoria/01-mocks-y-stubbing.md)). Si tenías otras dependencias, vuelve a añadirlas.
   - `java/src/test/java/com/bootcamp/ItemOperationTest.java` es nuevo: la plantilla de la suite con seis tests `@Disabled("TODO: ...")`.

3. Ejecuta:

   ```bash
   cd java
   mvn test
   # [INFO] Tests run: 33, Failures: 0, Errors: 0, Skipped: 6
   ```

   Tus tests de la semana 25 siguen pasando (27 en el ejemplo); los seis de la plantilla aparecen como saltados. El número total depende de tu suite.

### Paso 2: las tres dependencias

1. Crea una interfaz por dependencia en `src/main/java/com/bootcamp/`. Solo con los métodos que el servicio necesita:

   ```java
   public interface PieceRepository {
       Optional<Piece> findByCode(String code);
       List<Piece> findAll();
       void save(Piece piece);
   }

   public interface InstitutionRegistry {
       boolean isAccredited(String institutionId);
   }

   public interface Notifier {
       void send(LoanNotice notice);
   }
   ```

   `LoanNotice` es un `record` con lo que se envía: `record LoanNotice(String institutionId, String pieceCode, int days, int premium)`. Crea también la excepción que lanza el servicio externo cuando no responde (`RegistryUnavailableException extends RuntimeException`).

2. Cambia el servicio para recibirlas por el constructor y usa el repositorio en lugar del `Map`:

   ```java
   public PieceService(PieceRepository pieces, InstitutionRegistry registry, Notifier notifier) {
       this.pieces = pieces;
       this.registry = registry;
       this.notifier = notifier;
   }
   ```

3. Reescribe la operación principal para que use las tres. En el ejemplo, en este orden: buscar la pieza, rechazarla si ya está prestada, preguntar al registro, calcular el seguro, guardar la pieza prestada y enviar el aviso.

Los tests de la semana 25 ya no compilan: crean el servicio con `new PieceService()`.

### Paso 3: los tests de la semana 25 con un fake

Los tests de registro y de cálculo no hablan con el registro ni con el notificador, y comprueban el resultado de varias operaciones seguidas (registrar dos piezas, listar las disponibles). Para ellos, un repositorio en memoria que funciona de verdad (un **fake**) es más simple que programar un mock en cada test.

1. Crea `src/test/java/com/bootcamp/InMemoryPieceRepository.java`: implementa la interfaz con un `LinkedHashMap`, como hacía antes el servicio.
2. En el `@BeforeEach` de tu suite de la semana 25:

   ```java
   service = new PieceService(new InMemoryPieceRepository(), mock(InstitutionRegistry.class), mock(Notifier.class));
   ```

   Los dos `mock(...)` solo rellenan el constructor: esos tests no los usan.

3. Saca de esa suite los tests de la operación principal: los vas a reescribir en el paso 4.

```bash
mvn test
# [INFO] Tests run: 30, Failures: 0, Errors: 0, Skipped: 6
```

### Paso 4: la suite con Mockito

1. Renombra `ItemOperationTest` a tu operación (`PieceLendingTest`).
2. Añade los campos que lista el comentario de la plantilla:

   ```java
   @Mock
   private PieceRepository pieces;

   @Mock
   private InstitutionRegistry registry;

   @Mock
   private Notifier notifier;

   @InjectMocks
   private PieceService service;

   @Captor
   private ArgumentCaptor<LoanNotice> notice;
   ```

3. Escribe cada test y quita su `@Disabled`. Elige una forma, `when`/`verify` ([labs 01 a 03](../2-practicas/README.md)) o BDDMockito ([lab 04](../2-practicas/lab-04-bddmockito/README.md)), y úsala en toda la clase.

| Test de la plantilla | Qué preparas | Qué compruebas |
|---|---|---|
| `shouldSaveTheItemAfterTheOperation` | El repositorio devuelve el item; el servicio externo dice que sí | Con un captor, el estado del item guardado |
| `shouldNotifyWithTheOperationDetails` | Igual | Con un captor, **todos** los campos del aviso |
| `shouldSaveBeforeNotifying` | Igual | `InOrder`: `save` antes que `send` |
| `shouldRejectAnUnknownItem` | El repositorio devuelve `Optional.empty()` | La excepción, y que el servicio externo no recibe ninguna llamada |
| `shouldRejectTheOperationWhenTheExternalServiceSaysNo` | El servicio externo devuelve `false` | La excepción; no se guarda ni se notifica nada |
| `shouldChangeNothingWhenTheExternalServiceIsDown` | El servicio externo lanza su excepción (`thenThrow` / `willThrow`) | La excepción; no se guarda ni se notifica nada |

Añade al menos un test más con una regla propia de tu dominio (en el ejemplo, una pieza que ya está prestada).

4. Ejecuta:

   ```bash
   mvn test
   # [INFO] Tests run: 31, Failures: 0, Errors: 0, Skipped: 0
   ```

   El total depende de tu dominio; debe ser 20 o más, sin `Skipped`.

### Paso 5: comprobar que la suite detecta errores

Haz estos cambios en el servicio, uno cada vez. Con cada uno, `mvn test` debe fallar; si alguno pasa en verde, falta un test. Deshaz cada cambio antes del siguiente.

1. Comenta la línea que envía el aviso.
2. Comenta la línea que guarda el item.
3. Mueve el envío del aviso encima del guardado.
4. Cambia un campo del aviso (por ejemplo, suma 1 al importe).

### Paso 6: verificar y entregar

Desde la raíz del bootcamp:

```bash
PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 26
```

Añade al `README.md` de tu repositorio un párrafo: qué dependencias sustituiste con un mock, cuál con un fake y por qué.

Haz commit, sube la rama y abre un pull request a `main`.

## ✅ Requisitos verificables

Copia de [checks.json](../checks.json):

| Comprobación | Pista |
|---|---|
| La suite Java pasa con `mvn test` | Pasos 2 y 3 |
| El `pom.xml` carga Mockito como agente de Java | Paso 1 |
| No queda ningún `@Disabled` en `src/test` | Paso 4 |
| Al menos 20 ejecuciones de test, sin fallos ni saltados | Paso 4 |
| Al menos 3 dependencias sustituidas con `@Mock` | Paso 4 |
| Los mocks los crea `MockitoExtension` | Paso 4 |
| La suite verifica interacciones con `verify` o `then().should()` | Paso 4 |
| La suite captura argumentos con `ArgumentCaptor` | Paso 4 |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - BDDMockito](../2-practicas/lab-04-bddmockito/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 26](../README.md)
