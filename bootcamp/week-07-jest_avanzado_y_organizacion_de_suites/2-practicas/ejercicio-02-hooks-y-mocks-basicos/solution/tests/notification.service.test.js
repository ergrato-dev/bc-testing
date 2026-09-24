const { sendWelcomeEmail } = require("../src/notification.service");

// Colaborador real (no es un doble): un logger en memoria compartido,
// como lo seria un logger de la aplicacion.
const logger = {
  entries: [],
  info(event, data) {
    this.entries.push({ event, data });
  },
};

describe("NotificationService", () => {
  let gateway;

  beforeEach(() => {
    gateway = { send: jest.fn() };
    logger.entries = [];
  });

  afterEach(() => {
    jest.restoreAllMocks();
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
    const infoSpy = jest.spyOn(logger, "info");
    gateway.send.mockReturnValue({ status: "queued" });

    sendWelcomeEmail({ email: "test@example.com" }, gateway, logger);

    expect(infoSpy).toHaveBeenCalledWith("email_sent", {
      email: "test@example.com"
    });
    expect(logger.entries).toHaveLength(1);
  });

  test("should throw ValidationError when email is missing", () => {
    expect(() => sendWelcomeEmail({}, gateway, logger)).toThrow("ValidationError");
  });
});
