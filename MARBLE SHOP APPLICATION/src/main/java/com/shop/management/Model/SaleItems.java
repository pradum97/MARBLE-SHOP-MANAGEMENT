package com.shop.management.Model;

public class SaleItems {

    private int sale_item_Id, productId, stockId;
    private String productName, productColor, productSize, productType, productCategory;
    private double purchasePrice, mrp, sellPrice , discountAmount,taxAmount, netAmount;
    private String discountName, quantity;
    private int hsnSac, tax ,  igst , cgst , sgst;
    private String sellingDate , discountPer;

    public SaleItems(int sale_item_Id, int productId, int stockId, String productName,
                     String productColor, String productSize, String productType,
                     String productCategory, double purchasePrice, double mrp,
                     double sellPrice, double discountAmount, double taxAmount, double netAmount,
                     String discountName, String quantity, int hsnSac, int tax, int igst, int cgst, int sgst , String sellingDate, String discountPer ) {
        this.sale_item_Id = sale_item_Id;
        this.productId = productId;
        this.stockId = stockId;
        this.productName = productName;
        this.productColor = productColor;
        this.productSize = productSize;
        this.productType = productType;
        this.productCategory = productCategory;
        this.purchasePrice = purchasePrice;
        this.mrp = mrp;
        this.sellPrice = sellPrice;
        this.discountAmount = discountAmount;
        this.taxAmount = taxAmount;
        this.netAmount = netAmount;
        this.discountName = discountName;
        this.quantity = quantity;
        this.hsnSac = hsnSac;
        this.tax = tax;
        this.igst = igst;
        this.cgst = cgst;
        this.sgst = sgst;
        this.sellingDate = sellingDate;
        this.discountPer = discountPer;
    }

    public String getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(String discountPer) {
        this.discountPer = discountPer;
    }

    public String getSellingDate() {
        return sellingDate;
    }

    public void setSellingDate(String sellingDate) {
        this.sellingDate = sellingDate;
    }

    public int getSale_item_Id() {
        return sale_item_Id;
    }

    public void setSale_item_Id(int sale_item_Id) {
        this.sale_item_Id = sale_item_Id;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
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

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getHsnSac() {
        return hsnSac;
    }

    public void setHsnSac(int hsnSac) {
        this.hsnSac = hsnSac;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public int getIgst() {
        return igst;
    }

    public void setIgst(int igst) {
        this.igst = igst;
    }

    public int getCgst() {
        return cgst;
    }

    public void setCgst(int cgst) {
        this.cgst = cgst;
    }

    public int getSgst() {
        return sgst;
    }

    public void setSgst(int sgst) {
        this.sgst = sgst;
    }
}
