const { applyDiscount } = require("./discount.service");

test("should return discounted price when percentage is valid", () => {
  const result = applyDiscount(100, 10);
  expect(result).toBe(90);
});

test.each([
  [100, 10, 90],
  [200, 25, 150],
  [80, 0, 80],
])(
  "should return %i when price is %i and discount is %i",
  (price, percentage, expected) => {
    const result = applyDiscount(price, percentage);
    expect(result).toBe(expected);
  },
);

test.each([
  [-1, 10],
  [100, -5],
  [100, 120],
])(
  "should throw ValidationError when inputs are invalid",
  (price, percentage) => {
    expect(() => applyDiscount(price, percentage)).toThrow("ValidationError");
  },
);
