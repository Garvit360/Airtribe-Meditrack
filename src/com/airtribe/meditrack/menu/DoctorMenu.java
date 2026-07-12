package com.airtribe.meditrack.menu;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.service.DoctorService;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DoctorMenu {

    public static void handleMenu(Scanner scanner, DoctorService doctorService) {
        boolean back = false;
        while (!back) {
            displayMenu();
            int choice = ConsoleInput.readInt(scanner, "Enter your choice: ");
            try {
                switch (choice) {
                    case 1:
                        registerDoctor(scanner, doctorService);
                        break;
                    case 2:
                        viewDoctor(scanner, doctorService);
                        break;
                    case 3:
                        updateDoctor(scanner, doctorService);
                        break;
                    case 4:
                        listAllDoctors(doctorService);
                        break;
                    case 5:
                        deleteDoctor(scanner, doctorService);
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
        System.out.println("\n--- Doctor Management ---");
        System.out.println("1. Register New Doctor");
        System.out.println("2. View Doctor Details");
        System.out.println("3. Update Doctor");
        System.out.println("4. List All Doctors");
        System.out.println("5. Delete Doctor");
        System.out.println("0. Back to Main Menu");
    }

    private static void registerDoctor(Scanner scanner, DoctorService doctorService) {
        System.out.println("\n--- Register New Doctor ---");
        String name = ConsoleInput.readRequired(scanner, "Enter name: ");
        int age = ConsoleInput.readInt(scanner, "Enter age: ");
        String gender = ConsoleInput.readRequired(scanner, "Enter gender: ");
        String phone = ConsoleInput.readRequired(scanner, "Enter phone (10 digits): ");
        String email = ConsoleInput.readRequired(scanner, "Enter email: ");
        Specialization specialization = readSpecialization(scanner);
        int yearsOfExperience = ConsoleInput.readInt(scanner, "Enter years of experience: ");
        double consultationRate = ConsoleInput.readDouble(scanner, "Enter consultation rate: ");

        Doctor doctor = new Doctor(name, age, gender, phone, email, specialization, yearsOfExperience, consultationRate);

        System.out.print("Enter availability slots (comma-separated, optional): ");
        String availability = scanner.nextLine().trim();
        if (!availability.isEmpty()) {
            doctor.setAvailability(Arrays.asList(availability.split("\\s*,\\s*")));
        }

        doctorService.registerDoctor(doctor);
        System.out.println("Doctor registered successfully. Doctor ID: " + doctor.getDoctorId());
    }

    private static void viewDoctor(Scanner scanner, DoctorService doctorService) {
        String doctorId = ConsoleInput.readRequired(scanner, "Enter Doctor ID: ");
        printDoctor(doctorService.getDoctor(doctorId));
    }

    private static void updateDoctor(Scanner scanner, DoctorService doctorService) {
        String doctorId = ConsoleInput.readRequired(scanner, "Enter Doctor ID: ");
        Doctor doctor = doctorService.getDoctor(doctorId);

        System.out.println("\nEnter new information. Press Enter to keep the current value.");
        System.out.print("Name [" + doctor.getName() + "]: ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) {
            doctor.setName(name);
        }

        System.out.print("Consultation Rate [" + doctor.getConsultationRate() + "]: ");
        String rate = scanner.nextLine().trim();
        if (!rate.isEmpty()) {
            doctor.setConsultationRate(Double.parseDouble(rate));
        }

        System.out.print("Years of Experience [" + doctor.getYearsOfExperience() + "]: ");
        String experience = scanner.nextLine().trim();
        if (!experience.isEmpty()) {
            doctor.setYearsOfExperience(Integer.parseInt(experience));
        }

        System.out.print("Availability " + doctor.getAvailability() + ": ");
        String availability = scanner.nextLine().trim();
        if (!availability.isEmpty()) {
            doctor.setAvailability(Arrays.asList(availability.split("\\s*,\\s*")));
        }

        doctorService.updateDoctor(doctor);
        System.out.println("Doctor updated successfully.");
    }

    private static void listAllDoctors(DoctorService doctorService) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("No doctors registered.");
            return;
        }
        System.out.println("Total Doctors: " + doctors.size());
        for (Doctor doctor : doctors) {
            printDoctor(doctor);
        }
    }

    private static void deleteDoctor(Scanner scanner, DoctorService doctorService) {
        String doctorId = ConsoleInput.readRequired(scanner, "Enter Doctor ID to delete: ");
        Doctor doctor = doctorService.getDoctor(doctorId);
        System.out.print("Delete doctor " + doctor.getName() + "? (yes/no): ");
        if ("yes".equalsIgnoreCase(scanner.nextLine().trim())) {
            doctorService.deleteDoctor(doctorId);
            System.out.println("Doctor deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static Specialization readSpecialization(Scanner scanner) {
        Specialization[] specializations = Specialization.values();
        System.out.println("\nAvailable Specializations:");
        for (int i = 0; i < specializations.length; i++) {
            System.out.println((i + 1) + ". " + specializations[i]);
        }
        while (true) {
            int choice = ConsoleInput.readInt(scanner, "Select specialization (1-" + specializations.length + "): ");
            if (choice >= 1 && choice <= specializations.length) {
                return specializations[choice - 1];
            }
            System.out.println("Invalid specialization.");
        }
    }

    static void printDoctor(Doctor doctor) {
        System.out.println("\nDoctor ID: " + doctor.getDoctorId());
        System.out.println("Name: " + doctor.getName());
        System.out.println("Age: " + doctor.getAge());
        System.out.println("Gender: " + doctor.getGender());
        System.out.println("Phone: " + doctor.getContactNumber());
        System.out.println("Email: " + doctor.getEmail());
        System.out.println("Specialization: " + doctor.getSpecialization());
        System.out.println("Years of Experience: " + doctor.getYearsOfExperience());
        System.out.println("Consultation Rate: " + doctor.getConsultationRate());
        System.out.println("Availability: " + doctor.getAvailability());
    }
}
