package com.airtribe.meditrack.observer;

import com.airtribe.meditrack.entity.Appointment;

public class ConsoleAppointmentNotifier implements AppointmentObserver {

    @Override
    public void onAppointmentCreated(Appointment appointment) {
        System.out.println("[Notification] Appointment created: " + appointment.getAppointmentId());
    }

    @Override
    public void onAppointmentConfirmed(Appointment appointment) {
        System.out.println("[Notification] Appointment confirmed: " + appointment.getAppointmentId());
    }

    @Override
    public void onAppointmentCancelled(Appointment appointment) {
        System.out.println("[Notification] Appointment cancelled: " + appointment.getAppointmentId());
    }
}
