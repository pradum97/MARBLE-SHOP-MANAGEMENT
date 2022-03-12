package com.shop.management.Model;

public class DailySaleReport {


    private int totalItems;
    private String productCategory;
    private double totalNetAmount, totalPurChaseAmount, profit;

    public DailySaleReport(int totalItems, String productCategory, double totalNetAmount, double totalPurChaseAmount, double profit) {
        this.totalItems = totalItems;
        this.productCategory = productCategory;
        this.totalNetAmount = totalNetAmount;
        this.totalPurChaseAmount = totalPurChaseAmount;
        this.profit = profit;
    }


    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public double getTotalNetAmount() {
        return totalNetAmount;
    }

    public void setTotalNetAmount(double totalNetAmount) {
        this.totalNetAmount = totalNetAmount;
    }

    public double getTotalPurChaseAmount() {
        return totalPurChaseAmount;
    }

    public void setTotalPurChaseAmount(double totalPurChaseAmount) {
        this.totalPurChaseAmount = totalPurChaseAmount;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }
}
