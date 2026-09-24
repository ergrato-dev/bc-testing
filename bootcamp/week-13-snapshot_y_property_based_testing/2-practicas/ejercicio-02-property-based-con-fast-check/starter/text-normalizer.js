function normalizeText(input) {
  // ============================================
  // PASO 5: Corregir el bug encontrado en el PASO 4
  // ============================================
  // Borra la linea marcada con "PASO 5: borrar" y descomenta esta:
  // return input.trim().replace(/\s+/g, " ").toLowerCase();

  // Version inicial con bug: solo colapsa espacios, no tabs ni saltos de linea.
  return input.trim().replace(/ +/g, " ").toLowerCase(); // PASO 5: borrar
}

module.exports = { normalizeText };
