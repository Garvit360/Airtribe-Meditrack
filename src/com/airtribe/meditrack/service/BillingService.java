package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.exception.BillNotFoundException;
import com.airtribe.meditrack.util.DataStore;

public class BillingService {
    private DataStore<Bill> billStore;
    private AppointmentService appointmentService;

    public BillingService(AppointmentService appointmentService){
        this.billStore = new DataStore<>();
        this.appointmentService = appointmentService;
    }

    public Bill generateBill(String appointmentId, double consultationCharge){
        Appointment appointment = appointmentService.getAppointment(appointmentId);
        Bill bill = new Bill(appointmentId, appointment.getPatientId(), consultationCharge);
        billStore.add(bill.getBillId(), bill);
        return bill;
    }

    public Bill generateBill(Appointment appointment, Doctor doctor){
        return generateBill(appointment.getAppointmentId(), doctor.getConsultationRate());
    }

    public void addMedicationCharges(String billId, double amount){
        Bill bill = getRequireBill(billId);
        bill.addCharges(amount);
        billStore.update(billId, bill);
    }

    public void addLabCharges(String billId, double amount){
        Bill bill = getRequireBill(billId);
        bill.addCharges(0, amount);
        billStore.update(billId, bill);
    }

    public void processPayment(String billId){
        Bill bill = getRequireBill(billId);
        bill.processPayment();
        billStore.update(billId, bill);
    }

    public Bill getBill(String billId){
        return billStore.findById(billId);
    }

    private Bill getRequireBill(String billId) throws BillNotFoundException {
        Bill bill = billStore.findById(billId);
        if(bill == null){
            throw new BillNotFoundException("Bill Not Found: " + billId);
        }
        return bill;
    }
}
