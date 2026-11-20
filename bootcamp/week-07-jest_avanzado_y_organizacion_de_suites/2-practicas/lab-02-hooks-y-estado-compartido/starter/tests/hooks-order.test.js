// Experimento: en qué orden ejecuta Jest los hooks. No prueba nada del planetario.
// process.stdout.write escribe en la salida normal; el informe de Jest va a la de errores.
function log(text) {
  process.stdout.write(`${text}\n`);
}

describe("outer", () => {
  log("outer: cuerpo del describe");

  beforeAll(() => log("outer: beforeAll"));
  beforeEach(() => log("outer: beforeEach"));
  afterEach(() => log("outer: afterEach"));
  afterAll(() => log("outer: afterAll"));

  it("first", () => log("    test first"));

  describe("inner", () => {
    log("inner: cuerpo del describe");

    beforeEach(() => log("inner: beforeEach"));
    afterEach(() => log("inner: afterEach"));

    it("second", () => log("    test second"));
  });
});
