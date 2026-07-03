package com.airtribe.meditrack.interfaces;

import com.airtribe.meditrack.entity.BillStatus;

public interface Payable {

    double calculateAmount();
    void processPayment();
    BillStatus isPaymentComplete();

    default void printReceipt(){
        System.out.println("Receipt:");
        System.out.println("Amount: $" + calculateAmount());
        System.out.println("Status: " + isPaymentComplete());
    }
}
