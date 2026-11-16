package com.bootcamp;

public class LoanNotFoundException extends RuntimeException {

    public LoanNotFoundException(String id) {
        super("loan not found: " + id);
    }
}
