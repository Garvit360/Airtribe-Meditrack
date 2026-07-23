package com.airtribe.meditrack.entity;

public final class BillSummary{

    private final String billId;
    private final String patientId;
    private final double subTotal;
    private final double totalAmount;
    private final double tax;
    private final BillStatus billStatus;
    private final long generatedTimeStamp;

    public BillSummary(String billId, String patientId, double subTotal, double totalAmount, double tax, BillStatus billStatus) {
        this.billId = billId;
        this.patientId = patientId;
        this.subTotal = subTotal;
        this.totalAmount = totalAmount;
        this.tax = tax;
        this.billStatus = billStatus;
        this.generatedTimeStamp = System.currentTimeMillis();
    }

    public String getBillId() {
        return billId;
    }

    public String getPatientId() {
        return patientId;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getTax() {
        return tax;
    }

    public BillStatus getBillStatus() {
        return billStatus;
    }

    public long getGeneratedTimeStamp() {
        return generatedTimeStamp;
    }

    @Override
    public String toString() {
        return "BillSummary{" +
                "billId='" + billId + '\'' +
                ", patient=" + patientId +
                ", tax=" + tax +
                ", billStatus=" + billStatus +
                '}';
    }

}
