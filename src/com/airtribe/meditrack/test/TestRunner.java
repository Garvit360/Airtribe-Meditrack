package com.airtribe.meditrack.test;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.AppointmentStatus;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.CSVUtil;

import java.util.Arrays;
import java.util.List;

public class TestRunner {

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        testSearchablePatients();
        testSearchableDoctors();
        testMedicalEntityIds();
        testPatientDeepClone();
        testAppointmentClone();
        testCsvPersistenceRoundTrip();
        testLoadedIdsSyncCounters();

        System.out.println("Tests passed: " + passed);
        System.out.println("Tests failed: " + failed);

        if (failed > 0) {
            throw new AssertionError("Manual tests failed: " + failed);
        }
    }

    private static void testSearchablePatients() {
        PatientService patientService = new PatientService(false);
        Patient patient = new Patient("Asha Mehta", 32, "Female", "9876543210",
                "asha@example.com", "O+", "9123456780", "Pune");

        patientService.registerPatient(patient);

        assertEquals(1, patientService.searchPatientsByKeyword("asha").size(),
                "Patient keyword search should match name");
        assertEquals(1, patientService.searchPatientsByKeyword("O+").size(),
                "Patient keyword search should match blood group");
        assertEquals(0, patientService.searchPatientsByKeyword("unknown").size(),
                "Patient keyword search should ignore unrelated text");
    }

    private static void testSearchableDoctors() {
        DoctorService doctorService = new DoctorService(false);
        Doctor doctor = new Doctor("Raj Singh", 45, "Male", "9876543211",
                "raj@example.com", Specialization.CARDIOLOGY, 18, 800.0);

        doctorService.registerDoctor(doctor);

        assertEquals(1, doctorService.searchDoctorsByKeyword("raj").size(),
                "Doctor keyword search should match name");
        assertEquals(1, doctorService.searchDoctorsByKeyword("cardiology").size(),
                "Doctor keyword search should match specialization");
        assertEquals(0, doctorService.searchDoctorsByKeyword("dermatology").size(),
                "Doctor keyword search should ignore unrelated specialization");
    }

    private static void testMedicalEntityIds() {
        Patient patient = new Patient("Neha Rao", 28, "Female", "9876543212",
                "neha@example.com", "A+", "9123456781", "Mumbai");
        Doctor doctor = new Doctor("Kiran Shah", 50, "Male", "9876543213",
                "kiran@example.com", Specialization.NEUROLOGY, 22, 1000.0);

        assertTrue(patient.getPatientId().equals(patient.getId()),
                "Patient ID should come from MedicalEntity");
        assertTrue(doctor.getDoctorId().equals(doctor.getId()),
                "Doctor ID should come from MedicalEntity");
    }

    private static void testPatientDeepClone() {
        Patient patient = new Patient("Ira Jain", 39, "Female", "9876543214",
                "ira@example.com", "B+", "9123456782", "Delhi");
        patient.setMedicalHistory("Diabetes");
        patient.setAllergies(new String[]{"Penicillin"});

        Patient copy = patient.clone();
        copy.getMedicalHistory()[0] = "Hypertension";
        copy.getAllergies()[0] = "Dust";

        assertEquals("Diabetes", patient.getMedicalHistory()[0],
                "Patient clone should deep-copy medical history");
        assertEquals("Penicillin", patient.getAllergies()[0],
                "Patient clone should deep-copy allergies");
    }

    private static void testAppointmentClone() {
        PatientService patientService = new PatientService(false);
        DoctorService doctorService = new DoctorService(false);
        AppointmentService appointmentService = new AppointmentService(patientService, doctorService, false);

        Patient patient = new Patient("Dev Kapoor", 41, "Male", "9876543215",
                "dev@example.com", "AB+", "9123456783", "Bengaluru");
        Doctor doctor = new Doctor("Maya Iyer", 44, "Female", "9876543216",
                "maya@example.com", Specialization.GENERAL_MEDICINE, 16, 700.0);

        patientService.registerPatient(patient);
        doctorService.registerDoctor(doctor);

        Appointment appointment = appointmentService.createAppointment(
                doctor.getDoctorId(), patient.getPatientId(), 1720000000000L, "Fever");
        Appointment copy = appointment.clone();
        copy.setReasonOfVisit("Follow-up");

        assertEquals("Fever", appointment.getReasonOfVisit(),
                "Appointment clone should be a separate object");
        assertEquals(appointment.getAppointmentId(), copy.getAppointmentId(),
                "Cloned appointment should preserve the original ID");
    }

    private static void testCsvPersistenceRoundTrip() {
        String basePath = System.getProperty("java.io.tmpdir") + "/meditrack-test-" + System.nanoTime();
        String patientsFile = basePath + "/patients.csv";
        String doctorsFile = basePath + "/doctors.csv";
        String appointmentsFile = basePath + "/appointments.csv";

        Patient patient = new Patient("PAT9101", "Sara Khan", 29, "Female", "9876543217",
                "sara@example.com", "A-", "9123456784", "Chennai");
        Doctor doctor = new Doctor("DOC9201", "Amit Das", 52, "Male", "9876543218",
                "amit@example.com", Specialization.ORTHOPEDICS, 24, 1200.0);
        Appointment appointment = new Appointment("APPT9301", 1730000000000L, AppointmentStatus.CONFIRMED,
                "BackPain", patient.getPatientId(), doctor.getDoctorId());

        CSVUtil.savePatients(Arrays.asList(patient), patientsFile);
        CSVUtil.saveDoctors(Arrays.asList(doctor), doctorsFile);
        CSVUtil.saveAppointments(Arrays.asList(appointment), appointmentsFile);

        List<Patient> loadedPatients = CSVUtil.loadPatients(patientsFile);
        List<Doctor> loadedDoctors = CSVUtil.loadDoctors(doctorsFile);
        List<Appointment> loadedAppointments = CSVUtil.loadAppointments(appointmentsFile);

        assertEquals(1, loadedPatients.size(), "CSV should load one patient");
        assertEquals(patient.getPatientId(), loadedPatients.get(0).getPatientId(),
                "CSV should preserve patient ID");
        assertEquals(patient.getName(), loadedPatients.get(0).getName(),
                "CSV should preserve patient name");

        assertEquals(1, loadedDoctors.size(), "CSV should load one doctor");
        assertEquals(doctor.getDoctorId(), loadedDoctors.get(0).getDoctorId(),
                "CSV should preserve doctor ID");
        assertEquals(doctor.getSpecialization(), loadedDoctors.get(0).getSpecialization(),
                "CSV should preserve doctor specialization");

        assertEquals(1, loadedAppointments.size(), "CSV should load one appointment");
        assertEquals(appointment.getAppointmentId(), loadedAppointments.get(0).getAppointmentId(),
                "CSV should preserve appointment ID");
        assertEquals(appointment.getAppointmentStatus(), loadedAppointments.get(0).getAppointmentStatus(),
                "CSV should preserve appointment status");
    }

    private static void testLoadedIdsSyncCounters() {
        String basePath = System.getProperty("java.io.tmpdir") + "/meditrack-counter-test-" + System.nanoTime();
        String patientsFile = basePath + "/patients.csv";
        String doctorsFile = basePath + "/doctors.csv";
        String appointmentsFile = basePath + "/appointments.csv";

        CSVUtil.savePatients(Arrays.asList(new Patient("PAT9900", "Counter Patient", 30, "Female",
                "9876543219", "counter.patient@example.com", "B-", "9123456785", "Noida")), patientsFile);
        CSVUtil.saveDoctors(Arrays.asList(new Doctor("DOC9900", "Counter Doctor", 55, "Male",
                "9876543220", "counter.doctor@example.com", Specialization.DERMATOLOGY, 25, 1500.0)), doctorsFile);
        CSVUtil.saveAppointments(Arrays.asList(new Appointment("APPT9900", 1740000000000L,
                AppointmentStatus.PENDING, "SkinCheck", "PAT9900", "DOC9900")), appointmentsFile);

        CSVUtil.loadPatients(patientsFile);
        CSVUtil.loadDoctors(doctorsFile);
        CSVUtil.loadAppointments(appointmentsFile);

        Patient nextPatient = new Patient("Next Patient", 31, "Female", "9876543221",
                "next.patient@example.com", "O-", "9123456786", "Gurgaon");
        Doctor nextDoctor = new Doctor("Next Doctor", 56, "Male", "9876543222",
                "next.doctor@example.com", Specialization.PEDIATRICS, 26, 1600.0);
        Appointment nextAppointment = new Appointment(1750000000000L, "Review",
                nextPatient.getPatientId(), nextDoctor.getDoctorId());

        assertEquals("PAT9901", nextPatient.getPatientId(),
                "Loaded patient IDs should sync the patient counter");
        assertEquals("DOC9901", nextDoctor.getDoctorId(),
                "Loaded doctor IDs should sync the doctor counter");
        assertEquals("APPT9901", nextAppointment.getAppointmentId(),
                "Loaded appointment IDs should sync the appointment counter");
    }

    private static void assertTrue(boolean condition, String message) {
        if (condition) {
            passed++;
            return;
        }

        failed++;
        System.out.println("FAIL: " + message);
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual == null : expected.equals(actual)) {
            passed++;
            return;
        }

        failed++;
        System.out.println("FAIL: " + message + " expected=[" + expected + "] actual=[" + actual + "]");
    }
}
