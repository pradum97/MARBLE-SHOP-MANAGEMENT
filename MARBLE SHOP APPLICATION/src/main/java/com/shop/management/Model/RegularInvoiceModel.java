package com.shop.management.Model;

public class RegularInvoiceModel {

    private String productName , fullQuantity , discountName;
    private double sellingPrice, discountAmount;
    private String fullSize ;
    private int quantity;

    public RegularInvoiceModel(String productName, String fullQuantity, String discountName, double sellingPrice, double discountAmount, String fullSize, int quantity) {
        this.productName = productName;
        this.fullQuantity = fullQuantity;
        this.discountName = discountName;
        this.sellingPrice = sellingPrice;
        this.discountAmount = discountAmount;
        this.fullSize = fullSize;
        this.quantity = quantity;
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
}
