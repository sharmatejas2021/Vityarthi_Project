package com.library.exception;

/** Thrown when a member tries to issue more books than their type allows. */
public class MemberLimitExceededException extends LibraryException {
    public MemberLimitExceededException(String message) {
        super(message);
    }
}
