jest.mock("./payment.gateway", () => ({
  charge: jest.fn(),
}));

const gateway = require("./payment.gateway");
const { confirmOrder } = require("./order.service");

afterEach(() => {
  jest.clearAllMocks();
});

test("should confirm order when gateway approves", async () => {
  gateway.charge.mockResolvedValue({ approved: true, transactionId: "tx-123" });

  const result = await confirmOrder(200);

  expect(result).toEqual({ status: "confirmed", transactionId: "tx-123" });
  expect(gateway.charge).toHaveBeenCalledWith(200);
});

test("should throw error when gateway rejects payment", async () => {
  gateway.charge.mockResolvedValue({ approved: false });

  await expect(confirmOrder(800)).rejects.toThrow("payment rejected");
  expect(gateway.charge).toHaveBeenCalledTimes(1);
});
