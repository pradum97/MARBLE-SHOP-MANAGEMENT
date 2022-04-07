package com.shop.management.Model;

public class PurchaseHistoryModel {

    private int purchaseId , supplierId , productId , StockId , sellerId;
    private String supplierName , invoiceNum , productCode , size , quantity , activity , date ;
    private double purchasePrice , mrp , minSell;

    public PurchaseHistoryModel(int purchaseId, int supplierId, int productId, int stockId, int sellerId, String supplierName, String invoiceNum, String productCode,
                                String size, String quantity, String activity, String date, double purchasePrice, double mrp, double minSell) {
        this.purchaseId = purchaseId;
        this.supplierId = supplierId;
        this.productId = productId;
        StockId = stockId;
        this.sellerId = sellerId;
        this.supplierName = supplierName;
        this.invoiceNum = invoiceNum;
        this.productCode = productCode;
        this.size = size;
        this.quantity = quantity;
        this.activity = activity;
        this.date = date;
        this.purchasePrice = purchasePrice;
        this.mrp = mrp;
        this.minSell = minSell;
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

    public double getMinSell() {
        return minSell;
    }

    public void setMinSell(double minSell) {
        this.minSell = minSell;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getStockId() {
        return StockId;
    }

    public void setStockId(int stockId) {
        StockId = stockId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
