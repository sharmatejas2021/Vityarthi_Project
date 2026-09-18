package com.library.exception;

/** Thrown when user-supplied input fails validation (format, range, etc.). */
public class InvalidInputException extends LibraryException {
    public InvalidInputException(String message) {
        super(message);
    }
}
