package com.airtribe.meditrack.util;

public class IdGenerator {
    private static int patientCounter = 1000;
    private static int doctorCounter = 2000;
    private static int appointmentCounter = 3000;
    private static int billCounter = 4000;

    public static synchronized String generatePatientId(){
        return "PAT" + (++patientCounter);
    }

    public static synchronized String generateDoctorId(){
        return "DOC" + (++doctorCounter);
    }

    public static synchronized String generateAppointmentId(){
        return "APPT" + (++appointmentCounter);
    }

    public static synchronized String generateBillId(){
        return "BILL" + (++billCounter);
    }
}
