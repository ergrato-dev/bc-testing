const { buildDeliveryReport } = require("./report.builder");

test("should create report object", () => {
  const result = buildDeliveryReport({
    id: "r-1",
    customerName: " Ada ",
    delivered: true,
    items: ["book", "pen"],
  });

  expect(result).toBeTruthy();
});

test("should build normalized delivery report", () => {
  const result = buildDeliveryReport({
    id: "r-1",
    customerName: " Ada ",
    delivered: true,
    items: ["book", "pen"],
  });

  expect(result).toEqual({
    id: "r-1",
    customerName: "Ada",
    delivered: true,
    itemCount: 2,
    status: "delivered",
  });
});

test("should throw error when required fields are missing", () => {
  expect(() => buildDeliveryReport({ delivered: false, items: [] })).toThrow(
    "Missing required fields",
  );
});
