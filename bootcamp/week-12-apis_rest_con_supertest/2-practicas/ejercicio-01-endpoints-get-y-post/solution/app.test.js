const request = require("supertest");
const { createApp } = require("./app");

let app;

beforeEach(() => {
  // App y repositorio nuevos por test: ningun test hereda datos de otro.
  app = createApp();
});

test("should return api health status", async () => {
  const response = await request(app).get("/health");

  expect(response.status).toBe(200);
  expect(response.body).toEqual({ status: "ok" });
});

test("should return item list", async () => {
  const response = await request(app).get("/items");

  expect(response.status).toBe(200);
  expect(Array.isArray(response.body.items)).toBe(true);
  expect(response.body.items.length).toBeGreaterThan(0);
});

test("should create item when payload is valid", async () => {
  const response = await request(app).post("/items").send({ name: "Mouse" });

  expect(response.status).toBe(201);
  expect(response.body).toEqual({
    id: expect.any(String),
    name: "Mouse",
  });
});

test("should not see items created by previous tests", async () => {
  const response = await request(app).get("/items");

  expect(response.body.items).toEqual([{ id: "it-1", name: "Notebook" }]);
});
