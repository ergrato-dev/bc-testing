const { calculateFinalPrice } = require("./pricing.service");

// ============================================
// PASO 1: Caso feliz sin descuentos
// ============================================
// test("should return base price when customer is not premium and hour is daytime", () => {
//   const result = calculateFinalPrice({
//     basePrice: 20,
//     isPremium: false,
//     hour: 12,
//   });
//
//   expect(result).toBe(20);
// });

// ============================================
// PASO 2: Validacion de precio base
// ============================================
// test("should throw error when base price is invalid", () => {
//   expect(() =>
//     calculateFinalPrice({ basePrice: 0, isPremium: false, hour: 10 }),
//   ).toThrow("Invalid base price");
// });

// ============================================
// PASO 3: Rama premium
// ============================================
// test("should apply premium discount when customer is premium", () => {
//   const result = calculateFinalPrice({
//     basePrice: 30,
//     isPremium: true,
//     hour: 14,
//   });
//
//   expect(result).toBe(27);
// });

// ============================================
// PASO 4: Rama nocturna
// ============================================
// test("should add night surcharge when hour is between 22 and 05", () => {
//   const result = calculateFinalPrice({
//     basePrice: 20,
//     isPremium: false,
//     hour: 23,
//   });
//
//   expect(result).toBe(25);
// });

// ============================================
// PASO 6: Cubrir la rama que el reporte marco como no cubierta (Invalid hour)
// ============================================
// test.each([-1, 24, "12"])(
//   "should throw Invalid hour when hour is %p",
//   (hour) => {
//     expect(() =>
//       calculateFinalPrice({ basePrice: 20, isPremium: false, hour }),
//     ).toThrow("Invalid hour");
//   },
// );

// ============================================
// PASO 7: Bordes de la franja nocturna (22 y 5) que el coverage no exige
// ============================================
// test.each([
//   [21, 20],
//   [22, 25],
//   [5, 25],
//   [6, 20],
// ])(
//   "should apply the right surcharge when hour is %p on the night border (expected %p)",
//   (hour, expected) => {
//     const result = calculateFinalPrice({ basePrice: 20, isPremium: false, hour });
//
//     expect(result).toBe(expected);
//   },
// );
