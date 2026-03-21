const { retryOperation } = require("./retry.service");

beforeEach(() => {
  jest.useFakeTimers();
});

afterEach(() => {
  jest.useRealTimers();
  jest.clearAllMocks();
});

test("should resolve after one retry", async () => {
  const operation = jest
    .fn()
    .mockRejectedValueOnce(new Error("temporary"))
    .mockResolvedValueOnce("ok");

  const promise = retryOperation(operation, 2, 1000);

  await Promise.resolve();
  jest.advanceTimersByTime(1000);

  await expect(promise).resolves.toBe("ok");
  expect(operation).toHaveBeenCalledTimes(2);
});

test("should reject when retries are exhausted", async () => {
  const operation = jest.fn().mockRejectedValue(new Error("fatal"));

  const promise = retryOperation(operation, 1, 1000);

  await Promise.resolve();
  jest.advanceTimersByTime(1000);

  await expect(promise).rejects.toThrow("fatal");
  expect(operation).toHaveBeenCalledTimes(2);
});
