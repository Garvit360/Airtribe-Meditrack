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

    public static synchronized void syncPatientCounter(String patientId) {
        patientCounter = Math.max(patientCounter, extractNumber(patientId, "PAT"));
    }

    public static synchronized void syncDoctorCounter(String doctorId) {
        doctorCounter = Math.max(doctorCounter, extractNumber(doctorId, "DOC"));
    }

    public static synchronized void syncAppointmentCounter(String appointmentId) {
        appointmentCounter = Math.max(appointmentCounter, extractNumber(appointmentId, "APPT"));
    }

    private static int extractNumber(String id, String prefix) {
        if (id == null || !id.startsWith(prefix)) {
            return 0;
        }
        return Integer.parseInt(id.substring(prefix.length()));
    }
}
