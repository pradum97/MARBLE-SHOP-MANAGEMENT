package com.shop.management.Model;

public class ProductSize {


   private double purchasePrice, productMRP , MinSellPrice ;
   private double width , height;
   private long quantity ;
   private String  sizeUnit , quantityUnit , priceType;
   private int pcsPerPkt;

    public ProductSize(double purchasePrice, double productMRP, double minSellPrice,
                       double width, double height, long quantity, String sizeUnit, String quantityUnit,
                       String priceType, int pcsPerPkt) {
        this.purchasePrice = purchasePrice;
        this.productMRP = productMRP;
        MinSellPrice = minSellPrice;
        this.width = width;
        this.height = height;
        this.quantity = quantity;
        this.sizeUnit = sizeUnit;
        this.quantityUnit = quantityUnit;
        this.priceType = priceType;
        this.pcsPerPkt = pcsPerPkt;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public int getPcsPerPkt() {
        return pcsPerPkt;
    }

    public void setPcsPerPkt(int pcsPerPkt) {
        this.pcsPerPkt = pcsPerPkt;
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

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
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
