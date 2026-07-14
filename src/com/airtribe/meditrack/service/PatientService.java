package com.airtribe.meditrack.service;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.PatientNotFoundException;
import com.airtribe.meditrack.util.CSVUtil;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.Validator;

import java.util.ArrayList;
import java.util.List;

public class PatientService {
    private DataStore<Patient> patientStore;
    private boolean persistenceEnabled;

    public PatientService(){
        this(true);
    }

    public PatientService(boolean persistenceEnabled){
        this.patientStore = new DataStore<Patient>();
        this.persistenceEnabled = persistenceEnabled;
    }

    //CRUD OPERATION
    public void registerPatient(Patient patient){
        Validator.validPatient(patient);
        patientStore.add(patient.getPatientId(), patient);
        savePatients();
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
        savePatients();
    }

    public void deletePatient(String patientId){
        patientStore.remove(patientId);
        savePatients();
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

    public List<Patient> searchPatientsByKeyword(String keyword) {
        List<Patient> results = new ArrayList<>();
        for (Patient patient : patientStore.getAll()) {
            if (patient.matchesSearchCriteria(keyword)) {
                results.add(patient);
            }
        }
        return results;
    }

    public void loadPatients(List<Patient> patients) {
        for (Patient patient : patients) {
            Validator.validPatient(patient);
            patientStore.add(patient.getPatientId(), patient);
        }
    }

    public void loadPatientsFromCsv() {
        loadPatients(CSVUtil.loadPatients(Constants.PATIENTS_FILE));
    }

    public void savePatients() {
        if (!persistenceEnabled) {
            return;
        }
        CSVUtil.savePatients(getAllPatients(), Constants.PATIENTS_FILE);
    }
}
