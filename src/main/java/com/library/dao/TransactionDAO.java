package com.library.dao;

import com.library.db.DatabaseConnection;
import com.library.exception.RecordNotFoundException;
import com.library.model.Transaction;
import com.library.model.TransactionStatus;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Transaction (issue/return) records.
 */
public class TransactionDAO {
    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    public Transaction create(Transaction txn) throws SQLException {
        String sql = "INSERT INTO transactions (book_id, member_id, issue_date, due_date, status) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, txn.getBookId());
            ps.setInt(2, txn.getMemberId());
            ps.setString(3, txn.getIssueDate().toString());
            ps.setString(4, txn.getDueDate().toString());
            ps.setString(5, txn.getStatus().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    txn.setId(keys.getInt(1));
                }
            }
        }
        return txn;
    }

    public Transaction getById(int id) throws SQLException, RecordNotFoundException {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        throw new RecordNotFoundException("No transaction found with id " + id);
    }

    public void markReturned(int transactionId, LocalDate returnDate, double fineAmount) throws SQLException {
        String sql = "UPDATE transactions SET return_date=?, fine_amount=?, status='RETURNED' WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, returnDate.toString());
            ps.setDouble(2, fineAmount);
            ps.setInt(3, transactionId);
            ps.executeUpdate();
        }
    }

    public List<Transaction> getActiveByMember(int memberId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE member_id = ? AND status = 'ISSUED'";
        List<Transaction> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Transaction> getAll() throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY issue_date DESC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Transaction> getOverdue(LocalDate today) throws SQLException {
        List<Transaction> list = new ArrayList<>();
        for (Transaction t : getAll()) {
            if (t.isOverdue(today)) {
                list.add(t);
            }
        }
        return list;
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        String returnDateStr = rs.getString("return_date");
        return new Transaction(
                rs.getInt("id"),
                rs.getInt("book_id"),
                rs.getInt("member_id"),
                LocalDate.parse(rs.getString("issue_date")),
                LocalDate.parse(rs.getString("due_date")),
                returnDateStr == null ? null : LocalDate.parse(returnDateStr),
                rs.getDouble("fine_amount"),
                TransactionStatus.valueOf(rs.getString("status"))
        );
    }
}
