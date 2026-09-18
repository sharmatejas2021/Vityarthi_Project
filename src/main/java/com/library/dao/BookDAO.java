package com.library.dao;

import com.library.db.DatabaseConnection;
import com.library.exception.RecordNotFoundException;
import com.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all DB access for Book records.
 */
public class BookDAO {
    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    public Book addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, isbn, category, total_copies, available_copies) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setString(4, book.getCategory());
            ps.setInt(5, book.getTotalCopies());
            ps.setInt(6, book.getAvailableCopies());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    book.setId(keys.getInt(1));
                }
            }
        }
        return book;
    }

    public Book getById(int id) throws SQLException, RecordNotFoundException {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        throw new RecordNotFoundException("No book found with id " + id);
    }

    public List<Book> getAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY title";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapRow(rs));
            }
        }
        return books;
    }

    public List<Book> search(String keyword) throws SQLException {
        List<Book> results = new ArrayList<>();
        for (Book b : getAll()) {
            if (b.matches(keyword)) {
                results.add(b);
            }
        }
        return results;
    }

    public void update(Book book) throws SQLException {
        String sql = "UPDATE books SET title=?, author=?, isbn=?, category=?, total_copies=?, available_copies=? " +
                "WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setString(4, book.getCategory());
            ps.setInt(5, book.getTotalCopies());
            ps.setInt(6, book.getAvailableCopies());
            ps.setInt(7, book.getId());
            ps.executeUpdate();
        }
    }

    public void updateAvailableCopies(int bookId, int delta) throws SQLException {
        String sql = "UPDATE books SET available_copies = available_copies + ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, delta);
            ps.setInt(2, bookId);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("isbn"),
                rs.getString("category"),
                rs.getInt("total_copies"),
                rs.getInt("available_copies")
        );
    }
}
