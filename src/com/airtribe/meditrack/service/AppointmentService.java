package com.airtribe.meditrack.service;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.AppointmentStatus;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.observer.AppointmentObserver;
import com.airtribe.meditrack.util.CSVUtil;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class AppointmentService {
    private DataStore<Appointment> appointmentStore;
    private DoctorService doctorService;
    private PatientService patientService;
    private boolean persistenceEnabled;
    private List<AppointmentObserver> observers = new ArrayList<>();

    public AppointmentService(PatientService patientService, DoctorService doctorService){
        this(patientService, doctorService, true);
    }

    public AppointmentService(PatientService patientService, DoctorService doctorService, boolean persistenceEnabled){
        this.appointmentStore = new DataStore<>();
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.persistenceEnabled = persistenceEnabled;
    }

    public Appointment createAppointment(String doctorId, String patientId, Long dateTime, String reason){

        Patient patient = patientService.getPatient(patientId);
        Doctor doctor = doctorService.getDoctor(doctorId);
        assertDoctorSlotFree(doctorId, dateTime);

        Appointment appointment = new Appointment(dateTime, reason, patientId, doctorId);
        appointmentStore.add(appointment.getAppointmentId(), appointment);
        saveAppointments();
        notifyAppointmentCreated(appointment);
        return appointment;
    }

    private void assertDoctorSlotFree(String doctorId, long startMillis) {
        long durationMs = Constants.APPOINTMENT_DURATION_MINUTES * 60L * 1000L;
        int sameDayCount = 0;
        for (Appointment existing : appointmentStore.getAll()) {
            if (!existing.getDoctorId().equals(doctorId)) {
                continue;
            }
            if (existing.getAppointmentStatus() == AppointmentStatus.CANCELLED) {
                continue;
            }
            if (DateUtil.sameCalendarDay(existing.getAppointmentDateTime(), startMillis)) {
                sameDayCount++;
            }
            if (DateUtil.slotsOverlap(existing.getAppointmentDateTime(), startMillis, durationMs)) {
                throw new InvalidDataException(
                        "Doctor " + doctorId + " already has an appointment in this time slot");
            }
        }
        if (sameDayCount >= Constants.MAX_APPOINTMENTS_PER_DAY) {
            throw new InvalidDataException(
                    "Doctor " + doctorId + " has reached the daily appointment limit");
        }
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
        saveAppointments();
        notifyAppointmentCancelled(appointment);
    }

    public void confirmAppointment(String appointmentId){
        Appointment appointment = getAppointment(appointmentId);
        appointment.confirmAppointment();
        appointmentStore.update(appointmentId, appointment);
        saveAppointments();
        notifyAppointmentConfirmed(appointment);
    }

    public boolean hasAppointmentForPatient(String patientId){
        for (Appointment appointment : appointmentStore.getAll()){
            if (appointment.getPatientId().equals(patientId)){
                return true;
            }
        }
        return false;
    }

    public boolean hasAppointmentForDoctor(String doctorId){
        for (Appointment appointment : appointmentStore.getAll()){
            if (appointment.getDoctorId().equals(doctorId)){
                return true;
            }
        }
        return false;
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

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointmentStore.getAll());
    }

    public void loadAppointments(List<Appointment> appointments) {
        for (Appointment appointment : appointments) {
            patientService.getPatient(appointment.getPatientId());
            doctorService.getDoctor(appointment.getDoctorId());
            appointmentStore.add(appointment.getAppointmentId(), appointment);
        }
    }

    public void loadAppointmentsFromCsv() {
        loadAppointments(CSVUtil.loadAppointments(Constants.APPOINTMENTS_FILE));
    }

    public void saveAppointments() {
        if (!persistenceEnabled) {
            return;
        }
        CSVUtil.saveAppointments(getAllAppointments(), Constants.APPOINTMENTS_FILE);
    }

    public void addObserver(AppointmentObserver observer){
        observers.add(observer);
    }

    public void removeObserver(AppointmentObserver observer){
        observers.remove(observer);
    }

    private void notifyAppointmentCreated(Appointment appointment){
        for (AppointmentObserver observer: observers){
            observer.onAppointmentCreated(appointment);
        }
    }

    private void notifyAppointmentConfirmed(Appointment appointment){
        for (AppointmentObserver observer: observers){
            observer.onAppointmentConfirmed(appointment);
        }
    }

    private void notifyAppointmentCancelled(Appointment appointment){
        for (AppointmentObserver observer: observers){
            observer.onAppointmentCancelled(appointment);
        }
    }
}
