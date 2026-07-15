package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.Doctor;

public class BillFactory {

    private BillFactory(){
    }

    public static Bill createConsultationBill(Appointment appointment, Doctor doctor){
        return new Bill(
                appointment.getAppointmentId(),
                appointment.getPatientId(),
                doctor.getConsultationRate()
        );
    }

    public static Bill createManualBill(String appointmentId, String patientId, double consultationCharges){
        return new Bill(appointmentId, patientId,consultationCharges);
    }
}
