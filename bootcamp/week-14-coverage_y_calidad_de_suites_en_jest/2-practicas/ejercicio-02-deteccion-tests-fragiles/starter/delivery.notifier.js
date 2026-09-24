const { buildDeliveryReport } = require("./report.builder");

// El notifier es la frontera externa (email, cola, etc.): es lo único que conviene simular.
function notifyDelivery(input, notifier) {
  const report = buildDeliveryReport(input);

  if (report.delivered) {
    notifier.send(`Report ${report.id} delivered to ${report.customerName}`);
  }

  return report;
}

module.exports = { notifyDelivery };
