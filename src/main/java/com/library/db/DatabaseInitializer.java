package com.library.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Creates the DB tables on first launch if they don't already exist.
 */
public class DatabaseInitializer {

    public static void initialize() {
        String createBooks = "CREATE TABLE IF NOT EXISTS books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "author TEXT NOT NULL," +
                "isbn TEXT NOT NULL UNIQUE," +
                "category TEXT," +
                "total_copies INTEGER NOT NULL," +
                "available_copies INTEGER NOT NULL" +
                ");";

        String createMembers = "CREATE TABLE IF NOT EXISTS members (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "email TEXT NOT NULL UNIQUE," +
                "phone TEXT NOT NULL," +
                "member_type TEXT NOT NULL CHECK(member_type IN ('STUDENT','FACULTY'))" +
                ");";

        String createTransactions = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "book_id INTEGER NOT NULL," +
                "member_id INTEGER NOT NULL," +
                "issue_date TEXT NOT NULL," +
                "due_date TEXT NOT NULL," +
                "return_date TEXT," +
                "fine_amount REAL DEFAULT 0," +
                "status TEXT NOT NULL CHECK(status IN ('ISSUED','RETURNED','OVERDUE'))," +
                "FOREIGN KEY (book_id) REFERENCES books(id)," +
                "FOREIGN KEY (member_id) REFERENCES members(id)" +
                ");";

        // speed up common lookups
        String idxBookIsbn = "CREATE INDEX IF NOT EXISTS idx_books_isbn ON books(isbn);";
        String idxTxnMember = "CREATE INDEX IF NOT EXISTS idx_txn_member ON transactions(member_id);";
        String idxTxnBook = "CREATE INDEX IF NOT EXISTS idx_txn_book ON transactions(book_id);";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createBooks);
            stmt.execute(createMembers);
            stmt.execute(createTransactions);
            stmt.execute(idxBookIsbn);
            stmt.execute(idxTxnMember);
            stmt.execute(idxTxnBook);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to initialize database schema: " + e.getMessage(), e);
        }
    }
}
