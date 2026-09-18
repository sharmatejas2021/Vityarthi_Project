package com.library.main;

import com.library.db.DatabaseInitializer;

/**
 * Entry point.
 */
public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.initialize();
        new ConsoleUI().start();
    }
}
