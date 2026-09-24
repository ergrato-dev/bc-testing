const {
  buildDeliveryReport,
  isDeliveryOverdue,
  createReportStore,
} = require("./report.builder");
const { notifyDelivery } = require("./delivery.notifier");

// ============================================
// PASO 1: Reemplazar assert débil por contrato claro
// ============================================
// /*
//  * FRÁGIL (assert débil): pasa aunque el builder devuelva cualquier objeto.
//  *
//  * test("should create report object", () => {
//  *   const result = buildDeliveryReport({ id: "r-1", customerName: " Ada ", delivered: true, items: ["book", "pen"] });
//  *   expect(result).toBeTruthy();
//  * });
//  */
// test("should build normalized delivery report when input is valid", () => {
//   const result = buildDeliveryReport({
//     id: "r-1",
//     customerName: " Ada ",
//     delivered: true,
//     items: ["book", "pen"],
//   });
//
//   expect(result).toEqual({
//     id: "r-1",
//     customerName: "Ada",
//     delivered: true,
//     itemCount: 2,
//     status: "delivered",
//   });
// });

// ============================================
// PASO 2: Ruta de validación
// ============================================
// test("should throw error when required fields are missing", () => {
//   expect(() => buildDeliveryReport({ delivered: false, items: [] })).toThrow(
//     "Missing required fields",
//   );
// });

// ============================================
// PASO 3: Dependencia del tiempo -> reloj controlado
// ============================================
// /*
//  * FRÁGIL (dependencia del tiempo): usa el reloj real.
//  * Pasa hoy y empieza a fallar solo el 1 de enero de 2031, sin que nadie toque el código.
//  *
//  * test("should not be overdue when due date is in the future", () => {
//  *   expect(isDeliveryOverdue("2031-01-01T00:00:00Z")).toBe(false);
//  * });
//  */
// describe("isDeliveryOverdue", () => {
//   beforeEach(() => {
//     jest.useFakeTimers();
//     jest.setSystemTime(new Date("2026-06-01T12:00:00Z"));
//   });
//
//   afterEach(() => {
//     jest.useRealTimers();
//   });
//
//   test("should return false when due date is later than now", () => {
//     expect(isDeliveryOverdue("2026-06-02T00:00:00Z")).toBe(false);
//   });
//
//   test("should return true when due date is earlier than now", () => {
//     expect(isDeliveryOverdue("2026-05-31T00:00:00Z")).toBe(true);
//   });
//
//   test("should return false when due date is exactly now", () => {
//     expect(isDeliveryOverdue("2026-06-01T12:00:00Z")).toBe(false);
//   });
// });

// ============================================
// PASO 4: Dependencia del orden -> estado nuevo en cada test
// ============================================
// /*
//  * FRÁGIL (dependencia del orden): una sola instancia compartida por todos los tests.
//  * El segundo test solo pasa si el primero corrió antes; con `test.only` o en otro orden, falla.
//  *
//  * const sharedStore = createReportStore();
//  * test("should add first report", () => {
//  *   sharedStore.add({ id: "r-1" });
//  *   expect(sharedStore.count()).toBe(1);
//  * });
//  * test("should add second report", () => {
//  *   sharedStore.add({ id: "r-2" });
//  *   expect(sharedStore.count()).toBe(2);
//  * });
//  */
// describe("createReportStore", () => {
//   let store;
//
//   beforeEach(() => {
//     store = createReportStore();
//   });
//
//   test("should count one report when a single report is added", () => {
//     store.add({ id: "r-1" });
//
//     expect(store.count()).toBe(1);
//   });
//
//   test("should count two reports when two reports are added", () => {
//     store.add({ id: "r-1" });
//     store.add({ id: "r-2" });
//
//     expect(store.count()).toBe(2);
//   });
// });

// ============================================
// PASO 5: Over-mocking -> simular solo la frontera externa
// ============================================
// /*
//  * FRÁGIL (over-mocking): se mockea el builder real, que es lógica propia y pura.
//  * El test sigue en verde aunque alguien borre el `.trim()` o cambie el cálculo de `status`,
//  * porque solo verifica el cableado entre mocks. Incluso pasa con un input vacío `{}`,
//  * que en producción lanzaría "Missing required fields".
//  *
//  * jest.mock("./report.builder", () => ({
//  *   buildDeliveryReport: jest.fn(() => ({ id: "r-1", customerName: "Ada", delivered: true })),
//  * }));
//  * test("should notify", () => {
//  *   const notifier = { send: jest.fn() };
//  *   notifyDelivery({}, notifier);
//  *   expect(notifier.send).toHaveBeenCalled();
//  * });
//  */
// describe("notifyDelivery", () => {
//   test("should send normalized message when report is delivered", () => {
//     const notifier = { send: jest.fn() };
//
//     const report = notifyDelivery(
//       { id: "r-1", customerName: " Ada ", delivered: true, items: [] },
//       notifier,
//     );
//
//     expect(report.status).toBe("delivered");
//     expect(notifier.send).toHaveBeenCalledTimes(1);
//     expect(notifier.send).toHaveBeenCalledWith("Report r-1 delivered to Ada");
//   });
//
//   test("should not send message when report is pending", () => {
//     const notifier = { send: jest.fn() };
//
//     notifyDelivery(
//       { id: "r-2", customerName: "Grace", delivered: false, items: [] },
//       notifier,
//     );
//
//     expect(notifier.send).not.toHaveBeenCalled();
//   });
// });
