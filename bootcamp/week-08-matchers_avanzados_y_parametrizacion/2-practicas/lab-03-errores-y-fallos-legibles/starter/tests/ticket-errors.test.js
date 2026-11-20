const { createTicket } = require("../src/ticket");

const ana = { name: "Ana Ruiz", age: 30, schoolGroup: false };
const session = { date: "2026-12-05", room: "Cúpula" };

describe("createTicket errors", () => {
  describe("when the visitor is invalid", () => {
    it.each([
      { what: "a 1 character name", change: { name: "A" }, expected: "El nombre debe tener de 2 a 50 caracteres" },
      { what: "a digit in the name", change: { name: "Ana3" }, expected: "El nombre solo admite letras y espacios" },
      { what: "age 4", change: { age: 4 }, expectd: "La edad debe ser un número entero de 5 a 99" },
      { what: "age 18 in a school group", change: { age: 18, schoolGroup: true }, expected: "Los grupos escolares son para menores de 18 años" },
    ])("should throw '$expected' for $what", ({ change, expected }) => {
      const visitor = { ...ana, ...change };

      expect(() => createTicket(visitor, session)).toThrow(expected);
    });
  });
});
