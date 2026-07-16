# MediTrack System Design

## Scope

MediTrack is a single-process Java console application. It uses in-memory stores while the app is running and CSV files for durable patient, doctor, and appointment persistence.

The app does not currently include a database, network API, authentication, or external integrations.

## System Context

```mermaid
flowchart LR
    User["Console User"]
    App["MediTrack Java CLI"]
    Csv["CSV files\npatients.csv\ndoctors.csv\nappointments.csv"]

    User -->|"Keyboard input"| App
    App -->|"Console output"| User
    App <-->|"load/save"| Csv
```

## Component View

```mermaid
flowchart TB
    Main["Main"]
    Application["MediTrackApplication"]

    subgraph Menu["menu"]
        MainMenu["MainMenu"]
        PatientMenu["PatientMenu"]
        DoctorMenu["DoctorMenu"]
        AppointmentMenu["AppointmentMenu"]
        BillingMenu["BillingMenu"]
        SearchMenu["SearchMenu"]
        FeatureMenu["FeatureDemonstrationMenu"]
        ConsoleInput["ConsoleInput"]
    end

    subgraph Service["service"]
        PatientService["PatientService"]
        DoctorService["DoctorService"]
        AppointmentService["AppointmentService"]
        BillingService["BillingService"]
    end

    subgraph Domain["entity"]
        MedicalEntity["MedicalEntity"]
        Person["Person"]
        Patient["Patient"]
        Doctor["Doctor"]
        Appointment["Appointment"]
        Bill["Bill"]
        BillSummary["BillSummary"]
        Enums["Specialization\nAppointmentStatus\nBillStatus"]
    end

    subgraph Utility["util"]
        DataStore["DataStore<T>"]
        Validator["Validator"]
        CSVUtil["CSVUtil"]
        IdGenerator["IdGenerator singleton"]
        BillFactory["BillFactory"]
        DateUtil["DateUtil"]
    end

    subgraph Observer["observer"]
        AppointmentObserver["AppointmentObserver"]
        ConsoleNotifier["ConsoleAppointmentNotifier"]
    end

    Main --> Application
    Application --> MainMenu
    Application --> ConsoleNotifier
    MainMenu --> Menu
    Menu --> Service
    PatientService --> DataStore
    DoctorService --> DataStore
    AppointmentService --> DataStore
    BillingService --> DataStore
    PatientService --> CSVUtil
    DoctorService --> CSVUtil
    AppointmentService --> CSVUtil
    BillingService --> BillFactory
    AppointmentService --> AppointmentObserver
    ConsoleNotifier -.implements.-> AppointmentObserver
    Service --> Domain
    Domain --> IdGenerator
```

## Main Data Flows

### Startup With `--loadData`

```mermaid
sequenceDiagram
    participant Main
    participant App as MediTrackApplication
    participant Patients as PatientService
    participant Doctors as DoctorService
    participant Appointments as AppointmentService
    participant CSV as CSVUtil

    Main->>App: main(args)
    App->>App: detect --loadData
    App->>Patients: loadPatientsFromCsv()
    Patients->>CSV: loadPatients(data/patients.csv)
    App->>Doctors: loadDoctorsFromCsv()
    Doctors->>CSV: loadDoctors(data/doctors.csv)
    App->>Appointments: loadAppointmentsFromCsv()
    Appointments->>CSV: loadAppointments(data/appointments.csv)
```

Patients load before doctors and appointments. Appointments validate that referenced patient and doctor IDs exist.

### Patient / Doctor Save

```mermaid
sequenceDiagram
    actor User
    participant Menu as PatientMenu / DoctorMenu
    participant Service as PatientService / DoctorService
    participant Store as DataStore<T>
    participant CSV as CSVUtil

    User->>Menu: create/update/delete
    Menu->>Service: service operation
    Service->>Store: update in-memory store
    Service->>CSV: save current records
    Service-->>Menu: success
```

### Appointment Observer Flow

```mermaid
sequenceDiagram
    participant App as MediTrackApplication
    participant Service as AppointmentService
    participant Observer as ConsoleAppointmentNotifier

    App->>Service: addObserver(observer)
    Service->>Service: create/confirm/cancel appointment
    Service->>Observer: onAppointmentCreated / Confirmed / Cancelled
    Observer-->>Service: print notification
```

### Billing

```mermaid
sequenceDiagram
    actor User
    participant Menu as BillingMenu
    participant Billing as BillingService
    participant Factory as BillFactory
    participant Store as DataStore<Bill>

    User->>Menu: enter appointment ID
    Menu->>Billing: generateBill(appointment, doctor)
    Billing->>Factory: createConsultationBill(appointment, doctor)
    Factory-->>Billing: Bill
    Billing->>Store: add(billId, bill)
    Billing-->>Menu: generated bill
```

## Architectural Notes

- `MedicalEntity` owns shared entity identity; `Person` extends it, and `Patient` / `Doctor` extend `Person`.
- `IdGenerator` is an eager singleton. It owns counters and syncs counters after loading CSV IDs.
- `CSVUtil` uses try-with-resources and simple comma splitting per assignment requirements.
- `BillFactory` centralizes bill construction for `BillingService`.
- `AppointmentService` is the observer subject for appointment lifecycle events.
- `DataStore<T>` is still the runtime repository; CSV persistence is a simple file-backed durability layer.

## Current Limitations

- CSV parsing is simple and does not support commas inside user-entered fields.
- Billing records are not persisted to CSV.
- The app is single-user and single-process; `IdGenerator` methods are synchronized, but stores are not designed for concurrent writers.
