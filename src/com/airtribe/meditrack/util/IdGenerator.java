package com.airtribe.meditrack.util;

public class IdGenerator {
    private static final IdGenerator INSTANCE = new IdGenerator();

    private int patientCounter = 1000;
    private int doctorCounter = 2000;
    private int appointmentCounter = 3000;
    private int billCounter = 4000;

    private IdGenerator(){
    }

    public static IdGenerator getInstance(){
        return INSTANCE;
    }

    public synchronized String generatePatientId(){
        return "PAT" + (++patientCounter);
    }

    public synchronized String generateDoctorId(){
        return "DOC" + (++doctorCounter);
    }

    public synchronized String generateAppointmentId(){
        return "APPT" + (++appointmentCounter);
    }

    public synchronized String generateBillId(){
        return "BILL" + (++billCounter);
    }

    public synchronized void syncPatientCounter(String patientId) {
        patientCounter = Math.max(patientCounter, extractNumber(patientId, "PAT"));
    }

    public synchronized void syncDoctorCounter(String doctorId) {
        doctorCounter = Math.max(doctorCounter, extractNumber(doctorId, "DOC"));
    }

    public synchronized void syncAppointmentCounter(String appointmentId) {
        appointmentCounter = Math.max(appointmentCounter, extractNumber(appointmentId, "APPT"));
    }

    private static int extractNumber(String id, String prefix) {
        if (id == null || !id.startsWith(prefix)) {
            return 0;
        }
        return Integer.parseInt(id.substring(prefix.length()));
    }
}
