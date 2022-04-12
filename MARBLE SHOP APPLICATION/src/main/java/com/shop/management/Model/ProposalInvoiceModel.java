package com.shop.management.Model;

public class ProposalInvoiceModel {

    private String productName , fullQuantity ;
    private double sellingPrice, discountAmount;
    private String fullSize ;
    private int quantity;
    private String discountName;

    public ProposalInvoiceModel(String productName, String fullQuantity, double sellingPrice,
                                double discountAmount, String fullSize, int quantity, String discountName) {
        this.productName = productName;
        this.fullQuantity = fullQuantity;
        this.sellingPrice = sellingPrice;
        this.discountAmount = discountAmount;
        this.fullSize = fullSize;
        this.quantity = quantity;
        this.discountName = discountName;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
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
}
