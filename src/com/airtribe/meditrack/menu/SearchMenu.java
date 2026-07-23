package com.airtribe.meditrack.menu;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;

import java.util.List;
import java.util.Scanner;

public class SearchMenu {

    public static void handleMenu(Scanner scanner, PatientService patientService,
            DoctorService doctorService, AppointmentService appointmentService) {
        boolean back = false;
        while (!back) {
            displayMenu();
            int choice = ConsoleInput.readInt(scanner, "Enter your choice: ");
            try {
                switch (choice) {
                    case 1:
                        searchPatient(scanner, patientService);
                        break;
                    case 2:
                        searchDoctor(scanner, doctorService);
                        break;
                    case 3:
                        searchAppointment(scanner, appointmentService, patientService, doctorService);
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n--- Search Menu ---");
        System.out.println("1. Search Patient");
        System.out.println("2. Search Doctor");
        System.out.println("3. Search Appointment");
        System.out.println("0. Back to Main Menu");
    }

    private static void searchPatient(Scanner scanner, PatientService patientService) {
        System.out.println("\n--- Search Patient ---");
        System.out.println("1. Search by Patient ID");
        System.out.println("2. Search by Name (Exact Match)");
        System.out.println("3. Search by Name (Partial Match)");
        System.out.println("4. Search by Age");
        int choice = ConsoleInput.readInt(scanner, "Enter your choice: ");

        switch (choice) {
            case 1:
                Patient patient = patientService.searchPatient(ConsoleInput.readRequired(scanner, "Enter Patient ID: "));
                if (patient == null) {
                    System.out.println("Patient not found.");
                } else {
                    PatientMenu.printPatient(patient);
                }
                break;
            case 2:
                displayPatientResults(patientService.searchPatient(ConsoleInput.readRequired(scanner, "Enter Patient Name: "), true));
                break;
            case 3:
                displayPatientResults(patientService.searchPatient(ConsoleInput.readRequired(scanner, "Enter Patient Name: "), false));
                break;
            case 4:
                displayPatientResults(patientService.searchPatient(ConsoleInput.readInt(scanner, "Enter Age: ")));
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void searchDoctor(Scanner scanner, DoctorService doctorService) {
        System.out.println("\n--- Search Doctor ---");
        System.out.println("1. Search by Doctor ID");
        System.out.println("2. Search by Name (Exact Match)");
        System.out.println("3. Search by Name (Partial Match)");
        System.out.println("4. Search by Specialization");
        System.out.println("5. Search by Years of Experience");
        int choice = ConsoleInput.readInt(scanner, "Enter your choice: ");

        switch (choice) {
            case 1:
                Doctor doctor = doctorService.searchDoctor(ConsoleInput.readRequired(scanner, "Enter Doctor ID: "));
                if (doctor == null) {
                    System.out.println("Doctor not found.");
                } else {
                    DoctorMenu.printDoctor(doctor);
                }
                break;
            case 2:
                displayDoctorResults(doctorService.searchDoctor(ConsoleInput.readRequired(scanner, "Enter Doctor Name: "), true));
                break;
            case 3:
                displayDoctorResults(doctorService.searchDoctor(ConsoleInput.readRequired(scanner, "Enter Doctor Name: "), false));
                break;
            case 4:
                displayDoctorResults(doctorService.searchDoctor(readSpecialization(scanner)));
                break;
            case 5:
                displayDoctorResults(doctorService.searchDoctor(ConsoleInput.readInt(scanner, "Enter Years of Experience: ")));
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void searchAppointment(Scanner scanner, AppointmentService appointmentService,
            PatientService patientService, DoctorService doctorService) {
        String appointmentId = ConsoleInput.readRequired(scanner, "Enter Appointment ID: ");
        Appointment appointment = appointmentService.getAppointment(appointmentId);
        AppointmentMenu.printAppointment(appointment, patientService, doctorService);
    }

    private static Specialization readSpecialization(Scanner scanner) {
        Specialization[] specializations = Specialization.values();
        for (int i = 0; i < specializations.length; i++) {
            System.out.println((i + 1) + ". " + specializations[i]);
        }
        while (true) {
            int choice = ConsoleInput.readInt(scanner, "Select specialization: ");
            if (choice >= 1 && choice <= specializations.length) {
                return specializations[choice - 1];
            }
            System.out.println("Invalid specialization.");
        }
    }

    private static void displayPatientResults(List<Patient> patients) {
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }
        System.out.println("Search Results: " + patients.size());
        for (Patient patient : patients) {
            PatientMenu.printPatient(patient);
        }
    }

    private static void displayDoctorResults(List<Doctor> doctors) {
        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
            return;
        }
        System.out.println("Search Results: " + doctors.size());
        for (Doctor doctor : doctors) {
            DoctorMenu.printDoctor(doctor);
        }
    }
}
