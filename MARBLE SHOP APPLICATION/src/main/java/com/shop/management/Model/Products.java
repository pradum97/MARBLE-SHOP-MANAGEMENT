package com.shop.management.Model;

public class Products extends Stock {

   private int  productID;
   private String productName, productDescription, productColor , productType , category ;
   private int discountId , taxId;
   private Product_Images productImages;
   private String added_date , totalDiscount , totalTax ;
   private int hsn_sac;

    public Products(int stockID, int productID, double purchasePrice, double productMRP, double minSellingPrice, double height, double width, String sizeUnit, String quantityUnit, int quantity, int productID1, String productName,
                    String productDescription, String productColor, String productType, String category, int discountId, int taxId, Product_Images productImages, String added_date, String totalDiscount, String totalTax, int hsn_sac) {
        super(stockID, productID, purchasePrice, productMRP, minSellingPrice, height, width, sizeUnit, quantityUnit, quantity);
        this.productID = productID1;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productColor = productColor;
        this.productType = productType;
        this.category = category;
        this.discountId = discountId;
        this.taxId = taxId;
        this.productImages = productImages;
        this.added_date = added_date;
        this.totalDiscount = totalDiscount;
        this.totalTax = totalTax;
        this.hsn_sac = hsn_sac;
    }

    public int getHsn_sac() {
        return hsn_sac;
    }

    public void setHsn_sac(int hsn_sac) {
        this.hsn_sac = hsn_sac;
    }

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    @Override
    public int getProductID() {
        return productID;
    }

    @Override
    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(String totalTax) {
        this.totalTax = totalTax;
    }

    public Product_Images getProductImages() {
        return productImages;
    }

    public void setProductImages(Product_Images productImages) {
        this.productImages = productImages;
    }
}
