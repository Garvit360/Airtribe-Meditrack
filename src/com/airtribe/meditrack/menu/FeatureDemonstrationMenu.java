package com.airtribe.meditrack.menu;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;

import java.util.List;

public class FeatureDemonstrationMenu {

    public static void demonstrateFeatures(PatientService patientService, DoctorService doctorService) {
        System.out.println("\n=== Demonstrating MediTrack Features ===\n");
        demonstrateMethodOverloading(patientService);
        demonstrateInheritance(patientService, doctorService);
        demonstrateEnums();
        System.out.println("=== Feature Demonstration Complete ===\n");
    }

    private static void demonstrateMethodOverloading(PatientService patientService) {
        System.out.println("1. METHOD OVERLOADING:");
        System.out.println("PatientService exposes searchPatient by ID, name, and age.");

        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("(No patients available for demonstration)\n");
            return;
        }

        Patient sample = patients.get(0);
        Patient found = patientService.searchPatient(sample.getPatientId());
        System.out.println("Search by ID " + sample.getPatientId() + ": " + (found == null ? "not found" : found.getName()));
        System.out.println();
    }

    private static void demonstrateInheritance(PatientService patientService, DoctorService doctorService) {
        System.out.println("2. INHERITANCE:");
        System.out.println("Patient and Doctor both extend Person and share common identity/contact fields.");

        List<Patient> patients = patientService.getAllPatients();
        List<Doctor> doctors = doctorService.getAllDoctors();
        if (!patients.isEmpty()) {
            Patient patient = patients.get(0);
            System.out.println("Sample patient: " + patient.getName() + ", " + patient.getContactNumber());
        }
        if (!doctors.isEmpty()) {
            Doctor doctor = doctors.get(0);
            System.out.println("Sample doctor: " + doctor.getName() + ", " + doctor.getContactNumber());
        }
        if (patients.isEmpty() && doctors.isEmpty()) {
            System.out.println("(No data available for demonstration)");
        }
        System.out.println();
    }

    private static void demonstrateEnums() {
        System.out.println("3. ENUMS:");
        System.out.println("Specialization values:");
        for (Specialization specialization : Specialization.values()) {
            System.out.println("- " + specialization);
        }
        System.out.println();
    }
}
