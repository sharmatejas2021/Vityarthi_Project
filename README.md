# VITyarthi Project
Library Management System

---

# Library Management System

A console-based Library Management System built in **Java** with **JDBC** and **SQLite**, developed as the CSE2006 (Programming in Java) course project. It manages a library's book catalog, its members, and the day-to-day process of issuing and returning books, including overdue fine calculation.

---

## 📌 Features

- Add, view, search, and remove **books** (tracks total vs. available copies per title)
- Add, view, search, and remove **members** — `Student` (max 3 books, 15-day loan) and `Faculty` (max 5 books, 30-day loan), modeled via inheritance
- **Issue a book** to a member (blocked automatically if no copies are available or the member is at their borrowing limit)
- **Return a book** with automatic overdue fine calculation (₹5/day late)
- View a member's active loans, the full transaction history, and currently overdue books
- Simple, menu-driven console interface

---

## 🛠 Technology Stack

### Language & Runtime
- Java 11+

### Persistence
- JDBC with the [sqlite-jdbc](https://github.com/xerial/sqlite-jdbc) driver
- SQLite (single-file database, zero server setup)

### Build & Tooling
- Maven (dependency management and build)
- Git (version control)

---

## 🏗 Architecture

Layered, package-per-concern structure:

```
com.library.model      → Book, Member (abstract), Student, Faculty, Transaction, enums
com.library.exception  → Custom checked exceptions (LibraryException and subclasses)
com.library.db         → Singleton DB connection + schema initializer
com.library.dao        → BookDAO, MemberDAO, TransactionDAO (all SQL lives here)
com.library.service    → LibraryService (all business rules live here)
com.library.util       → ValidationUtil, FineCalculator, AppLogger
com.library.main       → Main (entry point), ConsoleUI (menu-driven I/O)
```

The UI doesn't talk to the DB directly, and the DAOs don't enforce business rules — that all goes through `LibraryService`.

---

## ✅ Non-Functional Requirements

- **Performance** — single shared DB connection, indexes on ISBN and transaction foreign keys
- **Security** — all queries use `PreparedStatement`; every input is validated before it hits the DB
- **Reliability** — foreign key constraints, explicit transaction status checks
- **Maintainability** — layered packages (model / dao / service / util / ui), one responsibility per class
- **Error handling** — custom exceptions (`BookNotAvailableException`, `MemberLimitExceededException`, `RecordNotFoundException`, `InvalidInputException`) caught and shown cleanly by the UI
- **Logging** — `AppLogger` writes timestamped events to console and `library.log`

---

## 🚀 How to Run the Project

#### 1. Software Requirements
- **JDK**: Java 11 or later installed
- **Build tool**: Maven 3.6+

#### 2. Install & Build
Open **Command Prompt** (Windows) or **Terminal** (Mac/Linux), navigate to the project folder, then run:

```bash
mvn clean package
```

This downloads the SQLite JDBC driver automatically and packages everything into a single runnable jar.

#### 3. Run the Application

```bash
java -jar target/library-management-system.jar
```

On first run, the app automatically creates `library.db` (SQLite file) in the project directory and sets up all required tables — no manual database setup needed.

#### 4. How to Use the System
- Choose **1** for Book Management → add, view, search, or remove books
- Choose **2** for Member Management → add, view, search, or remove members
- Choose **3** for Issue/Return → issue a book, return a book, view active loans, view all transactions, or view overdue books
- Choose **0** to exit

---

## ⚙️ Instructions for Testing

Manual/functional testing (no external test framework required, since this is a console app):

#### 1. Test Book Management
Add a book, then view/search for it to confirm it was saved correctly.

#### 2. Test Member Management
Add a Student and a Faculty member, then view/search for them to confirm the correct borrowing limits were assigned.

#### 3. Test Issue Book Rules
Issue a book to a member. Try issuing the **same book again after copies run out** — a `BookNotAvailableException` message should appear instead of a crash. Try issuing **more books than a member's limit allows** — a `MemberLimitExceededException` message should appear.

#### 4. Test Return Book & Fine Calculation
Return a book before its due date — the fine should be `0`. Return a book past its due date — the fine should be calculated correctly (₹5 × days late).

#### 5. Test Error Handling
Try invalid inputs at each prompt (bad email, bad phone, bad ISBN, non-numeric IDs, a non-existent book/member ID). Each should produce a clear error message, never a crash.

#### 6. Test Full Workflow
Start the app, add a book and a member, issue the book, view active loans, return the book, and view all transactions — everything should flow smoothly end-to-end.

---

## 📸 Screenshots

**Adding a book**
<img width="1889" height="1386" alt="screenshot_1_add_book" src="https://github.com/user-attachments/assets/7bc80b7b-6254-4a47-8cd9-7856eb5facd0" />

**Issuing a book and viewing updated availability**
<img width="1889" height="1097" alt="screenshot_2_issue_book" src="https://github.com/user-attachments/assets/bb4f8882-5e5d-4a49-8351-8cf9d0a2d93a" />

**Returning a book and input validation**
<img width="1889" height="1097" alt="screenshot_3_return_and_validation" src="https://github.com/user-attachments/assets/62459489-aa86-4c13-9277-41fc9bda575d" />
