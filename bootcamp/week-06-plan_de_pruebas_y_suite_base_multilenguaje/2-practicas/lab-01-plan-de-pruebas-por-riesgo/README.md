# Lab 01 - Plan de pruebas por riesgo

## Objetivo

Escribir el plan de pruebas del registro de visitantes del planetario, que ya tiene tres suites en verde, y descubrir leyéndolas que no prueban lo mismo.

## Tiempo estimado

75 minutos.

## Requisitos

- La [teoría](../../1-teoria/01-plan-de-pruebas-y-trazabilidad.md) de esta semana.
- Node 22 con pnpm, uv y Java 21 con Maven, como en las semanas [3](../../../week-03-primeros_tests_automatizados_con_javascript/1-teoria/01-setup-jest.md), [4](../../../week-04-primeros_tests_automatizados_con_python/1-teoria/01-setup-pytest.md) y [5](../../../week-05-primeros_tests_automatizados_con_java/1-teoria/01-setup-junit.md).

## Punto de partida

```bash
cp -r <ruta-del-bootcamp>/bootcamp/week-06-plan_de_pruebas_y_suite_base_multilenguaje/2-practicas/lab-01-plan-de-pruebas-por-riesgo/starter ~/labs-semana-06
cd ~/labs-semana-06
ls
# java  js  plan-de-pruebas.md  python
```

Ejecuta cada suite en su carpeta. Los paréntesis abren una subshell: el `cd` solo vale dentro de ellos, y al terminar sigues en `~/labs-semana-06`.

```bash
(cd js && pnpm install && pnpm test)
# Tests:       8 passed, 8 total

(cd python && uv sync && uv run pytest -q)
# 7 passed in 0.02s

(cd java && mvn test)
# [INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

Guarda este punto de partida en git, como en las semanas anteriores:

```bash
cat > .gitignore <<'EOF'
node_modules/
.venv/
__pycache__/
.pytest_cache/
target/
EOF
git init
git add .
git commit -m "labs semana 06: punto de partida"
```

`cat > archivo <<'EOF' ... EOF` escribe en el archivo las líneas que hay entre las dos marcas `EOF`.

## Contexto

Es la validación del formulario del planetario de la [semana 2](../../../week-02-casos_de_prueba_y_tecnicas_de_diseno/2-practicas/lab-02-particion-y-valores-limite/README.md), escrita en los tres lenguajes y **sin bugs**. Las reglas están en la sección 2 de `plan-de-pruebas.md`. Tres suites, todas en verde: 8, 7 y 8 tests. Que el número no coincida ya es una pista.

---

## Paso 1: alcance (10 min)

Abre `plan-de-pruebas.md` y rellena la sección 1. Dentro va lo que prueban estas suites: las funciones de validación y sus cuatro reglas. Fuera va todo lo que no, con el motivo. Piensa en el formulario web, el pago y el correo de confirmación.

Una respuesta posible:

```markdown
**Dentro**:

- La validación de los cuatro campos del registro (`validateVisitor` en JS, `validate_visitor` en Python, `VisitorValidator.validate` en Java), con las reglas R1 a R4.

**Fuera**:

- El formulario web y sus mensajes en pantalla: se prueban en la etapa de E2E.
- El pago de la entrada y el correo de confirmación: son otros módulos.
```

## Paso 2: riesgos (15 min)

Rellena la sección 3 con un riesgo por regla: qué pasa si la regla falla, cuánto daño hace (impacto) y cuánto se espera que falle (probabilidad). La prioridad sale de la [tabla de la teoría](../../1-teoria/01-plan-de-pruebas-y-trazabilidad.md#prioridad-por-riesgo), no de la intuición.

Hazlo antes de mirar esta respuesta:

| ID | Riesgo | Regla | Impacto | Probabilidad | Prioridad |
|---|---|---|---|---|---|
| K1 | Un adulto entra con un grupo escolar | R4 | Alto | Media | Alta |
| K2 | Se rechaza una edad válida o se acepta una inválida en los límites 5 y 99 | R3 | Medio | Alta | Alta |
| K3 | Se rechaza un nombre válido de 2 o de 50 caracteres | R1 | Bajo | Alta | Media |
| K4 | Se acepta un nombre con números o símbolos | R2 | Bajo | Media | Baja |

- K1 es Alto porque un adulto en un grupo de menores es un problema de seguridad y legal; Media porque la regla combina dos campos.
- K2 y K3 tienen probabilidad Alta porque son límites: un `<` en lugar de un `<=` basta.

Si tus valores difieren, vale, siempre que cada uno tenga su porqué. Para que los pasos siguientes coincidan, copia esta tabla en tu plan.

## Paso 3: inventario (15 min)

Antes de escribir la matriz, mira lo que hay. Cuenta los tests de cada lenguaje con `grep -c`, que cuenta las líneas que casan con un patrón:

```bash
grep -c "^ *it(" js/tests/visitor.test.js
# 8
grep -c "^def test_" python/tests/test_visitor.py
# 7
grep -c "@Test" java/src/test/java/com/bootcamp/VisitorValidatorTest.java
# 8
```

`^ *` es «principio de línea y cero o más espacios». Ahora lista los ID con `grep -n`, que muestra el número de línea:

```bash
grep -n "TC-PLAN" python/tests/test_visitor.py
# 4:# TC-PLAN-001
# 9:# TC-PLAN-002
# 14:# TC-PLAN-003
# 19:# TC-PLAN-004
# 24:# TC-PLAN-005
# 29:# TC-PLAN-07
# 34:# TC-PLAN-008
```

Repite con `js/tests/visitor.test.js` y con el test de Java, y abre los tres archivos. Para cada test anota su ID y qué comprueba en una tabla de borrador (no va en el plan):

| ID | Qué comprueba | JS | Python | Java |
|---|---|---|---|---|
| TC-PLAN-001 | Visitante válido | ✅ | ✅ | ✅ |
| ... | | | | |

Busca tres cosas: un ID que falta en un lenguaje, un ID mal escrito y un test sin ID.

## Paso 4: matriz (15 min)

Rellena la sección 6. La matriz dice **lo que el plan exige**, no lo que hay: un ✅ significa «este caso tiene que estar automatizado en este lenguaje», aunque hoy falte. Usa la prioridad del riesgo que cubre cada caso.

Añade dos casos nuevos:

- **TC-PLAN-009**, edad 100: es el vecino de fuera del máximo de R3. Java ya lo prueba, en el test sin ID.
- **TC-PLAN-010**, edad 12.5: la clase «no es un entero» de R3. En Java no se puede escribir: `age` es un `int` y `12.5` no compila. Lleva — en Java.

```markdown
| Caso | Regla | Descripción | Prioridad | JS | Python | Java |
|---|---|---|---|---|---|---|
| TC-PLAN-001 | R1-R4 | Visitante válido | Media | ✅ | ✅ | ✅ |
| TC-PLAN-002 | R1 | Nombre de 1 carácter | Media | ✅ | ✅ | ✅ |
| TC-PLAN-003 | R1 | Nombre de 50 caracteres | Media | ✅ | ✅ | ✅ |
| TC-PLAN-004 | R2 | Nombre con un dígito | Baja | ✅ | ✅ | ✅ |
| TC-PLAN-005 | R3 | Edad 4 | Alta | ✅ | ✅ | ✅ |
| TC-PLAN-006 | R3 | Edad 99 | Alta | ✅ | ✅ | ✅ |
| TC-PLAN-007 | R4 | Grupo escolar con 17 años | Alta | ✅ | ✅ | ✅ |
| TC-PLAN-008 | R4 | Grupo escolar con 18 años | Alta | ✅ | ✅ | ✅ |
| TC-PLAN-009 | R3 | Edad 100 | Alta | ✅ | ✅ | ✅ |
| TC-PLAN-010 | R3 | Edad 12.5 | Media | ✅ | ✅ | — |
```

Escribe el motivo del — en la sección 7:

```markdown
- TC-PLAN-010 no se automatiza en Java: `age` es un `int` y `new Visitor("Ana Ruiz", 12.5, false)` no compila.
```

Escribe ✅ y — tal cual: el script del lab 02 los busca en la matriz.

## Paso 5: estrategia y criterios (10 min)

Rellena la sección 4 en dos o tres frases: qué nivel de test, con qué herramientas, y por qué no hace falta base de datos.

Para la sección 5, estos son los criterios de salida que escribió otro equipo. Ninguno se puede comprobar:

- «Los tests principales pasan.»
- «Cuando esté listo.»
- «La cobertura es buena.»

Escribe los tuyos de forma que se comprueben con un comando o un número. Una respuesta posible:

```markdown
**Entrada**:

- Las reglas R1 a R4 están aprobadas por el responsable del planetario.
- `pnpm test`, `uv run pytest` y `mvn test` funcionan en el equipo de quien prueba.

**Salida**:

- Las tres suites terminan con código de salida 0, sin tests saltados.
- Todos los casos de prioridad Alta están automatizados en los tres lenguajes.
- Cada caso marcado con ✅ en la matriz aparece en los tests de ese lenguaje, y cada test lleva el ID de un caso de la matriz.
```

## Paso 6: huecos (5 min)

Compara la matriz con tu inventario del paso 3 y apunta en la sección 8 cada diferencia:

```markdown
| Hueco | Lenguaje | Caso | Prioridad |
|---|---|---|---|
| Falta el test | Python | TC-PLAN-006 | Alta |
| El ID está mal escrito (`TC-PLAN-07`) | Python | TC-PLAN-007 | Alta |
| Falta el test | Java | TC-PLAN-008 | Alta |
| Test sin ID (edad 100) | Java | TC-PLAN-009 | Alta |
| Falta el test | JS y Python | TC-PLAN-009 | Alta |
| Falta el test | JS y Python | TC-PLAN-010 | Media |
```

El caso de prioridad más alta de la regla de más riesgo, TC-PLAN-008, no está en Java, y su suite está en verde. Si una mutación cambiara `age >= 18` por `age >= 19` en `VisitorValidator.java`, ningún test de Java lo notaría.

No arregles los tests todavía: el lab 02 los encuentra con un script y los arregla.

---

## Comprobación

```bash
grep -c TODO plan-de-pruebas.md
# 1
```

El único `TODO` que queda es el de la sección 9, la evidencia, que rellena el lab 02. Guarda el plan:

```bash
git add plan-de-pruebas.md
git commit -m "lab 01: plan de pruebas"
```

## Repaso

- [ ] La sección 1 dice qué queda fuera y por qué.
- [ ] Cada riesgo tiene impacto, probabilidad y la prioridad que sale de la tabla.
- [ ] La matriz tiene 10 casos, con ✅ o — en cada lenguaje.
- [ ] El — de TC-PLAN-010 en Java tiene su motivo en la sección 7.
- [ ] Los criterios de salida se comprueban con un comando o un número.
- [ ] La sección 8 tiene los seis huecos, aunque las tres suites estén en verde.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Un comando para tres suites](../lab-02-un-comando-para-tres-suites/README.md) → |

[↑ Semana 06](../../README.md)
