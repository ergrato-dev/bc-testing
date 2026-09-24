const { sellTickets, pricing } = require("./ticket.service");

let seatApi;
let ticketPrinter;

beforeEach(() => {
  // mockImplementation: el doble calcula la respuesta a partir de los argumentos.
  seatApi = {
    reserve: jest.fn().mockImplementation(async (showId, quantity) =>
      Array.from({ length: quantity }, (_, index) => `A${index + 1}`),
    ),
  };
  ticketPrinter = { print: jest.fn() };
});

afterEach(() => {
  jest.restoreAllMocks();
});

test("should reserve as many seats as tickets requested", async () => {
  const result = await sellTickets({ showId: "S1", showType: "flat", quantity: 3 }, seatApi, ticketPrinter);

  expect(result.seats).toEqual(["A1", "A2", "A3"]);
  expect(seatApi.reserve).toHaveBeenCalledWith("S1", 3);
});

test("should propagate the error only on the call overridden with mockImplementationOnce", async () => {
  seatApi.reserve.mockImplementationOnce(async () => {
    throw new Error("sold out");
  });
  const order = { showId: "S1", showType: "flat", quantity: 1 };

  await expect(sellTickets(order, seatApi, ticketPrinter)).rejects.toThrow("sold out");
  await expect(sellTickets(order, seatApi, ticketPrinter)).resolves.toEqual({ total: 12, seats: ["A1"] });
});

test("should print tickets in seat order", async () => {
  await sellTickets({ showId: "S1", showType: "flat", quantity: 2 }, seatApi, ticketPrinter);

  expect(ticketPrinter.print).toHaveBeenCalledTimes(2);
  expect(ticketPrinter.print).toHaveBeenNthCalledWith(1, { seat: "A1", number: 1 });
  expect(ticketPrinter.print).toHaveBeenNthCalledWith(2, { seat: "A2", number: 2 });
});

test("should ask pricing for the show type when selling tickets", async () => {
  const priceSpy = jest.spyOn(pricing, "basePrice");

  const result = await sellTickets({ showId: "S2", showType: "dome", quantity: 2 }, seatApi, ticketPrinter);

  // El spy observa la llamada que hace sellTickets, sin reemplazar la lógica real.
  expect(priceSpy).toHaveBeenCalledWith("dome");
  expect(result.total).toBe(40);
});

test("should use a stubbed price when the spy overrides the return value", async () => {
  jest.spyOn(pricing, "basePrice").mockReturnValue(5);

  const result = await sellTickets({ showId: "S2", showType: "dome", quantity: 2 }, seatApi, ticketPrinter);

  expect(result.total).toBe(10);
});

test("should show the difference between mockClear, mockReset and mockRestore", () => {
  const priceSpy = jest.spyOn(pricing, "basePrice").mockReturnValue(99);
  pricing.basePrice("dome");

  // mockClear: borra el historial de llamadas, conserva la implementación falsa.
  priceSpy.mockClear();
  expect(priceSpy).not.toHaveBeenCalled();
  expect(pricing.basePrice("dome")).toBe(99);

  // mockReset: además borra la implementación; el mock devuelve undefined.
  priceSpy.mockReset();
  expect(pricing.basePrice("dome")).toBeUndefined();

  // mockRestore: vuelve a poner el método original (solo aplica a spies).
  priceSpy.mockRestore();
  expect(pricing.basePrice("dome")).toBe(20);
  expect(jest.isMockFunction(pricing.basePrice)).toBe(false);
});
