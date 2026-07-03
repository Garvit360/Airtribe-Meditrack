package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.util.IdGenerator;

import java.util.List;

public class Doctor extends Person{

    private String doctorId;
    private Specialization specialization;
    private int yearsOfExperience;
    private List<String> availability;
    private double consultationRate;

    public Doctor(String name, int age, String gender, String contactNumber, String email, Specialization specialization, int yearsOfExperience, double consultationRate) {
        super(name, age, gender, contactNumber, email);
        this.doctorId = IdGenerator.generateDoctorId();
        this.specialization = specialization;
        this.yearsOfExperience = yearsOfExperience;
        this.consultationRate = consultationRate;
    }

    public String getDoctorId() {
        return doctorId;
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
                "doctorId='" + doctorId + '\'' +
                ", specialization=" + specialization +
                ", yearsOfExperience=" + yearsOfExperience +
                ", availability=" + availability +
                ", consultationRate=" + consultationRate +
                '}';
    }
}
