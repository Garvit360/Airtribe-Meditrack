package com.airtribe.meditrack.menu;

import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.BillingService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;

import java.util.Scanner;

public class MainMenu {

    public static void displayMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Patient Management");
        System.out.println("2. Doctor Management");
        System.out.println("3. Appointment Management");
        System.out.println("4. Billing");
        System.out.println("5. Search");
        System.out.println("6. Demonstrate Features");
        System.out.println("0. Exit");
        System.out.println("===============================");
    }

    public static boolean handleMenuChoice(int choice, Scanner scanner,
            PatientService patientService,
            DoctorService doctorService,
            AppointmentService appointmentService,
            BillingService billingService) {
        try {
            switch (choice) {
                case 1:
                    PatientMenu.handleMenu(scanner, patientService, appointmentService);
                    return true;
                case 2:
                    DoctorMenu.handleMenu(scanner, doctorService, appointmentService);
                    return true;
                case 3:
                    AppointmentMenu.handleMenu(scanner, appointmentService, patientService, doctorService);
                    return true;
                case 4:
                    BillingMenu.handleMenu(scanner, billingService, appointmentService, doctorService);
                    return true;
                case 5:
                    SearchMenu.handleMenu(scanner, patientService, doctorService, appointmentService);
                    return true;
                case 6:
                    FeatureDemonstrationMenu.demonstrateFeatures(patientService, doctorService);
                    return true;
                case 0:
                    System.out.println("Thank you for using MediTrack!");
                    return false;
                default:
                    System.out.println("Invalid choice. Try again.");
                    return true;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return true;
        }
    }
}
