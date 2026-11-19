const { cartTotal, applyCoupon, shippingCost } = require("../src/cart");

// Los cinco tests pasan. Ninguno prueba lo que dice su nombre.

describe("cart", () => {
  it("should return 25 when cart has a 10 euro item and a 15 euro item", () => {
    const items = [
      { price: 10, quantity: 1 },
      { price: 15, quantity: 1 },
    ];

    const total = cartTotal(items);

    total == 25;
  });

  it("should return 35 when cart has two 10 euro items and a 15 euro item", () => {
    const items = [
      { price: 10, quantity: 2 },
      { price: 15, quantity: 1 },
    ];

    const total = cartTotal(items);

    expect(total).toBeTruthy();
  });

  it("should throw an error when coupon is unknown", () => {
    try {
      applyCoupon(50, "FALSO");
    } catch (error) {
      expect(error.message).toBe("Invalid coupon");
    }
  });

  it("should return 45 when coupon PLANETA10 is applied to 50", () => {
    const expected = applyCoupon(50, "PLANETA10");

    const result = applyCoupon(50, "PLANETA10");

    expect(result).toBe(expected);
  });

  it("should return free shipping when total is 30 or more", () => {
    const result = shippingCost(100);

    expect(result).toBe(0);
  });
});
