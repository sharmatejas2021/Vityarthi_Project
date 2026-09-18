package com.library.util;

import com.library.exception.InvalidInputException;

import java.util.regex.Pattern;

/**
 * Input validation helpers used before data reaches the DAOs.
 */
public final class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{10}$");
    private static final Pattern ISBN_PATTERN =
            Pattern.compile("^[0-9\\-]{10,17}$");

    private ValidationUtil() { }

    public static void requireNonBlank(String value, String fieldName) throws InvalidInputException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(fieldName + " cannot be empty.");
        }
    }

    public static void validateEmail(String email) throws InvalidInputException {
        requireNonBlank(email, "Email");
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidInputException("Invalid email format: " + email);
        }
    }

    public static void validatePhone(String phone) throws InvalidInputException {
        requireNonBlank(phone, "Phone");
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new InvalidInputException("Phone number must be exactly 10 digits: " + phone);
        }
    }

    public static void validateIsbn(String isbn) throws InvalidInputException {
        requireNonBlank(isbn, "ISBN");
        if (!ISBN_PATTERN.matcher(isbn).matches()) {
            throw new InvalidInputException("Invalid ISBN format: " + isbn);
        }
    }

    public static void validatePositive(int value, String fieldName) throws InvalidInputException {
        if (value <= 0) {
            throw new InvalidInputException(fieldName + " must be a positive number.");
        }
    }
}
