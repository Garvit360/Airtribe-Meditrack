package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.util.DataStore;

import java.util.ArrayList;
import java.util.List;

public class AppointmentService {
    private DataStore<Appointment> appointmentStore;
    private DoctorService doctorService;
    private PatientService patientService;

    public AppointmentService(PatientService patientService, DoctorService doctorService){
        this.appointmentStore = new DataStore<>();
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    public Appointment createAppointment(String doctorId, String patientId, Long dateTime, String reason){

        Patient patient = patientService.getPatient(patientId);
        Doctor doctor = doctorService.getDoctor(doctorId);

        Appointment appointment = new Appointment(dateTime, reason, patientId, doctorId);
        appointmentStore.add(appointment.getAppointmentId(), appointment);
        return appointment;
    }

    public Appointment bookAppointment(String patientId, String doctorId, Long dateTime, String reason){
        return createAppointment(doctorId, patientId, dateTime, reason);
    }

    public Appointment getAppointment(String appointmentId) throws AppointmentNotFoundException {
        Appointment appointment = appointmentStore.findById(appointmentId);
        if(appointment == null){
            throw new AppointmentNotFoundException("Appointment Not Found:" +appointmentId);
        }
        return appointment;
    }

    public void cancelAppointment(String appointmentId){
        Appointment appointment = getAppointment(appointmentId);
        appointment.cancelAppointment();
        appointmentStore.update(appointmentId, appointment);
    }

    public void confirmAppointment(String appointmentId){
        Appointment appointment = getAppointment(appointmentId);
        appointment.confirmAppointment();
        appointmentStore.update(appointmentId, appointment);
    }

    public List<Appointment> getPatientAppointment(String patientId){
        List<Appointment> results = new ArrayList<>();
        for (Appointment apt: appointmentStore.getAll()){
            if (apt.getPatientId().equals(patientId)){
                results.add(apt);
            }
        }
        return results;
    }

    public List<Appointment> getPatientAppointments(String patientId){
        return getPatientAppointment(patientId);
    }

}
