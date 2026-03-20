const { ItemService } = require("./item.service");

// ============================================
// TEST SUITE: ItemService
// Implementacion incremental guiada por TDD
// ============================================

// NOTA PARA EL APRENDIZ:
// Adapta esta suite a tu dominio asignado.
// Ejemplos:
// - Museo: ExhibitService (exhibicion)
// - Planetario: SessionService (sesion)
// - Acuario: SpeciesService (especie)

describe("ItemService", () => {
  // TODO (Green): Inicializar dependencias y servicio
  // let repository;
  // let service;

  beforeEach(() => {
    // TODO: Configurar doubles para el primer ciclo
    // repository = {
    //   create: jest.fn(),
    //   updateStock: jest.fn(),
    // };
    // service = new ItemService(repository);
  });

  describe("create", () => {
    // TODO (Red 1): should create item when input is valid
    // TODO (Green 1): implementacion minima para pasar
    // TODO (Red 2): should throw validation error when name is missing
    // TODO (Refactor 1): limpiar duplicacion en setup
  });

  describe("updateStock", () => {
    // TODO (Red 3): should update stock when quantity is valid
    // TODO (Red 4): should throw validation error when itemId is missing
    // TODO (Red 5): should throw validation error when quantity is negative
    // TODO (Refactor 2): mejorar nombres y estructura sin cambiar comportamiento
  });
});
