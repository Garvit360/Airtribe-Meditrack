package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.InvalidDataException;

public class Validator {

    public static boolean isValidAge(int age) {
        return age > 0 && age < 100;
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isValidContactNumber(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    public static boolean isValidFee(double fee) {
        return fee > 0;
    }

    public static void validPatient(Patient patient) throws InvalidDataException {
        if (patient.getName() == null || patient.getName().trim().isEmpty()) {
            throw new InvalidDataException("Patient Name can not be empty");
        }

        if (!isValidAge(patient.getAge())) {
            throw new InvalidDataException("Please insert correct and valid age.");
        }

        if (!isValidEmail(patient.getEmail())) {
            throw new InvalidDataException("Please insert correct email.");
        }

        if (!isValidContactNumber(patient.getContactNumber())) {
            throw new InvalidDataException("Please insert correct phone.");
        }
    }

    public static void validDoctor(Doctor doctor) throws InvalidDataException {
        if (doctor.getName() == null || doctor.getName().trim().isEmpty()) {
            throw new InvalidDataException("Doctor name can not be empty");
        }

        if (!isValidAge(doctor.getAge())) {
            throw new InvalidDataException("Please insert correct and valid age.");
        }

        if (!isValidEmail(doctor.getEmail())) {
            throw new InvalidDataException("Please insert correct email.");
        }

        if (!isValidContactNumber(doctor.getContactNumber())) {
            throw new InvalidDataException("Please insert correct phone.");
        }
    }
}
