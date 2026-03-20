package com.bootcamp;

public class Calculator {
    public static int add(int a, int b) {
        // Bug intencional para practicar red-green.
        return a - b;
    }

    public static boolean isEven(int n) {
        return n % 2 == 0;
    }
}
