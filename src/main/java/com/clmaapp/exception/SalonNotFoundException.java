package com.clmaapp.exception;

public class SalonNotFoundException extends RuntimeException {

    public SalonNotFoundException(String message) {
        super(message);
    }

    public SalonNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
