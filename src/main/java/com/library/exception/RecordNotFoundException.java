package com.library.exception;

/** Thrown when a lookup by ID (book, member, or transaction) finds nothing. */
public class RecordNotFoundException extends LibraryException {
    public RecordNotFoundException(String message) {
        super(message);
    }
}
