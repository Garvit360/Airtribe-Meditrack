# Airtribe MediTrack

MediTrack is a Java console application for managing patients, doctors, appointments, billing, and basic search workflows. It is built as an OOP learning project and includes simple CSV persistence for core records.

## Quick Start

Compile:

```bash
javac -d /tmp/meditrack-build $(find src -name '*.java')
```

Run:

```bash
java -cp /tmp/meditrack-build Main
```

Run with persisted CSV data:

```bash
java -cp /tmp/meditrack-build Main --loadData
```

## Tests

Run manual feature checks:

```bash
java -cp /tmp/meditrack-build com.airtribe.meditrack.test.TestRunner
```

Run automated menu-flow checks:

```bash
java -cp /tmp/meditrack-build com.airtribe.meditrack.test.MainMenuAutomationTest
```

## Documentation

- [Project brief](docs/project.md)
- [System design](docs/system-design-diagram.md)
- [Class UML](docs/class-uml-diagram.md)
- [JVM report](docs/JVM_Report.md)
- [Setup instructions](docs/Setup_Instructions.md)

## Notes

CSV persistence stores patient, doctor, and appointment data under `data/`. Billing records are currently runtime-only.
