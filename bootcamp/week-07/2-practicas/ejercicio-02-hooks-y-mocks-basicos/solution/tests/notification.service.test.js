const { sendWelcomeEmail } = require("../src/notification.service");

describe("NotificationService", () => {
  let gateway;
  let logger;

  beforeEach(() => {
    gateway = { send: jest.fn() };
    logger = { info: jest.fn() };
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  test("should return queued status when gateway accepts request", () => {
    gateway.send.mockReturnValue({ status: "queued" });

    const result = sendWelcomeEmail(
      { email: "test@example.com" },
      gateway,
      logger
    );

    expect(result).toEqual({ status: "queued" });
  });

  test("should call logger when email is sent", () => {
    gateway.send.mockReturnValue({ status: "queued" });

    sendWelcomeEmail({ email: "test@example.com" }, gateway, logger);

    expect(logger.info).toHaveBeenCalledWith("email_sent", {
      email: "test@example.com"
    });
  });

  test("should throw ValidationError when email is missing", () => {
    expect(() => sendWelcomeEmail({}, gateway, logger)).toThrow("ValidationError");
  });
});
