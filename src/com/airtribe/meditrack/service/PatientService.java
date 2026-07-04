package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.PatientNotFoundException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.Validator;

import java.util.ArrayList;
import java.util.List;

public class PatientService {
    private DataStore<Patient> patientStore;

    public PatientService(){
        this.patientStore = new DataStore<Patient>();
    }

    //CRUD OPERATION
    public void registerPatient(Patient patient){
        Validator.validPatient(patient);
        patientStore.add(patient.getPatientId(), patient);
    }

    public Patient getPatient(String patientId) throws PatientNotFoundException {
        Patient patient = patientStore.findById(patientId);
        if (patient == null){
            throw new PatientNotFoundException("Patient not found: " +patientId);
        }
        return patient;
    }
    public void updatePatient(Patient patient){
        Validator.validPatient(patient);
        patientStore.update(patient.getPatientId(), patient);
    }

    public void deletePatient(String patientId){
        patientStore.remove(patientId);
    }

    public List<Patient> getAllPatient(){
        return new ArrayList<>(patientStore.getAll());
    }

    public List<Patient> getAllPatients(){
        return getAllPatient();
    }

    public Patient searchPatient(String patientId){
        return patientStore.findById(patientId);
    }

    public List<Patient> searchPatient(int age){
        List<Patient> results = new ArrayList<>();
        for(Patient p: patientStore.getAll()){
            if(p.getAge() == age){
                results.add(p);
            }
        }
        return results;
    }

    public List<Patient> searchPatient(String name, boolean exactMatch){
        List<Patient> results = new ArrayList<>();
        for(Patient p: patientStore.getAll()){
            if(exactMatch){
                if(p.getName().equalsIgnoreCase(name)){
                    results.add(p);
                }
            } else {
                if (p.getName().toLowerCase().contains(name.toLowerCase()))
                    results.add(p);
            }
        }
        return results;
    }
}
