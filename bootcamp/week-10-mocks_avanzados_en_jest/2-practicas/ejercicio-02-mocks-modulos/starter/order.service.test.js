// ============================================
// PASO 1: Mock parcial del módulo con jest.requireActual
// ============================================
// // Mock parcial: se conserva la implementación real del módulo (toCents)
// // y solo se reemplaza la función que sale a la red (charge).
// jest.mock("./payment.gateway", () => ({
//   ...jest.requireActual("./payment.gateway"),
//   charge: jest.fn(),
// }));
//
// const gateway = require("./payment.gateway");
// const { confirmOrder } = require("./order.service");
//
// afterEach(() => {
//   jest.clearAllMocks();
// });
//
// test("should keep real toCents and mock only charge", () => {
//   expect(jest.isMockFunction(gateway.charge)).toBe(true);
//   expect(jest.isMockFunction(gateway.toCents)).toBe(false);
// });

// ============================================
// PASO 2: Verificar qué parte es real y qué parte es mock
// ============================================
// test("should confirm order when gateway approves", async () => {
//   gateway.charge.mockResolvedValue({ approved: true, transactionId: "tx-123" });
//
//   const result = await confirmOrder(200);
//
//   expect(result).toEqual({ status: "confirmed", transactionId: "tx-123" });
//   // 200 llega convertido a céntimos por la implementación real de toCents.
//   expect(gateway.charge).toHaveBeenCalledWith(20000);
// });

// ============================================
// PASO 3: Escenario exitoso
// ============================================
// test("should throw error when gateway rejects payment", async () => {
//   gateway.charge.mockResolvedValue({ approved: false });
//
//   await expect(confirmOrder(800)).rejects.toThrow("payment rejected");
//   expect(gateway.charge).toHaveBeenCalledTimes(1);
// });

// ============================================
// PASO 4: Escenario de rechazo
// ============================================
// test("should propagate network error when charge fails", async () => {
//   gateway.charge.mockImplementation(async () => {
//     throw new Error("gateway timeout");
//   });
//
//   await expect(confirmOrder(50)).rejects.toThrow("gateway timeout");
// });

// ============================================
// PASO 5: Fallo de red con mockImplementation
// ============================================
//
