package com.shop.management.Model;

public class Products extends Stock {

    int productID;
    String productName, productDescription, productColor , productType , category ;
    int discount , tax;
    Product_Images productImages;

    public Products(int stockID, int productID, double purchasePrice, double productMRP, double minSellingPrice, double height, double width, String sizeUnit, String quantityUnit, int quantity, int productID1, String productName, String productDescription, String productColor,

                    String productType, String category, int discount, int tax, Product_Images productImages) {
        super(stockID, productID, purchasePrice, productMRP, minSellingPrice, height, width, sizeUnit, quantityUnit, quantity);
        this.productID = productID1;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productColor = productColor;
        this.productType = productType;
        this.category = category;
        this.discount = discount;
        this.tax = tax;
        this.productImages = productImages;
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

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public Product_Images getProductImages() {
        return productImages;
    }

    public void setProductImages(Product_Images productImages) {
        this.productImages = productImages;
    }
}
