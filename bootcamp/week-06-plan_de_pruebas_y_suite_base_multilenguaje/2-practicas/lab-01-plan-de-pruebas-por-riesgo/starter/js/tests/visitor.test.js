const { validateVisitor } = require("../src/visitor");

describe("validateVisitor", () => {
  // TC-PLAN-001
  it("should register the visitor when all fields are valid", () => {
    expect(validateVisitor({ name: "Ana Ruiz", age: 30, schoolGroup: false })).toBe("Visitante registrado");
  });

  // TC-PLAN-002
  it("should reject the name when it has 1 character", () => {
    expect(validateVisitor({ name: "A", age: 30, schoolGroup: false })).toBe(
      "El nombre debe tener de 2 a 50 caracteres",
    );
  });

  // TC-PLAN-003
  it("should register the visitor when the name has 50 characters", () => {
    expect(validateVisitor({ name: "a".repeat(50), age: 30, schoolGroup: false })).toBe("Visitante registrado");
  });

  // TC-PLAN-004
  it("should reject the name when it contains a digit", () => {
    expect(validateVisitor({ name: "Ana 2", age: 30, schoolGroup: false })).toBe(
      "El nombre solo admite letras y espacios",
    );
  });

  // TC-PLAN-005
  it("should reject the age when it is 4", () => {
    expect(validateVisitor({ name: "Ana Ruiz", age: 4, schoolGroup: false })).toBe(
      "La edad debe ser un número entero de 5 a 99",
    );
  });

  // TC-PLAN-006
  it("should register the visitor when the age is 99", () => {
    expect(validateVisitor({ name: "Ana Ruiz", age: 99, schoolGroup: false })).toBe("Visitante registrado");
  });

  // TC-PLAN-007
  it("should register the school group visitor when the age is 17", () => {
    expect(validateVisitor({ name: "Ana Ruiz", age: 17, schoolGroup: true })).toBe("Visitante registrado");
  });

  // TC-PLAN-008
  it("should reject the school group visitor when the age is 18", () => {
    expect(validateVisitor({ name: "Ana Ruiz", age: 18, schoolGroup: true })).toBe(
      "Los grupos escolares son para menores de 18 años",
    );
  });
});
