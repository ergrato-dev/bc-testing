const fc = require("fast-check");
const { buildPublicItem, paginate } = require("./item.presenter");

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
    // TODO: should trim item name in public payload
    // TODO: should split 5 items into pages of 2, 2 and 1
  });

  describe("snapshot tests", () => {
    // TODO: should match snapshot for stable public payload
  });

  describe("property-based tests", () => {
    // TODO: should keep all items in order when pages are concatenated
    // TODO: should never create a page larger than pageSize
    // TODO: should create Math.ceil(items.length / pageSize) pages
    // Pista: fc.array(...) para items y fc.integer({ min: 1, max: 20 }) para pageSize
  });

  describe("validation tests", () => {
    // TODO: should throw when item id is missing
    // TODO: should throw when item name is blank
    // TODO: should throw when pageSize is not a positive integer
    // TODO: should throw when items is not an array
  });
});
