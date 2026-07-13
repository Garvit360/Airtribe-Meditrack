package com.airtribe.meditrack.menu;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.PatientService;

import java.util.List;
import java.util.Scanner;

public class PatientMenu {

    public static void handleMenu(Scanner scanner, PatientService patientService, AppointmentService appointmentService) {
        boolean back = false;
        while (!back) {
            displayMenu();
            int choice = ConsoleInput.readInt(scanner, "Enter your choice: ");
            try {
                switch (choice) {
                    case 1:
                        registerPatient(scanner, patientService);
                        break;
                    case 2:
                        viewPatient(scanner, patientService);
                        break;
                    case 3:
                        updatePatient(scanner, patientService);
                        break;
                    case 4:
                        listAllPatients(patientService);
                        break;
                    case 5:
                        deletePatient(scanner, patientService, appointmentService);
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
        System.out.println("\n--- Patient Management ---");
        System.out.println("1. Register New Patient");
        System.out.println("2. View Patient Details");
        System.out.println("3. Update Patient");
        System.out.println("4. List All Patients");
        System.out.println("5. Delete Patient");
        System.out.println("0. Back to Main Menu");
    }

    private static void registerPatient(Scanner scanner, PatientService patientService) {
        System.out.println("\n--- Register New Patient ---");
        String name = ConsoleInput.readRequired(scanner, "Enter name: ");
        int age = ConsoleInput.readInt(scanner, "Enter age: ");
        String gender = ConsoleInput.readRequired(scanner, "Enter gender: ");
        String phone = ConsoleInput.readRequired(scanner, "Enter phone (10 digits): ");
        String email = ConsoleInput.readRequired(scanner, "Enter email: ");
        String bloodGroup = ConsoleInput.readRequired(scanner, "Enter blood group: ");
        String emergencyContact = ConsoleInput.readRequired(scanner, "Enter emergency contact: ");
        String address = ConsoleInput.readRequired(scanner, "Enter address: ");

        Patient patient = new Patient(name, age, gender, phone, email, bloodGroup, emergencyContact, address);
        patientService.registerPatient(patient);
        System.out.println("Patient registered successfully. Patient ID: " + patient.getPatientId());
    }

    private static void viewPatient(Scanner scanner, PatientService patientService) {
        String patientId = ConsoleInput.readRequired(scanner, "Enter Patient ID: ");
        printPatient(patientService.getPatient(patientId));
    }

    private static void updatePatient(Scanner scanner, PatientService patientService) {
        String patientId = ConsoleInput.readRequired(scanner, "Enter Patient ID: ");
        Patient patient = patientService.getPatient(patientId);

        System.out.println("\nEnter new information. Press Enter to keep the current value.");
        System.out.print("Name [" + patient.getName() + "]: ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) {
            patient.setName(name);
        }

        System.out.print("Age [" + patient.getAge() + "]: ");
        String age = scanner.nextLine().trim();
        if (!age.isEmpty()) {
            patient.setAge(Integer.parseInt(age));
        }

        System.out.print("Phone [" + patient.getContactNumber() + "]: ");
        String phone = scanner.nextLine().trim();
        if (!phone.isEmpty()) {
            patient.setContactNumber(phone);
        }

        System.out.print("Email [" + patient.getEmail() + "]: ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) {
            patient.setEmail(email);
        }

        System.out.print("Blood Group [" + patient.getBloodGroup() + "]: ");
        String bloodGroup = scanner.nextLine().trim();
        if (!bloodGroup.isEmpty()) {
            patient.setBloodGroup(bloodGroup);
        }

        System.out.print("Emergency Contact [" + patient.getEmergencyContact() + "]: ");
        String emergencyContact = scanner.nextLine().trim();
        if (!emergencyContact.isEmpty()) {
            patient.setEmergencyContact(emergencyContact);
        }

        System.out.print("Address [" + patient.getAddress() + "]: ");
        String address = scanner.nextLine().trim();
        if (!address.isEmpty()) {
            patient.setAddress(address);
        }

        patientService.updatePatient(patient);
        System.out.println("Patient updated successfully.");
    }

    private static void listAllPatients(PatientService patientService) {
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients registered.");
            return;
        }
        System.out.println("Total Patients: " + patients.size());
        for (Patient patient : patients) {
            printPatient(patient);
        }
    }

    private static void deletePatient(Scanner scanner, PatientService patientService, AppointmentService appointmentService) {
        String patientId = ConsoleInput.readRequired(scanner, "Enter Patient ID to delete: ");
        Patient patient = patientService.getPatient(patientId);
        if (appointmentService.hasAppointmentForPatient(patientId)) {
            throw new IllegalStateException("Cannot delete patient with existing appointments: " + patientId);
        }
        System.out.print("Delete patient " + patient.getName() + "? (yes/no): ");
        if ("yes".equalsIgnoreCase(scanner.nextLine().trim())) {
            patientService.deletePatient(patientId);
            System.out.println("Patient deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    static void printPatient(Patient patient) {
        System.out.println("\nPatient ID: " + patient.getPatientId());
        System.out.println("Name: " + patient.getName());
        System.out.println("Age: " + patient.getAge());
        System.out.println("Gender: " + patient.getGender());
        System.out.println("Phone: " + patient.getContactNumber());
        System.out.println("Email: " + patient.getEmail());
        System.out.println("Blood Group: " + patient.getBloodGroup());
        System.out.println("Emergency Contact: " + patient.getEmergencyContact());
        System.out.println("Address: " + patient.getAddress());
    }
}
