// ============================================
// PASO 1: Mockear modulo payment gateway
// ============================================
// jest.mock("./payment.gateway", () => ({
//   charge: jest.fn(),
// }));

// const gateway = require("./payment.gateway");
// const { confirmOrder } = require("./order.service");

// ============================================
// PASO 2: Escenario exitoso
// ============================================
// test("should confirm order when gateway approves", async () => {
//   gateway.charge.mockResolvedValue({ approved: true, transactionId: "tx-123" });
//
//   const result = await confirmOrder(200);
//
//   expect(result).toEqual({ status: "confirmed", transactionId: "tx-123" });
//   expect(gateway.charge).toHaveBeenCalledWith(200);
// });

// ============================================
// PASO 3: Escenario de rechazo
// ============================================
// test("should throw error when gateway rejects payment", async () => {
//   gateway.charge.mockResolvedValue({ approved: false });
//
//   await expect(confirmOrder(800)).rejects.toThrow("payment rejected");
//   expect(gateway.charge).toHaveBeenCalledTimes(1);
// });
