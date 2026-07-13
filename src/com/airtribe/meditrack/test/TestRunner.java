package com.airtribe.meditrack.test;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;

public class TestRunner {

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        testSearchablePatients();
        testSearchableDoctors();
        testMedicalEntityIds();
        testPatientDeepClone();
        testAppointmentClone();

        System.out.println("Tests passed: " + passed);
        System.out.println("Tests failed: " + failed);

        if (failed > 0) {
            throw new AssertionError("Manual tests failed: " + failed);
        }
    }

    private static void testSearchablePatients() {
        PatientService patientService = new PatientService();
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
        DoctorService doctorService = new DoctorService();
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
        PatientService patientService = new PatientService();
        DoctorService doctorService = new DoctorService();
        AppointmentService appointmentService = new AppointmentService(patientService, doctorService);

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
