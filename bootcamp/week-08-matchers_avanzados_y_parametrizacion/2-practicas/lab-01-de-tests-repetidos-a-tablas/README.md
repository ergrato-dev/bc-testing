# Lab 01 - De tests repetidos a tablas

## Objetivo

Convertir los 19 tests del formulario del planetario en tablas con `it.each`, con el ID de cada caso encima de su fila, y comprobar que el nombre de cada test dice qué fila falla.

## Tiempo estimado

80 minutos.

## Requisitos

- La [teoría 01](../../1-teoria/01-tablas-de-casos-con-each.md).
- El [lab 03 de la semana 3](../../../week-03-primeros_tests_automatizados_con_javascript/2-practicas/lab-03-de-casos-a-tests/README.md): las reglas del formulario y sus 19 casos.

## Punto de partida

La carpeta `~/labs-semana-08` es el proyecto de los tres labs. Créala y copia el código de este lab:

```bash
mkdir ~/labs-semana-08
cd ~/labs-semana-08
cp -r <ruta-del-bootcamp>/bootcamp/week-08-matchers_avanzados_y_parametrizacion/2-practicas/lab-01-de-tests-repetidos-a-tablas/starter/. .
pnpm install
pnpm test
# Tests:       19 passed, 19 total
```

Guárdalo en git:

```bash
cat > .gitignore <<'EOF'
node_modules/
EOF
git init
git add .
git commit -m "labs semana 08: punto de partida"
```

## Contexto

`src/visitor.js` es el `validateVisitor` de la semana 3, con los cuatro bugs ya corregidos. `tests/visitor.test.js` tiene un test por caso, del TC-PLAN-REG-001 al 019, cada uno con su ID encima. Las reglas:

| Campo | Regla | Mensaje si no se cumple |
|---|---|---|
| `name` | De 2 a 50 caracteres; solo letras (con tildes y ñ) y espacios | `NAME_LENGTH` o `NAME_LETTERS` |
| `age` | Número entero de 5 a 99 | `AGE` |
| `schoolGroup` | Con `true`, la edad tiene que ser 17 o menos | `SCHOOL` |

Los mensajes están en constantes al principio del archivo de tests, y `REGISTERED` es `"Visitante registrado"`.

---

## Paso 1: lo que se repite (10 min)

```bash
wc -l tests/visitor.test.js
# 189 tests/visitor.test.js
```

`wc -l` cuenta las líneas de un archivo. Abre el archivo y compara los tests de `TC-PLAN-REG-010` y `TC-PLAN-REG-015`: solo cambian el ID, el nombre y la edad.

Haz un inventario en un borrador. Agrupa los 19 tests por campo y por resultado (se registra o se rechaza):

| Campo | Resultado | Casos | Qué cambia entre ellos |
|---|---|---|---|
| `name` | Se registra | 002, 003, 004, 005, 008 | El nombre |
| `name` | Se rechaza | 001, 006, 007 | El nombre y el mensaje |
| ... | | | |

Deberían salir seis grupos. Cada grupo será una tabla, salvo los que tengan un solo caso.

## Paso 2: la primera tabla, con arrays (15 min)

Sustituye los cinco tests de `name` que se registran por una tabla de arrays. Déjalos dentro de un contexto nuevo, `describe("when the name is valid")`, dentro de `describe("name")`:

```javascript
    describe("when the name is valid", () => {
      it.each([
        // TC-PLAN-REG-002
        ["Al"],
        // TC-PLAN-REG-003
        ["Ana"],
        // TC-PLAN-REG-004
        ["a".repeat(49)],
        // TC-PLAN-REG-005
        ["a".repeat(50)],
        // TC-PLAN-REG-008
        ["José Núñez"],
      ])("should register the visitor when the name is %s", (name) => {
        const visitor = { ...validVisitor, name };

        const result = validateVisitor(visitor);

        expect(result).toBe(REGISTERED);
      });
    });
```

`{ ...validVisitor, name }` es lo mismo que `{ ...validVisitor, name: name }`: si el campo y la variable se llaman igual, basta con escribirlo una vez.

```bash
pnpm test --verbose 2>&1 | grep "name is"
#       when the name is valid
#         ✓ should register the visitor when the name is Al
#         ✓ should register the visitor when the name is Ana (1 ms)
#         ✓ should register the visitor when the name is aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
#         ✓ should register the visitor when the name is aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa (1 ms)
#         ✓ should register the visitor when the name is José Núñez
```

Sigue habiendo 19 tests. Pero los dos nombres largos no se distinguen sin contar letras.

## Paso 3: filas de objetos (15 min)

Cambia cada fila por un objeto con una columna `what` que describe el nombre, y usa `$what` en el nombre del test:

```javascript
      it.each([
        // TC-PLAN-REG-002
        { what: "2 characters", name: "Al" },
        // TC-PLAN-REG-003
        { what: "3 characters", name: "Ana" },
        // TC-PLAN-REG-004
        { what: "49 characters", name: "a".repeat(49) },
        // TC-PLAN-REG-005
        { what: "50 characters", name: "a".repeat(50) },
        // TC-PLAN-REG-008
        { what: "accents and a space", name: "José Núñez" },
      ])("should register the visitor when the name has $what", ({ name }) => {
```

El cuerpo no cambia. Escribe tú la tabla de los nombres que se rechazan, en `describe("when the name is invalid")`, con una columna `expected` para el mensaje:

```javascript
        // TC-PLAN-REG-001
        { what: "1 character", name: "A", expected: NAME_LENGTH },
```

El nombre del test: `"should return '$expected' when the name has $what"`. En el cuerpo, `expect(result).toBe(expected)`.

```bash
pnpm test --verbose 2>&1 | grep "name has"
#         ✓ should register the visitor when the name has 2 characters (2 ms)
#         ✓ should register the visitor when the name has 3 characters (1 ms)
#         ✓ should register the visitor when the name has 49 characters
#         ✓ should register the visitor when the name has 50 characters
#         ✓ should register the visitor when the name has accents and a space (1 ms)
#         ✓ should return 'El nombre debe tener de 2 a 50 caracteres' when the name has 1 character
#         ✓ should return 'El nombre debe tener de 2 a 50 caracteres' when the name has 51 characters
#         ✓ should return 'El nombre solo admite letras y espacios' when the name has a digit
```

## Paso 4: age y schoolGroup (20 min)

Haz lo mismo con los otros grupos del inventario. En `age`, el número se lee bien en el nombre, así que no hace falta `what`:

```javascript
        // TC-PLAN-REG-011
        { age: 5 },
```

Usa estos nombres:

| Contexto | Nombre del test |
|---|---|
| `when the age is valid` | `should register the visitor when the age is $age` |
| `when the age is invalid` | `should return the age message when the age is $age` |
| `when the visitor is 17 or younger` (en `schoolGroup`) | `should register a school group visitor aged $age` |

`TC-PLAN-REG-019` (18 años en un grupo escolar) es el único que se rechaza en `schoolGroup`: déjalo como un `it` normal en `describe("when the visitor is 18 or older")`. Una tabla de una fila solo añade líneas.

Cada ID sigue en una línea sola, encima de su fila o de su `it`:

```bash
pnpm test --verbose
# PASS tests/visitor.test.js
#   validateVisitor
#     name
#       when the name is valid
#         ...
#     age
#       when the age is valid
#         ✓ should register the visitor when the age is 30 (1 ms)
#         ✓ should register the visitor when the age is 5
#         ✓ should register the visitor when the age is 6
#         ✓ should register the visitor when the age is 98
#         ✓ should register the visitor when the age is 99
#       when the age is invalid
#         ✓ should return the age message when the age is 4 (1 ms)
#         ✓ should return the age message when the age is 100
#         ✓ should return the age message when the age is 10.5
#     schoolGroup
#       when the visitor is 17 or younger
#         ✓ should register a school group visitor aged 16
#         ✓ should register a school group visitor aged 17
#       when the visitor is 18 or older
#         ✓ should reject a school group visitor aged 18
#
# Tests:       19 passed, 19 total
wc -l tests/visitor.test.js
# 120 tests/visitor.test.js
grep -c "// TC-PLAN-REG" tests/visitor.test.js
# 19
```

Los mismos 19 tests en 69 líneas menos. Un caso nuevo de edad es ahora una línea de datos y una de ID.

## Paso 5: ¿qué fila falla? (10 min)

Mete un bug en `src/visitor.js`: cambia `age > 99` por `age > 98`. Ejecuta:

```bash
pnpm test 2>&1 | grep -E '●|Expected|Received|^Tests:'
#   ● validateVisitor › age › when the age is valid › should register the visitor when the age is 99
#     Expected: "Visitante registrado"
#     Received: "La edad debe ser un número entero de 5 a 99"
# Tests:       1 failed, 18 passed, 19 total
```

El fallo señala la línea del `expect`, que es la misma para las cinco filas. Lo que dice qué fila falló es el nombre: «when the age is 99», `TC-PLAN-REG-014`.

Devuelve `age > 99` a su sitio:

```bash
git diff --stat src/
```

No tiene que salir nada.

## Paso 6: dos trampas del nombre (10 min)

En la tabla de `when the age is valid`, cambia el nombre del test por este, con una errata y un `%#`:

```javascript
"should register $expectd when the age is $age (%#)"
```

```bash
pnpm test --verbose 2>&1 | grep "expectd"
```

Mira cómo salen `$expectd` y `$age`, y explícalo con la [sección 6 de la teoría](../../1-teoria/01-tablas-de-casos-con-each.md#6-dos-trampas-del-nombre). Después, devuelve el nombre del paso 4.

---

## Comprobación

```bash
pnpm test
# Tests:       19 passed, 19 total
grep -c "\.each(" tests/visitor.test.js
# 5
grep -c "// TC-PLAN-REG" tests/visitor.test.js
# 19
```

Guarda el lab:

```bash
git add tests/visitor.test.js
git commit -m "lab 01: tablas con it.each"
```

## Repaso

- [ ] Sabes reconocer los tests que son el mismo test con otros datos.
- [ ] Las filas son objetos y el cuerpo toma los campos por nombre.
- [ ] El nombre de cada test lleva los datos de su fila, y el ID está encima de la fila.
- [ ] Los casos con un `expect` distinto van en otra tabla, y uno solo va en un `it`.
- [ ] Sabes encontrar la fila que falla por el nombre del test.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Matchers de objetos y arrays](../lab-02-matchers-de-objetos-y-arrays/README.md) → |

[↑ Semana 08](../../README.md)
