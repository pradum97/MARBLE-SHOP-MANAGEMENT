package com.shop.management.Model;

public class DuesHistoryModel {

    private int dues_historyId , duesId , customer_id , sale_mainId;
    private double previousDues , paidAmount , currentDues;
    private String paymentMode , paymentDate;

    public DuesHistoryModel(int dues_historyId, int duesId, int customer_id, int sale_mainId, double previousDues,
                            double paidAmount, double currentDues, String paymentMode, String paymentDate) {
        this.dues_historyId = dues_historyId;
        this.duesId = duesId;
        this.customer_id = customer_id;
        this.sale_mainId = sale_mainId;
        this.previousDues = previousDues;
        this.paidAmount = paidAmount;
        this.currentDues = currentDues;
        this.paymentMode = paymentMode;
        this.paymentDate = paymentDate;
    }

    public int getDues_historyId() {
        return dues_historyId;
    }

    public void setDues_historyId(int dues_historyId) {
        this.dues_historyId = dues_historyId;
    }

    public int getDuesId() {
        return duesId;
    }

    public void setDuesId(int duesId) {
        this.duesId = duesId;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getSale_mainId() {
        return sale_mainId;
    }

    public void setSale_mainId(int sale_mainId) {
        this.sale_mainId = sale_mainId;
    }

    public double getPreviousDues() {
        return previousDues;
    }

    public void setPreviousDues(double previousDues) {
        this.previousDues = previousDues;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public double getCurrentDues() {
        return currentDues;
    }

    public void setCurrentDues(double currentDues) {
        this.currentDues = currentDues;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
}
