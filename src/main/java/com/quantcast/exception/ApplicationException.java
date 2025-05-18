package com.quantcast.exception;

/**
 * Base unchecked exception for expected, user‐facing errors.
 */
public class ApplicationException extends Exception {
    public ApplicationException(String message) {
        super(message);
    }
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
