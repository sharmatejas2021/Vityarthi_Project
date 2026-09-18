package com.library.model;

import java.util.Objects;

/**
 * Base class for a library member. Student and Faculty extend this.
 */
public abstract class Member implements Searchable {
    protected int id;
    protected String name;
    protected String email;
    protected String phone;

    protected Member(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    protected Member(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public abstract MemberType getMemberType();

    public int getMaxBooksAllowed() {
        return getMemberType().getMaxBooksAllowed();
    }

    public int getLoanPeriodDays() {
        return getMemberType().getLoanPeriodDays();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public boolean matches(String keyword) {
        if (keyword == null) return false;
        String k = keyword.toLowerCase();
        return name.toLowerCase().contains(k)
                || email.toLowerCase().contains(k)
                || phone.toLowerCase().contains(k);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member member = (Member) o;
        return id == member.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("[%d] %-20s | %-25s | %-12s | Type: %-8s | Max books: %d",
                id, name, email, phone, getMemberType(), getMaxBooksAllowed());
    }
}
