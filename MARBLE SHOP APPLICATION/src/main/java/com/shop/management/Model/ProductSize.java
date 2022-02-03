package com.shop.management.Model;

public class ProductSize {

    double width , height ;
    long quantity ;
    String  sizeUnit , quantityUnit;

    public ProductSize(double width, double height, long quantity, String sizeUnit,
                       String quantityUnit) {

        this.width = width;
        this.height = height;
        this.quantity = quantity;
        this.sizeUnit = sizeUnit;
        this.quantityUnit = quantityUnit;
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
