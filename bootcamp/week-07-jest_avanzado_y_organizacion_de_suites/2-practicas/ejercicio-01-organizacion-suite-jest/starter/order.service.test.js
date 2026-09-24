const { calculateTotal, validateOrder } = require("./order.service");

// Suite principal: agrupa todo el comportamiento de OrderService.
describe("OrderService", () => {
  // ============================================
  // PASO 1: Anida un describe por método (calculateTotal)
  // ============================================
  // Descomenta las siguientes líneas:
  // describe("calculateTotal", () => {
  //   test("should return total amount when items are valid", () => {
  //     const items = [
  //       { price: 10, quantity: 2 },
  //       { price: 5, quantity: 1 }
  //     ];
  //
  //     const result = calculateTotal(items);
  //
  //     expect(result).toBe(25);
  //   });
  // });

  describe("validateOrder", () => {
    // ============================================
    // PASO 2: Caso inválido dentro del grupo validateOrder
    // ============================================
    // Descomenta las siguientes líneas:
    // test("should throw ValidationError when items are empty", () => {
    //   expect(() => validateOrder([])).toThrow("ValidationError");
    // });

    // ============================================
    // PASO 3: Caso válido en el mismo grupo
    // ============================================
    // Descomenta las siguientes líneas:
    // test("should return true when items contain at least one entry", () => {
    //   const result = validateOrder([{ price: 1, quantity: 1 }]);
    //   expect(result).toBe(true);
    // });
  });
});
