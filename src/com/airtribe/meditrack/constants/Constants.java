package com.airtribe.meditrack.constants;

public class Constants {

    public static final double TAX_RATE = 0.18;

    public static final int MAX_APPOINTMENTS_PER_DAY = 20;
    public static final int APPOINTMENT_DURATION_MINUTES = 30;

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

    public static final String DATA_DIR = "data";
    public static final String PATIENTS_FILE = DATA_DIR + "/patients.csv";
    public static final String DOCTORS_FILE = DATA_DIR + "/doctors.csv";
    public static final String APPOINTMENTS_FILE = DATA_DIR + "/appointments.csv";

    private Constants(){
        // private constructor to prevent instantiations
    }
}
