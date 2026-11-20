# Lab 02 - Un comando para tres suites

## Objetivo

Construir `verify.sh`, un script que ejecuta las tres suites del planetario y compara la matriz del plan con los ID de los tests; usarlo para encontrar y cerrar los huecos del lab 01, y registrar un caso que da veredictos distintos según el lenguaje.

## Tiempo estimado

75 minutos.

## Requisitos

- El [lab 01](../lab-01-plan-de-pruebas-por-riesgo/README.md) terminado: `~/labs-semana-06` con el plan y su commit.
- La sección [Un comando para las tres suites](../../1-teoria/01-plan-de-pruebas-y-trazabilidad.md#un-comando-para-las-tres-suites) de la teoría.

## Punto de partida

```bash
cd ~/labs-semana-06
cp <ruta-del-bootcamp>/bootcamp/week-06-plan_de_pruebas_y_suite_base_multilenguaje/2-practicas/lab-02-un-comando-para-tres-suites/starter/verify.sh .
./verify.sh
echo $?
# 0
```

`./verify.sh` ejecuta el script de la carpeta actual; el `./` hace falta porque la carpeta actual no está entre las que la terminal busca. El script ya es ejecutable (lo marca `chmod +x`, que conserva la copia). Todavía no hace nada: cada bloque está comentado y lleva una marca `# PASO N`. En cada paso borras el `# ` del principio de las líneas de ese bloque. En VS Code, selecciona el bloque y pulsa `Ctrl+/`.

Las dos primeras líneas no se tocan:

```bash
#!/usr/bin/env bash
cd "$(dirname "$0")"
```

- `#!/usr/bin/env bash` (el *shebang*) dice qué programa ejecuta el archivo.
- `cd "$(dirname "$0")"` entra en la carpeta del script. `$0` es la ruta del script y `dirname` le quita el nombre del archivo. Así funciona igual si lo lanzas desde otra carpeta.

---

## Paso 1: las tres suites (10 min)

Descomenta el bloque `PASO 1` y ejecuta:

```bash
./verify.sh
```

```text
== JavaScript
$ jest
PASS tests/visitor.test.js
...
Tests:       8 passed, 8 total
...
== Python
.......                                                                  [100%]
7 passed in 0.01s
== Java
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.081 s -- in com.bootcamp.VisitorValidatorTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
```

`mvn -B test | grep -E "Tests run:|FAIL"` deja pasar solo las líneas del resultado. `-B` (*batch*) quita los colores de Maven, para que `grep` lea texto limpio.

Ahora rompe el código de JS a propósito. En `js/src/visitor.js`, cambia `age > 99` por `age > 98` y ejecuta:

```bash
./verify.sh
echo $?
```

```text
== JavaScript
$ jest
FAIL tests/visitor.test.js
  validateVisitor
...
    ✕ should register the visitor when the age is 99 (3 ms)
...
Tests:       1 failed, 7 passed, 8 total
...
[ELIFECYCLE] Test failed. See above for more details.
== Python
.......                                                                  [100%]
7 passed in 0.01s
== Java
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.081 s -- in com.bootcamp.VisitorValidatorTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
0
```

Jest falla y el script termina con **0**: el código de salida de un script es el de su último comando, y el último es el de Java. Si este script decidiera una entrega o un despliegue, el bug pasaría.

Fíjate también en que pytest y JUnit siguen en verde: cada lenguaje prueba **su** implementación. Una regla en tres lenguajes son tres sitios donde puede romperse.

## Paso 2: parar en el primer fallo (10 min)

Descomenta la línea `set -euo pipefail` del bloque `PASO 2` y ejecuta otra vez, con la mutación todavía puesta:

```bash
./verify.sh
echo $?
```

```text
== JavaScript
...
Tests:       1 failed, 7 passed, 8 total
...
[ELIFECYCLE] Test failed. See above for more details.
1
```

Con `-e`, el script se detiene en el primer comando que falla y devuelve su código. Python y Java ya no se ejecutan: no hace falta, la entrega no vale. Qué hace `-u` y por qué `-o pipefail` importa en la línea de Maven está en la [tabla de la teoría](../../1-teoria/01-plan-de-pruebas-y-trazabilidad.md#un-comando-para-las-tres-suites).

Deshaz la mutación:

```bash
git restore js/src/visitor.js
./verify.sh > /dev/null 2>&1
echo $?
# 0
```

`> /dev/null 2>&1` tira la salida normal y la de errores: solo te interesa el código.

## Paso 3: comparar la matriz con los tests (20 min)

Descomenta el bloque `PASO 3`. Define una función, `trazar`, que todavía nadie llama. Recibe cinco argumentos, `$1` a `$5`:

| Argumento | Qué es | Para JavaScript |
|---|---|---|
| `$1` | Nombre del lenguaje, para los mensajes | `JavaScript` |
| `$2` | Columna de la matriz | `6` |
| `$3` | Carpeta de los tests | `js/tests` |
| `$4` | Archivos que se miran | `'*.js'` |
| `$5` | Patrón de una línea que empieza un test | `'^ *(it\|test)[(]'` |

Línea a línea:

1. **`esperados`**: los casos que la matriz marca con ✅ en esa columna.

   ```bash
   awk -F'|' -v c="$2" '/^[|] TC-/ && $c ~ /✅/ { gsub(/ /, "", $2); print $2 }' plan-de-pruebas.md | sort -u
   ```

   `awk` lee el plan línea a línea y la corta por `|` (`-F'|'`). Como cada fila empieza por `|`, el campo `$1` está vacío, `$2` es el caso, `$5` la prioridad y `$6`, `$7` y `$8` las columnas JS, Python y Java. `-v c="$2"` pasa la columna; `/^[|] TC-/` se queda con las filas de la matriz; `$c ~ /✅/` con las que tienen ✅ en esa columna; `gsub` quita los espacios del ID antes de imprimirlo. `sort -u` ordena y quita repetidos.

2. **`encontrados`**: los ID que hay en los comentarios de los tests.

   ```bash
   grep -rhIE --include="$4" "^ *(//|#) *$ID *$" "$3" | grep -oE "$ID" | sort -u || true
   ```

   `-r` busca en toda la carpeta, `-h` no escribe el nombre del archivo, `-I` salta los binarios (los `.pyc` de Python) y `-E` activa las expresiones extendidas. Solo cuentan las líneas que son únicamente un comentario con el ID, como `// TC-PLAN-008`. El segundo `grep -o` imprime solo el ID. `|| true` evita que `set -e` pare el script cuando un lenguaje no tiene ningún ID: `grep` termina con 1 si no encuentra nada.

3. **`faltan`** y **`sobran`**: `comm` compara dos listas ordenadas. `comm -23 A B` imprime lo que solo está en A (casos sin test); `comm -13 A B`, lo que solo está en B (ID de los tests que la matriz no tiene). `<(...)` pasa la salida de un comando como si fuera un archivo.

4. **`tests`** y **`con_id`**: cuántas líneas empiezan un test (`$5`) y cuántas son un comentario con ID. Si hay menos ID que tests, algún test no tiene caso.

Al final, la función imprime lo que encuentra y pone `huecos=1` si algo no cuadra. Las variables `local` solo existen dentro de la función.

## Paso 4: los huecos, encontrados por el script (20 min)

Descomenta el bloque `PASO 4`: una llamada a `trazar` por lenguaje y el veredicto. Ejecuta:

```bash
./verify.sh
echo $?
```

```text
...
== Trazabilidad
JavaScript: 8 tests, 8 con ID
  faltan en los tests: TC-PLAN-009 TC-PLAN-010
Python: 7 tests, 7 con ID
  faltan en los tests: TC-PLAN-006 TC-PLAN-007 TC-PLAN-009 TC-PLAN-010
  no están en la matriz: TC-PLAN-07
Java: 8 tests, 7 con ID
  faltan en los tests: TC-PLAN-008 TC-PLAN-009
  hay tests sin ID
FALLO: la matriz y los tests no coinciden
1
```

Compáralo con la sección 8 de tu plan: son los seis huecos del lab 01. TC-PLAN-007 aparece como falta en Python porque el test existe, pero su ID dice `TC-PLAN-07`.

Ciérralos. En `js/tests/visitor.test.js`, antes del test de TC-PLAN-007:

```javascript
  // TC-PLAN-009
  it("should reject the age when it is 100", () => {
    expect(validateVisitor({ name: "Ana Ruiz", age: 100, schoolGroup: false })).toBe(
      "La edad debe ser un número entero de 5 a 99",
    );
  });

  // TC-PLAN-010
  it("should reject the age when it is 12.5", () => {
    expect(validateVisitor({ name: "Ana Ruiz", age: 12.5, schoolGroup: false })).toBe(
      "La edad debe ser un número entero de 5 a 99",
    );
  });
```

En `python/tests/test_visitor.py`, cambia `# TC-PLAN-07` por `# TC-PLAN-007` y añade antes de ese test:

```python
# TC-PLAN-006
def test_registers_the_visitor_when_the_age_is_99() -> None:
    assert validate_visitor("Ana Ruiz", 99, False) == "Visitante registrado"


# TC-PLAN-009
def test_rejects_the_age_when_it_is_100() -> None:
    assert validate_visitor("Ana Ruiz", 100, False) == "La edad debe ser un número entero de 5 a 99"


# TC-PLAN-010
def test_rejects_the_age_when_it_is_12_5() -> None:
    assert validate_visitor("Ana Ruiz", 12.5, False) == "La edad debe ser un número entero de 5 a 99"
```

En `VisitorValidatorTest.java`, pon `// TC-PLAN-009` encima del `@Test` del test de la edad 100 y añade al final de la clase, antes de la última `}`:

```java
    // TC-PLAN-008
    @Test
    @DisplayName("should reject the school group visitor when the age is 18")
    void shouldRejectTheSchoolGroupVisitorWhenTheAgeIs18() {
        assertEquals("Los grupos escolares son para menores de 18 años",
                VisitorValidator.validate(new Visitor("Ana Ruiz", 18, true)));
    }
```

Ejecuta otra vez:

```bash
./verify.sh
echo $?
```

```text
...
Tests:       10 passed, 10 total
...
10 passed in 0.01s
== Java
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.075 s -- in com.bootcamp.VisitorValidatorTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
== Trazabilidad
JavaScript: 10 tests, 10 con ID
Python: 10 tests, 10 con ID
Java: 9 tests, 9 con ID
OK: tres suites en verde y trazabilidad completa
0
```

Java tiene 9 tests y no 10: TC-PLAN-010 lleva — en su columna.

## Paso 5: mismo caso, distinto veredicto (15 min)

Los tests de TC-PLAN-010 usan `12.5`. ¿Y si la edad llega como `5.0`, como hacen muchos formularios con los números? Pregúntaselo a cada implementación con un comando de una línea. `node -e` ejecuta el código que le pasas; `uv run python -c`, lo mismo con Python:

```bash
cd js
node -e 'const { validateVisitor } = require("./src/visitor"); console.log(validateVisitor({ name: "Ana Ruiz", age: 5.0, schoolGroup: false }))'
# Visitante registrado

cd ../python
uv run python -c 'from src.visitor import validate_visitor; print(validate_visitor("Ana Ruiz", 5.0, False))'
# La edad debe ser un número entero de 5 a 99
cd ..
```

La misma regla, dos veredictos. En JavaScript `5.0` y `5` son el mismo número; en Python `5.0` es un `float` y `isinstance(5.0, int)` es `False`. En Java la pregunta no existe: `age` es un `int`.

Ninguna de las dos es un bug del código: la regla R3 no dice qué hacer con `5.0`. Es una pregunta para quien escribió la regla, y hasta que responda va al plan. Añade en la sección 7:

```markdown
- Una edad de `5.0` se acepta en JavaScript (`5.0` es el número `5`) y se rechaza en Python (`isinstance(5.0, int)` es `False`). R3 no dice qué hacer: pregunta abierta al responsable del planetario.
```

Por último, la evidencia. Copia en la sección 9 el final de `./verify.sh`, desde `== Trazabilidad`, dentro de un bloque `text`, y guarda:

```bash
git add .
git commit -m "lab 02: verify.sh y huecos cerrados"
```

---

## Comprobación

```bash
./verify.sh > /dev/null 2>&1; echo $?
# 0
grep -c TODO plan-de-pruebas.md
# 0
```

## Repaso

- [ ] Sin `set -e`, un script con un test en rojo puede terminar con 0.
- [ ] `-o pipefail` hace que `mvn test | grep` falle cuando falla Maven.
- [ ] `verify.sh` encuentra los seis huecos del lab 01 sin leer los tests a mano, incluido el ID mal escrito.
- [ ] Cada test lleva el ID de un caso de la matriz, y cada ✅ tiene su test.
- [ ] La diferencia de `5.0` está en la sección 7 como pregunta abierta, no como bug.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Plan de pruebas por riesgo](../lab-01-plan-de-pruebas-por-riesgo/README.md) | [Proyecto — Proyecto - Plan de pruebas y suite base en tres lenguajes](../../3-proyecto/README.md) → |

[↑ Semana 06](../../README.md)
