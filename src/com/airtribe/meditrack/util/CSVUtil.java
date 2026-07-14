package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.AppointmentStatus;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.entity.Specialization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {

    private static final String PATIENT_HEADER =
            "patientId,name,age,gender,contactNumber,email,bloodGroup,emergencyContact,address";
    private static final String DOCTOR_HEADER =
            "doctorId,name,age,gender,contactNumber,email,specialization,yearsOfExperience,consultationRate";
    private static final String APPOINTMENT_HEADER =
            "appointmentId,appointmentDateTime,status,reasonOfVisit,patientId,doctorId";

    private CSVUtil() {
    }

    public static void savePatients(List<Patient> patients, String filePath) {
        ensureParentDirectory(filePath);
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println(PATIENT_HEADER);
            for (Patient patient : patients) {
                writer.println(toPatientCsv(patient));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save patients to " + filePath, e);
        }
    }

    public static List<Patient> loadPatients(String filePath) {
        List<Patient> patients = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return patients;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    patients.add(fromPatientCsv(line.split(",")));
                }
            }
            return patients;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load patients from " + filePath, e);
        }
    }

    public static void saveDoctors(List<Doctor> doctors, String filePath) {
        ensureParentDirectory(filePath);
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println(DOCTOR_HEADER);
            for (Doctor doctor : doctors) {
                writer.println(toDoctorCsv(doctor));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save doctors to " + filePath, e);
        }
    }

    public static List<Doctor> loadDoctors(String filePath) {
        List<Doctor> doctors = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return doctors;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    doctors.add(fromDoctorCsv(line.split(",")));
                }
            }
            return doctors;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load doctors from " + filePath, e);
        }
    }

    public static void saveAppointments(List<Appointment> appointments, String filePath) {
        ensureParentDirectory(filePath);
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println(APPOINTMENT_HEADER);
            for (Appointment appointment : appointments) {
                writer.println(toAppointmentCsv(appointment));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save appointments to " + filePath, e);
        }
    }

    public static List<Appointment> loadAppointments(String filePath) {
        List<Appointment> appointments = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return appointments;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    appointments.add(fromAppointmentCsv(line.split(",")));
                }
            }
            return appointments;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load appointments from " + filePath, e);
        }
    }

    private static String toPatientCsv(Patient patient) {
        return String.join(",",
                patient.getPatientId(),
                patient.getName(),
                String.valueOf(patient.getAge()),
                patient.getGender(),
                patient.getContactNumber(),
                patient.getEmail(),
                patient.getBloodGroup(),
                patient.getEmergencyContact(),
                patient.getAddress());
    }

    private static Patient fromPatientCsv(String[] values) {
        requireColumnCount(values, 9, "patient");
        Patient patient = new Patient(
                values[0],
                values[1],
                Integer.parseInt(values[2]),
                values[3],
                values[4],
                values[5],
                values[6],
                values[7],
                values[8]);
        IdGenerator.syncPatientCounter(patient.getPatientId());
        return patient;
    }

    private static String toDoctorCsv(Doctor doctor) {
        return String.join(",",
                doctor.getDoctorId(),
                doctor.getName(),
                String.valueOf(doctor.getAge()),
                doctor.getGender(),
                doctor.getContactNumber(),
                doctor.getEmail(),
                doctor.getSpecialization().name(),
                String.valueOf(doctor.getYearsOfExperience()),
                String.valueOf(doctor.getConsultationRate()));
    }

    private static Doctor fromDoctorCsv(String[] values) {
        requireColumnCount(values, 9, "doctor");
        Doctor doctor = new Doctor(
                values[0],
                values[1],
                Integer.parseInt(values[2]),
                values[3],
                values[4],
                values[5],
                Specialization.valueOf(values[6]),
                Integer.parseInt(values[7]),
                Double.parseDouble(values[8]));
        IdGenerator.syncDoctorCounter(doctor.getDoctorId());
        return doctor;
    }

    private static String toAppointmentCsv(Appointment appointment) {
        return String.join(",",
                appointment.getAppointmentId(),
                String.valueOf(appointment.getAppointmentDateTime()),
                appointment.getAppointmentStatus().name(),
                appointment.getReasonOfVisit(),
                appointment.getPatientId(),
                appointment.getDoctorId());
    }

    private static Appointment fromAppointmentCsv(String[] values) {
        requireColumnCount(values, 6, "appointment");
        Appointment appointment = new Appointment(
                values[0],
                Long.parseLong(values[1]),
                AppointmentStatus.valueOf(values[2]),
                values[3],
                values[4],
                values[5]);
        IdGenerator.syncAppointmentCounter(appointment.getAppointmentId());
        return appointment;
    }

    private static void ensureParentDirectory(String filePath) {
        File parent = new File(filePath).getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IllegalStateException("Unable to create data directory: " + parent.getPath());
        }
    }

    private static void requireColumnCount(String[] values, int expected, String rowType) {
        if (values.length != expected) {
            throw new IllegalArgumentException("Invalid " + rowType + " CSV row. Expected " + expected
                    + " columns but got " + values.length);
        }
    }
}
