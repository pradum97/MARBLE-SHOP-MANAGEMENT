package com.shop.management.Model;

public class GstInvoiceModel {

    private String productName , fullQuantity , discountName;
    private double sellingPrice, discountAmount;
    private String fullSize ;
    private int quantity;
    private long hsn ;
    private double  sgst , cgst , igst ;

    public GstInvoiceModel(String productName, String fullQuantity, String discountName, double sellingPrice,
                           double discountAmount, String fullSize, int quantity, long hsn, double sgst, double cgst, double igst) {
        this.productName = productName;
        this.fullQuantity = fullQuantity;
        this.discountName = discountName;
        this.sellingPrice = sellingPrice;
        this.discountAmount = discountAmount;
        this.fullSize = fullSize;
        this.quantity = quantity;
        this.hsn = hsn;
        this.sgst = sgst;
        this.cgst = cgst;
        this.igst = igst;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFullQuantity() {
        return fullQuantity;
    }

    public void setFullQuantity(String fullQuantity) {
        this.fullQuantity = fullQuantity;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getFullSize() {
        return fullSize;
    }

    public void setFullSize(String fullSize) {
        this.fullSize = fullSize;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getHsn() {
        return hsn;
    }

    public void setHsn(long hsn) {
        this.hsn = hsn;
    }

    public double getSgst() {
        return sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public double getCgst() {
        return cgst;
    }

    public void setCgst(double cgst) {
        this.cgst = cgst;
    }

    public double getIgst() {
        return igst;
    }

    public void setIgst(double igst) {
        this.igst = igst;
    }
}
