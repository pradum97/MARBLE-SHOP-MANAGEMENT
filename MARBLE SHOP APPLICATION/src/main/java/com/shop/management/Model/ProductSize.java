package com.shop.management.Model;

public class ProductSize {


   private double purchasePrice, productMRP , MinSellPrice ;
   private int width , height;
   private long quantity ;
   private String  sizeUnit , quantityUnit;

    public ProductSize(double purchasePrice, double productMRP, double minSellPrice,
                       int width, int height, long quantity, String sizeUnit, String quantityUnit) {
        this.purchasePrice = purchasePrice;
        this.productMRP = productMRP;
        MinSellPrice = minSellPrice;
        this.width = width;
        this.height = height;
        this.quantity = quantity;
        this.sizeUnit = sizeUnit;
        this.quantityUnit = quantityUnit;
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

    public double getMinSellPrice() {
        return MinSellPrice;
    }

    public void setMinSellPrice(double minSellPrice) {
        MinSellPrice = minSellPrice;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
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
}
