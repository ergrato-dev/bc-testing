# Estructura de un Test en Jest (AAA)

> **Semana 03 — Teoría 02** | Lenguaje: JavaScript

---

## Anatomía básica de Jest

Un test en Jest normalmente tiene 3 bloques estructurales:

1. `describe()` — agrupa casos de prueba relacionados
2. `test()` / `it()` — define un escenario puntual
3. `expect()` — realiza la verificación (assertion)

Ejemplo base:

```javascript
describe("Calculator", () => {
  test("should add two numbers", () => {
    expect(2 + 3).toBe(5);
  });
});
```

---

## Patrón AAA (Arrange-Act-Assert)

El patrón AAA hace que tus tests sean legibles y consistentes.

- **Arrange**: preparar datos y dependencias
- **Act**: ejecutar la acción que se evalúa
- **Assert**: verificar resultados

Ejemplo con AAA explícito:

```javascript
const { add } = require("../src/calculator");

describe("add", () => {
  test("should return 7 when adding 3 and 4", () => {
    // Arrange
    const a = 3;
    const b = 4;

    // Act
    const result = add(a, b);

    // Assert
    expect(result).toBe(7);
  });
});
```

---

## Nomenclatura profesional de tests

Formato recomendado:

```text
should [expected result] when [condition]
```

Ejemplos:

- `should return total amount when cart has valid items`
- `should throw ValidationError when email is invalid`
- `should return false when password is empty`

Nombres a evitar:

- `test1`
- `works`
- `debe funcionar`

---

## Agrupación y setup compartido

Cuando varios tests comparten preparación, usa `beforeEach`.

```javascript
describe("UserService", () => {
  let service;

  beforeEach(() => {
    service = {
      users: [],
      create(user) {
        this.users.push(user);
        return user;
      },
    };
  });

  test("should create a user", () => {
    const created = service.create({ id: 1, name: "Ana" });
    expect(created.name).toBe("Ana");
  });

  test("should increase total users after create", () => {
    service.create({ id: 2, name: "Luis" });
    expect(service.users.length).toBe(1);
  });
});
```

---

## `test` vs `it`

En Jest son equivalentes. Escoge uno y mantén consistencia en la suite.

```javascript
test("should return true", () => {});
it("should return true", () => {});
```

Convención sugerida para bootcamp:

- Usar `describe()` para contexto
- Usar `it("should ... when ...")`

---

## Cuándo un test está mal diseñado

Señales frecuentes:

- Verifica demasiadas cosas no relacionadas
- Tiene pasos ocultos que no se entienden en lectura rápida
- No sigue AAA (Arrange y Assert mezclados)
- El nombre no explica intención
- Falla de forma intermitente (flaky)

---

## Resumen

```text
describe -> agrupa comportamiento
  test/it -> define escenario
    Arrange -> prepara
    Act     -> ejecuta
    Assert  -> verifica
```

La consistencia estructural es clave para escalar de 5 a 500 tests sin caos.

---

## Próximo tema

→ [Matchers básicos y ejecución de pruebas](./03-matchers-y-ejecucion.md)
