package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.util.IdGenerator;

import java.time.LocalDateTime;

public class Appointment {

    private String appointmentId;
    private Long appointmentDateTime;
    private AppointmentStatus appointmentStatus;
    private String reasonOfVisit;
    private String patientId;
    private String doctorId;

    public Appointment(Long appointmentDateTime, String reasonOfVisit, String patientId, String doctorId) {
        this.appointmentId = IdGenerator.generateAppointmentId();
        this.appointmentDateTime = appointmentDateTime;
        this.reasonOfVisit = reasonOfVisit;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentStatus = AppointmentStatus.PENDING;
    }

    public void confirmAppointment(){
        this.appointmentStatus = AppointmentStatus.CONFIRMED;
    }

    public void cancelAppointment(){
        this.appointmentStatus = AppointmentStatus.CANCELLED;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public Long getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(Long appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getReasonOfVisit() {
        return reasonOfVisit;
    }

    public void setReasonOfVisit(String reasonOfVisit) {
        this.reasonOfVisit = reasonOfVisit;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId='" + appointmentId + '\'' +
                ", appointmentDateTime=" + appointmentDateTime +
                ", appointmentStatus=" + appointmentStatus +
                ", reasonOfVisit='" + reasonOfVisit + '\'' +
                ", patient=" + patientId +
                ", doctor=" + doctorId +
                '}';
    }
}
