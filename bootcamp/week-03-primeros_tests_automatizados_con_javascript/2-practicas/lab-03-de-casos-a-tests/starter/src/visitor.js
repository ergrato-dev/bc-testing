// Validación del formulario de registro del planetario (semana 02, lab 02).
// Devuelve "Visitante registrado" o el mensaje del primer campo inválido.

const NAME_PATTERN = /^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+$/;

function validateVisitor({ name, age, schoolGroup }) {
  if (name.length < 3 || name.length > 50) {
    return "El nombre debe tener de 2 a 50 caracteres";
  }
  if (!NAME_PATTERN.test(name)) {
    return "El nombre solo admite letras y espacios";
  }
  if (typeof age !== "number" || age < 5 || age >= 99) {
    return "La edad debe ser un número entero de 5 a 99";
  }
  if (schoolGroup && age >= 17) {
    return "Los grupos escolares son para menores de 18 años";
  }
  return "Visitante registrado";
}

module.exports = { validateVisitor };
