# MediTrack Setup Instructions

This guide explains how to configure Java, compile MediTrack, run the console application, and execute the manual test runners.

## Prerequisites

- JDK 17 or higher
- Terminal or command prompt
- Git, if cloning the repository

The application uses only the Java standard library. No external dependencies are required.

## Verify Java Installation

Run:

```bash
java -version
javac -version
```

Both commands should print an installed Java version. `javac` is required because the project is compiled from source.

## Project Setup

From the repository root:

```bash
pwd
```

Expected project root:

```text
meditrack
```

The main source folder is:

```text
src/
```

The main entry point is:

```text
src/Main.java
```

## Compile

Compile all Java files into a temporary build directory:

```bash
javac -d /tmp/meditrack-build $(find src -name '*.java')
```

If compilation succeeds, the command prints no output.

## Run The Application

Run the console application:

```bash
java -cp /tmp/meditrack-build Main
```

You should see the main menu:

```text
========== MAIN MENU ==========
1. Patient Management
2. Doctor Management
3. Appointment Management
4. Billing
5. Search
6. Demonstrate Features
0. Exit
```

## Run With Persisted Data

MediTrack can load saved CSV data at startup:

```bash
java -cp /tmp/meditrack-build Main --loadData
```

CSV files are stored under:

```text
data/patients.csv
data/doctors.csv
data/appointments.csv
```

If the files do not exist yet, the application starts normally with empty data.

## Run Tests

Run the manual feature test runner:

```bash
java -cp /tmp/meditrack-build com.airtribe.meditrack.test.TestRunner
```

Run the automated menu-flow test:

```bash
java -cp /tmp/meditrack-build com.airtribe.meditrack.test.MainMenuAutomationTest
```

The menu-flow test simulates user input for CRUD, appointment, billing, and search flows.

## Screenshots

Setup and project structure screenshots are stored in:

```text
docs/screenshots/new-project-configuration.png
docs/screenshots/package-structure.png
docs/screenshots/project-structure.png
```

These screenshots support the environment setup and package-structure requirements from the project brief.

## Notes

- Run all commands from the repository root.
- Billing data is runtime-only and is not persisted to CSV.
- The CSV implementation is intentionally simple and expects fields without commas.
