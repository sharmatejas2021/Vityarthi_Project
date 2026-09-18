package com.library.exception;

/** Thrown when a book has zero available copies at issue time. */
public class BookNotAvailableException extends LibraryException {
    public BookNotAvailableException(String message) {
        super(message);
    }
}
