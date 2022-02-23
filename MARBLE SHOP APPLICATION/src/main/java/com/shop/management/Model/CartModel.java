package com.shop.management.Model;

import java.math.BigDecimal;

public class CartModel {

    private int cartId, productId,productStockID , discountId, taxId, sellerId;

    private String productName, type, category ;

    private double purchasePrice, productMRP, minSellPrice, sellingPrice, height, width;

    private String sizeUnit, quantityUnit  ,  totalDiscount;

    private double totalTax;

    public String fullSize, fullQuantity;

    private int quantity;
    private String productColor , discountName  ;
    private long hsn_sac;
    private double discountAmount , netAmount , gstAmount;
    private int sgst , csgt , igst;


    public CartModel(int cartId, int productId, int stockID, int discountId, int taxId, int sellerId,
                     String productName, String product_type, String productCategory, double
                             purchasePrice, double productMRP, double minSellPrice,
                     double sellingPrice, double height, double width, String sizeUnit,
                     String quantityUnit, String totalDis, double totalTax, int quantity,
                     String productColor, String discount_name, double dis, long hsn_sac, double netAmount , double gstAmount ,
                     int sgst , int cgst , int igst) {

        this.cartId = cartId;
        this.productId = productId;
        this.productStockID = stockID;
        this.discountId = discountId;
        this.taxId = taxId;
        this.sellerId = sellerId;
        this.productName = productName;
        this.type = product_type;
        this.category = productCategory;
        this.purchasePrice = purchasePrice;
        this.productMRP = productMRP;
        this.minSellPrice = minSellPrice;
        this.sellingPrice = sellingPrice;
        this.height = height;
        this.width = width;
        this.sizeUnit = sizeUnit;
        this.quantityUnit = quantityUnit;
        this.totalDiscount = totalDis;
        this.totalTax = totalTax;
        this.quantity = quantity;
        this.productColor = productColor;
        this.discountName = discount_name;
        this.discountAmount = dis;
        this.hsn_sac = hsn_sac;
        this.netAmount = netAmount;
        this.gstAmount = gstAmount;
        this.sgst = sgst;
        this.csgt = cgst;
        this.igst = igst;

    }

    public int getSgst() {
        return sgst;
    }

    public void setSgst(int sgst) {
        this.sgst = sgst;
    }

    public int getCsgt() {
        return csgt;
    }

    public void setCsgt(int csgt) {
        this.csgt = csgt;
    }

    public int getIgst() {
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

    public long getHsn_sac() {
        return hsn_sac;
    }

    public void setHsn_sac(long hsn_sac) {
        this.hsn_sac = hsn_sac;
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

    public String getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
