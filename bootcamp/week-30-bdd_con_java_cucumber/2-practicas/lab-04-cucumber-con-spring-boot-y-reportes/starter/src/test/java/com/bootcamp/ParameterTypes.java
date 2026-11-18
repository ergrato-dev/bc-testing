package com.bootcamp;

import io.cucumber.java.ParameterType;
import java.time.LocalDate;

/** Tipos de parámetro de todo el glue: cualquier clase de steps puede usar {isoDate}. */
public class ParameterTypes {

    @ParameterType("\\d{4}-\\d{2}-\\d{2}")
    public LocalDate isoDate(String text) {
        return LocalDate.parse(text);
    }
}
