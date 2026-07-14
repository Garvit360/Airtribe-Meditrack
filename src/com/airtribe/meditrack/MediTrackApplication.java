package com.airtribe.meditrack;

import com.airtribe.meditrack.menu.MainMenu;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.BillingService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;

import java.util.Scanner;

public class MediTrackApplication {

    public static void main(String[] args){
        PatientService patientService = new PatientService();
        DoctorService doctorService = new DoctorService();
        AppointmentService appointmentService = new AppointmentService(patientService, doctorService);
        BillingService billingService = new BillingService(appointmentService);

        if (shouldLoadData(args)) {
            patientService.loadPatientsFromCsv();
            doctorService.loadDoctorsFromCsv();
            appointmentService.loadAppointmentsFromCsv();
            System.out.println("Persisted data loaded successfully.");
        }

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            System.out.println("MediTrack application started successfully.");
            while (running) {
                MainMenu.displayMenu();
                int choice = readMenuChoice(scanner);
                running = MainMenu.handleMenuChoice(choice, scanner, patientService, doctorService,
                        appointmentService, billingService);
            }
        }
    }

    private static boolean shouldLoadData(String[] args) {
        for (String arg : args) {
            if ("--loadData".equals(arg)) {
                return true;
            }
        }
        return false;
    }

    private static int readMenuChoice(Scanner scanner) {
        while (true) {
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
