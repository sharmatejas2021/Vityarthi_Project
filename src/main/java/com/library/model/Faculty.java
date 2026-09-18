package com.library.model;

/**
 * Faculty member — max 5 books, 30-day loan period.
 */
public class Faculty extends Member {

    public Faculty(String name, String email, String phone) {
        super(name, email, phone);
    }

    public Faculty(int id, String name, String email, String phone) {
        super(id, name, email, phone);
    }

    @Override
    public MemberType getMemberType() {
        return MemberType.FACULTY;
    }
}
