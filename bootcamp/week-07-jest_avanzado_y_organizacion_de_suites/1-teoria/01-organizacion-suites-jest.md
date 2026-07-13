# 01 - Organizacion de Suites y Convenciones de Jest

**Tipo**: JavaScript (Jest)

![Mapa de suites Jest](../0-assets/01-mapa-suites-jest.svg)

---

## Objetivo

Pasar de tests sueltos a una suite estructurada y mantenible: que cualquier persona del equipo encuentre un test por su nombre, sin leer el archivo completo.

---

## Estructura recomendada

```javascript
describe("UserService", () => {
  describe("createUser", () => {
    it("should create user when payload is valid", () => {
      // Arrange
      // Act
      // Assert
    });
  });

  describe("deleteUser", () => {
    it("should throw NotFoundError when user does not exist", () => {
      // Arrange
      // Act
      // Assert
    });
  });
});
```

---

## Reglas de oro

1. Un `describe` principal por modulo o clase.
2. Un `describe` secundario por metodo o comportamiento.
3. Tests nombrados con formato `should [expected] when [condition]`.
4. Evitar repetir setup en cada test cuando un hook puede simplificar.
5. Un `it`/`test` valida un solo comportamiento, no una lista de casos encadenados.

---

## Ejemplo completo: organizar por comportamiento

Un servicio con varios metodos se agrupa por metodo primero y por escenario despues, no por orden de escritura del codigo:

```javascript
describe("PlanetariumSessionService", () => {
  describe("schedule", () => {
    it("should create session when slot is available", () => {
      // Arrange, Act, Assert
    });

    it("should throw SlotTakenError when slot overlaps another session", () => {
      // Arrange, Act, Assert
    });
  });

  describe("cancel", () => {
    it("should mark session as cancelled when session exists", () => {
      // Arrange, Act, Assert
    });

    it("should throw NotFoundError when session id does not exist", () => {
      // Arrange, Act, Assert
    });
  });

  describe("listUpcoming", () => {
    it("should return empty array when no sessions are scheduled", () => {
      // Arrange, Act, Assert
    });
  });
});
```

Cada `describe` interno responde a una sola pregunta: "que hace este metodo en este escenario". Si un `describe` crece mas de 6-7 tests, suele ser señal de que el metodo hace demasiado.

---

## Olores comunes

- Suite plana con 30 tests sin agrupacion.
- Nombres ambiguos como `works` o `test1`.
- Multiples comportamientos mezclados en un solo test.
- `describe` que agrupa por tipo de dato en vez de por comportamiento (ej. "tests de strings", "tests de numeros").
- Tests que dependen del orden de ejecucion de otros tests dentro del mismo `describe`.

---

## Ejecucion selectiva

Con la suite organizada por archivo y `describe`, correr un subconjunto es directo:

```bash
pnpm test PlanetariumSessionService
pnpm test -- -t "should throw SlotTakenError"
```

Nombrar bien los archivos y los `describe` no es cosmetica: es lo que hace posible ejecutar solo lo relevante durante desarrollo.

---

## Beneficio clave

Una buena estructura reduce friccion en code review y acelera debugging: localizar el test que falla toma segundos, no minutos.
