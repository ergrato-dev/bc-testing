const fc = require("fast-check");
const { buildPublicItem, normalizeName } = require("./item.presenter");

// ============================================
// TEST SUITE: ItemPresenter
// Snapshot + Property-Based
// ============================================

// NOTA PARA EL APRENDIZ:
// Adapta esta suite a tu dominio asignado.
// Ejemplos:
// - Museo: ExhibitPresenter
// - Planetario: SessionPresenter
// - Acuario: SpeciesPresenter

describe("ItemPresenter", () => {
  describe("example tests", () => {
    // TODO: should build public item when input is valid
    // TODO: should normalize name for display rules
  });

  describe("snapshot tests", () => {
    // TODO: should match snapshot for stable public payload
  });

  describe("property-based tests", () => {
    // TODO: should be idempotent when normalizing names
    // TODO: should remove duplicate spaces for any string
  });

  describe("validation tests", () => {
    // TODO: agregar test de entrada invalida si aplica a tu dominio
  });
});
