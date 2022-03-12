package com.shop.management.Model;

public class Dues {

    private int dues_id , customerId , sale_main_id;
    private double duesAmount;
    private String dues_notes , paymentMode , invoiceNumber , lastPayment;

    public Dues(int dues_id, int customerId, int sale_main_id, double duesAmount, String dues_notes, String paymentMode, String invoiceNumber, String lastPayment) {
        this.dues_id = dues_id;
        this.customerId = customerId;
        this.sale_main_id = sale_main_id;
        this.duesAmount = duesAmount;
        this.dues_notes = dues_notes;
        this.paymentMode = paymentMode;
        this.invoiceNumber = invoiceNumber;
        this.lastPayment = lastPayment;
    }

    public int getDues_id() {
        return dues_id;
    }

    public void setDues_id(int dues_id) {
        this.dues_id = dues_id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getSale_main_id() {
        return sale_main_id;
    }

    public void setSale_main_id(int sale_main_id) {
        this.sale_main_id = sale_main_id;
    }

    public double getDuesAmount() {
        return duesAmount;
    }

    public void setDuesAmount(double duesAmount) {
        this.duesAmount = duesAmount;
    }

    public String getDues_notes() {
        return dues_notes;
    }

    public void setDues_notes(String dues_notes) {
        this.dues_notes = dues_notes;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(String lastPayment) {
        this.lastPayment = lastPayment;
    }
}
