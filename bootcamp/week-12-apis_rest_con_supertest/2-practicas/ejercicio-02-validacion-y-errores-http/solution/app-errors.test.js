const request = require("supertest");
const { createApp } = require("./app-errors");

let app;

beforeEach(() => {
  // App y repositorio nuevos por test: ningun test hereda datos de otro.
  app = createApp();
});

test("should return 400 when name is missing", async () => {
  const response = await request(app).post("/items").send({});

  expect(response.status).toBe(400);
  expect(response.body).toEqual({
    error: "ValidationError",
    message: "name is required",
  });
});

test("should return 404 when item does not exist", async () => {
  const response = await request(app).get("/items/it-999");

  expect(response.status).toBe(404);
  expect(response.body).toEqual({
    error: "NotFoundError",
    message: "item not found",
  });
});

test("should return 409 when item name already exists", async () => {
  const response = await request(app).post("/items").send({ name: "Notebook" });

  expect(response.status).toBe(409);
  expect(response.body).toEqual({
    error: "ConflictError",
    message: "item name already exists",
  });
});

test("should return 500 when repository fails unexpectedly", async () => {
  const failingRepository = {
    findById: () => {
      throw new Error("database down");
    },
  };
  const failingApp = createApp({ repository: failingRepository });

  const response = await request(failingApp).get("/items/it-1");

  expect(response.status).toBe(500);
  expect(response.body).toEqual({
    error: "InternalServerError",
    message: "unexpected error",
  });
});
