const { calculateDiscount } = require("./discount-calculator");

test("should apply 10 percent discount when membership is premium", () => {
  const result = calculateDiscount(100, "premium");
  expect(result).toBe(90);
});

test("should return full price when membership is basic", () => {
  const result = calculateDiscount(100, "basic");
  expect(result).toBe(100);
});

test("should throw error when price is negative", () => {
  expect(() => calculateDiscount(-10, "premium")).toThrow("invalid price");
});
