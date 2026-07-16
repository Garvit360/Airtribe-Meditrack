# MediTrack Class UML Diagram

This diagram reflects the current implementation, not the original assignment wish list.

## Domain, Service, Utility, And Pattern UML

```mermaid
classDiagram
    direction LR

    class Main {
        +main(String[] args) void
    }

    class MediTrackApplication {
        +main(String[] args) void
        -shouldLoadData(String[] args) boolean
        -readMenuChoice(Scanner scanner) int
    }

    class MedicalEntity {
        <<abstract>>
        -String id
        #MedicalEntity(String id)
        +getId() String
        +getEntityType()* String
        +printSummary() void
        #clone() MedicalEntity
    }

    class Person {
        -String name
        -int age
        -String gender
        -String contactNumber
        -String email
        +Person(String id, String name, int age, String gender, String contactNumber, String email)
        +getEntityType() String
        +getName() String
        +setName(String name) void
        +getAge() int
        +setAge(int age) void
        +getGender() String
        +setGender(String gender) void
        +getContactNumber() String
        +setContactNumber(String contactNumber) void
        +getEmail() String
        +setEmail(String email) void
        #clone() Person
    }

    class Patient {
        -String[] medicalHistory
        -String[] allergies
        -String bloodGroup
        -String emergencyContact
        -String address
        +Patient(String name, int age, String gender, String contactNumber, String email, String bloodGroup, String emergencyContact, String address)
        +Patient(String patientId, String name, int age, String gender, String contactNumber, String email, String bloodGroup, String emergencyContact, String address)
        +getEntityType() String
        +getPatientId() String
        +matchesSearchCriteria(String keyword) boolean
        +clone() Patient
    }

    class Doctor {
        -Specialization specialization
        -int yearsOfExperience
        -List~String~ availability
        -double consultationRate
        +Doctor(String name, int age, String gender, String contactNumber, String email, Specialization specialization, int yearsOfExperience, double consultationRate)
        +Doctor(String doctorId, String name, int age, String gender, String contactNumber, String email, Specialization specialization, int yearsOfExperience, double consultationRate)
        +getEntityType() String
        +getDoctorId() String
        +matchesSearchCriteria(String keyword) boolean
    }

    class Appointment {
        -String appointmentId
        -Long appointmentDateTime
        -AppointmentStatus appointmentStatus
        -String reasonOfVisit
        -String patientId
        -String doctorId
        +Appointment(Long appointmentDateTime, String reasonOfVisit, String patientId, String doctorId)
        +Appointment(String appointmentId, Long appointmentDateTime, AppointmentStatus appointmentStatus, String reasonOfVisit, String patientId, String doctorId)
        +confirmAppointment() void
        +cancelAppointment() void
        +clone() Appointment
    }

    class Bill {
        -String billId
        -String appointmentId
        -String patientId
        -double consultationCharge
        -double medicationCharges
        -double labCharges
        -BillStatus billStatus
        +Bill(String appointmentId, String patientId, double consultationCharge)
        +calculateAmount() double
        +processPayment() void
        +addCharges(double medication, double labCharges) void
        +generateSummary() BillSummary
    }

    class BillSummary {
        <<final>>
        -String billId
        -String patientId
        -double subTotal
        -double totalAmount
        -double tax
        -BillStatus billStatus
        -long generatedTimeStamp
    }

    class PatientService {
        -DataStore~Patient~ patientStore
        -boolean persistenceEnabled
        +registerPatient(Patient patient) void
        +getPatient(String patientId) Patient
        +updatePatient(Patient patient) void
        +deletePatient(String patientId) void
        +searchPatientsByKeyword(String keyword) List~Patient~
        +loadPatientsFromCsv() void
        +savePatients() void
    }

    class DoctorService {
        -DataStore~Doctor~ doctorStore
        -boolean persistenceEnabled
        +registerDoctor(Doctor doctor) void
        +getDoctor(String doctorId) Doctor
        +updateDoctor(Doctor doctor) void
        +deleteDoctor(String doctorId) void
        +searchDoctorsByKeyword(String keyword) List~Doctor~
        +loadDoctorsFromCsv() void
        +saveDoctors() void
    }

    class AppointmentService {
        -DataStore~Appointment~ appointmentStore
        -DoctorService doctorService
        -PatientService patientService
        -boolean persistenceEnabled
        -List~AppointmentObserver~ observers
        +createAppointment(String doctorId, String patientId, Long dateTime, String reason) Appointment
        +cancelAppointment(String appointmentId) void
        +confirmAppointment(String appointmentId) void
        +loadAppointmentsFromCsv() void
        +saveAppointments() void
        +addObserver(AppointmentObserver observer) void
        +removeObserver(AppointmentObserver observer) void
    }

    class BillingService {
        -DataStore~Bill~ billStore
        -AppointmentService appointmentService
        +generateBill(String appointmentId, double consultationCharge) Bill
        +generateBill(Appointment appointment, Doctor doctor) Bill
        +addMedicationCharges(String billId, double amount) void
        +addLabCharges(String billId, double amount) void
        +processPayment(String billId) void
        +getBill(String billId) Bill
    }

    class DataStore~T~ {
        -HashMap~String,T~ dataMap
        -ArrayList~T~ dataList
        +add(String id, T item) void
        +findById(String id) T
        +update(String id, T item) void
        +remove(String id) void
        +getAll() List~T~
    }

    class CSVUtil {
        +savePatients(List~Patient~ patients, String filePath) void$
        +loadPatients(String filePath) List~Patient~$
        +saveDoctors(List~Doctor~ doctors, String filePath) void$
        +loadDoctors(String filePath) List~Doctor~$
        +saveAppointments(List~Appointment~ appointments, String filePath) void$
        +loadAppointments(String filePath) List~Appointment~$
    }

    class IdGenerator {
        -IdGenerator INSTANCE$
        -int patientCounter
        -int doctorCounter
        -int appointmentCounter
        -int billCounter
        -IdGenerator()
        +getInstance() IdGenerator$
        +generatePatientId() String
        +generateDoctorId() String
        +generateAppointmentId() String
        +generateBillId() String
        +syncPatientCounter(String patientId) void
        +syncDoctorCounter(String doctorId) void
        +syncAppointmentCounter(String appointmentId) void
    }

    class BillFactory {
        +createConsultationBill(Appointment appointment, Doctor doctor) Bill$
        +createManualBill(String appointmentId, String patientId, double consultationCharges) Bill$
    }

    class AppointmentObserver {
        <<interface>>
        +onAppointmentCreated(Appointment appointment) void
        +onAppointmentConfirmed(Appointment appointment) void
        +onAppointmentCancelled(Appointment appointment) void
    }

    class ConsoleAppointmentNotifier {
        +onAppointmentCreated(Appointment appointment) void
        +onAppointmentConfirmed(Appointment appointment) void
        +onAppointmentCancelled(Appointment appointment) void
    }

    class Payable {
        <<interface>>
        +calculateAmount() double
        +processPayment() void
        +isPaymentComplete() BillStatus
        +printReceipt() void
    }

    class Searchable {
        <<interface>>
        +matchesSearchCriteria(String keyword) boolean
        +displaySearchResult() void
    }

    MedicalEntity <|-- Person
    Person <|-- Patient
    Person <|-- Doctor
    Searchable <|.. Patient
    Searchable <|.. Doctor
    Payable <|.. Bill
    AppointmentObserver <|.. ConsoleAppointmentNotifier

    PatientService *-- DataStore~Patient~
    DoctorService *-- DataStore~Doctor~
    AppointmentService *-- DataStore~Appointment~
    BillingService *-- DataStore~Bill~

    PatientService --> CSVUtil
    DoctorService --> CSVUtil
    AppointmentService --> CSVUtil
    AppointmentService --> AppointmentObserver
    BillingService --> BillFactory

    Patient --> IdGenerator
    Doctor --> IdGenerator
    Appointment --> IdGenerator
    Bill --> IdGenerator
    Bill --> BillSummary
```

## Menu And Test Layer UML

```mermaid
classDiagram
    direction TB

    class MainMenu {
        +displayMenu() void$
        +handleMenuChoice(int choice, Scanner scanner, PatientService patientService, DoctorService doctorService, AppointmentService appointmentService, BillingService billingService) boolean$
    }

    class PatientMenu
    class DoctorMenu
    class AppointmentMenu
    class BillingMenu
    class SearchMenu
    class FeatureDemonstrationMenu
    class ConsoleInput
    class TestRunner
    class MainMenuAutomationTest

    MainMenu --> PatientMenu
    MainMenu --> DoctorMenu
    MainMenu --> AppointmentMenu
    MainMenu --> BillingMenu
    MainMenu --> SearchMenu
    MainMenu --> FeatureDemonstrationMenu
    PatientMenu --> ConsoleInput
    DoctorMenu --> ConsoleInput
    AppointmentMenu --> ConsoleInput
    BillingMenu --> ConsoleInput
    SearchMenu --> ConsoleInput
    MainMenuAutomationTest --> MainMenu
```

## Notes

- `Person` is concrete but inherits shared identity behavior from abstract `MedicalEntity`.
- `Patient` deep-copies mutable arrays in `clone()`.
- `Appointment` uses `super.clone()` because its fields are IDs, immutable values, or enums.
- `Searchable` is implemented by `Patient` and `Doctor`.
- CSV persistence covers patients, doctors, and appointments. Bills remain in memory only.
- `AppointmentService` owns observer notification because appointment lifecycle changes happen in the service layer.
