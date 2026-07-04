package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.exception.DoctorNotFoundException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.Validator;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

public class DoctorService {
    private DataStore<Doctor> doctorStore;
    public DoctorService(){
        this.doctorStore = new DataStore<Doctor>();
    }

    //CRUD OPERATION
    public void registerDoctor(Doctor doctor){
        Validator.validDoctor(doctor);
        doctorStore.add(doctor.getDoctorId(), doctor);
    }

    public Doctor getDoctor(String doctorId) throws DoctorNotFoundException {
        Doctor doctor = doctorStore.findById(doctorId);
        if (doctor == null){
            throw new DoctorNotFoundException("Doctor Not Found: " + doctorId);
        }
        return doctor;
    }

    public void updateDoctor(Doctor doctor){
        Validator.validDoctor(doctor);
        doctorStore.update(doctor.getDoctorId(), doctor);
    }

    public void deleteDoctor(String doctorId){
        doctorStore.remove(doctorId);
    }

    public List<Doctor> getAllDoctors(){
        return new ArrayList<>(doctorStore.getAll());
    }

    public Doctor searchDoctor(String doctorId){
        return doctorStore.findById(doctorId);
    }

    public List<Doctor> searchDoctor(int experience){
        List<Doctor> results = new ArrayList<>();
        for (Doctor d: doctorStore.getAll()){
            if(d.getYearsOfExperience() == experience){
                results.add(d);
            }
        }
        return results;
    }

    public List<Doctor> searchDoctor(Specialization specialization){
        List<Doctor> results = new ArrayList<>();
        for (Doctor d: doctorStore.getAll()){
            if(d.getSpecialization() == specialization){
                results.add(d);
            }
        }
        return results;
    }

    public List<Doctor> searchDoctor(String name, boolean exactMatch){
        List<Doctor> results = new ArrayList<>();
        for(Doctor d: doctorStore.getAll()){
            if(exactMatch){
                if(d.getName().equalsIgnoreCase(name)){
                    results.add(d);
                }
            } else {
                if(d.getName().toLowerCase().contains(name.toLowerCase())){
                    results.add(d);
                }
            }
        }
        return results;
    }
}
