package com.airtribe.meditrack.observer;

import com.airtribe.meditrack.entity.Appointment;

public interface AppointmentObserver {

    void onAppointmentCreated(Appointment appointment);

    void onAppointmentConfirmed(Appointment appointment);

    void onAppointmentCancelled(Appointment appointment);
}
