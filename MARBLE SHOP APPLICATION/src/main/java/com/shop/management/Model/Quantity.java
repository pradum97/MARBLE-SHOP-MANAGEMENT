package com.shop.management.Model;

public class Quantity {

   private long quantity ;
   private String quantityUnit ;
   private double sellingPrice;
   private String priceType;

    public Quantity(long quantity, String quantityUnit, double sellingPrice , String priceType) {
        this.quantity = quantity;
        this.quantityUnit = quantityUnit;
        this.sellingPrice = sellingPrice;
        this.priceType = priceType;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
