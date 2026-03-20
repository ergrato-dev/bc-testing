const { isValidAmount } = require("./amount.service");

describe("AmountService", () => {
  test("should return true when amount is positive", () => {
    const result = isValidAmount(10);
    expect(result).toBe(true);
  });

  test("should return false when amount is negative", () => {
    const result = isValidAmount(-1);
    expect(result).toBe(false);
  });

  test("should return true when amount is zero", () => {
    const result = isValidAmount(0);
    expect(result).toBe(true);
  });
});
