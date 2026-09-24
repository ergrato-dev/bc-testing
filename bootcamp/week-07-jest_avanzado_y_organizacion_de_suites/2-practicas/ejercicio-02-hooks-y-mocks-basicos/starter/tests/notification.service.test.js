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

  // ============================================
  // PASO 1: Inicializa dobles por test y restaura spies
  // ============================================
  // Descomenta las siguientes lineas:
  // beforeEach(() => {
  //   gateway = { send: jest.fn() };
  //   logger.entries = [];
  // });
  //
  // afterEach(() => {
  //   jest.restoreAllMocks();
  // });

  // ============================================
  // PASO 2: Stub de respuesta de dependencia
  // ============================================
  // Descomenta las siguientes lineas:
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
  // PASO 3: Spy sobre el colaborador real con jest.spyOn
  // ============================================
  // Descomenta las siguientes lineas:
  // test("should call logger when email is sent", () => {
  //   const infoSpy = jest.spyOn(logger, "info");
  //   gateway.send.mockReturnValue({ status: "queued" });
  //
  //   sendWelcomeEmail({ email: "test@example.com" }, gateway, logger);
  //
  //   expect(infoSpy).toHaveBeenCalledWith("email_sent", {
  //     email: "test@example.com"
  //   });
  //   expect(logger.entries).toHaveLength(1);
  // });

  // ============================================
  // PASO 4: Caso de validacion
  // ============================================
  // Descomenta las siguientes lineas:
  // test("should throw ValidationError when email is missing", () => {
  //   expect(() => sendWelcomeEmail({}, gateway, logger)).toThrow("ValidationError");
  // });
});
