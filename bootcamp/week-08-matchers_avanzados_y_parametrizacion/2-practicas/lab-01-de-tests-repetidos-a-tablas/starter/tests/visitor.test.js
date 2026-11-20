const { validateVisitor } = require("../src/visitor");

// Un visitante con los tres campos válidos. Cada test cambia un solo campo.
const validVisitor = { name: "Ana", age: 30, schoolGroup: false };

const REGISTERED = "Visitante registrado";
const NAME_LENGTH = "El nombre debe tener de 2 a 50 caracteres";
const NAME_LETTERS = "El nombre solo admite letras y espacios";
const AGE = "La edad debe ser un número entero de 5 a 99";
const SCHOOL = "Los grupos escolares son para menores de 18 años";

describe("validateVisitor", () => {
  describe("name", () => {
    // TC-PLAN-REG-001
    it("should reject the visitor when name has 1 character", () => {
      const visitor = { ...validVisitor, name: "A" };

      const result = validateVisitor(visitor);

      expect(result).toBe(NAME_LENGTH);
    });

    // TC-PLAN-REG-002
    it("should register the visitor when name has 2 characters", () => {
      const visitor = { ...validVisitor, name: "Al" };

      const result = validateVisitor(visitor);

      expect(result).toBe(REGISTERED);
    });

    // TC-PLAN-REG-003
    it("should register the visitor when name has 3 characters", () => {
      const visitor = { ...validVisitor, name: "Ana" };

      const result = validateVisitor(visitor);

      expect(result).toBe(REGISTERED);
    });

    // TC-PLAN-REG-004
    it("should register the visitor when name has 49 characters", () => {
      const visitor = { ...validVisitor, name: "a".repeat(49) };

      const result = validateVisitor(visitor);

      expect(result).toBe(REGISTERED);
    });

    // TC-PLAN-REG-005
    it("should register the visitor when name has 50 characters", () => {
      const visitor = { ...validVisitor, name: "a".repeat(50) };

      const result = validateVisitor(visitor);

      expect(result).toBe(REGISTERED);
    });

    // TC-PLAN-REG-006
    it("should reject the visitor when name has 51 characters", () => {
      const visitor = { ...validVisitor, name: "a".repeat(51) };

      const result = validateVisitor(visitor);

      expect(result).toBe(NAME_LENGTH);
    });

    // TC-PLAN-REG-007
    it("should reject the visitor when name has a digit", () => {
      const visitor = { ...validVisitor, name: "Ana3" };

      const result = validateVisitor(visitor);

      expect(result).toBe(NAME_LETTERS);
    });

    // TC-PLAN-REG-008
    it("should register the visitor when name has accents and a space", () => {
      const visitor = { ...validVisitor, name: "José Núñez" };

      const result = validateVisitor(visitor);

      expect(result).toBe(REGISTERED);
    });
  });

  describe("age", () => {
    // TC-PLAN-REG-009
    it("should register the visitor when age is 30", () => {
      const visitor = { ...validVisitor, age: 30 };

      const result = validateVisitor(visitor);

      expect(result).toBe(REGISTERED);
    });

    // TC-PLAN-REG-010
    it("should reject the visitor when age is 4", () => {
      const visitor = { ...validVisitor, age: 4 };

      const result = validateVisitor(visitor);

      expect(result).toBe(AGE);
    });

    // TC-PLAN-REG-011
    it("should register the visitor when age is 5", () => {
      const visitor = { ...validVisitor, age: 5 };

      const result = validateVisitor(visitor);

      expect(result).toBe(REGISTERED);
    });

    // TC-PLAN-REG-012
    it("should register the visitor when age is 6", () => {
      const visitor = { ...validVisitor, age: 6 };

      const result = validateVisitor(visitor);

      expect(result).toBe(REGISTERED);
    });

    // TC-PLAN-REG-013
    it("should register the visitor when age is 98", () => {
      const visitor = { ...validVisitor, age: 98 };

      const result = validateVisitor(visitor);

      expect(result).toBe(REGISTERED);
    });

    // TC-PLAN-REG-014
    it("should register the visitor when age is 99", () => {
      const visitor = { ...validVisitor, age: 99 };

      const result = validateVisitor(visitor);

      expect(result).toBe(REGISTERED);
    });

    // TC-PLAN-REG-015
    it("should reject the visitor when age is 100", () => {
      const visitor = { ...validVisitor, age: 100 };

      const result = validateVisitor(visitor);

      expect(result).toBe(AGE);
    });

    // TC-PLAN-REG-016
    it("should reject the visitor when age is 10.5", () => {
      const visitor = { ...validVisitor, age: 10.5 };

      const result = validateVisitor(visitor);

      expect(result).toBe(AGE);
    });
  });

  describe("schoolGroup", () => {
    // TC-PLAN-REG-017
    it("should register the visitor when age is 16 in a school group", () => {
      const visitor = { ...validVisitor, age: 16, schoolGroup: true };

      const result = validateVisitor(visitor);

      expect(result).toBe(REGISTERED);
    });

    // TC-PLAN-REG-018
    it("should register the visitor when age is 17 in a school group", () => {
      const visitor = { ...validVisitor, age: 17, schoolGroup: true };

      const result = validateVisitor(visitor);

      expect(result).toBe(REGISTERED);
    });

    // TC-PLAN-REG-019
    it("should reject the visitor when age is 18 in a school group", () => {
      const visitor = { ...validVisitor, age: 18, schoolGroup: true };

      const result = validateVisitor(visitor);

      expect(result).toBe(SCHOOL);
    });
  });
});
