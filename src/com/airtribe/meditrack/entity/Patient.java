package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.util.IdGenerator;

import java.util.Arrays;

public class Patient extends Person{

    private String patientId;
    private String[] medicalHistory;
    private String[] allergies;
    private String bloodGroup;
    private String emergencyContact;
    private String address;

    {
        this.medicalHistory = new String[50];
        this.allergies = new String[10];
    }

    public Patient(String name, int age, String gender, String contactNumber, String email, String bloodGroup, String emergencyContact, String address) {
        super(name, age, gender, contactNumber, email);
        this.patientId = IdGenerator.generatePatientId();
        this.bloodGroup = bloodGroup;
        this.emergencyContact = emergencyContact;
        this.address = address;
    }

    public String getPatientId() {
        return patientId;
    }

    public String[] getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String condition) {
        for(int i=0; i< medicalHistory.length; i++){
            if(medicalHistory[i] == null){
                medicalHistory[i] = condition;
                break;
            }
        }
    }

    public String[] getAllergies() {
        return allergies;
    }

    public void setAllergies(String[] allergies) {
        this.allergies = allergies;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId='" + patientId + '\'' +
                ", medicalHistory=" + Arrays.toString(medicalHistory) +
                ", allergies=" + Arrays.toString(allergies) +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", emergencyContact='" + emergencyContact + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
