package com.shop.management.Model;

public class Return_ItemsModel {
    private int returnItemId;
    private String productName, productSize, returnQuantity;
    private double rate;

    public Return_ItemsModel(int returnItemId, String productName, String productSize, String returnQuantity, double rate) {
        this.returnItemId = returnItemId;
        this.productName = productName;
        this.productSize = productSize;
        this.returnQuantity = returnQuantity;
        this.rate = rate;
    }

    public int getReturnItemId() {
        return returnItemId;
    }

    public void setReturnItemId(int returnItemId) {
        this.returnItemId = returnItemId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(String returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
