package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.MemberDAO;
import com.library.dao.TransactionDAO;
import com.library.exception.BookNotAvailableException;
import com.library.exception.MemberLimitExceededException;
import com.library.exception.RecordNotFoundException;
import com.library.model.*;
import com.library.util.AppLogger;
import com.library.util.FineCalculator;
import com.library.util.ValidationUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Business logic layer — availability checks, borrowing limits,
 * fine calculation. DAOs just do persistence, UI just does I/O.
 */
public class LibraryService {
    private final BookDAO bookDAO = new BookDAO();
    private final MemberDAO memberDAO = new MemberDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    // ---------- Book Management ----------

    public Book addBook(String title, String author, String isbn, String category, int totalCopies)
            throws Exception {
        ValidationUtil.requireNonBlank(title, "Title");
        ValidationUtil.requireNonBlank(author, "Author");
        ValidationUtil.validateIsbn(isbn);
        ValidationUtil.validatePositive(totalCopies, "Total copies");

        Book book = new Book(title, author, isbn, category, totalCopies);
        Book saved = bookDAO.addBook(book);
        AppLogger.info("Book added: " + saved);
        return saved;
    }

    public List<Book> listBooks() throws SQLException {
        return bookDAO.getAll();
    }

    public List<Book> searchBooks(String keyword) throws SQLException {
        return bookDAO.search(keyword);
    }

    public void removeBook(int bookId) throws Exception {
        bookDAO.getById(bookId); // ensures it exists, throws RecordNotFoundException otherwise
        bookDAO.delete(bookId);
        AppLogger.info("Book removed: id=" + bookId);
    }

    // ---------- Member Management ----------

    public Member addMember(String name, String email, String phone, MemberType type) throws Exception {
        ValidationUtil.requireNonBlank(name, "Name");
        ValidationUtil.validateEmail(email);
        ValidationUtil.validatePhone(phone);

        Member member = (type == MemberType.STUDENT)
                ? new Student(name, email, phone)
                : new Faculty(name, email, phone);

        Member saved = memberDAO.addMember(member);
        AppLogger.info("Member added: " + saved);
        return saved;
    }

    public List<Member> listMembers() throws SQLException {
        return memberDAO.getAll();
    }

    public List<Member> searchMembers(String keyword) throws SQLException {
        return memberDAO.search(keyword);
    }

    public void removeMember(int memberId) throws Exception {
        memberDAO.getById(memberId);
        memberDAO.delete(memberId);
        AppLogger.info("Member removed: id=" + memberId);
    }

    // ---------- Issue / Return ----------

    /**
     * Issues a book to a member after checking:
     *  1. The book has at least one available copy.
     *  2. The member has not reached their borrowing limit.
     */
    public Transaction issueBook(int bookId, int memberId) throws Exception {
        Book book = bookDAO.getById(bookId);
        Member member = memberDAO.getById(memberId);

        if (!book.isAvailable()) {
            throw new BookNotAvailableException(
                    "'" + book.getTitle() + "' has no available copies right now.");
        }

        int activeLoans = memberDAO.countActiveLoans(memberId);
        if (activeLoans >= member.getMaxBooksAllowed()) {
            throw new MemberLimitExceededException(
                    member.getName() + " has reached their borrowing limit of " +
                            member.getMaxBooksAllowed() + " books.");
        }

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(member.getLoanPeriodDays());

        Transaction txn = new Transaction(bookId, memberId, issueDate, dueDate);
        transactionDAO.create(txn);
        bookDAO.updateAvailableCopies(bookId, -1);

        AppLogger.info(String.format("Book issued: '%s' -> %s (due %s)",
                book.getTitle(), member.getName(), dueDate));
        return txn;
    }

    /**
     * Returns a book, calculates any overdue fine, and restores
     * the book's available copy count.
     */
    public Transaction returnBook(int transactionId) throws Exception {
        Transaction txn = transactionDAO.getById(transactionId);
        if (txn.getStatus() == TransactionStatus.RETURNED) {
            throw new RecordNotFoundException("This transaction has already been closed.");
        }

        LocalDate returnDate = LocalDate.now();
        double fine = FineCalculator.calculate(txn.getDueDate(), returnDate);

        transactionDAO.markReturned(transactionId, returnDate, fine);
        bookDAO.updateAvailableCopies(txn.getBookId(), +1);

        txn.setReturnDate(returnDate);
        txn.setFineAmount(fine);
        txn.setStatus(TransactionStatus.RETURNED);

        AppLogger.info(String.format("Book returned: txn=%d fine=Rs.%.2f", transactionId, fine));
        return txn;
    }

    public List<Transaction> viewActiveLoans(int memberId) throws SQLException {
        return transactionDAO.getActiveByMember(memberId);
    }

    public List<Transaction> viewAllTransactions() throws SQLException {
        return transactionDAO.getAll();
    }

    public List<Transaction> viewOverdue() throws SQLException {
        return transactionDAO.getOverdue(LocalDate.now());
    }
}
