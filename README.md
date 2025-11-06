# 🗂️ FileSort – File Sorting Utility

A Java-based command-line tool that processes a list of file commands from an input file.
It can display file information, delete files, and sort multiple files based on different criteria.

---

## 📘 Overview

**FileSort** reads a text file that contains one command per line.
Each command starts with a keyword (e.g., `size`, `type`, `alphabetical`) followed by one or more arguments (file names).
The program executes each command sequentially and prints the result to the command line.

---

## 👥 Team Roles & Implementation Plan

| **Member**                            | **Main Role**                                                           | **Main Modules**            | **Key Responsibilities**                                                                                                                                            | **Implementation Steps**                                                                                                                                                                                                                                                                 |
| ------------------------------------- | ----------------------------------------------------------------------- | --------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **A – Core Logic & Command Parser**   | Controls the overall program flow and command routing.                  | `FileSort`, `CommandParser` | - Reads input file and processes commands.<br>- Parses commands and dispatches them to the correct service.<br>- Handles error messages and integrates all modules. | 1. Create the main class `FileSort` with `main()`.<br>2. Implement `CommandParser` to split command and arguments.<br>3. Map commands to `FileService` or `SortService`.<br>4. Handle user errors according to the specification.<br>5. Test full end-to-end command execution.          |
| **B – File Operations Specialist**    | Manages single-file operations (retrieving info, deletion, validation). | `FileService`               | - Implements `size`, `type`, and `delete` functions.<br>- Validates file names and existence.<br>- Handles file I/O and exceptions.                                 | 1. Implement the `FileService` class.<br>2. Add methods `getSize()`, `getType()`, and `delete()`.<br>3. Format size (kB → MB → GB → TB).<br>4. Move deleted files to `bin/deleted/` folder.<br>5. Implement file validation and exception handling.                                      |
| **C – Sorting & Metadata Specialist** | Handles multi-file sorting and metadata operations.                     | `SortService`               | - Implements alphabetical and date-based sorting.<br>- Uses file metadata for sorting.<br>- Validates number of arguments (2–10).                                   | 1. Implement the `SortService` class.<br>2. Add methods for `alphabetical`, `created`, and `modified` (and their reverse versions).<br>3. Use `Files.readAttributes()` to access timestamps.<br>4. Implement ascending/descending sorting logic.<br>5. Format and display sorted output. |

---

## ⚙️ Development Conventions

### 🧱 Code Structure

* Each member works mainly within their assigned module (`FileSort`, `FileService`, `SortService`).
* Do not modify another member’s module without prior discussion.
* Shared constants (error messages, directory paths, etc.) should be defined in `Constants.java`.
* Utility functions (e.g., validation) should be placed in `utils/ValidationUtil.java`.

### ✍️ Naming Conventions

| Element                 | Convention         | Example                            |
| ----------------------- | ------------------ | ---------------------------------- |
| **Classes**             | `PascalCase`       | `FileSort`, `CommandParser`        |
| **Methods & Variables** | `camelCase`        | `getFileSize()`, `sortByCreated()` |
| **Constants**           | `UPPER_SNAKE_CASE` | `MAX_ARGUMENTS`, `ERROR_NO_FILE`   |
| **Packages**            | lowercase          | `com.team.filesort`                |

### 🧩 Collaboration Rules

* Commit frequently with clear, descriptive messages (e.g., `Implement alphabetical sorting in SortService`).
* Use consistent indentation (4 spaces, no tabs).
* Write brief Javadoc comments for public methods.
* Test your module independently before integration.
* Review teammates’ pull requests before merging.

---

## 🧱 Project Structure

```plaintext
FileSort/
├── src/
│   ├── com/team/filesort/
│   │   ├── FileSort.java
│   │   ├── CommandParser.java
│   │   ├── FileService.java
│   │   ├── SortService.java
│   │   ├── Constants.java
│   │   └── utils/
│   │       └── ValidationUtil.java
├── input/
│   └── commands.txt
├── bin/
│   └── deleted/
└── README.md
```

---

## ✅ Integration Checklist

Before submitting, make sure that:

* All commands (`help`, `size`, `type`, `delete`, `alphabetical`, `reverse_alphabetical`, `created`, `reverse_created`, `modified`, `reverse_modified`) execute correctly.
* Error messages exactly match the specification.
* Input file format (case-sensitive, no spaces in file names) is respected.
* Each module passes its individual tests.
* The final program runs smoothly from the command line.

---

## 🧪 Testing Strategy (Optional but Recommended)

| Module            | Test Focus                                             | Example Test                                                              |
| ----------------- | ------------------------------------------------------ | ------------------------------------------------------------------------- |
| **FileService**   | Size calculation, file type detection, deletion logic. | Verify that a `.txt` file of 1200 kB is displayed as `1.2 MB`.            |
| **SortService**   | Alphabetical and timestamp sorting.                    | Confirm that files are sorted correctly in both normal and reverse order. |
| **CommandParser** | Command parsing and error handling.                    | Check that invalid commands trigger the correct error messages.           |

---

## 🏁 Notes

This README and development plan strictly follow the **FileSort Specification** provided in the V&V document.
All functional requirements (commands, constraints, and error handling) are implemented exactly as described,
with additional conventions added for clean and maintainable team development.

---
