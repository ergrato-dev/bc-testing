const pricing = {
  basePrice(showType) {
    return showType === "dome" ? 20 : 12;
  },
};

async function sellTickets(order, seatApi, ticketPrinter) {
  if (!order?.quantity || order.quantity < 1) {
    throw new Error("quantity must be at least 1");
  }

  const seats = await seatApi.reserve(order.showId, order.quantity);
  seats.forEach((seat, index) => ticketPrinter.print({ seat, number: index + 1 }));

  const total = pricing.basePrice(order.showType) * order.quantity;
  return { total, seats };
}

module.exports = { sellTickets, pricing };
