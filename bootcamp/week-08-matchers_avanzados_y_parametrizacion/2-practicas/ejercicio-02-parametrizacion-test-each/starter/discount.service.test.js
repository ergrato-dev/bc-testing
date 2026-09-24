const { applyDiscount } = require("./discount.service");

// ============================================
// PASO 1: Caso individual
// ============================================
// test("should return discounted price when percentage is valid", () => {
//   const result = applyDiscount(100, 10);
//   expect(result).toBe(90);
// });

// ============================================
// PASO 2: Parametrizacion con test.each (array de arrays)
// ============================================
// // El orden de los %i sigue el orden de las columnas: [price, percentage, expected]
// test.each([
//   [100, 10, 90],
//   [200, 25, 150],
//   [80, 0, 80],
// ])(
//   "should turn price %i with discount %i into %i",
//   (price, percentage, expected) => {
//     const result = applyDiscount(price, percentage);
//     expect(result).toBe(expected);
//   },
// );

// ============================================
// PASO 3: Tabla con template literal y $variable
// ============================================
// test.each`
//   price  | percentage | expected
//   ${50}  | ${50}      | ${25}
//   ${100} | ${100}     | ${0}
//   ${0}   | ${30}      | ${0}
// `(
//   "should return $expected when price is $price and discount is $percentage",
//   ({ price, percentage, expected }) => {
//     expect(applyDiscount(price, percentage)).toBe(expected);
//   },
// );

// ============================================
// PASO 4: Casos invalidos agrupados con describe.each
// ============================================
// describe.each([
//   [-1, 10],
//   [100, -5],
//   [100, 120],
// ])("when price is %i and discount is %i", (price, percentage) => {
//   test("should throw ValidationError", () => {
//     expect(() => applyDiscount(price, percentage)).toThrow("ValidationError");
//   });
// });
