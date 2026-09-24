const { buildProduct, sumPrices, validatePrice } = require("./product.utils");

// ============================================
// PASO 1: Igualdad: toEqual vs toStrictEqual
// ============================================
// describe("toEqual vs toStrictEqual", () => {
//   test("should build product object when payload is valid", () => {
//     const result = buildProduct("Notebook", 1200);
//     expect(result).toStrictEqual({
//       name: "Notebook",
//       price: 1200,
//       tags: ["catalog", "active"],
//       stock: { warehouse: "central", units: 10 },
//     });
//   });
//
//   test("should detect undefined keys only with toStrictEqual", () => {
//     const result = buildProduct("Notebook", 1200);
//     const expected = {
//       name: "Notebook",
//       price: 1200,
//       tags: ["catalog", "active"],
//       stock: { warehouse: "central", units: 10 },
//       discount: undefined,
//     };
//
//     // toEqual ignora la clave con valor undefined; toStrictEqual no.
//     expect(result).toEqual(expected);
//     expect(result).not.toStrictEqual(expected);
//   });
// });

// ============================================
// PASO 2: Inclusion, forma parcial y propiedades anidadas
// ============================================
// describe("inclusion, forma parcial y propiedades", () => {
//   test("should include active tag when product is built", () => {
//     const result = buildProduct("Notebook", 1200);
//     expect(result.tags).toContain("active");
//   });
//
//   test("should find a product object inside a catalog", () => {
//     const catalog = [buildProduct("Notebook", 1200), buildProduct("Mouse", 25)];
//     expect(catalog).toContainEqual({
//       name: "Mouse",
//       price: 25,
//       tags: ["catalog", "active"],
//       stock: { warehouse: "central", units: 10 },
//     });
//   });
//
//   test("should match product partial shape", () => {
//     const result = buildProduct("Notebook", 1200);
//     expect(result).toMatchObject({ name: "Notebook", stock: { units: 10 } });
//   });
//
//   test("should expose nested stock warehouse property", () => {
//     const result = buildProduct("Notebook", 1200);
//     expect(result).toHaveProperty("stock.warehouse", "central");
//   });
// });

// ============================================
// PASO 3: Decimales con toBeCloseTo
// ============================================
// describe("numeros decimales", () => {
//   test("should sum decimal prices with floating point tolerance", () => {
//     const total = sumPrices([0.1, 0.2]);
//     expect(total).not.toBe(0.3);
//     expect(total).toBeCloseTo(0.3, 5);
//   });
// });

// ============================================
// PASO 4: Error esperado
// ============================================
// describe("errores", () => {
//   test("should throw ValidationError when price is negative", () => {
//     expect(() => validatePrice(-1)).toThrow("ValidationError");
//   });
// });
