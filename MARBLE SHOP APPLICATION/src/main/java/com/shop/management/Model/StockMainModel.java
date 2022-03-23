package com.shop.management.Model;

public class StockMainModel {
    private  int productId ,  stockId , quantity;
    private String productCode  , productType  , productCategory , productSize , productColor , productFullQuantity ;
    private double purchasePrice , mrp , minSellPrice;


    public StockMainModel(int productId, int stockId, int quantity, String productCode, String productType, String productCategory, String productSize, String productColor,
                          String productFullQuantity, double purchasePrice, double mrp, double minSellPrice) {
        this.productId = productId;
        this.stockId = stockId;
        this.quantity = quantity;
        this.productCode = productCode;
        this.productType = productType;
        this.productCategory = productCategory;
        this.productSize = productSize;
        this.productColor = productColor;
        this.productFullQuantity = productFullQuantity;
        this.purchasePrice = purchasePrice;
        this.mrp = mrp;
        this.minSellPrice = minSellPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getProductFullQuantity() {
        return productFullQuantity;
    }

    public void setProductFullQuantity(String productFullQuantity) {
        this.productFullQuantity = productFullQuantity;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public double getMinSellPrice() {
        return minSellPrice;
    }

    public void setMinSellPrice(double minSellPrice) {
        this.minSellPrice = minSellPrice;
    }
}
