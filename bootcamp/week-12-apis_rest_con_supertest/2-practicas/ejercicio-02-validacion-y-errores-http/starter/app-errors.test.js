const request = require("supertest");
const { app } = require("./app-errors");

// ============================================
// PASO 1: Error de validacion
// ============================================
// test("should return 400 when name is missing", async () => {
//   const response = await request(app).post("/items").send({});
//
//   expect(response.status).toBe(400);
//   expect(response.body).toEqual({
//     error: "ValidationError",
//     message: "name is required",
//   });
// });

// ============================================
// PASO 2: Error de no encontrado
// ============================================
// test("should return 404 when item does not exist", async () => {
//   const response = await request(app).get("/items/it-999");
//
//   expect(response.status).toBe(404);
//   expect(response.body).toEqual({
//     error: "NotFoundError",
//     message: "item not found",
//   });
// });

// ============================================
// PASO 3: Error de conflicto
// ============================================
// test("should return 409 when item name already exists", async () => {
//   const response = await request(app)
//     .post("/items")
//     .send({ name: "Notebook" });
//
//   expect(response.status).toBe(409);
//   expect(response.body).toEqual({
//     error: "ConflictError",
//     message: "item name already exists",
//   });
// });
