const fc = require("fast-check");
const { buildOrderSummary } = require("../order.summary");

// ============================================
// PASO 1: Unit test de regla base
// ============================================
// test("should build summary with subtotal and total", () => {
//   const summary = buildOrderSummary({
//     orderId: "o-1",
//     items: [{ price: 10 }, { price: 5.5 }],
//     shipping: 2,
//   });
//
//   expect(summary).toEqual({
//     orderId: "o-1",
//     itemCount: 2,
//     subtotal: 15.5,
//     shipping: 2,
//     total: 17.5,
//   });
// });

// ============================================
// PASO 2: Rama de error
// ============================================
// test("should throw Invalid shipping when shipping is negative", () => {
//   expect(() =>
//     buildOrderSummary({ orderId: "o-1", items: [{ price: 10 }], shipping: -1 })
//   ).toThrow("Invalid shipping");
// });

// ============================================
// PASO 3: Snapshot acotado
// ============================================
// test("should match inline snapshot for stable summary payload", () => {
//   const summary = buildOrderSummary({
//     orderId: "o-2",
//     items: [{ price: 12.25 }, { price: 7.75 }],
//     shipping: 3,
//   });
//
//   expect(summary).toMatchInlineSnapshot(`
// {
//   "itemCount": 2,
//   "orderId": "o-2",
//   "shipping": 3,
//   "subtotal": 20,
//   "total": 23,
// }
// `);
// });

// ============================================
// PASO 4: Property-based
// ============================================
// test("should always return non-negative total when prices and shipping are non-negative", () => {
//   fc.assert(
//     fc.property(
//       fc.array(fc.float({ min: 0, max: 1000, noNaN: true }), { maxLength: 20 }),
//       fc.float({ min: 0, max: 100, noNaN: true }),
//       (prices, shipping) => {
//         const items = prices.map((price) => ({ price }));
//         const summary = buildOrderSummary({ orderId: "o-p", items, shipping });
//
//         expect(summary.total).toBeGreaterThanOrEqual(0);
//         expect(summary.total).toBeGreaterThanOrEqual(summary.subtotal);
//       }
//     )
//   );
// });
