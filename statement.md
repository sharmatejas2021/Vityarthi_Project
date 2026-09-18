## 1. Problem Statement
Libraries — whether in a college, school, or community setting — still commonly rely on manual registers or spreadsheets to track which books are available, who has borrowed what, and when items are due back. This leads to lost records, no consistent way to calculate overdue fines, and no quick way to check a book's availability before a member walks up to the counter. This project provides a lightweight, reliable system that a librarian can run on a single machine to manage the catalog, the member base, and the borrowing process end-to-end.

## 2. Project Scope
- Maintain a catalog of books (title, author, ISBN, category, copy counts)
- Maintain a member registry, distinguishing Student and Faculty members with different borrowing privileges
- Issue and return books, with automatic enforcement of availability and borrowing-limit rules
- Automatically calculate overdue fines on return
- Persist all data locally via SQLite so it survives across application restarts

Out of scope for this version: multi-branch library support, a graphical or web front-end, and online/remote access — the system is designed as a single-machine, single-librarian console tool.

## 3. Target Users

- ### Librarians / Library Staff
  The primary users, who use the console menus to manage the catalog, register members, and process issue/return transactions.

- ### Academic Institutions
  Colleges or departments running a small-to-medium library that doesn't need a full multi-user networked system.

## 4. High-Level Features

- **Book Management** — add, view, search, and remove books; track total and available copies.
- **Member Management** — add, view, search, and remove members; members are typed as Student (3-book limit, 15-day loan) or Faculty (5-book limit, 30-day loan).
- **Issue / Return & Fine Calculation** — issue a book (blocked automatically if unavailable or the member is over their limit), return a book with an automatically computed overdue fine, and view active loans, full transaction history, and currently overdue books.
