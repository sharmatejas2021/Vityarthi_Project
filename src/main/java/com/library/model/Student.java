package com.library.model;

/**
 * Student member — max 3 books, 15-day loan period.
 */
public class Student extends Member {

    public Student(String name, String email, String phone) {
        super(name, email, phone);
    }

    public Student(int id, String name, String email, String phone) {
        super(id, name, email, phone);
    }

    @Override
    public MemberType getMemberType() {
        return MemberType.STUDENT;
    }
}
