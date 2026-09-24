function add(a, b) {
  // ============================================
  // PASO 1: Corrige el bug (red → green)
  // ============================================
  // El test "should return 5 when adding 2 and 3" falla porque aqui se resta.
  // Borra la linea `return a - b;` y descomenta la linea correcta:
  // return a + b;
  return a - b;
}

function isEven(n) {
  return n % 2 === 0;
}

module.exports = { add, isEven };
