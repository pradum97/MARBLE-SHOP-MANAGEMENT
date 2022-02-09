package com.shop.management.Model;

import java.math.BigDecimal;

public class Stock {

    int stockID , productID;
    double purchasePrice , productMRP , minSellingPrice,
            height,width ;

    String  sizeUnit ,quantityUnit;

    int quantity;

    public Stock(int stockID, int productID, double purchasePrice, double productMRP, double minSellingPrice,
                 double height, double width, String sizeUnit, String quantityUnit, int quantity) {
        this.stockID = stockID;
        this.productID = productID;
        this.purchasePrice = purchasePrice;
        this.productMRP = productMRP;
        this.minSellingPrice = minSellingPrice;
        this.height = height;
        this.width = width;
        this.sizeUnit = sizeUnit;
        this.quantityUnit = quantityUnit;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        BigDecimal h = BigDecimal.valueOf(this.getHeight());
        BigDecimal w = BigDecimal.valueOf(this.getWidth());

        return h.stripTrailingZeros().toPlainString()+" x "
                +w.stripTrailingZeros().toPlainString()+getSizeUnit();
    }

    public int getStockID() {
        return stockID;
    }

    public void setStockID(int stockID) {
        this.stockID = stockID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getProductMRP() {
        return productMRP;
    }

    public void setProductMRP(double productMRP) {
        this.productMRP = productMRP;
    }

    public double getMinSellingPrice() {
        return minSellingPrice;
    }

    public void setMinSellingPrice(double minSellingPrice) {
        this.minSellingPrice = minSellingPrice;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public String getSizeUnit() {
        return sizeUnit;
    }

    public void setSizeUnit(String sizeUnit) {
        this.sizeUnit = sizeUnit;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
