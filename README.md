# Most Active Cookie Finder (MACF) 🍪🚀

This command-line tool processes a cookie log file to identify the most active cookie(s) for a specific day.

## 📄 Table of Contents

* [🎯 Overview](#-overview)
* [✨ Key Features](#-key-features)
* [🔧 Technologies Used](#-technologies-used)
* [🏛 Architecture Overview](#-architecture-overview)
* [🚀 Running the Application](#-running-the-application)

    * [CLI Usage](#cli-usage)
    * [Docker](#docker)
* [📝 Project Structure](#-project-structure)
* [👷‍♂️ Design Patterns & SOLID Principles](#-design-patterns--solid-principles)
* [🧪 Testing & Edge Cases](#-testing--edge-cases)
* [📬 Contact Information](#-contact-information)

## 🎯 Overview

**Most Active Cookie Finder (MACF)** is a command-line Java application that parses large cookie log CSV files and identifies the most active cookies for a given date or date range. Leveraging an event-driven observer pattern and modular design, MACF provides clear progress reporting, robust error handling, and an extremely high-performance heap-based algorithm for fast top‑N frequency analysis.

## ✨ Key Features

* **High-Performance Heap Algorithm ⚡**
  Efficiently retrieves the top‑N most frequent cookies using a max‑heap (priority queue) for O(M + N log M) complexity.
* **High-Performance Parsing**
  Streams through CSV, skipping malformed lines and reporting parse progress.
* **Date Range Filtering**
  Supports single dates or date ranges (e.g., `2025-05-16` or `2025-05-10:2025-05-12`).
* **Top-N Analysis**
  Returns all cookies sharing the Nth highest frequency, grouping ties correctly.
* **Observer-Based Logging**
  Console and SLF4J logger listeners report info, warnings, and errors throughout parsing and analysis.
* **Modular Analyzer**
  Swap between default (sorting) and heap-based (priority queue) analyzers as needed.
* **Dockerized Deployment**
  Build and run as a lightweight container.

## 🔧 Technologies Used

* **Language:** Java 17
* **Build & Dependency Management:** Maven 3.9.6
* **Logging:** SLF4J + Logback
* **Testing:** JUnit 5, Mockito
* **Containerization:** Docker, Alpine Linux

## 🏛 Architecture Overview

```plaintext
App.java
├─ com.quantcast.cli       # Command-line parsing (Apache Commons CLI)
├─ com.quantcast.parser    # CSV parsing with Observer notifications
├─ com.quantcast.analyzer  # CookieAnalyzer interface + implementations
├─ com.quantcast.observer  # Observer and listeners (Console, Logger)
└─ com.quantcast.domain    # Application entrypoint & orchestration
```

* **CLI Layer** parses input flags (`-f`, `-d`, `-t`) into a `Config`.
* **Parser Layer** streams the CSV, counting cookies within date bounds while reporting malformed lines and progress.
* **Analyzer Layer** computes the top-N most active cookies via a high-performance heap algorithm or default sort-based approach.
* **Observer Layer** dispatches events to any registered `EventListener` implementations.
* **Domain Layer** wires components and drives application logic.

## 🚀 Running the Application

### CLI Usage

1. **Verify JRE Installation**

   Ensure you have Java 21+ installed. You can check your Java version with:

   ```bash
   java -version
   ```
   
2. **Build the JAR**

   ```bash
   mvn clean package
   ```

   Produces `target/most-active-cookie.jar`, if not already built.

3. **Run**

   ```bash
   java -jar target/most-active-cookie.jar \
     -f /path/to/cookie_log.csv \
     -d 2025-05-16[:2025-05-18] \
     -t 3
   ```

    * `-f, --file`    Path to CSV file (required)
    * `-d, --date`    Single date or range `YYYY-MM-DD[:YYYY-MM-DD]` (required)
    * `-t, --top`    Top N ranks to display (default: 1)

Example:

```bash
java -jar app.jar -f cookies.csv -d 2018-12-9
```

### Docker

1. **Build the Docker image**

   Ensure you have Docker installed and running. You can check your Docker version with:

   ```bash
   docker -v
   ```

2. **Build the image**

   ```bash
   docker build -t macf:latest .
   ```
3. **Run the container**

   ```bash
   docker run -v /cookies.csv:/data/cookies.csv macf:latest -f /data/cookies.csv -d 2018-12-9
   ```

## 📝 Project Structure

```
src/
├── main/
│   ├── java/com/quantcast/
│   │   ├── App.java
│   │   ├── cli/             # Cli.java, Config record
│   │   ├── parser/          # CookieLogParser, FileCookieLogParser
│   │   ├── analyzer/        # CookieAnalyzer, HeapCookieAnalyzer, DefaultCookieAnalyzer
│   │   ├── observer/        # EventObserver, EventListener, observers & listeners
│   │   └── domain/          # MostActiveCookie
└── test/
    └── java/com/quantcast/  # JUnit & Mockito test classes covering normal flows, edge cases, and error conditions
Dockerfile
pom.xml
README.md
```

## 👷‍♂️ Design Patterns & SOLID Principles

* **Observer Pattern:** `CookieAppObserver` publishes parsing/analysis events to listeners.
* **Strategy Pattern:** Multiple `CookieAnalyzer` implementations (heap vs. sort).
* **Dependency Injection:** Components wired via constructors.
* **SRP:** Classes have single responsibility (parsing vs. analyzing vs. CLI).
* **OCP:** Easily extend new analyzers or observers without modifying existing code.
* **LSP & ISP:** Interfaces define only needed methods; implementations follow contracts.
* **DIP:** High-level modules depend on abstractions (`CookieLogParser`, `CookieAnalyzer`).

## 🧪 Testing & Edge Cases

* **Coverage:** Comprehensive JUnit test suite covering:

    * CLI argument validation and error codes.
    * Parsing malformed lines, out-of-range and boundary timestamps.
    * Default vs. heap analyzers, including zero/negative `topN` and tie-handling.
    * Observer notifications via Mockito verifications.
* **Edge Cases:**

    * Empty or header-only files.
    * Files with missing headers or extra commas.
    * Date ranges where start > end (invalid).
    * Negative/topN greater than unique cookies.

## 📬 Contact Information

* **Alaa Ismail**
* **Email:** [alaaismail286@gmail.com](mailto:alaaismail286@gmail.com)
* **GitHub:** [https://github.com/3laaHisham](https://github.com/3laaHisham)
* **LinkedIn:** [https://www.linkedin.com/in/alaahisham](https://www.linkedin.com/in/alaahisham)

Enjoy finding the most active cookies! 🍪✨
