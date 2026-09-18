package com.library.model;

import java.time.LocalDate;

/**
 * Represents a single book issue/return transaction linking a
 * Book and a Member, including due date and fine tracking.
 */
public class Transaction {
    private int id;
    private int bookId;
    private int memberId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate; // null while the book is still out
    private double fineAmount;
    private TransactionStatus status;

    public Transaction(int bookId, int memberId, LocalDate issueDate, LocalDate dueDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.fineAmount = 0.0;
        this.status = TransactionStatus.ISSUED;
    }

    public Transaction(int id, int bookId, int memberId, LocalDate issueDate, LocalDate dueDate,
                        LocalDate returnDate, double fineAmount, TransactionStatus status) {
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookId() { return bookId; }
    public int getMemberId() { return memberId; }

    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public double getFineAmount() { return fineAmount; }
    public void setFineAmount(double fineAmount) { this.fineAmount = fineAmount; }

    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }

    public boolean isOverdue(LocalDate today) {
        return status == TransactionStatus.ISSUED && today.isAfter(dueDate);
    }

    @Override
    public String toString() {
        String returnStr = returnDate == null ? "Not returned" : returnDate.toString();
        return String.format("Txn[%d] Book:%d Member:%d Issued:%s Due:%s Returned:%s Fine:Rs.%.2f Status:%s",
                id, bookId, memberId, issueDate, dueDate, returnStr, fineAmount, status);
    }
}
