const { validateVisitor } = require("../src/visitor");

// Un visitante con los tres campos válidos. Cada test cambia un solo campo:
// una clase inválida por caso, como en la semana 02.
const validVisitor = { name: "Ana", age: 30, schoolGroup: false };

describe("validateVisitor", () => {
  describe("name", () => {
    // TC-PLAN-REG-001
    it("should reject the visitor when name has 1 character", () => {
      // Arrange
      const visitor = { ...validVisitor, name: "A" };

      // Act
      const result = validateVisitor(visitor);

      // Assert
      expect(result).toBe("El nombre debe tener de 2 a 50 caracteres");
    });

    // TODO: TC-PLAN-REG-002 a TC-PLAN-REG-008 (tabla del README)
  });

  describe("age", () => {
    // TC-PLAN-REG-009
    it("should register the visitor when age is 30", () => {
      // Arrange
      const visitor = { ...validVisitor, age: 30 };

      // Act
      const result = validateVisitor(visitor);

      // Assert
      expect(result).toBe("Visitante registrado");
    });

    // TODO: TC-PLAN-REG-010 a TC-PLAN-REG-016 (tabla del README)
  });

  describe("schoolGroup", () => {
    // TODO: TC-PLAN-REG-017 a TC-PLAN-REG-019 (tabla del README)
  });
});
