package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interfaces.Searchable;
import com.airtribe.meditrack.util.IdGenerator;

import java.util.List;

public class Doctor extends Person implements Searchable {

    //private String doctorId;
    private Specialization specialization;
    private int yearsOfExperience;
    private List<String> availability;
    private double consultationRate;

    public Doctor(String name, int age, String gender, String contactNumber, String email, Specialization specialization, int yearsOfExperience, double consultationRate) {
        super(IdGenerator.generateDoctorId(), name, age, gender, contactNumber, email);
        this.specialization = specialization;
        this.yearsOfExperience = yearsOfExperience;
        this.consultationRate = consultationRate;
    }

    @Override
    public String getEntityType() {
        return "Doctor";
    }

    public String getDoctorId() {
        return getId();
    }

    @Override
    public boolean matchesSearchCriteria(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return false;
        }

        String normalizedKeyword = keyword.toLowerCase();
        return getDoctorId().toLowerCase().contains(normalizedKeyword)
                || getName().toLowerCase().contains(normalizedKeyword)
                || specialization.name().toLowerCase().contains(normalizedKeyword)
                || containsIgnoreCase(getContactNumber(), normalizedKeyword)
                || containsIgnoreCase(getEmail(), normalizedKeyword);
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public List<String> getAvailability() {
        return availability;
    }

    public void setAvailability(List<String> availability) {
        this.availability = availability;
    }

    public double getConsultationRate() {
        return consultationRate;
    }

    public void setConsultationRate(double consultationRate) {
        this.consultationRate = consultationRate;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId='" + getDoctorId() + '\'' +
                ", specialization=" + specialization +
                ", yearsOfExperience=" + yearsOfExperience +
                ", availability=" + availability +
                ", consultationRate=" + consultationRate +
                '}';
    }

    private boolean containsIgnoreCase(String value, String normalizedKeyword) {
        return value != null && value.toLowerCase().contains(normalizedKeyword);
    }
}
