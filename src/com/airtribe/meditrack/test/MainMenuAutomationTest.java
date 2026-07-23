package com.airtribe.meditrack.test;

import com.airtribe.meditrack.menu.MainMenu;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.BillingService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class MainMenuAutomationTest {

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        String output = runMainMenuScript();

        System.out.println("========== MAIN MENU AUTOMATION TEST ==========");
        assertContains(output, "Patient registration", "Patient registered successfully. Patient ID: PAT1001");
        assertContains(output, "Patient view", "Name: Alice Patient");
        assertContains(output, "Patient update", "Patient updated successfully.");
        assertContains(output, "Patient list", "Total Patients:");
        assertContains(output, "Patient delete", "Patient deleted successfully.");

        assertContains(output, "Doctor registration", "Doctor registered successfully. Doctor ID: DOC2001");
        assertContains(output, "Doctor view", "Name: Raj Doctor");
        assertContains(output, "Doctor update", "Doctor updated successfully.");
        assertContains(output, "Doctor list", "Total Doctors:");
        assertContains(output, "Doctor delete", "Doctor deleted successfully.");

        assertContains(output, "Appointment create", "Appointment created successfully. Appointment ID: APPT3001");
        assertContains(output, "Appointment view", "Reason for Visit: Fever");
        assertContains(output, "Appointment confirm", "Appointment confirmed successfully.");
        assertContains(output, "Appointment list", "Appointments for Alice Updated:");
        assertContains(output, "Appointment cancel", "Appointment cancelled successfully.");

        assertContains(output, "Bill generate", "Bill generated successfully. Bill ID: BILL4001");
        assertContains(output, "Bill view", "Payment Status: UNPAID");
        assertContains(output, "Medication charge", "Medication charges added. Updated total:");
        assertContains(output, "Lab charge", "Lab charges added. Updated total:");
        assertContains(output, "Payment process", "Payment processed successfully.");
        assertContains(output, "Bill summary", "BillSummary{");

        assertContains(output, "Patient search", "Patient ID: PAT1001");
        assertContains(output, "Doctor search", "Specialization: CARDIOLOGY");
        assertContains(output, "Appointment search", "Appointment ID: APPT3001");
        assertNotContains(output, "No menu errors", "Error:");
        assertNotContains(output, "No invalid numeric input", "Please enter a valid");

        System.out.println("-----------------------------------------------");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);

        if (failed > 0) {
            throw new AssertionError("Main menu automation failed: " + failed);
        }
    }

    private static String runMainMenuScript() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();

        try (Scanner scanner = new Scanner(new ByteArrayInputStream(buildScript().getBytes(StandardCharsets.UTF_8)))) {
            System.setOut(new PrintStream(capturedOutput));

            PatientService patientService = new PatientService(false);
            DoctorService doctorService = new DoctorService(false);
            AppointmentService appointmentService = new AppointmentService(patientService, doctorService, false);
            BillingService billingService = new BillingService(appointmentService);

            boolean running = true;
            while (running) {
                MainMenu.displayMenu();
                int choice = Integer.parseInt(scanner.nextLine().trim());
                running = MainMenu.handleMenuChoice(choice, scanner, patientService, doctorService,
                        appointmentService, billingService);
            }
        } finally {
            System.setOut(originalOut);
        }

        return capturedOutput.toString();
    }

    private static String buildScript() {
        StringBuilder script = new StringBuilder();

        addPatientFlow(script);
        addDoctorFlow(script);
        addAppointmentFlow(script);
        addBillingFlow(script);
        addSearchFlow(script);
        add(script, "0");

        return script.toString();
    }

    private static void addPatientFlow(StringBuilder script) {
        add(script, "1");
        add(script, "1");
        add(script, "Alice Patient");
        add(script, "30");
        add(script, "Female");
        add(script, "9876543210");
        add(script, "alice@example.com");
        add(script, "O+");
        add(script, "9999999999");
        add(script, "Delhi");

        add(script, "2");
        add(script, "PAT1001");

        add(script, "3");
        add(script, "PAT1001");
        add(script, "Alice Updated");
        add(script, "31");
        add(script, "9876543211");
        add(script, "alice.updated@example.com");
        add(script, "A+");
        add(script, "9999999998");
        add(script, "Mumbai");

        add(script, "4");

        add(script, "1");
        add(script, "Bob Delete");
        add(script, "25");
        add(script, "Male");
        add(script, "9876543212");
        add(script, "bob@example.com");
        add(script, "B+");
        add(script, "9999999997");
        add(script, "Pune");

        add(script, "5");
        add(script, "PAT1002");
        add(script, "yes");

        add(script, "0");
    }

    private static void addDoctorFlow(StringBuilder script) {
        add(script, "2");
        add(script, "1");
        add(script, "Raj Doctor");
        add(script, "45");
        add(script, "Male");
        add(script, "9876543213");
        add(script, "raj@example.com");
        add(script, "1");
        add(script, "20");
        add(script, "800");
        add(script, "Mon 10");

        add(script, "2");
        add(script, "DOC2001");

        add(script, "3");
        add(script, "DOC2001");
        add(script, "Raj Updated");
        add(script, "900");
        add(script, "21");
        add(script, "Tue 11");

        add(script, "4");

        add(script, "1");
        add(script, "Doctor Delete");
        add(script, "50");
        add(script, "Male");
        add(script, "9876543214");
        add(script, "doctor.delete@example.com");
        add(script, "2");
        add(script, "10");
        add(script, "500");
        add(script, "");

        add(script, "5");
        add(script, "DOC2002");
        add(script, "yes");

        add(script, "0");
    }

    private static void addAppointmentFlow(StringBuilder script) {
        add(script, "3");
        add(script, "1");
        add(script, "PAT1001");
        add(script, "DOC2001");
        add(script, "2099-01-01 10:00");
        add(script, "Fever");

        add(script, "2");
        add(script, "APPT3001");

        add(script, "4");
        add(script, "APPT3001");

        add(script, "5");
        add(script, "PAT1001");

        add(script, "3");
        add(script, "APPT3001");
        add(script, "yes");

        add(script, "0");
    }

    private static void addBillingFlow(StringBuilder script) {
        add(script, "4");
        add(script, "1");
        add(script, "APPT3001");

        add(script, "2");
        add(script, "BILL4001");

        add(script, "3");
        add(script, "BILL4001");
        add(script, "100");

        add(script, "4");
        add(script, "BILL4001");
        add(script, "200");

        add(script, "5");
        add(script, "BILL4001");
        add(script, "yes");

        add(script, "6");
        add(script, "BILL4001");

        add(script, "0");
    }

    private static void addSearchFlow(StringBuilder script) {
        add(script, "5");
        add(script, "1");
        add(script, "1");
        add(script, "PAT1001");

        add(script, "2");
        add(script, "4");
        add(script, "1");

        add(script, "3");
        add(script, "APPT3001");

        add(script, "0");
    }

    private static void add(StringBuilder script, String input) {
        script.append(input).append(System.lineSeparator());
    }

    private static void assertContains(String output, String name, String expectedText) {
        if (output.contains(expectedText)) {
            passed++;
            System.out.println("PASS - " + name);
            return;
        }

        failed++;
        System.out.println("FAIL - " + name + " | Missing text: " + expectedText);
    }

    private static void assertNotContains(String output, String name, String unexpectedText) {
        if (!output.contains(unexpectedText)) {
            passed++;
            System.out.println("PASS - " + name);
            return;
        }

        failed++;
        System.out.println("FAIL - " + name + " | Unexpected text: " + unexpectedText);
    }
}
