package com.bootcamp;

public class DuplicateMemberException extends RuntimeException {

    public DuplicateMemberException(String email) {
        super("email already registered: " + email);
    }
}
