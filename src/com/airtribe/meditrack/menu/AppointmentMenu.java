package com.airtribe.meditrack.menu;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.DateUtil;

import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

public class AppointmentMenu {

    public static void handleMenu(Scanner scanner, AppointmentService appointmentService,
            PatientService patientService, DoctorService doctorService) {
        boolean back = false;
        while (!back) {
            displayMenu();
            int choice = ConsoleInput.readInt(scanner, "Enter your choice: ");
            try {
                switch (choice) {
                    case 1:
                        createAppointment(scanner, appointmentService, patientService, doctorService);
                        break;
                    case 2:
                        viewAppointment(scanner, appointmentService, patientService, doctorService);
                        break;
                    case 3:
                        cancelAppointment(scanner, appointmentService);
                        break;
                    case 4:
                        confirmAppointment(scanner, appointmentService);
                        break;
                    case 5:
                        listPatientAppointments(scanner, appointmentService, patientService, doctorService);
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
        System.out.println("\n--- Appointment Management ---");
        System.out.println("1. Create Appointment");
        System.out.println("2. View Appointment Details");
        System.out.println("3. Cancel Appointment");
        System.out.println("4. Confirm Appointment");
        System.out.println("5. List Patient Appointments");
        System.out.println("0. Back to Main Menu");
    }

    private static void createAppointment(Scanner scanner, AppointmentService appointmentService,
            PatientService patientService, DoctorService doctorService) throws ParseException {
        System.out.println("\n--- Create Appointment ---");
        String patientId = ConsoleInput.readRequired(scanner, "Enter Patient ID: ");
        String doctorId = ConsoleInput.readRequired(scanner, "Enter Doctor ID: ");
        String dateTimeInput = ConsoleInput.readRequired(scanner, "Enter appointment date and time (yyyy-MM-dd HH:mm): ");
        String reason = ConsoleInput.readRequired(scanner, "Enter reason for visit: ");

        patientService.getPatient(patientId);
        doctorService.getDoctor(doctorId);

        long dateTime = DateUtil.parseDateTime(dateTimeInput);
        if (!DateUtil.isFutureDate(dateTime)) {
            System.out.println("Warning: appointment date is not in the future.");
        }

        Appointment appointment = appointmentService.createAppointment(doctorId, patientId, dateTime, reason);
        System.out.println("Appointment created successfully. Appointment ID: " + appointment.getAppointmentId());
    }

    private static void viewAppointment(Scanner scanner, AppointmentService appointmentService,
            PatientService patientService, DoctorService doctorService) {
        String appointmentId = ConsoleInput.readRequired(scanner, "Enter Appointment ID: ");
        Appointment appointment = appointmentService.getAppointment(appointmentId);
        printAppointment(appointment, patientService, doctorService);
    }

    private static void cancelAppointment(Scanner scanner, AppointmentService appointmentService) {
        String appointmentId = ConsoleInput.readRequired(scanner, "Enter Appointment ID: ");
        appointmentService.getAppointment(appointmentId);
        System.out.print("Cancel this appointment? (yes/no): ");
        if ("yes".equalsIgnoreCase(scanner.nextLine().trim())) {
            appointmentService.cancelAppointment(appointmentId);
            System.out.println("Appointment cancelled successfully.");
        } else {
            System.out.println("Cancellation cancelled.");
        }
    }

    private static void confirmAppointment(Scanner scanner, AppointmentService appointmentService) {
        String appointmentId = ConsoleInput.readRequired(scanner, "Enter Appointment ID: ");
        appointmentService.confirmAppointment(appointmentId);
        System.out.println("Appointment confirmed successfully.");
    }

    private static void listPatientAppointments(Scanner scanner, AppointmentService appointmentService,
            PatientService patientService, DoctorService doctorService) {
        String patientId = ConsoleInput.readRequired(scanner, "Enter Patient ID: ");
        Patient patient = patientService.getPatient(patientId);
        List<Appointment> appointments = appointmentService.getPatientAppointments(patientId);
        if (appointments.isEmpty()) {
            System.out.println("No appointments found for patient " + patient.getName() + ".");
            return;
        }
        System.out.println("Appointments for " + patient.getName() + ":");
        for (Appointment appointment : appointments) {
            printAppointment(appointment, patientService, doctorService);
        }
    }

    static void printAppointment(Appointment appointment, PatientService patientService, DoctorService doctorService) {
        System.out.println("\nAppointment ID: " + appointment.getAppointmentId());
        System.out.println("Patient ID: " + appointment.getPatientId());
        System.out.println("Doctor ID: " + appointment.getDoctorId());
        System.out.println("Date & Time: " + DateUtil.formatDateTime(appointment.getAppointmentDateTime()));
        System.out.println("Status: " + appointment.getAppointmentStatus());
        System.out.println("Reason for Visit: " + appointment.getReasonOfVisit());
        try {
            Patient patient = patientService.getPatient(appointment.getPatientId());
            System.out.println("Patient Name: " + patient.getName());
        } catch (Exception e) {
            System.out.println("Patient Name: unavailable");
        }
        try {
            Doctor doctor = doctorService.getDoctor(appointment.getDoctorId());
            System.out.println("Doctor Name: " + doctor.getName());
        } catch (Exception e) {
            System.out.println("Doctor Name: unavailable");
        }
    }
}
