package com.library.main;

import com.library.model.Book;
import com.library.model.Member;
import com.library.model.MemberType;
import com.library.model.Transaction;
import com.library.service.LibraryService;
import com.library.util.AppLogger;

import java.util.List;
import java.util.Scanner;

/**
 * Console menus for the app. Delegates all business logic to LibraryService.
 */
public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);
    private final LibraryService service = new LibraryService();

    public void start() {
        System.out.println("=========================================");
        System.out.println("   LIBRARY MANAGEMENT SYSTEM");
        System.out.println("=========================================");

        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1": bookMenu(); break;
                    case "2": memberMenu(); break;
                    case "3": issueReturnMenu(); break;
                    case "0": running = false; break;
                    default: System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                AppLogger.error(e.getMessage());
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Goodbye!");
    }

    private void printMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Book Management");
        System.out.println("2. Member Management");
        System.out.println("3. Issue / Return Books");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    // ---------------- Book Management ----------------

    private void bookMenu() throws Exception {
        System.out.println("\n--- BOOK MANAGEMENT ---");
        System.out.println("1. Add Book");
        System.out.println("2. View All Books");
        System.out.println("3. Search Books");
        System.out.println("4. Remove Book");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1": addBook(); break;
            case "2": printBooks(service.listBooks()); break;
            case "3":
                System.out.print("Search keyword: ");
                printBooks(service.searchBooks(scanner.nextLine().trim()));
                break;
            case "4":
                System.out.print("Book ID to remove: ");
                service.removeBook(Integer.parseInt(scanner.nextLine().trim()));
                System.out.println("Book removed.");
                break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void addBook() throws Exception {
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Author: ");
        String author = scanner.nextLine().trim();
        System.out.print("ISBN (10-17 digits): ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Category: ");
        String category = scanner.nextLine().trim();
        System.out.print("Total copies: ");
        int copies = Integer.parseInt(scanner.nextLine().trim());

        Book book = service.addBook(title, author, isbn, category, copies);
        System.out.println("Added: " + book);
    }

    private void printBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        books.forEach(System.out::println);
    }

    // ---------------- Member Management ----------------

    private void memberMenu() throws Exception {
        System.out.println("\n--- MEMBER MANAGEMENT ---");
        System.out.println("1. Add Member");
        System.out.println("2. View All Members");
        System.out.println("3. Search Members");
        System.out.println("4. Remove Member");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1": addMember(); break;
            case "2": printMembers(service.listMembers()); break;
            case "3":
                System.out.print("Search keyword: ");
                printMembers(service.searchMembers(scanner.nextLine().trim()));
                break;
            case "4":
                System.out.print("Member ID to remove: ");
                service.removeMember(Integer.parseInt(scanner.nextLine().trim()));
                System.out.println("Member removed.");
                break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void addMember() throws Exception {
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Phone (10 digits): ");
        String phone = scanner.nextLine().trim();
        System.out.print("Type (1=Student, 2=Faculty): ");
        String typeChoice = scanner.nextLine().trim();
        MemberType type = typeChoice.equals("2") ? MemberType.FACULTY : MemberType.STUDENT;

        Member member = service.addMember(name, email, phone, type);
        System.out.println("Added: " + member);
    }

    private void printMembers(List<Member> members) {
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }
        members.forEach(System.out::println);
    }

    // ---------------- Issue / Return ----------------

    private void issueReturnMenu() throws Exception {
        System.out.println("\n--- ISSUE / RETURN ---");
        System.out.println("1. Issue Book");
        System.out.println("2. Return Book");
        System.out.println("3. View Active Loans for a Member");
        System.out.println("4. View All Transactions");
        System.out.println("5. View Overdue Books");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1": issueBook(); break;
            case "2": returnBook(); break;
            case "3":
                System.out.print("Member ID: ");
                printTransactions(service.viewActiveLoans(Integer.parseInt(scanner.nextLine().trim())));
                break;
            case "4": printTransactions(service.viewAllTransactions()); break;
            case "5": printTransactions(service.viewOverdue()); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void issueBook() throws Exception {
        System.out.print("Book ID: ");
        int bookId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Member ID: ");
        int memberId = Integer.parseInt(scanner.nextLine().trim());

        Transaction txn = service.issueBook(bookId, memberId);
        System.out.println("Issued. Due date: " + txn.getDueDate());
    }

    private void returnBook() throws Exception {
        System.out.print("Transaction ID: ");
        int txnId = Integer.parseInt(scanner.nextLine().trim());

        Transaction txn = service.returnBook(txnId);
        if (txn.getFineAmount() > 0) {
            System.out.printf("Returned. Overdue fine: Rs.%.2f%n", txn.getFineAmount());
        } else {
            System.out.println("Returned on time. No fine.");
        }
    }

    private void printTransactions(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        transactions.forEach(System.out::println);
    }
}
