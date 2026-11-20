package com.bootcamp;

public class Calculator {

    public static int add(int a, int b) {
        // ============================================
        // PASO 1: Corrige el bug (rojo → verde)
        // ============================================
        // El test shouldReturn5WhenAdding2And3 falla porque aquí se resta.
        // Borra la línea `return a - b;` y descomenta la línea correcta:
        // return a + b;
        return a - b;
    }

    public static boolean isEven(int n) {
        return n % 2 == 0;
    }
}
