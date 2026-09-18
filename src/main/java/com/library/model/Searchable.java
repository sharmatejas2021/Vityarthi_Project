package com.library.model;

/**
 * Implemented by entities that can be matched against a search keyword.
 */
public interface Searchable {
    boolean matches(String keyword);
}
