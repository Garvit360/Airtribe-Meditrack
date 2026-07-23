package com.airtribe.meditrack.menu;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.BillSummary;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.BillingService;
import com.airtribe.meditrack.service.DoctorService;

import java.util.Scanner;

public class BillingMenu {

    public static void handleMenu(Scanner scanner, BillingService billingService,
            AppointmentService appointmentService, DoctorService doctorService) {
        boolean back = false;
        while (!back) {
            displayMenu();
            int choice = ConsoleInput.readInt(scanner, "Enter your choice: ");
            try {
                switch (choice) {
                    case 1:
                        generateBill(scanner, billingService, appointmentService, doctorService);
                        break;
                    case 2:
                        viewBill(scanner, billingService);
                        break;
                    case 3:
                        addMedicationCharges(scanner, billingService);
                        break;
                    case 4:
                        addLabCharges(scanner, billingService);
                        break;
                    case 5:
                        processPayment(scanner, billingService);
                        break;
                    case 6:
                        viewBillSummary(scanner, billingService);
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
        System.out.println("\n--- Billing Management ---");
        System.out.println("1. Generate Bill");
        System.out.println("2. View Bill Details");
        System.out.println("3. Add Medication Charges");
        System.out.println("4. Add Lab Charges");
        System.out.println("5. Process Payment");
        System.out.println("6. View Bill Summary");
        System.out.println("0. Back to Main Menu");
    }

    private static void generateBill(Scanner scanner, BillingService billingService,
            AppointmentService appointmentService, DoctorService doctorService) {
        String appointmentId = ConsoleInput.readRequired(scanner, "Enter Appointment ID: ");
        Appointment appointment = appointmentService.getAppointment(appointmentId);
        Doctor doctor = doctorService.getDoctor(appointment.getDoctorId());
        Bill bill = billingService.generateBill(appointment, doctor);
        System.out.println("Bill generated successfully. Bill ID: " + bill.getBillId());
        System.out.println("Total Amount: " + String.format("%.2f", bill.calculateAmount()));
    }

    private static void viewBill(Scanner scanner, BillingService billingService) {
        Bill bill = readBill(scanner, billingService);
        printBill(bill);
    }

    private static void addMedicationCharges(Scanner scanner, BillingService billingService) {
        String billId = ConsoleInput.readRequired(scanner, "Enter Bill ID: ");
        double amount = ConsoleInput.readDouble(scanner, "Enter medication charges: ");
        billingService.addMedicationCharges(billId, amount);
        Bill bill = billingService.getBill(billId);
        if (bill == null) {
            System.out.println("Bill not found.");
            return;
        }
        System.out.println("Medication charges added. Updated total: " + String.format("%.2f", bill.getTotalAmount()));
    }

    private static void addLabCharges(Scanner scanner, BillingService billingService) {
        String billId = ConsoleInput.readRequired(scanner, "Enter Bill ID: ");
        double amount = ConsoleInput.readDouble(scanner, "Enter lab charges: ");
        billingService.addLabCharges(billId, amount);
        Bill bill = billingService.getBill(billId);
        if (bill == null) {
            System.out.println("Bill not found.");
            return;
        }
        System.out.println("Lab charges added. Updated total: " + String.format("%.2f", bill.getTotalAmount()));
    }

    private static void processPayment(Scanner scanner, BillingService billingService) {
        Bill bill = readBill(scanner, billingService);
        System.out.println("Bill Amount: " + String.format("%.2f", bill.calculateAmount()));
        System.out.print("Confirm payment? (yes/no): ");
        if ("yes".equalsIgnoreCase(scanner.nextLine().trim())) {
            billingService.processPayment(bill.getBillId());
            System.out.println("Payment processed successfully.");
        } else {
            System.out.println("Payment cancelled.");
        }
    }

    private static void viewBillSummary(Scanner scanner, BillingService billingService) {
        Bill bill = readBill(scanner, billingService);
        BillSummary summary = bill.generateSummary();
        System.out.println(summary);
    }

    private static Bill readBill(Scanner scanner, BillingService billingService) {
        String billId = ConsoleInput.readRequired(scanner, "Enter Bill ID: ");
        Bill bill = billingService.getBill(billId);
        if (bill == null) {
            throw new IllegalArgumentException("Bill not found: " + billId);
        }
        return bill;
    }

    private static void printBill(Bill bill) {
        System.out.println("\nBill ID: " + bill.getBillId());
        System.out.println("Appointment ID: " + bill.getAppointmentId());
        System.out.println("Patient ID: " + bill.getPatient());
        System.out.println("Consultation Charge: " + String.format("%.2f", bill.getConsultationCharge()));
        System.out.println("Medication Charges: " + String.format("%.2f", bill.getMedicationCharges()));
        System.out.println("Lab Charges: " + String.format("%.2f", bill.getLabCharges()));
        System.out.println("Subtotal: " + String.format("%.2f", bill.getSubTotal()));
        System.out.println("Tax: " + String.format("%.2f", bill.getTax()));
        System.out.println("Total Amount: " + String.format("%.2f", bill.getTotalAmount()));
        System.out.println("Payment Status: " + bill.getBillStatus());
    }
}
