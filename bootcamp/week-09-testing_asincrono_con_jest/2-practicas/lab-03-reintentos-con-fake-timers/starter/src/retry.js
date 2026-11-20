// Envía un aviso con reintentos: si notifier.send rechaza, espera y lo vuelve a intentar.
// Antes del primer reintento espera `delay` ms, y el doble antes de cada siguiente (1 s, 2 s, 4 s...).
// Si todos los intentos fallan, rechaza con el último error.

function wait(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

async function sendWithRetry(notifier, to, message, { retries = 2, delay = 1000 } = {}) {
  for (let attempt = 0; ; attempt += 1) {
    try {
      return await notifier.send(to, message);
    } catch (error) {
      if (attempt === retries) {
        throw error;
      }
      await wait(delay);
    }
  }
}

module.exports = { sendWithRetry };
