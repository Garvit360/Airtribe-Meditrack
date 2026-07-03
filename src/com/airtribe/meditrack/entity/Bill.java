package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.interfaces.Payable;
import com.airtribe.meditrack.util.IdGenerator;

public class Bill implements Payable {

     private String billId;
     private String appointmentId;
     private String patientId;
     private double consultationCharge;
     private double medicationCharges;
     private double labCharges;
     private double subTotal;
     private double tax;
     private double totalAmount;
     private BillStatus billStatus;

     public Bill(String appointmentId, String patientId, double consultationCharge) {
          this.billId = IdGenerator.generateBillId();
          this.appointmentId = appointmentId;
          this.patientId = patientId;
          this.consultationCharge = consultationCharge;
          this.medicationCharges = 0.0;
          this.labCharges = 0.0;
          this.billStatus = BillStatus.UNPAID;

     }

     private void calculateTotal(){
          this.subTotal = consultationCharge + medicationCharges + labCharges;
          this.tax = Constants.TAX_RATE * this.subTotal;
          this.totalAmount = this.subTotal + this.tax;
     }

     @Override
     public double calculateAmount(){
          calculateTotal();
          return totalAmount;
     }

     @Override
     public void processPayment() {
          this.billStatus = BillStatus.PAID;
          System.out.println("Payment processed for Bill Id: " + this.billId);
     }

     @Override
     public BillStatus isPaymentComplete() {
          return this.billStatus;
     }

     public void addCharges( double medication, double labCharges){
          this.medicationCharges += medication;
          this.labCharges += labCharges;
          calculateTotal();
     }

     public void addCharges(double medication){
          this.medicationCharges += medication;
          calculateTotal();
     }

     public BillSummary generateSummary() {
          calculateTotal();
          return new BillSummary(billId, patientId, subTotal, totalAmount, tax, billStatus);
     }

     public String getBillId() {
          return billId;
     }

     public String getAppointmentId() {
          return appointmentId;
     }

     public String getPatient() {
          return patientId;
     }

     public double getConsultationCharge() {
          return consultationCharge;
     }

     public double getMedicationCharges() {
          return medicationCharges;
     }

     public double getLabCharges() {
          return labCharges;
     }

     public double getSubTotal() {
          calculateTotal();
          return subTotal;
     }

     public double getTax() {
          calculateTotal();
          return tax;
     }

     public double getTotalAmount() {
          calculateTotal();
          return totalAmount;
     }

     public BillStatus getBillStatus() {
          return billStatus;
     }
}
