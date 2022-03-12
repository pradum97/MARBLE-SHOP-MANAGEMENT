package com.shop.management.Model;

public class ReturnProductModel {

    private boolean isReturn;
    private int productID , stockId , saleMainId , saleItemId;
    private String productName, productSize, quantity;
    private double rate, discountPercentage ;
    private String returnQuantity ;

    public ReturnProductModel(boolean isReturn, int productID, int stockId, int saleMainId, int saleItemId,  String productName,
                              String productSize, String quantity, double rate, double discountPercentage, String returnQuantity) {
        this.isReturn = isReturn;
        this.productID = productID;
        this.stockId = stockId;
        this.saleMainId = saleMainId;
        this.saleItemId = saleItemId;
        this.productName = productName;
        this.productSize = productSize;
        this.quantity = quantity;
        this.rate = rate;
        this.discountPercentage = discountPercentage;
        this.returnQuantity = returnQuantity;
    }

    public int getSaleItemId() {
        return saleItemId;
    }

    public void setSaleItemId(int saleItemId) {
        this.saleItemId = saleItemId;
    }

    public int getSaleMainId() {
        return saleMainId;
    }

    public void setSaleMainId(int saleMainId) {
        this.saleMainId = saleMainId;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }


    public boolean isReturn() {
        return isReturn;
    }

    public void setReturn(boolean aReturn) {
        isReturn = aReturn;
    }

    public String getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(String returnQuantity) {
        this.returnQuantity = returnQuantity;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
