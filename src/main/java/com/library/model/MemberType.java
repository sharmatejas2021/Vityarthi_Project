package com.library.model;

/**
 * Member type, with the borrowing rules attached to each type.
 */
public enum MemberType {
    STUDENT(3, 15),   // max 3 books, 15 days loan period
    FACULTY(5, 30);   // max 5 books, 30 days loan period

    private final int maxBooksAllowed;
    private final int loanPeriodDays;

    MemberType(int maxBooksAllowed, int loanPeriodDays) {
        this.maxBooksAllowed = maxBooksAllowed;
        this.loanPeriodDays = loanPeriodDays;
    }

    public int getMaxBooksAllowed() {
        return maxBooksAllowed;
    }

    public int getLoanPeriodDays() {
        return loanPeriodDays;
    }
}
