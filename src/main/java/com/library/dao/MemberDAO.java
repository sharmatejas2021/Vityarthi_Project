package com.library.dao;

import com.library.db.DatabaseConnection;
import com.library.exception.RecordNotFoundException;
import com.library.model.Faculty;
import com.library.model.Member;
import com.library.model.MemberType;
import com.library.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all DB access for Member records. Rebuilds the correct
 * Student/Faculty subclass based on the stored member_type column.
 */
public class MemberDAO {
    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    public Member addMember(Member member) throws SQLException {
        String sql = "INSERT INTO members (name, email, phone, member_type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            ps.setString(4, member.getMemberType().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    member.setId(keys.getInt(1));
                }
            }
        }
        return member;
    }

    public Member getById(int id) throws SQLException, RecordNotFoundException {
        String sql = "SELECT * FROM members WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        throw new RecordNotFoundException("No member found with id " + id);
    }

    public List<Member> getAll() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY name";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                members.add(mapRow(rs));
            }
        }
        return members;
    }

    public List<Member> search(String keyword) throws SQLException {
        List<Member> results = new ArrayList<>();
        for (Member m : getAll()) {
            if (m.matches(keyword)) {
                results.add(m);
            }
        }
        return results;
    }

    public void update(Member member) throws SQLException {
        String sql = "UPDATE members SET name=?, email=?, phone=?, member_type=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            ps.setString(4, member.getMemberType().name());
            ps.setInt(5, member.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM members WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int countActiveLoans(int memberId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM transactions WHERE member_id = ? AND status = 'ISSUED'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private Member mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        MemberType type = MemberType.valueOf(rs.getString("member_type"));

        if (type == MemberType.STUDENT) {
            return new Student(id, name, email, phone);
        } else {
            return new Faculty(id, name, email, phone);
        }
    }
}
