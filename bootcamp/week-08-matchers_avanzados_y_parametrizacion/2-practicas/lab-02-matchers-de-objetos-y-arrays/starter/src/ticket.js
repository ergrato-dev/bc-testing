// Entradas del planetario: precio por edad, código de descuento y recibo.
const { validateVisitor } = require("./visitor");

const PRICES = { child: 5, adult: 9, senior: 6 };
const DISCOUNTS = { ESTRELLA: 10, COMETA: 25 };
let lastNumber = 0;

function priceFor(age) {
  if (age < 12) return PRICES.child;
  if (age >= 65) return PRICES.senior;
  return PRICES.adult;
}

function createTicket(visitor, session, discountCode) {
  const message = validateVisitor(visitor);
  if (message !== "Visitante registrado") {
    throw new Error(message);
  }
  lastNumber += 1;
  const basePrice = priceFor(visitor.age);
  const percent = discountCode ? DISCOUNTS[discountCode] : 0;
  const ticket = {
    code: `PLAN-${session.date.replaceAll("-", "")}-${String(lastNumber).padStart(4, "0")}`,
    visitor: { name: visitor.name, age: visitor.age },
    session: { date: session.date, room: session.room },
    price: basePrice - (basePrice * percent) / 100,
    discount: discountCode ? { code: discountCode, percent } : undefined,
    issuedAt: new Date().toISOString(),
  };
  return ticket;
}

function createGroupTickets(visitors, session) {
  return visitors.map((visitor) => createTicket(visitor, session));
}

// Una línea por campo, en el orden del objeto.
function formatReceipt(ticket) {
  return Object.entries(ticket)
    .map(([key, value]) => `${key}: ${typeof value === "object" ? JSON.stringify(value) : value}`)
    .join("\n");
}

module.exports = { createTicket, createGroupTickets, formatReceipt };
