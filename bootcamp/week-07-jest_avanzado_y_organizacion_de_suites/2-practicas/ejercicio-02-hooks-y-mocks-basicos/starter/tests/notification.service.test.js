const { sendWelcomeEmail } = require("../src/notification.service");

describe("NotificationService", () => {
  let gateway;
  let logger;

  // ============================================
  // PASO 1: Inicializa dobles por test
  // ============================================
  // beforeEach(() => {
  //   gateway = { send: jest.fn() };
  //   logger = { info: jest.fn() };
  // });

  // ============================================
  // PASO 2: Stub de respuesta de dependencia
  // ============================================
  // test("should return queued status when gateway accepts request", () => {
  //   gateway.send.mockReturnValue({ status: "queued" });
  //
  //   const result = sendWelcomeEmail(
  //     { email: "test@example.com" },
  //     gateway,
  //     logger
  //   );
  //
  //   expect(result).toEqual({ status: "queued" });
  // });

  // ============================================
  // PASO 3: Spy de colaborador
  // ============================================
  // test("should call logger when email is sent", () => {
  //   gateway.send.mockReturnValue({ status: "queued" });
  //
  //   sendWelcomeEmail({ email: "test@example.com" }, gateway, logger);
  //
  //   expect(logger.info).toHaveBeenCalledWith("email_sent", {
  //     email: "test@example.com"
  //   });
  // });
});
