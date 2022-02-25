package com.shop.management.Model;

public class SaleItems {

    private String customerName , customerPhone , customerAddress;
    private int saleId, customerId, productId, stockId, sellerId;
    private String invoiceNumber;
    private String productName, productColor, productSize, productType, productCategory;
    private double purchasePrice, mrp, sellPrice, discountAmount;
    private String discountName, quantity;
    private String billType;
    private int hsnSac, tax;
    private double netAmount;
    private String date;
    private int igst , cgst , sgst;
    private String sellerName;

    public SaleItems(String customerName, String customerPhone, String customerAddress,
                     int saleId, int customerId, int productId, int stockId, int sellerId,
                     String invoiceNumber, String productName, String productColor, String productSize,
                     String productType, String productCategory, double purchasePrice, double mrp,
                     double sellPrice, double discountAmount, String discountName, String quantity,
                     String billType, int hsnSac, int tax, double netAmount, String date, int igst,
                     int cgst, int sgst , String sellerFullName) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.saleId = saleId;
        this.customerId = customerId;
        this.productId = productId;
        this.stockId = stockId;
        this.sellerId = sellerId;
        this.invoiceNumber = invoiceNumber;
        this.productName = productName;
        this.productColor = productColor;
        this.productSize = productSize;
        this.productType = productType;
        this.productCategory = productCategory;
        this.purchasePrice = purchasePrice;
        this.mrp = mrp;
        this.sellPrice = sellPrice;
        this.discountAmount = discountAmount;
        this.discountName = discountName;
        this.quantity = quantity;
        this.billType = billType;
        this.hsnSac = hsnSac;
        this.tax = tax;
        this.netAmount = netAmount;
        this.date = date;
        this.igst = igst;
        this.cgst = cgst;
        this.sgst = sgst;
        this.sellerName = sellerFullName;
    }



    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public int getHsnSac() {
        return hsnSac;
    }

    public void setHsnSac(int hsnSac) {
        this.hsnSac = hsnSac;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
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

    public int getSellsId() {
        return saleId;
    }

    public void setSellsId(int sellsId) {
        this.saleId = sellsId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public int getHsnSan() {
        return hsnSac;
    }

    public void setHsnSan(int hsnSan) {
        this.hsnSac = hsnSan;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
