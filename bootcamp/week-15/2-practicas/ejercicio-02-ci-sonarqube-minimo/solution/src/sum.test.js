const { sum } = require("./sum");

test("should sum two numbers", () => {
  expect(sum(2, 3)).toBe(5);
});

test("should throw error when values are not numbers", () => {
  expect(() => sum("2", 3)).toThrow("Both values must be numbers");
});
