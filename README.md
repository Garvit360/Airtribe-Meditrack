# Airtribe MediTrack

MediTrack is a Java console application for managing patients, doctors, appointments, billing, searching, and CSV-based persistence.

## Quick Start

Compile:

```bash
javac -d /tmp/meditrack-build $(find src -name '*.java')
```

Run the app:

```bash
java -cp /tmp/meditrack-build Main
```

Run the app and load persisted CSV data:

```bash
java -cp /tmp/meditrack-build Main --loadData
```

## Tests

Run the manual feature tests:

```bash
java -cp /tmp/meditrack-build com.airtribe.meditrack.test.TestRunner
```

Run the automated console menu flow:

```bash
java -cp /tmp/meditrack-build com.airtribe.meditrack.test.MainMenuAutomationTest
```

`MainMenuAutomationTest` simulates typed menu input for CRUD, appointment, billing, and search flows.

## Implemented Features

- Patient and doctor CRUD.
- Appointment create, view, confirm, cancel, and list by patient.
- Billing with consultation, medication, lab charges, tax, summary, and payment status.
- Patient and doctor search by explicit criteria and `Searchable` keyword matching.
- `MedicalEntity` abstraction above `Person`.
- Deep clone demonstration for `Patient`; safe clone for `Appointment`.
- Immutable `BillSummary`.
- CSV persistence for patients, doctors, and appointments.
- `--loadData` startup loading.
- Singleton `IdGenerator`.
- `BillFactory` for bill creation.
- Observer notifications for appointment lifecycle events.

## Persistence

Runtime CSV files are stored under:

```text
data/patients.csv
data/doctors.csv
data/appointments.csv
```

The CSV implementation intentionally uses `String.split(",")` as required by the assignment, so user-entered fields should not contain commas.

## Project Structure

```text
src/
  Main.java
  com/airtribe/meditrack/
    MediTrackApplication.java
    constants/
    entity/
    exception/
    interfaces/
    menu/
    observer/
    service/
    test/
    util/
docs/
  project.md
  JVM_Report.md
  Setup_Instructions.md
  class-uml-diagram.md
  system-design-diagram.md
```
