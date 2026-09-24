// ============================================
// TEST SUITE: ItemService (JavaScript)
// ============================================
// TODO: crea `item.service.js` con la logica de tu dominio y requiérelo aqui.
// TODO: sustituye cada `test.todo` por un test AAA real. Usa los mismos
//       Test Case ID que en `test-plan.md` y que en las suites Python y Java.

describe("ItemService", () => {
  // TODO: declara aqui la instancia del servicio bajo prueba.

  beforeEach(() => {
    // TODO: crea una instancia nueva del servicio antes de cada test.
  });

  describe("createItem", () => {
    test.todo("should create item when payload is valid"); // TC-001
    test.todo("should throw ValidationError when name is empty"); // TC-002
    test.todo("should throw ValidationError when amount is negative"); // TC-003
    // TODO: anade un caso limite (p. ej. amount = 0) y su TC en test-plan.md.
  });

  describe("updateItem", () => {
    // TODO: casos de actualizacion (happy path + validaciones) con su TC-00X.
  });
});
