package com.shop.management.Model;

public class ProposalItemModel {

    private int proposalItemId;
    private String productName , productType , productCategory , productSize , quantity , rate ;
    private double discountAmount;

    public ProposalItemModel(int proposalItemId, String productName, String productType,
                             String productCategory, String productSize, String quantity, String rate, double discountAmount) {
        this.proposalItemId = proposalItemId;
        this.productName = productName;
        this.productType = productType;
        this.productCategory = productCategory;
        this.productSize = productSize;
        this.quantity = quantity;
        this.rate = rate;
        this.discountAmount = discountAmount;
    }

    public int getProposalItemId() {
        return proposalItemId;
    }

    public void setProposalItemId(int proposalItemId) {
        this.proposalItemId = proposalItemId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }
}
