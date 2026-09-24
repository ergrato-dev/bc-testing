const { ItemService } = require("./item.service");

// ============================================
// TEST SUITE: ItemService
// Servicio asíncrono para gestión de elementos del dominio
// ============================================

// NOTA PARA EL APRENDIZ:
// Adapta esta suite a tu dominio asignado.
// Ejemplos:
// - Museo: ExhibitService (pieza de exhibición)
// - Planetario: ShowService (función)
// - Acuario: TankService (estanque)

describe("ItemService", () => {
  // TODO: Configurar mocks del repositorio
  // let repository;
  // let service;

  beforeEach(() => {
    // TODO: Inicializar repository y service
    // repository = { create: jest.fn(), findById: jest.fn() };
    // service = new ItemService(repository);
  });

  describe("create", () => {
    // TODO: Implementar tests asíncronos para create
    // 1. should create item when input is valid
    // 2. should throw validation error when name is missing
    // 3. should propagate repository error when create fails
  });

  describe("findById", () => {
    // TODO: Implementar tests asíncronos para findById
    // 1. should return item when id exists
    // 2. should throw validation error when id is missing
    // 3. should throw not found error when repository returns null
  });

  describe("findByIdWithRetry", () => {
    // TODO: Activar fake timers en beforeEach y restaurarlos en afterEach
    // TODO: Implementar tests con fake timers (usa await jest.advanceTimersByTimeAsync)
    // 1. should resolve after retry when first attempt fails
    // 2. should reject with last error when retries are exhausted
    // 3. should not retry when id is missing
  });
});
