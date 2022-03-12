package com.shop.management.Model;

public class Sale_Main {
    private int sale_main_id , duesId, customerId, sellerId;
    private String customerName, customerPhone, customerAddress;
    private double additionalDisc, receivedAmount, totalDiscountAmount, totalTaxAmount, netAmount;
    private String paymentMode, billType, invoiceNumber;
    private String sellerName, sellingDate;
    private int totalItems;
    private double duesAmount;

    public Sale_Main(int sale_main_id,int duesId , int customerId, int sellerId, String customerName, String customerPhone,
                     String customerAddress, double additionalDisc, double receivedAmount, double totalDiscountAmount,
                     double totalTaxAmount, double netAmount, String paymentMode, String billType,
                     String invoiceNumber, String sellerName, String sellingDate , int totalItems , double duesAmount) {
        this.sale_main_id = sale_main_id;
        this.duesId = duesId;
        this.customerId = customerId;
        this.sellerId = sellerId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.additionalDisc = additionalDisc;
        this.receivedAmount = receivedAmount;
        this.totalDiscountAmount = totalDiscountAmount;
        this.totalTaxAmount = totalTaxAmount;
        this.netAmount = netAmount;
        this.paymentMode = paymentMode;
        this.billType = billType;
        this.invoiceNumber = invoiceNumber;
        this.sellerName = sellerName;
        this.sellingDate = sellingDate;
        this.totalItems = totalItems;
        this.duesAmount = duesAmount;
    }

    public int getDuesId() {
        return duesId;
    }

    public void setDuesId(int duesId) {
        this.duesId = duesId;
    }

    public double getDuesAmount() {
        return duesAmount;
    }

    public void setDuesAmount(double duesAmount) {
        this.duesAmount = duesAmount;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getSale_main_id() {
        return sale_main_id;
    }

    public void setSale_main_id(int sale_main_id) {
        this.sale_main_id = sale_main_id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public double getAdditionalDisc() {
        return additionalDisc;
    }

    public void setAdditionalDisc(double additionalDisc) {
        this.additionalDisc = additionalDisc;
    }

    public double getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(double receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public double getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(double totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public double getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellingDate() {
        return sellingDate;
    }

    public void setSellingDate(String sellingDate) {
        this.sellingDate = sellingDate;
    }
}
