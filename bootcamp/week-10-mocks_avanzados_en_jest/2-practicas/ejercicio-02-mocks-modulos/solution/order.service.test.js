// Mock parcial: se conserva la implementacion real del modulo (toCents)
// y solo se reemplaza la funcion que sale a la red (charge).
jest.mock("./payment.gateway", () => ({
  ...jest.requireActual("./payment.gateway"),
  charge: jest.fn(),
}));

const gateway = require("./payment.gateway");
const { confirmOrder } = require("./order.service");

afterEach(() => {
  jest.clearAllMocks();
});

test("should keep real toCents and mock only charge", () => {
  expect(jest.isMockFunction(gateway.charge)).toBe(true);
  expect(jest.isMockFunction(gateway.toCents)).toBe(false);
});

test("should confirm order when gateway approves", async () => {
  gateway.charge.mockResolvedValue({ approved: true, transactionId: "tx-123" });

  const result = await confirmOrder(200);

  expect(result).toEqual({ status: "confirmed", transactionId: "tx-123" });
  // 200 llega convertido a centimos por la implementacion real de toCents.
  expect(gateway.charge).toHaveBeenCalledWith(20000);
});

test("should throw error when gateway rejects payment", async () => {
  gateway.charge.mockResolvedValue({ approved: false });

  await expect(confirmOrder(800)).rejects.toThrow("payment rejected");
  expect(gateway.charge).toHaveBeenCalledTimes(1);
});

test("should propagate network error when charge fails", async () => {
  gateway.charge.mockImplementation(async () => {
    throw new Error("gateway timeout");
  });

  await expect(confirmOrder(50)).rejects.toThrow("gateway timeout");
});
