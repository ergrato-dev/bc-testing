package com.bootcamp;

// Registro de visitantes del planetario (reglas R1 a R4 de plan-de-pruebas.md).
// Devuelve "Visitante registrado" o el mensaje de la primera regla que no se cumple.
public class VisitorValidator {

    private static final String NAME_PATTERN = "[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+";

    public static String validate(Visitor visitor) {
        String name = visitor.name();
        int age = visitor.age();

        if (name.length() < 2 || name.length() > 50) {
            return "El nombre debe tener de 2 a 50 caracteres";
        }
        if (!name.matches(NAME_PATTERN)) {
            return "El nombre solo admite letras y espacios";
        }
        if (age < 5 || age > 99) {
            return "La edad debe ser un número entero de 5 a 99";
        }
        if (visitor.schoolGroup() && age >= 18) {
            return "Los grupos escolares son para menores de 18 años";
        }
        return "Visitante registrado";
    }
}
