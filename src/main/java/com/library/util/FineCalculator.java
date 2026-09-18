package com.library.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Encapsulates fine calculation logic in one place so the rate
 * can change without touching DAO or service code.
 */
public final class FineCalculator {
    private static final double FINE_PER_DAY = 5.0; // Rs. 5 per day overdue

    private FineCalculator() { }

    public static double calculate(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate == null || !returnDate.isAfter(dueDate)) {
            return 0.0;
        }
        long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
        return overdueDays * FINE_PER_DAY;
    }
}
