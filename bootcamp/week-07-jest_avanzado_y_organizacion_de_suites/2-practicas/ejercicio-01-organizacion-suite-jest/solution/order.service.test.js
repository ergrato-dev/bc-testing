const { calculateTotal, validateOrder } = require("./order.service");

describe("OrderService", () => {
  describe("calculateTotal", () => {
    test("should return total amount when items are valid", () => {
      const items = [
        { price: 10, quantity: 2 },
        { price: 5, quantity: 1 }
      ];

      const result = calculateTotal(items);

      expect(result).toBe(25);
    });
  });

  describe("validateOrder", () => {
    test("should throw ValidationError when items are empty", () => {
      expect(() => validateOrder([])).toThrow("ValidationError");
    });

    test("should return true when items contain at least one entry", () => {
      const result = validateOrder([{ price: 1, quantity: 1 }]);
      expect(result).toBe(true);
    });
  });
});
