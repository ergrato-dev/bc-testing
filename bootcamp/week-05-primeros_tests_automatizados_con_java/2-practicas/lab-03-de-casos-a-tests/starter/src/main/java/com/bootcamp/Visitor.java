package com.bootcamp;

// Datos del formulario de registro del planetario (semana 02, lab 02).
// Cada método with... devuelve una copia con un solo campo cambiado.
public record Visitor(String name, double age, boolean schoolGroup) {

    public Visitor withName(String newName) {
        return new Visitor(newName, age, schoolGroup);
    }

    public Visitor withAge(double newAge) {
        return new Visitor(name, newAge, schoolGroup);
    }

    public Visitor withSchoolGroup(boolean newSchoolGroup) {
        return new Visitor(name, age, newSchoolGroup);
    }
}
