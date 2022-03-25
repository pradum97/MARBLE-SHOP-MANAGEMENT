package com.example.test;

import java.math.BigDecimal;

public class CartModel {

    private int cartId, productId,productStockID , discountId, taxId, sellerId;

    private String productName, type, category ;

    private double purchasePrice, productMRP, minSellPrice, sellingPrice, height, width;

    private String sizeUnit, quantityUnit  ,  totalDiscountStr;

    private double totalTaxPer;

    public String fullSize, fullQuantity;

    private int quantity;
    private String productColor , discountName  ;
    private long hsn;
    private double discountAmount , netAmount , gstAmount;
    private double sgst , cgst , igst;
    private String totalTaxStr ;
    private  double discountPercentage;

    public CartModel(int cartId, int productId, int productStockID, int discountId, int taxId, int sellerId, String productName, String type, String category, double purchasePrice, double productMRP,
                     double minSellPrice, double sellingPrice, double height, double width, String sizeUnit, String quantityUnit, String totalDiscountStr, double totalTaxPer,
                     int quantity, String productColor, String discountName, double discountAmount ,
                     long hsn, double netAmount, double gstAmount, double sgst, double cgst, double igst, String totalTaxStr, double discountPercentage) {

        this.cartId = cartId;
        this.productId = productId;
        this.productStockID = productStockID;
        this.discountId = discountId;
        this.taxId = taxId;
        this.sellerId = sellerId;
        this.productName = productName;
        this.type = type;
        this.category = category;
        this.purchasePrice = purchasePrice;
        this.productMRP = productMRP;
        this.minSellPrice = minSellPrice;
        this.sellingPrice = sellingPrice;
        this.height = height;
        this.width = width;
        this.sizeUnit = sizeUnit;
        this.quantityUnit = quantityUnit;
        this.totalDiscountStr = totalDiscountStr;
        this.totalTaxPer = totalTaxPer;
        this.quantity = quantity;
        this.productColor = productColor;
        this.discountName = discountName;
        this.hsn = hsn;
        this.discountAmount = discountAmount;
        this.netAmount = netAmount;
        this.gstAmount = gstAmount;
        this.sgst = sgst;
        this.cgst = cgst;
        this.igst = igst;
        this.totalTaxStr = totalTaxStr;
        this.discountPercentage = discountPercentage;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getTotalDiscountStr() {
        return totalDiscountStr;
    }

    public void setTotalDiscountStr(String totalDiscountStr) {
        this.totalDiscountStr = totalDiscountStr;
    }

    public String getTotalTaxStr() {
        return totalTaxStr;
    }

    public void setTotalTaxStr(String totalTaxStr) {
        this.totalTaxStr = totalTaxStr;
    }

    public double getSgst() {
        return sgst;
    }

    public void setSgst(int sgst) {
        this.sgst = sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public double getCgst() {
        return cgst;
    }

    public void setCgst(double cgst) {
        this.cgst = cgst;
    }

    public void setIgst(double igst) {
        this.igst = igst;
    }

    public double getIgst() {
        return igst;
    }

    public void setIgst(int igst) {
        this.igst = igst;
    }

    public double getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(double gstAmount) {
        this.gstAmount = gstAmount;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public long getHsn() {
        return hsn;
    }

    public void setHsn(long hsn) {
        this.hsn = hsn;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductStockID() {
        return productStockID;
    }

    public void setProductStockID(int productStockID) {
        this.productStockID = productStockID;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public int getTaxId() {
        return taxId;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
        return minSellPrice;
    }

    public void setMinSellPrice(double minSellPrice) {
        this.minSellPrice = minSellPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
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

    public String getFullSize() {

        BigDecimal h = BigDecimal.valueOf(this.getHeight());
        BigDecimal w = BigDecimal.valueOf(this.getWidth());

        return h.stripTrailingZeros().toPlainString()+" x "
                +w.stripTrailingZeros().toPlainString()+" "+getSizeUnit();
    }

    public void setFullSize(String fullSize) {
        this.fullSize = fullSize;
    }

    public String getFullQuantity() {
        return this.getQuantity()+" -"+this.getQuantityUnit();
    }

    public void setFullQuantity(String fullQuantity) {
        this.fullQuantity = fullQuantity;
    }


    public double getTotalTaxPer() {
        return totalTaxPer;
    }

    public void setTotalTaxPer(double totalTaxPer) {
        this.totalTaxPer = totalTaxPer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
