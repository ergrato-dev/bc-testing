// Registro de visitantes del planetario (reglas R1 a R4 de plan-de-pruebas.md).
// Devuelve "Visitante registrado" o el mensaje de la primera regla que no se cumple.

const NAME_PATTERN = /^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+$/;

function validateVisitor({ name, age, schoolGroup }) {
  if (name.length < 2 || name.length > 50) {
    return "El nombre debe tener de 2 a 50 caracteres";
  }
  if (!NAME_PATTERN.test(name)) {
    return "El nombre solo admite letras y espacios";
  }
  if (!Number.isInteger(age) || age < 5 || age > 99) {
    return "La edad debe ser un número entero de 5 a 99";
  }
  if (schoolGroup && age >= 18) {
    return "Los grupos escolares son para menores de 18 años";
  }
  return "Visitante registrado";
}

module.exports = { validateVisitor };
