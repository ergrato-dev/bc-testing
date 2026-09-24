const { calculateTotal, validateOrder } = require("./order.service");

// Suite principal: agrupa todo el comportamiento de OrderService.
describe("OrderService", () => {
  // ============================================
  // PASO 1: Anida un describe por metodo (calculateTotal)
  // ============================================
  // Descomenta las siguientes lineas:
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
    // PASO 2: Caso invalido dentro del grupo validateOrder
    // ============================================
    // Descomenta las siguientes lineas:
    // test("should throw ValidationError when items are empty", () => {
    //   expect(() => validateOrder([])).toThrow("ValidationError");
    // });

    // ============================================
    // PASO 3: Caso valido en el mismo grupo
    // ============================================
    // Descomenta las siguientes lineas:
    // test("should return true when items contain at least one entry", () => {
    //   const result = validateOrder([{ price: 1, quantity: 1 }]);
    //   expect(result).toBe(true);
    // });
  });
});
