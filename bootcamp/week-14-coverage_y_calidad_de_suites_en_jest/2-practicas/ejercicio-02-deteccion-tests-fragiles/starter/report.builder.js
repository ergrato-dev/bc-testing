function buildDeliveryReport({ id, customerName, delivered, items }) {
  if (!id || !customerName) {
    throw new Error("Missing required fields");
  }

  const safeItems = Array.isArray(items) ? items : [];

  return {
    id,
    customerName: customerName.trim(),
    delivered: Boolean(delivered),
    itemCount: safeItems.length,
    status: delivered ? "delivered" : "pending",
  };
}

// Depende del reloj del sistema: sin controlarlo, el test cambia de resultado con el tiempo.
function isDeliveryOverdue(dueDate) {
  return Date.now() > new Date(dueDate).getTime();
}

// Estado interno mutable: si varios tests comparten la misma instancia, dependen del orden.
function createReportStore() {
  const reports = [];

  return {
    add(report) {
      reports.push(report);
    },
    count() {
      return reports.length;
    },
  };
}

module.exports = {
  buildDeliveryReport,
  isDeliveryOverdue,
  createReportStore,
};
