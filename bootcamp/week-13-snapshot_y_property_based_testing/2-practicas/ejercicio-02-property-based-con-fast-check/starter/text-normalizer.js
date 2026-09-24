function normalizeText(input) {
  // ============================================
  // PASO 5: Corregir el bug encontrado en el PASO 4
  // ============================================
  // Borra la línea marcada con "PASO 5: borrar" y descomenta esta:
  // return input.trim().replace(/\s+/g, " ").toLowerCase();

  // Versión inicial con bug: solo colapsa espacios, no tabs ni saltos de línea.
  return input.trim().replace(/ +/g, " ").toLowerCase(); // PASO 5: borrar
}

module.exports = { normalizeText };
