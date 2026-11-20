const { sendWithRetry } = require("../src/retry");

const TO = "ana@example.com";
const MESSAGE = "Reserva 1 confirmada: 2 butacas el 2026-12-05";

// Un notificador que falla siempre, como un servidor de correo caído.
function failingNotifier() {
  return { send: jest.fn().mockRejectedValue(new Error("SMTP no responde")) };
}

describe("sendWithRetry", () => {
  describe("when every attempt fails", () => {
    it("should try three times and reject with the last error", async () => {
      const notifier = failingNotifier();

      await expect(sendWithRetry(notifier, TO, MESSAGE)).rejects.toThrow("SMTP no responde");

      expect(notifier.send).toHaveBeenCalledTimes(3);
    });
  });
});
