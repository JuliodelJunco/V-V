# V-V
# 🧩 FileSort – Command-Line File Utility

## 📦 Overview

`FileSort` is a simple Java command-line utility that allows users to inspect, sort, and manage files using commands like `size`, `type`, `delete`, and several sorting modes (alphabetical, created date, modified date, etc.).

---

## ⚙️ Project Structure

```
FileSort/
├── src/
│   └── FileSort.java
├── out/
│   └── (compiled .class files will be generated here)
├── bin/
│   └── deleted/   # deleted files are moved here
├── sample_commands.txt
└── README.md
```

---

## 🧰 Requirements

* Java 17 or later
* macOS, Linux, or Windows command line

---

## 🚀 How to Run

### 1️⃣ Compile

Compile the Java source file and place the `.class` files in the `out` directory:

```bash
javac -d out src/FileSort.java
```

### 2️⃣ Execute

Run the compiled program by specifying the classpath and the input command file:

```bash
java -cp out FileSort sample_commands.txt
```

---

## 📄 Sample Output

```
Available commands:
help
    Prints this help message.
size <file>
    Shows the file size in kB/MB/GB/TB depending on magnitude.
type <file>
    Shows the detected file type (.txt, .json, .csv).
delete <file>
    Moves the file into bin/deleted inside the project directory.
alphabetical <file1> <file2> ... <file10>
    Sorts 2-10 files alphabetically by name.
reverse_alphabetical <file1> <file2> ... <file10>
    Sorts 2-10 files in reverse alphabetical order.
created <file1> <file2> ... <file10>
    Sorts 2-10 files by creation date (oldest first).
reverse_created <file1> <file2> ... <file10>
    Sorts 2-10 files by creation date (newest first).
modified <file1> <file2> ... <file10>
    Sorts 2-10 files by last modification date (newest first).
reverse_modified <file1> <file2> ... <file10>
    Sorts 2-10 files by last modification date (oldest first).

report.txt size: 0.04 kB
config.json type: JSON (.json)
Alphabetical order: config.json, data.csv, report.txt
Reverse alphabetical order: report.txt, data.csv, config.json
Created (oldest to newest): report.txt, config.json, data.csv
Created (newest to oldest): data.csv, config.json, report.txt
Modified (newest to oldest): data.csv, config.json, report.txt
Modified (oldest to newest): report.txt, config.json, data.csv
File not found: archive.txt
```

---

## 🔁 Updating the Code

After modifying `FileSort.java`, recompile it before running again:

```bash
javac -d out src/FileSort.java
```

Then rerun the command:

```bash
java -cp out FileSort sample_commands.txt
```

---

## 🧹 Notes

* Deleted files are **not permanently removed**, but moved to `bin/deleted/`.
* Ensure `sample_commands.txt` contains one command per line (e.g., `help`, `size report.txt`, etc.).
* If you have multiple Java files, you can compile them all at once:

  ```bash
  javac -d out src/*.java
  ```

---

