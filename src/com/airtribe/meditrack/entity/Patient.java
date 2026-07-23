package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interfaces.Searchable;
import com.airtribe.meditrack.util.IdGenerator;

import java.util.Arrays;

public class Patient extends Person implements Searchable {

    //private String patientId;
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
        this(IdGenerator.getInstance().generatePatientId(), name, age, gender, contactNumber, email, bloodGroup,
                emergencyContact, address);
    }

    public Patient(String patientId, String name, int age, String gender, String contactNumber, String email,
                   String bloodGroup, String emergencyContact, String address) {
        super(patientId, name, age, gender, contactNumber, email);
        this.bloodGroup = bloodGroup;
        this.emergencyContact = emergencyContact;
        this.address = address;
    }

    @Override
    public String getEntityType() {
        return "Patient";
    }

    public String getPatientId() {
        return getId();
    }

    @Override
    public boolean matchesSearchCriteria(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return false;
        }

        String normalizedKeyword = keyword.toLowerCase();
        return getPatientId().toLowerCase().contains(normalizedKeyword)
                || getName().toLowerCase().contains(normalizedKeyword)
                || containsIgnoreCase(bloodGroup, normalizedKeyword)
                || containsIgnoreCase(getContactNumber(), normalizedKeyword)
                || containsIgnoreCase(getEmail(), normalizedKeyword);
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
                "patientId='" + getPatientId() + '\'' +
                ", medicalHistory=" + Arrays.toString(medicalHistory) +
                ", allergies=" + Arrays.toString(allergies) +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", emergencyContact='" + emergencyContact + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public Patient clone() {
        try {
            Patient clone = (Patient) super.clone();
            clone.medicalHistory = this.medicalHistory.clone();
            clone.allergies = this.allergies.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Patient Cloning failed", e);
        }
    }

    private boolean containsIgnoreCase(String value, String normalizedKeyword) {
        return value != null && value.toLowerCase().contains(normalizedKeyword);
    }
}
