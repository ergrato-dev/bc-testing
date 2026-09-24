const request = require("supertest");
const { createApp } = require("./app");

// ============================================
// TEST SUITE: API REST ItemService
// Adaptar al dominio asignado
// ============================================

// NOTA PARA EL APRENDIZ:
// Adapta esta suite a tu dominio asignado.
// Ejemplos:
// - Museo: ExhibitApi
// - Planetario: SessionApi
// - Acuario: SpeciesApi

describe("Item API", () => {
  let app;

  beforeEach(() => {
    // App y repositorio nuevos por test: ningun test depende de otro.
    app = createApp();
  });

  describe("GET /items", () => {
    // TODO: should return list of items
  });

  describe("GET /items/:id", () => {
    // TODO: should return item when id exists
    // TODO: should return 404 when id does not exist
  });

  describe("POST /items", () => {
    // TODO: should create item when payload is valid
    // TODO: should return 400 when name is missing
    // TODO: should return 409 when item already exists
  });

  describe("Error contracts", () => {
    // TODO: validar shape de errores { error, message }
    // TODO: should return 500 when repository fails
    //       (usa createApp({ repository }) con un repositorio que lance)
  });
});
