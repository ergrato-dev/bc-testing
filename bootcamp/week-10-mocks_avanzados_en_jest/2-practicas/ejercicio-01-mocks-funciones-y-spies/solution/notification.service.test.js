const { notifyUser, auditLogger } = require("./notification.service");

test("should return delivered true when notifier resolves", async () => {
  const notifier = {
    send: jest.fn().mockResolvedValue({ ok: true }),
  };

  const result = await notifyUser({ email: "learner@test.com" }, notifier);

  expect(result).toEqual({ delivered: true });
});

test("should call notifier with expected payload", async () => {
  const notifier = {
    send: jest.fn().mockResolvedValue({ ok: true }),
  };

  await notifyUser({ email: "learner@test.com" }, notifier);

  expect(notifier.send).toHaveBeenCalledTimes(1);
  expect(notifier.send).toHaveBeenCalledWith({
    to: "learner@test.com",
    template: "welcome",
  });
});

test("should observe logger calls with spy", () => {
  const logSpy = jest.spyOn(auditLogger, "log");

  const entry = auditLogger.log("NOTIFY", "learner@test.com");

  expect(entry).toBe("NOTIFY:learner@test.com");
  expect(logSpy).toHaveBeenCalledWith("NOTIFY", "learner@test.com");
  logSpy.mockRestore();
});
