const request = require("supertest");
const { createApp } = require("./app");
const { createItemService } = require("./item.service");

// ============================================
// TEST SUITE: API de items (integracion con Supertest)
// ============================================
// Integracion real: app Express + service real + repository en memoria.
// Solo el almacenamiento se reemplaza; la logica de negocio NO se mockea.

describe("POST /items", () => {
  // TODO: Declarar app (y el repository en memoria si lo necesitas en los asserts)

  beforeEach(() => {
    // TODO: Crear un repository en memoria nuevo en cada test (findByName/save sobre un Map)
    // TODO: Crear el service real con ese repository y la app con createApp(service)
  });

  // TODO: should respond 201 with the saved item when payload is valid
  // TODO: should respond 400 with error message when name is empty
  // TODO: should respond 400 when quantity is negative
  // TODO: should respond 409 when item name already exists (case insensitive)
});
