package com.quantcast.exception;

/**
 * Thrown for CLI or input errors (missing flags, bad dates, etc.).
 */
public class InputValidationException extends ApplicationException {
    public InputValidationException(String message) {
        super(message);
    }
}


