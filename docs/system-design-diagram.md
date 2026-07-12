# MediTrack System Design Diagram

## Scope

MediTrack is currently a single-process console application. It has no database, network API, authentication layer, or external integrations. Data is stored in memory through `DataStore<T>`, so all records are lost when the JVM exits.

## System Context

```mermaid
flowchart LR
    User["Console User"]
    App["MediTrack Console Application\nJava CLI"]

    User -->|"Keyboard input"| App
    App -->|"Console output"| User
```

## Container / Component View

```mermaid
flowchart TB
    subgraph Runtime["JVM Process"]
        Main["Main\nDefault entry point"]
        Application["MediTrackApplication\nBootstraps services and menu loop"]

        subgraph MenuLayer["Menu Layer\ncom.airtribe.meditrack.menu"]
            MainMenu["MainMenu"]
            PatientMenu["PatientMenu"]
            DoctorMenu["DoctorMenu"]
            AppointmentMenu["AppointmentMenu"]
            BillingMenu["BillingMenu"]
            SearchMenu["SearchMenu"]
            FeatureMenu["FeatureDemonstrationMenu"]
            ConsoleInput["ConsoleInput"]
        end

        subgraph ServiceLayer["Service Layer\ncom.airtribe.meditrack.service"]
            PatientService["PatientService"]
            DoctorService["DoctorService"]
            AppointmentService["AppointmentService"]
            BillingService["BillingService"]
        end

        subgraph DomainLayer["Domain Layer\ncom.airtribe.meditrack.entity"]
            Person["Person"]
            Patient["Patient"]
            Doctor["Doctor"]
            Appointment["Appointment"]
            Bill["Bill"]
            BillSummary["BillSummary"]
            Enums["AppointmentStatus\nBillStatus\nSpecialization"]
        end

        subgraph UtilityLayer["Utility Layer\ncom.airtribe.meditrack.util"]
            DataStore["DataStore<T>\nIn-memory HashMap + ArrayList"]
            Validator["Validator"]
            IdGenerator["IdGenerator"]
            DateUtil["DateUtil"]
        end
    end

    Main --> Application
    Application --> MainMenu
    MainMenu --> PatientMenu
    MainMenu --> DoctorMenu
    MainMenu --> AppointmentMenu
    MainMenu --> BillingMenu
    MainMenu --> SearchMenu
    MainMenu --> FeatureMenu
    PatientMenu --> ConsoleInput
    DoctorMenu --> ConsoleInput
    AppointmentMenu --> ConsoleInput
    BillingMenu --> ConsoleInput
    SearchMenu --> ConsoleInput

    PatientMenu --> PatientService
    DoctorMenu --> DoctorService
    AppointmentMenu --> AppointmentService
    AppointmentMenu --> PatientService
    AppointmentMenu --> DoctorService
    BillingMenu --> BillingService
    BillingMenu --> AppointmentService
    BillingMenu --> DoctorService
    SearchMenu --> PatientService
    SearchMenu --> DoctorService
    SearchMenu --> AppointmentService

    PatientService --> Validator
    DoctorService --> Validator
    PatientService --> DataStore
    DoctorService --> DataStore
    AppointmentService --> DataStore
    BillingService --> DataStore
    AppointmentService --> PatientService
    AppointmentService --> DoctorService
    BillingService --> AppointmentService

    PatientService --> Patient
    DoctorService --> Doctor
    AppointmentService --> Appointment
    BillingService --> Bill
    Bill --> BillSummary
    Patient --> Person
    Doctor --> Person
    Patient --> IdGenerator
    Doctor --> IdGenerator
    Appointment --> IdGenerator
    Bill --> IdGenerator
    AppointmentMenu --> DateUtil
    SearchMenu --> DateUtil
```

## Main Data Flows

### Patient / Doctor Registration

```mermaid
sequenceDiagram
    actor User
    participant Menu as PatientMenu / DoctorMenu
    participant Service as PatientService / DoctorService
    participant Validator
    participant Store as DataStore<T>
    participant Ids as IdGenerator

    User->>Menu: Enter registration details
    Menu->>Ids: Entity constructor requests ID
    Menu->>Service: registerPatient(patient) / registerDoctor(doctor)
    Service->>Validator: Validate required fields
    Validator-->>Service: OK or InvalidDataException
    Service->>Store: add(id, entity)
    Store-->>Service: Stored in memory
    Service-->>Menu: Success
    Menu-->>User: Print generated ID
```

### Appointment Booking

```mermaid
sequenceDiagram
    actor User
    participant Menu as AppointmentMenu
    participant AppointmentService
    participant PatientService
    participant DoctorService
    participant Store as DataStore<Appointment>

    User->>Menu: Enter patient ID, doctor ID, date, reason
    Menu->>PatientService: getPatient(patientId)
    PatientService-->>Menu: Patient or PatientNotFoundException
    Menu->>DoctorService: getDoctor(doctorId)
    DoctorService-->>Menu: Doctor or DoctorNotFoundException
    Menu->>AppointmentService: createAppointment(doctorId, patientId, dateTime, reason)
    AppointmentService->>PatientService: getPatient(patientId)
    AppointmentService->>DoctorService: getDoctor(doctorId)
    AppointmentService->>Store: add(appointmentId, appointment)
    AppointmentService-->>Menu: Appointment
    Menu-->>User: Print appointment ID
```

### Billing

```mermaid
sequenceDiagram
    actor User
    participant Menu as BillingMenu
    participant BillingService
    participant AppointmentService
    participant DoctorService
    participant Store as DataStore<Bill>
    participant Bill

    User->>Menu: Enter appointment ID
    Menu->>AppointmentService: getAppointment(appointmentId)
    AppointmentService-->>Menu: Appointment
    Menu->>DoctorService: getDoctor(doctorId)
    DoctorService-->>Menu: Doctor
    Menu->>BillingService: generateBill(appointment, doctor)
    BillingService->>AppointmentService: getAppointment(appointmentId)
    BillingService->>Bill: new Bill(appointmentId, patientId, consultationRate)
    BillingService->>Store: add(billId, bill)
    BillingService-->>Menu: Bill
    Menu-->>User: Print bill ID and total
```

## Architectural Notes

- The menu layer owns console input/output only. It should not own business rules.
- The service layer owns validation orchestration, lookup, status changes, and in-memory persistence access.
- The entity layer owns domain state and simple domain behavior such as appointment status transitions and bill total calculation.
- `DataStore<T>` is an in-memory repository substitute, not durable persistence.
- `Appointment` stores `patientId` and `doctorId` instead of object references. This keeps the entity simple but means services must enforce referential integrity.

## Current Risks / Limitations

- Data is not durable. Restarting the application clears all patients, doctors, appointments, and bills.
- `DataStore.add` can duplicate items in `dataList` when the same ID is added again.
- Delete operations do not protect referential integrity. A patient or doctor can be deleted while appointments still reference their IDs.
- Billing service silently ignores unknown bill IDs for charge/payment updates.
- There is no concurrency protection beyond synchronized ID generation. This is acceptable for the current single-user CLI but not for a multi-user application.
