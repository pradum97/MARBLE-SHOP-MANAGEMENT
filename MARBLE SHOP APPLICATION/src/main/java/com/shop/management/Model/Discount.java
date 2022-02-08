package com.shop.management.Model;

public class Discount {

    int discount_id ;
    int discount;
    String discountType,description;

    public Discount(int discount_id, int discount, String discountType, String description) {
        this.discount_id = discount_id;
        this.discount = discount;
        this.discountType = discountType;
        this.description = description;
    }

    @Override
    public String toString() {

        return this.getDiscount()+" %";
    }

    public int getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(int discount_id) {
        this.discount_id = discount_id;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
