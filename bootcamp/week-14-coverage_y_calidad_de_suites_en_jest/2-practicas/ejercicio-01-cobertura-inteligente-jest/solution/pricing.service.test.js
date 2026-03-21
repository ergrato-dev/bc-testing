const { calculateFinalPrice } = require("./pricing.service");

test("should return base price when customer is not premium and hour is daytime", () => {
  const result = calculateFinalPrice({
    basePrice: 20,
    isPremium: false,
    hour: 12,
  });

  expect(result).toBe(20);
});

test("should throw error when base price is invalid", () => {
  expect(() =>
    calculateFinalPrice({ basePrice: 0, isPremium: false, hour: 10 }),
  ).toThrow("Invalid base price");
});

test("should apply premium discount", () => {
  const result = calculateFinalPrice({
    basePrice: 30,
    isPremium: true,
    hour: 14,
  });

  expect(result).toBe(27);
});

test("should add night surcharge between 22 and 05", () => {
  const result = calculateFinalPrice({
    basePrice: 20,
    isPremium: false,
    hour: 23,
  });

  expect(result).toBe(25);
});
